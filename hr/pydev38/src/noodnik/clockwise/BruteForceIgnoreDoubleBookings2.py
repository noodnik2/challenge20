
debug_enabled = False

#
#   In this primitive "brute-force" approach, we order the meetings by the
#   number of attendees (largest meetings first) then by the number of double
#   bookings across all attendees of the meeting, rejecting subsequent
#   meetings having double booked attendees.
#

def packed_meetings(meetings_list):

    # construct a set of meeting tuples, each sorted by attendee id
    ordered_meeting_set = set( tuple(sorted(meeting)) for meeting in meetings_list )

    # construct a dictionary of attendee to the number of double bookings for each
    attendee_meeting_count_dict = {}
    for meeting in ordered_meeting_set:
        for attendee in meeting:
            attendee_meeting_count_dict[attendee] = attendee_meeting_count_dict.get(attendee, 0) + 1
    debug(f"attendee_meeting_count_dict{attendee_meeting_count_dict}")

    # construct a dictionary of meetings to number of double bookings for each
    meeting_double_booked_count_dict = {}
    for meeting in ordered_meeting_set:
        meeting_double_booked_count = 0
        for attendee in meeting:
            meeting_double_booked_count += attendee_meeting_count_dict[attendee] - 1
        meeting_double_booked_count_dict[meeting] = meeting_double_booked_count
    debug(f"meeting_double_booked_count_dict{meeting_double_booked_count_dict}")

    #  canonicalize and order the list of meetings, ordered by attendee count (largest first),
    #  then by number of double bookings (across all attendees for the meeting),
    #  then by (natural) meeting order

    meetings_list_sortkey = lambda meeting: (-len(meeting), meeting_double_booked_count_dict[tuple(meeting)], meeting)
    sorted_meetings_list = sorted(
        ordered_meeting_set,
        key = meetings_list_sortkey
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

    # Return the list of meetings planned for all attendees (i.e., map values)
    return sorted(
        list(
            list(meeting) for meeting in set(planned_attendee_meetings.values())
        ),
        key = meetings_list_sortkey
    )

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
