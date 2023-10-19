package ChessEulMelk.Logics;

import ChessEulMelk.BitBoards.ChecksPinsHandler;
import ChessEulMelk.UserInterfaces.BoardFlags;

public class MoveHandler {

    public static void makeMove(Board board, Move move, BoardFlags flags) {
        board.removePiece(move.sourceRow, move.sourceCol);
        board.removePiece(move.destRow, move.destCol);

        if (move.isPromotion)
            board.setPiece(move.destRow, move.destCol, move.promotedPiece);
        else
            board.setPiece(move.destRow, move.destCol, move.movingPiece);

        if (move.isCastling) {
            if (move.destCol == 2) {
                Piece piece = board.getPiece(move.destRow, 0);
                board.removePiece(move.destRow, 0);
                board.setPiece(move.destRow, 3, piece);
            } else if (move.destCol == 6) {
                Piece piece = board.getPiece(move.destRow, 7);
                board.removePiece(move.destRow, 7);
                board.setPiece(move.destRow, 5, piece);
            }
        }

        if (move.isEnPassant) {
            if (move.movingPiece == Piece.WHITE_PAWN) {
                board.removePiece(move.destRow + 1, move.destCol);
            } else if (move.movingPiece == Piece.BLACK_PAWN) {
                board.removePiece(move.destRow - 1, move.destCol);
            }
        }

        board.swapTurn();

        ChecksPinsHandler.calculateChecksAndPins(board, flags);
        board.isCheck = flags.checkFlags != 0;

        updateCastlingRights(board, move);

        if (move.movingPiece == Piece.WHITE_PAWN && (move.sourceRow - move.destRow) == 2)
            board.setEnPassantMask(move.destRow + 1, move.destCol);
        else if (move.movingPiece == Piece.BLACK_PAWN && (move.destRow - move.sourceRow) == 2)
            board.setEnPassantMask(move.destRow - 1, move.destCol);
        else
            board.resetEnPassantMask();

        if (move.movingPiece != Piece.WHITE_PAWN && move.movingPiece != Piece.BLACK_PAWN
                && move.capturedPiece == Piece.NONE)
            ++board.halfMoveClock;
        else
            board.halfMoveClock = 0;

        MoveGenerator.updateCheckMateStatus(board, flags);
        System.out.println(BoardState.generateFENString(board));
    }

    public static PieceColor isPromotion(int row, Piece piece) {
        if (piece == Piece.WHITE_PAWN && row == 0)
            return PieceColor.WHITE;

        if (piece == Piece.BLACK_PAWN && row == 7)
            return PieceColor.BLACK;

        return PieceColor.NONE;
    }

    private static void updateCastlingRights(Board board, Move move) {
        if (move.movingPiece == Piece.WHITE_KING) {
            board.setCastlingRight(Castling.WHITE_KING_SIDE, false);
            board.setCastlingRight(Castling.WHITE_QUEEN_SIDE, false);
        } else if (move.movingPiece == Piece.BLACK_KING) {
            board.setCastlingRight(Castling.BLACK_KING_SIDE, false);
            board.setCastlingRight(Castling.BLACK_QUEEN_SIDE, false);
        } else if (move.movingPiece == Piece.WHITE_ROOK) {
            if ((move.sourceRow == 7 && move.sourceCol == 7))
                board.setCastlingRight(Castling.WHITE_KING_SIDE, false);

            if (move.sourceRow == 7 && move.sourceCol == 0)
                board.setCastlingRight(Castling.WHITE_QUEEN_SIDE, false);

        } else if (move.movingPiece == Piece.BLACK_ROOK) {
            if (move.sourceRow == 0 && move.sourceCol == 7)
                board.setCastlingRight(Castling.BLACK_KING_SIDE, false);

            if (move.sourceRow == 0 && move.sourceCol == 0)
                board.setCastlingRight(Castling.BLACK_QUEEN_SIDE, false);
        }

        if (move.destRow == 7 && move.destCol == 7)
            board.setCastlingRight(Castling.WHITE_KING_SIDE, false);

        if (move.destRow == 7 && move.destCol == 0)
            board.setCastlingRight(Castling.WHITE_QUEEN_SIDE, false);

        if (move.destRow == 0 && move.destCol == 7)
            board.setCastlingRight(Castling.BLACK_KING_SIDE, false);

        if (move.destRow == 0 && move.destCol == 0)
            board.setCastlingRight(Castling.BLACK_QUEEN_SIDE, false);

    }

}