package design_patterns.structural.decorator;

import java.util.logging.Logger;

public abstract class BasePizza {
    private static final Logger LOGGER = Logger.getLogger(BasePizza.class.getName());

    public abstract int getCost();

    // This is the main method to demonstrate the Decorator pattern
    public static void main(String[] args) {
        BasePizza pizza = new Margarita(); //because Margarita extends BasePizza
        pizza = new ExtraCheese(pizza);
        pizza = new Mushroom(pizza);

        int totalCost = pizza.getCost();
        LOGGER.info(() -> "Total cost: " + totalCost);
    }
}

class Farmhouse extends BasePizza {
    @Override
    public int getCost() {
        return 200;
    }
}

class VegDelight extends BasePizza {
    @Override
    public int getCost() {
        return 150;
    }
}

class Margarita extends BasePizza {
    @Override
    public int getCost() {
        return 100;
    }
}
// these 3 classes are concrete components and Inheritance


//lets create the Decorator class
abstract class ToppingDecorator extends BasePizza {
    protected final BasePizza basePizza;

    public ToppingDecorator(BasePizza basePizza) {
        this.basePizza = basePizza;
    }
}

class ExtraCheese extends ToppingDecorator {
    public ExtraCheese(BasePizza pizza) {
        super(pizza);
    }

    @Override
    public int getCost() {
        return this.basePizza.getCost() + 50;
    }
}

class Mushroom extends ToppingDecorator {
    public Mushroom(BasePizza pizza) {
        super(pizza);
    }

    @Override
    public int getCost() {
        return this.basePizza.getCost() + 30;
    }
}
