import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChessGame {
    private final ChessBoard board;
    private final ChessPlayer whitePlayer;
    private final ChessPlayer blackPlayer;
    private final List<Move> moveHistory;

    private Color currentTurn;
    private ChessGameStatus status;
    private ChessPlayer winner;

    public ChessGame(String whitePlayerName, String blackPlayerName) {
        this.board = new ChessBoard();
        this.whitePlayer = new ChessPlayer(whitePlayerName, Color.WHITE);
        this.blackPlayer = new ChessPlayer(blackPlayerName, Color.BLACK);
        this.moveHistory = new ArrayList<>();
        this.currentTurn = Color.WHITE;
        this.status = ChessGameStatus.IN_PROGRESS;
    }

    public void makeMove(String fromSquare, String toSquare) {
        Position from = Position.fromAlgebraic(fromSquare);
        Position to = Position.fromAlgebraic(toSquare);
        makeMove(from, to);
    }

    public void makeMove(Position from, Position to) {
        if (status != ChessGameStatus.IN_PROGRESS) {
            throw new ChessInvalidMoveException("Game is already finished.");
        }
        if (from.equals(to)) {
            throw new ChessInvalidMoveException("Source and destination cannot be same.");
        }

        Piece piece = board.getPiece(from);
        if (piece == null) {
            throw new ChessInvalidMoveException("No piece at source square " + from + ".");
        }
        if (piece.getColor() != currentTurn) {
            throw new ChessInvalidMoveException("It is " + currentTurn + " turn.");
        }

        Piece destinationPiece = board.getPiece(to);
        if (destinationPiece != null && destinationPiece.getColor() == currentTurn) {
            throw new ChessInvalidMoveException("Cannot capture own piece at " + to + ".");
        }

        if (!piece.isValidMove(board, from, to)) {
            throw new ChessInvalidMoveException("Illegal move for " + piece.getType() + ": " + from + " to " + to + ".");
        }

        Piece capturedPiece = board.movePiece(from, to);
        moveHistory.add(new Move(from, to, piece, capturedPiece));

        if (capturedPiece instanceof King) {
            status = currentTurn == Color.WHITE ? ChessGameStatus.WHITE_WIN : ChessGameStatus.BLACK_WIN;
            winner = currentTurn == Color.WHITE ? whitePlayer : blackPlayer;
            return;
        }

        currentTurn = currentTurn.opposite();
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ChessPlayer getCurrentPlayer() {
        return currentTurn == Color.WHITE ? whitePlayer : blackPlayer;
    }

    public Color getCurrentTurn() {
        return currentTurn;
    }

    public ChessGameStatus getStatus() {
        return status;
    }

    public ChessPlayer getWinner() {
        return winner;
    }

    public List<Move> getMoveHistory() {
        return Collections.unmodifiableList(moveHistory);
    }
}
