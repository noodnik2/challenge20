package noodnik.twilio;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Hr4thBit {
	
	int sampleInput0 = 32;
	int sampleInput1 = 77;
	int sampleInput2 = -1;

	@Test
	public void testCase0() {
		assertCorrect(0, sampleInput0);
	}

	@Test
	public void testCase1() {
		assertCorrect(1, sampleInput1);
	}
	
	@Test
	public void testCase2() {
		assertCorrect(1, sampleInput1);
	}

	@Test
	public void testCaseMax() {
		assertCorrect(1, Integer.MAX_VALUE);
	}

	@Test
	public void testCaseMin() {
		assertCorrect(0, Integer.MIN_VALUE);
	}
	
	void assertCorrect(int expectedResult, int sampleInputValue) {
		assertEquals(expectedResult, fourthBit(sampleInputValue));
	}
	
	long fourthBit(int inputValue) {
		return (inputValue & 8) != 0 ? 1 : 0;
	}

}
