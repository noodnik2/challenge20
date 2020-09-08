import itertools

def split_list(list):
    uniques = []
    dups = [x for x in list if x & 1]
    singles = [x for x in list if not x & 1]
    return dups, singles


def doit(list):
    if len(list) < 2:
        return
    dups, singles = split_list(list)
    dup = dups.pop()
    print(f"({dup}),({dups}),({singles})")
    if dups:
        doit(dups)


# doit([x for x in range(100)])


def p(x):
    print(f"x({x})")


def x(s, list):
    if not list:
        p('-')
        return
    p(s)
    for y in list:
        x(y)
        x([u for u in list if u != y])


# x([1,2,3])


def combine(inp):
    return combine_helper(inp, [], [])


def combine_helper(inp, temp, ans):
    for i in range(len(inp)):
        current = inp[i]
        remaining = inp[i + 1:]
        temp.append(current)
        ans.append(tuple(temp))
        combine_helper(remaining, temp, ans)
        temp.pop()
    return ans


print(combine(['a', 'b', 'c', 'd']))


# have "1234"
# want: [1234, 1243, 1342, 1324, 1423, 1432, 2341, 2431, 2324, 2123