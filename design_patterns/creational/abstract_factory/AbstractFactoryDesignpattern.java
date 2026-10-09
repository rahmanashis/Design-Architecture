package Week_2;
// Abstract Factory Pattern - Payment Gateway Example

// ================= PRODUCT INTERFACES =================
interface Payment {
    void pay();
}

interface Refund {
    void refund();
}

interface Invoice {
    void generate();
}

// ================= PAYTM PRODUCTS =================
class PaytmPayment implements Payment {
    public void pay() {
        System.out.println("Paytm payment done");
    }
}

class PaytmRefund implements Refund {
    public void refund() {
        System.out.println("Paytm refund processed");
    }
}

class PaytmInvoice implements Invoice {
    public void generate() {
        System.out.println("Paytm invoice generated");
    }
}

// ================= RAZORPAY PRODUCTS =================
class RazorpayPayment implements Payment {
    public void pay() {
        System.out.println("Razorpay payment done");
    }
}

class RazorpayRefund implements Refund {
    public void refund() {
        System.out.println("Razorpay refund processed");
    }
}

class RazorpayInvoice implements Invoice {
    public void generate() {
        System.out.println("Razorpay invoice generated");
    }
}

// ================= ABSTRACT FACTORY =================
interface PaymentGatewayFactory {
    Payment createPayment();
    Refund createRefund();
    Invoice createInvoice();
}

// ================= CONCRETE FACTORIES =================
class PaytmFactory implements PaymentGatewayFactory {

    public Payment createPayment() {
        return new PaytmPayment();
    }

    public Refund createRefund() {
        return new PaytmRefund();
    }

    public Invoice createInvoice() {
        return new PaytmInvoice();
    }
}

class RazorpayFactory implements PaymentGatewayFactory {

    public Payment createPayment() {
        return new RazorpayPayment();
    }

    public Refund createRefund() {
        return new RazorpayRefund();
    }

    public Invoice createInvoice() {
        return new RazorpayInvoice();
    }
}

// ================= CLIENT =================
public class AbstractFactoryDesignpattern {

    public static void main(String[] args) {

        // 🔁 Switch entire payment gateway here
        PaymentGatewayFactory factory = new PaytmFactory();
        // PaymentGatewayFactory factory = new RazorpayFactory();

        Payment payment = factory.createPayment();
        Refund refund = factory.createRefund();
        Invoice invoice = factory.createInvoice();

        payment.pay();
        refund.refund();
        invoice.generate();
    }
}
