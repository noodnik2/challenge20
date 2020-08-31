
# from BruteForceIgnoreDoubleBookingsPermutations import packed_meetings
# from BruteForceIgnoreDoubleBookings import packed_meetings
# from BruteForceIgnoreDoubleBookings2 import packed_meetings
# from BruteForceIgnoreDoubleBookings3 import packed_meetings
# from BruteForceIgnoreDoubleBookings4 import packed_meetings
# from BruteForceIgnoreDoubleBookings5 import packed_meetings
# from BruteForceIgnoreDoubleBookings6 import packed_meetings
# from BruteForceIgnoreDoubleBookings7 import packed_meetings
# from BruteForceIgnoreDoubleBookings8 import packed_meetings
from BruteForceIgnoreDoubleBookings9 import packed_meetings

def attendee_count(meeting_list):
    meeting_attendees = [p for sublist in meeting_list for p in sublist]
    return len(set(tuple(meeting_attendees)))

def canonicalize(result):
    return sorted(
        [sorted(meeting) for meeting in result],
        key = lambda meeting: (-len(meeting), meeting)
    )

def runTestCase(description, expected_result, test_data):
    # if description != "Test3":
    # if description != "exampleNew.1":
    # if description != "example3.1c":
    # if description != "example3.3":
    # if description != "example3.5a":
    # if description != "example3.5b":
    # if description != "Test5":
    # if description != "example3.4":
    #     return
    print(f"{description}: {test_data}")
    canonicalized_expected_result = canonicalize(expected_result)
    canonicalized_actual_result = canonicalize(packed_meetings(test_data))
    total_count = attendee_count(test_data)
    expected_count = attendee_count(canonicalized_expected_result)
    actual_count = attendee_count(canonicalized_actual_result)
    print(f"  expected: ({expected_count} / {total_count}) {canonicalized_expected_result}")
    print(f"    actual: ({actual_count} / {total_count}) {canonicalized_actual_result}")
    assert expected_count <= actual_count
    if expected_count < actual_count:
        print(f"WARNING: actual count greater than expected count (yay!); tests should be updated to reflect progress")
    else:
        if canonicalized_expected_result != canonicalized_actual_result:
            print("WARNING: specific results differ; tests should be updated")
    print()

# Based upon the examples given:
runTestCase("example1", [[0,1,2]], [[0,1,2],[1,2]])
runTestCase("example2", [[0,1]], [[0,1],[0],[1]])
runTestCase("example3", [[0,1,2]], [[0,1,2],[2,3]])
runTestCase("example3.1", [[1,2,3,4]], [[1,2,3,4],[0,1,2]])
runTestCase("example3.1b", [[1,5,7], [0,2]], [[0,1],[0,2],[1,5,7]])
runTestCase("example3.1c", [[1,11], [2,12], [3,13], [4,14], [5,15]], [[1,11], [2,12], [3,13], [4,14], [5,15], [1,2,3,4,5,6]])
runTestCase("example3.2", [[1,2,3,4]], [[0,1,2],[1,2,3,4]])
runTestCase("example3.3", [[0, 2, 3, 6, 7]], [[0,1], [0,2], [0,2,3,6,7], [0,3,4,5]])
runTestCase("example3.4", [[0, 1], [2, 3, 4]], [[0,1], [0,2], [2,3,4]])

runTestCase(
    "example3.5a",
    [['bob', 'betty'], ['fred', 'wilma']],
    [['bob', 'betty'], ['bob', 'henry', 'fred'], ['fred', 'wilma']]
)

runTestCase(
    "example3.5b",
    # [['bill', 'dave', 'kali'], ['betty', 'bob'], ['fred', 'wilma']],
    [['bill', 'dave', 'kali'], ['bob', 'henry', 'fred', 'a1', 'a2']],
    [['bob', 'betty'], ['bob', 'doug'], ['bob', 'henry', 'fred', 'a1', 'a2'], ['fred', 'wilma'], ['steve', 'kali'], ['bill', 'dave', 'kali'], ['kali', 'some1'], ['kali', 'some2'], ['kali', 'some3']]
)

runTestCase("exampleNew.1", [[6,7,8,9,10,11],[1,2,3,4]], [[0,1,2],[1,2,3,4],[2,3,4,5,6],[6,7,8,9,10,11]])

# Test cases:

runTestCase(
    "Test1",
    [[0, 1]],
    [[0,1],[0],[1]]
)

runTestCase(
    "Test2",
    [[0, 1, 2]],
    [[0,1,2],[2,3]]
)

