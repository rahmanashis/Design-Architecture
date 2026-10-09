import java.util.*;

/*
Core Entities:
    Player
    Dice
    Snake
    Ladder
    Board
    Game
    SnakeAndLadderController

Player
    Name
    Current position on board
Dice
    Roll and give random number (1–6)
Snak
    Has head and tail
Ladder
    Has start and end
Board
    Size (usually 100)
    Stores snakes and ladders
    Calculates final position after move
Game
    Players list
Board
    Dice
    Turn management
    Winning conditio
Controller
    Starts game
    Loops turns
    Prints output
*/

/*
 * ===== PLAYER =====
 */
class Player {
    String name;
    int position;

    Player(String name) {
        this.name = name;
        this.position = 0;
    }
}

/*
 * ===== DICE =====
 */
class Dice {
    Random random = new Random();

    int roll() {
        return random.nextInt(6) + 1;
    }
}

/*
 * ===== SNAKE =====
 */
class Snake {
    int head;
    int tail;

    Snake(int head, int tail) {
        this.head = head;
        this.tail = tail;
    }
}

/*
 * ===== LADDER =====
 */
class Ladder {
    int start;
    int end;

    Ladder(int start, int end) {
        this.start = start;
        this.end = end;
    }
}

/*
 * ===== BOARD =====
 */
class Board {
    int size;
    Map<Integer, Integer> snakes = new HashMap<>();
    Map<Integer, Integer> ladders = new HashMap<>();

    Board(int size) {
        this.size = size;
    }

    void addSnake(Snake snake) {
        snakes.put(snake.head, snake.tail);
    }

    void addLadder(Ladder ladder) {
        ladders.put(ladder.start, ladder.end);
    }

    int getFinalPosition(int position) {
        if (snakes.containsKey(position)) {
            System.out.println("Bitten by snake at " + position);
            return snakes.get(position);
        }
        if (ladders.containsKey(position)) {
            System.out.println("Climbed ladder at " + position);
            return ladders.get(position);
        }
        return position;
    }
}

/*
 * ===== GAME =====
 */
class Game {
    Queue<Player> players;
    Board board;
    Dice dice;
    boolean isGameOver;

    Game(List<Player> playerList, Board board, Dice dice) {
        this.players = new LinkedList<>(playerList);
        this.board = board;
        this.dice = dice;
        this.isGameOver = false;
    }

    void start() {
        while (!isGameOver) {
            Player currentPlayer = players.poll();
            takeTurn(currentPlayer);
            if (!isGameOver) {
                players.offer(currentPlayer);
            }
        }
    }

    private void takeTurn(Player player) {
        int roll = dice.roll();
        System.out.println(player.name + " rolled " + roll);

        int newPosition = player.position + roll;
        if (newPosition > board.size) {
            System.out.println(player.name + " cannot move");
            return;
        }

        newPosition = board.getFinalPosition(newPosition);
        player.position = newPosition;

        System.out.println(player.name + " moved to " + player.position);

        if (player.position == board.size) {
            System.out.println(player.name + " wins!");
            isGameOver = true;
        }
    }
}

/*
 * ===== CONTROLLER =====
 */
public class SnakeAndLadderController {

    public static void main(String[] args) {

        // Players
        Player p1 = new Player("Player-1");
        Player p2 = new Player("Player-2");

        List<Player> players = Arrays.asList(p1, p2);

        // Board
        Board board = new Board(100);

        // Snakes
        board.addSnake(new Snake(99, 10));
        board.addSnake(new Snake(70, 30));
        board.addSnake(new Snake(52, 11));

        // Ladders
        board.addLadder(new Ladder(2, 38));
        board.addLadder(new Ladder(15, 26));
        board.addLadder(new Ladder(8, 31));

        // Dice
        Dice dice = new Dice();

        // Game
        Game game = new Game(players, board, dice);
        game.start();
    }
}