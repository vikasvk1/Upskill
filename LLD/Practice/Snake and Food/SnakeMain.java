package snakefood;

import java.util.Scanner;

public class SnakeMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Game game = new Game(10, 10);

        System.out.println("Snake Game (Turn-Based)");
        System.out.println("Controls: W/A/S/D, Q to quit");

        while (!game.isGameOver()) {
            printBoard(game);
            System.out.print("Move: ");
            String raw = scanner.nextLine();

            if (raw == null || raw.trim().isEmpty()) {
                game.tick(null);
                continue;
            }

            char ch = Character.toUpperCase(raw.charAt(0));
            if (ch == 'Q') {
                break;
            }

            Direction direction = mapInput(ch);
            game.tick(direction);
        }

        printBoard(game);
        System.out.println("Game over. Final score: " + game.score());
    }

    private static Direction mapInput(char ch) {
        switch (ch) {
            case 'W':
                return Direction.UP;
            case 'S':
                return Direction.DOWN;
            case 'A':
                return Direction.LEFT;
            case 'D':
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    private static void printBoard(Game game) {
        char[][] board = game.boardSnapshot();
        for (char[] row : board) {
            System.out.println(new String(row));
        }
        System.out.println("Score: " + game.score());
        System.out.println("Direction: " + game.snake().direction());
        System.out.println("------------");
    }
}
