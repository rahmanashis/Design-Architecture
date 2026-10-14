package solid_principles.LSP;

// No surprises for the client
// ==========================================
// LSP VIOLATION DEMO
// ==========================================

//  Fat Interface
// Assumes all payment methods support refund
interface PaymentMethod {

    void pay(double amount);

    void refund(double amount);
}

// ------------------------------------------
// VALID IMPLEMENTATION
// ------------------------------------------

class CardPayment implements PaymentMethod {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Card");
    }

    public void refund(double amount) {
        System.out.println("Refunded " + amount + " to Card");
    }
}

// ------------------------------------------
//  LSP VIOLATION
// ------------------------------------------

class CashPayment implements PaymentMethod {

    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Cash");
    }

    //  Cannot honor interface contract
    public void refund(double amount) {
        throw new UnsupportedOperationException("Cash refund not supported");
    }
}

// ------------------------------------------
// CLIENT CODE
// ------------------------------------------

public class LSP_SOLID {

    public static void main(String[] args) {

        PaymentMethod card = new CardPayment();
        card.pay(500);
        card.refund(200);   // works fine

        System.out.println();

        PaymentMethod cash = new CashPayment();
        cash.pay(300);

        // Runtime failure — LSP violated
        cash.refund(100);
    }
}

/*
    A subclass:
        Should NOT throw new exceptions
        Should NOT narrow input range
        Should NOT change expected output
 */