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
    
    /* Starts the round by dealing cards and setting up the initial discard pile 
    return void */
    public void start() {
        this.deck.setCards(this.deck.shuffle());

        for (Player p : this.players) { // Clear players' hands and reset dropout status before dealing new cards
            p.getHand().clear();
            p.setDropout(false);
            ArrayList<Card> hand = new ArrayList<>();
            for (int i = 0; i < 6; i++) { // Each player draws 6 cards from the deck to form their initial hand
                Card c = this.deck.drawCard();
                if (c != null) hand.add(c);
            }
            p.addHand(hand);
        }

        Card firstCard = this.deck.drawCard();
        if (firstCard != null) { // Place the first card on the discard pile to start the game
            this.discard.addCard(firstCard);
        }
    }
}