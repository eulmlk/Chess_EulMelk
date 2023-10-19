package ChessEulMelk.Logics;

public enum PieceColor {
    WHITE, BLACK, NONE;

    public static PieceColor getColor(Piece piece) {
        if (piece == Piece.WHITE_KING || piece == Piece.WHITE_QUEEN || piece == Piece.WHITE_ROOK
                || piece == Piece.WHITE_BISHOP || piece == Piece.WHITE_KNIGHT || piece == Piece.WHITE_PAWN) {
            return PieceColor.WHITE;
        } else if (piece == Piece.BLACK_KING || piece == Piece.BLACK_QUEEN || piece == Piece.BLACK_ROOK
                || piece == Piece.BLACK_BISHOP || piece == Piece.BLACK_KNIGHT || piece == Piece.BLACK_PAWN) {
            return PieceColor.BLACK;
        }

        return PieceColor.NONE;
    }
}