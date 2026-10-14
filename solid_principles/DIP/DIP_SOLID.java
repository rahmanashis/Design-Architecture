package solid_principles.DIP;

// ==========================================
// DIP DEMO: PAYMENT PROCESSING
// ==========================================

// -------- Abstraction --------
interface Payment {
    void pay(double amount);
}

// -------- Low-level modules --------
class CardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Card");
    }
}

class UpiPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

// -------- High-level module --------
class PaymentProcessor {

        //High Level module code depends on abstraction(Payment)
    private Payment payment;

    // Dependency is injected via constructor
    public PaymentProcessor(Payment payment) {
        this.payment = payment;
    }

    public void process(double amount) {
        payment.pay(amount);
    }
}

// -------- Client --------
public class DIP_SOLID {

    public static void main(String[] args) {

        // Using Card payment
        Payment cardPayment = new CardPayment();
        PaymentProcessor processor1 = new PaymentProcessor(new CardPayment());
        processor1.process(500);

        // Using UPI payment
        Payment upiPayment = new UpiPayment();
        PaymentProcessor processor2 = new PaymentProcessor(upiPayment);
        processor2.process(1000);
    }
}