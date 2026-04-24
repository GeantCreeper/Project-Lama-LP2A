package game.model;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    // CONSTRUCTORS
    public Deck() {
        this.cards = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < 8; j++) {
                this.cards.add(new Card(i));
            }
        }
        for (int j = 0; j < 8; j++) {
            this.cards.add(new Card(7 + j)); // Add Lama cards
        }
    }


    /* GETTERS AND SETTERS */

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }


    /* METHODS */
    
    // Returns true if the deck is empty (no cards left to draw)
    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    // Shuffles the deck using Collections.shuffle and returns a new shuffled list of cards (without modifying the original deck)
    public ArrayList<Card> shuffle() {
        ArrayList<Card> shuffled = new ArrayList<>(this.cards);
        java.util.Collections.shuffle(shuffled);
        return shuffled;
    }

    // Draws a card from the top of the deck (removing it from the deck) and returns it. Returns null if the deck is empty.
    public Card drawCard() {
        if (!this.cards.isEmpty()) {
            return this.cards.remove(0);
        }
        return null; // No cards left to draw
    }
}
