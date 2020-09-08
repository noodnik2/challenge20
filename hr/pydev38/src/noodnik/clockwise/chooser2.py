
def comb(sofar, rest, n):
    if n == 0:
        print(sofar)
    else:
        for i in range(len(rest)):
            comb(sofar + rest[i], rest[i+1:], n-1)

# comb("", "abcde", 3)
