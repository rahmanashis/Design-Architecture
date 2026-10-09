
/*
we'll design a multi-elevator system like in a corporate bulding

Requirments:
Multiple elevators in a building
User can:
call elevator(UP/DOWN) from a floor
select destination floor inside elevator

Elevator should:
	move up/down
	open/close door
	stop at requested floors

Non-functional 
efficient assignment of elevator
extensible
thread-safe

Core entities:
	ElevatorSystem
	Elevator
	ElevatorController --> control panel of the elevator
	Scheduler(Strategy Pattern)
	Request
	Direction (enum)
	ElevatorState(State Pattern)


ElevatorSystem
	--> List<ElevatorController>

ElevatorController
	-> Elevator
	-> Scheduler

Elevator
	-> currentFloor
	-> direction
	-> state
	-> upStops
	-> downStops

User
  ↓
ExternalRequest
  ↓
ElevatorSystem
  ↓
Scheduler
  ↓
ElevatorController
  ↓
Elevator arrives
  ↓
InternalRequest
  ↓
Elevator processes destination

*/

//User --> ElevatorSystem --> Scheduler --> ElevatorController --> Elevator(Execution Only)

/*
    Elevator System LLD
    - Supports External & Internal Requests
    - Multi-elevator
    - Strategy based scheduling
*/


    import java.util.*;

/*
    Elevator System LLD
    - Supports External & Internal Requests
    - Multi-elevator
    - Strategy based scheduling
*/

public class ElevatorSystem_basics {

    /* ================= ENUMS ================= */

    enum Direction {
        UP, DOWN, IDLE
    }

    enum ElevatorState {
        MOVING, STOPPED, IDLE
    }

    /* ================= REQUESTS ================= */

    // External request (floor panel)
    static class ExternalRequest {
        int sourceFloor;
        Direction direction;

        public ExternalRequest(int sourceFloor, Direction direction) {
            this.sourceFloor = sourceFloor;
            this.direction = direction;
        }
    }

    // Internal request (cabin panel)
    static class InternalRequest {
        int destinationFloor;

        public InternalRequest(int destinationFloor) {
            this.destinationFloor = destinationFloor;
        }
    }

    /* ================= ELEVATOR ================= */

    static class Elevator {

        private int id;
        private int currentFloor;
        private Direction direction;
        private ElevatorState state;

        // Sorted stops
        private TreeSet<Integer> upStops;
        private TreeSet<Integer> downStops;

        public Elevator(int id) {
            this.id = id;
            this.currentFloor = 0;
            this.direction = Direction.IDLE;
            this.state = ElevatorState.IDLE;

            upStops = new TreeSet<>();
            downStops = new TreeSet<>(Collections.reverseOrder());
        }

        public int getCurrentFloor() {
            return currentFloor;
        }

        public Direction getDirection() {
            return direction;
        }

        public int getId() {
            return id;
        }

        /* Add stop (internal or external) */
        public void addStop(int floor) {
            if (floor > currentFloor) {
                upStops.add(floor);
            } else if (floor < currentFloor) {
                downStops.add(floor);
            }
        }

        /* Start processing stops */
        public void processStops() {
            if (!upStops.isEmpty()) {
                direction = Direction.UP;
                moveUp();
            } else if (!downStops.isEmpty()) {
                direction = Direction.DOWN;
                moveDown();
            } else {
                direction = Direction.IDLE;
                state = ElevatorState.IDLE;
            }
        }

        private void moveUp() {
            while (!upStops.isEmpty()) {
                state = ElevatorState.MOVING;
                currentFloor++;
                System.out.println("Elevator " + id + " at floor " + currentFloor);

                if (upStops.contains(currentFloor)) {
                    upStops.remove(currentFloor);
                    stop();
                }
            }
        }

        private void moveDown() {
            while (!downStops.isEmpty()) {
                state = ElevatorState.MOVING;
                currentFloor--;
                System.out.println("Elevator " + id + " at floor " + currentFloor);

                if (downStops.contains(currentFloor)) {
                    downStops.remove(currentFloor);
                    stop();
                }
            }
        }

        private void stop() {
            state = ElevatorState.STOPPED;
            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
            openDoor();
            closeDoor();
        }

        private void openDoor() {
            System.out.println("Door opening...");
        }

        private void closeDoor() {
            System.out.println("Door closing...");
        }
    }

    /* ================= SCHEDULER ================= */

    interface Scheduler {
        ElevatorController selectElevator(
                List<ElevatorController> controllers,
                ExternalRequest request);
    }

    // Simple Nearest Elevator Strategy
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

        // Handle pickup request
        public void handleExternalRequest(ExternalRequest request) {
            elevator.addStop(request.sourceFloor);
            elevator.processStops();
        }

        // Handle destination request
        public void handleInternalRequest(InternalRequest request) {
            elevator.addStop(request.destinationFloor);
            elevator.processStops();
        }
    }

    /* ================= SYSTEM ================= */

    static class ElevatorSystem {

        private List<ElevatorController> controllers;
        private Scheduler scheduler;

        public ElevatorSystem(int numberOfElevators) {
            controllers = new ArrayList<>();
            scheduler = new NearestElevatorStrategy();

            for (int i = 0; i < numberOfElevators; i++) {
                controllers.add(
                        new ElevatorController(new Elevator(i)));
            }
        }

        // User presses floor button
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

        // User inside cabin presses destination
        public void submitInternalRequest(int elevatorId, int destination) {

            InternalRequest request =
                    new InternalRequest(destination);

            ElevatorController controller =
                    controllers.get(elevatorId);

            controller.handleInternalRequest(request);
        }
    }

    /* ================= MAIN ================= */

    public static void main(String[] args) {

        ElevatorSystem system = new ElevatorSystem(2);

        // Person at floor 3 presses UP
        system.submitExternalRequest(3, Direction.UP);

        // Assume passenger entered Elevator 0 and pressed 7
        system.submitInternalRequest(0, 7);
    }
}