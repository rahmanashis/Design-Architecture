package solid_principles.ISP;
// ==========================================
// VERY FAT INTERFACE (ISP VIOLATION)
// ==========================================

interface PaymentSystem {

    void pay(double amount);

    void refund(double amount);

    void schedulePayment(double amount);

    void cancelScheduledPayment();

    void generateReceipt();

    void applyCashback();
}

// ------------------------------------------
// CARD PAYMENT (Supports most features)
// ------------------------------------------

class CardPayment implements PaymentSystem {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Card");
    }

    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to Card");
    }

    public void schedulePayment(double amount) {
        System.out.println("Scheduled card payment of " + amount);
    }

    public void cancelScheduledPayment() {
        System.out.println("Cancelled scheduled card payment");
    }

    public void generateReceipt() {
        System.out.println("Generated card payment receipt");
    }

    public void applyCashback() {
        System.out.println("Applied cashback on card payment");
    }
}

// ------------------------------------------
// UPI PAYMENT (PARTIAL SUPPORT)
// ------------------------------------------

class UpiPayment implements PaymentSystem {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }

    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to UPI");
    }

    public void schedulePayment(double amount) {
        throw new UnsupportedOperationException("UPI scheduling not supported");
    }

    public void cancelScheduledPayment() {
        throw new UnsupportedOperationException("No scheduled UPI payment");
    }

    public void generateReceipt() {
        System.out.println("Generated UPI receipt");
    }

    public void applyCashback() {
        throw new UnsupportedOperationException("UPI cashback not supported");
    }
}

// ------------------------------------------
// CASH PAYMENT (MINIMAL SUPPORT)
// ------------------------------------------

class CashPayment implements PaymentSystem {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Cash");
    }

    public void refund(double amount) {
        throw new UnsupportedOperationException("Cash refund not supported");
    }

    public void schedulePayment(double amount) {
        throw new UnsupportedOperationException("Cash scheduling not supported");
    }

    public void cancelScheduledPayment() {
        throw new UnsupportedOperationException("No scheduled cash payment");
    }

    public void generateReceipt() {
        throw new UnsupportedOperationException("Cash receipt not supported");
    }

    public void applyCashback() {
        throw new UnsupportedOperationException("Cash cashback not supported");
    }
}

// ------------------------------------------
// CLIENT CODE
// ------------------------------------------

public class ISP_SOLID {

    public static void main(String[] args) {

        PaymentSystem payment = new CashPayment();

        payment.pay(500);                 // ✅ works
        payment.refund(100);              // 💥 runtime exception
        payment.generateReceipt();        // 💥 runtime exception
    }
}