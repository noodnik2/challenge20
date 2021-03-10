from c3p_grader import calc_winners

# functions
def assert_expected_result(message, expected, player_hands_s):
    actual = ' '.join(calc_winners([player_hand_s.split() for player_hand_s in player_hands_s]))
    if expected != actual:
        print(f"{message}: expected({expected}), actual({actual})")
    assert expected == actual

# test module operations
assert_expected_result("high card wins", "2", ["5 2c 3s 5d", "2 2h 4c 6s"])
assert_expected_result("pair trumps high card", "1", ["0 Ac Js Qd", "1 2h 2c 3s"])
assert_expected_result("kicker in action", "0", ["0 2c 2s 5d", "1 2h 2d 4s"])
assert_expected_result("two way tie", "2 3", ["0 Qc Kc 4s", "1 Ah 2c Js", "2 3h 9h Th", "3 Tc 9c 3c"])
assert_expected_result("three way tie", "0 1 2", ["0 3q 8s 5h", "2 3q 8s 5h", "1 3q 8s 5h"])

print("grader OK")
