package game.controller;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import game.gui.*;
import game.model.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceDialog;

public class GameController {

    private Game game;
    private BoardPanel view;
    private GamePanel app; // Référence à la classe principale pour les transitions d'écran

    // cartes speciales necessitant un deuxieme clic du joueur
    private boolean waitingAcademicComeback = false;
    private boolean waitingSaving = false;
    private boolean waitingErrorMoodle = false; 

    private Player moodleTarget = null; // Stocke la cible de l'Erreur Moodle en attente

    public GameController(Game game, GamePanel app) {
        this.game = game;
        this.app = app;
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
        Deck deck = game.getRound().getDeck();

        if (waitingAcademicComeback) {
            card.academicComebackCard(human, card); // Retire la carte sans vérifier les règles
            game.getRound().getDiscard().addCard(card); // La pose sur la défausse
            waitingAcademicComeback = false;
            processTurn();
            return;
        }

        if (waitingSaving) {
            card.savedAtJuryCard(human, deck, card); // Déplace la carte de la main vers la pioche
            waitingSaving = false;
            processTurn();
            return;
        }

        if (waitingErrorMoodle) {
            if (moodleTarget != null) {
                card.errorMoodleCard(human, moodleTarget, card); // Transfert entre joueurs
                view.showMessage("Carte donnée à " + moodleTarget.getName(), "#3c24aa");
            }
            waitingErrorMoodle = false;
            moodleTarget = null;
            processTurn();
            return;
        }

        if (!card.canBePlayedOnTopOf(top)) {
            view.showMessage("Cette carte ne peut pas être jouée ici", "#e74c3c");
            return;
        }

        human.playCard(card);
        game.getRound().getDiscard().addCard(card);
        if(!card.isLama()) {
            human.addRoundPoints(card.getValue());
        }

        boolean specialEffect = applyCardEffect(human, card, deck);
        if (specialEffect) {
            view.showMessage("Carte jouée", "#27ae60");
            processTurn();
        }
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
            // Si l'humain est couché, continuer à faire jouer les bots jusqu'à fin de manche
            while (getHuman().isDropout() && !checkRoundEnd()) {
                playBots();
            }
            // Sinon gérer le skip de tour normal
            while (!getHuman().isDropout() && getHuman().isTurnSkipped() && !checkRoundEnd()) {
                getHuman().setSkipTurn(false);
                view.showMessage("Vous passez votre tour.", "#e67e22");
                playBots();
            }
            checkRoundEnd();
        }
        view.refresh();
    }

    private void playBots() {
        Deck deck = game.getRound().getDeck();

        for (int i = 1; i < game.getPlayers().size(); i++) {
            Player bot = game.getPlayers().get(i);
            
            if (bot instanceof BotPlayer && !bot.isDropout()) {
                
                // Le bot passe son tour s'il est ciblé par le 2ème Jury
                if (bot.isTurnSkipped()) {
                    bot.setSkipTurn(false);
                    continue; 
                }

                Card top = game.getRound().getDiscard().getTopCard();
                Card played = bot.playCard(top);
                
                if (played != null) {
                    game.getRound().getDiscard().addCard(played);
                    if (!played.isLama()) {
                        bot.addRoundPoints(played.getValue());
                    }
                    applyCardEffect(bot, played, deck); // Application de l'effet fusionné
                } else {
                    Card drawn = bot.drawCard(deck);
                    if (drawn == null) {
                        bot.quit();
                    }
                }
                if (checkRoundEnd()) return;
            }
        }
    }

    private boolean checkRoundEnd() {
        boolean allDropped = game.getPlayers().stream().allMatch(Player::isDropout);
        boolean anyEmpty = game.getPlayers().stream().anyMatch(p -> !p.isDropout() && p.getHand().isEmpty());

        if (allDropped || anyEmpty) {
            endOfRoundScoring();
            if (game.isGameOver()) {
                showFinalScores();
            } else {
                view.showMessage("Fin de manche ! Nouveau semestre.", "#8e44ad");
                game.newRound(); // Relance une manche (re-distribue les cartes)
            }
            return true;
        }
        return false;
    }

    private HumanPlayer getHuman() {
        return (HumanPlayer) game.getPlayers().get(0);
    }

    /**
     * Applique l'effet d'une carte.
     * Retourne 'true' si le tour est terminé, 'false' s'il est mis en pause (attente de clic humain).
     */
    private boolean applyCardEffect(Player player, Card card, Deck deck) {
        if (player.getHand().isEmpty()) {
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
            case 10: // 2eme jury
                target = getTarget(player, "2ème Jury");
                if (target != null) card.secondJuryCard(target);
                break;
            case 11: // Error Moodle
                if (player instanceof BotPlayer) {
                    target = getTarget(player, "Erreur Moodle");
                    if (target != null) {
                        Card toGive = player.getHand().get(new Random().nextInt(player.getHand().size()));
                        card.errorMoodleCard(player, target, toGive);
                    }
                } else {
                    moodleTarget = getTarget(player, "Erreur Moodle");
                    if (moodleTarget != null) {
                        waitingErrorMoodle = true;
                        view.showMessage("Cliquez sur la carte à donner à " + moodleTarget.getName(), "#9b59b6");
                        return false; // Pause le tour pour le clic
                    }
                }
                break;
            case 12: // Sauvetage au Jury
                if (player instanceof BotPlayer) {
                    Card toSave = player.getHand().get(new Random().nextInt(player.getHand().size()));
                    card.savedAtJuryCard(player, deck, toSave);
                } else {
                    waitingSaving = true;
                    view.showMessage("Cliquez sur une carte à remettre en pioche", "#3498db");
                    return false; // Pause le tour
                }
                break;
            case 13: // Academic Comeback
                if (player instanceof BotPlayer) {
                    Card toPlay = player.getHand().get(new Random().nextInt(player.getHand().size()));
                    player.playCard(toPlay); 
                    game.getRound().getDiscard().addCard(toPlay);
                } else {
                    waitingAcademicComeback = true;
                    view.showMessage("Cliquez sur n'importe quelle carte pour la poser", "#f1c40f");
                    return false; // Pause le tour
                }
                break;
            case 14: // Annal Escape
                target = getTarget(player, "Fuite d'Annales");
                if (player instanceof HumanPlayer && target != null) {
                    showPlayerHandUI(target);
                }
                break;
        }
        return true; // L'effet est résolu, le tour peut continuer
    }

    /**
     * Trouve une cible. Si c'est un Bot, choix aléatoire. Si c'est l'Humain, Pop-up.
     */
    private Player getTarget(Player source, String cardName) {
        ArrayList<Player> validTargets = new ArrayList<>();
        ArrayList<String> validNames = new ArrayList<>();

        for (Player p : game.getPlayers()) {
            if (p != source && !p.isDropout()) {
                validTargets.add(p);
                validNames.add(p.getName());
            }
        }

        if (validTargets.isEmpty()) return null;

        // Comportement du Bot
        if (source instanceof BotPlayer) {
            return validTargets.get(new Random().nextInt(validTargets.size()));
        }

        // Comportement de l'Humain
        ChoiceDialog<String> dialog = new ChoiceDialog<>(validNames.get(0), validNames);
        dialog.setTitle("Carte : " + cardName);
        dialog.setHeaderText("Choisissez un joueur à cibler :");
        
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            for (Player p : validTargets) {
                if (p.getName().equals(result.get())) return p;
            }
        }
        return null;
    }

    private void showPlayerHandUI(Player target) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fuite d'Annales");
        alert.setHeaderText("Main de : " + target.getName());
        
        StringBuilder sb = new StringBuilder();
        for(Card c : target.getHand()) {
            sb.append("- Carte de valeur : ").append(c.getValue()).append("\n");
        }
        if (target.getHand().isEmpty()) sb.append("Ce joueur n'a plus de cartes.");
        
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    private Player getNextActivePlayer() {
        ArrayList<Player> validTargets = new ArrayList<>();

        for (Player p : game.getPlayers()) {
            if (!p.isDropout()) {
                validTargets.add(p);
            }
        }

        if (validTargets.isEmpty()) {
            return null;
        }
        return validTargets.get(0); // Retourne le prochain joueur actif (ici, le premier de la liste)
    }

    private void endOfRoundScoring() {
        int maxPerRound = game.getMaxPointsParRound();
        for (Player p : game.getPlayers()) {
            int gained = Math.min(p.getRoundScore() + p.calculateScore(), maxPerRound);
            p.addPoints(gained);
            p.resetRoundScore();
        }
    }

    private void showFinalScores() {
        StringBuilder sb = new StringBuilder("Scores finaux :\n\n");
        game.getPlayers().stream()
            .sorted((a, b) -> a.getScore() - b.getScore())
            .forEach(p -> sb.append(p.getName()).append(" : ").append(p.getScore()).append(" pts\n"));

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Fin de partie");
        alert.setHeaderText("La partie est terminée !");
        alert.setContentText(sb.toString());
        alert.showAndWait();
        app.showMenu();
    }
}