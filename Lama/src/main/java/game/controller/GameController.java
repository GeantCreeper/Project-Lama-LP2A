package game.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import game.gui.BoardPanel;
import game.gui.GamePanel;
import game.model.BotPlayer;
import game.model.Card;
import game.model.Deck;
import game.model.Game;
import game.model.HumanPlayer;
import game.model.Player;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;
import java.text.MessageFormat;

public class GameController {

    private Game game;
    private BoardPanel view;
    private GamePanel app; // reference to main application for navigation

    // special card needing to wait for a second click to resolve their effect
    private boolean waitingAcademicComeback = false;
    private boolean waitingSaving = false;
    private boolean waitingErrorMoodle = false;

    private Player moodleTarget = null; // Stock the target of Error Moodle while waiting for the card click

    // Constructor
    public GameController(Game game, GamePanel app) {
        this.game = game;
        this.app = app;
    }

    // link the controller to the view (BoardPanel)
    public void setView(BoardPanel view) {
        this.view = view;
    }

    // Getter for the game model
    public Game getGame() {
        return game;
    }

    // initialize the game and start the first round
    public void start() {
        this.game.newRound();
        if (view != null) {
            view.refresh();
        }
    }

    /* Human Player Actions from the View
    playCard is called when the human player clicks on a card in their hand to play it.
    It checks if the card can be played, applies its effect, and then processes the turn.
    return void */

    public void playCard(Card card) {
        HumanPlayer human = getHuman();
        Card top = game.getRound().getDiscard().getTopCard();
        Deck deck = game.getRound().getDeck();

        if (waitingAcademicComeback) {
            card.academicComebackCard(human, card); // play the card directly from hand to discard without checking if it's playable
            game.getRound().getDiscard().addCard(card); // play the card on top of the discard
            waitingAcademicComeback = false;
            processTurn();
            return;
        }

        if (waitingSaving) {
            card.savedAtJuryCard(human, deck, card); // place the card back in the deck
            waitingSaving = false;
            processTurn();
            return;
        }

        if (waitingErrorMoodle) {
            if (moodleTarget != null) {
                card.errorMoodleCard(human, moodleTarget, card); // transfer to the target player
                view.showMessage(MessageFormat.format(LanguageController.getString("msg.card.given"), moodleTarget.getName()), "#3c24aa");
            }

            // Reset the moodle state
            waitingErrorMoodle = false;
            moodleTarget = null;
            processTurn();
            return;
        }

        if (!card.canBePlayedOnTopOf(top)) { // stop the action if the card is not playable
            view.showMessage(LanguageController.getString("msg.card.not.playable"), "#e74c3c");
            return;
        }

        // play the card and apply its effect
        human.playCard(card);
        game.getRound().getDiscard().addCard(card);
        if(!card.isLama()) {
            human.addRoundPoints(card.getValue());
            human.addPoints(card.getValue());
        }

        boolean specialEffect = applyCardEffect(human, card, deck);
        if (specialEffect) {
            view.showMessage(LanguageController.getString("msg.card.played"), "#27ae60");
            processTurn();
        }
    }

    /* drawCard allows the human player to draw a card from the deck.
    If the deck is empty, it shows a message.
    Otherwise, it processes the turn as normal after drawing.
    return void */
    public void drawCard() {
        HumanPlayer human = getHuman();
        Card drawn = human.drawCard(game.getRound().getDeck());

        if (drawn == null) {
            view.showMessage(LanguageController.getString("msg.deck.empty"), "#e67e22");
        } else {
            view.showMessage(LanguageController.getString("msg.card.drawn"), "#2980b9");
        }
        processTurn();
    }

    /* quitSemester allows the human player to quit the current semester.
    It marks the human player as having quit and shows a message.
    Then it processes the turn.
    return void */
    public void quitSemester() {
        getHuman().quit();
        view.showMessage(LanguageController.getString("msg.quit"), "#c0392b");
        processTurn();
    }


    /* processTurn handles the logic for processing each turn in the game.
    It checks if the round has ended and manages the turns for both human and bot players.
    return void */
    private void processTurn() {
        if (!checkRoundEnd()) {
            playBots();
            // if the human player has quit, make the bots play until the end of the round without waiting for human input
            while (getHuman().isDropout() && !checkRoundEnd()) {
                playBots();
            }
            // otherwise, if the human player is still in the game but has their turn skipped,
            // make the bots play until the end of the round without waiting for human input
            while (!getHuman().isDropout() && getHuman().isTurnSkipped() && !checkRoundEnd()) {
                getHuman().setSkipTurn(false);
                view.showMessage(LanguageController.getString("msg.skip.turn"), "#e67e22");
                playBots();
            }
            checkRoundEnd();
        }
        view.refresh();
    }


