# Add words to dictionaries in every language
# Guilherme Dantas

import os

EXT = ".xml"
LANGFOLDER_FOLDERS = ["ressources","lang"]
LANGFOLDER = ""
for folder in LANGFOLDER_FOLDERS:
  LANGFOLDER = os.path.join(LANGFOLDER,folder)
META = "_meta_"+EXT
SCRIPT_PATH = os.path.dirname(os.path.realpath(__file__))
LANGFOLDER_PATH = os.path.join(SCRIPT_PATH,LANGFOLDER)
os.chdir(LANGFOLDER_PATH)

def invalid_xml(s):
  invalid_chars = ["&","<",">",'"',"'"]
  return True in [(c in s) for c in invalid_chars]

def found_in_file(key,file):
  with open(file,'r',encoding='utf-8') as f:
    content = f.read()
    return content.find("key=\"{}\"".format(key)) != -1

def new_key(files_list):
  new_key = input("New key: ")
  if len(new_key) == 0:
    return False, "Too short"
  if invalid_xml(new_key):
    return False, "Invalid character found"
  found_in = {}
  for file in files_list:
    found_in[file] = found_in_file(new_key,file)
  if True in found_in.values():
    if False in found_in.values():
      lacking = []
      for k,v in found_in.items():
        if v == False:
          lacking.append(k)
      print(new_key+" is missing in files "+", ".join(lacking))
      print(new_key+" will apply only to the missing files")
      return set_new_key(new_key, lacking)
    else:
      return False, new_key+" already exists in all files"
  else:
    return set_new_key(new_key, files_list)

def add_key_on_content(content,key,value):
  import re
  pat = re.compile("(.*)(</properties>)(.*)", re.DOTALL)
  found = pat.findall(content)
  if len(found) == 0:
    print("Could not operate")
    return content
  found = found[0]
  return found[0]+"<entry key=\"{}\">{}</entry>\n".format(key,value)+found[1]+found[2]

def langname_from_filename(filename):
  return os.path.splitext(filename)[0]

def get_key_value(filename,key):
  langname = langname_from_filename(filename)
  value = input("[{}] {}=".format(langname,key))
  while len(value) == 0 or invalid_xml(value):
    print("[!] Value invalid")
    value = input("[{}] {}=".format(langname, key))
  return value  

def set_new_key(key, files_list):
  for file in files_list:
    with open(file, 'r', encoding='utf-8') as f:
      old_content = f.read()
      value = get_key_value(file,key)
      new_content = add_key_on_content(old_content,key,value)
    with open(file, 'w', encoding='utf-8') as f:
      f.write(new_content)
  return True, ""

files = os.listdir(LANGFOLDER_PATH) # language xmls
files.remove(META)                  # but _meta_
success, error = new_key(files)
while(success):
  success, error = new_key(files)
print(error)
