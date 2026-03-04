public class Knight extends Piece {
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'N' : 'n';
    }

    @Override
    public Piece copy() {
        Knight copy = new Knight(getColor());
        copy.setHasMoved(hasMoved());
        return copy;
    }
}
