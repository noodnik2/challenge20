# Implementation Notes

## Brute Force - "Ignore Double-Bookings"

### Algorithm

1. Canonicalize and order the list of meetings:
    1. Remove duplicate meetings
    1. Order the meeting list by attendee count, largest first
1. Traverse the meeting list, building a map of attendees to meetings
    1. Ignore the meeting if an incoming meeting would produce any double bookings; else:   
    1. Plan the incoming meeting into the map
1. Return the list of meetings planned for all attendees (i.e., map values)
