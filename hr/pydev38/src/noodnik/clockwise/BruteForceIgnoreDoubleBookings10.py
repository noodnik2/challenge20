
# debug_enabled = False
debug_enabled = True

#
#   I have no idea (yet) what this does.
#

class MeetingCalculator:

    def __init__(self, meeting_list):
        self.meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

        # construct a dictionary of attendee to meetings they're in
        # and initialize a dictionary of meeting to double booked attendees
        # and initialize a dictionary of meeting to double booked meetings
        self.attendee_meetings = {}
        self.meeting_double_booked_attendees = {}
        self.meeting_double_booked_meetings = {}
        for meeting in self.meetings:
            self.meeting_double_booked_attendees[meeting] = []
            self.meeting_double_booked_meetings[meeting] = set()
            for attendee in meeting:
                self.attendee_meetings.setdefault(attendee, []).append(meeting)
        debug(f"attendee_meetings{self.attendee_meetings}")

        # update the dictionary of meeting to double booked attendees
        for attendee, meetings_for_attendee in self.attendee_meetings.items():
            if len(meetings_for_attendee) > 1:
                for meeting in meetings_for_attendee:
                    self.meeting_double_booked_attendees[meeting].append(attendee)
        debug(f"meeting_double_booked_attendees{self.meeting_double_booked_attendees}")

        # update the dictionary of meeting to the set of double booked meetings
        for meeting in self.meetings:
            double_booked_attendees = self.meeting_double_booked_attendees[meeting]
            for double_booked_attendee in double_booked_attendees:
                double_booked_meetings = self.attendee_meetings[double_booked_attendee]
                self.meeting_double_booked_meetings[meeting].update(double_booked_meetings)
        debug(f"meeting_double_booked_meetings{self.meeting_double_booked_meetings}")

        # construct a dictionary of meeting to the list of all related attendees


    def non_double_booked_meetings(self):
        return []

def packed_meetings(meeting_list):
    return MeetingCalculator(meeting_list).non_double_booked_meetings()

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
