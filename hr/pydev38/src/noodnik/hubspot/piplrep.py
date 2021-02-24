import json
import pyjq

# demonstrates simple use of pyjq using data from PIPL

def doit(json_string):
    output_json = pyjq.all(
        '.[] | {degree:.person.education.certificate,email:.person.online_info.email,spouse:.person.marriage.spouse_name}',
        json.loads(json_string)
    )
    print(json.dumps(output_json, indent=2))

# the following JSON string was generated from repeated queries to the URL: https://pipl.ir/v1/getPerson
# e.g.:
#   bash:   touch /tmp/x; for x in 1..4; do curl --silent https://pipl.ir/v1/getPerson >> /tmp/x; done
#   python: print(f"{json.dumps(json.load(open('/tmp/x')), indent=2)}")

doit("""
[
  {
    "person": {
      "education": {
        "certificate": "PhD",
        "university": "Stanford University"
      },
      "marriage": {
        "children": 3,
        "married": true,
        "spouse_name": "Myrtie"
      },
      "online_info": {
        "email": "guunyx@riseup.net",
        "ip_address": "80.62.23.64",
        "ipv6_address": "ea18:046c:2817:af26:7155:485c:290c:9748",
        "password": "GKmc5RPQOt",
        "password_md5": "d0466df87e4696bb10bc1c37b5f703d1",
        "user_agent": "Mozilla/5.0 (Mobile; Nokia_8110_4G; rv:48.0) Gecko/48.0 Firefox/48.0 KAIOS/2.5.1",
        "username": "5ln829"
      },
      "personal": {
        "age": 39,
        "blood": "AB+",
        "born": null,
        "born_place": "Ararat",
        "cellphone": "+3747391256",
        "city": "Shiraz",
        "country": "Sweden",
        "eye_color": "Gray",
        "father_name": "Olva",
        "gender": "Male",
        "height": "1.67",
        "last_name": "Kaplan",
        "name": "Mufinella",
        "national_code": "5487713448",
        "religion": "Hindu",
        "system_id": "83fe47d9-5faa-4d52-a370-87e8098d1938",
        "weight": 41
      },
      "work": {
        "country_code": "AL",
        "insurance": true,
        "position": "Barber",
        "salary": "$6.100"
      }
    }
  },
  {
    "person": {
      "education": {
        "certificate": "Master",
        "university": "Sharif University"
      },
      "marriage": {
        "children": 0,
        "married": true,
        "spouse_name": "Leona"
      },
      "online_info": {
        "email": "uh4kcd@yahoo.com",
        "ip_address": "218.207.100.170",
        "ipv6_address": "c4fe:80b9:62ef:3df3:d80d:727c:b638:09a1",
        "password": "zEl5sO7LKp",
        "password_md5": "0050be28aafe7defc1c41f7b67d68a57",
        "user_agent": "Mozilla/5.0 (Linux; Android 9; KSA-LX9 Build/HONORKSA-LX9) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 YaBrowser/18.10.2.188.00 Mobile Safari/537.36",
        "username": "m9ei7b"
      },
      "personal": {
        "age": 51,
        "blood": "B-",
        "born": null,
        "born_place": "Tehran",
        "cellphone": "+18867499",
        "city": "Ararat",
        "country": "USA",
        "eye_color": "Gray",
        "father_name": "Brandi",
        "gender": "Female",
        "height": "1.83",
        "last_name": "Snow",
        "name": "Modestia",
        "national_code": "5881378857",
        "religion": "Muslim",
        "system_id": "29b024f1-0af4-43a0-8205-86c1a9b130ce",
        "weight": 76
      },
      "work": {
        "country_code": "TD",
        "insurance": false,
        "position": "Developer",
        "salary": "$4.000"
      }
    }
  },
  {
    "person": {
      "education": {
        "certificate": "Diploma",
        "university": "Stanford University"
      },
      "marriage": {
        "married": false
      },
      "online_info": {
        "email": "cd9yrg@yahoo.com",
        "ip_address": "33.93.203.171",
        "ipv6_address": "9abb:6f65:49a1:402c:820f:4bb2:4aa9:6fd2",
        "password": "1muCV91AoM",
        "password_md5": "1cc9093987119c389fc96b9e4f75266a",
        "user_agent": "Mozilla/5.0 (Linux; Android 7.0; SM-T585) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.90 Safari/537.36",
        "username": "2k1y6q"
      },
      "personal": {
        "age": 37,
        "blood": "AB-",
        "born": null,
        "born_place": "Hong Kong",
        "cellphone": "+3747300201",
        "city": "Denver",
        "country": "Canada",
        "eye_color": "Hazel",
        "father_name": "Phyllys",
        "gender": "Female",
        "height": "2.00",
        "last_name": "Shapiro",
        "name": "Erna",
        "national_code": "1932165096",
        "religion": "Muslim",
        "system_id": "a258f2d7-360d-4642-8292-f8e8f962e3da",
        "weight": 83
      },
      "work": {
        "country_code": "IR",
        "insurance": false,
        "position": "Graphist",
        "salary": "$6.400"
      }
    }
  },
  {
    "person": {
      "education": {
        "certificate": "Master",
        "university": "Boston University"
      },
      "marriage": {
        "married": false
      },
      "online_info": {
        "email": "jfu5vj@yahoo.com",
        "ip_address": "184.135.202.253",
        "ipv6_address": "7dd4:710a:16b4:0ab5:03dc:3a9a:65be:8005",
        "password": "ogbI9PfIWf",
        "password_md5": "f7c6591ee46ad6449e518abeb607f384",
        "user_agent": "Mozilla/5.0 (Linux; Android 9; SM-A750FN) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.111 Mobile Safari/537.36",
        "username": "hj6xqa"
      },
      "personal": {
        "age": 37,
        "blood": "O+",
        "born": null,
        "born_place": "San Diego",
        "cellphone": "+3741446357",
        "city": "Denver",
        "country": "France",
        "eye_color": "Blue",
        "father_name": "Idalina",
        "gender": "Female",
        "height": "2.08",
        "last_name": "Baker",
        "name": "Jeannie",
        "national_code": "5428914130",
        "religion": "Christian",
        "system_id": "ea77b7ff-ff91-4e3b-9004-129f2f5da490",
        "weight": 147
      },
      "work": {
        "country_code": "TD",
        "insurance": true,
        "position": "Developer",
        "salary": "$12.200"
      }
    }
  },
  {
    "person": {
      "education": {
        "certificate": "PhD",
        "university": "Harvard University"
      },
      "marriage": {
        "children": 0,
        "married": true,
        "spouse_name": "Bessie"
      },
      "online_info": {
        "email": "1l6n7h@live.com",
        "ip_address": "183.183.105.81",
        "ipv6_address": "021a:cd2a:1839:d68a:567c:b071:0db6:6ec4",
        "password": "bh8MSCkBj8",
        "password_md5": "21c06f22445e7f2be5913c199aa65fa1",
        "user_agent": "Mozilla/5.0 (Linux; Android 4.4.2; Lenovo A536) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.111 Mobile Safari/537.36",
        "username": "p6v9de"
      },
      "personal": {
        "age": 22,
        "blood": "A-",
        "born": null,
        "born_place": "San Diego",
        "cellphone": "+3743499529",
        "city": "Venice",
        "country": "Belarus",
        "eye_color": "Hazel",
        "father_name": "Cloe",
        "gender": "Female",
        "height": "1.80",
        "last_name": "Preston",
        "name": "Danella",
        "national_code": "8428051046",
        "religion": "Muslim",
        "system_id": "9449725b-1219-4f1e-9875-fdb0228ce2f3",
        "weight": 34
      },
      "work": {
        "country_code": "DZ",
        "insurance": false,
        "position": "Engineer",
        "salary": "$1.700"
      }
    }
  }
]
""")

