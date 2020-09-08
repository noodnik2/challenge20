import itertools

# debug_enabled = False
debug_enabled = True

#
#   Tries to improve the results of another solver by iterating over
#   each meeting in the list, eliminating all double booked meetings
#   then using the solver to solve for the sublist.
#

attendee_count = lambda meetings: sum( len(meeting) for meeting in meetings )


def partition_meetings(meetings):

    # return two lists of meetings:
    # 1. list of meetings which are mutually disjoint
    # 2. list of meetings having at least one conflict

    disjoint_meetings = []
    conflict_meetings = []

    for outer_meeting in meetings:
        is_disjoint = True
        for inner_meeting in meetings:
            if not set(inner_meeting).isdisjoint(outer_meeting):
                is_disjoint = False
                break
        if is_disjoint:
            disjoint_meetings.append(outer_meeting)
        else:
            conflict_meetings.append(outer_meeting)

    return disjoint_meetings, conflict_meetings


def plan_meetings(meetings):
    no_conflict_meetings = []
    planned_attendees = []
    for meeting in meetings:
        if set(meeting).isdisjoint(planned_attendees):
            planned_attendees += meeting
            no_conflict_meetings.append(meeting)
    return no_conflict_meetings


def packed_meetings(meeting_list):

    disjoint_meetings, conflict_meetings = partition_meetings(meeting_list)
    print(f"disjoint_meetings{disjoint_meetings} conflict_meetings{conflict_meetings}")

    conflict_permutations = itertools.permutations(conflict_meetings)
    print(f"conflict_permutations_count({len([x for x in conflict_permutations])})")

    return[]

    # best_meetings = []
    # for conflict_permutation in conflict_permutations:
    #     proposed_meetings = plan_meetings(conflict_permutation)
    #     print(f"proposed_meetings{proposed_meetings}")
    #     if attendee_count(proposed_meetings) > attendee_count(best_meetings):
    #         best_meetings = proposed_meetings
    #
    # print(f"best_meetings{best_meetings}")
    # return best_meetings


def debug(message):
    if debug_enabled:
        print("     debug: " + message)
