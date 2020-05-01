//package noodnik;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.Arrays;
//
//import org.junit.Test;
//
//import marty.lib.Common;
//
//import static marty.lib.Common.*;
//
//// https://www.hackerrank.com/challenges/new-year-chaos
//
//public class NewYearChaos {
//
//	
//	int[] testQueueNoSwap = { 1, 2, 3, 4, 5 };
//	int[] testQueueOneSwap = { 1, 3, 2, 4, 5 };
//	int[] testQueueTwoSwap = { 3, 1, 2, 4, 5 };
//	int[] testQueueM0 = { 1, 2, 5, 3, 4 };
//	int[] testQueueEx1 = { 2, 1, 5, 3, 4 };
//	int[] testQueueEx2 = { 2, 5, 1, 3, 4 };
//	
//	int[] testCase1a = { 5, 1, 2, 3, 7, 8, 6, 4 };
//
//	int[] testCase1b0 = { 1, 2, 3, 4, 5, 6, 7, 8 }; // start
//	int[] testCase1b1 = { 1, 2, 3, 5, 4, 6, 7, 8 };	// 5
//	int[] testCase1b7 = { 1, 2, 5, 3, 4, 6, 7, 8 };	// 5
//	int[] testCase1b2 = { 1, 2, 5, 3, 4, 7, 6, 8 };	// 7
//	int[] testCase1b3 = { 1, 2, 5, 3, 7, 4, 6, 8 };	// 7
//	int[] testCase1b4 = { 1, 2, 5, 3, 7, 4, 8, 6 };	// 8
//	int[] testCase1b5 = { 1, 2, 5, 3, 7, 8, 4, 6 };	// 8
//	int[] testCase1b6 = { 1, 2, 5, 3, 7, 8, 6, 4 };	// 6
//	
//	int[] testCase1b  = { 1, 2, 5, 3, 7, 8, 6, 4 };
//	
//	// swapForward Algo
//	// bribeCount = 0
//	// for position = n to 1:
//	//     while label[position] != position:
//	//         // finds label backwards from position
//	//         positionOfLabel = findBackwards(position) 
//	//         swap(positionOfLabel, positionOfLabel+1)
//	//         bribeCount++
//	// return bribeCount    
//
//	int[][] testSwapForwardAlgo  = {
//		{ 1, 2, 3, 4, 5, 6, 7, 8 },
//		{ 1, 2, 3, 5, 4, 6, 7, 8 }, // 5
//		{ 1, 2, 3, 5, 6, 4, 7, 8 }, // 6
//		{ 1, 2, 3, 5, 6, 7, 4, 8 }, // 7
//		{ 1, 2, 3, 5, 6, 7, 8, 4 }, // 8
//		{ 1, 2, 3, 5, 7, 6, 8, 4 }, // 7
//		{ 1, 2, 3, 5, 7, 8, 6, 4 }, // 8
//		{ 1, 2, 5, 3, 7, 8, 6, 4 }, // 5
//	};
//	
//	// swapBackward Algo
//	// bribeCount = 0
//	// for position = n to 1:
//	//     while label[position] != position:
//	//         // finds label backwards from position
//	//         positionOfLabel = findBackwards(position)
//	//         swap(positionOfLabel, positionOfLabel+1)
//	//         bribeCount++
//	// return bribeCount    
//
//	int[][] testSwapBackwardRightToLeftAlgo  = {
//			{ 1, 2, 5, 3, 7, 8, 6, 4 },
//			{ 1, 2, 5, 3, 7, 6, 8, 4 }, // 6
//			{ 1, 2, 5, 3, 7, 6, 4, 8 }, // 4
//			{ 1, 2, 5, 3, 6, 7, 4, 8 }, // 6
//			{ 1, 2, 5, 3, 6, 4, 7, 8 }, // 4
//			{ 1, 2, 5, 3, 4, 6, 7, 8 }, // 4 !!
//			{ 1, 2, 5, 3, 4, 6, 7, 8 }, // 4 !!
//	};
//	
//
//	int[][] testSwapBackwardLeftToRightAlgo  = {
//			{ 1, 2, 5, 3, 7, 8, 6, 4 },
//			{ 1, 2, 3, 5, 7, 8, 6, 4 }, // 3
//			{ 1, 2, 3, 5, 7, 8, 6, 4 }, // 3
//	};
//	
//	int[] testCase1d  = { 1, 2, 3, 4, 5, 6, 7, 8 };
//	int[] testCase1e  = { 0, 0, 2, 0, 2, 2, 0, 0 };
//
//	int[] testCase1f  = { 0, 0, 2,-1, 2, 2,-1,-4 };
//
//	
//	interface MinBribeCalculator {
//		int calcMinimumBribes(int[] q);
//	}
//	
//	
//	@Test
//	public void testOne() {
//		
////		MinBribeCalculator mbc = new MinBribeCalcTry1();
////		MinBribeCalculator mbc = new MinBribeCalcTry2();
//		MinBribeCalculator mbc = new MinBribeCalcTry3();
//
//		assertBribes(0, testQueueNoSwap, mbc);
//		assertBribes(1, testQueueOneSwap, mbc);
//		assertBribes(2, testQueueTwoSwap, mbc);
//
//		assertBribes(2, testQueueM0, mbc);
//
//		assertBribes(3, testQueueEx1, mbc);
//		assertBribes(-1, testQueueEx2, mbc);
//		
//		assertBribes(-1, testCase1a, mbc);
//		assertBribes(7, testCase1b, mbc);
//
//	}
//	
//	void assertBribes(int i, int[] q, MinBribeCalculator mbc) {		
//		assertEquals(i, mbc.calcMinimumBribes(q));
//	}
//
//	static void minimumBribes(int[] q, MinBribeCalculator mbc) {
//		int minBribes = mbc.calcMinimumBribes(q);
//		System.out.println(minBribes >= 0 ? "" + minBribes : "Too chaotic");	
//    }
//	
//	
//	static class MinBribeCalcTry3 implements MinBribeCalculator {
//
//		// "what's the relationship between a position and a label?"
//		
//		@Override
//		public int calcMinimumBribes(int[] q) {
//			int bribeCount = 0;
//			for (int position = 1; position < q.length; position++) {
//				int labelAtPosition = labelAt(q, position);
//				if (labelAtPosition != position) {
//					int bribes = labelAtPosition - position;
//					if (bribes < 0) {
//						continue;
//					}
//					if (bribes > 2) {
//						return -1;
//					}
//					bribeCount += bribes;
//				}
//			}
//			return bribeCount;
//		}
//	}
//	
//	static class MinBribeCalcTry2 implements MinBribeCalculator {
//
//		@Override
//		public int calcMinimumBribes(int[] q) {
//			int bribeCount = 0;
//			for (int position = 1; position < q.length; position++) {
//				int labelAtPosition = labelAt(q, position);
//				if (labelAtPosition != position) {
//					int bribes = labelAtPosition - labelAt(q, position + 1);
//					log("pos(%s) bribes(%s)", position, bribes);
//					if (bribes < 0) {
//						continue;
//					}
//					if (bribes > 2) {
//						return -1;
//					}
//					position += 1;
//					bribeCount += bribes;
//				}
//			}
//			log("for(%s) swaps(%s)", listOf(q), bribeCount);
//			return bribeCount;
//		}
//		
//	}
//
//	static class MinBribeCalcTry1 implements MinBribeCalculator {
//
//		@Override
//		public int calcMinimumBribes(int[] q) {
//			int bribeCount = 0;
//			for (int position = 1; position <= q.length; position++) {
//				if (labelAt(q, position) == position + 1 && labelAt(q, position + 1) == position) {
//					bribeCount++;
//					continue;
//				}
//				if (labelAt(q, position) == position + 2 && labelAt(q, position + 1) == position) {
//					bribeCount++;
//					continue;
//				}
//			}
//			log("for(%s) swaps(%s)", listOf(q), bribeCount);
//			return bribeCount;
//		}
//		
//	}
//	/**
//	 * 
//	 * 	Cases:
//	 * 
//	 *  1. no swap
//	 *     - label[position] == position
//	 * 	1. single swap
//	 *     - label[position] == position + 1 && label[position + 1] = position 
//	 *  1. double swap
//	 *     - label[position] == position + 2 && label[position + 1] = position
//	 * 
//	 */
//	
//	static int labelAt(int[] q, int position) {
//		return q[position - 1];
//	}
// 
//	
//}
