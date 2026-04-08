package game.model;

import java.util.ArrayList;

public class Card {
    private int value;
    private int[] lamas = {7, 8, 9, 10, 11, 12, 13, 14};

    public Card(int value) {
        this.value = value;
    }


    /* GETTERS AND SETTERS */

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


    /* METHODS */
    
    public boolean isLama() {
        for (int lama : this.lamas) {
            if (lama == this.value) {
                return true;
            }
        }
        return false;
    }

    public boolean canBePlayedOnTopOf(Card topCard) {
        if (topCard.isLama()) {
            if (this.isLama() || this.value == 1) {
                return true;
            }
        }
        if (this.value == topCard.getValue() || this.value == topCard.getValue() + 1) {
            return true; 
        }
        return this.isLama() && topCard.getValue() == 6;
    }

    public ArrayList<Card> annalEscapeCard(Player player) {
        return player.getHand();
    }

    public void academicComebackCard(Player player, Card card) {
        player.playCard(card);
    }

    public void savedAtJuryCard(Player player, Deck deck, Card cardToMove) {
        if (player.getHand().remove(cardToMove)) {
            deck.getCards().add(cardToMove);
        }
    }

    public void errorMoodleCard(Player player, Player target, Card cardToMove) {
        if (player.getHand().remove(cardToMove)) {
            target.getHand().add(cardToMove);
        }
    }

    public void secondJuryCard(Player player) {
        player.quit();
    }

    public void warningCard(Player nextPlayer, Deck deck) {
        nextPlayer.drawCard(deck);
    }

    public void gaussCard(ArrayList<Player> players, Deck deck) {
        for (Player p : players) {
            if (!p.isDropout()) {
                p.drawCard(deck);
            }
        }
    }

}
