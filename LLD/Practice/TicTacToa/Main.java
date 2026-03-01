import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Board size (N): ");
        int size = readInt(scanner, 3, 50);

        System.out.print("Win length (K, <= N): ");
        int winLength = readInt(scanner, 3, size);

        System.out.print("Number of players (>=2): ");
        int playerCount = readInt(scanner, 2, 10);

        List<Player> players = new ArrayList<>();
        Set<Character> usedSymbols = new HashSet<>();

        for (int i = 0; i < playerCount; i++) {
            System.out.printf("Player %d name: ", i + 1);
            String name = readNonEmpty(scanner);

            System.out.printf("Player %d symbol (single character): ", i + 1);
            char symbolChar = readSymbol(scanner, usedSymbols);
            usedSymbols.add(symbolChar);

            players.add(new Player(name, new Symbol(symbolChar)));
        }

        Game game = new Game(players, size, winLength, new DefaultWinningStrategy());

        System.out.println("\nGame start!");
        game.printBoard();

        while (game.getStatus() == GameStatus.IN_PROGRESS) {
            Player current = game.getCurrentPlayer();
            System.out.printf("%n%s move [row col]: ", current);
            int[] move = readMove(scanner);

            try {
                game.makeMove(move[0], move[1]);
                game.printBoard();
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }

        if (game.getStatus() == GameStatus.WIN) {
            System.out.println("\nWinner: " + game.getWinner());
        } else {
            System.out.println("\nGame ended in a draw.");
        }
    }

    private static int[] readMove(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                System.out.print("Enter two numbers: ");
                continue;
            }
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                return new int[]{row, col};
            } catch (NumberFormatException e) {
                System.out.print("Invalid numbers. Try again: ");
            }
        }
    }

    private static int readInt(Scanner scanner, int min, int max) {
        while (true) {
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value < min || value > max) {
                    System.out.printf("Enter a number between %d and %d: ", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    private static String readNonEmpty(Scanner scanner) {
        while (true) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.print("Input cannot be empty. Try again: ");
        }
    }

    private static char readSymbol(Scanner scanner, Set<Character> usedSymbols) {
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.length() != 1) {
                System.out.print("Enter exactly one character: ");
                continue;
            }
            char symbol = line.charAt(0);
            if (symbol == Symbol.EMPTY.getDisplayChar()) {
                System.out.print("Symbol cannot be '_' Try again: ");
                continue;
            }
            if (usedSymbols.contains(symbol)) {
                System.out.print("Symbol already taken. Try again: ");
                continue;
            }
            return symbol;
        }
    }
}
