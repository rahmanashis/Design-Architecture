package design_patterns.behavioral.state;

// ================= State =================
interface OrderState {
    void pay();
    void ship();
    void deliver();
    void cancel();
}

// ================= Concrete States =================
class CreatedState implements OrderState {
    private Order order;

    CreatedState(Order order) {
        this.order = order;
    }

    public void pay() {
        System.out.println("Payment successful");
        order.setState(order.getPaidState());
    }

    public void ship() {
        System.out.println("Pay before shipping");
    }

    public void deliver() {
        System.out.println("Cannot deliver before shipping");
    }

    public void cancel() {
        System.out.println("Order cancelled");
        order.setState(order.getCancelledState());
    }
}

class PaidState implements OrderState {
    private Order order;

    PaidState(Order order) {
        this.order = order;
    }

    public void pay() {
        System.out.println("Order already paid");
    }

    public void ship() {
        System.out.println("Order shipped");
        order.setState(order.getShippedState());
    }

    public void deliver() {
        System.out.println("Ship order first");
    }

    public void cancel() {
        System.out.println("Order cancelled & refund initiated");
        order.setState(order.getCancelledState());
    }
}

class ShippedState implements OrderState {
    private Order order;

    ShippedState(Order order) {
        this.order = order;
    }

    public void pay() {
        System.out.println("Already paid");
    }

    public void ship() {
        System.out.println("Already shipped");
    }

    public void deliver() {
        System.out.println("Order delivered");
        order.setState(order.getDeliveredState());
    }

    public void cancel() {
        System.out.println("Cannot cancel after shipping");
    }
}

class DeliveredState implements OrderState {
    public void pay() {
        System.out.println("Order already completed");
    }

    public void ship() {
        System.out.println("Order already completed");
    }

    public void deliver() {
        System.out.println("Order already delivered");
    }

    public void cancel() {
        System.out.println("Cannot cancel delivered order");
    }
}

class CancelledState implements OrderState {
    public void pay() {
        System.out.println("Order is cancelled");
    }

    public void ship() {
        System.out.println("Order is cancelled");
    }

    public void deliver() {
        System.out.println("Order is cancelled");
    }

    public void cancel() {
        System.out.println("Order already cancelled");
    }
}

// ================= Context =================
class Order {
    private OrderState createdState;
    private OrderState paidState;
    private OrderState shippedState;
    private OrderState deliveredState;
    private OrderState cancelledState;

    private OrderState currentState;

    public Order() {
        createdState = new CreatedState(this);
        paidState = new PaidState(this);
        shippedState = new ShippedState(this);
        deliveredState = new DeliveredState();
        cancelledState = new CancelledState();

        currentState = createdState;
    }

    void setState(OrderState state) {
        this.currentState = state;
    }

    OrderState getPaidState() { return paidState; }
    OrderState getShippedState() { return shippedState; }
    OrderState getDeliveredState() { return deliveredState; }
    OrderState getCancelledState() { return cancelledState; }

    // Delegation
    public void pay() { currentState.pay(); }
    public void ship() { currentState.ship(); }
    public void deliver() { currentState.deliver(); }
    public void cancel() { currentState.cancel(); }
}

// ================= Client =================
public class OrderStateDemo {
    public static void main(String[] args) {

        Order order = new Order();

        order.ship();     // invalid
        order.pay();      // CREATED -> PAID
        order.pay();      // invalid
        order.ship();     // PAID -> SHIPPED
        order.cancel();   // invalid
        order.deliver();  // SHIPPED -> DELIVERED
        order.cancel();   // invalid
    }
}