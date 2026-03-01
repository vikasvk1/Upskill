public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }

    @Override
    public boolean isValidMove(ChessBoard board, Position from, Position to) {
        int direction = getColor() == Color.WHITE ? -1 : 1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = to.getCol() - from.getCol();

        Piece target = board.getPiece(to);

        if (colDiff == 0) {
            if (target != null) {
                return false;
            }
            if (rowDiff == direction) {
                return true;
            }
            int startRow = getColor() == Color.WHITE ? 6 : 1;
            if (from.getRow() == startRow && rowDiff == 2 * direction) {
                Position middle = new Position(from.getRow() + direction, from.getCol());
                return board.getPiece(middle) == null;
            }
            return false;
        }

        if (Math.abs(colDiff) == 1 && rowDiff == direction) {
            return target != null && target.getColor() != getColor();
        }

        return false;
    }

    @Override
    public char getSymbol() {
        return getColor() == Color.WHITE ? 'P' : 'p';
    }
}
