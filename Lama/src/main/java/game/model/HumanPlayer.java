package game.model;

public class HumanPlayer extends Player {
    private String name;
    private int age;
    
    public HumanPlayer(String name, int age) {
        super();
        this.name = name;
        this.age = age;
        
    }


    /* GETTERS AND SETTERS */
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return this.age;
    }
    
}
