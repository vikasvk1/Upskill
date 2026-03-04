public class Queen extends Piece {
    public Queen(Color color) {
        super(color, PieceType.QUEEN);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        if (dr == dc || dr == 0 || dc == 0) {
            return board.isPathClear(from, to);
        }
        return false;
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'Q' : 'q';
    }

    @Override
    public Piece copy() {
        Queen copy = new Queen(getColor());
        copy.setHasMoved(hasMoved());
        return copy;
    }
}
