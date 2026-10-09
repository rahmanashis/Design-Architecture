import java.util.*;
import java.util.concurrent.*;

// ======================================================
// ENUMS
// ======================================================

enum PaymentStatus {
    CREATED,
    PROCESSING,
    SUCCESS,
    FAILED
}

// ======================================================
// ENTITY
// ======================================================

class Payment {

    private final String paymentId;
    private final String orderId;
    private final double amount;
    private final String userId;

    private PaymentStatus status;

    public Payment(String paymentId, String orderId, double amount, String userId) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.amount = amount;
        this.userId = userId;
        this.status = PaymentStatus.CREATED;
    }

    public synchronized void updateStatus(PaymentStatus newStatus) {
        if (this.status == PaymentStatus.SUCCESS) {
            throw new IllegalStateException("Cannot modify successful payment");
        }
        this.status = newStatus;
    }

    public String getPaymentId() { return paymentId; }
    public String getOrderId() { return orderId; }
    public double getAmount() { return amount; }
    public String getUserId() { return userId; }
    public PaymentStatus getStatus() { return status; }

    @Override
    public String toString() {
        return "Payment{id=" + paymentId +
                ", order=" + orderId +
                ", amount=" + amount +
                ", status=" + status + "}";
    }
}

// ======================================================
// PAYMENT METHOD STRATEGY
// ======================================================

interface PaymentMethod {
    void validate();
}

class CardPayment implements PaymentMethod {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void validate() {
        if (cardNumber == null || cardNumber.length() < 12) {
            throw new RuntimeException("Invalid Card");
        }
    }
}

class UpiPayment implements PaymentMethod {
    private final String upiId;

    public UpiPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public void validate() {
        if (upiId == null || !upiId.contains("@")) {
            throw new RuntimeException("Invalid UPI ID");
        }
    }
}

// ======================================================
// PROVIDER STRATEGY
// ======================================================

interface PaymentProvider {
    boolean process(Payment payment);
}

class DummyProvider implements PaymentProvider {

    @Override
    public boolean process(Payment payment) {
        // simulate external gateway
        return true;
    }
}

// ======================================================
// REPOSITORY
// ======================================================

class PaymentRepository {

    private final Map<String, Payment> payments = new ConcurrentHashMap<>();
    private final Map<String, String> orderIndex = new ConcurrentHashMap<>();
    private final Map<String, String> idempotencyIndex = new ConcurrentHashMap<>();

    public void save(Payment payment, String idempotencyKey) {
        payments.put(payment.getPaymentId(), payment);
        orderIndex.put(payment.getOrderId(), payment.getPaymentId());
        idempotencyIndex.put(idempotencyKey, payment.getPaymentId());
    }

    public Payment findByOrderId(String orderId) {
        String paymentId = orderIndex.get(orderId);
        return paymentId == null ? null : payments.get(paymentId);
    }

    public Payment findByIdempotencyKey(String key) {
        String paymentId = idempotencyIndex.get(key);
        return paymentId == null ? null : payments.get(paymentId);
    }
}

// ======================================================
// SERVICE
// ======================================================

class PaymentService {

    private final PaymentRepository repository;
    private final PaymentProvider provider;

    public PaymentService(PaymentRepository repository,
                          PaymentProvider provider) {
        this.repository = repository;
        this.provider = provider;
    }

    public Payment initiatePayment(
            String paymentId,
            String orderId,
            double amount,
            String userId,
            String idempotencyKey,
            PaymentMethod method) {

        synchronized (orderId.intern()) {

            // 1️⃣ Idempotency
            Payment existingByKey = repository.findByIdempotencyKey(idempotencyKey);
            if (existingByKey != null) {
                return existingByKey;
            }

            // 2️⃣ Prevent double payment
            Payment existingOrderPayment = repository.findByOrderId(orderId);
            if (existingOrderPayment != null &&
                    existingOrderPayment.getStatus() == PaymentStatus.SUCCESS) {
                throw new RuntimeException("Order already paid");
            }

            // 3️⃣ Validate
            method.validate();

            // 4️⃣ Create Payment
            Payment payment = new Payment(paymentId, orderId, amount, userId);
            payment.updateStatus(PaymentStatus.PROCESSING);

            // 5️⃣ Call provider
            boolean success = provider.process(payment);

            payment.updateStatus(success ? PaymentStatus.SUCCESS : PaymentStatus.FAILED);

            repository.save(payment, idempotencyKey);

            return payment;
        }
    }
}

// ======================================================
// MAIN
// ======================================================

public class PaymentGatewayMidLevel {

    public static void main(String[] args) {

        PaymentRepository repository = new PaymentRepository();
        PaymentProvider provider = new DummyProvider();
        PaymentService service = new PaymentService(repository, provider);

        Payment payment = service.initiatePayment(
                "PAY-101",
                "ORDER-500",
                2000,
                "USER-9",
                "IDEMP-500",
                new CardPayment("1234567890123456")
        );

        System.out.println(payment);
    }
}