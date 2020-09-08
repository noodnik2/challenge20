
debug_enabled = False
# debug_enabled = True

#
#   This approach ignores double booked meetings after ordering the meetings by the number
#   of attendees that can be seated after eliminating other meeting having any of the same
#   double booked attendees.
#

class MeetingCalculator:

    def __init__(self, meeting_list):

        self.meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

        # construct a dictionary of attendee to meetings they're in
        # and initialize a dictionary of meeting to double booked attendees
        self.attendee_meetings = {}
        self.meeting_double_booked_attendees = {}
        for meeting in self.meetings:
            self.meeting_double_booked_attendees[meeting] = []
            for attendee in meeting:
                self.attendee_meetings.setdefault(attendee, []).append(meeting)
        debug(f"attendee_meetings{self.attendee_meetings}")

        # update the dictionary of meeting to double booked attendees
        # self.meeting_double_booked_attendees = {}
        for attendee, meetings_for_attendee in self.attendee_meetings.items():
            if len(meetings_for_attendee) > 1:
                for meeting in meetings_for_attendee:
                    self.meeting_double_booked_attendees[meeting].append(attendee)
        debug(f"meeting_double_booked_attendees{self.meeting_double_booked_attendees}")

        # construct a dictionary of double booked attendees to the largest meeting
        self.double_booked_attendees_largest_meeting = {}
        for meeting, double_booked_attendees in self.meeting_double_booked_attendees.items():
            double_booked_attendees_key = tuple(double_booked_attendees)
            if (
                    double_booked_attendees_key not in self.double_booked_attendees_largest_meeting
             or len(meeting) > len(self.double_booked_attendees_largest_meeting[double_booked_attendees_key])
            ):
                self.double_booked_attendees_largest_meeting[double_booked_attendees_key] = meeting
        debug(f"double_booked_attendees_largest_meeting{self.double_booked_attendees_largest_meeting}")

    def meeting_value(self, focus_meeting):
        seated_attendees = set(focus_meeting)
        double_booked_focus_attendees = set(self.meeting_double_booked_attendees[focus_meeting])
        for meeting in self.meetings:
            if meeting == focus_meeting:
                continue
            double_booked_attendees = self.meeting_double_booked_attendees[meeting]
            if double_booked_focus_attendees.isdisjoint(double_booked_attendees):
                if not double_booked_attendees:
                    seated_attendees.update(meeting)
                    continue
                largest_unrelated_meeting = self.double_booked_attendees_largest_meeting[tuple(double_booked_attendees)]
                debug(f"largest_unrelated_meeting{largest_unrelated_meeting} for{double_booked_attendees}")
                seated_attendees.update(largest_unrelated_meeting)
        debug(f"for{focus_meeting} seated_attendee_count({len(seated_attendees)}) seated_attendees{seated_attendees}")
        return len(seated_attendees)

    def non_double_booked_meetings(self):

        meeting_ranker = lambda meeting : -self.meeting_value(meeting)

        ranked_meetings = sorted(self.meetings, key = meeting_ranker)
        debug(f"ranked_meetings{ranked_meetings}")

        # construct a dictionary of attendee to meeting, rejecting double bookings
        ranked_attendee_meeting = {}
        for ranked_meeting in ranked_meetings:
            if not set(ranked_meeting).isdisjoint(ranked_attendee_meeting.keys()):
                debug(f"ignoring ranked_meeting{ranked_meeting}")
                continue
            for attendee in ranked_meeting:
                ranked_attendee_meeting[attendee] = ranked_meeting

        # return the list of meetings planned for all attendees
        return [ list(meeting) for meeting in set(ranked_attendee_meeting.values()) ]

def packed_meetings(meeting_list):
    return MeetingCalculator(meeting_list).non_double_booked_meetings()

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
