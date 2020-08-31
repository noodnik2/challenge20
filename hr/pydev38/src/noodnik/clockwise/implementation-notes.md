# Implementation Notes

## Brute Force - "Ignore Double-Bookings"

### Algorithm

1.  Canonicalize and order the list of meetings:
    1. Remove duplicate meetings
    1. Order the meeting list by decreasing meeting value 
1.  Traverse the meeting list, building a map of attendees to meetings
    which do not produce double bookings   
1.  Return the list of meetings planned for all attendees (i.e., map values)

#### Determination of Meeting Value

Meetings are ranked by a value that reflects their contribution towards
producing the desired result of maximizing the number of attendees that
can be seated across all meetings (i.e., with no double bookings).

Successive, increasingly effective approaches have been used to construct
this value, including simple calculations of:

- meeting size
- number of attendees _not_ seated should the meeting take place 
- overall number of attendees that can be seated in the best case
  (e.g., discarding all conflicting meetings) should the meeting take place

The concept of determining and ranking according to _meeting value_
continues to be the direction of pursuit.  Though tweaks to this
approach continue demonstrate better overall results, none have
[so far](./BruteForceIgnoreDoubleBookings9.py) improved results
across all test cases as compared to the original,
[simple `meeting size` valuation](./BruteForceIgnoreDoubleBookings.py).
It's suspected that a deterministic, global approach has yet to be found.
Something that considers all possible configurations, yet vastly more
efficient than a brute-force permutation (which was
[also tried](./BruteForceIgnoreDoubleBookingsPermutations.py),
but could not get past test 4!).
