package noodnik.agari;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static noodnik.lib.Common.log;

/**
 *  Play the game of "War"
 *  This variation simply awards the player all of the cards in the pile when they
 *  produce a card of greater face value (suits ignored) than the card on the top
 *  of the pile.
 */
public class War {

    @Test
    public void playWarGame() {
        final Game game = new Game(2);
        log("Winner(%s)", game.playGame());
    }

    private static class Card {
        static final int[] UNICODE_SUIT_CODEPOINT_BASE = { 0x1f0a1, 0x1f0b1, 0x1f0c1, 0x1f0d1  };
        static final int PIPS = 13;
        static final int SUITS = 4;
        int pip;    // A, 2-10, J, Q, K
        int suit;   // Spades, Hearts, Diamonds, Clubs
        Card(int pip, int suit) {
            if (pip < 0 || pip >= PIPS || suit < 0 || suit >= SUITS) {
                throw new IllegalArgumentException();
            }
            this.pip = pip;
            this.suit = suit;
        }
        public String toString() {
            return new String(Character.toChars(UNICODE_SUIT_CODEPOINT_BASE[suit] + pip));
        }
    }

    private static class Game {

        private final List<Card>[] playerHands;
        private final List<Card> pile = new LinkedList<>();

        public Game(final int nPlayers) {
            playerHands = new List[nPlayers];
        }

        public int playGame() {
            deal(shuffledDeck());
            for (int player = 0;; player = (player + 1) % playerHands.length) {
                playHand(player);
                final Integer[] activePlayers = activePlayers();
                if (activePlayers.length == 1) {
                    final int winner = activePlayers[0];
                    log("player(%s) wins; has(%s) cards", winner, playerHands[winner].size());
                    return winner;
                }
            }
        }

        Integer[] activePlayers() {
            final List<Integer> activePlayerList = new ArrayList<>(playerHands.length);
            for (int player = 0; player < playerHands.length; player++) {
                if (playerHands[player].size() > 0) {
                    activePlayerList.add(player);
                }
            }
            return activePlayerList.toArray(new Integer[0]);
        }

        Card[] shuffledDeck() {
            log("shuffling");
            final List<Card> cards = new ArrayList<>(52);
            for (int suit = 0; suit < Card.SUITS; suit++) {
                for (int pip = 0; pip < Card.PIPS; pip++) {
                    cards.add(new Card(pip, suit));
                }
            }
            Collections.shuffle(cards);
            return cards.toArray(new Card[0]);
        }

        void deal(final Card[] dealersDeck) {
            log("dealing");
            for (int p = 0; p < playerHands.length; p++) {
                playerHands[p] = new ArrayList<>();
            }
            for (int c = 0; c < dealersDeck.length; c++) {
                playerHands[c % playerHands.length].add(dealersDeck[c]);
            }
            this.pile.clear();
        }

        void playHand(final int player) {
            final Card card = playerHands[player].remove(0);
            log("player(%s) throws(%s)", player, card);
            pile.add(card);
            final int pileSize = pile.size();
            if (pileSize > 1) {
                final Card playedCard = pile.get(pileSize - 1);
                final Card previousCard = pile.get(pileSize - 2);
                if (acesHighValue(playedCard) >= acesHighValue(previousCard)) {
                    log(" ... and picks up pile(%s)", pile);
                    playerHands[player].addAll(pile);
                    pile.clear();
                }
            }
        }

        int acesHighValue(final Card card) {
            return card.pip == 0 ? 12 : card.pip - 1;
        }

    }

}
