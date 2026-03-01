import java.util.ArrayList;
import java.util.List;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final WinningStrategy winningStrategy;
    private final int winLength;
    private int currentPlayerIndex;
    private GameStatus status;
    private Player winner;

    public Game(List<Player> players, int boardSize, int winLength, WinningStrategy winningStrategy) {
        if (players.size() < 2) {
            throw new IllegalArgumentException("At least two players are required.");
        }
        if (winLength < 3 || winLength > boardSize) {
            throw new IllegalArgumentException("Win length must be between 3 and board size.");
        }
        this.players = new ArrayList<>(players);
        this.board = new Board(boardSize);
        this.winLength = winLength;
        this.winningStrategy = winningStrategy;
        this.currentPlayerIndex = 0;
        this.status = GameStatus.IN_PROGRESS;
    }

    public void makeMove(int row, int col) {
        if (status != GameStatus.IN_PROGRESS) {
            throw new InvalidMoveException("Game is already over.");
        }
        if (!board.isCellEmpty(row, col)) {
            throw new InvalidMoveException("Cell (" + row + ", " + col + ") is already occupied.");
        }

        Player current = getCurrentPlayer();
        board.placeSymbol(row, col, current.getSymbol());

        if (winningStrategy.checkWin(board, row, col, current.getSymbol(), winLength)) {
            status = GameStatus.WIN;
            winner = current;
            return;
        }

        if (board.isFull()) {
            status = GameStatus.DRAW;
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public GameStatus getStatus() {
        return status;
    }

    public Player getWinner() {
        return winner;
    }

    public void printBoard() {
        board.printBoard();
    }
}
