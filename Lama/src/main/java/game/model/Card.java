package game.model;

import java.util.ArrayList;

public class Card {
    private int value;
    private int[] lamas = {7, 8, 9, 10, 11, 12, 13, 14};

    // CONSTRUCTORS
    public Card(int value) {
        this.value = value;
    }


    // GETTERS AND SETTERS

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }


    // METHODS
    
    // Returns true if the card is a Lama (value between 7 and 14), false otherwise
    public boolean isLama() {
        for (int lama : this.lamas) {
            if (lama == this.value) {
                return true;
            }
        }
        return false;
    }

    // Returns true if the card can be played on top of the given card according to the game rules
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

    // Action methods for special cards (these would be called by the GameController when a special card is played)
    public void gaussCard(ArrayList<Player> players, Deck deck) {
        for (Player p : players) {
            if (!p.isDropout()) {
                p.drawCard(deck);
            }
        }
    }

    // warning card: the next player must draw a card from the deck and skip their turn
    public void warningCard(Player nextPlayer, Deck deck) {
        nextPlayer.drawCard(deck);
    }

    // second jury card: the next player must skip their turn without drawing a card
    public void secondJuryCard(Player player) {
        player.setSkipTurn(true);
    }

    // moodle card: the player can choose to give one of their cards to another player
    public void errorMoodleCard(Player player, Player target, Card cardToMove) {
        if (player.getHand().remove(cardToMove)) {
            target.getHand().add(cardToMove);
        }
    }

    // saved at jury card: the player can choose to put one of their cards back in the deck (at the bottom)
    public void savedAtJuryCard(Player player, Deck deck, Card cardToMove) {
        if (player.getHand().remove(cardToMove)) {
            deck.getCards().add(cardToMove);
        }
    }

    // academic comeback card: the player can choose to play a card from their hand immediately (instead of waiting for their turn)
    public void academicComebackCard(Player player, Card card) {
        player.playCard(card);
    }

    // annal escape card: the player can choose to reveal their entire hand to all other players (for strategic purposes)
    public ArrayList<Card> annalEscapeCard(Player player) {
        return player.getHand();
    }

}
