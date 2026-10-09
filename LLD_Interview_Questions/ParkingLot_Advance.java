//parking lot advance
/*
Slot Allocation Strategy (Nearest / FirstAvailable)
Thread Safety
Entry / Exit Gate
Factory Pattern
Payment Modes (Strategy)
Pricing Strategy
Clean OOP


*/

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ParkingLot_Advance {

    /* ================= ENUMS ================= */

    enum VehicleType { BIKE, CAR, TRUCK }
    enum SpotType { BIKE, CAR, TRUCK }
    enum PaymentMode { CASH, CARD, UPI }

    /* ================= VEHICLE ================= */

    static class Vehicle {
        private String licenseNumber;
        private VehicleType type;

        public Vehicle(String licenseNumber, VehicleType type) {
            this.licenseNumber = licenseNumber;
            this.type = type;
        }

        public VehicleType getType() { return type; }
        public String getLicenseNumber() { return licenseNumber; }
    }

    /* ================= FACTORY PATTERN ================= */

    static class VehicleFactory {
        public static Vehicle createVehicle(String number, VehicleType type) {
            return new Vehicle(number, type);
        }
    }

    /* ================= PARKING SPOT ================= */

    static class ParkingSpot {
        private int spotId;
        private SpotType type;
        private boolean occupied;
        private Vehicle vehicle;
        private final ReentrantLock lock = new ReentrantLock();

        public ParkingSpot(int spotId, SpotType type) {
            this.spotId = spotId;
            this.type = type;
        }

        public boolean canFit(Vehicle vehicle) {
            return !occupied && vehicle.getType().name().equals(type.name());
        }

        public boolean park(Vehicle vehicle) {
            lock.lock();
            try {
                if (occupied) return false;
                this.vehicle = vehicle;
                this.occupied = true;
                return true;
            } finally {
                lock.unlock();
            }
        }

        public void unpark() {
            lock.lock();
            try {
                this.vehicle = null;
                this.occupied = false;
            } finally {
                lock.unlock();
            }
        }

        public boolean isOccupied() { return occupied; }
        public int getSpotId() { return spotId; }
        public SpotType getType() { return type; }
    }

    /* ================= PARKING FLOOR ================= */

    static class ParkingFloor {
        private int floorNumber;
        private List<ParkingSpot> spots;

        public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
            this.floorNumber = floorNumber;
            this.spots = spots;
        }

        public List<ParkingSpot> getSpots() { return spots; }
        public int getFloorNumber() { return floorNumber; }
    }

    /* ================= SLOT ALLOCATION STRATEGY ================= */

    interface SlotAllocationStrategy {
        ParkingSpot allocateSpot(Vehicle vehicle, List<ParkingFloor> floors);
    }

    // First Available Strategy
    static class FirstAvailableStrategy implements SlotAllocationStrategy {
        public ParkingSpot allocateSpot(Vehicle vehicle, List<ParkingFloor> floors) {
            for (ParkingFloor floor : floors) {
                for (ParkingSpot spot : floor.getSpots()) {
                    if (spot.canFit(vehicle) && spot.park(vehicle)) {
                        return spot;
                    }
                }
            }
            return null;
        }
    }

    // Nearest (Lowest Spot ID)
    static class NearestSpotStrategy implements SlotAllocationStrategy {
        public ParkingSpot allocateSpot(Vehicle vehicle, List<ParkingFloor> floors) {
            ParkingSpot best = null;

            for (ParkingFloor floor : floors) {
                for (ParkingSpot spot : floor.getSpots()) {
                    if (spot.canFit(vehicle)) {
                        if (best == null || spot.getSpotId() < best.getSpotId()) {
                            best = spot;
                        }
                    }
                }
            }

            if (best != null && best.park(vehicle)) return best;
            return null;
        }
    }

    /* ================= TICKET ================= */

    static class Ticket {
        private String ticketId;
        private Vehicle vehicle;
        private ParkingSpot spot;
        private LocalDateTime entryTime;
        private LocalDateTime exitTime;

        public Ticket(Vehicle vehicle, ParkingSpot spot) {
            this.ticketId = UUID.randomUUID().toString();
            this.vehicle = vehicle;
            this.spot = spot;
            this.entryTime = LocalDateTime.now();
        }

        public void close() {
            this.exitTime = LocalDateTime.now();
        }

        public long getHours() {
            return Math.max(1,
                    Duration.between(entryTime, exitTime).toHours());
        }

        public String getTicketId() { return ticketId; }
        public ParkingSpot getSpot() { return spot; }
        public Vehicle getVehicle() { return vehicle; }
    }

    /* ================= PRICING STRATEGY ================= */

    interface PricingStrategy {
        double calculate(long hours, VehicleType type);
    }

    static class HourlyPricingStrategy implements PricingStrategy {
        public double calculate(long hours, VehicleType type) {
            switch (type) {
                case BIKE: return hours * 10;
                case CAR: return hours * 20;
                case TRUCK: return hours * 30;
                default: return hours * 20;
            }
        }
    }

    /* ================= PAYMENT STRATEGY ================= */

    interface PaymentStrategy {
        void pay(double amount);
    }

    static class CashPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Paid " + amount + " via CASH");
        }
    }

    static class CardPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Paid " + amount + " via CARD");
        }
    }

    static class UPIPayment implements PaymentStrategy {
        public void pay(double amount) {
            System.out.println("Paid " + amount + " via UPI");
        }
    }

    static class PaymentFactory {
        public static PaymentStrategy getPaymentMode(PaymentMode mode) {
            switch (mode) {
                case CASH: return new CashPayment();
                case CARD: return new CardPayment();
                case UPI: return new UPIPayment();
                default: throw new RuntimeException("Invalid payment mode");
            }
        }
    }

    /* ================= PARKING LOT ================= */

    static class ParkingLot {

        private List<ParkingFloor> floors;
        private SlotAllocationStrategy allocationStrategy;
        private PricingStrategy pricingStrategy;
        private Map<String, Ticket> activeTickets = new ConcurrentHashMap<>();

        public ParkingLot(List<ParkingFloor> floors,
                          SlotAllocationStrategy allocationStrategy,
                          PricingStrategy pricingStrategy) {
            this.floors = floors;
            this.allocationStrategy = allocationStrategy;
            this.pricingStrategy = pricingStrategy;
        }

        public Ticket park(Vehicle vehicle) {
            ParkingSpot spot =
                    allocationStrategy.allocateSpot(vehicle, floors);

            if (spot == null)
                throw new RuntimeException("No spot available");

            Ticket ticket = new Ticket(vehicle, spot);
            activeTickets.put(ticket.getTicketId(), ticket);

            System.out.println("Vehicle parked at Spot: " + spot.getSpotId());
            return ticket;
        }

        public double unpark(String ticketId, PaymentMode mode) {
            Ticket ticket = activeTickets.get(ticketId);
            if (ticket == null)
                throw new RuntimeException("Invalid ticket");

            ticket.close();
            ticket.getSpot().unpark();

            double amount =
                    pricingStrategy.calculate(
                            ticket.getHours(),
                            ticket.getVehicle().getType());

            PaymentStrategy payment =
                    PaymentFactory.getPaymentMode(mode);

            payment.pay(amount);

            activeTickets.remove(ticketId);

            return amount;
        }
    }

    /* ================= ENTRY / EXIT GATES ================= */

    static class EntryGate {
        private ParkingLot parkingLot;

        public EntryGate(ParkingLot parkingLot) {
            this.parkingLot = parkingLot;
        }

        public Ticket processEntry(Vehicle vehicle) {
            return parkingLot.park(vehicle);
        }
    }

    static class ExitGate {
        private ParkingLot parkingLot;

        public ExitGate(ParkingLot parkingLot) {
            this.parkingLot = parkingLot;
        }

        public void processExit(String ticketId, PaymentMode mode) {
            parkingLot.unpark(ticketId, mode);
        }
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) throws Exception {

        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot(1, SpotType.BIKE),
                new ParkingSpot(2, SpotType.CAR),
                new ParkingSpot(3, SpotType.TRUCK),
                new ParkingSpot(4, SpotType.CAR)
        );

        ParkingFloor floor1 = new ParkingFloor(1, spots);

        ParkingLot parkingLot = new ParkingLot(
                Arrays.asList(floor1),
                new NearestSpotStrategy(),     // change to FirstAvailableStrategy()
                new HourlyPricingStrategy()
        );

        EntryGate entryGate = new EntryGate(parkingLot);
        ExitGate exitGate = new ExitGate(parkingLot);

        Vehicle car =
                VehicleFactory.createVehicle("HR26AB1234", VehicleType.CAR);

        Ticket ticket = entryGate.processEntry(car);

        Thread.sleep(2000);

        exitGate.processExit(ticket.getTicketId(), PaymentMode.UPI);
    }
}