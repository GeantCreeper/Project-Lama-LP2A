package game.model;

public abstract class HumanPlayer extends Player {
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

        public abstract Card playCard(Card card);
}
