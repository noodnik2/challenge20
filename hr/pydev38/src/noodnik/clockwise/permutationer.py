

def missing(miss,src):
    "Returns the list of items in src not present in miss"
    return [i for i in src if i not in miss]


def permutation_gen(n,l):
    "Generates all the permutations of n items of the l list"
    for i in l:
        if n<=1: yield [i]
        r = [i]
        for j in permutation_gen(n-1,missing([i],l)):  yield r+j

