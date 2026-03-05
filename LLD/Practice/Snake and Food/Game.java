package snakefood;

public class Game {
    private final int rows;
    private final int cols;
    private final Snake snake;
    private final FoodGenerator foodGenerator;
    private Position food;
    private int score;
    private boolean gameOver;

    public Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.snake = new Snake(new Position(rows / 2, cols / 2), Direction.RIGHT);
        this.foodGenerator = new FoodGenerator(rows, cols);
        this.food = foodGenerator.generate(snake.occupiedPositions());
        this.score = 0;
        this.gameOver = false;
    }

    public int score() {
        return score;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Snake snake() {
        return snake;
    }

    public Position food() {
        return food;
    }

    public void tick(Direction inputDirection) {
        if (gameOver) {
            return;
        }

        if (inputDirection != null) {
            snake.changeDirection(inputDirection);
        }

        Position nextHead = snake.nextHead();
        if (isOutOfBounds(nextHead)) {
            gameOver = true;
            return;
        }

        boolean willEatFood = nextHead.equals(food);
        Position tail = snake.tail();
        boolean hitsBody = snake.occupies(nextHead);
        boolean tailWillMove = !willEatFood;

        // Moving onto current tail is valid only when tail moves away in the same tick.
        if (hitsBody && !(tailWillMove && nextHead.equals(tail))) {
            gameOver = true;
            return;
        }

        snake.move(nextHead, willEatFood);
        if (willEatFood) {
            score++;
            food = foodGenerator.generate(snake.occupiedPositions());
            if (food == null) {
                gameOver = true;
            }
        }
    }

    public char[][] boardSnapshot() {
        char[][] board = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = '.';
            }
        }

        for (Position p : snake.occupiedPositions()) {
            board[p.row()][p.col()] = 'S';
        }

        Position head = snake.head();
        board[head.row()][head.col()] = 'H';

        if (food != null) {
            board[food.row()][food.col()] = 'F';
        }
        return board;
    }

    private boolean isOutOfBounds(Position position) {
        return position.row() < 0
            || position.row() >= rows
            || position.col() < 0
            || position.col() >= cols;
    }
}
