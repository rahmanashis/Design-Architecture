package week_4;
// ================= State =================
interface VendingMachineState {
    void insertCoin();
    void selectItem();
    void dispenseItem();
}

// ================= Concrete States =================
class NoCoinState implements VendingMachineState {
    private VendingMachine machine;

    NoCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin() {
        System.out.println("Coin inserted");
        machine.setState(machine.getHasCoinState());
    }

    public void selectItem() {
        System.out.println("Insert coin first");
    }

    public void dispenseItem() {
        System.out.println("Insert coin first");
    }
}

class HasCoinState implements VendingMachineState {
    private VendingMachine machine;

    HasCoinState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin() {
        System.out.println("Coin already inserted");
    }

    public void selectItem() {
        System.out.println("Item selected");
        machine.setState(machine.getDispenseState());
    }

    public void dispenseItem() {
        System.out.println("Select item first");
    }
}

class DispenseState implements VendingMachineState {
    private VendingMachine machine;

    DispenseState(VendingMachine machine) {
        this.machine = machine;
    }

    public void insertCoin() {
        System.out.println("Please wait, dispensing item");
    }

    public void selectItem() {
        System.out.println("Already dispensing");
    }

    public void dispenseItem() {
        System.out.println("Item dispensed");
        machine.setState(machine.getNoCoinState());
    }
}

// ================= Context =================
class VendingMachine {
    private VendingMachineState noCoinState;
    private VendingMachineState hasCoinState;
    private VendingMachineState dispenseState;

    private VendingMachineState currentState;

    public VendingMachine() {
        noCoinState = new NoCoinState(this);
        hasCoinState = new HasCoinState(this);
        dispenseState = new DispenseState(this);

        currentState = noCoinState;
    }

    void setState(VendingMachineState state) {
        this.currentState = state;
    }

    VendingMachineState getNoCoinState() {
        return noCoinState;
    }

    VendingMachineState getHasCoinState() {
        return hasCoinState;
    }

    VendingMachineState getDispenseState() {
        return dispenseState;
    }

    // Delegation
    public void insertCoin() {
        currentState.insertCoin();
    }

    public void selectItem() {
        currentState.selectItem();
    }

    public void dispenseItem() {
        currentState.dispenseItem();
    }
}

// ================= Client =================
public class VendingMachineDemo {
    public static void main(String[] args) {

        VendingMachine machine = new VendingMachine();

        machine.selectItem();
        machine.insertCoin();
        machine.insertCoin();
        machine.selectItem();
        machine.dispenseItem();

        System.out.println("---- Next Customer ----");

        machine.dispenseItem();
        machine.insertCoin();
        machine.selectItem();
        machine.dispenseItem();
    }
}