package ChessEulMelk.Logics;

public enum Piece {
    NONE,
    WHITE_KING,
    WHITE_QUEEN,
    WHITE_ROOK,
    WHITE_BISHOP,
    WHITE_KNIGHT,
    WHITE_PAWN,
    BLACK_KING,
    BLACK_QUEEN,
    BLACK_ROOK,
    BLACK_BISHOP,
    BLACK_KNIGHT,
    BLACK_PAWN;

    public static Piece getPiece(char s) {
        switch (s) {
            case 'K':
                return Piece.WHITE_KING;
            case 'Q':
                return Piece.WHITE_QUEEN;
            case 'R':
                return Piece.WHITE_ROOK;
            case 'B':
                return Piece.WHITE_BISHOP;
            case 'N':
                return Piece.WHITE_KNIGHT;
            case 'P':
                return Piece.WHITE_PAWN;
            case 'k':
                return Piece.BLACK_KING;
            case 'q':
                return Piece.BLACK_QUEEN;
            case 'r':
                return Piece.BLACK_ROOK;
            case 'b':
                return Piece.BLACK_BISHOP;
            case 'n':
                return Piece.BLACK_KNIGHT;
            case 'p':
                return Piece.BLACK_PAWN;
            default:
                return Piece.NONE;
        }
    }
}