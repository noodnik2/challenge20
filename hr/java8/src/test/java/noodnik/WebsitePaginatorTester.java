package noodnik;

import org.junit.Test;

import static java.lang.Integer.parseInt;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class WebsitePaginatorTester {

	@Test
	public void websitePagination() {
	
		WebsitePaginator wp = new WebsitePaginatorTry0();
		assertCorrectPagination(
			wp, 
			asList("item3"), 
			asList(
				asList("item1", "10", "15"),
				asList("item2", "3", "4"),
				asList("item3", "17", "8")
			),
			1,
			0,
			2,
			1
		);
		
	}
	
	interface WebsitePaginator {
		
	    /*
	     * Complete the 'fetchItemsToDisplay' function below.
	     *
	     * The function is expected to return a STRING_ARRAY.
	     * The function accepts following parameters:
	     *  1. 2D_STRING_ARRAY items
	     *  2. INTEGER sortParameter
	     *  3. INTEGER sortOrder
	     *  4. INTEGER itemsPerPage
	     *  5. INTEGER pageNumber
	     */
	    List<String> fetchItemsToDisplay(
    		List<List<String>> items, 
    		int sortParameter, 
    		int sortOrder, 
    		int itemsPerPage, 
    		int pageNumber
		);

	}
	
	static class WebsitePaginatorTry0 implements WebsitePaginator {

		static Comparator<List<String>> newIntegerColumnComparator(int integerColumnIndex, int sortOrder) {
			return new Comparator<List<String>>() {
				public int compare(List<String> o1, List<String> o2) {
					Integer r1 = parseInt(o1.get(integerColumnIndex));
					Integer r2 = parseInt(o2.get(integerColumnIndex));
					return sortOrder == 0 ? r1.compareTo(r2) : r2.compareTo(r1);
				}
			};
		}

		static Comparator<List<String>> newStringColumnComparator(int stringColumnIndex, int sortOrder) {
			return new Comparator<List<String>>() {
				public int compare(List<String> o1, List<String> o2) {
					String s1 = o1.get(stringColumnIndex);
					String s2 = o2.get(stringColumnIndex);
					return sortOrder == 0 ? s1.compareTo(s2) : s2.compareTo(s1);
				}		
			};
		}
		
		public List<String> fetchItemsToDisplay(
			List<List<String>> items, 
			int sortParameter, 
			int sortOrder,
			int itemsPerPage, 
			int pageNumber
		) {
			
			Comparator<List<String>> sortComparator = (
				(sortParameter == 0)
			  ? newStringColumnComparator(sortParameter, sortOrder)
			  : newIntegerColumnComparator(sortParameter, sortOrder)
			);
			
			int pageOffset = pageNumber * itemsPerPage;

			return (
				items
				.stream()
				.sorted(sortComparator)
				.collect(Collectors.toList())
				.stream()
				.skip(pageOffset)
				.limit(itemsPerPage)
				.map(pageItem -> pageItem.get(0))
				.collect(Collectors.toList())
			);		
			
		}
		
	}
	
	static void assertCorrectPagination(
		WebsitePaginator wp,
		List<String> expectedAnswer,
		List<List<String>> testInput,
		int sortParameter, 
		int sortOrder, 
		int itemsPerPage, 
		int pageNumber
	) {
		List<String> actualAnswer = wp.fetchItemsToDisplay(testInput, sortParameter, sortOrder, itemsPerPage, pageNumber);
		assertEquals(expectedAnswer, actualAnswer);
	}

}
