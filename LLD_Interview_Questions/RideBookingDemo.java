/*
Functional Requirements

	User can request a ride
	System assigns nearest available driver
	Driver can accept/reject ride
	Ride has lifecycle:
		REQUESTED
		ACCEPTED
		STARTED
		COMPLETED
		CANCELLED

	Fare calculation (basic distance-based)
	Multiple drivers and users

Out of Scope (to control complexity)
	Payments
	Surge pricing
	Maps API
	Real-time GPS tracking
	Distributed system


User → RideService → DriverManager
                      ↓
                 Driver Matching
                      ↓
                    Ride


Core Entities:
	User
	Driver
	Location
	Ride
	RideStatus (enum)
	DriverStatus (enum)
	RideService
	DriverManager
	FareStrategy (Strategy Pattern)


Design Patterns Used
	Strategy Pattern → Fare calculation
	Singleton → RideService
	SRP → Matching separate from ride logic
	Open/Closed → Add new fare strategy easily


User
Driver
Location

Ride
 - User
 - Driver
 - status

RideService
 - requestRide()
 - updateRideStatus()

DriverManager
 - findNearestDriver()

FareStrategy
 - calculateFare()

*/


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// ================= ENUMS =================
enum RideStatus {
    REQUESTED, ACCEPTED, STARTED, COMPLETED, CANCELLED
}

enum DriverStatus {
    AVAILABLE, BUSY
}

// ================= LOCATION =================
class Location {
    double x;
    double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double distance(Location other) {
        return Math.sqrt(
                Math.pow(this.x - other.x, 2) +
                Math.pow(this.y - other.y, 2)
        );
    }
}

// ================= USER =================
class User {
    private String id;
    private String name;
    private Location location;

    public User(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }
}

// ================= DRIVER =================
class Driver {
    private String id;
    private String name;
    private Location location;
    private DriverStatus status;

    public Driver(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.status = DriverStatus.AVAILABLE;
    }

    public synchronized boolean assignRide() {
        if (status == DriverStatus.AVAILABLE) {
            status = DriverStatus.BUSY;
            return true;
        }
        return false;
    }

    public synchronized void completeRide() {
        status = DriverStatus.AVAILABLE;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public Location getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }
}

// ================= RIDE =================
class Ride {
    private String id;
    private User user;
    private Driver driver;
    private RideStatus status;
    private double fare;

    public Ride(String id, User user, Driver driver) {
        this.id = id;
        this.user = user;
        this.driver = driver;
        this.status = RideStatus.REQUESTED;
    }

    public void updateStatus(RideStatus status) {
        this.status = status;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public RideStatus getStatus() {
        return status;
    }

    public Driver getDriver() {
        return driver;
    }
}

// ================= STRATEGY =================
interface FareStrategy {
    double calculateFare(Location from, Location to);
}

class DistanceFareStrategy implements FareStrategy {

    private static final double RATE_PER_KM = 10.0;

    @Override
    public double calculateFare(Location from, Location to) {
        return from.distance(to) * RATE_PER_KM;
    }
}

// ================= DRIVER MANAGER =================
class DriverManager {

    private List<Driver> drivers = new ArrayList<>();

    public void addDriver(Driver driver) {
        drivers.add(driver);
    }

    public Driver findNearestAvailableDriver(Location userLocation) {

        Driver nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Driver driver : drivers) {
            if (driver.getStatus() == DriverStatus.AVAILABLE) {
                double distance = driver.getLocation().distance(userLocation);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearest = driver;
                }
            }
        }
        return nearest;
    }
}

// ================= RIDE SERVICE =================
class RideService {

    private static RideService instance;
    private DriverManager driverManager;
    private FareStrategy fareStrategy;
    private Map<String, Ride> rides = new ConcurrentHashMap<>();

    private RideService(DriverManager driverManager, FareStrategy fareStrategy) {
        this.driverManager = driverManager;
        this.fareStrategy = fareStrategy;
    }

    public static synchronized RideService getInstance(
            DriverManager driverManager,
            FareStrategy fareStrategy
    ) {
        if (instance == null) {
            instance = new RideService(driverManager, fareStrategy);
        }
        return instance;
    }

    public Ride requestRide(User user, Location destination) {

        Driver driver = driverManager.findNearestAvailableDriver(user.getLocation());

        if (driver == null) {
            throw new RuntimeException("No drivers available");
        }

        boolean assigned = driver.assignRide();

        if (!assigned) {
            throw new RuntimeException("Driver already assigned");
        }

        Ride ride = new Ride(UUID.randomUUID().toString(), user, driver);
        ride.updateStatus(RideStatus.ACCEPTED);

        double fare = fareStrategy.calculateFare(user.getLocation(), destination);
        ride.setFare(fare);

        rides.put(ride.toString(), ride);

        return ride;
    }

    public void completeRide(Ride ride) {
        ride.updateStatus(RideStatus.COMPLETED);
        ride.getDriver().completeRide();
    }
}

// ================= DEMO =================
public class RideBookingDemo {

    public static void main(String[] args) {

        DriverManager driverManager = new DriverManager();

        driverManager.addDriver(new Driver("D1", "Driver1", new Location(0, 0)));
        driverManager.addDriver(new Driver("D2", "Driver2", new Location(5, 5)));

        RideService rideService = RideService.getInstance(
                driverManager,
                new DistanceFareStrategy()
        );

        User user = new User("U1", "Pradeep", new Location(1, 1));

        Ride ride = rideService.requestRide(user, new Location(10, 10));

        System.out.println("Ride booked successfully!");

        rideService.completeRide(ride);

        System.out.println("Ride completed!");
    }
}
