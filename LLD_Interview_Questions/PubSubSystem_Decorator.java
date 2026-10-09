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

    public String getId() {
        return id;
    }
}

interface Subscriber {
    void consume(Message message) throws Exception;
}

abstract class SubscriberDecorator implements Subscriber {

    protected Subscriber subscriber;

    public SubscriberDecorator(Subscriber subscriber) {
        this.subscriber = subscriber;
    }
}

class RetryDecorator extends SubscriberDecorator {

    private int maxRetries;
    private long delay;

    public RetryDecorator(Subscriber subscriber, int maxRetries, long delay) {
        super(subscriber);
        this.maxRetries = maxRetries;
        this.delay = delay;
    }

    public void consume(Message message) throws Exception {

        int attempt = 0;

        while (attempt <= maxRetries) {

            try {

                subscriber.consume(message);
                return;

            } catch (Exception e) {

                attempt++;

                if (attempt > maxRetries) {
                    throw e;
                }

                Thread.sleep(delay);
            }
        }
    }
}

class DLQDecorator extends SubscriberDecorator {

    private DeadLetterQueue dlq;

    public DLQDecorator(Subscriber subscriber, DeadLetterQueue dlq) {
        super(subscriber);
        this.dlq = dlq;
    }

    public void consume(Message message) {

        try {

            subscriber.consume(message);

        } catch (Exception e) {

            dlq.add(message);
        }
    }
}

class DeadLetterQueue {

    private List<Message> failedMessages = new CopyOnWriteArrayList<>();

    public void add(Message message) {

        failedMessages.add(message);

        System.out.println("Moved to DLQ: " + message.getPayload());
    }
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

    public void publish(Message message) {

        for (Subscriber subscriber : subscribers) {

            executor.submit(() -> {

                try {
                    subscriber.consume(message);
                } catch (Exception ignored) {
                }

            });
        }
    }
}

class MessageBroker {

    private Map<String, Topic> topics = new ConcurrentHashMap<>();

    public void createTopic(String name) {
        topics.putIfAbsent(name, new Topic(name));
    }

    public void subscribe(String topic, Subscriber subscriber) {

        Topic t = topics.get(topic);

        if (t != null)
            t.addSubscriber(subscriber);
    }

    public void publish(String topic, Message message) {

        Topic t = topics.get(topic);

        if (t != null)
            t.publish(message);
    }
}

class InventorySubscriber implements Subscriber {

    public void consume(Message message) {

        System.out.println("Inventory processed: " + message.getPayload());
    }
}

class EmailSubscriber implements Subscriber {

    public void consume(Message message) throws Exception {

        System.out.println("Email service processing...");

        throw new Exception("Email service failure");
    }
}

public class PubSubSystem {

    public static void main(String[] args) {

        MessageBroker broker = new MessageBroker();
        broker.createTopic("order");

        DeadLetterQueue dlq = new DeadLetterQueue();

        Subscriber inventory = new InventorySubscriber();

        Subscriber email =
                new RetryDecorator(
                        new DLQDecorator(
                                new EmailSubscriber(),
                                dlq
                        ),
                        3,
                        1000
                );

        broker.subscribe("order", inventory);
        broker.subscribe("order", email);

        broker.publish("order",
                new Message("1", "Order Created"));
    }
}