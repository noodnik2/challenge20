#!/usr/bin/env python

import cgi
from wslib import shell, ldrpath

# subs

def load_file(file):
    sql_cmd = "sqlplus -S / as sysdba @%s/load_projx_table.sql '%s' 'projx_table' < /dev/null" % (ldrpath("scripts"), file)
    sql_log = shell(sql_cmd)
    mv_cmd = "mv %s/%s %s" % (ldrpath("inbox"), file, ldrpath("outbox"))
    mv_log = shell(mv_cmd)
    return "$ %s\n%s\n$ %s\n%s" % (sql_cmd, sql_log, mv_cmd, mv_log)

# begin

print("Content-Type: text/plain")
print("")

for file in cgi.FieldStorage().getlist("file"):
    print(load_file(file))

