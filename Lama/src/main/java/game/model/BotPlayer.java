package game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotPlayer extends Player {
    private Random random;
    private static int bot_count = 1; // apartient a la classe donc partagé par tous les bots

    // CONSTRUCTORS
    public BotPlayer() {
        super("Bot"+(bot_count++));
        this.random = new Random();
    }

    // METHODS

    public Card playCard(Card topCard) {
        if (getHand().isEmpty()) {
            return null;
        }

        if (getRoundScore() >= 18 && random.nextInt(100) < 15) { // as a probability of 15% to quit when score >= 18
            quit();
            return null;
        }

        for (Card card : getHand()) { // Prioritize playing a Lama if possible
            if (card.isLama() && card.canBePlayedOnTopOf(topCard)) {
                getHand().remove(card);
                return card;
            }
        }

        List<Card> playableCards = new ArrayList<>();
        for (Card card : getHand()) { // Collect all playable cards
            if (card.canBePlayedOnTopOf(topCard)) {
                playableCards.add(card);
            }
        }

        if (!playableCards.isEmpty()) { // Randomly choose one of the playable cards
            Card chosen = playableCards.get(random.nextInt(playableCards.size()));
            getHand().remove(chosen);
            return chosen;
        }
        
        return null; // no playable card, bot will have to draw
    }

    public static void resetBotCount() { // to reset the bot count between games (if needed)
        bot_count = 1;
    }

}
