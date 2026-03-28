package game.model;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<Token> tokens;
    private ArrayList<Card> hand;

    public Player(String name) {
        this.name = name;
        this.tokens = new ArrayList<>();
        this.hand = new ArrayList<>();
    }

}
