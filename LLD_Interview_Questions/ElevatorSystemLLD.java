package LLD_Interview_Questions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class ElevatorSystemLLD {

    enum Direction {
        UP, DOWN, IDLE
    }

    // ================= ELEVATOR =================

    static class Elevator implements Runnable {

        private final int id;
        private int currentFloor = 0;
        private Direction direction = Direction.IDLE;

        private final TreeSet<Integer> upStops = new TreeSet<>();
        private final TreeSet<Integer> downStops = new TreeSet<>(Collections.reverseOrder());

        private final ReentrantLock lock = new ReentrantLock();

        public Elevator(int id) {
            this.id = id;
        }

        public int getCurrentFloor() {
            return currentFloor;
        }

        public Direction getDirection() {
            return direction;
        }

        public void addRequest(int floor) {
            lock.lock();
            try {
                if (floor > currentFloor) {
                    upStops.add(floor);
                } else if (floor < currentFloor) {
                    downStops.add(floor);
                } else {
                    System.out.println("Elevator " + id + " already at floor " + floor);
                }

                if (direction == Direction.IDLE) {
                    direction = (floor > currentFloor) ? Direction.UP : Direction.DOWN;
                }

            } finally {
                lock.unlock();
            }
        }

        @Override
        public void run() {

            while (true) {

                lock.lock();
                try {

                    if (direction == Direction.UP) {
                        moveUp();
                    } else if (direction == Direction.DOWN) {
                        moveDown();
                    } else {
                        idle();
                    }

                } finally {
                    lock.unlock();
                }

                sleep();
            }
        }

        private void moveUp() {
            if (!upStops.isEmpty()) {
                int nextFloor = upStops.pollFirst();
                goToFloor(nextFloor);
            } else {
                direction = downStops.isEmpty() ? Direction.IDLE : Direction.DOWN;
            }
        }

        private void moveDown() {
            if (!downStops.isEmpty()) {
                int nextFloor = downStops.pollFirst();
                goToFloor(nextFloor);
            } else {
                direction = upStops.isEmpty() ? Direction.IDLE : Direction.UP;
            }
        }

        private void idle() {
            if (!upStops.isEmpty()) {
                direction = Direction.UP;
            } else if (!downStops.isEmpty()) {
                direction = Direction.DOWN;
            }
        }

        private void goToFloor(int targetFloor) {

            while (currentFloor != targetFloor) {

                if (targetFloor > currentFloor) {
                    currentFloor++;
                } else {
                    currentFloor--;
                }

                System.out.println("Elevator " + id + " at floor " + currentFloor);
                sleep();
            }

            System.out.println("Elevator " + id + " stopped at floor " + currentFloor);
        }

        private void sleep() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ================= STRATEGY =================

    interface SchedulingStrategy {
        Elevator selectElevator(List<Elevator> elevators, int floor);
    }

    static class NearestElevatorStrategy implements SchedulingStrategy {

        @Override
        public Elevator selectElevator(List<Elevator> elevators, int floor) {

            Elevator best = null;
            int minDistance = Integer.MAX_VALUE;

            for (Elevator elevator : elevators) {
                int distance = Math.abs(elevator.getCurrentFloor() - floor);
                if (distance < minDistance) {
                    minDistance = distance;
                    best = elevator;
                }
            }

            return best;
        }
    }

    // ================= SYSTEM =================

    static class ElevatorSystem {

        private final List<Elevator> elevators = new ArrayList<>();
        private final SchedulingStrategy strategy;

        public ElevatorSystem(int count) {

            strategy = new NearestElevatorStrategy();

            for (int i = 0; i < count; i++) {
                Elevator elevator = new Elevator(i);
                elevators.add(elevator);
                new Thread(elevator).start();
            }
        }

        public void requestElevator(int floor) {
            Elevator selected = strategy.selectElevator(elevators, floor);
            System.out.println("Assigning Elevator " + selected.id + " to floor " + floor);
            selected.addRequest(floor);
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) throws InterruptedException {

        ElevatorSystem system = new ElevatorSystem(2);

        system.requestElevator(5);
        Thread.sleep(2000);

        system.requestElevator(2);
        Thread.sleep(2000);

        system.requestElevator(8);
    }
}