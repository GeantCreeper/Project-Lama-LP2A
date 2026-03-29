package game.model;

import java.util.ArrayList;

public class Discard {
    private ArrayList<Card> cards;

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
    
    public Card getTopCard() {
        if (!this.cards.isEmpty()) {
            return this.cards.get(this.cards.size() - 1);
        }
        return null; // No cards in discard pile
    }

    public void addCard(Card card) {
        this.cards.add(card);
    }
}
