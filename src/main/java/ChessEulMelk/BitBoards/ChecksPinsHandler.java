package ChessEulMelk.BitBoards;

import ChessEulMelk.Globals.Globals;
import ChessEulMelk.Logics.Piece;
import ChessEulMelk.Logics.PieceColor;
import ChessEulMelk.Logics.Board;
import ChessEulMelk.Logics.Direction;
import ChessEulMelk.UserInterfaces.BoardFlags;

public class ChecksPinsHandler {

    private static long kingOccupancy, friendPawnOccupancy, friendOccupancy;
    private static long enemyRookOccupancy, enemyBishopOccupancy;
    private static long enemyKingOccupancy, enemyKnightOccupancy, enemyPawnOccupancy;
    private static long totalOccupancy;
    private static BoardFlags flags;

    public static void calculateChecksAndPins(Board board, BoardFlags flags) {
        flags.resetBlockPinFlags();
        ChecksPinsHandler.flags = flags;
        boolean isWhite = board.turn == PieceColor.WHITE;

        populateOccupancies(board, isWhite);

        getSliderCheckFlag(true);
        getSliderCheckFlag(false);
        getKnightCheckFlag();
        getPawnCheckFlag(isWhite);

        getPinFlag(true);
        getPinFlag(false);
        getSpecialPinFlag(Direction.LEFT);
        getSpecialPinFlag(Direction.RIGHT);

        totalOccupancy &= ~kingOccupancy;

        getSliderDangerFlag(true);
        getSliderDangerFlag(false);
        getKnightDangerFlag();
        getKingDangerFlag();
        getPawnDangerFlag(isWhite);
    }

    private static void populateOccupancies(Board board, boolean isWhite) {
        long enemyQueenOccupancy;

        if (isWhite) {
            kingOccupancy = board.getPieceOccupancy(Piece.WHITE_KING);
            friendPawnOccupancy = board.getPieceOccupancy(Piece.WHITE_PAWN);
            friendOccupancy = board.getWhiteOccupancy();

            enemyKingOccupancy = board.getPieceOccupancy(Piece.BLACK_KING);
            enemyQueenOccupancy = board.getPieceOccupancy(Piece.BLACK_QUEEN);
            enemyRookOccupancy = board.getPieceOccupancy(Piece.BLACK_ROOK);
            enemyBishopOccupancy = board.getPieceOccupancy(Piece.BLACK_BISHOP);
            enemyKnightOccupancy = board.getPieceOccupancy(Piece.BLACK_KNIGHT);
            enemyPawnOccupancy = board.getPieceOccupancy(Piece.BLACK_PAWN);
        } else {
            kingOccupancy = board.getPieceOccupancy(Piece.BLACK_KING);
            friendPawnOccupancy = board.getPieceOccupancy(Piece.BLACK_PAWN);
            friendOccupancy = board.getBlackOccupancy();

            enemyKingOccupancy = board.getPieceOccupancy(Piece.WHITE_KING);
            enemyQueenOccupancy = board.getPieceOccupancy(Piece.WHITE_QUEEN);
            enemyRookOccupancy = board.getPieceOccupancy(Piece.WHITE_ROOK);
            enemyBishopOccupancy = board.getPieceOccupancy(Piece.WHITE_BISHOP);
            enemyKnightOccupancy = board.getPieceOccupancy(Piece.WHITE_KNIGHT);
            enemyPawnOccupancy = board.getPieceOccupancy(Piece.WHITE_PAWN);
        }

        totalOccupancy = board.getTotalOccupancy();
        enemyRookOccupancy |= enemyQueenOccupancy;
        enemyBishopOccupancy |= enemyQueenOccupancy;
    }

    private static void getSliderCheckFlag(boolean isBishop) {
        Direction[] directions = isBishop ? Direction.BISHOP_DIRECTIONS : Direction.ROOK_DIRECTIONS;
        long pieceOccupancy = isBishop ? enemyBishopOccupancy : enemyRookOccupancy;

        for (Direction direction : directions) {
            long miniMask = BoardFiller.fill(kingOccupancy, direction);
            long blocker = BoardFiller.getBlocker(miniMask & totalOccupancy, direction);

            if ((blocker & pieceOccupancy) != 0) {
                flags.checkFlags |= kingOccupancy;

                miniMask &= ~BoardFiller.fill(blocker, direction);
                flags.blockFlags &= miniMask;
            }
        }

    }

    private static void getKnightCheckFlag() {
        int index = Long.numberOfLeadingZeros(kingOccupancy);

        if (index == 64)
            return;

        int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

        long knightMask = NonSliderHandler.getKnightMoves(row, col);
        long attackerKnightMask = knightMask & enemyKnightOccupancy;

        if (attackerKnightMask != 0) {
            flags.checkFlags |= kingOccupancy;
            flags.blockFlags &= attackerKnightMask;
        }

    }

