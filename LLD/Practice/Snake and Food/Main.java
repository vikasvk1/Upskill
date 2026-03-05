package snakefood;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(10, 10);
        BlockingQueue<Direction> inputQueue = new LinkedBlockingQueue<>();
        AtomicBoolean running = new AtomicBoolean(true);

        Thread inputThread = new Thread(() -> readInput(inputQueue, running), "input-thread");
        inputThread.setDaemon(true);
        inputThread.start();

        ScheduledExecutorService loop = Executors.newSingleThreadScheduledExecutor();
        loop.scheduleAtFixedRate(() -> {
            if (!running.get()) {
                return;
            }
            Direction latest = latestDirection(inputQueue);
            game.tick(latest);
            printBoard(game);
            if (game.isGameOver()) {
                running.set(false);
                System.out.println("Game over. Final score: " + game.score());
            }
        }, 0, 300, TimeUnit.MILLISECONDS);

        while (running.get()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running.set(false);
            }
        }

        loop.shutdownNow();
    }

    private static void readInput(BlockingQueue<Direction> inputQueue, AtomicBoolean running) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Controls: W/A/S/D, Q to quit");
        while (running.get()) {
            String raw = scanner.nextLine();
            if (raw == null || raw.trim().isEmpty()) {
                continue;
            }
            char ch = Character.toUpperCase(raw.charAt(0));
            if (ch == 'Q') {
                running.set(false);
                break;
            }
            Direction direction = mapInput(ch);
            if (direction != null) {
                inputQueue.offer(direction);
            }
        }
    }

    private static Direction mapInput(char ch) {
        switch (ch) {
            case 'W':
                return Direction.UP;
            case 'S':
                return Direction.DOWN;
            case 'A':
                return Direction.LEFT;
            case 'D':
                return Direction.RIGHT;
            default:
                return null;
        }
    }

    private static Direction latestDirection(BlockingQueue<Direction> inputQueue) {
        Direction direction = null;
        Direction polled;
        while ((polled = inputQueue.poll()) != null) {
            direction = polled;
        }
        return direction;
    }

    private static void printBoard(Game game) {
        clearConsoleView();
        char[][] board = game.boardSnapshot();
        for (char[] row : board) {
            System.out.println(new String(row));
        }
        System.out.println("Score: " + game.score());
        System.out.println("Direction: " + game.snake().direction());
    }

    private static void clearConsoleView() {
        for (int i = 0; i < 20; i++) {
            System.out.println();
        }
    }
}
