
# debug_enabled = False
debug_enabled = True

#
#   This approach orders meetings according to the number of attendees they will
#   prevent from being seated (in other meetings).
#

class MeetingCalculator:

    def __init__(self, meeting_list):

        self.meetings = set( tuple(sorted(meeting)) for meeting in meeting_list )

        # construct a dictionary of attendee to meetings they're in
        self.attendee_meetings = {}
        for meeting in self.meetings:
            for attendee in meeting:
                self.attendee_meetings.setdefault(attendee, []).append(meeting)
        debug(f"attendee_meetings{self.attendee_meetings}")

        # construct a dictionary of meeting to double booked attendees
        self.meeting_double_booked_attendees = {}
        for attendee, meetings_for_attendee in self.attendee_meetings.items():
            if len(meetings_for_attendee) > 1:
                for meeting in meetings_for_attendee:
                    self.meeting_double_booked_attendees.setdefault(meeting, []).append(attendee)
        debug(f"meeting_double_booked_attendees{self.meeting_double_booked_attendees}")

    def accumulate_double_booked_attendees(self, meeting, attendee_meetings, output_attendees):
        double_booked_attendees = self.meeting_double_booked_attendees.get(meeting, ())
        for double_booked_attendee in double_booked_attendees:
            linked_meetings = list(attendee_meetings[double_booked_attendee])
            linked_meetings.remove(meeting)

    def meeting_value(self, meeting):
        displaced_attendees = set()
        for attendee in meeting:
            self.pick_meeting_for_attendee(attendee, meeting, displaced_attendees)
        return len(displaced_attendees)
        # return displaced_attendees

    def pick_meeting_for_attendee(self, attendee, meeting, displaced_attendees):
        x = list(self.attendee_meetings[attendee])
        x.remove(meeting)
        for m in x:
            displaced_attendees.update(m)

    # def meeting_value0(self, meeting):
    #     removed_attendees = set()
    #     double_booked_attendees = self.meeting_double_booked_attendees.get(meeting, ())
    #     for double_booked_attendee in double_booked_attendees:
    #         double_booked_attendee_meetings = list(self.attendee_meetings[double_booked_attendee])
    #         double_booked_attendee_meetings.remove(meeting)
    #         if len(double_booked_attendee_meetings) == 1:
    #             for linked_meeting in double_booked_attendee_meetings:
    #                 linked_meeting_value = self.list1_minus_list2(linked_meeting, self.meeting_double_booked_attendees.get(linked_meeting, ()))
    #                 non_double_booked_attendees.extend(linked_meeting_value)
    #     return non_double_booked_attendees

    def list1_minus_list2(self, list1, list2):
        return list(set(list1) - set(list2))

def packed_meetings(meeting_list):

    meeting_calculator = MeetingCalculator(meeting_list)

    for meeting in meeting_calculator.meetings:
        print(f"value of meeting{meeting} is {meeting_calculator.meeting_value(meeting)}")

    # return the list of meetings planned for all attendees
    return [ ]

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