    private static void getPawnCheckFlag(boolean isWhite) {
        int index = Long.numberOfLeadingZeros(kingOccupancy);

        if (index == 64)
            return;

        int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;
        PieceColor pieceColor = isWhite ? PieceColor.WHITE : PieceColor.BLACK;

        long pawnMask = NonSliderHandler.getPawnCaptures(pieceColor, row, col);
        long attackerPawnMask = pawnMask & enemyPawnOccupancy;

        if (attackerPawnMask != 0) {
            flags.checkFlags |= kingOccupancy;
            flags.blockFlags &= attackerPawnMask;
        }
    }

    private static void getSliderDangerFlag(boolean isBishop) {
        long sliderOccupancy = isBishop ? enemyBishopOccupancy : enemyRookOccupancy;

        while (sliderOccupancy != 0L) {
            long isolatedMask = Long.lowestOneBit(sliderOccupancy);
            sliderOccupancy &= ~isolatedMask;

            int index = Long.numberOfLeadingZeros(isolatedMask);
            int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

            flags.dangerFlags |= SliderHandler.getSliderMoves(row, col, totalOccupancy, isBishop);
        }
    }

    private static void getKnightDangerFlag() {
        long pieceOccupancy = enemyKnightOccupancy;

        while (pieceOccupancy != 0L) {
            long isolatedMask = Long.lowestOneBit(pieceOccupancy);
            pieceOccupancy &= ~isolatedMask;

            int index = Long.numberOfLeadingZeros(isolatedMask);
            int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

            flags.dangerFlags |= NonSliderHandler.getKnightMoves(row, col);
        }
    }

    private static void getKingDangerFlag() {
        long pieceOccupancy = enemyKingOccupancy;

        while (pieceOccupancy != 0L) {
            long isolatedMask = Long.lowestOneBit(pieceOccupancy);
            pieceOccupancy &= ~isolatedMask;

            int index = Long.numberOfLeadingZeros(isolatedMask);
            int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

            flags.dangerFlags |= NonSliderHandler.getKingMoves(row, col);
        }
    }

    private static void getPawnDangerFlag(boolean isWhite) {
        PieceColor pieceColor = isWhite ? PieceColor.BLACK : PieceColor.WHITE;
        long pieceOccupancy = enemyPawnOccupancy;

        while (pieceOccupancy != 0L) {
            long isolatedMask = Long.lowestOneBit(pieceOccupancy);
            pieceOccupancy &= ~isolatedMask;

            int index = Long.numberOfLeadingZeros(isolatedMask);
            int row = index / Globals.BOARD_SIZE, col = index % Globals.BOARD_SIZE;

            flags.dangerFlags |= NonSliderHandler.getPawnCaptures(pieceColor, row, col);
        }
    }

    private static void getPinFlag(boolean isBishop) {
        Direction[] directions = isBishop ? Direction.BISHOP_DIRECTIONS : Direction.ROOK_DIRECTIONS;
        long pieceOccupancy = isBishop ? enemyBishopOccupancy : enemyRookOccupancy;

        for (Direction direction : directions) {
            long miniMask = BoardFiller.fill(kingOccupancy, direction);
            long blocker = BoardFiller.getBlocker(miniMask & totalOccupancy, direction);

            if ((blocker & friendOccupancy) != 0) {
                long xRayMask = BoardFiller.fill(blocker, direction);
                long pinner = BoardFiller.getBlocker(xRayMask & totalOccupancy, direction);

                if ((pinner & pieceOccupancy) != 0) {
                    miniMask &= ~BoardFiller.fill(pinner, direction);

                    flags.pinFlags[direction.ordinal()] = miniMask;
                    flags.pinnedFlags[direction.ordinal()] = blocker;
                }
            }
        }
    }

    private static void getSpecialPinFlag(Direction direction) {
        long totalPawnOccupancy = friendPawnOccupancy | enemyPawnOccupancy;

        long miniMask = BoardFiller.fill(kingOccupancy, direction);
        long blocker1 = BoardFiller.getBlocker(miniMask & totalOccupancy, direction);

        if ((blocker1 & totalPawnOccupancy) != 0) {
            long xRayMask1 = BoardFiller.fill(blocker1, direction);
            long blocker2 = BoardFiller.getBlocker(xRayMask1 & totalOccupancy, direction);

            long blockersMask = blocker1 | blocker2;
            if ((blocker2 & totalPawnOccupancy) != 0 && (blockersMask & friendPawnOccupancy) != 0
                    && (blockersMask & enemyPawnOccupancy) != 0) {
                long xRayMask2 = BoardFiller.fill(blocker2, direction);
                long pinner = BoardFiller.getBlocker(xRayMask2 & totalOccupancy, direction);

                if ((pinner & enemyRookOccupancy) != 0)
                    flags.isSpecialPin = true;
            }
        }
    }
}