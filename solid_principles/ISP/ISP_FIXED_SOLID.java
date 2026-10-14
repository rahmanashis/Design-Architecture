package solid_principles.ISP;

// ==========================================
// ISP
// ==========================================

// -------- Small, Focused Interfaces --------

interface Payable {
    void pay(double amount);
}

interface Refundable {
    void refund(double amount);
}

interface Schedulable {
    void schedulePayment(double amount);
    void cancelScheduledPayment();
}

interface Receiptable {
    void generateReceipt();
}

interface CashbackApplicable {
    void applyCashback();
}

// ------------------------------------------
// CARD PAYMENT (Supports many capabilities)
// ------------------------------------------

class CardPayment implements
        Payable,
        Refundable,
        Schedulable,
        Receiptable,
        CashbackApplicable {

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
// UPI PAYMENT (Limited capabilities)
// ------------------------------------------

class UpiPayment implements
        Payable,
        Refundable,
        Receiptable {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI");
    }

    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to UPI");
    }

    public void generateReceipt() {
        System.out.println("Generated UPI payment receipt");
    }
}

// ------------------------------------------
// CASH PAYMENT (Minimal capabilities)
// ------------------------------------------

class CashPayment implements Payable {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Cash");
    }
}

// ------------------------------------------
// CLIENT CODE
// ------------------------------------------

public class ISP_FIXED_SOLID {

    public static void main(String[] args) {

        Payable cash = new CashPayment();
        cash.pay(500);   // ✅ only allowed operation

        System.out.println();

        Payable upi = new UpiPayment();
        upi.pay(1000);

        Refundable refundableUpi = new UpiPayment();
        refundableUpi.refund(200);

        System.out.println();

        CardPayment card = new CardPayment();
        card.pay(2000);
        card.applyCashback();
        card.generateReceipt();
    }
}