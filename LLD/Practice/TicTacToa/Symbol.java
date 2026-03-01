public final class Symbol {
    public static final Symbol EMPTY = new Symbol('_');

    private final char displayChar;

    public Symbol(char displayChar) {
        this.displayChar = displayChar;
    }

    public char getDisplayChar() {
        return displayChar;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Symbol symbol = (Symbol) other;
        return displayChar == symbol.displayChar;
    }

    @Override
    public int hashCode() {
        return Character.hashCode(displayChar);
    }
}