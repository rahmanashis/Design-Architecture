public class OptimisticLockingSimulation {

    enum SeatStatus {
        AVAILABLE,
        BOOKED
    }

    static class Seat {
        private SeatStatus status = SeatStatus.AVAILABLE;
        private int version = 0;

        public SeatStatus getStatus() {
            return status;
        }

        public int getVersion() {
            return version;
        }

        // This simulates atomic DB update
        public synchronized boolean updateIfVersionMatches(int expectedVersion) {

            if (this.version != expectedVersion) {
                return false; // someone else updated
            }

            // simulate update
            this.status = SeatStatus.BOOKED;
            this.version++;
            return true;
        }
    }

    static class BookingTask implements Runnable {

        private final Seat seat;
        private final String user;

        public BookingTask(Seat seat, String user) {
            this.seat = seat;
            this.user = user;
        }

        @Override
        public void run() {

            // Step 1: Read seat
            int version = seat.getVersion();

            if (seat.getStatus() != SeatStatus.AVAILABLE) {
                System.out.println(user + " → Seat already booked");
                return;
            }

            // simulate delay (race condition window)
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Step 2: Try atomic update
            boolean success = seat.updateIfVersionMatches(version);

            if (success) {
                System.out.println(user + " → Successfully booked seat!");
            } else {
                System.out.println(user + " → Failed due to version mismatch");
            }
        }
    }

    public static void main(String[] args) {

        Seat seat = new Seat();

        Thread user1 = new Thread(new BookingTask(seat, "User1"));
        Thread user2 = new Thread(new BookingTask(seat, "User2"));
        Thread user3 = new Thread(new BookingTask(seat, "User3"));
        Thread user4 = new Thread(new BookingTask(seat, "User4"));

        user1.start();
        user2.start();
        user3.start();
        user4.start();
    }
}