package design_patterns.creational.prototype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Cloneable {
    Cloneable clone();
}

class GameBotCharacters implements Cloneable{
    //Immutable fields
    private String name;
    private int health;
    private int attackPower;
    //Mutable fields
    private List<String> weapons;

    // Copy constructor for cloning
    public GameBotCharacters(String name, int health, int attackPower, List<String> weapons) {

    //expensive Operations
    System.out.println("GameBotCharacters.GameBotCharacters");
    System.out.println("Expensive operations performed for " + name);
    System.out.println("Health: " + health);
    System.out.println("Loading sound effects form DB");

    try{
        Thread.sleep(2000); //wait for 2 seconds to simulate expensive operations
    } catch (InterruptedException e) {
        System.out.println("e.getMessage())");
    }
    
    this.name = name;
    this.health = health;
    this.attackPower = attackPower;
    this.weapons = weapons;

    }


// Copy constructor for cloning
private GameBotCharacters(GameBotCharacters gbc){
    this.name = gbc.name;
    this.health = gbc.health;
    this.attackPower = gbc.attackPower;
    this.weapons = gbc.weapons;
    //now to overcome the problem of shared reference
    this.weapons = new ArrayList<>(gbc.weapons); //now deep copy
}

    @Override
    public GameBotCharacters clone() {
       return new GameBotCharacters(this); //this refers to the current object, which is being cloned
    }

    // Beginner-friendly name for clone().
    // It creates a copy of this character without running the expensive constructor.
    public GameBotCharacters customizedClone() {
        return clone();
    }

    @Override
    public String toString() {
        return "GameBotCharacters{" +
                "name='" + name + '\'' +
                ", health=" + health +
                ", attackPower=" + attackPower +
                ", weapons=" + weapons +
                '}';
        //with templete
        //return STR."GameBotCharacters{name='\{name}', health=\{health}, attackPower=\{attackPower}}";
        }

    public void setName(String name) {
        this.name = name;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public List<String> getWeapons() {
        return weapons;
    }

    public int getAttackPower() {
        return attackPower;
    }
}

public class GameBotCharactersDemo {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();


        // Its constructor performs the expensive setup operations.
        GameBotCharacters gbc1 = new GameBotCharacters(
                "Bot1",
                100,
                0,
                new ArrayList<>(Arrays.asList("Rifle"))
        );

        // Create copies from the base character.
        // Cloning is faster because the expensive constructor is not called again.
        GameBotCharacters gbc2 = gbc1.customizedClone();
        gbc2.setName("Bot2");

        GameBotCharacters gbc3 = gbc1.customizedClone();
        gbc3.setName("Bot3");

        GameBotCharacters gbc4 = gbc1.customizedClone();
        gbc4.setName("Bot4");

        GameBotCharacters gbc5 = gbc1.customizedClone();
        gbc5.setName("Bot5");

        // 3. Customize the weapons of only one cloned character.
        List<String> weapons = gbc5.getWeapons();
        weapons.add("Sniper");

        // 4. Print the base character and all clones.
        System.out.println(gbc1);
        System.out.println(gbc2);
        System.out.println(gbc3);
        System.out.println(gbc4);
        System.out.println(gbc5);

        long endTime = System.currentTimeMillis();
        System.out.println("Total Time Taken: " + (endTime - startTime) + " ms");
    }
}
