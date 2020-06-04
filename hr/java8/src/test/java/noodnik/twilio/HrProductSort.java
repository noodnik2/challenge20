package noodnik.twilio;
import org.junit.Test;

import static noodnik.lib.Common.*;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class HrProductSort {
	
	// Given an array of n item values, sort the array in ascending order,
	// first by the number of items with a certain value, then by the values themselves
	
	@Test
	public void testCase0a() {
		assertCorrectResult(new int[] { 3, 6, 4, 4, 5, 5 }, new int[] { 4, 5, 6, 5, 4, 3 });
	}
	
	@Test
	public void testCas0() {
		assertCorrectResult(new int[] { 1, 3, 4, 2, 2 }, new int[] { 3, 1, 2, 2, 4 });
	}

	@Test
	public void testCase2() {
		assertCorrectResult(new int[] { 8, 4, 4, 1, 1, 1, 5, 5, 5, 5 }, new int[] { 8, 5, 5, 5, 5, 1, 1, 1, 4, 4 });
	}
	
	Comparator<Entry<Integer, Long>> comparator() {
		return (o1, o2) -> {

			if (o1.getValue() == 1 && o2.getValue() == 1) {
				return o1.getKey().compareTo(o2.getKey());
			}
			if (o1.getValue() == 1) {
				return -1;
			}
			if (o2.getValue() == 1) {
				return 1;
			}
			int quantityComparison = o1.getValue().compareTo(o2.getValue());
			if (quantityComparison != 0) {
				return quantityComparison;
			}
			int itemComparison = o1.getKey().compareTo(o2.getKey());
			if (itemComparison != 0) {
				return itemComparison;
			}
			return 0;			
		};
	}
	
	List<Integer> itemsSort(List<Integer> items) {
		
		return (
			items
			.stream()
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
			.entrySet()
			.stream()
			.sorted(comparator())
			.flatMap(e -> Collections.nCopies(e.getValue().intValue(), e.getKey()).stream())
			.collect(Collectors.toList())
		);

    }
	
	void assertCorrectResult(int[] expectedResult, int[] testItems) {
		assertEquals(listOf(expectedResult), itemsSort(listOf(testItems)));
	}

}
