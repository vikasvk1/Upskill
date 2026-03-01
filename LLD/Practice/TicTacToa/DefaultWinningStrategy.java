public class DefaultWinningStrategy implements WinningStrategy {
    @Override
    public boolean checkWin(Board board, int row, int col, Symbol symbol, int winLength) {
        return checkDirection(board, row, col, 1, 0, symbol, winLength) ||
               checkDirection(board, row, col, 0, 1, symbol, winLength) ||
               checkDirection(board, row, col, 1, 1, symbol, winLength) ||
               checkDirection(board, row, col, 1, -1, symbol, winLength);
    }

    private boolean checkDirection(Board board, int row, int col, int dRow, int dCol,
                                   Symbol symbol, int winLength) {
        int count = 1;
        count += countInDirection(board, row, col, dRow, dCol, symbol);
        count += countInDirection(board, row, col, -dRow, -dCol, symbol);
        return count >= winLength;
    }

    private int countInDirection(Board board, int row, int col, int dRow, int dCol, Symbol symbol) {
        int count = 0;
        int r = row + dRow;
        int c = col + dCol;
        while (r >= 0 && r < board.getSize() && c >= 0 && c < board.getSize()) {
            if (!board.getCell(r, c).getSymbol().equals(symbol)) {
                break;
            }
            count++;
            r += dRow;
            c += dCol;
        }
        return count;
    }
}
