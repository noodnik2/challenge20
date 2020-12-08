
from subprocess import check_output, STDOUT, CalledProcessError
from string import rstrip

def ldrpath(selector):
    path_dict = {
        "scripts": "/home/oracle/scripts",
        "inbox": "/home/oracle/inbox",
        "outbox": "/home/oracle/outbox"
    }
    return path_dict.get(selector, None)

def shell(cmd):
    try:
        return rstrip(check_output(cmd, shell=True, stderr=STDOUT))
    except CalledProcessError as e:
        return "RC(%s); cmd(%s) => %s" % (e.returncode, e.cmd, rstrip(e.output))
