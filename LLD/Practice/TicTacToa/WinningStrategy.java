public interface WinningStrategy {
    boolean checkWin(Board board, int row, int col, Symbol symbol, int winLength);
}
