// ======================================================
// SOLID PRINCIPLES – BAD vs GOOD (Teaching Version)
// ======================================================

public class SolidTeachingDemo {

    public static void main(String[] args) {
        System.out.println("Use this file for teaching SOLID.");
    }
}

/* ======================================================
   1️⃣ S - SINGLE RESPONSIBILITY PRINCIPLE
   ====================================================== */

// ❌ BAD - Violates SRP
class InvoiceBad {

    public void calculateTotal() {
        System.out.println("Calculating total...");
    }

    public void saveToDB() {
        System.out.println("Saving to DB...");
    }

    public void printInvoice() {
        System.out.println("Printing invoice...");
    }
}
// Problem:
// One class handling Business logic + Persistence + Printing
// Multiple reasons to change


// ✅ GOOD - Follows SRP
class Invoice {
    public void calculateTotal() {
        System.out.println("Calculating total...");
    }
}

class InvoiceRepository {
    public void save(Invoice invoice) {
        System.out.println("Saving to DB...");
    }
}

class InvoicePrinter {
    public void print(Invoice invoice) {
        System.out.println("Printing invoice...");
    }
}



/* ======================================================
   2️⃣ O - OPEN CLOSED PRINCIPLE
   ====================================================== */

// ❌ BAD - Violates OCP
class DiscountCalculatorBad {

    public double calculate(String type, double amount) {

        if (type.equals("NO_DISCOUNT")) {
            return amount;
        } else if (type.equals("PERCENTAGE")) {
            return amount - (amount * 10 / 100);
        } else if (type.equals("FESTIVE")) {
            return amount - 100;
        }

        return amount;
    }
}
// Problem:
// Every new discount → modify this class


// ✅ GOOD - Follows OCP
interface DiscountStrategy {
    double apply(double amount);
}

class NoDiscount implements DiscountStrategy {
    public double apply(double amount) {
        return amount;
    }
}

class PercentageDiscount implements DiscountStrategy {
    public double apply(double amount) {
        return amount - (amount * 10 / 100);
    }
}

class FestiveDiscount implements DiscountStrategy {
    public double apply(double amount) {
        return amount - 100;
    }
}

class DiscountCalculator {
    public double calculate(DiscountStrategy strategy, double amount) {
        return strategy.apply(amount);
    }
}



/* ======================================================
   3️⃣ L - LISKOV SUBSTITUTION PRINCIPLE
   ====================================================== */

// ❌ BAD - Violates LSP
class BirdBad {
    public void fly() {
        System.out.println("Bird flying");
    }
}

class OstrichBad extends BirdBad {
    public void fly() {
        throw new UnsupportedOperationException("Ostrich can't fly");
    }
}
// Problem:
// Child class breaking parent behavior


// ✅ GOOD - Follows LSP
abstract class Bird {
}

interface Flyable {
    void fly();
}

class Sparrow extends Bird implements Flyable {
    public void fly() {
        System.out.println("Sparrow flying");
    }
}

class Ostrich extends Bird {
    // No fly method
}
// Now substitution is safe



/* ======================================================
   4️⃣ I - INTERFACE SEGREGATION PRINCIPLE
   ====================================================== */

// ❌ BAD - Violates ISP
interface MachineBad {
    void print();
    void scan();
    void fax();
}

class BasicPrinterBad implements MachineBad {

    public void print() {
        System.out.println("Printing...");
    }

    public void scan() {
        throw new UnsupportedOperationException();
    }

    public void fax() {
        throw new UnsupportedOperationException();
    }
}
// Problem:
// Forced to implement unnecessary methods


// ✅ GOOD - Follows ISP
interface Printable {
    void print();
}

interface Scannable {
    void scan();
}

interface Faxable {
    void fax();
}

class BasicPrinter implements Printable {
    public void print() {
        System.out.println("Printing...");
    }
}

class AdvancedPrinter implements Printable, Scannable, Faxable {
    public void print() {
        System.out.println("Printing...");
    }

    public void scan() {
        System.out.println("Scanning...");
    }

    public void fax() {
        System.out.println("Faxing...");
    }
}



/* ======================================================
   5️⃣ D - DEPENDENCY INVERSION PRINCIPLE
   ====================================================== */

// ❌ BAD - Violates DIP
class EmailService {
    public void send() {
        System.out.println("Sending Email");
    }
}

class NotificationBad {

    private EmailService emailService = new EmailService();

    public void notifyUser() {
        emailService.send();
    }
}
// Problem:
// High-level class depends on concrete implementation



// ✅ GOOD - Follows DIP
interface MessageService {
    void send();
}

class EmailServiceGood implements MessageService {
    public void send() {
        System.out.println("Sending Email");
    }
}

class SMSService implements MessageService {
    public void send() {
        System.out.println("Sending SMS");
    }
}

class Notification {

    private MessageService messageService;

    public Notification(MessageService messageService) {
        this.messageService = messageService;
    }

    public void notifyUser() {
        messageService.send();
    }
}
// Now Notification depends on abstraction
// We can switch Email/SMS without modifying Notification