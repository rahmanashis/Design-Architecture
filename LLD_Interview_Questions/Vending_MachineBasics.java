/*
Functional Requirements

Add products (Coke, Pepsi, Chips, etc.)

Select product

Insert money

Dispense product

Return change

Handle insufficient balance

Handle out-of-stock


Main Objects

	VendingMachine

	Product

	Inventory

	Coin

	State (important for interview)


Why Design Pattern? --> State Design Pattern
Because vending machine behavior changes based on:

Idle

Has Money

Dispensing

Out of Stock

VendingMachine
 ├── State currentState
 ├── Inventory
 ├── balance
 └── methods (insertCoin, selectProduct, dispense)

State (interface)
 ├── IdleState
 ├── HasMoneyState
 ├── DispenseState
 └── OutOfStockState


*/

import java.util.*;

// PRODUCT
class Product {
    private String name;
    private int price;

    public Product(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public int getPrice() { return price; }
}

// INVENTORY
class Inventory {
    private Map<Product, Integer> stock = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }

    public boolean isAvailable(Product product) {
        return stock.getOrDefault(product, 0) > 0;
    }

    public void reduce(Product product) {
        stock.put(product, stock.get(product) - 1);
    }
}

// STATE INTERFACE
interface State {
    void insertCoin(int amount);
    void selectProduct(Product product);
    void dispense();
}

// VENDING MACHINE
class VendingMachine {
    private State idleState;
    private State hasMoneyState;
    private State dispenseState;

    private State currentState;
    private Inventory inventory;
    private int balance;
    private Product selectedProduct;

    public VendingMachine() {
        inventory = new Inventory();
        idleState = new IdleState(this);
        hasMoneyState = new HasMoneyState(this);
        dispenseState = new DispenseState(this);

        currentState = idleState;
        balance = 0;
    }

    public void setState(State state) {
        this.currentState = state;
    }

    public State getIdleState() { return idleState; }
    public State getHasMoneyState() { return hasMoneyState; }
    public State getDispenseState() { return dispenseState; }

    public Inventory getInventory() { return inventory; }

    public int getBalance() { return balance; }
    public void addBalance(int amount) { balance += amount; }
    public void deductBalance(int amount) { balance -= amount; }

    public Product getSelectedProduct() { return selectedProduct; }
    public void setSelectedProduct(Product product) { this.selectedProduct = product; }

    public void insertCoin(int amount) {
        currentState.insertCoin(amount);
    }

    public void selectProduct(Product product) {
        currentState.selectProduct(product);
    }

    public void dispense() {
        currentState.dispense();
    }
}

// IDLE STATE
class IdleState implements State {
    private VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(int amount) {
        machine.addBalance(amount);
        System.out.println("Money inserted: " + amount);
        machine.setState(machine.getHasMoneyState());
    }

    public void selectProduct(Product product) {
        System.out.println("Insert money first.");
    }

    public void dispense() {
        System.out.println("Insert money and select product first.");
    }
}

// HAS MONEY STATE
class HasMoneyState implements State {
    private VendingMachine machine;

    public HasMoneyState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(int amount) {
        machine.addBalance(amount);
        System.out.println("Money added: " + amount);
    }

    public void selectProduct(Product product) {
        if (!machine.getInventory().isAvailable(product)) {
            System.out.println("Product out of stock.");
            return;
        }

        if (machine.getBalance() < product.getPrice()) {
            System.out.println("Insufficient balance.");
            return;
        }

        machine.setSelectedProduct(product);
        machine.setState(machine.getDispenseState());
        machine.dispense();
    }

    public void dispense() {
        System.out.println("Select product first.");
    }
}

// DISPENSE STATE
class DispenseState implements State {
    private VendingMachine machine;

    public DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(int amount) {
        System.out.println("Wait! Dispensing product.");
    }

    public void selectProduct(Product product) {
        System.out.println("Already processing.");
    }

    public void dispense() {
        Product product = machine.getSelectedProduct();
        machine.getInventory().reduce(product);
        machine.deductBalance(product.getPrice());

        System.out.println("Dispensed: " + product.getName());

        if (machine.getBalance() > 0) {
            System.out.println("Returned change: " + machine.getBalance());
            machine.deductBalance(machine.getBalance());
        }

        machine.setState(machine.getIdleState());
    }
}

// MAIN
public class Vending_MachineBasics {
    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        Product coke = new Product("Coke", 20);
        Product chips = new Product("Chips", 10);

        machine.getInventory().addProduct(coke, 5);
        machine.getInventory().addProduct(chips, 5);

        machine.insertCoin(50);
        machine.selectProduct(coke);
    }
}








 