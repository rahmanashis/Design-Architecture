package week_3;


interface PaymentProcessor{
    public void pay();
}

class Order{
    PaymentProcessor paymentProcessor;
    public Order(PaymentProcessor paymentProcessor){
        this.paymentProcessor = paymentProcessor;
    }
    void doPayment(){
        paymentProcessor.pay();
    }
}

class Paytm implements PaymentProcessor{
    @Override
    public void pay() {
        System.out.println("Payment done using paytm");
    }

}

class Stripe{
    public void makePayment(){
        System.out.println("Payment done using stripe");
    }
}

public class AdapterDemoStripe {
    public static void main(String[] args) {
        Order order = new Order(new Paytm());
        order.doPayment();
    }
}