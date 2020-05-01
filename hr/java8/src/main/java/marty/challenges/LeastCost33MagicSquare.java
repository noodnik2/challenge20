//package marty.challenges;
//
//import static java.lang.Math.abs;
//import static java.lang.Math.min;
//import static org.junit.Assert.assertEquals;
//
//public class LeastCost33MagicSquare {
//
//    public static void main(String[] args) {
//        final int[][] square0 = { // => 7
//            {5, 3, 4},
//            {1, 5, 8},
//            {6, 4, 2}
//        };
//        final int[][] square1 = { // => 1
//            {4, 9, 2},
//            {3, 5, 7},
//            {8, 1, 5}
//        };
//        final int[][] square2 = { // => 4
//            {4, 8, 2},
//            {4, 5, 7},
//            {6, 1, 6}
//        };
//
//        assertEquals(7, formingMagicSquare(square0));
//        assertEquals(1, formingMagicSquare(square1));
//        assertEquals(4, formingMagicSquare(square2));
//    }
//
//    static int formingMagicSquare(int[][] s) {
//        int magicSquares[][] = {
//            {4,9,2,3,5,7,8,1,6},
//            {4,3,8,9,5,1,2,7,6},
//            {2,9,4,7,5,3,6,1,8},
//            {2,7,6,9,5,1,4,3,8},
//            {8,1,6,3,5,7,4,9,2},
//            {8,3,4,1,5,9,6,7,2},
//            {6,7,2,1,5,9,8,3,4},
//            {6,1,8,7,5,3,2,9,4},
//        };
//
//        int minCost = Integer.MAX_VALUE;
//        for (final int[] magicSquare : magicSquares) {
//            final int costForThisSquare = (
//                abs(s[0][0] - magicSquare[0])
//              + abs(s[0][1] - magicSquare[1])
//              + abs(s[0][2] - magicSquare[2])
//              + abs(s[1][0] - magicSquare[3])
//              + abs(s[1][1] - magicSquare[4])
//              + abs(s[1][2] - magicSquare[5])
//              + abs(s[2][0] - magicSquare[6])
//              + abs(s[2][1] - magicSquare[7])
//              + abs(s[2][2] - magicSquare[8])
//            );
//
//            minCost = min(costForThisSquare, minCost);
//        }
//        return minCost;
//    }
//
//}
