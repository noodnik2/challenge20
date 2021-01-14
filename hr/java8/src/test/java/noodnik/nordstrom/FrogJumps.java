package noodnik.nordstrom;

import java.io.PrintStream;

/*
 * A frog can hop and jump
 * hop = 1 units of movement
 * jump = 2 units of movement
 *
 * Given an integer 'n' print out the combinations of hops and jumps the frog
 * can take to get to 'n' units of distance
 *
 * ex: a valid answer for n=4
 * hop hop hop hop
 * jump jump
 * hop jump hop
 */
public class FrogJumps {

    public static void main(String[] args) {
        printCombinations(7);
    }

    /**

         return a list of lists - each inner list is teh sequence of movements

         one of the answers is all hops (since hops is 1)    int[2] <a hops, b jumps>
         at the other end os all jumps plus {0,1} hops
         and everything in between

         don't need to ever expand the list of types of jumps

         1 <= n <= Integer.MAX_VALUE

         since a movement can be modeled as a bit, allow for that memory saving
         in the future, for today, will use a boolean

        describe tests that could / should be written, but no need to write them

     */
    private static void printCombinations(int n) {

        // n = 7;
        // 3 jumps, 1 hop => final
        // 2 jumps, 3 hops => final-1
        // 1 jump, 5 hops
        // 0 jumps, 7 hops

        for (int jumps = n / 2; jumps >= 0; jumps--) {
            printMovements(jumps, n - jumps * 2);
        }

    }

    private static void printMovements(int jumps, int hops) {
        final PrintStream outputStream = System.out;
        for (int jump = 0; jump < jumps; jump++) {
            outputStream.print("jump ");
        }
        for (int hop = 0; hop < hops; hop++) {
            outputStream.print("hop ");
        }
        outputStream.println();
    }

}