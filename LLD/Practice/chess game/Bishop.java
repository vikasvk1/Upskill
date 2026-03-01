public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        return dr == dc && board.isPathClear(from, to);
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'B' : 'b';
    }
}
