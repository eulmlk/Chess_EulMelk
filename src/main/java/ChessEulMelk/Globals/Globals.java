package ChessEulMelk.Globals;

public class Globals {
    public static final int BOARD_SIZE = 8;
    public static final int LAST_INDEX = BOARD_SIZE - 1;
    public static final int LABEL_COUNT = 16;
    public static final int DIRECTION_COUNT = 8;
    public static final int PIECE_COUNT = 13;

    public static final String[] FILES = { "a", "b", "c", "d", "e", "f", "g", "h" };
    public static final String[] RANKS = { "8", "7", "6", "5", "4", "3", "2", "1" };
    public static final String[] PIECE_LETTERS = { "", "K", "Q", "R", "B", "N", "P", "k", "q", "r", "b", "n", "p" };
    public static final String[] ALGEBRAIC_LETTERS = { "", "K", "Q", "R", "B", "N", "", "K", "Q", "R", "B", "N", "" };
}
