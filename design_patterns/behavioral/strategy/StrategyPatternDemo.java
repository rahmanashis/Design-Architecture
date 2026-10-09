package week_4;

// ================= Strategy =================
interface PaymentStrategy {
    void pay(int amount);
}

// ================= Concrete Strategies =================
class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class UPIPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using UPI");
    }
}

class NetBankingPayment implements PaymentStrategy {
    @Override
    public void pay(int amount) {
        System.out.println("Paid " + amount + " using Net Banking");
    }
}

// ================= Context =================
class PaymentContext {
    private PaymentStrategy strategy;

    // Strategy can be changed at runtime
    public void setStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void pay(int amount) {
        if (strategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        strategy.pay(amount);
    }
}

// ================= Client =================
public class StrategyPatternDemo {
    public static void main(String[] args) {

        PaymentContext context = new PaymentContext();

        context.setStrategy(new CreditCardPayment());
        context.pay(1000);

        context.setStrategy(new UPIPayment());
        context.pay(500);

        context.setStrategy(new NetBankingPayment());
        context.pay(2000);
    }
}