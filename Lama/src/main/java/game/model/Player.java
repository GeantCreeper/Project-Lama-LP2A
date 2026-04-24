package game.model;

import java.util.ArrayList;

public abstract class Player {
    private String name;
    private int score;
    private int roundScore;
    private ArrayList<Card> hand;
    private boolean dropout;
    private boolean skipTurn;

    // CONSTRUCTORS
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.hand = new ArrayList<>();
        this.dropout = false;
        this.skipTurn = false;
    }

    // GETTERS AND SETTERS

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
        return this.roundScore;
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

    // Adds a list of cards to the player's hand (used when dealing cards at the start of a round)
    public void addHand(ArrayList<Card> cards) {
        if (cards != null) {
            this.hand.addAll(cards);
        }
    }

    public void addPoints(int points) {
        this.score += points;
    }
    
    public void addRoundPoints(int points) {
        this.roundScore += points;
    }
    public void resetRoundScore() {
        this.roundScore = 0;
    }
    
    // Abstract method to play a card, implemented differently for human and Bots
    public abstract Card playCard(Card card);

    // Draws a card from the deck and adds it to the player's hand. Returns the drawn card or null if the deck is empty.
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

    /*
     * Calculates the player's score based on the cards in their hand.
     * Lamas are worth -10 points each, and other cards are summed one time for each type.
     */
    public int calculateScore() {
        java.util.Set<Integer> seenValues = new java.util.HashSet<>();
        int score = 0;
        int lamaCount = 0;

        for (Card card : this.hand) { // Count Lamas and sum other card values only once
            if (card.isLama()) {
                lamaCount++;
            } else if (seenValues.add(card.getValue()) == true) { // add() returns true if the value was not already in the set
                score += card.getValue();
            }
        }

        if (!isDropout()) { // Only apply Lama penalty if the player has not dropped out
            score -= lamaCount * 10;
        }

        return score;
    }
}
