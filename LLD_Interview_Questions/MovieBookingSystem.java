import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Simple movie ticket booking system with seat locking and payment

enum SeatStatus {
    AVAILABLE,
    LOCKED,
    BOOKED
}

class Seat {
    private final String seatId;
    private SeatStatus status = SeatStatus.AVAILABLE;

    public Seat(String seatId) {
        this.seatId = seatId;
    }

    public String getSeatId() {
        return seatId;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }
}

class Screen {
    private final String screenId;
    private final List<Seat> seats;

    public Screen(String screenId, List<Seat> seats) {
        this.screenId = screenId;
        this.seats = seats;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}

class Movie {
    private final String movieId;
    private final String title;

    public Movie(String movieId, String title) {
        this.movieId = movieId;
        this.title = title;
    }
}

class Show {
    private final String showId;
    private final Movie movie;
    private final Screen screen;

    public Show(String showId, Movie movie, Screen screen) {
        this.showId = showId;
        this.movie = movie;
        this.screen = screen;
    }

    public String getShowId() {
        return showId;
    }

    public Screen getScreen() {
        return screen;
    }
}

class User {
    private final String userId;

    public User(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}

class Booking {
    private final String bookingId;
    private final User user;
    private final Show show;
    private final List<String> seatIds;

    public Booking(String bookingId, User user, Show show, List<String> seatIds) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.seatIds = seatIds;
    }
}

class SeatLock {

    private final String seatId;
    private final String showId;
    private final User user;
    private final long lockTime;

    public SeatLock(String seatId, String showId, User user) {
        this.seatId = seatId;
        this.showId = showId;
        this.user = user;
        this.lockTime = System.currentTimeMillis();
    }

    public String getSeatId() {
        return seatId;
    }

    public String getShowId() {
        return showId;
    }
}

interface ShowRepository {
    Show findById(String showId);
}

class InMemoryShowRepository implements ShowRepository {

    private final Map<String, Show> shows = new HashMap<>();

    public void addShow(Show show) {
        shows.put(show.getShowId(), show);
    }

    public Show findById(String showId) {
        return shows.get(showId);
    }
}

interface BookingRepository {
    void save(Booking booking);
}

class InMemoryBookingRepository implements BookingRepository {

    private final Map<String, Booking> bookings = new HashMap<>();

    public void save(Booking booking) {
        bookings.put(UUID.randomUUID().toString(), booking);
    }
}

class SeatLockManager {

    private final Map<String, SeatLock> lockedSeats = new ConcurrentHashMap<>();

    private String getKey(String showId, String seatId) {
        return showId + "_" + seatId;
    }

    public synchronized boolean lockSeats(User user, String showId, List<String> seatIds) {

        for (String seatId : seatIds) {
            String key = getKey(showId, seatId);
            if (lockedSeats.containsKey(key)) {
                return false;
            }
        }

        for (String seatId : seatIds) {
            String key = getKey(showId, seatId);
            lockedSeats.put(key, new SeatLock(seatId, showId, user));
        }

        return true;
    }

    public synchronized void unlockSeats(String showId, List<String> seatIds) {
        for (String seatId : seatIds) {
            String key = getKey(showId, seatId);
            lockedSeats.remove(key);
        }
    }

    public synchronized void confirmSeats(String showId, List<String> seatIds) {
        for (String seatId : seatIds) {
            String key = getKey(showId, seatId);
            lockedSeats.remove(key);
        }
    }
}

class PaymentService {

    public boolean processPayment(User user, double amount) {
        System.out.println("Payment processed for user " + user.getUserId()
                + " amount: " + amount);
        return true;
    }
}

class BookingService {

    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final SeatLockManager lockManager;
    private final PaymentService paymentService;

    public BookingService(
            ShowRepository showRepository,
            BookingRepository bookingRepository,
            SeatLockManager lockManager,
            PaymentService paymentService) {

        this.showRepository = showRepository;
        this.bookingRepository = bookingRepository;
        this.lockManager = lockManager;
        this.paymentService = paymentService;
    }

    public boolean bookSeats(User user, String showId, List<String> seatIds) {

        Show show = showRepository.findById(showId);
        if (show == null) {
            System.out.println("Show not found");
            return false;
        }

        boolean locked = lockManager.lockSeats(user, showId, seatIds);

        if (!locked) {
            System.out.println("Seats already locked");
            return false;
        }

        boolean paymentSuccess = paymentService.processPayment(user, 500);

        if (!paymentSuccess) {
            lockManager.unlockSeats(showId, seatIds);
            return false;
        }

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                user,
                show,
                seatIds
        );

        bookingRepository.save(booking);
        lockManager.confirmSeats(showId, seatIds);

        System.out.println("Booking successful");

        return true;
    }

    // add other functionalities: search, recommendation, etc.
}

public class MovieBookingSystem {

    public static void main(String[] args) {

        List<Seat> seats = Arrays.asList(
                new Seat("A1"),
                new Seat("A2"),
                new Seat("A3")
        );

        Screen screen = new Screen("Screen1", seats);

        Movie movie = new Movie("M1", "Avengers");

        Show show = new Show("SHOW1", movie, screen);

        InMemoryShowRepository showRepo = new InMemoryShowRepository();
        showRepo.addShow(show);

        InMemoryBookingRepository bookingRepo = new InMemoryBookingRepository();

        SeatLockManager lockManager = new SeatLockManager();

        PaymentService paymentService = new PaymentService();

        BookingService bookingService =
                new BookingService(showRepo, bookingRepo, lockManager, paymentService);

        User user = new User("U1");

        bookingService.bookSeats(user, "SHOW1", Arrays.asList("A1", "A2"));
    }
}



Entities
    Seat 
    SeatLock 
    Movie

Repositiory
Services

Concurrency Handling