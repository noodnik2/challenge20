#!/usr/bin/env python

import cgi
from os import listdir
from os.path import isfile, join
from wslib import ldrpath
import json

print("Content-Type: application/json")
print("")

dir = cgi.FieldStorage().getfirst("dir", "inbox")
inbox_path = ldrpath(dir)
print(json.dumps([file for file in listdir(inbox_path) if isfile(join(inbox_path, file))]))
