package ChessEulMelk.BitBoards;

import ChessEulMelk.Logics.Piece;
import ChessEulMelk.Logics.PieceColor;

public class LegalFlagGenerator {

    private static boolean isInit = false;
    private static MoveMaskGetter[] maskGetters = new MoveMaskGetter[7];

    public static void init() {
        if (isInit)
            return;

        NonSliderHandler.init();
        initNonSliderMoveMaskGetters();
        initSliderMoveMaskGetters();
        isInit = true;
    }

    private static void initNonSliderMoveMaskGetters() {
        maskGetters[PieceToMove.KING.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                return NonSliderHandler.getKingMoves(i, j) & ~friendOccupancy;
            }

        };

        maskGetters[PieceToMove.WHITE_PAWN.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                long totalOccupancy = enemyOccupancy | friendOccupancy;

                long pushBitBoard = NonSliderHandler.getPawnPushes(PieceColor.WHITE, i, j) & ~totalOccupancy;
                long doublePushBitBoard = NonSliderHandler.getPawnDoublePushes(PieceColor.WHITE, i, j);
                doublePushBitBoard = pushBitBoard == 0 ? 0 : (doublePushBitBoard & ~totalOccupancy);

                long captureBitBoard = NonSliderHandler.getPawnCaptures(PieceColor.WHITE, i, j) & enemyOccupancy;

                return pushBitBoard | doublePushBitBoard | captureBitBoard;
            }

        };

        maskGetters[PieceToMove.BLACK_PAWN.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                long totalOccupancy = enemyOccupancy | friendOccupancy;

                long pushBitBoard = NonSliderHandler.getPawnPushes(PieceColor.BLACK, i, j) & ~totalOccupancy;
                long doublePushBitBoard = NonSliderHandler.getPawnDoublePushes(PieceColor.BLACK, i, j);
                doublePushBitBoard = pushBitBoard == 0 ? 0 : (doublePushBitBoard & ~totalOccupancy);

                long captureBitBoard = NonSliderHandler.getPawnCaptures(PieceColor.BLACK, i, j) & enemyOccupancy;

                return pushBitBoard | doublePushBitBoard | captureBitBoard;
            }

        };

        maskGetters[PieceToMove.KNIGHT.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                return NonSliderHandler.getKnightMoves(i, j) & ~friendOccupancy;
            }

        };
    }

    private static void initSliderMoveMaskGetters() {
        maskGetters[PieceToMove.BISHOP.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                long totalOccupancy = enemyOccupancy | friendOccupancy;

                return SliderHandler.getSliderMoves(i, j, totalOccupancy, true) & ~friendOccupancy;
            }

        };

        maskGetters[PieceToMove.ROOK.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                long totalOccupancy = enemyOccupancy | friendOccupancy;

                return SliderHandler.getSliderMoves(i, j, totalOccupancy, false) & ~friendOccupancy;
            }

        };

        maskGetters[PieceToMove.QUEEN.ordinal()] = new MoveMaskGetter() {

            @Override
            public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j) {
                long totalOccupancy = enemyOccupancy | friendOccupancy;
                long queenMasks = SliderHandler.getSliderMoves(i, j, totalOccupancy, true)
                        | SliderHandler.getSliderMoves(i, j, totalOccupancy, false);

                return queenMasks & ~friendOccupancy;
            }

        };
    }

    public static long getMoveMasks(long enemyOccupancy, long friendOccupancy, Piece piece, int i, int j) {
        return maskGetters[PieceToMove.getPiece(piece).ordinal()].getMoveMasks(enemyOccupancy, friendOccupancy, i, j);
    }

    public static void printBitBoard(long bitBoard) {
        final long FIRST_BIT_MASK = 1L << 63;

        for (int i = 1; i <= 64; ++i) {
            long value = bitBoard & FIRST_BIT_MASK;

            System.out.print(value != 0 ? "1" : "0");
            System.out.print(i % 8 == 0 ? "\n" : " ");

            bitBoard <<= 1;
        }
        System.out.println();
    }

}