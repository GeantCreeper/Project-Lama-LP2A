package game.model;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private int score;
    private int roundScore;
    private ArrayList<Card> hand;
    private boolean dropout;
    private boolean skipTurn;


    /* GETTERS AND SETTERS */

    public Player(String name) {
        this.name = name;
        this.score = 0;
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

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public void setRoundScore(int roundScore) { 
        this.roundScore = roundScore; 
    }
    
    public int getRoundScore() { 
        return roundScore; 
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

    public void addPoints(int points) {
        this.score += points;
    }
    
    public void addRoundPoints(int points) { 
        roundScore += points; 
    }
    public void resetRoundScore() { 
        roundScore = 0; 
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

    public int calculateScore() {
        java.util.Set<Integer> seenValues = new java.util.HashSet<>();
        int score = 0;
        int lamaCount = 0;

        for (Card card : this.hand) {
            if (card.isLama()) {
                lamaCount++;
            } else if (seenValues.add(card.getValue()) == true) {
                // add() retourne true si la valeur n'était pas déjà présente
                score += card.getValue();
            }
        }

        if (!isDropout()) {
            score -= lamaCount * 10;
        }

        return score;
    }
}
