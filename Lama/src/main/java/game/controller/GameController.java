package game.controller;

import game.gui.BoardPanel;
import game.model.*;

public class GameController {

    private Game game;
    private BoardPanel view;

    public GameController(Game game) {
        this.game = game;
    }

    // Lie la vue au contrôleur
    public void setView(BoardPanel view) {
        this.view = view;
    }

    public Game getGame() {
        return game;
    }

    // Initialise le début de la partie
    public void start() {
        this.game.newRound();
        if (view != null) {
            view.refresh();
        }
    }

    /* Actions du joueur humain reçues depuis la Vue */

    public void playCard(Card card) {
        HumanPlayer human = getHuman();
        Card top = game.getRound().getDiscard().getTopCard();

        if (!card.canBePlayedOnTopOf(top)) {
            view.showMessage("Cette carte ne peut pas être jouée ici", "#e74c3c");
            return;
        }

        human.playCard(card);
        game.getRound().getDiscard().addCard(card);
        view.showMessage("Carte jouée", "#27ae60");

        processTurn();
    }

    public void drawCard() {
        HumanPlayer human = getHuman();
        Card drawn = human.drawCard(game.getRound().getDeck());

        if (drawn == null) {
            view.showMessage("La pioche est vide", "#e67e22");
        } else {
            view.showMessage("Vous avez pioché une carte.", "#2980b9");
        }

        processTurn();
    }

    public void quitSemester() {
        getHuman().quit();
        view.showMessage("Vous avez abandonné ce semestre", "#c0392b");

        processTurn();
    }


    /* Logique interne du tour de jeu */

    private void processTurn() {
        if (!checkRoundEnd()) {
            playBots();
            checkRoundEnd();
        }
        view.refresh();
    }

    private void playBots() {
        for (Player p : game.getPlayers()) {
            if (p instanceof BotPlayer bot && !bot.isDropout()) {
                Card top = game.getRound().getDiscard().getTopCard();
                Card played = bot.playCard(top);
                
                if (played != null) {
                    game.getRound().getDiscard().addCard(played);
                } else {
                    Card drawn = bot.drawCard(game.getRound().getDeck());
                    if (drawn == null) {
                        bot.quit();
                    }
                }
            }
        }
    }

    private boolean checkRoundEnd() {
        boolean allDropped = game.getPlayers().stream().allMatch(Player::isDropout);
        boolean humanEmpty = getHuman().getHand().isEmpty();

        if (allDropped || humanEmpty) {
            view.showMessage("Fin de manche", "#8e44ad");
            return true;
        }
        return false;
    }

    private HumanPlayer getHuman() {
        return (HumanPlayer) game.getPlayers().get(0);
    }
}