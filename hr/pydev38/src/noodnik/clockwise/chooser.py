
# >>> len(list(choose_iter("abcdefgh",3)))
# 56

def choose_iter(elements, length):
    for i in range(len(elements)):
        if length == 1:
            yield (elements[i],)
        else:
            for next in choose_iter(elements[i+1:len(elements)], length-1):
                yield (elements[i],) + next


def choose(l, k):
    return list(choose_iter(l, k))
