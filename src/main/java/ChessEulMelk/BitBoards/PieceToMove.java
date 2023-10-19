package ChessEulMelk.BitBoards;

import ChessEulMelk.Logics.Piece;

public enum PieceToMove {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, WHITE_PAWN, BLACK_PAWN, NONE;

    private static PieceToMove[] getPieceList = { PieceToMove.NONE, PieceToMove.KING, PieceToMove.QUEEN,
            PieceToMove.ROOK, PieceToMove.BISHOP, PieceToMove.KNIGHT, PieceToMove.WHITE_PAWN, PieceToMove.KING,
            PieceToMove.QUEEN, PieceToMove.ROOK, PieceToMove.BISHOP, PieceToMove.KNIGHT, PieceToMove.BLACK_PAWN };

    public static PieceToMove getPiece(Piece piece) {
        return getPieceList[piece.ordinal()];
    }
}