from random import randint

# A one-line expression that generates an infinite
# sequence of even numbers between 1 and 100 (inclusive)
generator = (2*(1+randint(0, 49)) for r in iter(int, 1))

# test
for n in generator:
    print(n)
