public class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        return dr <= 1 && dc <= 1 && (dr + dc > 0);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'K' : 'k';
    }
}
