public abstract class Piece {
    private final Color color;
    private final PieceType type;

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public abstract boolean isValidMove(ChessBoard board, Position from, Position to);

    public abstract char getSymbol();
}
