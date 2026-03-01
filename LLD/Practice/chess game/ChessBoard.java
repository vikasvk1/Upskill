public class ChessBoard {
    private static final int SIZE = 8;
    private final Piece[][] grid;

    public ChessBoard() {
        this.grid = new Piece[SIZE][SIZE];
        initializeStandardSetup();
    }

    public void initializeStandardSetup() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = null;
            }
        }

        setupBackRank(Color.BLACK, 0);
        setupPawns(Color.BLACK, 1);
        setupPawns(Color.WHITE, 6);
        setupBackRank(Color.WHITE, 7);
    }

    private void setupBackRank(Color color, int row) {
        grid[row][0] = new Rook(color);
        grid[row][1] = new Knight(color);
        grid[row][2] = new Bishop(color);
        grid[row][3] = new Queen(color);
        grid[row][4] = new King(color);
        grid[row][5] = new Bishop(color);
        grid[row][6] = new Knight(color);
        grid[row][7] = new Rook(color);
    }

    private void setupPawns(Color color, int row) {
        for (int col = 0; col < SIZE; col++) {
            grid[row][col] = new Pawn(color);
        }
    }

    public Piece getPiece(Position position) {
        validatePosition(position);
        return grid[position.getRow()][position.getCol()];
    }

    public void setPiece(Position position, Piece piece) {
        validatePosition(position);
        grid[position.getRow()][position.getCol()] = piece;
    }

    public Piece movePiece(Position from, Position to) {
        validatePosition(from);
        validatePosition(to);
        Piece moving = getPiece(from);
        Piece captured = getPiece(to);
        setPiece(to, moving);
        setPiece(from, null);
        return captured;
    }

    public boolean isPathClear(Position from, Position to) {
        int rowStep = Integer.compare(to.getRow(), from.getRow());
        int colStep = Integer.compare(to.getCol(), from.getCol());

        int currentRow = from.getRow() + rowStep;
        int currentCol = from.getCol() + colStep;

        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (grid[currentRow][currentCol] != null) {
                return false;
            }
            currentRow += rowStep;
            currentCol += colStep;
        }
        return true;
    }

    public Position findKing(Color color) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Piece piece = grid[row][col];
                if (piece instanceof King && piece.getColor() == color) {
                    return new Position(row, col);
                }
            }
        }
        return null;
    }

    public int getSize() {
        return SIZE;
    }

    public boolean isInside(Position position) {
        return position.getRow() >= 0 && position.getRow() < SIZE
                && position.getCol() >= 0 && position.getCol() < SIZE;
    }

    private void validatePosition(Position position) {
        if (!isInside(position)) {
            throw new ChessInvalidMoveException("Invalid board position: " + position);
        }
    }

    public void printBoard() {
        System.out.println();
        for (int row = 0; row < SIZE; row++) {
            System.out.print((8 - row) + " ");
            for (int col = 0; col < SIZE; col++) {
                Piece piece = grid[row][col];
                char symbol = piece == null ? '.' : piece.getSymbol();
                System.out.print(symbol + " ");
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
        System.out.println();
    }
}
