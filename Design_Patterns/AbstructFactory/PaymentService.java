package DesignPatterns;




interface Payment{
    void pay(int amount);
}

class UPI implements Payment{
    @Override
    public void pay(int amount) {
        System.out.println(amount +" paid using" + " UPI");
    }
}

class CardPayment implements Payment{
    @Override
    public void pay(int amount) {
        System.out.println(amount +" paid using" + " Card Payment");
    }
}


class PaymentProcessor {
    private Payment payment;
    PaymentProcessor(Payment payment){
        this.payment = payment;
    }
    void pay(int amount){
        payment.pay(amount);
    }
}

public class PaymentService {
    public static void main(String[] args) {
        PaymentProcessor paymentProcessor = new PaymentProcessor(new CardPayment());
        paymentProcessor.pay(2000);
    }
}