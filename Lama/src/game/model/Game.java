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


    /* GETTERS AND SETTERS */

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Round getRound() {
        return this.round;
    }

    public void setGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }


    /* METHODS */

    public void startGame() {

    }

    public void newRound() {
        this.round.start();
    }
}
