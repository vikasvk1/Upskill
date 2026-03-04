public abstract class Piece {
    private final Color color;
    private final PieceType type;
    private boolean hasMoved;

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public abstract boolean isValidMove(ChessBoard board, Position from, Position to);

    public abstract char getSymbol();

    public abstract Piece copy();
}