runTestCase(
    "Test3",
    [[0, 8, 9, 10, 13], [1, 5, 7], [3, 4, 12], [2, 6]],
	[[4,10],[3,4,12],[0,8,9,10,13],[1,5,7],[2,6],[9,4,10,11,12],[11,13]]
)

runTestCase(
    "Test4",
    [[2, 5, 6, 7, 9, 12, 14, 15], [1, 4, 8]],
	[[6,16,17],[8,9],[1],[7,14,9],[10,5],[2,7],[0,6,7,9],[10,11,5,13,15,16,17],[7,9],[5,9],[2,12,5,6,14,7,15,9],[10,5,14],[1,4,8],[1,3,9],[5]]
)

runTestCase(
    "Test5",
    [[5, 6, 10, 15, 16, 17, 21, 22, 24, 27, 33, 34, 35], [0, 2, 23, 28]],
	[[9,16,34],[10,13,18,20,23,28,30,31,32],[4,7,8,11,16,17,19,25,29,36,37],[0,2,23,28],[1,3,7,8,14,15,19,24,25,26,32],[12,28],[16,21,24,33,34],[5,6,10,15,16,17,21,22,24,27,33,34,35]]
)

runTestCase(
    "Test6",
    [[2, 3, 4, 14, 22, 41, 43, 49, 52, 54, 55, 75, 76], [12, 19, 21, 24, 25, 40, 59, 71, 77, 78], [6, 17, 30, 31, 37, 69, 73], [10, 47, 58, 62, 67], [34, 46, 56, 79], [11, 26, 60, 64], [1, 32, 42], [39, 51, 81], [16, 48, 53], [7, 36], [9, 83], [23, 61], [5, 29], [72]],
	[[55,2],[34,66,60],[15,9,12,82],[39,51,81],[65,69,70],[67,47,58,10,62],[30],[36,7],[0,16],[75,2,20,43],[37,38,44],[34,56,46,79],[26,11,72],[67,47],[16,50],[72],[12],[16,63],[60,18],[64,16],[16,63],[32],[34,16],[16,8],[16,8],[2,3,4,49,52,54,55,75,14,76,22,41,43],[57,30],[64,80,23],[64,69,30],[2,49],[48,16,53],[16,63],[5,16],[69,70],[34,65,69,38,6,30,17],[25,77,59,71,78,19,21,40,12,24],[34,16],[55,66,5,60],[27,14,5,46,76,49],[33,28,69],[25,77,59,71,78,19,40,21,12,24],[5,29],[32],[28,16],[2,14],[37,69,6,30,17,31,73],[32,1,42],[64,26,60,11],[16,13],[16,8],[34,16],[16,44],[9,83],[45,81],[46,76],[46,8,39,51],[8,54],[16,50],[74,81,53],[5,16],[61,23],[65,68,77,81],[74,35,37,6,17]]
)

runTestCase(
    "Test7",
    [[3, 4, 5, 18, 26, 48, 50, 55, 59, 60, 61, 82, 85], [16, 22, 23, 28, 29, 47, 65, 78, 86, 87], [7, 20, 35, 37, 42, 75, 80], [10, 54, 64, 68, 73], [11, 15, 36, 58, 79], [9, 45, 52, 57], [17, 33, 67, 71], [1, 38, 49], [12, 31, 88], [19, 27, 77], [39, 40, 43], [2, 56], [14, 66]],
	[[71,61,26],[72,75,76],[73,54,64,10,68],[88,56,69],[71,17,8],[18],[40,17,66,47,13],[40,62,52,88],[11,79,36,58,15],[17,67],[33],[38],[3,4,5,55,59,60,61,82,18,85,26,48,50],[17,84],[74,83,43],[71,89,27],[11,79,46,58],[34,33],[71,75,35],[66,14],[72,33],[75,76],[19,77,27],[40,72,75,44,7,35,20],[18,34],[71,17,33,67],[29,86,65,78,87,22,23,47,16,28],[38,37,27],[30,18,6,52,85,55],[79,28,58],[17,88,69],[76,23],[39,32,75],[29,86,65,78,87,22,47,23,16,28],[11,79,58],[21,88,51],[38],[61,88],[3,18],[39,0,24,23],[33,70],[42,75,7,35,20,37,80],[38,1,49],[31,12,88],[39,40,43],[33,56],[33,25],[33],[2,56],[52,85],[52,9,45,57],[53,33],[66,27],[63,88],[81,41,42,7,20]]
)

print('All tests passed')