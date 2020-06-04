package noodnik.twilio;

import org.junit.Test;

import static noodnik.lib.Common.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class HrMissingWords {
	
	/**
	 *
	 * 	Given two strings, one is a subsequence of all of the elements of the first string
	 *	occur in the same order within the second string.  They do not have to be contiguous
	 *	in the second string, but order must be maintained.  For example, given the string
	 *	'I like cheese', the words ('I', 'cheese') are one possible subsequence of that
	 *	string.  Words are space delimited.
	 *
	 *	Given two strings, s and t, where t is a subsequence of s, report the words of s,
	 *	missing in t (case sensitive), in the order they are missing.
	 *
	 */
	
	@Test
	public void testCase0() {
		assertCorrectResult("I cheese", "I like cheese", "like");
	}

	@Test
	public void testCase1() {
		assertCorrectResult("I using programming", "I am using HackerRank to improve programming", "am HackerRank to improve");
	}
	
	
	/**
	 * 	@param s sentence of space-separated words
	 * 	@param t sentence of space-separated words
	 * 	@return array of strings containing all words in s that are missing from s,
	 * 	in the order they occur within s
	 */
	List<String> missingWords(String s, String t) {
		List<String> sWords = words(s);
		List<String> tWords = words(t);
		List<String> missingWords = new ArrayList<>();
		int sIndex = 0;
		int tIndex = 0;
		while(sIndex < sWords.size() && tIndex < tWords.size()) {
			String sWord = sWords.get(sIndex);
			String tWord = tWords.get(tIndex);
			if (sWord.equals(tWord)) {
				sIndex++;
				tIndex++;
				continue;
			}
			missingWords.add(sWord);
			sIndex++;
		}
		while(sIndex < sWords.size()) {
			String sWord = sWords.get(sIndex++);
			missingWords.add(sWord);
		}
		while(tIndex < tWords.size()) {
			String tWord = tWords.get(tIndex++);
			missingWords.add(tWord);
		}
		return missingWords;
	}
	
	void assertCorrectResult(String expectedResult, String s, String t) {
		assertEquals(words(expectedResult), missingWords(s, t));
	}
	
	List<String> words(String s) {
		return listOf(s.split(" "));
	}
	

}
