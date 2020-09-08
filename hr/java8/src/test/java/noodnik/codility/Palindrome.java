package noodnik.codility;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Palindrome {

    /**
     *  Given string S of length N,
     *  return palindrome obtained by replacing all question marks in S
     *  by lowercase letters ('a'-'z').  If none possible, return "NO"
     *
     *  N is 1..1,000
     *  S contains only ['a'..'z', '?']
     *
     *  Focus on correctness, not performance
     */

    static
    class Solution {

        public String solution(final String s) {

            final int length = s.length();
            final char[] result = new char[length];

            final int halfLength = length / 2;
            for (int i = 0; i < halfLength; i++) {

                final int rightIndex = length - i - 1;
                char left = s.charAt(i);
                char right = s.charAt(rightIndex);

                if (left == '?') {
                    if (right == '?') {
                        right = 'z';
                    }
                    left = right;
                } else if (right == '?') {
                    right = left;
                }

                if (right != left) {
                    return "NO";
                }

                result[i] = left;
                result[rightIndex] = right;
            }

            if ((length % 2) == 1) {
                final char middle = s.charAt(halfLength);
                result[halfLength] = middle == '?' ? 'a' : middle;
            }

            return new String(result);
        }

    }

    @Test
    public void testCase1() {
        assertEquals("aabbaa", new Solution().solution("?ab??a"));
    }

    @Test
    public void testCase2() {
        assertEquals("NO", new Solution().solution("bab??a"));
    }

    @Test
    public void testCase3() {
        assertEquals("zaz", new Solution().solution("?a?"));
    }

}
