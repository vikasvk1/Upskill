public class Move {
    private final Position from;
    private final Position to;
    private final PieceType movingPieceType;
    private final Color movingColor;
    private final PieceType capturedPieceType;
    private final boolean enPassant;
    private final boolean castling;
    private final PieceType promotionType;

    public Move(
            Position from,
            Position to,
            Piece movingPiece,
            Piece capturedPiece,
            boolean enPassant,
            boolean castling,
            PieceType promotionType) {
        this.from = from;
        this.to = to;
        this.movingPieceType = movingPiece.getType();
        this.movingColor = movingPiece.getColor();
        this.capturedPieceType = capturedPiece == null ? null : capturedPiece.getType();
        this.enPassant = enPassant;
        this.castling = castling;
        this.promotionType = promotionType;
    }

    public Position getFrom() {
        return from;
    }

    public Position getTo() {
        return to;
    }

    public PieceType getMovingPieceType() {
        return movingPieceType;
    }

    public Color getMovingColor() {
        return movingColor;
    }

    public PieceType getCapturedPieceType() {
        return capturedPieceType;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public boolean isCastling() {
        return castling;
    }

    public PieceType getPromotionType() {
        return promotionType;
    }

    public boolean isPawnDoubleStep() {
        return movingPieceType == PieceType.PAWN && Math.abs(to.getRow() - from.getRow()) == 2;
    }

    @Override
    public String toString() {
        String capture = capturedPieceType == null ? "" : "x" + capturedPieceType;
        String special = castling ? " castling" : (enPassant ? " en-passant" : "");
        String promotion = promotionType == null ? "" : " =" + promotionType;
        return movingPieceType + " " + from + " -> " + to + capture + special + promotion;
    }
}
