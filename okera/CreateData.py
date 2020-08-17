from random import randint
f = open("large-data.txt", "w")
for key in range(100):
    for value in range(randint(1, 100)):
        f.write(f"key{key} value{value}\n")