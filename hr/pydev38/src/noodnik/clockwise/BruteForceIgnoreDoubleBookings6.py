
# debug_enabled = False
debug_enabled = True

#
#   This approach orders meetings according to the number of attendees they will
#   prevent from being seated (in other meetings).
#

def packed_meetings(meeting_list):

    meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

    # construct a dictionary of attendee to meetings they're in
    attendee_meetings = {}
    for meeting in meetings:
        for attendee in meeting:
            attendee_meetings.setdefault(attendee, []).append(meeting)
    debug(f"attendee_meetings{attendee_meetings}")

    # construct a dictionary of meeting to double booked attendees
    double_booked_meeting_attendees = {}
    for a, ms in attendee_meetings.items():
        if len(ms) == 2:
            for m in ms:
                double_booked_meeting_attendees.setdefault(m, []).append(a)
    debug(f"double_booked_meeting_attendees{double_booked_meeting_attendees}")

    # construct a dictionary of meeting to attendees that would not be seated
    meeting_related_attendees = {}
    for meeting in meetings:
        related_attendees = []
        for attendee in meeting:
            meetings_for_attendee = attendee_meetings[attendee]
            if len(meetings_for_attendee) > 1:
                for related_meeting in meetings_for_attendee:
                    related_attendees.extend(related_meeting)
                    if related_meeting in double_booked_meeting_attendees:
                        related_meeting_double_booked_attendees = double_booked_meeting_attendees[related_meeting]
                        for related_double_booked_attendee in related_meeting_double_booked_attendees:
                            related_attendees.remove(related_double_booked_attendee)
        related_attendees_for_meeting = set(attendee for attendee in related_attendees if attendee not in meeting)
        meeting_related_attendees[meeting] = related_attendees_for_meeting
    debug(f"meeting_related_attendees{meeting_related_attendees}")

    meeting_related_attendees_counts = {
        meeting: len(attendee)
        for (meeting, attendee)
        in meeting_related_attendees.items()
    }
    debug(f"meeting_related_attendees_counts{meeting_related_attendees_counts}")

    meeting_ranker = lambda meeting: (len(meeting_related_attendees[meeting]), -len(meeting))
    ranked_meetings = sorted(meetings, key = meeting_ranker)
    debug(f"ranked_meetings{ranked_meetings}")

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

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
