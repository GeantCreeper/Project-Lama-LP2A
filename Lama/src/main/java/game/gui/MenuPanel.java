package game.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class MenuPanel {

    private GamePanel app;
    private VBox view;

    public MenuPanel(GamePanel app) {
        this.app = app;
        buildUI();
    }

    private void buildUI() {
        // Titre
        Label title = new Label("Mention F");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        Label subtitle = new Label("Le jeu de cartes où rater son semestre demande de la stratégie.");
        subtitle.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Champ nom
        Label nameLabel = new Label("Votre pseudo :");
        TextField nameField = new TextField();
        nameField.setPromptText("Ex: Nikoslaï");
        nameField.setMaxWidth(250);

        // Choix nombre de bots
        Label botsLabel = new Label("Nombre de bots :");
        ToggleGroup botsGroup = new ToggleGroup();
        RadioButton bot1 = new RadioButton("1 bot");
        RadioButton bot3 = new RadioButton("3 bots");
        bot1.setToggleGroup(botsGroup);
        bot3.setToggleGroup(botsGroup);
        bot1.setSelected(true);

        HBox botsBox = new HBox(20, bot1, bot3);
        botsBox.setAlignment(Pos.CENTER);

        // Bouton jouer
        Button playBtn = new Button("Commencer la partie");
        playBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Message d'erreur
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        playBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText("Veuillez entrer votre pseudo.");
                return;
            }
            int nbBots = bot3.isSelected() ? 3 : 1;
            app.startGame(name, nbBots);
        });

        // Layout
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getChildren().addAll(
            title, subtitle,
            new Separator(),
            nameLabel, nameField,
            botsLabel, botsBox,
            playBtn, errorLabel
        );
    }

    public VBox getView() {
        return view;
    }
}