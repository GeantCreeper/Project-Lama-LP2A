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

import java.util.Locale;

import game.controller.LanguageController;

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
        Label title = new Label(LanguageController.getString("menu.title"));
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        Label subtitle = new Label(LanguageController.getString("menu.subtitle"));
        subtitle.setFont(Font.font("Arial", FontPosture.ITALIC, 14));
        subtitle.setWrapText(true);
        subtitle.setTextAlignment(TextAlignment.CENTER);

        // Input for player name
        Label nameLabel = new Label(LanguageController.getString("menu.name.label"));
        TextField nameField = new TextField();
        nameField.setPromptText(LanguageController.getString("menu.name.placeholder"));
        nameField.setMaxWidth(250);

        // Options for number of bots
        Label botsLabel = new Label(LanguageController.getString("menu.bots.label"));
        ToggleGroup botsGroup = new ToggleGroup();
        RadioButton bot1 = new RadioButton(LanguageController.getString("menu.bots.one"));
        RadioButton bot3 = new RadioButton(LanguageController.getString("menu.bots.three"));
        bot1.setToggleGroup(botsGroup);
        bot3.setToggleGroup(botsGroup);
        bot1.setSelected(true);

        HBox botsBox = new HBox(20, bot1, bot3);
        botsBox.setAlignment(Pos.CENTER);

        // Play button
        Button playBtn = new Button(LanguageController.getString("menu.play.btn"));
        playBtn.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        // Error label for input validation
        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: red;");

        playBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                errorLabel.setText(LanguageController.getString("menu.error.name"));
                return;
            }
            int nbBots = bot3.isSelected() ? 3 : 1;
            app.startGame(name, nbBots);
        });

        // Language toggle button
        Button langBtn = new Button(LanguageController.getCurrentLocale().equals(Locale.FRENCH) ? "English" : "Français");
        langBtn.setFont(Font.font("Arial", 12));
        langBtn.setStyle("-fx-background-color: #7f8c8d; -fx-text-fill: white; -fx-background-radius: 6;");
        langBtn.setOnAction(e -> {
            if (LanguageController.getCurrentLocale().equals(Locale.FRENCH)) {
                LanguageController.setLocale(Locale.ENGLISH);
            } else {
                LanguageController.setLocale(Locale.FRENCH);
            }
            app.showMenu(); // rebuild the menu with the new language
        });
 
        HBox langBox = new HBox(langBtn);
        langBox.setAlignment(Pos.CENTER_RIGHT);

        // Layout
        view = new VBox(20);
        view.setAlignment(Pos.CENTER);
        view.setPadding(new Insets(40));
        view.getChildren().addAll(
            langBox, title, subtitle,
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