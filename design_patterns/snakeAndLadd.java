package week_1;

import java.util.*;

// =================== MAIN ===================
public class SnakeAndLadder {

    public static void main(String[] args) {

        // Players
        List<Player> players = Arrays.asList(
                new Player("Alice"),
                new Player("Bob")
        );

        // Dice
        Dice dice = new StandardDice();

        // Board
        Board board = new Board(100);

        // Snakes
        board.addJump(new Snake(99, 54));
        board.addJump(new Snake(70, 55));
        board.addJump(new Snake(52, 42));

        // Ladders
        board.addJump(new Ladder(10, 40));
        board.addJump(new Ladder(25, 75));
        board.addJump(new Ladder(60, 85));

        // Game
        Game game = new Game(players, board, dice);
        game.start();
    }
}

// =================== GAME ===================
class Game {
    private Queue<Player> players;
    private Board board;
    private Dice dice;

    public Game(List<Player> players, Board board, Dice dice) {
        this.players = new LinkedList<>(players);
        this.board = board;
        this.dice = dice;
    }

    public void start() {
        while (true) {
            Player currentPlayer = players.poll();
            int roll = dice.roll();

            System.out.println(currentPlayer.getName() + " rolled " + roll);

            int nextPosition = currentPlayer.getPosition() + roll;

            if (nextPosition <= board.getSize()) {
                nextPosition = board.resolvePosition(nextPosition);
                currentPlayer.setPosition(nextPosition);
            }

            System.out.println(currentPlayer.getName() + " is now at " + currentPlayer.getPosition());
            System.out.println("----------------------------------");

            if (currentPlayer.getPosition() == board.getSize()) {
                System.out.println("🎉 " + currentPlayer.getName() + " WINS THE GAME!");
                break;
            }

            players.offer(currentPlayer);
        }
    }
}

// =================== PLAYER ===================
class Player {
    private String name;
    private int position;

    public Player(String name) {
        this.name = name;
        this.position = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

// =================== BOARD ===================
class Board {
    private int size;
    private Map<Integer, Jump> jumps;

    public Board(int size) {
        this.size = size;
        this.jumps = new HashMap<>();
    }

    public int getSize() {
        return size;
    }

    public void addJump(Jump jump) {
        jumps.put(jump.getStart(), jump);
    }

    public int resolvePosition(int position) {
        if (jumps.containsKey(position)) {
            Jump jump = jumps.get(position);
            System.out.println(jump.getMessage());
            return jump.getEnd();
        }
        return position;
    }
}

// =================== DICE ===================
interface Dice {
    int roll();
}

class StandardDice implements Dice {
    private Random random = new Random();

    public int roll() {
        return random.nextInt(6) + 1;
    }
}

// =================== JUMP ===================
interface Jump {
    int getStart();
    int getEnd();
    String getMessage();
}

// =================== SNAKE ===================
class Snake implements Jump {
    private int start;
    private int end;

    public Snake(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getMessage() {
        return "🐍 Snake bite! Go down from " + start + " to " + end;
    }
}

// =================== LADDER ===================
class Ladder implements Jump {
    private int start;
    private int end;

    public Ladder(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public String getMessage() {
        return "🪜 Ladder climb! Go up from " + start + " to " + end;
    }
}