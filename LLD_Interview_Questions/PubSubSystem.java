import java.util.*;
import java.util.concurrent.*;

class Message {
    private String id;
    private String payload;

    public Message(String id, String payload) {
        this.id = id;
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }
}

interface Subscriber {
    void consume(Message message);
}

class Topic {

    private String name;
    private List<Subscriber> subscribers = new CopyOnWriteArrayList<>();
    private ExecutorService executor = Executors.newCachedThreadPool();

    public Topic(String name) {
        this.name = name;
    }

    public void addSubscriber(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void removeSubscriber(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public void publish(Message message) {

        for (Subscriber subscriber : subscribers) {

            executor.submit(() -> {
                subscriber.consume(message);
            });

        }
    }
}

class MessageBroker {

    private Map<String, Topic> topics = new ConcurrentHashMap<>();

    public void createTopic(String topicName) {
        topics.putIfAbsent(topicName, new Topic(topicName));
    }

    public void subscribe(String topicName, Subscriber subscriber) {

        Topic topic = topics.get(topicName);
        if (topic != null) {
            topic.addSubscriber(subscriber);
        }
    }

    public void unsubscribe(String topicName, Subscriber subscriber) {

        Topic topic = topics.get(topicName);
        if (topic != null) {
            topic.removeSubscriber(subscriber);
        }
    }

    public void publish(String topicName, Message message) {

        Topic topic = topics.get(topicName);

        if (topic != null) {
            topic.publish(message);
        }
    }
}

class InventorySubscriber implements Subscriber {

    public void consume(Message message) {
        System.out.println("Inventory received: " + message.getPayload());
    }
}

class NotificationSubscriber implements Subscriber {

    public void consume(Message message) {
        System.out.println("Notification received: " + message.getPayload());
    }
}

public class PubSubSystem {

    public static void main(String[] args) {

        MessageBroker broker = new MessageBroker();

        broker.createTopic("order");

        Subscriber inventory = new InventorySubscriber();
        Subscriber notification = new NotificationSubscriber();

        broker.subscribe("order", inventory);
        broker.subscribe("order", notification);

        Message message = new Message("1", "Order 101 created");

        broker.publish("order", message);
    }
}