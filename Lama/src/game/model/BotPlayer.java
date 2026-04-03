package game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {
    private Random random;

    public BotPlayer() {
        super();
        this.random = new Random();
    }

    /* METHODS */

    public Card playCard(Card topCard) {
        if (getHand().isEmpty()) {
            return null;
        }

        for (Card card : getHand()) {
            if (card.isLama() && card.canBePlayedOnTopOf(topCard)) {
                getHand().remove(card);
                return card;
            }
        }

        List<Card> playableCards = new ArrayList<>();
        for (Card card : getHand()) {
            if (card.canBePlayedOnTopOf(topCard)) {
                playableCards.add(card);
            }
        }

        if (!playableCards.isEmpty()) {
            Card chosen = playableCards.get(random.nextInt(playableCards.size()));
            getHand().remove(chosen);
            return chosen;
        }
        
        return null; // Aucune carte jouable
    }   

}
