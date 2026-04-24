package game.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Round round;
    private boolean isGameOver;
    private int roundCount;

    public Game(int nbBots, String name, int age) {
        this.players = new ArrayList<>();
        this.players.add(new HumanPlayer(name, age));

        for (int i = 0; i < nbBots; i++) {
            this.players.add(new BotPlayer());
        }

        this.round = new Round(this.players); // passe les joueurs
        this.isGameOver = false;
        this.roundCount = 0;
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

    public void setRoundCount(int roundCount) {
        this.roundCount = roundCount;
    }

    public int getRoundCount() {
        return this.roundCount;
    }


    /* METHODS */

    public void startGame() {
        newRound();
    }

    public void newRound() {
        this.roundCount++;
        this.round = new Round(this.players); // recree un Round avec les joueurs
        this.round.start();

        // Check if game is over after 8 rounds
        if (this.roundCount >= 8) {
            this.isGameOver = true;
        }
    }
}