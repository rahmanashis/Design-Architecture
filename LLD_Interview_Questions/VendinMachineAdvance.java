/*
VendingMachine
 ├── Map<String, Slot> inventory
 ├── State currentState
 ├── IdleState
 ├── HasMoneyState
 ├── DispenseState
 ├── balance
 └── ReentrantLock (thread safety)

State decides behavior.

Machine only delegates.
*/

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/* ===========================
   COIN ENUM
   =========================== */
enum Coin {
    ONE(1), TWO(2), FIVE(5), TEN(10), TWENTY(20);

    private int value;

    Coin(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

/* ===========================
   PRODUCT
   =========================== */
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

/* ===========================
   SLOT
   =========================== */
class Slot {
    private String slotId;
    private Product product;
    private int quantity;

    public Slot(String slotId, Product product, int quantity) {
        this.slotId = slotId;
        this.product = product;
        this.quantity = quantity;
    }

    public String getSlotId() { return slotId; }
    public Product getProduct() { return product; }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public void reduceQuantity() {
        quantity--;
    }

    public int getQuantity() {
        return quantity;
    }
}

/* ===========================
   STATE INTERFACE
   =========================== */
interface State {
    void insertCoin(Coin coin);
    void selectSlot(String slotId);
    void refund();
}

/* ===========================
   VENDING MACHINE
   =========================== */
class VendingMachine {

    private Map<String, Slot> inventory = new HashMap<>();
    private int balance = 0;

    private final ReentrantLock lock = new ReentrantLock();

    // States
    private State idleState;
    private State hasMoneyState;
    private State dispenseState;

    private State currentState;

    private Slot selectedSlot;

    public VendingMachine() {
        idleState = new IdleState(this);
        hasMoneyState = new HasMoneyState(this);
        dispenseState = new DispenseState(this);
        currentState = idleState;
    }

    /* ===========================
       State Getters
       =========================== */
    public State getIdleState() { return idleState; }
    public State getHasMoneyState() { return hasMoneyState; }
    public State getDispenseState() { return dispenseState; }

    public void setState(State state) { this.currentState = state; }

    /* ===========================
       Inventory Methods
       =========================== */
    public void addSlot(String slotId, Product product, int quantity) {
        inventory.put(slotId, new Slot(slotId, product, quantity));
    }

    public Slot getSlot(String slotId) {
        return inventory.get(slotId);
    }

    public void displayProducts() {
        for (Slot slot : inventory.values()) {
            System.out.println(slot.getSlotId() + " | "
                    + slot.getProduct().getName()
                    + " | Price: " + slot.getProduct().getPrice()
                    + " | Qty: " + slot.getQuantity());
        }
    }

    /* ===========================
       Balance Handling
       =========================== */
    public void addBalance(int amount) {
        balance += amount;
    }

    public int getBalance() {
        return balance;
    }

    public void deductBalance(int amount) {
        balance -= amount;
    }

    public void resetBalance() {
        balance = 0;
    }

    public void setSelectedSlot(Slot slot) {
        this.selectedSlot = slot;
    }

    public Slot getSelectedSlot() {
        return selectedSlot;
    }

    /* ===========================
       Public APIs (Thread-safe)
       =========================== */
    public void insertCoin(Coin coin) {
        lock.lock();
        try {
            currentState.insertCoin(coin);
        } finally {
            lock.unlock();
        }
    }

    public void selectSlot(String slotId) {
        lock.lock();
        try {
            currentState.selectSlot(slotId);
        } finally {
            lock.unlock();
        }
    }

    public void refund() {
        lock.lock();
        try {
            currentState.refund();
        } finally {
            lock.unlock();
        }
    }
}

/* ===========================
   IDLE STATE
   =========================== */
class IdleState implements State {

    private VendingMachine machine;

    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.println("Inserted: " + coin.getValue());
        machine.setState(machine.getHasMoneyState());
    }

    public void selectSlot(String slotId) {
        System.out.println("Insert money first.");
    }

    public void refund() {
        System.out.println("No balance to refund.");
    }
}

/* ===========================
   HAS MONEY STATE
   =========================== */
class HasMoneyState implements State {

    private VendingMachine machine;

    public HasMoneyState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(Coin coin) {
        machine.addBalance(coin.getValue());
        System.out.println("Added: " + coin.getValue() +
                " | Balance: " + machine.getBalance());
    }

    public void selectSlot(String slotId) {
        Slot slot = machine.getSlot(slotId);

        if (slot == null) {
            System.out.println("Invalid slot.");
            return;
        }

        if (!slot.isAvailable()) {
            System.out.println("Out of stock.");
            return;
        }

        if (machine.getBalance() < slot.getProduct().getPrice()) {
            System.out.println("Insufficient balance.");
            return;
        }

        machine.setSelectedSlot(slot);
        machine.setState(machine.getDispenseState());
        machine.getDispenseState().selectSlot(slotId);
    }

    public void refund() {
        System.out.println("Refunded: " + machine.getBalance());
        machine.resetBalance();
        machine.setState(machine.getIdleState());
    }
}

/* ===========================
   DISPENSE STATE
   =========================== */
class DispenseState implements State {

    private VendingMachine machine;

    public DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin(Coin coin) {
        System.out.println("Wait, dispensing...");
    }

    public void selectSlot(String slotId) {
        Slot slot = machine.getSelectedSlot();

        slot.reduceQuantity();
        machine.deductBalance(slot.getProduct().getPrice());

        System.out.println("Dispensed: " + slot.getProduct().getName());

        if (machine.getBalance() > 0) {
            System.out.println("Returned Change: " + machine.getBalance());
            machine.resetBalance();
        }

        machine.setSelectedSlot(null);
        machine.setState(machine.getIdleState());
    }

    public void refund() {
        System.out.println("Already dispensing.");
    }
}

/* ===========================
   MAIN
   =========================== */
public class VendinMachineAdvance {
    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        machine.addSlot("A1", new Product("Coke", 20), 1);
        machine.addSlot("B1", new Product("Chips", 10), 5);

        machine.displayProducts();

        Runnable user1 = () -> {
            machine.insertCoin(Coin.TWENTY);
            machine.selectSlot("A1");
        };

        Runnable user2 = () -> {
            machine.insertCoin(Coin.TWENTY);
            machine.selectSlot("A1");
        };

        new Thread(user1).start();
        new Thread(user2).start();
    }
}