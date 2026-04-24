package game.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/* MenuPanel represents the main menu of the game */
public class MenuPanel {

    private GamePanel app;
    private VBox view;

    // Constructor
    public MenuPanel(GamePanel app) {
        this.app = app;
        buildUI();
    }

    /* Build the user interface for the menu 
    returns void */
    private void buildUI() {
        // Title and subtitle
        Label title = new Label("Mention F");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        Label subtitle = new Label("Le jeu de cartes où rater son semestre demande de la stratégie.");
        subtitle.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Input for player name
        Label nameLabel = new Label("Votre pseudo :");
        TextField nameField = new TextField();
        nameField.setPromptText("Ex: Nikoslaï");
        nameField.setMaxWidth(250);

        // Options for number of bots
        Label botsLabel = new Label("Nombre de bots :");
        ToggleGroup botsGroup = new ToggleGroup();
        RadioButton bot1 = new RadioButton("1 bot");
        RadioButton bot3 = new RadioButton("3 bots");
        bot1.setToggleGroup(botsGroup);
        bot3.setToggleGroup(botsGroup);
        bot1.setSelected(true);

        HBox botsBox = new HBox(20, bot1, bot3);
        botsBox.setAlignment(Pos.CENTER);

        // Play button
        Button playBtn = new Button("Commencer la partie");
        playBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Error label for input validation
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

    // Getter for the view
    public VBox getView() {
        return view;
    }
}