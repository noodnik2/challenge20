// TODO move this to somewhere more appropriate
package noodnik.revinate;

import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *  Revinate technical interview code
 */
public class RevinateTechnicalIntCode {

    enum Suit {
      HEARTS,
      CLUBS,
      DIAMONDS,
      SPADES
    }

    interface Value {
      String display();
      int cardinal();
    }

    interface Card {
      Suit suit();
      Value value();
    }

    interface Hand {
      Set<Card> cards();
    }

    interface Deck<H extends Hand> {
        /**Map
         * @return {@code null} if deck empty
         */
        H drawHand();
    }

    enum PokerHandType {
      KIND2,
      KIND3
    }

    class PokerHand implements Hand {

        private final Set<Card> pokerCards;
        private final Set<PokerHandType> handTypes;

        PokerHand(Card... handCards) {
            pokerCards = new HashSet<>(Arrays.asList(handCards));
            final Map<Value, Integer> cvf = new HashMap<>();
            for (final Card card : pokerCards) {
                final Integer currentCount = cvf.get(card.value());
                if (currentCount == null) {
                    cvf.put(card.value(), 1);
                    continue;
                }
                cvf.put(card.value(), currentCount + 1);
            }
            handTypes = new HashSet<>();
            if (cvf.values().contains(2)) {
                handTypes.add(PokerHandType.KIND2);
            }
            if (cvf.values().contains(3)) {
                handTypes.add(PokerHandType.KIND3);
            }
        }

        public Set<Card> cards() {
            return pokerCards;
        }

        public String toString() {
            return pokerCards.toString();
        }

        boolean isFullHouse() {
            return handTypes.contains(PokerHandType.KIND2)
                && handTypes.contains(PokerHandType.KIND3);
        }

    }

    class StdCard implements Card {

        private final Suit mySuit;
        private final Value myValue;

        StdCard(Suit suit, Value value) {
            mySuit = suit;
            myValue = value;
        }

        public Suit suit() { return mySuit; }
        public Value value() { return myValue; }
        public String toString() { return myValue + "/" + mySuit; }
    }

    class StdValue implements Value {
        private final int myCardinal;
        StdValue(int cardinal) { myCardinal = cardinal; }
        public String display() { return String.valueOf(myCardinal); }
        public int cardinal() { return myCardinal; }
        public String toString() { return String.valueOf(myCardinal); }
        public int hashCode() { return myCardinal; }
        public boolean equals(Object other) { return myCardinal == ((StdValue) other).myCardinal; }
    }

    class PokerDeck implements Deck<PokerHand> {

        PokerDeck() {
            cards = new ArrayList<>(52);
            for (Suit suit : Suit.values()) {
                for (int i = 1; i <= 13; i++) {
                    cards.add(new StdCard(suit, new StdValue(i)));
                }
            }
            Collections.shuffle(cards);
        }

        public PokerHand drawHand() {
            final Iterator<Card> cardIterator = cards.iterator();
            final Card[] handCards = new Card[NCARDS];
            for (int i = 0; i < handCards.length; i++) {
                if (!cardIterator.hasNext()) {
                    return null;
                }
                handCards[i] = cardIterator.next();
                cardIterator.remove();	// card is not "in the deck" anymore
            }
            return new PokerHand(handCards);
        }

        private final List<Card> cards;
        private static final int NCARDS = 5;

    }

    /**
     *  @param log log to write to, or {@code null}
     *  @return number of hands it took to get a full house, or -1 if failed
     */
    int drawsToFullHouse(final PrintStream log) {
        PokerDeck pokerDeck = new PokerDeck();
        int decksUsed = 1;
        int handsDrawn = 0;

        do {
            PokerHand pokerHand = pokerDeck.drawHand();
            if (pokerHand == null) {
                // need a new deck
                pokerDeck = new PokerDeck();
                pokerHand = pokerDeck.drawHand();
                decksUsed++;
            }
            handsDrawn++;
            if (log != null) log.format("deck(%s), draw(%s), hand(%s)\n", decksUsed, handsDrawn, pokerHand);
            if (pokerHand.isFullHouse()) {
                if (log != null) log.format("yay! full house after %s hand(s), %s deck(s)\n", handsDrawn, decksUsed);
                return handsDrawn;
            }
        } while(handsDrawn < 10000);    // some "reasonable" maximum tries

        return -1;
    }

    @Test
    public void testCanDrawFullHouse() {
        System.out.println("drawing (and showing) hands until full house");
        assertTrue(drawsToFullHouse(System.out) > 0);
    }

    @Test
    public void testAverageFullHouseDraws() {
        System.out.println("calculating average draws until full house");
        int nDrawsTotal = 0;
        int nAttempts = 0;
        for (int i = 0; i < 10; i++) {
            final int nDraws = drawsToFullHouse(null);
            assertTrue(nDraws > 0);
            nDrawsTotal += nDraws;
            nAttempts++;
        }
        System.out.format("average draws to full house over %s attempts is %s\n", nAttempts, ((double) nDrawsTotal / (double) nAttempts));
    }

}
