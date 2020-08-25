
debug_enabled = False

def packed_meetings(meetings_list):

    #  Canonicalize and order the list of meetings:
    #  - Remove duplicate meetings
    #  - Order the meeting list by attendee count, largest first
    sorted_meetings_list = sorted(list(set((tuple(l) for l in meetings_list))), key=len, reverse=True)
    debug(f"sorted_meetings_list{str(sorted_meetings_list)}")

    # Traverse the meeting list, building a map of attendees to meetings
    # - Ignore the meeting if an incoming meeting would produce any double bookings; else:
    # - Plan the incoming meeting into the map
    attendee_meeting = {}
    for meeting in sorted_meetings_list:
        current_attendees = list(attendee_meeting.keys())
        intersect = intersection(meeting, current_attendees)
        debug(f"meeting{meeting} current_attendees{str(current_attendees)} intersection{intersect}")
        if len(intersect) > 0:
            # replanning_cost = sum(len(attendee_meeting[attendee]) for attendee in intersect)
            debug(f"ignoring meeting{meeting}; double booking {intersect}")
            # if (replanning_cost < len(intersect)):
            #     print(f"  *** ---> would be worth replanning!")
            continue
        for attendee in meeting:
            attendee_meeting[attendee] = meeting

    # Return the list of meetings planned for all attendees (i.e., map values)
    return sorted([list(t) for t in set(attendee_meeting.values())], key=len, reverse=True)

def intersection(l1, l2):
    l2set = set(l2)
    return [value for value in l1 if value in l2set]

def debug(message):
    if debug_enabled:
        print("     debug: " + message)