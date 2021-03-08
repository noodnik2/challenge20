package noodnik.mark43;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 *  You are given a collection of person objects (in a natural manner for the
 *  language you're writing in) with three fields:
 *
 *  {firstName, lastName, age} e.g. {Jane, Smith, 14}
 *
 *  We want to know which family has the longest line of "juniors".
 *  A family has "juniors" when multiple family members have the same full name.
 *
 *  Consider the following example.
 *  [
 *      {firstName: "John",  lastName: "Doe",   age: 13},
 *      {firstName: "John",  lastName: "Doe",   age: 32},
 *      {firstName: "John",  lastName: "Doe",   age: 62},
 *      {firstName: "Janet", lastName: "Doe",   age: 14},
 *      {firstName: "Jenny", lastName: "Smith", age: 34},
 *      {firstName: "Jenny", lastName: "Smith", age: 12},
 *      {firstName: "Jenny", lastName: "Smith", age: 22},
 *      {firstName: "Jenny", lastName: "Smith", age: 48},
 *      {firstName: "Nathan", lastName: "Bialas", age: 48},
 *      {firstName: "John", lastName: "Doe", age: 104},
 *  ]
 *
 *  In this example, the Doe family has the longest line of juniors.
 *
 *  Let’s talk about your plan of attack before you start coding, and remember
 *  to keep talking me through what you are doing as you code.
 */
public class JuniorCalcer {

    @Test
    public void simpleTestCase() {
        assertExpectedFamilyName(
            new SimpleJuniorCalculator(),
        "Doe",
            new Person[] {
                new Person("John",  "Doe",   13),
                new Person("John",  "Doe",   32),
                new Person("John",  "Doe",   62),
                new Person("Janet", "Doe",   14),
                new Person("Jenny", "Smith", 34),
                new Person("Jenny", "Smith", 12),
            }
        );
    }

    interface JuniorCalculator {
        void addPerson(Person person);
        String familyWithMostJuniors();
    }

    static void assertExpectedFamilyName(
        JuniorCalculator juniorCalculator,
        String expectedFamilyName,
        Person[] people
    ) {
        for (Person person : people) {
            juniorCalculator.addPerson(person);
        }
        assertEquals(expectedFamilyName, juniorCalculator.familyWithMostJuniors());
    }

    static class Person {
        Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }
        String firstName;
        String lastName;
        int age;
    }

    static class SimpleJuniorCalculator implements JuniorCalculator {

        Map<String, Integer> fullNamePopulation = new HashMap<>();
        String mostJuniorsFamily;
        int mostJuniorsFamilCount;
        // familyName => members
        // fullName => f

        public void addPerson(Person person) {
            String fullName = person.lastName + person.firstName;
            Integer existingPopulation = fullNamePopulation.get(fullName);
            int newPopulation;
            if (existingPopulation == null) {
                fullNamePopulation.put(fullName, 1);
                newPopulation = 1;
            } else {
                newPopulation = existingPopulation + 1;
                fullNamePopulation.put(fullName, newPopulation);
            }
            if (newPopulation > mostJuniorsFamilCount) {
                mostJuniorsFamily = person.lastName;
                mostJuniorsFamilCount = newPopulation;
            }
        }

        public String familyWithMostJuniors() {
            return mostJuniorsFamily;
        }

    }

}
