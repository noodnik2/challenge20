
debug_enabled = True

#
#   In this primitive "brute-force" approach, we order the meetings by the
#   number number of double bookings across all attendees of the meeting,
#   rejecting subsequent meetings having double booked attendees.
#

def get_meeting_value_fn(meeting, attendee_meetings_dict, meeting_double_booked_attendees_dict, meeting_value_accounted_for_set, except_attendees):
    meeting_value = len(meeting)
    meeting_value_accounted_for_set.add(meeting)
    debug(f"getting value for meeting{meeting} except_attendees{except_attendees}")
    if meeting in meeting_double_booked_attendees_dict.keys():
        double_booked_attendees = meeting_double_booked_attendees_dict[meeting]
        debug(f"meeting{meeting} has double_booked_attendees{double_booked_attendees}")
        for attendee in double_booked_attendees:
            if except_attendees != None and attendee in except_attendees:
                debug(f"attendee({attendee} is in except_attendees{except_attendees} so skipping")
                continue
            for double_booked_meeting in attendee_meetings_dict[attendee]:
                if double_booked_meeting in meeting_value_accounted_for_set:
                    # debug(f"double_booked_meeting{double_booked_meeting} already accounted for")
                    continue
                double_booked_meeting_value = get_meeting_value_fn(
                    double_booked_meeting,
                    attendee_meetings_dict,
                    meeting_double_booked_attendees_dict,
                    meeting_value_accounted_for_set,
                    double_booked_attendees
                )
                meeting_value -= double_booked_meeting_value
    debug(f"value of{meeting} is {meeting_value}")
    return meeting_value

def packed_meetings(meetings_list):

    # construct a set of meeting tuples, each sorted by attendee id
    ordered_meeting_set = set( tuple(sorted(meeting)) for meeting in meetings_list )

    # construct a dictionary of attendee to meetings they're in
    attendee_meetings_dict = {}
    for meeting in ordered_meeting_set:
        for attendee in meeting:
            attendee_meetings_dict.setdefault(attendee, []).append(meeting)
    debug(f"attendee_meetings_dict{attendee_meetings_dict}")

    # construct a dictionary of meetings to double booked attendees
    meeting_double_booked_attendees_dict = {}
    for meeting in ordered_meeting_set:
        for attendee in meeting:
            if len(attendee_meetings_dict[attendee]) > 1:
                meeting_double_booked_attendees_dict.setdefault(meeting, []).append(attendee)
    debug(f"meeting_double_booked_attendees_dict{meeting_double_booked_attendees_dict}")

    #  canonicalize and order the list of meetings, ordered by number of double bookings
    #  (across all attendees for the meeting), then by attendee count (largest first),
    #  then by (natural) meeting order

    meetings_list_sortkey = lambda meeting: -get_meeting_value_fn(meeting, attendee_meetings_dict, meeting_double_booked_attendees_dict, set(), None)
    sorted_meetings_list = sorted(
        ordered_meeting_set,
        key = meetings_list_sortkey
    )
    print(f"sorted_meetings_list{str(sorted_meetings_list)}")
    # return []

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
        )
        # ,key = meetings_list_sortkey
    )

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
