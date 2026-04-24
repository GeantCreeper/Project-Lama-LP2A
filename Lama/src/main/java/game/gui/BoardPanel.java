package game.gui;

import game.controller.GameController;
import game.model.BotPlayer;
import game.model.Card;
import game.model.HumanPlayer;
import game.model.Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BoardPanel {

    private GamePanel app;
    private GameController controller;
    private VBox view;

    // Zone d'affichage dynamique
    private HBox humanCardsBox;
    private ImageView topCardImageView;
    private Label deckLabel;
    private Label messageLabel;
    private VBox botsStatusBox;
    private Label scoreLabel;

    // Gestionnaire d'images de cartes
    private final CardImageManager imageManager = new CardImageManager();

    public BoardPanel(GamePanel app, GameController controller) {
        this.app = app;
        this.controller = controller;
        buildUI();
    }

    private void buildUI() {
        // ... (Garde EXACTEMENT le même code buildUI que tu avais, 
        // avec juste une modification sur les actions des boutons) ...
        
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

        Label botsTitle = new Label("Bots");
        botsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        botsTitle.setStyle("-fx-text-fill: white;");
        botsStatusBox.getChildren().add(botsTitle);

        /* Zone du mid : pioche et défausse */
        deckLabel = new Label();
        deckLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        deckLabel.setAlignment(Pos.CENTER);
        deckLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        deckLabel.setStyle(cardStyle("#2980b9", "white"));

        topCardImageView = new ImageView();
        topCardImageView.setPreserveRatio(true);
        topCardImageView.setSmooth(true);

        StackPane discardPane = new StackPane(topCardImageView);
        discardPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        discardPane.setPrefHeight(200);
        discardPane.setPrefWidth(500);
        discardPane.setStyle(cardStyle("#e74c3c", "white"));

        VBox deckBox = new VBox(5, new Label("Pioche"), deckLabel);
        deckBox.setAlignment(Pos.CENTER);
        deckBox.setPrefWidth(167);
        deckBox.setMaxWidth(167);
        deckBox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(deckLabel, Priority.ALWAYS);
        ((Label) deckBox.getChildren().get(0)).setStyle("-fx-text-fill: #7f8c8d;");

        VBox discardBox = new VBox(5, new Label("Défausse"), discardPane);
        discardBox.setAlignment(Pos.CENTER);
        discardBox.setMaxWidth(167);
        discardBox.setPrefWidth(167);
        VBox.setVgrow(discardPane, Priority.ALWAYS);
        ((Label) discardBox.getChildren().get(0)).setStyle("-fx-text-fill: #7f8c8d;");

        HBox pileBox = new HBox(30, deckBox, discardBox);
        pileBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(deckBox, Priority.ALWAYS);
        HBox.setHgrow(discardBox, Priority.ALWAYS);

        topCardImageView.fitWidthProperty().bind(discardPane.widthProperty().multiply(0.9));
        topCardImageView.fitHeightProperty().bind(discardPane.heightProperty().multiply(0.9));

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

        // ICI : On délègue les clics au contrôleur
        drawBtn.setOnAction(e -> controller.drawCard());
        quitBtn.setOnAction(e -> controller.quitSemester());
        menuBtn.setOnAction(e -> app.showMenu());

        HBox actionBox = new HBox(15, drawBtn, quitBtn, menuBtn);
        actionBox.setAlignment(Pos.CENTER);

        /* Main du joueur */
        Label handTitle = new Label("Votre main :");
        handTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        humanCardsBox = new HBox(15);
        humanCardsBox.setAlignment(Pos.CENTER_LEFT);
        humanCardsBox.setPadding(new Insets(10));
        humanCardsBox.setMaxHeight(Double.MAX_VALUE);
        humanCardsBox.setPrefHeight(250);
        HBox.setHgrow(discardPane, Priority.ALWAYS);

        ScrollPane scrollHand = new ScrollPane(humanCardsBox);
        scrollHand.setFitToHeight(true);
        scrollHand.setFitToWidth(true);
        scrollHand.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollHand.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollHand.setStyle("-fx-background-color: transparent; -fx-background: #ecf0f1;");
        //scrollHand.setMinHeight(340);   // supprimer la hauteur fixe
        humanCardsBox.prefWidthProperty().bind(scrollHand.widthProperty());

        // Removed the listener that was preventing the UI from growing back
        VBox.setVgrow(scrollHand, Priority.ALWAYS);

        VBox handBox = new VBox(8, handTitle, scrollHand);
        handBox.setAlignment(Pos.CENTER);
        handBox.setPadding(new Insets(10));
        handBox.setStyle("-fx-background-color: #ecf0f1; -fx-background-radius: 8;");

        /* Centre */
        VBox centerBox = new VBox(20, pileBox, messageLabel, actionBox);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        /* Layout principal */
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


    /* Méthode appelée par le contrôleur pour afficher un texte */
    public void showMessage(String text, String colorHex) {
        messageLabel.setText(text);
        messageLabel.setStyle("-fx-text-fill: " + colorHex + ";");
    }

    /* Rafraîchissement de l'UI */

    public void refresh() {
        refreshTopCard();
        refreshDeck();
        refreshHand();
        refreshBots();
        refreshScore();
    }

    private void refreshTopCard() {
        Card top = controller.getGame().getRound().getDiscard().getTopCard();
        if (top != null) {
            int idx = imageManager.randomIndexFor(top);
            topCardImageView.setImage(imageManager.loadImageByIndex(idx));
        } else {
            topCardImageView.setImage(null);
        }
    }

    private void refreshDeck() {
        int remaining = controller.getGame().getRound().getDeck().getCards().size();
        deckLabel.setText(remaining + "\ncartes");
    }

    private void refreshHand() {
        humanCardsBox.getChildren().clear();
        HumanPlayer human = getHuman();
        Card top = controller.getGame().getRound().getDiscard().getTopCard();

        imageManager.syncWith(human.getHand());

        for (Card card : human.getHand()) {
            int idx = imageManager.getOrAssignIndex(card, human.getHand());
            Image img = imageManager.loadImageByIndex(idx);

            ImageView iv = new ImageView(img);
            iv.setSmooth(true);
            iv.fitHeightProperty().bind(humanCardsBox.heightProperty().multiply(0.9));
            iv.fitWidthProperty().bind(humanCardsBox.heightProperty().multiply(0.65));

            Button cardBtn = new Button("", iv);
            cardBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            cardBtn.setMinSize(167, 0);
            cardBtn.prefHeightProperty().bind(humanCardsBox.heightProperty().multiply(0.9));
            cardBtn.prefWidthProperty().bind(humanCardsBox.heightProperty().multiply(0.65));
            cardBtn.setPadding(Insets.EMPTY);

            boolean playable = top != null && card.canBePlayedOnTopOf(top);
            if (playable) {
                cardBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #27ae60; -fx-border-width: 3; -fx-border-radius: 8;");
            } else {
                cardBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #95a5a6; -fx-border-width: 1; -fx-border-radius: 8; -fx-opacity: 0.7;");
            }

            // ICI : On délègue le clic au contrôleur
            cardBtn.setOnAction(e -> controller.playCard(card));
            humanCardsBox.getChildren().add(cardBtn);
        }
    }

    private void refreshBots() {
        botsStatusBox.getChildren().subList(1, botsStatusBox.getChildren().size()).clear();

        int i = 1;
        for (Player p : controller.getGame().getPlayers()) {
            if (p instanceof BotPlayer bot) {
                String status = bot.isDropout() ? "Abandonné" : bot.getHand().size() + " cartes" +" (" + bot.getScore() + " ects)";
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
        scoreLabel.setText("ECTS : " + human.getScore() + " pts");
    }


    /* Utilitaires */

    private HumanPlayer getHuman() {
        return (HumanPlayer) controller.getGame().getPlayers().get(0);
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