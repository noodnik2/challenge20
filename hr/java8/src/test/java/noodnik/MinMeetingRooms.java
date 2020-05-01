package noodnik;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import org.junit.Test;

// Ode to Palas
public class MinMeetingRooms {

	@Test
	public void testCase1() {
		assertCorrectMeetingRooms(
			new LeetcodeMinMeetingRoomCalculatorAlt(), 
			2, 
			new int[][] {
				{ 1, 2 },
				{ 1, 3 },
				{ 3, 6 }
			}
		);
	}

	@Test
	public void testCase2() {
		assertCorrectMeetingRooms(
			new LeetcodeMinMeetingRoomCalculatorAlt(), 
			2, 
			new int[][] {
				{ 2, 15 },
				{ 36, 45 },
				{ 9, 29 },
				{ 16, 23 },
				{ 4, 9 }
			}
		);
	}

	interface MinMeetingRoomCalculator {
		int minMeetingRooms(int[][] intervals);
	}

	static class LeetcodeMinMeetingRoomCalculatorAlt implements MinMeetingRoomCalculator {

		public int minMeetingRooms(int[][] intervals) {
			
			Arrays.sort(intervals, comparing((int[] interval) -> interval[0]));
			PriorityQueue<Integer> heap = new PriorityQueue<>();
			
			for (int[] interval : intervals) {
				if (!heap.isEmpty() && interval[0] >= heap.peek()) {
					heap.poll();
				}
				heap.offer(interval[1]);
			}
			
			return heap.size();
		}
		
	}
	
	static class LeetcodeMinMeetingRoomCalculator implements MinMeetingRoomCalculator {

		// see: https://www.programcreek.com/2014/05/leetcode-meeting-rooms-ii-java/
		public int minMeetingRooms(int[][] intervals) {
			Arrays.sort(intervals, Comparator.comparing((int[] interval) -> interval[0]));
			PriorityQueue<Integer> heap = new PriorityQueue<>();
			int count = 0;
			for (int[] interval : intervals) {
				if (heap.isEmpty()) {
					count++;
					heap.offer(interval[1]);
				} else {
					if (interval[0] >= heap.peek()) {
						heap.poll();
					} else {
						count++;
					}
					heap.offer(interval[1]);
				}
			}
			return count;
		}
		
	}
	
	static void assertCorrectMeetingRooms(
		MinMeetingRoomCalculator mmc,
		int expectedCount, 
		int[][] intervals
	) {
		assertEquals(expectedCount, mmc.minMeetingRooms(intervals));
	}
	
}
