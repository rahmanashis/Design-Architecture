package week_4;

import java.util.*;

// ================= Observer =================
interface Observer {
    void update(int price);
}

// ================= Subject =================
interface Subject {
    void addObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
}

// ================= Concrete Subject =================
class Stock implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private int price;

    public void setPrice(int price) {
        this.price = price;
        notifyObservers();
    }

    public int getPrice() {
        return price;
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update(price);
        }
    }
}

// ================= Concrete Observers =================
class EmailService implements Observer {
    @Override
    public void update(int price) {
        System.out.println("Email sent: Stock price = " + price);
    }
}

class SMSService implements Observer {
    @Override
    public void update(int price) {
        System.out.println("SMS sent: Stock price = " + price);
    }
}

class MobileApp implements Observer {
    @Override
    public void update(int price) {
        System.out.println("Mobile app updated: Stock price = " + price);
    }
}

// ================= Client =================
public class ObserverPatternDemo {
    public static void main(String[] args) {

        Stock stock = new Stock();

        Observer email = new EmailService();
        Observer sms = new SMSService();
        Observer app = new MobileApp();

        stock.addObserver(email);
        stock.addObserver(sms);
        stock.addObserver(app);

        stock.setPrice(100);
        stock.setPrice(120);

        stock.removeObserver(sms);

        stock.setPrice(150);
    }
}