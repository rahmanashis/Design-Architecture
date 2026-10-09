package week_1;


// ===============================
// OCP DEMO: PAYMENT PROCESSING
// ===============================

interface PaymentMethod {
    void pay(double amount);
}

// -------------------------------
// Concrete Implementations
// -------------------------------

class CardPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Card");
    }
}

class UpiPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class WalletPayment implements PaymentMethod {
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Wallet");
    }
}

// -------------------------------
// Processor (Closed for modification)
// -------------------------------

class PaymentProcessor {

    public void process(PaymentMethod paymentMethod, double amount) {
        paymentMethod.pay(amount);
    }
}

// -------------------------------
// Main Class
// -------------------------------

public class Solid_OCP {

    public static void main(String[] args) {

        PaymentProcessor processor = new PaymentProcessor();

        PaymentMethod card = new CardPayment();
        PaymentMethod upi = new UpiPayment();
        PaymentMethod wallet = new WalletPayment();

        processor.process(card, 500);
        processor.process(upi, 1000);
        processor.process(wallet, 300);

    }
}