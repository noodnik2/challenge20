
debug_enabled = False

#
#   In this primitive "brute-force" approach, we order the meetings by the
#   number of attendees (largest meetings first), discarding subsequent
#   meetings having double booked attendees.
#

def packed_meetings(meetings_list):

    #  canonicalize and order the list of meetings:
    #  - remove duplicate meetings
    #  - order the meeting list by attendee count(largest first), then by meeting order

    meeting_list_sortkey = lambda meeting: (-len(meeting), meeting)
    sorted_meetings_list = sorted(
        list(
            set(
                tuple(sorted(meeting)) for meeting in meetings_list
            )
        ),
        key = meeting_list_sortkey
    )
    debug(f"sorted_meetings_list{str(sorted_meetings_list)}")

    # construct a dictionary of attendee to meeting, rejecting double bookings
    planned_attendee_meetings = {}
    for proposed_meeting in sorted_meetings_list:
        planned_attendees = list(planned_attendee_meetings.keys())
        double_booked_attendees = [attendee for attendee in proposed_meeting if attendee in set(planned_attendees)]
        if len(double_booked_attendees) > 0:
            # replanning_cost = sum(len(planned_attendee_meetings[attendee]) for attendee in double_booked_attendees)
            # if (replanning_cost <= len(double_booked_attendees)):
            #     print(f"  *** ---> might be worth re-planning")
            debug(f"ignoring proposed_meeting{proposed_meeting}; double_booked_attendees{double_booked_attendees})")
            continue
        for attendee in proposed_meeting:
            planned_attendee_meetings[attendee] = proposed_meeting

    # return the list of meetings planned for all attendees (i.e., map values)
    return sorted(
        list(
            list(meeting) for meeting in set(planned_attendee_meetings.values())
        ),
        key = meeting_list_sortkey
    )

def debug(message):
    if debug_enabled:
        print("     debug: " + message)

