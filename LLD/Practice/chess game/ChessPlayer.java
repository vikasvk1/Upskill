public class ChessPlayer {
    private final String name;
    private final Color color;

    public ChessPlayer(String name, Color color) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty.");
        }
        this.name = name.trim();
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return name + " (" + color + ")";
    }
}
