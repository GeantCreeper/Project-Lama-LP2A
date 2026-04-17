package game.model;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private ArrayList<Token> tokens;
    private ArrayList<Card> hand;
    private boolean dropout;
    private boolean skipTurn;


    /* GETTERS AND SETTERS */

    public Player(String name) {
        this.name = name;
        this.tokens = new ArrayList<>();
        this.hand = new ArrayList<>();
        this.dropout = false;
        this.skipTurn = false;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return this.name;
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

    public void setDropout(boolean dropout) {
        this.dropout = dropout;
    }

    public boolean isDropout() {
        return this.dropout;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }

    public boolean isTurnSkipped() {
        return this.skipTurn;
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

    public abstract Card playCard(Card card);

    public Card drawCard(Deck deck) {
        Card drawnCard = deck.drawCard();
        if (drawnCard != null) {
            this.hand.add(drawnCard);
        }
        return drawnCard; // Return the drawn card or null if deck is empty
    }

    public void quit() {
        setDropout(true);
    }

    public int calculateScore() { // placeholder
        int score = 0;
        for (Card card : this.hand) {
            score += card.getValue();
        }
        return score;
    }

}
