package game.model;

import java.util.ArrayList;

public class Discard {
    private ArrayList<Card> cards;

    // CONSTRUCTORS
    public Discard() {
        this.cards = new ArrayList<>();
    }


    /* GETTERS AND SETTERS */

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }


    /* METHODS */
    
    // Returns the top card of the discard pile (the last card in the list) without removing it. Returns null if the discard pile is empty.
    public Card getTopCard() {
        if (!this.cards.isEmpty()) {
            return this.cards.get(this.cards.size() - 1);
        }
        return null; // No cards in discard pile
    }

    // Adds a card to the top of the discard pile (the end of the list)
    public void addCard(Card card) {
        this.cards.add(card);
    }
}
