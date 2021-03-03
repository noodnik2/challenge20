package noodnik.weave;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.Test;

import static noodnik.lib.Common.listOf;
import static org.junit.Assert.assertEquals;

/**
 *
 *  Weave is building a Customer Data Platform for which we need to
 *  ingest data from disparate datasources.  Unfortunately there are
 *  a lot of rows that actually represent the same users.  A user is
 *  considered the same user if at least one of the phone numbers or
 *  email addresses in the set are the same.  Your task is to ensure
 *  the data are clean and generate output that has one row per user
 *  with all of their phone numbers and email addresses.  (Ignore
 *  case for name of the user, assume phone numbers are always in
 *  xxx-xxx-xxxx format and e-mail addresses are already formatted
 *  correctly).
 *
 */
public class CustomerDataPlatformTester {

    @Test
    public void testOne() {
        /**
         *  As you can observe, Tom is merged into one element and duplicate is removed.
         *  We still have two rows for 2 different Seans as there are no matching phone
         *  numbers.  The output is ordered alphabetically, as are the phone numbers and
         *  e-mail addresses.
         */
        assertCorrectPrefixesFound(
            new BruteForceDuplicateRemover(),
            listOf(
                "\"JANE\", \"415-454-3232\"",
                "\"SEAN\", \"408-121-3232\", \"415-454-3232\"",
//                NOTE: below was originally noted as the expected output for SEAN; however,
//                      it seems the above is correct, to meet the "sorted" requirement.
//                "\"SEAN\", \"415-454-3232\", \"408-121-3232\"",
                "\"SEAN\", \"508-112-2233\"",
                "\"TOM\", \"402-333-4444\", \"510-900-0000\", \"510-900-0001\", \"510-910-0000\", \"510-920-0001\", \"tom@gmail.com\", \"tom@msn.com\""
            ),
            listOf(
                "\"Tom\", \"402-333-4444\", \"510-900-0000\", \"510-900-0001\", \"tom@msn.com\"",
                "\"Jane\", \"415-454-3232\"",
                "\"Sean\", \"508-112-2233\"",
                "\"Sean\", \"415-454-3232\", \"408-121-3232\"",
                "\"tom\", \"402-333-4444\", \"510-920-0001\", \"510-910-0000\"",
                "\"TOM\", \"402-333-4444\", \"tom@gmail.com\""
            )
        );
    }

    private void assertCorrectPrefixesFound(
        DuplicateRemover duplicateRemover,
        List<String> expectedResults,
        List<String> inputData
    ) {
        assertEquals(
            expectedResults,
            duplicateRemover.removeDuplicatesAndMerge(inputData)
        );
    }

    interface DuplicateRemover {
        List<String> removeDuplicatesAndMerge(List<String> users);
    }

    class BruteForceDuplicateRemover implements DuplicateRemover {
        public List<String> removeDuplicatesAndMerge(List<String> users) {
            List<List<String>> existingUserDb = new ArrayList<>();
            for (String user : users) {
                List<String> userTokens = parseCsv(user);
                boolean wasMerged = false;
                for (List<String> existingUserTokens : existingUserDb) {
                    if (isSameUser(existingUserTokens, userTokens)) {
                        List<String> newUserTokens = mergeAndSort(existingUserTokens, userTokens);
                        existingUserTokens.clear();
                        existingUserTokens.addAll(newUserTokens);
                        wasMerged = true;
                        break;
                    }
                }
                if (!wasMerged) {
                    existingUserDb.add(mergeAndSort(userTokens, userTokens));
                }
            }
            return buildCsv(
                existingUserDb
                .stream()
                .sorted(
                    (o1, o2) -> (   // order output lines lexicographically
                        IntStream.range(0, o1.size())
                        .map(i -> o1.get(i).compareTo(o2.get(i)))
                        .filter(value -> value != 0)
                        .findFirst()
                        .orElse(0)
                    )
                )
                .collect(Collectors.toList())
            );
        }

        private List<String> mergeAndSort(List<String> existingUserTokens, List<String> userTokens) {
            return (
                Stream
                .concat(
                    Stream.of(existingUserTokens.get(0).toUpperCase()),
                    Stream
                    .concat(
                        existingUserTokens.subList(1, existingUserTokens.size()).stream(),
                        userTokens.subList(1, userTokens.size()).stream()
                    )
                    .collect(Collectors.toSet()).stream()
                    .sorted()
                )
                .collect(Collectors.toList())
            );
        }

        private boolean isSameUser(List<String> existingUserTokens, List<String> userTokens) {
            if (!existingUserTokens.get(0).equalsIgnoreCase(userTokens.get(0))) {
                return false;
            }
            return (
                existingUserTokens
                .stream()
                .distinct()
                .anyMatch(userTokens::contains)
            );
        }

        private List<String> buildCsv(List<List<String>> existingUserDb) {
            List<String> usersCsv = new ArrayList<>(existingUserDb.size());
            for (List<String> existingUsersTokens : existingUserDb) {
                usersCsv.add(
                    existingUsersTokens
                    .stream()
                    .map(l -> "\"" + l + "\"")
                    .collect(Collectors.joining(", "))
                );
            }
            return usersCsv;
        }

        List<String> parseCsv(String csv) {
            return (
                Arrays.stream(csv.split(","))
                .map(l -> l.replaceAll("[\" ]", ""))
                .collect(Collectors.toList())
            );
        }

    }

}