    /* playBots handles the logic for playing cards for all bot players.
    It iterates through the bot players and makes them play cards based on their strategies.
    return void */
    private void playBots() {
        Deck deck = game.getRound().getDeck();

        for (int i = 1; i < game.getPlayers().size(); i++) {
            Player bot = game.getPlayers().get(i);
            
            if (bot instanceof BotPlayer && !bot.isDropout()) {
                
                // the bot's turn is skipped if they were targeted by a Warning card on the previous turn
                if (bot.isTurnSkipped()) {
                    bot.setSkipTurn(false);
                    continue;
                }
                
                // the bot plays a card
                Card top = game.getRound().getDiscard().getTopCard();
                Card played = bot.playCard(top);
                
                if (played != null) {
                    game.getRound().getDiscard().addCard(played);
                    if (!played.isLama()) {
                        bot.addRoundPoints(played.getValue());
                        bot.addPoints(played.getValue());
                    }
                    applyCardEffect(bot, played, deck); // apply the card effect (some cards may cause the bot to play additional cards or skip turns, etc.)
                } else { // if the bot cannot play a card, it draws one
                    Card drawn = bot.drawCard(deck);
                    if (drawn == null) { // if the deck is empty, the bot must quit
                        bot.quit();
                    }
                }
                if (checkRoundEnd()) return;
            }
        }
    }


    /* checkRoundEnd determines if the current round has ended.
    It checks if all players have dropped out or if any active player has an empty hand.
    return boolean */
    private boolean checkRoundEnd() {
        boolean allDropped = game.getPlayers().stream().allMatch(Player::isDropout);
        boolean anyEmpty = game.getPlayers().stream().anyMatch(p -> !p.isDropout() && p.getHand().isEmpty());

        // If the round has ended, calculate scores, show messages, and start a new round or end the game if necessary
        if (allDropped || anyEmpty) {
            endOfRoundScoring();
            if (game.isGameOver()) { // If the game is over, show final scores and return to the menu
                showFinalScores();
            } else { // otherwise, start a new round
                view.showMessage(LanguageController.getString("msg.round.end"), "#8e44ad");
                game.newRound();
            }
            return true;
        }
        return false;
    }

    // Helper method to get the human player (the human is always the first player in the list)
    private HumanPlayer getHuman() {
        return (HumanPlayer) game.getPlayers().get(0);
    }

    /*
    applyCardEffect applies the effect of a played card. It checks the card's value and executes the corresponding effect.
    Return 'true' if the turn is completed, 'false' if it is paused (waiting for a human click). */
    private boolean applyCardEffect(Player player, Card card, Deck deck) {
        if (player.getHand().isEmpty()) { // If the player has no more cards after playing, the turn is over
            return true;
        }

        Player target;
        switch (card.getValue()) {
            case 8: // Gauss
                card.gaussCard(game.getPlayers(), deck);
                break;
            case 9: // Warning
                    target = getNextActivePlayer();
                if (target != null) card.warningCard(target, deck);
                break;
            case 10: // 2nd jury
                target = getTarget(player, LanguageController.getString("card.name.jury2"));
                if (target != null) card.secondJuryCard(target);
                break;
            case 11: // Error Moodle
                if (player instanceof BotPlayer) { // For bots, we directly choose a target and a card to give without waiting for clicks
                    target = getTarget(player, LanguageController.getString("card.name.moodle"));
                    if (target != null) {
                        Card toGive = player.getHand().get(new Random().nextInt(player.getHand().size()));
                        card.errorMoodleCard(player, target, toGive);
                    }
                } else { // For the human player, we need to wait for two clicks: one to choose the target and one to choose the card to give
                    moodleTarget = getTarget(player, LanguageController.getString("card.name.moodle"));
                    if (moodleTarget != null) { // We have the target, now we need to wait for the card click
                        waitingErrorMoodle = true;
                        view.showMessage(MessageFormat.format(LanguageController.getString("card.moodle.click"), moodleTarget.getName()), "#9b59b6");
                        return false;
                    }
                }
                break;
            case 12: // saved at the Jury
                if (player instanceof BotPlayer) { // For bots, we directly choose a card to save without waiting for clicks
                    Card toSave = player.getHand().get(new Random().nextInt(player.getHand().size()));
                    card.savedAtJuryCard(player, deck, toSave);
                } else { // For the human player, we need to wait for a click to choose the card to save
                    waitingSaving = true;
                    view.showMessage(LanguageController.getString("card.saving.click"), "#3498db");
                    return false;
                }
                break;
            case 13: // Academic Comeback
                if (player instanceof BotPlayer) { // For bots, we directly choose a card to play without waiting for clicks
                    Card toPlay = player.getHand().get(new Random().nextInt(player.getHand().size()));
                    player.playCard(toPlay);
                    game.getRound().getDiscard().addCard(toPlay);
                } else { // For the human player, we need to wait for a click to choose the card to play
                    waitingAcademicComeback = true;
                    view.showMessage(LanguageController.getString("card.comeback.click"), "#f1c40f");
                    return false;
                }
                break;
            case 14: // Annal Escape
                target = getTarget(player, LanguageController.getString("card.name.annals"));
                if (player instanceof HumanPlayer && target != null) { // For the human player, we show the target's hand in a pop-up
                    showPlayerHandUI(target);
                }
                break;
        }
        return true; // By default, the turn is completed after applying the card effect (unless we are waiting for a click for special cards like Error Moodle, 2nd Jury, etc.)
    }

