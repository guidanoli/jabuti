# Add words to dictionaries in every language
# Guilherme Dantas

import os
import re
import operator

EXT = ".xml"
META = "_meta_"+EXT
SCRIPT_FULLPATH = os.path.realpath(__file__)
SCRIPT_FOLDER, SCRIPT_NAME = os.path.split(SCRIPT_FULLPATH)
os.chdir(SCRIPT_FOLDER)                   # in case of running elsewhere
files = os.listdir(SCRIPT_FOLDER)         # list language xmls
if META in files:
  files.remove(META)                      # but _meta_, if exists
if SCRIPT_NAME in files:
  files.remove(SCRIPT_NAME)               # but itself, if exists

""" check if s is valid in xml """


def invalid_xml(s):
  invalid_chars = ["&", "<", ">", '"', "'"]
  return True in [(c in s) for c in invalid_chars]


""" check if key exists in file """


def found_in_file(key, file):
  with open(file, 'r', encoding='utf-8') as f:
    content = f.read()
    return content.find("key=\"{}\"".format(key)) != -1


""" add new_key inputted by stdin in every file that doesn't have it """


def new_key(files_list,saved_key):
  if len(saved_key) > 0 :
    is_using_saved_keyname = "y" in input("Would you like to use key '"+saved_key+"' (y/n)? ").lower()
  else:
    is_using_saved_keyname = False
  if not is_using_saved_keyname:
    new_key = input("New key (or 'end'): ")
  else:
    new_key = saved_key
  if new_key.strip().upper() == "END":
    return False, False, False
  if len(new_key) == 0:
    return False, "Too short", False
  if invalid_xml(new_key):
    return False, "Invalid character found", False
  found_in = {}
  for file in files_list:
    found_in[file] = found_in_file(new_key, file)
  if True in found_in.values():
    if False in found_in.values():
      lacking = []
      for k, v in found_in.items():
        if v == False:
          lacking.append(k)
      print(new_key+" is missing in files "+", ".join(lacking))
      print(new_key+" will apply only to the missing files")
      set_new_key(new_key, lacking)
      return True, "", is_using_saved_keyname
    else:
      return False, new_key+" already exists in all files", is_using_saved_keyname
  else:
    set_new_key(new_key, files_list)
    return True, "", is_using_saved_keyname


""" returns new content with new key and value """


def add_key_on_content(content, key, value):
  pat = re.compile("(.*)(</properties>)(.*)", re.DOTALL)
  found = pat.findall(content)
  if len(found) == 0:
    print("Could not operate")
    return content
  found = found[0]
  return found[0]+"<entry key=\"{}\">{}</entry>\n".format(key, value)+found[1]+found[2]


""" removes extension from filename """


def langname_from_filename(filename):
  return os.path.splitext(filename)[0]


""" get key value from file """


def get_key_value(filename, key):
  langname = langname_from_filename(filename)
  value = input("[{}] {}=".format(langname, key))
  if value.strip() == '&':
    return value
  while len(value) == 0 or invalid_xml(value):
    print("[!] Value invalid")
    value = input("[{}] {}=".format(langname, key))
  return value


""" adds key to all files in the list """


def set_new_key(key, files_list):
  previous_value = None
  print("'&' to use previous value inserted")
  for file in files_list:
    with open(file, 'r', encoding='utf-8') as f:
      content = f.read()
      value = get_key_value(file, key)
      while True:
        if previous_value != None or value != '&':
          break
        value = get_key_value(file, key) # first value can't be &
      if value == '&':
        value = previous_value
      previous_value = value
      content = add_key_on_content(content, key, value)
    with open(file, 'w', encoding='utf-8') as f:
      f.write(content)


""" Returns header STRING """
""" note that it will not end with newline """


def get_header(content):
  pat = re.compile("(.*<properties>)", re.DOTALL)
  found = pat.findall(content)
  if len(found) != 1:
    return False, "Wrong XML header format"
  return found[0]


""" Returns tuple consisting of matches in the following form """
""" (entire_line,key) """
""" note that entire_line does not end with newline """


def get_entries(content):
  pat = re.compile("(<entry key=\"(.*)\">.*</entry>)")
  found = pat.findall(content)
  return found


""" Return footer STRING """
""" note that it will not end with newline """


