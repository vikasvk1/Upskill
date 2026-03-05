package snakefood;

import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Snake {
    private final Deque<Position> body;
    private final Set<Position> occupied;
    private Direction direction;

    public Snake(Position start, Direction initialDirection) {
        this.body = new LinkedList<>();
        this.occupied = new HashSet<>();
        this.direction = initialDirection;
        body.addFirst(start);
        occupied.add(start);
    }

    public Direction direction() {
        return direction;
    }

    public void changeDirection(Direction newDirection) {
        if (newDirection == null || direction.isOpposite(newDirection)) {
            return;
        }
        this.direction = newDirection;
    }

    public Position head() {
        return body.peekFirst();
    }

    public Position tail() {
        return body.peekLast();
    }

    public int length() {
        return body.size();
    }

    public Set<Position> occupiedPositions() {
        return Collections.unmodifiableSet(occupied);
    }

    public boolean occupies(Position position) {
        return occupied.contains(position);
    }

    public Position nextHead() {
        return head().next(direction);
    }

    public void move(Position nextHead, boolean grow) {
        body.addFirst(nextHead);
        occupied.add(nextHead);
        if (!grow) {
            Position removedTail = body.removeLast();
            occupied.remove(removedTail);
        }
    }
}
