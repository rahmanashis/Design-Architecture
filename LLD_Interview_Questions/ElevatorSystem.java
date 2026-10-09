class Elevator implements Runnable {
    private int id;
    private int currentFloor = 0;

    public Elevator(int id) {
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            move();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void move() {
        System.out.println("Elevator " + id + " at floor " + currentFloor);
        currentFloor++;
    }
}

public class ElevatorSystem {
    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {
            Thread elevatorThread = new Thread(new Elevator(i));
            elevatorThread.start();
        }
    }
}