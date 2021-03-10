from sys import stdin
from c3p_grader import calc_winners

player_hands = []
for _ in range(0, int(stdin.readline())):
    player_hands.append(stdin.readline().strip().split())

print(' '.join(calc_winners(player_hands)))
