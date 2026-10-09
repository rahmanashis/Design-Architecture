import java.util.*;

/*
    Fully Asynchronous Elevator System
    - Thread per elevator
    - External + Internal Requests
    - Thread-safe stop management
*/

public class AsyncElevatorSystemDemo {

    /* ================= ENUMS ================= */

    enum Direction {
        UP, DOWN, IDLE
    }

    enum ElevatorState {
        MOVING, STOPPED, IDLE
    }

    /* ================= REQUESTS ================= */

    static class ExternalRequest {
        int sourceFloor;
        Direction direction;

        public ExternalRequest(int sourceFloor, Direction direction) {
            this.sourceFloor = sourceFloor;
            this.direction = direction;
        }
    }

    static class InternalRequest {
        int destinationFloor;

        public InternalRequest(int destinationFloor) {
            this.destinationFloor = destinationFloor;
        }
    }

    /* ================= ELEVATOR ================= */

    static class Elevator implements Runnable {

            private int id;
            private volatile int currentFloor = 0;
            private volatile Direction direction = Direction.IDLE;
            private volatile ElevatorState state = ElevatorState.IDLE;

            private final TreeSet<Integer> upStops = new TreeSet<>();
            private final TreeSet<Integer> downStops =
                    new TreeSet<>(Collections.reverseOrder());

            private final Object lock = new Object();

        public Elevator(int id) {
                this.id = id;
            }

            public int getId() {
                return id;
            }

            public int getCurrentFloor() {
                return currentFloor;
        }

        /* Add stop safely */
        public void addStop(int floor) {
            synchronized (lock) {
                if (floor > currentFloor) {
                    upStops.add(floor);
                } else if (floor < currentFloor) {
                    downStops.add(floor);
                }
                lock.notify();  // wake up elevator thread
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (upStops.isEmpty() && downStops.isEmpty()) {
                        try {
                            state = ElevatorState.IDLE;
                            direction = Direction.IDLE;
                            lock.wait();   // wait for new request
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

                processStops();
            }
        }

        private void processStops() {
            if (!upStops.isEmpty()) {
                direction = Direction.UP;
                moveUp();
            } else if (!downStops.isEmpty()) {
                direction = Direction.DOWN;
                moveDown();
            }
        }

        private void moveUp() {
            while (true) {
                synchronized (lock) {
                    if (upStops.isEmpty()) break;
                }

                state = ElevatorState.MOVING;
                currentFloor++;
                print("Moving UP to floor " + currentFloor);

                sleep();

                synchronized (lock) {
                    if (upStops.contains(currentFloor)) {
                        upStops.remove(currentFloor);
                        stop();
                    }
                }
            }
        }

        private void moveDown() {
            while (true) {
                synchronized (lock) {
                    if (downStops.isEmpty()) break;
                }

                state = ElevatorState.MOVING;
                currentFloor--;
                print("Moving DOWN to floor " + currentFloor);

                sleep();

                synchronized (lock) {
                    if (downStops.contains(currentFloor)) {
                        downStops.remove(currentFloor);
                        stop();
                    }
                }
            }
        }

        private void stop() {
            state = ElevatorState.STOPPED;
            print("Stopped at floor " + currentFloor);
            openDoor();
            closeDoor();
        }

        private void openDoor() {
            print("Door opening...");
            sleep();
        }

        private void closeDoor() {
            print("Door closing...");
            sleep();
        }

        private void sleep() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        private void print(String message) {
            System.out.println("Elevator " + id + ": " + message);
        }
    }

    /* ================= SCHEDULER ================= */

    interface Scheduler {
        ElevatorController selectElevator(
                List<ElevatorController> controllers,
                ExternalRequest request);
    }

    static class NearestElevatorStrategy implements Scheduler {

        @Override
        public ElevatorController selectElevator(
                List<ElevatorController> controllers,
                ExternalRequest request) {

            ElevatorController best = null;
            int minDistance = Integer.MAX_VALUE;

            for (ElevatorController controller : controllers) {
                int distance = Math.abs(
                        controller.getElevator().getCurrentFloor()
                                - request.sourceFloor);

                if (distance < minDistance) {
                    minDistance = distance;
                    best = controller;
                }
            }
            return best;
        }
    }

    /* ================= CONTROLLER ================= */

    static class ElevatorController {

        private Elevator elevator;

        public ElevatorController(Elevator elevator) {
            this.elevator = elevator;
        }

        public Elevator getElevator() {
            return elevator;
        }

        public void handleExternalRequest(ExternalRequest request) {
            elevator.addStop(request.sourceFloor);
        }

        public void handleInternalRequest(InternalRequest request) {
            elevator.addStop(request.destinationFloor);
        }
    }

    /* ================= SYSTEM ================= */

    static class ElevatorSystem {

        private List<ElevatorController> controllers = new ArrayList<>();
        private Scheduler scheduler = new NearestElevatorStrategy();

        public ElevatorSystem(int numberOfElevators) {

            for (int i = 0; i < numberOfElevators; i++) {
                Elevator elevator = new Elevator(i);
                ElevatorController controller =
                        new ElevatorController(elevator);

                controllers.add(controller);

                Thread thread = new Thread(elevator);
                thread.start();   // start async elevator
            }
        }

        public void submitExternalRequest(int floor, Direction direction) {
            ExternalRequest request =
                    new ExternalRequest(floor, direction);

            ElevatorController controller =
                    scheduler.selectElevator(controllers, request);

            System.out.println(
                    "External request assigned to Elevator "
                            + controller.getElevator().getId());

            controller.handleExternalRequest(request);
        }

        public void submitInternalRequest(int elevatorId, int destination) {
            InternalRequest request =
                    new InternalRequest(destination);

            controllers.get(elevatorId)
                    .handleInternalRequest(request);
        }
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) throws InterruptedException {

        ElevatorSystem system = new ElevatorSystem(2);

        // External request
        system.submitExternalRequest(3, Direction.UP);

        Thread.sleep(2000);

        // Internal request (destination)
        system.submitInternalRequest(0, 7);

        Thread.sleep(5000);
    }
}