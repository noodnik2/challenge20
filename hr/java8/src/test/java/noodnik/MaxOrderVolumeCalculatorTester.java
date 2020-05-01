package noodnik;

import org.junit.Test;

import static java.util.Comparator.comparing;
import static noodnik.lib.Common.*;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class MaxOrderVolumeCalculatorTester {
	
	@Test
	public void testCase1() {
		assertCorrectOrderVolume(
			76, 
			new int[][] {
				{ 10, 5, 15, 18, 30 }, 
				{ 30, 12, 20, 35, 35 }, 
				{ 50, 51, 20, 25, 10 }
			}
		);
	}

	@Test
	public void testCase2() {
		assertCorrectOrderVolume(
			4, 
			new int[][] {
				{ 1, 2, 4 }, 
				{ 2, 2, 1 }, 
				{ 1, 2, 3 }
			}
		);
	}

	@Test
	public void testCase3() {
		assertCorrectOrderVolume(
			111, 
			new int[][] {
				{ 1, 10, 100 }, 
				{ 1, 10, 100 }, 
				{ 1, 10, 100 }
			}
		);
	}
	
	interface OrderPlacementOptimizer {
		int maxOrderVolume(int[][] orderPlacementCalls);
	}

	static class StandardOrderPlacementOptimizer implements OrderPlacementOptimizer {


		public int maxOrderVolume(int[][] orderPlacementCalls) {
			
			// orderPlacementCall[0] is the starting time
			// orderPlacementCall[1] is the ending time
			// orderPlacementCall[1] is the volume of calls placed in that call
			Arrays.sort(orderPlacementCalls, comparing((int[] orderPlacementCall) -> orderPlacementCall[0]));
			
			// schedule[0] is the ending time for the most recent call within that schedule
			// schedule[1] is the cumulative volume of orders placed within that schedule
			PriorityQueue<int[]> heap = new PriorityQueue<>(comparing((int[] schedule) -> schedule[0]));
			
			for (int[] orderPlacementCall : orderPlacementCalls) {
				if (heap.isEmpty()) {
					heap.offer(new int[] { orderPlacementCall[1], orderPlacementCall[2] });
					continue;
				}
				int[] schedule = heap.peek();
				int volume = 0;
				if (orderPlacementCall[0] >= schedule[0]) {
					heap.poll();
					volume = schedule[1];
				}
				heap.offer(new int[] { orderPlacementCall[1], orderPlacementCall[2] + volume });
			}
			
//			heap.stream().forEach(schedule -> log("%s", listOf(schedule)));

			return (
				heap
				.stream()
				.map(s -> s[1])
				.max(Integer::compare)
				.orElse(0)
			);

		}
		
	}
	
	interface PhoneCallReceiver {
		
	    /*
	     * Complete the 'phoneCalls' function below.
	     *
	     * The function is expected to return an INTEGER.
	     * The function accepts following parameters:
	     *  1. INTEGER_ARRAY start
	     *  2. INTEGER_ARRAY duration
	     *  3. INTEGER_ARRAY volume
	     */
	    int phoneCalls(List<Integer> start, List<Integer> duration, List<Integer> volume);
	
	}
	
	static class PhoneCallOptimizer implements PhoneCallReceiver {

		public int phoneCalls(
			List<Integer> start, 
			List<Integer> duration, 
			List<Integer> volume
		) {
			assert start.size() == duration.size();
			assert duration.size() == volume.size();
			int[][] orderPlacementCalls = new int[start.size()][3];
			
			OrderPlacementOptimizer rc = new StandardOrderPlacementOptimizer();
			for (int index = 0; index < start.size(); index++) {
				orderPlacementCalls[index][0] = start.get(index);
				orderPlacementCalls[index][1] = start.get(index) + duration.get(index);
				orderPlacementCalls[index][2] = volume.get(index);
			}
			return rc.maxOrderVolume(orderPlacementCalls);
		}
		
	}
	
	static void assertCorrectOrderVolume(int expectedVolume, int[][] testData) {
		assertEquals(
			expectedVolume,
			new PhoneCallOptimizer().phoneCalls(
				listOf(testData[0]),	// start
				listOf(testData[1]), 	// duration
				listOf(testData[2])		// volume
			)
		);
	}
	
}
