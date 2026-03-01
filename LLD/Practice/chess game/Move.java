public class Move {
    private final Position from;
    private final Position to;
    private final Piece movingPiece;
    private final Piece capturedPiece;

    public Move(Position from, Position to, Piece movingPiece, Piece capturedPiece) {
        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;
        this.capturedPiece = capturedPiece;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public Piece getMovingPiece() {
        return movingPiece;
    }

    public Piece getCapturedPiece() {
        return capturedPiece;
    }

    @Override
    public String toString() {
        String capture = capturedPiece == null ? "" : "x" + capturedPiece.getType();
        return movingPiece.getType() + " " + from + " -> " + to + capture;
    }
}
