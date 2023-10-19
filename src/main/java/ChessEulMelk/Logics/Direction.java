package ChessEulMelk.Logics;

public enum Direction {
    LEFT, RIGHT, UP, DOWN, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT, NONE;

    public static final Direction[] BISHOP_DIRECTIONS = { UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT };
    public static final Direction[] ROOK_DIRECTIONS = { LEFT, RIGHT, UP, DOWN };

    // For calculating blockers in a bitboard
    public static boolean isHighest(Direction direction) {
        return direction == RIGHT || direction == DOWN || direction == DOWN_LEFT || direction == DOWN_RIGHT;
    }
}