    /*
    getTarget is a helper method to get a valid target player for cards that require one (like Warning, 2nd Jury, Error Moodle, etc.).
    It shows a dialog for the human player to choose a target, and for bots it randomly selects a valid target.
    return Player the chosen target, or null if no valid targets are available */
    private Player getTarget(Player source, String cardName) {
        ArrayList<Player> validTargets = new ArrayList<>();
        ArrayList<String> validNames = new ArrayList<>();

        for (Player p : game.getPlayers()) { // The source player cannot target themselves, and cannot target players who have dropped out
            if (p != source && !p.isDropout()) {
                validTargets.add(p);
                validNames.add(p.getName());
            }
        }

        // If there are no valid targets, return null
        if (validTargets.isEmpty()) return null;

        // Bot behavior: randomly choose a valid target without showing a dialog
        if (source instanceof BotPlayer) {
            return validTargets.get(new Random().nextInt(validTargets.size()));
        }

        // Human behavior: show a dialog to choose a target
        ChoiceDialog<String> dialog = new ChoiceDialog<>(validNames.get(0), validNames);
        dialog.setTitle(MessageFormat.format(LanguageController.getString("dialog.target.title"), cardName));
        dialog.setHeaderText(LanguageController.getString("dialog.target.header"));
        
        // Show the dialog and wait for the user's choice
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            for (Player p : validTargets) {
                if (p.getName().equals(result.get())) return p;
            }
        }
        return null;
    }

    /*
    showPlayerHandUI is a helper method to display the hand of a specific player in a pop-up dialog.
    return void */
    private void showPlayerHandUI(Player target) {
        // Build the content string with the values of the cards in the target's hand
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LanguageController.getString("dialog.annals.title"));
        alert.setHeaderText(MessageFormat.format(LanguageController.getString("dialog.annals.header"), target.getName()));
        
        // If the target has no cards, we display a message indicating that their hand is empty
        StringBuilder sb = new StringBuilder();
        for(Card c : target.getHand()) {
            sb.append(MessageFormat.format(LanguageController.getString("dialog.annals.card"), c.getValue())).append("\n");
        }
        if (target.getHand().isEmpty()) sb.append(LanguageController.getString("dialog.annals.empty"));
        
        // Set the content text of the alert to the built string and show the dialog
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }


    /*
    getNextActivePlayer is a helper method to get the next active player in the game.
    return Player the next active player, or null if no active players are available */
    private Player getNextActivePlayer() {
        ArrayList<Player> validTargets = new ArrayList<>();

        // We consider as valid targets all players who have not dropped out, including the human player if they are still active
        for (Player p : game.getPlayers()) {
            if (!p.isDropout()) {
                validTargets.add(p);
            }
        }

        // If there are no valid targets, return null
        if (validTargets.isEmpty()) {
            return null;
        }
        // return the first valid target (the human player if they are still active, otherwise the first bot in the list who is still active)
        return validTargets.get(0);
    }

    /*
    endOfRoundScoring is a helper method to update the scores at the end of each round.
    return void */
    private void endOfRoundScoring() {
        for (Player p : game.getPlayers()) {
            p.resetRoundScore();
        }
    }

    /*
    showFinalScores is a helper method to display the final scores of all players at the end of the game in a pop-up dialog,
    and then return to the main menu.
    return void */
    private void showFinalScores() {
        StringBuilder sb = new StringBuilder(LanguageController.getString("scores.intro"));
        // We sort the players by score in descending order and build the content string with their names and scores
        game.getPlayers().stream()
            .sorted((a, b) -> a.getScore() - b.getScore())
            .forEach(p -> sb.append(MessageFormat.format(LanguageController.getString("scores.line"), p.getName(), p.getScore())));
        
        // Set the content text of the alert to the built string and show the dialog
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(LanguageController.getString("scores.title"));
        alert.setHeaderText(LanguageController.getString("scores.header"));
        alert.setContentText(sb.toString());
        alert.showAndWait();
        app.showMenu();
    }
}