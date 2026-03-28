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
}
