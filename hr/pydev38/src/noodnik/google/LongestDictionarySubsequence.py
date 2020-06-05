'''
Given a string S and a set of words D, find the longest word in D that is a subsequence of S.

Word W is a subsequence of S if some number of characters, possibly zero, can be deleted from S
to form W, without reordering the remaining characters.

Note: D can appear in any format (list, hash table, prefix tree, etc.

For example, given the input of S = "abppplee" and D = {"able", "ale", "apple", "bale", "kangaroo"}
the correct output would be "apple"

The words "able" and "ale" are both subsequences of S, but they are shorter than "apple".
The word "bale" is not a subsequence of S because even though S has all the right letters, they are not in the right order.
The word "kangaroo" is the longest word in D, but it isn't a subsequence of S.
'''

#
# see discussions:
# - https://www.careercup.com/question?id=5757216146587648
# - https://techdevguide.withgoogle.com/resources/find-longest-word-in-dictionary-that-subsequence-of-given-string
#

def isSubsequence(s, w):
    sPos = -1
    for i in range(1, len(w)):
        sFound = s.find(w[i], sPos + 1)
        if sFound < 0:
            return False
        sPos = sFound
    return True

# loop across all words in dictionary and find longest subsequence
class ExhaustiveGreedySolution:
        
    def longestWord(self, S, D):
        maxLen = 0
        maxWord = None
        for w in D:
            if isSubsequence(S, w) and maxLen < len(w):
                maxLen = len(w)
                maxWord = w
        return maxWord
    
    def oTime(self):
        return "O(W*N); W=size(D), N=length(S)"

# same as abpve. but sort dictionary with longest word first
# so the first match is the one we want
class SortingGreedySolution:
    
    def longestWord(self, S, D):
        for w in sorted(D, key=len, reverse=True):
            if isSubsequence(S, w):
                return w
        return None
    
    def oTime(self):
        return "O(W*N + W log(W)); W=size(D), N=length(S) - cost of sort paid for by fast return in 'found' case"
    
import collections
    
class PreprocessedGreedtSolution:
    def longestWord(self, S, D):
        letter_positions = collections.defaultdict(list)
        for index, letter in enumerate(S):
            letter_positions[letter].append(index)
        for word in sorted(D, key=lambda w: len(w), reverse=True):
            pos = 0
            for letter in word:
                if letter not in letter_positions:
                    break
                possible_positions = [p for p in letter_positions[letter] if p >= pos]
                if not possible_positions:
                    break
                pos = possible_positions[0] + 1
#             else:
                # all letters have valid positions  
                return word

for algo in [ExhaustiveGreedySolution, SortingGreedySolution, PreprocessedGreedtSolution]:
    longestWord = algo().longestWord("abppplee", ["able", "ale", "apple", "bale", "kangaroo"])
    print(algo.__name__ + " -> " + longestWord)
    assert "apple" == longestWord
