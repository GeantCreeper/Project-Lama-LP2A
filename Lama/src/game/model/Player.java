package game.model;

import java.util.ArrayList;

public class Player {
    private ArrayList<Token> tokens;
    private ArrayList<Card> hand;


    /* GETTERS AND SETTERS */

    public Player() {
        this.tokens = new ArrayList<>();
        this.hand = new ArrayList<>();
    }

    public void setToken(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<Token> getTokens() {
        return this.tokens;
    }

    public void setHand(ArrayList<Card> hand) {
        this.hand = hand;
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }


    /* METHODS */

    public void addHand(ArrayList<Card> cards) {
        if (cards != null) {
            this.hand.addAll(cards);
        }
    }

    public void addToken(Token token) {
        if (token != null) {
            this.tokens.add(token);
        }
    }

    public Card playCard(Card card) {
        if (this.hand.contains(card)) {
            this.hand.remove(card);
            return card;
        }
        return null; // Card not in hand
    }

    public Card drawCard(Deck deck) {
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            this.hand.add(drawnCard);
        }
        return drawnCard; // Return the drawn card or null if deck is empty
    }

    public boolean quit() {
        return true; // placeholder
    }

    public int calculateScore() { // placeholder
        int score = 0;
        for (Card card : this.hand) {
            score += card.getValue();
        }
        return score;
    }

}
