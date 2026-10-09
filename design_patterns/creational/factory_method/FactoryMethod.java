package Week_2;

// Factory Method Pattern - Single File Example

//  Product Interface
interface Notification {
    void notifyUser();
}

//  Concrete Products
class EmailNotification implements Notification {
    public void notifyUser() {
        System.out.println("Email notification sent");
    }
}

class SMSNotification implements Notification {
    public void notifyUser() {
        System.out.println("SMS notification sent");
    }
}

//  Factory Interface (Creator)
interface NotificationFactory {
    Notification createNotification();   // factory method
}

// Concrete Factories
class EmailNotificationFactory implements NotificationFactory {
    public Notification createNotification() {
        return new EmailNotification();
    }
}

class SMSNotificationFactory implements NotificationFactory {
    public Notification createNotification() {
        return new SMSNotification();
    }
}

//  Client
public class FactoryMethod {

    public static void main(String[] args) {

        // Client decides WHICH factory to use
        NotificationFactory factory = new EmailNotificationFactory();
        Notification notification = factory.createNotification();
        notification.notifyUser();

        // Switching factory = switching product
        factory = new SMSNotificationFactory();
        notification = factory.createNotification();
        notification.notifyUser();
    }
}