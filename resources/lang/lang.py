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


def new_key(files_list):
  new_key = input("New key (or 'end'): ")
  if new_key.strip().upper() == "END":
    return False, False
  if len(new_key) == 0:
    return False, "Too short"
  if invalid_xml(new_key):
    return False, "Invalid character found"
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
      return set_new_key(new_key, lacking)
    else:
      return False, new_key+" already exists in all files"
  else:
    return set_new_key(new_key, files_list)


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
  return True, ""


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

success, error = new_key(files)
while(success):
  success, error = new_key(files)
if error:
  print(error)
sort_keys(files)
