package game.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GamePanel extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mention F");
        stage.setResizable(false);
        showMenu();
        stage.show();
    }

    public void showMenu() {
        MenuPanel menu = new MenuPanel(this);
        Scene scene = new Scene(menu.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public void startGame(String playerName, int nbBots) {
        BoardPanel board = new BoardPanel(this, playerName, nbBots);
        Scene scene = new Scene(board.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}