package design_patterns.structural.decorator;

import java.util.logging.Logger;

public abstract class CoffeeShop {
    private static final Logger LOGGER = Logger.getLogger(CoffeeShop.class.getName());

    public abstract int cost();

    public abstract String description();

    public static void main(String[] args) {
        CoffeeShop coffee = new Espresso();
        coffee = new Milk(coffee);
        coffee = new CaramelSyrup(coffee);
        coffee = new WhippedCream(coffee);

        LOGGER.info(coffee.description() + " = " + coffee.cost());
    }
}

class Espresso extends CoffeeShop {
    @Override
    public int cost() {
        return 120;
    }

    @Override
    public String description() {
        return "Espresso";
    }
}

class ColdBrew extends CoffeeShop {
    @Override
    public int cost() {
        return 180;
    }

    @Override
    public String description() {
        return "Cold brew";
    }
}

class CoffeeDecorator extends CoffeeShop {
    protected final CoffeeShop coffee;

    protected CoffeeDecorator(CoffeeShop coffee) {
        this.coffee = coffee;
    }

    @Override
    public int cost() {
        return coffee.cost();
    }

    @Override
    public String description() {
        return coffee.description();
    }
}

class Milk extends CoffeeDecorator {
    public Milk(CoffeeShop coffee) {
        super(coffee);
    }

    @Override
    public int cost() {
        return coffee.cost() + 30;
    }

    @Override
    public String description() {
        return coffee.description() + ", milk";
    }
}

class CaramelSyrup extends CoffeeDecorator {
    public CaramelSyrup(CoffeeShop coffee) {
        super(coffee);
    }

    @Override
    public int cost() {
        return coffee.cost() + 40;
    }

    @Override
    public String description() {
        return coffee.description() + ", caramel syrup";
    }
}

class WhippedCream extends CoffeeDecorator {
    public WhippedCream(CoffeeShop coffee) {
        super(coffee);
    }

    @Override
    public int cost() {
        return coffee.cost() + 35;
    }

    @Override
    public String description() {
        return coffee.description() + ", whipped cream";
    }
}
