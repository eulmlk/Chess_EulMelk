package ChessEulMelk.Logics;

public enum Castling {
    WHITE_KING_SIDE, WHITE_QUEEN_SIDE, BLACK_KING_SIDE, BLACK_QUEEN_SIDE;

    public static Castling getCastlingRight(char ch) {
        switch (ch) {
            case 'K':
                return WHITE_KING_SIDE;
            case 'Q':
                return WHITE_QUEEN_SIDE;
            case 'k':
                return BLACK_KING_SIDE;
            case 'q':
                return BLACK_QUEEN_SIDE;
            default:
                return null;
        }
    }
}