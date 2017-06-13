import yaml
import json
import sys

INPUT_NAME = sys.argv[1]

if INPUT_NAME[-5:] != ".yaml":
    print("FIle is not yaml")
    sys.exit(1)

OUTPUT_NAME = INPUT_NAME[:-5] + ".json"

print("Outputing to %s" % OUTPUT_NAME)

with open(INPUT_NAME) as input:
    with open(OUTPUT_NAME, 'w') as output:
	    o = yaml.load(input)
	    json.dump(o, output, indent=4, sort_keys=True)
