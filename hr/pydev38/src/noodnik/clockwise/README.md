# Event Packing	

## Duration
~90 minutes (please make sure to send across your code around the 90 minute mark).

Feel free to choose any language you'd like and use any resources/documentation/etc. that don't directly pertain to the specific problem asked (e.g. anything outside of googling a potential solution :)).

## Motivation
At Clockwise, we try to cluster meetings together across a team to
maximize the amount of heads-down focus time for everyone.

To do so, we have a set of algorithms which suggest alternate positions for
meetings. 

One such algorithm identifies the period of time that is the most collectively 
wasted across an entire team and attempts to move as many meetings as possible (chosen from a set 
of 'moveable' meetings) into that time slot.

Leaving aside the notion of finding 'wasted time' or identifying which meetings can 
'move', this question asks how to identify which combinations of meetings we should
suggest to move into a given time slot to reduce as much wasted time as possible.

## Question  
Given a list of meetings, each of which has a list of attendees, select a subset
of those meetings which:
  1. Must not double book any attendee
  2. Maximizes the total number of attendees across the subset
   
For input, we'll represent attendees as integers and meetings as arrays of integers:
* Meeting between three attendees: `[0,1,2]`

And the list of meetings as an array of arrays:
* Several meetings between four attendees: `[[0,1,2],[2,3],[1,3]]`

For output, we want the selected meetings as well as the count of involved attendees 
(higher is better).

## Examples
Taking the larger of two meetings:
* `[[0,1,2],[1,2]]` => `[[0,1,2]]` which has `3` attendees

One large or two small meetings are equivalent:
* `[[0,1],[0],[1]]` => either `[[0,1]]` or `[[0],[1]]` which both have `2` attendees

One attendee (`3`) loses out because another (`2`) can't be double booked:
* `[[0,1,2],[2,3]` => `[[0,1,2]]` which has `3` attendees  

## Caveats
We're not looking for a perfect solution (especially for large sets of meetings); rather, 
you should first optimize for finding *some* subset of meetings (with no double bookings), and then if possible
iteratively improve the solution. 

## Meetings to Solve
Because how the problem is solved depends on the structure of the data, we're asking for
a solution which is targeted at *these* specific lists of meetings (which are taken from actual runs on an 
engineering organization of ~100 people). 

Since your solution could involve heuristics + trade-offs,
if you have time, please document why you choose a given approach for your solution!

Test1:
```
[[0,1],[0],[1]]
```

Test2:
```
[[0,1,2],[2,3]]
```

Test3:
```
[[4,10],[3,4,12],[0,8,9,10,13],[1,5,7],[2,6],[9,4,10,11,12],[11,13]]
```

Test4:
```
[[6,16,17],[8,9],[1],[7,14,9],[10,5],[2,7],[0,6,7,9],[10,11,5,13,15,16,17],[7,9],[5,9],[2,12,5,6,14,7,15,9],[10,5,14],[1,4,8],[1,3,9],[5]]
```
Test5:
```
[[9,16,34],[10,13,18,20,23,28,30,31,32],[4,7,8,11,16,17,19,25,29,36,37],[0,2,23,28],[1,3,7,8,14,15,19,24,25,26,32],[12,28],[16,21,24,33,34],[5,6,10,15,16,17,21,22,24,27,33,34,35]]
```

Test6:
```
[[55,2],[34,66,60],[15,9,12,82],[39,51,81],[65,69,70],[67,47,58,10,62],[30],[36,7],[0,16],[75,2,20,43],[37,38,44],[34,56,46,79],[26,11,72],[67,47],[16,50],[72],[12],[16,63],[60,18],[64,16],[16,63],[32],[34,16],[16,8],[16,8],[2,3,4,49,52,54,55,75,14,76,22,41,43],[57,30],[64,80,23],[64,69,30],[2,49],[48,16,53],[16,63],[5,16],[69,70],[34,65,69,38,6,30,17],[25,77,59,71,78,19,21,40,12,24],[34,16],[55,66,5,60],[27,14,5,46,76,49],[33,28,69],[25,77,59,71,78,19,40,21,12,24],[5,29],[32],[28,16],[2,14],[37,69,6,30,17,31,73],[32,1,42],[64,26,60,11],[16,13],[16,8],[34,16],[16,44],[9,83],[45,81],[46,76],[46,8,39,51],[8,54],[16,50],[74,81,53],[5,16],[61,23],[65,68,77,81],[74,35,37,6,17]]
```

Test7:
```
[[71,61,26],[72,75,76],[73,54,64,10,68],[88,56,69],[71,17,8],[18],[40,17,66,47,13],[40,62,52,88],[11,79,36,58,15],[17,67],[33],[38],[3,4,5,55,59,60,61,82,18,85,26,48,50],[17,84],[74,83,43],[71,89,27],[11,79,46,58],[34,33],[71,75,35],[66,14],[72,33],[75,76],[19,77,27],[40,72,75,44,7,35,20],[18,34],[71,17,33,67],[29,86,65,78,87,22,23,47,16,28],[38,37,27],[30,18,6,52,85,55],[79,28,58],[17,88,69],[76,23],[39,32,75],[29,86,65,78,87,22,47,23,16,28],[11,79,58],[21,88,51],[38],[61,88],[3,18],[39,0,24,23],[33,70],[42,75,7,35,20,37,80],[38,1,49],[31,12,88],[39,40,43],[33,56],[33,25],[33],[2,56],[52,85],[52,9,45,57],[53,33],[66,27],[63,88],[81,41,42,7,20]]
```