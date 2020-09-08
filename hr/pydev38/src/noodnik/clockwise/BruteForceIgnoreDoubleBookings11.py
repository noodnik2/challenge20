
import BruteForceIgnoreDoubleBookings9 as solver

debug_enabled = False
# debug_enabled = True

#
#   Tries to improve the results of another solver by iterating over
#   each meeting in the list, eliminating all double booked meetings
#   then using the solver to solve for the sublist.
#

attendee_count = lambda meetings: sum( len(meeting) for meeting in meetings )

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


def packed_meetings(meeting_list):

    meetingCalculator = MeetingCalculator(meeting_list)

    meetings = meetingCalculator.meetings

    # start with solver's solution across the given set of meetings
    best_solution = solver.packed_meetings(meetings)
    best_solution_count = attendee_count(best_solution)
    debug(f"solver's initial solution seats {best_solution_count} attendee(s)")

    # see if we can do better
    for meeting in meetings:

        # start with all meetings
        remaining_meetings = list(meetings)

        # remove all other meetings having double bookings with this one
        double_booked_attendees = meetingCalculator.meeting_double_booked_attendees[meeting]
        if not double_booked_attendees:
            # no double booked attendees in this meeting; no improvement possible
            continue

        for attendee in double_booked_attendees:
            double_booked_meetings = meetingCalculator.attendee_meetings[attendee]
            for double_booked_meeting in double_booked_meetings:
                if double_booked_meeting in remaining_meetings:
                    remaining_meetings.remove(double_booked_meeting)

        current_solution = solver.packed_meetings(remaining_meetings)
        current_solution.append(meeting)
        current_solution_count = attendee_count(current_solution)
        debug(f"solution starting with{meeting} seats {current_solution_count} attendee(s)")
        if current_solution_count > best_solution_count:
            best_solution_count = current_solution_count
            best_solution = current_solution

    return best_solution

def debug(message):
    if debug_enabled:
        print("     debug: " + message)
