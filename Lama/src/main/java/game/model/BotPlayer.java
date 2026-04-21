package game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {
    private Random random;
    private static int bot_count = 1; // apartient a la classe donc partagé par tous les bots

    public BotPlayer() {
        super("Bot"+(bot_count++));
        this.random = new Random();
    }

    /* METHODS */

    public Card playCard(Card topCard) {
        if (getHand().isEmpty()) {
            return null;
        }

        if (getRoundScore() >= 25 && random.nextInt(100) < 15) {
            quit();
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

    public static void resetBotCount() {
        bot_count = 1;
    }

}
