
# debug_enabled = False
debug_enabled = True

#
#   This approach orders meetings according to the number of attendees they will
#   prevent from being seated (in other meetings).
#
#   Since you can't double book attendees, including a meeting in the final list
#   implies that any attendee not in the meeting which is in any other meeting
#   that can't take place (i.e., if the meeting in question takes place) is
#   counted as prevented from being seated.
#
#   Here's how to count it:
#
#   1.  For each meeting to be considered:
#   2.  Create a list (initially empty) of related attendees
#   3.  For each double booked attendee in the meeting:
#   4.  For each meeting in which the attendee is doubly booked:
#   5.  Dump all attendees of the (doubly booked) meeting into the list
#   6.  Return the count of attendees in the list which are not in the meeting to be considered
#

def packed_meetings(meeting_list):

    meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

    # construct a dictionary of attendee to meetings they're in
    attendee_meetings = {}
    for meeting in meetings:
        for attendee in meeting:
            attendee_meetings.setdefault(attendee, []).append(meeting)
    debug(f"attendee_meetings{attendee_meetings}")

    # construct a dictionary of meeting to attendees that would not be seated
    meeting_related_attendees = {}
    for meeting in meetings:
        related_attendees = []
        for attendee in meeting:
            meetings_for_attendee = attendee_meetings[attendee]
            if len(meetings_for_attendee) > 1:
                for related_meeting in meetings_for_attendee:
                    related_attendees.extend(related_meeting)
        meeting_related_attendees[meeting] = set( attendee for attendee in related_attendees if attendee not in meeting )

    debug(f"meeting_related_attendees{meeting_related_attendees}")

    meeting_related_attendees_counts = {
        meeting: len(attendee)
        for (meeting, attendee)
        in meeting_related_attendees.items()
    }
    debug(f"meeting_related_attendees_counts{meeting_related_attendees_counts}")

    meeting_ranker = lambda meeting: len(meeting_related_attendees[meeting])
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
