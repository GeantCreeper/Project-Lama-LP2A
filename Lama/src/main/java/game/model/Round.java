package game.model;

import java.util.ArrayList;

public class Round {
    private Deck deck;
    private Discard discard;
    private ArrayList<Player> players;

    public Round(ArrayList<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.discard = new Discard();
    }
    

    /* GETTERS AND SETTERS */

    public Deck getDeck() { 
        return this.deck; 
    }
    public Discard getDiscard() { 
        return this.discard; 
    }


    /* METHODS */
    
    public void start() {
        this.deck.setCards(this.deck.shuffle());

        for (Player p : this.players) {
            p.getHand().clear();
            p.setDropout(false);
            ArrayList<Card> hand = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                Card c = this.deck.drawCard();
                if (c != null) hand.add(c);
            }
            p.addHand(hand);
        }

        Card firstCard = this.deck.drawCard();
        if (firstCard != null) {
            this.discard.addCard(firstCard);
        }
    }

    public void nextTurn() {}

    public void end() {}
}