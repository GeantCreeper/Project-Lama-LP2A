package game.gui;

import game.controller.GameController;
import game.model.Game;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/* GamePanel takes care of the main game window */
public class GamePanel extends Application {

    private Stage primaryStage;

    /* open the game window
    returns void */
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        stage.setTitle("Mention F");
        stage.setResizable(true);
        showMenu();
        stage.show();
    }

    /* show the main menu
     * returns void
     */
    public void showMenu() {
        MenuPanel menu = new MenuPanel(this);
        Scene scene = new Scene(menu.getView(), 800, 600);
        primaryStage.setScene(scene);
    }

    /* start a new game with the given player name and number of bots
    returns void */ 
    public void startGame(String playerName, int nbBots) {

        Game game = new Game(nbBots, playerName, 20);
        
        GameController controller = new GameController(game, this);
        
        BoardPanel board = new BoardPanel(this, controller);
        
        controller.setView(board);
        controller.start();
        Scene scene = new Scene(board.getView(), 1920, 1080);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
    }

    // for testing purposes only
    public static void main(String[] args) {
        launch(args);
    }
}