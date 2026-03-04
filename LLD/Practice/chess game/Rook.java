public class Rook extends Piece {
    public Rook(Color color) {
        super(color, PieceType.ROOK);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        if (dr == 0 || dc == 0) {
            return board.isPathClear(from, to);
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'R' : 'r';
    }

    @Override
    public Piece copy() {
        Rook copy = new Rook(getColor());
        copy.setHasMoved(hasMoved());
        return copy;
    }
}
