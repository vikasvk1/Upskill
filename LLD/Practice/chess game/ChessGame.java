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
        makeMove(fromSquare, toSquare, null);
    }

    public void makeMove(String fromSquare, String toSquare, String promotion) {
        Position from = Position.fromAlgebraic(fromSquare);
        Position to = Position.fromAlgebraic(toSquare);
        PieceType promotionType = parsePromotion(promotion);
        makeMove(from, to, promotionType);
    }

    public void makeMove(Position from, Position to) {
        makeMove(from, to, null);
    }

    public void makeMove(Position from, Position to, PieceType promotionType) {
        if (status != ChessGameStatus.IN_PROGRESS) {
            throw new ChessInvalidMoveException("Game is already finished.");
        }
        if (from.equals(to)) {
            throw new ChessInvalidMoveException("Source and destination cannot be same.");
        }
        if (!board.isInside(from) || !board.isInside(to)) {
            throw new ChessInvalidMoveException("Move is outside the board.");
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

        boolean castling = isCastlingMove(piece, from, to);
        boolean enPassant = false;
        if (castling) {
            validateCastling(from, to, piece);
        } else if (piece instanceof Pawn && isEnPassantMove(board, from, to, piece.getColor())) {
            enPassant = true;
        } else if (!piece.isValidMove(board, from, to)) {
            throw new ChessInvalidMoveException(
                    "Illegal move for " + piece.getType() + ": " + from + " to " + to + ".");
        }

        PieceType finalPromotion = normalizePromotion(piece, to, promotionType);

        if (leavesOwnKingInCheck(from, to, castling, enPassant, finalPromotion, currentTurn)) {
            throw new ChessInvalidMoveException("Move leaves king in check.");
        }

        Piece capturedPiece = applyMove(board, from, to, castling, enPassant, finalPromotion);
        moveHistory.add(new Move(from, to, piece, capturedPiece, enPassant, castling, finalPromotion));

        updateGameStateAfterMove();
    }

    private void updateGameStateAfterMove() {
        Color playerWhoMoved = currentTurn;
        currentTurn = currentTurn.opposite();

        boolean inCheck = isInCheck(currentTurn, board);
        boolean hasLegalMove = hasAnyLegalMove(currentTurn);

        if (!hasLegalMove && inCheck) {
            status = playerWhoMoved == Color.WHITE ? ChessGameStatus.WHITE_WIN : ChessGameStatus.BLACK_WIN;
            winner = playerWhoMoved == Color.WHITE ? whitePlayer : blackPlayer;
            return;
        }

        if (!hasLegalMove) {
            status = ChessGameStatus.DRAW; // stalemate
            winner = null;
            return;
        }

        status = ChessGameStatus.IN_PROGRESS;
        winner = null;
    }

    private boolean hasAnyLegalMove(Color color) {
        for (int fromRow = 0; fromRow < board.getSize(); fromRow++) {
            for (int fromCol = 0; fromCol < board.getSize(); fromCol++) {
                Position from = new Position(fromRow, fromCol);
                Piece piece = board.getPiece(from);
                if (piece == null || piece.getColor() != color) {
                    continue;
                }

                for (int toRow = 0; toRow < board.getSize(); toRow++) {
                    for (int toCol = 0; toCol < board.getSize(); toCol++) {
                        Position to = new Position(toRow, toCol);
                        if (from.equals(to)) {
                            continue;
                        }
                        if (isLegalMoveForColor(from, to, color, null)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isLegalMoveForColor(Position from, Position to, Color color, PieceType promotionType) {
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != color) {
            return false;
        }

        Piece destinationPiece = board.getPiece(to);
        if (destinationPiece != null && destinationPiece.getColor() == color) {
            return false;
        }

        boolean castling = isCastlingMove(piece, from, to);
        boolean enPassant = false;
        if (castling) {
            if (!isCastlingValid(from, to, piece)) {
                return false;
            }
        } else if (piece instanceof Pawn && isEnPassantMove(board, from, to, color)) {
            enPassant = true;
        } else if (!piece.isValidMove(board, from, to)) {
            return false;
        }

        PieceType finalPromotion = normalizePromotion(piece, to, promotionType);
        return !leavesOwnKingInCheck(from, to, castling, enPassant, finalPromotion, color);
    }

    private PieceType normalizePromotion(Piece piece, Position to, PieceType requestedPromotion) {
        if (!(piece instanceof Pawn)) {
            if (requestedPromotion != null) {
                throw new ChessInvalidMoveException("Only pawns can be promoted.");
            }
            return null;
        }

        boolean reachesLastRank = (piece.getColor() == Color.WHITE && to.getRow() == 0)
                || (piece.getColor() == Color.BLACK && to.getRow() == 7);
        if (!reachesLastRank) {
            if (requestedPromotion != null) {
                throw new ChessInvalidMoveException("Promotion is allowed only on the last rank.");
            }
            return null;
        }

        if (requestedPromotion == null) {
            return PieceType.QUEEN;
        }
        if (requestedPromotion == PieceType.KING || requestedPromotion == PieceType.PAWN) {
            throw new ChessInvalidMoveException("Invalid promotion piece.");
        }
        return requestedPromotion;
    }

    private PieceType parsePromotion(String promotion) {
        if (promotion == null || promotion.trim().isEmpty()) {
            return null;
        }
        String value = promotion.trim().toUpperCase();
        switch (value) {
            case "Q":
            case "QUEEN":
                return PieceType.QUEEN;
            case "R":
            case "ROOK":
                return PieceType.ROOK;
            case "B":
            case "BISHOP":
                return PieceType.BISHOP;
            case "N":
            case "KNIGHT":
                return PieceType.KNIGHT;
            default:
                throw new ChessInvalidMoveException("Unknown promotion piece: " + promotion);
        }
    }

    private boolean leavesOwnKingInCheck(
            Position from,
            Position to,
            boolean castling,
            boolean enPassant,
            PieceType promotionType,
            Color movingColor) {
        ChessBoard simulation = new ChessBoard(board);
        applyMove(simulation, from, to, castling, enPassant, promotionType);
        return isInCheck(movingColor, simulation);
    }

    private Piece applyMove(
            ChessBoard targetBoard,
            Position from,
            Position to,
            boolean castling,
            boolean enPassant,
            PieceType promotionType) {
        Piece moving = targetBoard.getPiece(from);
        Piece captured = null;

        if (castling) {
            targetBoard.movePiece(from, to);
            int rookFromCol = to.getCol() == 6 ? 7 : 0;
            int rookToCol = to.getCol() == 6 ? 5 : 3;
            Position rookFrom = new Position(from.getRow(), rookFromCol);
            Position rookTo = new Position(from.getRow(), rookToCol);
            targetBoard.movePiece(rookFrom, rookTo);
        } else if (enPassant) {
            Position capturedPawnPos = new Position(from.getRow(), to.getCol());
            captured = targetBoard.getPiece(capturedPawnPos);
            targetBoard.setPiece(to, moving);
            targetBoard.setPiece(from, null);
            targetBoard.setPiece(capturedPawnPos, null);
            moving.setHasMoved(true);
        } else {
            captured = targetBoard.movePiece(from, to);
        }

        if (promotionType != null) {
            Piece promoted = createPromotedPiece(promotionType, moving.getColor());
            promoted.setHasMoved(true);
            targetBoard.setPiece(to, promoted);
        }

        return captured;
    }

    private Piece createPromotedPiece(PieceType type, Color color) {
        switch (type) {
            case QUEEN:
                return new Queen(color);
            case ROOK:
                return new Rook(color);
            case BISHOP:
                return new Bishop(color);
            case KNIGHT:
                return new Knight(color);
            default:
                throw new ChessInvalidMoveException("Invalid promotion piece.");
        }
    }

    private boolean isCastlingMove(Piece piece, Position from, Position to) {
        return piece instanceof King
                && from.getRow() == to.getRow()
                && Math.abs(to.getCol() - from.getCol()) == 2;
    }

    private void validateCastling(Position from, Position to, Piece king) {
        if (!isCastlingValid(from, to, king)) {
            throw new ChessInvalidMoveException("Castling is not legal here.");
        }
    }

    private boolean isCastlingValid(Position from, Position to, Piece king) {
        if (!(king instanceof King)) {
            return false;
        }
        if (king.hasMoved()) {
            return false;
        }
        int homeRow = king.getColor() == Color.WHITE ? 7 : 0;
        if (from.getRow() != homeRow || from.getCol() != 4 || to.getRow() != homeRow) {
            return false;
        }
        if (!(to.getCol() == 6 || to.getCol() == 2)) {
            return false;
        }
        if (isInCheck(king.getColor(), board)) {
            return false;
        }

        int rookCol = to.getCol() == 6 ? 7 : 0;
        Position rookPos = new Position(homeRow, rookCol);
        Piece rook = board.getPiece(rookPos);
        if (!(rook instanceof Rook) || rook.getColor() != king.getColor() || rook.hasMoved()) {
            return false;
        }

        int step = to.getCol() > from.getCol() ? 1 : -1;
        for (int col = from.getCol() + step; col != rookCol; col += step) {
            if (board.getPiece(new Position(homeRow, col)) != null) {
                return false;
            }
        }

        Color enemy = king.getColor().opposite();
        Position passSquare = new Position(homeRow, from.getCol() + step);
        if (isSquareAttacked(passSquare, enemy, board) || isSquareAttacked(to, enemy, board)) {
            return false;
        }
        return true;
    }

    private boolean isEnPassantMove(ChessBoard targetBoard, Position from, Position to, Color movingColor) {
        Piece moving = targetBoard.getPiece(from);
        if (!(moving instanceof Pawn)) {
            return false;
        }
        if (targetBoard.getPiece(to) != null) {
            return false;
        }

        int direction = movingColor == Color.WHITE ? -1 : 1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();
        if (rowDiff != direction || Math.abs(colDiff) != 1) {
            return false;
        }

        Move lastMove = getLastMove();
        if (lastMove == null || !lastMove.isPawnDoubleStep()) {
            return false;
        }
        if (lastMove.getMovingColor() == movingColor) {
            return false;
        }
        if (lastMove.getTo().getRow() != from.getRow() || lastMove.getTo().getCol() != to.getCol()) {
            return false;
        }

        Piece adjacent = targetBoard.getPiece(lastMove.getTo());
        return adjacent instanceof Pawn && adjacent.getColor() != movingColor;
    }

    private Move getLastMove() {
        return moveHistory.isEmpty() ? null : moveHistory.get(moveHistory.size() - 1);
    }

    public boolean isInCheck(Color color) {
        return isInCheck(color, board);
    }

    private boolean isInCheck(Color color, ChessBoard targetBoard) {
        Position kingPosition = targetBoard.findKing(color);
        if (kingPosition == null) {
            return true;
        }
        return isSquareAttacked(kingPosition, color.opposite(), targetBoard);
    }

    private boolean isSquareAttacked(Position target, Color byColor, ChessBoard targetBoard) {
        for (int row = 0; row < targetBoard.getSize(); row++) {
            for (int col = 0; col < targetBoard.getSize(); col++) {
                Position from = new Position(row, col);
                Piece attacker = targetBoard.getPiece(from);
                if (attacker == null || attacker.getColor() != byColor) {
                    continue;
                }
                if (canAttackSquare(attacker, from, target, targetBoard)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canAttackSquare(Piece attacker, Position from, Position target, ChessBoard targetBoard) {
        int dr = target.getRow() - from.getRow();
        int dc = target.getCol() - from.getCol();

        if (attacker instanceof Pawn) {
            int direction = attacker.getColor() == Color.WHITE ? -1 : 1;
            return dr == direction && Math.abs(dc) == 1;
        }
        if (attacker instanceof King) {
            return Math.abs(dr) <= 1 && Math.abs(dc) <= 1 && (Math.abs(dr) + Math.abs(dc) > 0);
        }
        return attacker.isValidMove(targetBoard, from, target);
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
