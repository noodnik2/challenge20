import sys

# This is a simple solution that does completely in-memory aggregation. Take
# a look at this to understand the exact computation we are looking for.

class AggResult:
  def __init__(self, max):
    self.max = max
    self.count = 1

if len(sys.argv) != 2:
  print("Usage: python SimpleSolution <input-file>", "r")
  sys.exit()

# For each key we've seen, update the current aggregated result. Note that
# this data structure can grow proportional to the input data and
# use arbitrarily large amounts of memory.
result = {}

# Loop through the input file line by line.
f = open(sys.argv[1])

for line in f.readlines():
  inp = line.split()
  if len(inp) < 1:
    continue
  elif len(inp) != 2:
    raise Exception("Invalid line data: " + line)

# Either insert the new key/value or update an existing result.
  key, val = inp[0], inp[1]
  if key in result:
    result[key].count += 1
    result[key].max = max(inp[1], result[key].max)
  else:
    result[key] = AggResult(val)

f.close()

# All done with aggregation, output the result.
for key,val in result.items():
    print(key + ": " + str(val.count) + ", " + val.max)