from c3p_scorer import calc_score

# functions
def assert_expected_result(message, expected, actual):
    if expected != actual:
        print(f"{message}: expected({expected}), actual({actual})")
    assert expected == actual

# test presumed python comparison of tuples
assert (1) > (0)
assert (1, 1) > (1, 0)
assert (1, 0) > (0, 1)
assert (3, 3, 2, 1) > (1, 2)

# test module operations
assert_expected_result("high card-1", (0, 6, 3, 1), calc_score("3q 8s 5h".split()))
assert_expected_result("high card-2", (0, 12, 8, 0), calc_score("Tq As 2h".split()))
assert_expected_result("pair-1", (1, 2, 12), calc_score("4c 4d Ah".split()))
assert_expected_result("pair-2", (1, 0, 2), calc_score("2c 4d 2h".split()))
assert_expected_result("pair-3", (1, 12, 2), calc_score("4c Ad Ah".split()))
assert_expected_result("pair-4", (1, 6, 2), calc_score("8c 8h 4d".split()))
assert_expected_result("pair-5", (1, 3, 0), calc_score("5s 5h 2h".split()))
assert_expected_result("flush-1", (2, 12, 6, 2), calc_score("Ac 4c 8c".split()))
assert_expected_result("flush-2", (2, 11, 10, 8), calc_score("Td Kd Qd".split()))
assert_expected_result("flush-3", (2, 12, 9, 0), calc_score("2s As Js".split()))
assert_expected_result("flush-4", (2, 5, 3, 1), calc_score("3h 5h 7h".split()))
assert_expected_result("straight-1", (3, 3, 2, 1), calc_score("5h 3c 4d".split()))
assert_expected_result("straight-2", (3, 9, 8, 7), calc_score("9h Td Js".split()))
assert_expected_result("straight-3", (3, 12, 11, 10), calc_score("Qh Kd As".split()))
assert_expected_result("straight-4", (3, 2, 1, 0), calc_score("4d 2s 3d".split()))
assert_expected_result("three of a kind-1", (4, 2, 2, 2), calc_score("4c 4h 4d".split()))
assert_expected_result("three of a kind-2", (4, 5, 5, 5), calc_score("7c 7s 7d".split()))
assert_expected_result("three of a kind-3", (4, 6, 6, 6), calc_score("8c 8s 8d".split()))
assert_expected_result("straight flush-1", (5, 11, 10, 9), calc_score("Qh Jh Kh".split()))
assert_expected_result("straight flush-2", (5, 2, 1, 0), calc_score("4s 3s 2s".split()))
assert_expected_result("straight flush-1", (5, 12, 11, 10), calc_score("Qh Ah Kh".split()))

print("scorer OK")
