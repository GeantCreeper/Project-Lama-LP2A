package game.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Round round;
    private boolean isGameOver;

    public Game(ArrayList<Player> players) {
        this.players = players;
        this.round = new Round();
        this.isGameOver = false;
    }

    public void startGame() {

    }

    public void newRound() {
        round.start();
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
