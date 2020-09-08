
# debug_enabled = False
debug_enabled = True

#
#   Tries to improve the results of another solver by iterating over
#   each meeting in the list, eliminating all double booked meetings
#   then using the solver to solve for the sublist.
#

attendee_count = lambda meetings: sum( len(meeting) for meeting in meetings )

class MeetingCalculator:

    def __init__(self, meeting_list):

        self.meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )


def partition_meetings0(meetings):
    # return two lists of meetings:
    # 1. the first meeting and all other meetings which don't have conflicts with it
    # 2. all meetings having conflicts with it
    if not meetings:
        return [], []
    without_conflict_meetings = [meetings[0]]
    with_conflict_meetings = []
    for meeting in [meeting for meeting in meetings if meeting != meetings[0]]:
        if set(meeting).isdisjoint(meetings[0]):
            without_conflict_meetings.append(meeting)
        else:
            with_conflict_meetings.append(meeting)
    return without_conflict_meetings, with_conflict_meetings


def partition_meetings(meetings):
    # return two lists of meetings:
    # 1. the first meeting and all other meetings which don't have conflicts with it
    # 2. all meetings having conflicts with it
    if not meetings:
        return [], []
    best_without_conflict_meetings = []
    best_with_conflict_meetings = []
    # for target_meeting in sorted(meetings, key = len):
    for target_meeting in meetings:
        without_conflict_meetings = [target_meeting]
        with_conflict_meetings = []
        for meeting in [meeting for meeting in meetings if meeting != target_meeting]:
            has_conflict = False
            for without_conflict_meeting in without_conflict_meetings:
                if not set(meeting).isdisjoint(without_conflict_meeting):
                    has_conflict = True
                    break
            if has_conflict:
                with_conflict_meetings.append(meeting)
            else:
                without_conflict_meetings.append(meeting)
        if attendee_count(without_conflict_meetings) > attendee_count(best_without_conflict_meetings):
            best_without_conflict_meetings = without_conflict_meetings
            best_with_conflict_meetings = with_conflict_meetings

    return best_without_conflict_meetings, best_with_conflict_meetings


def packed_meetings(meeting_list):

    # meetingCalculator = MeetingCalculator(meeting_list)

    without_conflict_meetings, with_conflict_meetings = partition_meetings(meeting_list)
    print(f"with_conflict_meetings{with_conflict_meetings} without_conflict_meetings{without_conflict_meetings}")
    return without_conflict_meetings


def debug(message):
    if debug_enabled:
        print("     debug: " + message)
