import json
import pyjq

json_input = json.loads("""{
    "name": "Bob Odenkirk",
    "title": "Software Engineer",
    "location": {
        "locality": "San Francisco",
        "region": "CA",
        "country": "United States"
    },
    "age": 62,
    "status": "Active"
}""")

r = pyjq.one(
    '{names: [.name], occupations: [.title], locations: [.location.locality + ", " + .location.region]}',
    json_input,
)

print(json.dumps(r, indent=4))