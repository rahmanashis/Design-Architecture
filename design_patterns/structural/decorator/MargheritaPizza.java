package design_patterns.structural.decorator;

interface Pizza {
    String getDescription();
    int getCost();
}

//class MargheritaPizza implements design_patterns.structural.decorator.Pizza {
//    @Override
//    public String getDescription() {
//        return "Margherita Pizza";
//    }
//
//    @Override
//    public int getCost() {
//        return 100;
//    }
//}
abstract class PizzaDecorator implements Pizza {
    protected final Pizza pizza; //create a reference of Pizza class to store the object of Pizza class

    public PizzaDecorator(Pizza pizza) {
        this.pizza = pizza;
    }

    @Override
    public String getDescription() {
        return pizza.getDescription();
    }

    @Override
    public int getCost() {
        return pizza.getCost();
    }
}

class CheezeDecorator extends PizzaDecorator { //child class of PizzaDecorator class and it is a concrete decorator class
    public CheezeDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Extra Cheese";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 50;
    }
}

class MushroomDecorator extends PizzaDecorator { //child class of PizzaDecorator class and it is a concrete decorator class
    public MushroomDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Mushroom";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 30;
    }
}

class OlivesDecorator extends PizzaDecorator { //child class of PizzaDecorator class and it is a concrete decorator class
    public OlivesDecorator(Pizza pizza) {
        super(pizza);
    }

    @Override
    public String getDescription() {
        return pizza.getDescription() + ", Olives";
    }

    @Override
    public int getCost() {
        return pizza.getCost() + 20;
    }
}

//public class with inheritance and decorator pattern
public class MargheritaPizza implements Pizza {
    @Override
    public String getDescription() {
        return "Margherita Pizza";
    }

    @Override
    public int getCost() {
        return 100;
    }

    public static void main(String[] args) {
        Pizza pizza = new MargheritaPizza();
        System.out.println("Description: " + pizza.getDescription());
        System.out.println("Total cost: " + pizza.getCost());

        pizza = new CheezeDecorator(pizza);
        System.out.println("Description: " + pizza.getDescription());
        System.out.println("Total cost: " + pizza.getCost());

        pizza = new MushroomDecorator(pizza);
        System.out.println("Description: " + pizza.getDescription());
        System.out.println("Total cost: " + pizza.getCost());

        pizza = new OlivesDecorator(pizza);
        System.out.println("Description: " + pizza.getDescription());
        System.out.println("Total cost: " + pizza.getCost());

        /* this can be simplified by using the nested constructors calls like below
        Pizza pizza = new OlivesDecorator(
                        new MushroomDecorator(
                        new CheezeDecorator(
                        new MargheritaPizza()));
         */
    }
}