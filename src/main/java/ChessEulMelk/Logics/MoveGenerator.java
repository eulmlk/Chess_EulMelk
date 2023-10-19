package ChessEulMelk.Logics;

import ChessEulMelk.BitBoards.BitBoards;
import ChessEulMelk.BitBoards.LegalFlagGenerator;
import ChessEulMelk.BitBoards.NonSliderHandler;
import ChessEulMelk.Globals.Globals;
import ChessEulMelk.UserInterfaces.BoardFlags;

public class MoveGenerator {

    private static final long WHITE_KING_SIDE_SQUARES = 0x0000000000000006L;
    private static final long WHITE_QUEEN_SIDE_SQUARES = 0x0000000000000070L;
    private static final long BLACK_KING_SIDE_SQUARES = 0x0600000000000000L;
    private static final long BLACK_QUEEN_SIDE_SQUARES = 0x7000000000000000L;

    private static final long WHITE_KING_SIDE_MASK = 0x0000000000000002L;
    private static final long WHITE_QUEEN_SIDE_MASK = 0x0000000000000020L;
    private static final long BLACK_KING_SIDE_MASK = 0x0200000000000000L;
    private static final long BLACK_QUEEN_SIDE_MASK = 0x2000000000000000L;

    public static boolean generateMoves(Board board, Move hypoMove, BoardFlags flags, int row, int col) {
        Piece piece = board.getPiece(row, col);
        PieceColor color = PieceColor.getColor(piece);

        flags.legalFlags = BitBoards.EMPTY_BITBOARD;
        flags.specialFlags = BitBoards.EMPTY_BITBOARD;

        hypoMove.isCastling = false;
        hypoMove.isPromotion = false;
        hypoMove.isEnPassant = false;

        if (color == PieceColor.NONE || color != board.turn)
            return false;

        long friendOccupancy = color == PieceColor.WHITE ? board.getWhiteOccupancy() : board.getBlackOccupancy();
        long enemyOccupancy = color == PieceColor.BLACK ? board.getWhiteOccupancy() : board.getBlackOccupancy();

        long enPassantMask = board.getEnPassantMask();

        flags.legalFlags = LegalFlagGenerator.getMoveMasks(enemyOccupancy, friendOccupancy, piece, row, col)
                & ~friendOccupancy;

        if (piece == Piece.WHITE_KING) {
            flags.legalFlags &= ~flags.dangerFlags;
            flags.legalFlags |= getCastlingMask(board, hypoMove, flags, piece);
        } else if (piece == Piece.BLACK_KING) {
            flags.legalFlags &= ~flags.dangerFlags;
            flags.legalFlags |= getCastlingMask(board, hypoMove, flags, piece);
        } else {
            if (piece == Piece.WHITE_PAWN) {
                flags.specialFlags |= BitBoards.RANK_8 & flags.legalFlags;

                if (!flags.isSpecialPin) {
                    long attackMask = NonSliderHandler.getPawnCaptures(PieceColor.WHITE, row, col) & enPassantMask;

                    flags.legalFlags |= attackMask;
                    flags.specialFlags |= attackMask;
                    hypoMove.isEnPassant = attackMask != 0;
                }
            } else if (piece == Piece.BLACK_PAWN) {
                flags.specialFlags |= BitBoards.RANK_1 & flags.legalFlags;

                if (!flags.isSpecialPin) {
                    long attackMask = NonSliderHandler.getPawnCaptures(PieceColor.BLACK, row, col) & enPassantMask;

                    flags.legalFlags |= attackMask;
                    flags.specialFlags |= attackMask;
                    hypoMove.isEnPassant = attackMask != 0;
                }
            }

            for (int i = 0; i < Globals.DIRECTION_COUNT; ++i) {
                if (BitBoards.checkBit(flags.pinnedFlags[i], row, col))
                    flags.legalFlags &= flags.pinFlags[i];
            }

            flags.legalFlags &= flags.blockFlags;
        }
        return true;
    }

    private static long getCastlingMask(Board board, Move hypoMove, BoardFlags flags, Piece piece) {
        if (flags.checkFlags != 0) {
            hypoMove.isCastling = false;
            return BitBoards.EMPTY_BITBOARD;
        }

        long castlingMask = BitBoards.EMPTY_BITBOARD;
        long occupancy = board.getWhiteOccupancy() | board.getBlackOccupancy() | flags.dangerFlags;

        if (piece == Piece.WHITE_KING) {
            if (board.getCastlingRight(Castling.WHITE_KING_SIDE) && (occupancy & WHITE_KING_SIDE_SQUARES) == 0)
                castlingMask |= WHITE_KING_SIDE_MASK;

            if (board.getCastlingRight(Castling.WHITE_QUEEN_SIDE) && (occupancy & WHITE_QUEEN_SIDE_SQUARES) == 0)
                castlingMask |= WHITE_QUEEN_SIDE_MASK;
        } else if (piece == Piece.BLACK_KING) {
            if (board.getCastlingRight(Castling.BLACK_KING_SIDE) && (occupancy & BLACK_KING_SIDE_SQUARES) == 0)
                castlingMask |= BLACK_KING_SIDE_MASK;

            if (board.getCastlingRight(Castling.BLACK_QUEEN_SIDE) && (occupancy & BLACK_QUEEN_SIDE_SQUARES) == 0)
                castlingMask |= BLACK_QUEEN_SIDE_MASK;
        }

        hypoMove.isCastling = castlingMask != 0;
        flags.specialFlags |= castlingMask;

        return castlingMask;
    }

    public static void updateCheckMateStatus(Board board, BoardFlags flags) {
        Move move = new Move();
        long totalFlags = BitBoards.EMPTY_BITBOARD;

        for (int i = 1; i < Globals.PIECE_COUNT; ++i) {
            long pieceOccupancy = board.getPieceOccupancy(Piece.values()[i]);

            while (pieceOccupancy != 0) {
                long isolatedMask = Long.lowestOneBit(pieceOccupancy);
                int index = Long.numberOfLeadingZeros(isolatedMask);
                pieceOccupancy &= ~isolatedMask;

                int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

                generateMoves(board, move, flags, row, col);
                totalFlags |= flags.legalFlags;
            }
        }

        board.isCheckMate = (totalFlags == 0) && (flags.checkFlags != 0);
        board.isStaleMate = (totalFlags == 0) && (flags.checkFlags == 0);
        flags.legalFlags = BitBoards.EMPTY_BITBOARD;
    }
}
