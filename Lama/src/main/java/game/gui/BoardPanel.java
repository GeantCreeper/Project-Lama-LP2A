package game.gui;

import game.model.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class BoardPanel {

    private GamePanel app;
    private Game game;
    private VBox view;

    // Zone d'affichage dynamique
    private HBox humanCardsBox;
    private Label topCardLabel;
    private Label deckLabel;
    private Label messageLabel;
    private VBox botsStatusBox;
    private Label scoreLabel;

    public BoardPanel(GamePanel app, String playerName, int nbBots) {
        this.app = app;
        this.game = new Game(nbBots, playerName, 20);
        this.game.newRound();
        buildUI();
        refresh();
    }

    private void buildUI() {
        
        /* En-tête */
        Label title = new Label("Mention F");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        scoreLabel = new Label();
        scoreLabel.setFont(Font.font("Arial", 14));

        HBox header = new HBox(20, title, scoreLabel);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(10, 20, 10, 20));
        header.setStyle("-fx-background-color: #2c3e50;");
        title.setStyle("-fx-text-fill: white;");
        scoreLabel.setStyle("-fx-text-fill: #ecf0f1;");


        /* Zone bots */
        botsStatusBox = new VBox(8);
        botsStatusBox.setPadding(new Insets(10));
        botsStatusBox.setStyle("-fx-background-color: #34495e; -fx-background-radius: 8;");
        botsStatusBox.setMinWidth(160);

        Label botsTitle = new Label("🤖 Bots");
        botsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        botsTitle.setStyle("-fx-text-fill: white;");
        botsStatusBox.getChildren().add(botsTitle);


        /* Zone du mid : pioche et défausse */
        deckLabel = new Label();
        deckLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        deckLabel.setAlignment(Pos.CENTER);
        deckLabel.setMinSize(90, 120);
        deckLabel.setStyle(cardStyle("#2980b9", "white"));

        topCardLabel = new Label();
        topCardLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        topCardLabel.setAlignment(Pos.CENTER);
        topCardLabel.setMinSize(90, 120);
        topCardLabel.setStyle(cardStyle("#e74c3c", "white"));

        VBox deckBox = new VBox(5, new Label("Pioche"), deckLabel);
        deckBox.setAlignment(Pos.CENTER);
        ((Label)deckBox.getChildren().get(0)).setStyle("-fx-text-fill: #7f8c8d;");

        VBox discardBox = new VBox(5, new Label("Défausse"), topCardLabel);
        discardBox.setAlignment(Pos.CENTER);
        ((Label)discardBox.getChildren().get(0)).setStyle("-fx-text-fill: #7f8c8d;");

        HBox pileBox = new HBox(30, deckBox, discardBox);
        pileBox.setAlignment(Pos.CENTER);


        /* Message d'info */
        messageLabel = new Label("À votre tour !");
        messageLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        messageLabel.setStyle("-fx-text-fill: #27ae60;");


        /* Boutons d'action */
        Button drawBtn = new Button("Piocher");
        Button quitBtn = new Button("Abandonner le semestre");
        Button menuBtn = new Button("Menu");

        drawBtn.setFont(Font.font("Arial", 14));
        quitBtn.setFont(Font.font("Arial", 14));
        menuBtn.setFont(Font.font("Arial", 12));

        drawBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-background-radius: 6;");
        quitBtn.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-background-radius: 6;");
        menuBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 6;");

        drawBtn.setOnAction(e -> onDraw());
        quitBtn.setOnAction(e -> onQuit());
        menuBtn.setOnAction(e -> app.showMenu());

        HBox actionBox = new HBox(15, drawBtn, quitBtn, menuBtn);
        actionBox.setAlignment(Pos.CENTER);


        /* Main du joueur */
        Label handTitle = new Label("Votre main :");
        handTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        humanCardsBox = new HBox(10);
        humanCardsBox.setAlignment(Pos.CENTER);
        humanCardsBox.setPadding(new Insets(10));

        VBox handBox = new VBox(8, handTitle, humanCardsBox);
        handBox.setAlignment(Pos.CENTER);
        handBox.setPadding(new Insets(10));
        handBox.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 8;");


        /* Centre */
        VBox centerBox = new VBox(20, pileBox, messageLabel, actionBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));


        /*Layout principal */
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(header);
        mainLayout.setLeft(botsStatusBox);
        mainLayout.setCenter(centerBox);
        mainLayout.setBottom(handBox);
        BorderPane.setMargin(botsStatusBox, new Insets(10));
        BorderPane.setMargin(handBox, new Insets(10));

        view = new VBox(mainLayout);
        VBox.setVgrow(mainLayout, Priority.ALWAYS);
    }


    /* Actions joueur */
    private void onPlayCard(Card card) {
        HumanPlayer human = getHuman();
        Card top = game.getRound().getDiscard().getTopCard();

        if (!card.canBePlayedOnTopOf(top)) {
            messageLabel.setText("Cette carte ne peut pas être jouée ici");
            messageLabel.setStyle("-fx-text-fill: #e74c3c;");
            return;
        }

        human.playCard(card);
        game.getRound().getDiscard().addCard(card);
        messageLabel.setText("Carte jouée");
        messageLabel.setStyle("-fx-text-fill: #27ae60;");

        checkRoundEnd();
        if (!game.isGameOver()) {
            playBots();
            refresh();
        }
    }

    private void onDraw() {
        HumanPlayer human = getHuman();
        Card drawn = human.drawCard(game.getRound().getDeck());

        if (drawn == null) {
            messageLabel.setText("La pioche est vide");
            messageLabel.setStyle("-fx-text-fill: #e67e22;");
        } else {
            messageLabel.setText("Vous avez pioché une carte.");
            messageLabel.setStyle("-fx-text-fill: #2980b9;");
        }

        playBots();
        refresh();
    }

    private void onQuit() {
        getHuman().quit();
        messageLabel.setText("Vous avez abandonné ce semestre");
        messageLabel.setStyle("-fx-text-fill: #c0392b;");

        checkRoundEnd();
        if (!game.isGameOver()) {
            playBots();
            refresh();
        }
    }

    private void playBots() {
        for (Player p : game.getPlayers()) {
            if (p instanceof BotPlayer bot && !bot.isDropout()) {
                Card top = game.getRound().getDiscard().getTopCard();
                Card played = bot.playCard(top);
                if (played != null) {
                    game.getRound().getDiscard().addCard(played);
                } else {
                    // Le bot pioche ou abandonne
                    Card drawn = bot.drawCard(game.getRound().getDeck());
                    if (drawn == null) {
                        bot.quit();
                    }
                }
            }
        }
        checkRoundEnd();
    }

    private void checkRoundEnd() {
        boolean allDropped = game.getPlayers().stream().allMatch(Player::isDropout);
        boolean humanEmpty = getHuman().getHand().isEmpty();

        if (allDropped || humanEmpty) {
            messageLabel.setText("Fin de manche");
            messageLabel.setStyle("-fx-text-fill: #8e44ad;");
            // a faire : appeler game.getRound().end() quand Round sera fait
        }
    }

    /* Rafraîchissement de l'UI */

    private void refresh() {
        refreshTopCard();
        refreshDeck();
        refreshHand();
        refreshBots();
        refreshScore();
    }

    private void refreshTopCard() {
        Card top = game.getRound().getDiscard().getTopCard();
        if (top != null) {
            topCardLabel.setText(cardName(top));
        } else {
            topCardLabel.setText("—");
        }
    }

    private void refreshDeck() {
        int remaining = game.getRound().getDeck().getCards().size();
        deckLabel.setText(remaining + "\ncartes");
    }

    private void refreshHand() {
        humanCardsBox.getChildren().clear();
        HumanPlayer human = getHuman();
        Card top = game.getRound().getDiscard().getTopCard();

        for (Card card : human.getHand()) {
            Button cardBtn = new Button(cardName(card));
            cardBtn.setMinSize(70, 100);
            cardBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            boolean playable = top != null && card.canBePlayedOnTopOf(top);
            if (playable) {
                cardBtn.setStyle(cardStyle("#27ae60", "white"));
            } else {
                cardBtn.setStyle(cardStyle("#95a5a6", "white"));
            }

            cardBtn.setOnAction(e -> onPlayCard(card));
            humanCardsBox.getChildren().add(cardBtn);
        }
    }

    private void refreshBots() {
        // Garder seulement le titre donc index 
        botsStatusBox.getChildren().subList(1, botsStatusBox.getChildren().size()).clear();

        int i = 1;
        for (Player p : game.getPlayers()) {
            if (p instanceof BotPlayer bot) {
                String status = bot.isDropout() ? "Abandonné" : bot.getHand().size() + " cartes";
                Label botLabel = new Label("Bot " + i + " : " + status);
                botLabel.setStyle("-fx-text-fill: " + (bot.isDropout() ? "#e74c3c" : "#ecf0f1") + ";");
                botLabel.setFont(Font.font("Arial", 13));
                botsStatusBox.getChildren().add(botLabel);
                i++;
            }
        }
    }

    private void refreshScore() {
        HumanPlayer human = getHuman();
        int tokens = human.getTokens().stream().mapToInt(Token::getValue).sum();
        scoreLabel.setText("ECTS perdus : " + tokens + " pts");
    }

    /* Utilitaires */

    private HumanPlayer getHuman() {
        return (HumanPlayer) game.getPlayers().get(0);
    }

    private String cardName(Card card) {
        return card.isLama() ? "Jury" : String.valueOf(card.getValue());
    }

    private String cardStyle(String bg, String fg) {
        return "-fx-background-color: " + bg + "; " +
               "-fx-text-fill: " + fg + "; " +
               "-fx-background-radius: 8; " +
               "-fx-border-radius: 8; " +
               "-fx-border-color: rgba(0,0,0,0.2); " +
               "-fx-border-width: 1;";
    }

    public VBox getView() {
        return view;
    }
}