def get_footer(content):
  return "</properties>"


""" formats a new content from three parts: """
""" header  - from get_header (dangerous to be manipulated) """
""" entries - from get_entries (can be rearranged) """
""" footer  - from get_footer (dangerous to be manipulated) """


def format_xml(header, entries, footer):
  entries_s = "\n".join([e[0] for e in entries])
  return "{}\n{}\n{}".format(header, entries_s, footer)


""" sort entries according to keys (alphabetical order) """


def sort_keys(files_list):
  for file in files_list:
    with open(file, 'r', encoding='utf-8') as f:
      content = f.read()
      header = get_header(content)
      entries = get_entries(content)
      footer = get_footer(content)
      entries.sort(key=operator.itemgetter(1))  # sort
      content = format_xml(header, entries, footer)
    with open(file, 'w', encoding='utf-8') as f:
      f.write(content)  

def add_key(files_list,saved_key):
  success, error, used_key = new_key(files_list, saved_key)
  while success:
    if used_key:
      saved_key = ""
    sort_keys(files_list)
    success, error, used_key = new_key(files_list,saved_key)
  if error:
    print(error)
  return used_key

""" get keys from language file as an array of strings """

def get_keys_from_langfile(file):
  with open(file, 'r', encoding='utf-8') as f:
    content = f.read()
  entries = get_entries(content)
  keys = []
  for e in entries:
    keys.append(e[1])
  return keys

""" display key tree on screen """
""" returns key navigated by the explorer """

def show_key_tree(files_list):
  keys = []
  for file in files_list:
    new_keys = get_keys_from_langfile(file)
    for k in new_keys:
      if not k in keys:
        keys.append(k)
  tree = dict()
  for k in keys:
    parts = k.split("_")
    node = tree
    for p in parts:
      if not p in node:
        node[p] = dict()
      node = node[p]
  depth = int(input("depth (-1 = show all tree): "))
  print_tree_root(tree, depth)
  terminator = "$"
  node_msg = "\nnode: "
  leaf_node_msg = "\nnode ("+terminator+" to end): "
  new_node = input(node_msg).lower()
  while new_node == "":
    new_node = input(node_msg).lower()
  key_parts = [ new_node ]
  while new_node in tree:
    tree = tree[new_node]
    print("==================================")
    print_tree_root(tree, depth)
    new_node = input(node_msg).lower()
    while new_node == "":
      new_node = input(node_msg).lower()
    if new_node != terminator:
      key_parts.append(new_node)
  while new_node != terminator:
    new_node = input(leaf_node_msg).lower()
    while new_node == "":
      new_node = input(leaf_node_msg).lower()
    if new_node != terminator:
      key_parts.append(new_node)
  key = "_".join(key_parts)
  print("key: "+key)
  return key

""" prints tree from root with defined recursion depth """

def print_tree_root(tree,depth):
  print_tree(tree, 0, "",depth)

""" prints tree to screen, identing according to node height """
""" tree - dictionary tree """
""" height - current height """
""" ident_chars - characters at the left of node """
""" depth - recursion depth """

def print_tree(tree,height,ident_chars,depth):
  if depth == 0:
    return
  count = len(tree)
  i = 0
  last_node = "\u2514"
  middle_node = "\u251c"
  parent_node = "\u2502"
  if count > 0:
    print(ident_chars+parent_node)
  for node_name, node_dict in tree.items():
    if i == count - 1:
      symbol = last_node
      parent_symbol = " "
    else:
      symbol = middle_node
      parent_symbol = parent_node
    if len(node_dict) > 0:
      node_name = "[" + node_name + "]"
    print(ident_chars+symbol+node_name)
    print_tree(node_dict,height+1,ident_chars+parent_symbol,depth-1)
    i += 1

def show_diff(files_list):
  print("wip")

cmd_msg = "(a)dd/(t)ree/(d)iff/(e)nd: "
cmd = input(cmd_msg).lower()
saved_key = ""
while not 'e' in cmd:
  if 'a' in cmd:
    used_key = add_key(files, saved_key)
    if used_key:
      saved_key = ""
  elif 't' in cmd:
    saved_key = show_key_tree(files)
  elif 'd' in cmd:
    show_diff(files)
  else:
    print(cmd+" not recognized")
  cmd = input('\n'+cmd_msg).lower()
