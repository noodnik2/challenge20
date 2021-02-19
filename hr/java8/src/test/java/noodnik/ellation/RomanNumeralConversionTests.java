package noodnik.ellation;

import org.junit.Test;

import static noodnik.lib.Common.log;
import static org.junit.Assert.assertEquals;

/**
  *
  *     Conversion To (and From) Roman Numerals
  *
  *     In the Roman numeral system, we have 7 symbols that represent numbers:
  *
  *     I represents 1
  *     V represents 5
  *     X represents 10
  *     L represents 50
  *     C represents 100
  *     D represents 500
  *     M represents 1000
  *
  *     Roman numerals use subtractive notation in some cases:
  *
  *     IV represents 4 (instead of IIII)
  *     IX represents 9
  *     XL represents 40
  *     XC represents 90
  *     CD represents 400
  *     CM represents 900
  *
  *     Write a function that converts an Arabic number < 4000 to Roman Numerals, or the reverse.
  *
  */

public class RomanNumeralConversionTests {

    RomanNumeralConverter romanNumeralConverter = new SubmittedRomanNumeralConverter();

    @Test
    public void ConversionTests() {
        assertBothWays("", 0);
        assertBothWays("I", 1);
        assertBothWays("XCIX", 99);
        assertBothWays("C", 100);
        assertBothWays("CCCXCVIII", 398);
        assertBothWays("CCCXCIX", 399);
        assertBothWays("CD", 400);
        assertBothWays("CDI", 401);
        assertBothWays("D", 500);
        assertBothWays("DI", 501);
        assertBothWays("DII", 502);
        assertBothWays("DIII", 503);
        assertBothWays("DIV", 504);
        assertBothWays("DCCCXCIX", 899);
        assertBothWays("MMMCMXCIX", 3999);
        assertBothWays("MMMM", 4000);
    }

    void assertBothWays(String roman, int number) {
        log("testing(%s: '%s')", number, roman);
        assertEquals(roman, romanNumeralConverter.convert(number));
        assertEquals(romanNumeralConverter.convert(roman), number);
    }

    interface RomanNumeralProducer {
        String convert(int i);
    }

    interface RomanNumeralConsumer {
        int convert(String romanNumeral);
    }

    interface RomanNumeralConverter extends RomanNumeralProducer, RomanNumeralConsumer {

    }

    static class SubmittedRomanNumeralConverter implements RomanNumeralConverter {

        static class ConversionTableEntry {
            ConversionTableEntry(String roman, int value) {
                this.value = value;
                this.roman = roman;
            }
            int value;
            String roman;
        }

        static ConversionTableEntry[] conversionTable = new ConversionTableEntry[] {
            new ConversionTableEntry("I",1),
            new ConversionTableEntry("IV",4),
            new ConversionTableEntry("V",5),
            new ConversionTableEntry("IX",9),
            new ConversionTableEntry("X",10),
            new ConversionTableEntry("XL",40),
            new ConversionTableEntry("L",50),
            new ConversionTableEntry("XC",90),
            new ConversionTableEntry("C",100),
            new ConversionTableEntry("CD",400),
            new ConversionTableEntry("D",500),
            new ConversionTableEntry("CM",900),
            new ConversionTableEntry("M",1000)
        };

        public int convert(String romanNumeral) {
            int v = 0;
            for (int i = 0; i < romanNumeral.length();) {
                ConversionTableEntry matchingEntry = lookupFirstEntry(romanNumeral.substring(i));
                v += matchingEntry.value;
                i += matchingEntry.roman.length();
            }
            return v;
        }

        ConversionTableEntry lookupFirstEntry(String romanNumeral) {
            ConversionTableEntry bestEntry = null;
            for (ConversionTableEntry entry : conversionTable) {
                if (romanNumeral.startsWith(entry.roman)) {
                    if (bestEntry == null || bestEntry.roman.length() < entry.roman.length()) {
                        bestEntry = entry;
                    }
                }
            }
            return bestEntry;
        }

        public String convert(int n) {
            StringBuilder sb = new StringBuilder();
            for (int i = conversionTable.length - 1; i >= 0 && n != 0; i--) {
                int targetValue = conversionTable[i].value;
                int v = n / targetValue;
                if (v == 0) {
                    // lower than the base; try again
                    continue;
                }
                // v is the number of "base" values we need to add
                String romanEquivalent = conversionTable[i].roman;
                sb.append(String.valueOf(romanEquivalent).repeat(Math.max(0, v)));
                n = n % targetValue;
            }
            return sb.toString();
        }

    }

}
