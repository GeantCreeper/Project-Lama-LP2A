package game.model;

public class Token {
    private int value;

    public Token(int value) {
        this.value = value;
    }

    /* GETTERS AND SETTERS */
    
    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
