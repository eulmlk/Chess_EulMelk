package ChessEulMelk.BitBoards;

import ChessEulMelk.Globals.Globals;
import ChessEulMelk.Logics.PieceColor;

public class NonSliderHandler {
    private static boolean isInit = false;

    private static long knightMoveMasks[][] = new long[Globals.BOARD_SIZE][Globals.BOARD_SIZE];
    private static long kingMoveMasks[][] = new long[Globals.BOARD_SIZE][Globals.BOARD_SIZE];
    private static long pawnPushMasks[][][] = new long[2][Globals.BOARD_SIZE][Globals.BOARD_SIZE];
    private static long pawnDoublePushMasks[][][] = new long[2][Globals.BOARD_SIZE][Globals.BOARD_SIZE];
    private static long pawnAttackMasks[][][] = new long[2][Globals.BOARD_SIZE][Globals.BOARD_SIZE];

    public static void init() {
        if (isInit)
            return;

        initKnightMasks();
        initKingMasks();
        initPawnPushMasks();
        initPawnDoublePushMasks();
        initPawnAttackMasks();
        isInit = true;
    }

    private static void initKnightMasks() {
        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                long knightLocation = BitBoards.getMask(i, j);

                long targetLocation = BitBoards.EMPTY_BITBOARD;

                targetLocation |= (knightLocation << 10) & BitBoards.NOT_GH_FILE;
                targetLocation |= (knightLocation << 6) & BitBoards.NOT_AB_FILE;
                targetLocation |= (knightLocation << 17) & BitBoards.NOT_H_FILE;
                targetLocation |= (knightLocation << 15) & BitBoards.NOT_A_FILE;
                targetLocation |= (knightLocation >>> 10) & BitBoards.NOT_AB_FILE;
                targetLocation |= (knightLocation >>> 6) & BitBoards.NOT_GH_FILE;
                targetLocation |= (knightLocation >>> 17) & BitBoards.NOT_A_FILE;
                targetLocation |= (knightLocation >>> 15) & BitBoards.NOT_H_FILE;

                knightMoveMasks[i][j] = targetLocation;
            }
        }
    }

    private static void initKingMasks() {
        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                long kingLocation = BitBoards.getMask(i, j);

                long targetLocation = BitBoards.EMPTY_BITBOARD;

                targetLocation |= (kingLocation << 1) & BitBoards.NOT_H_FILE;
                targetLocation |= (kingLocation >>> 1) & BitBoards.NOT_A_FILE;
                targetLocation |= kingLocation << 8;
                targetLocation |= kingLocation >>> 8;
                targetLocation |= (kingLocation << 9) & BitBoards.NOT_H_FILE;
                targetLocation |= (kingLocation << 7) & BitBoards.NOT_A_FILE;
                targetLocation |= (kingLocation >>> 9) & BitBoards.NOT_A_FILE;
                targetLocation |= (kingLocation >>> 7) & BitBoards.NOT_H_FILE;

                kingMoveMasks[i][j] = targetLocation;
            }
        }
    }

    private static void initPawnPushMasks() {
        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                long pawnLocation = BitBoards.getMask(i, j);

                pawnPushMasks[PieceColor.WHITE.ordinal()][i][j] = pawnLocation << 8;
                pawnPushMasks[PieceColor.BLACK.ordinal()][i][j] = pawnLocation >>> 8;
            }
        }
    }

    private static void initPawnDoublePushMasks() {
        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                long pawnLocation = BitBoards.getMask(i, j);

                if (i < 2) {
                    pawnDoublePushMasks[PieceColor.WHITE.ordinal()][i][j] = 0;
                    pawnDoublePushMasks[PieceColor.BLACK.ordinal()][i][j] = pawnLocation >>> 16;
                } else if (i > 5) {
                    pawnDoublePushMasks[PieceColor.WHITE.ordinal()][i][j] = pawnLocation << 16;
                    pawnDoublePushMasks[PieceColor.BLACK.ordinal()][i][j] = 0;
                } else {
                    pawnDoublePushMasks[PieceColor.WHITE.ordinal()][i][j] = 0;
                    pawnDoublePushMasks[PieceColor.BLACK.ordinal()][i][j] = 0;
                }
            }
        }
    }

    private static void initPawnAttackMasks() {
        for (int i = 0; i < Globals.BOARD_SIZE; ++i) {
            for (int j = 0; j < Globals.BOARD_SIZE; ++j) {
                long pawnLocation = BitBoards.getMask(i, j);

                long whiteAttackMask = 0;
                long blackAttackMask = 0;

                whiteAttackMask |= (pawnLocation << 9) & BitBoards.NOT_H_FILE;
                whiteAttackMask |= (pawnLocation << 7) & BitBoards.NOT_A_FILE;

                blackAttackMask |= (pawnLocation >>> 9) & BitBoards.NOT_A_FILE;
                blackAttackMask |= (pawnLocation >>> 7) & BitBoards.NOT_H_FILE;

                pawnAttackMasks[PieceColor.WHITE.ordinal()][i][j] = whiteAttackMask;
                pawnAttackMasks[PieceColor.BLACK.ordinal()][i][j] = blackAttackMask;
            }
        }
    }

    public static long getKingMoves(int i, int j) {
        return kingMoveMasks[i][j];
    }

    public static long getPawnPushes(PieceColor color, int i, int j) {
        return pawnPushMasks[color.ordinal()][i][j];
    }

    public static long getPawnDoublePushes(PieceColor color, int i, int j) {
        return pawnDoublePushMasks[color.ordinal()][i][j];
    }

    public static long getPawnCaptures(PieceColor color, int i, int j) {
        return pawnAttackMasks[color.ordinal()][i][j];
    }

    public static long getKnightMoves(int i, int j) {
        return knightMoveMasks[i][j];
    }
}