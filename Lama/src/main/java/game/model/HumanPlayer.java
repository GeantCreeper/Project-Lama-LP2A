package game.model;

public class HumanPlayer extends Player {
    private int age;
    
    public HumanPlayer(String name, int age) {
        super(name);
        this.age = age;
        
    }


    /* GETTERS AND SETTERS */

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }
    

    /* METHODS */

        public Card playCard(Card card) {
        if (getHand().contains(card)) {
            getHand().remove(card);
            return card;
        }
        return null; // Card not in hand
    }
}
