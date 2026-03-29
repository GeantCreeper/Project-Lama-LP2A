package game.model;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        for (int i = 1; i <= 6; i++) {
            for (int j = 0; j < 8; j++) {
                this.cards.add(new Card(i));
            }
        }
        for (int j = 0; j < 8; j++) {
            this.cards.add(new Card(7)); // Add Lama cards
        }
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public ArrayList<Card> getCards() {
        return this.cards;
    }

    public boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public ArrayList<Card> shuffle() {
        ArrayList<Card> shuffled = new ArrayList<>(this.cards);
        java.util.Collections.shuffle(shuffled);
        return shuffled;
    }

    public Card drawCard() {
        if (!this.cards.isEmpty()) {
            return this.cards.remove(0);
        }
        return null; // No cards left to draw
    }
}
