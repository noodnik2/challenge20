
import requests
import json
import pyjq

gitjobs_raw = requests.get("https://jobs.github.com/positions.json?full_time=true")

gitjobs_transformed = pyjq.all(
    '.[] | select(.location=="Remote") | {company:.company, url:.url, title:.title}',
    gitjobs_raw.json(),
)

print(json.dumps(gitjobs_transformed, indent=4))