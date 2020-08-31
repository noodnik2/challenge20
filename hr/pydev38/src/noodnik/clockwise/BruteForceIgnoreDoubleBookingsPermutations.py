
# debug_enabled = True
debug_enabled = False

#
#   In this approach, we simply run each permutation of the meetings through
#   the "ignore double bookings" packer and use the one that produces the
#   most seated attendees.
#

def packed_meetings(meeting_list):

    meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

    meeting_permutations = []
    heapPermutation(meeting_permutations, list(meetings), len(meetings), len(meetings))

    max_attendee_count = 0
    best_no_double_bookings_meetings = None
    for permuted_meetings in meeting_permutations:
        debug(f"permuted_meetings{permuted_meetings}")
        no_double_bookings_meetings = pack_meetings_by_ignoring_double_bookings(permuted_meetings)
        attendee_count = sum( len(meeting) for meeting in no_double_bookings_meetings )
        debug(f"no_double_bookings_meetings{no_double_bookings_meetings} => attendee_count({attendee_count})")
        if attendee_count > max_attendee_count:
            best_no_double_bookings_meetings = no_double_bookings_meetings
            max_attendee_count = attendee_count
            debug(f"*** NEW WINNER: attendee_count({attendee_count})")

    return best_no_double_bookings_meetings

def pack_meetings_by_ignoring_double_bookings(ranked_meetings):

    # construct a dictionary of attendee to meeting, rejecting double bookings
    ranked_attendee_meeting = {}
    for ranked_meeting in ranked_meetings:
        ranked_attendees = list(ranked_attendee_meeting.keys())
        double_booked_attendees = [attendee for attendee in ranked_meeting if attendee in set(ranked_attendees)]
        if len(double_booked_attendees) > 0:
            # replanning_cost = sum(len(planned_attendee_meetings[attendee]) for attendee in double_booked_attendees)
            # if (replanning_cost <= len(double_booked_attendees)):
            #     debug(f"  *** ---> might be worth re-planning")
            debug(f"ignoring ranked_meeting{ranked_meeting}; double_booked_attendees{double_booked_attendees}")
            continue
        for attendee in ranked_meeting:
            ranked_attendee_meeting[attendee] = ranked_meeting

    # return the list of meetings planned for all attendees
    return [ list(meeting) for meeting in set(ranked_attendee_meeting.values()) ]

# Generating permutation using Heap Algorithm
def heapPermutation(output, a, size, n):

    # if size becomes 1 then prints the obtained permutation

    if (size == 1):
        output.append([v for v in a])
        return

    for i in range(size):

        heapPermutation(output, a, size - 1, n)

        # if size is odd, swap first and last element
        # else swap ith and last element
        if size & 1:
            a[0], a[size-1] = a[size-1], a[0]
        else:
            a[i], a[size-1] = a[size-1], a[i]

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
