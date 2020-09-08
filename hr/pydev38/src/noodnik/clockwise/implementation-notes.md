# Implementation Notes

## Brute Force - "Ignore Double-Bookings"

### Algorithm

1.  Canonicalize and order the list of meetings:
    1. Remove duplicate meetings
    1. Order the meeting list by decreasing meeting value 
1.  Traverse the ordered meeting list, building a map of attendees to meetings
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

#### Other Ideas

Looking to make a graph of each attendee to the (number of) other attendees
who won't be seated in the optimal configuration should they get a seat.

Example:

1.  In the case of [[Bob, Betty, Frank], [Bob, Henry, Fred], [Fred, Wilma], [Frank, Louise]]:
    - if Betty gets a seat, then Henry and Louise won't get seats
    - if Henry gets a seat, then Betty and Wilma won't get seats
    - if Louise gets a seat, then Betty won't get seats
    - if Wilma gets a seat, then:
        - Henry will not get a seat
        - Either Louise or Bob and Betty don't get seats
        - worst case: 2 attendees are out
    - if Frank gets a seat, then either:
        - it's with Louise, meaning than Betty won't get a seat
        - it's with Bob and Betty, then Henry and Louise dont get seats
        - worse case: 2 attendees are out
    - if Bob gets a seat, then either:
        - it's with Betty, meaning Henry won't get a seat
        - it's with Henry and Fred, meaning neither Betty nor Wilma will get a seat
        - worst case: 2 attendees are out
    - if Fred gets a seat, then either:
        - it's with Wilma, meaning Henry won't get a seat
        - it's with Bob and Henry, meaning neither Betty nor Wilma will get a seat
        -         
    - taking the cases above one at a time, it seems we want to discard the meeting
        [Bob, Henry, Fred]; however, taking them both together 
    - also note that we came up with the same evidence from looking from the perspectives
        of either the double booked attendees or the single booked attendees 

How about a graph of each meeting to the set of conflicting meeting:

Example:

1.  In the case of [[Bob, Betty], [Bob, Henry, Fred], [Fred, Wilma]]:
    - [Bob, Betty]: (Bob)
        - Bob => [[Bob, Henry, Fred]]: (Fred) -> Henry loses a seat
        - Fred => [[Fred, Wilma]]: () -> Wilma gets a seat

How about using the number of non double booked attendees as a clue?

Example:

1.  In the case of [[Bob, Betty], [Bob, Henry, Fred], [Fred, Wilma]]:
    - [Bob, Betty]: (Bob) -> 1
    - [Bob, Henry, Fred]: (Bob, Fred) -> 1
    - [Fred, Wilma]: (Fred) -> 1
