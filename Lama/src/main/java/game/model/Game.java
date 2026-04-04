package game.model;

import java.util.ArrayList;

public class Game {
    private ArrayList<Player> players;
    private Round round;
    private boolean isGameOver;

    public Game(int nbBots, String name, int age) {
        this.players = new ArrayList<>();
        this.players.add(new HumanPlayer(name, age));

        for (int i = 0; i < nbBots; i++) {
            this.players.add(new BotPlayer());
        }

        this.round = new Round(this.players); // passe les joueurs
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
        newRound(); 
    }

    public void newRound() {
        this.round = new Round(this.players); // recree un Round avec les joueurs
        this.round.start();
    }
}