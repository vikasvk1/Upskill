import java.util.Scanner;

public class ChessMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("White player name: ");
        String white = readNonEmpty(scanner);
        System.out.print("Black player name: ");
        String black = readNonEmpty(scanner);

        ChessGame game = new ChessGame(white, black);

        System.out.println("\nChess game started.");
        System.out.println("Enter moves as: e2 e4");
        System.out.println("Promotion format: e7 e8 q");
        System.out.println("Type 'exit' to stop.");

        while (game.getStatus() == ChessGameStatus.IN_PROGRESS) {
            game.getBoard().printBoard();
            ChessPlayer current = game.getCurrentPlayer();
            System.out.print(current + " move: ");
            String line = scanner.nextLine().trim();

            if (line.equalsIgnoreCase("exit")) {
                System.out.println("Game stopped.");
                return;
            }

            String[] parts = line.split("\\s+");
            if (parts.length != 2 && parts.length != 3) {
                System.out.println("Invalid input. Use format: e2 e4 or e7 e8 q");
                continue;
            }

            try {
                if (parts.length == 2) {
                    game.makeMove(parts[0], parts[1]);
                } else {
                    game.makeMove(parts[0], parts[1], parts[2]);
                }
            } catch (IllegalArgumentException | ChessInvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }

        game.getBoard().printBoard();
        if (game.getStatus() == ChessGameStatus.DRAW) {
            System.out.println("Game over: draw (stalemate).");
        } else {
            System.out.println("Winner: " + game.getWinner());
        }
    }

    private static String readNonEmpty(Scanner scanner) {
        while (true) {
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.print("Input cannot be empty, try again: ");
        }
    }
}
