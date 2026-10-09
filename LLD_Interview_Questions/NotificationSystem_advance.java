import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/*
Functional:
- Send notification to multiple channels
- Respect user preferences

Non-Functional:
- Thread-safe
- Extensible
- Durable (in-memory persistence simulation)
- Async per channel
*/

enum NotificationChannelType {
    EMAIL,
    SMS,
    PUSH
}

enum NotificationStatus {
    PENDING,
    SENT,
    FAILED
}

/* ========================== ENTITY ========================== */

class Notification {
    private final long id;
    private final String userId;
    private final String message;
    private volatile NotificationStatus status;

    public Notification(long id, String userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.status = NotificationStatus.PENDING;
    }

    public long getId() { return id; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
}

/* ========================== CHANNEL ========================== */

interface NotificationChannel {
    void sendNotification(Notification notification);
}

class EmailNotificationChannel implements NotificationChannel {
    @Override
    public void sendNotification(Notification notification) {
        System.out.println("EMAIL sent to " + notification.getUserId());
    }
}

class SMSNotificationChannel implements NotificationChannel {
    @Override
    public void sendNotification(Notification notification) {
        System.out.println("SMS sent to " + notification.getUserId());
    }
}

class PushNotificationChannel implements NotificationChannel {
    @Override
    public void sendNotification(Notification notification) {
        System.out.println("PUSH sent to " + notification.getUserId());
    }
}

/* ========================== FACTORY ========================== */

class NotificationChannelFactory {

    private final Map<NotificationChannelType, NotificationChannel> channelMap =
            new ConcurrentHashMap<>();

    public NotificationChannelFactory() {
        channelMap.put(NotificationChannelType.EMAIL, new EmailNotificationChannel());
        channelMap.put(NotificationChannelType.SMS, new SMSNotificationChannel());
        channelMap.put(NotificationChannelType.PUSH, new PushNotificationChannel());
    }

    public NotificationChannel getChannel(NotificationChannelType type) {
        return channelMap.get(type);
    }
}

/* ========================== USER PREFERENCES ========================== */

class UserPreference {

    private final Map<String, List<NotificationChannelType>> preferences =
            new ConcurrentHashMap<>();

    public void addUserPreference(String userId, List<NotificationChannelType> channels) {
        preferences.put(userId, new ArrayList<>(channels));
    }

    public List<NotificationChannelType> getUserPreference(String userId) {
        return preferences.getOrDefault(userId, Collections.emptyList());
    }
}

/* ========================== DURABILITY (SIMULATED) ========================== */

class NotificationRepository {

    private final Map<Long, Notification> storage = new ConcurrentHashMap<>();

    public void save(Notification notification) {
        storage.put(notification.getId(), notification);
    }

    public void updateStatus(long id, NotificationStatus status) {
        Notification notification = storage.get(id);
        if (notification != null) {
            notification.setStatus(status);
        }
    }

    public Notification get(long id) {
        return storage.get(id);
    }
}

/* ========================== DISPATCHER ========================== */

class NotificationDispatcher {

    private final UserPreference userPreference;
    private final NotificationChannelFactory factory;
    private final NotificationRepository repository;

    private final ExecutorService executor;

    public NotificationDispatcher(UserPreference userPreference,
                                  NotificationChannelFactory factory,
                                  NotificationRepository repository) {

        this.userPreference = userPreference;
        this.factory = factory;
        this.repository = repository;

        // Bounded thread pool (production-style)
        this.executor = new ThreadPoolExecutor(
                5,
                10,
                60,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    public void dispatch(Notification notification) {

        List<NotificationChannelType> channels =
                userPreference.getUserPreference(notification.getUserId());

        if (channels.isEmpty()) {
            System.out.println("No preferences. Skipping.");
            return;
        }

        for (NotificationChannelType type : channels) {

            executor.submit(() -> {
                try {
                    NotificationChannel channel = factory.getChannel(type);
                    if (channel != null) {
                        channel.sendNotification(notification);
                        repository.updateStatus(notification.getId(), NotificationStatus.SENT);
                    }
                } catch (Exception e) {
                    repository.updateStatus(notification.getId(), NotificationStatus.FAILED);
                    System.out.println("Failed for channel: " + type);
                }
            });
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}

/* ========================== SERVICE ========================== */

class NotificationService {

    private final NotificationDispatcher dispatcher;
    private final NotificationRepository repository;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public NotificationService(NotificationDispatcher dispatcher,
                               NotificationRepository repository) {
        this.dispatcher = dispatcher;
        this.repository = repository;
    }

    public void sendNotification(String userId, String message) {

        long id = idGenerator.getAndIncrement();
        Notification notification = new Notification(id, userId, message);

        // Save first (durability simulation)
        repository.save(notification);

        dispatcher.dispatch(notification);
    }
}

/* ========================== MAIN ========================== */

public class NotificationSystem_advance {

    public static void main(String[] args) throws InterruptedException {

        UserPreference preference = new UserPreference();
        preference.addUserPreference("user1",
                Arrays.asList(NotificationChannelType.EMAIL,
                              NotificationChannelType.SMS,
                              NotificationChannelType.PUSH));

        NotificationChannelFactory factory = new NotificationChannelFactory();
        NotificationRepository repository = new NotificationRepository();

        NotificationDispatcher dispatcher =
                new NotificationDispatcher(preference, factory, repository);

        NotificationService service =
                new NotificationService(dispatcher, repository);

        service.sendNotification("user1", "Welcome to our platform!");

        Thread.sleep(2000); // allow async tasks to finish
        dispatcher.shutdown();
    }
}