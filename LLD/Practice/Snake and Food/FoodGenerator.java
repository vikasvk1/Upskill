package snakefood;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FoodGenerator {
    private final int rows;
    private final int cols;
    private final Random random;

    public FoodGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.random = new Random();
    }

    public Position generate(Set<Position> occupied) {
        int capacity = rows * cols;
        if (occupied.size() >= capacity) {
            return null;
        }

        List<Position> emptyCells = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Position candidate = new Position(r, c);
                if (!occupied.contains(candidate)) {
                    emptyCells.add(candidate);
                }
            }
        }

        return emptyCells.get(random.nextInt(emptyCells.size()));
    }
}
