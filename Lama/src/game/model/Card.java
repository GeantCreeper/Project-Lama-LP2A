package game.model;

public class Card {
    private int value;

    public Card(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public boolean isLama() {
        return this.value == 7;
    }

    public boolean canBePlayedOnTopOf(Card topCard) {
        if (topCard.isLama()) {
            if (this.isLama() || this.value == 1) {
                return true;
            }
        }
        if (this.value == topCard.getValue() || this.value == topCard.getValue() + 1) {
            return true; 
        }
        if (this.isLama() && topCard.getValue() == 6) {
            return true;
        }
        return false;
    }
}
