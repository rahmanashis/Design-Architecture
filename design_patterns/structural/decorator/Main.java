package design_patterns.structural.decorator;
// problem: https://app.notion.com/p/Decorator-414d0522eee7808c80dec86f57b9a2c6

//Create Coffee Interface
interface Coffee {
    String getDescription();
    double getCost();
}


//Create BasicCoffee Concrete Class
class BasicCoffee implements Coffee {

    public String getDescription() {
        return "Basic Coffee";
    }

    public double getCost() {
        return 50;
    }
}


//Create Abstract Decorator Class
abstract class CoffeeDecorator implements Coffee {

    protected Coffee coffee;
    // This will store the Coffee object that we are decorating
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }

    public String getDescription() {
        return coffee.getDescription();
    }

    public double getCost() {
        return coffee.getCost();
    }
}

//Create MilkDecorator
class MilkDecorator extends CoffeeDecorator {

    public MilkDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Milk";
    }

    public double getCost() {
        return coffee.getCost() + 20;
    }
}


//Create SugarDecorator
class SugarDecorator extends CoffeeDecorator {

    public SugarDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Sugar";
    }

    public double getCost() {
        return coffee.getCost() + 10;
    }
}


// ChocolateDecorator
class ChocolateDecorator extends CoffeeDecorator {

    public ChocolateDecorator(Coffee coffee) {
        super(coffee);
    }

    public String getDescription() {
        return coffee.getDescription() + ", Chocolate";
    }

    public double getCost() {
        return coffee.getCost() + 30;
    }
}


public class Main {
    public static void main(String[] args) {

            // Basic Coffee
            Coffee coffee1 = new BasicCoffee();

            System.out.println(coffee1.getDescription());
            System.out.println("Cost: " + coffee1.getCost());

            // Coffee with Milk
            Coffee coffee2 =
                    new MilkDecorator(new BasicCoffee());

            System.out.println("\n" + coffee2.getDescription());
            System.out.println("Cost: " + coffee2.getCost());

            // Coffee with Milk and Sugar
            Coffee coffee3 =
                    new SugarDecorator(
                            new MilkDecorator(new BasicCoffee())
                    );

            System.out.println("\n" + coffee3.getDescription());
            System.out.println("Cost: " + coffee3.getCost());

            // Coffee with Milk, Sugar and Chocolate
            Coffee coffee4 =
                    new ChocolateDecorator(
                            new SugarDecorator(
                                    new MilkDecorator(new BasicCoffee())
                            )
                    );

            System.out.println("\n" + coffee4.getDescription());
            System.out.println("Cost: " + coffee4.getCost());
        }
    }
}
