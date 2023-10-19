package ChessEulMelk.BitBoards;

import ChessEulMelk.Globals.Globals;

public class BitBoards {
    public static final long FIRST_BIT_MASK = 1L << 63;
    public static final long LAST_BIT_MASK = 1L;
    public static final long EMPTY_BITBOARD = 0;
    public static final long UNIVERSAL_BITBOARD = -1;

    public static final long NOT_A_FILE = 0x7F7F7F7F7F7F7F7FL;
    public static final long NOT_H_FILE = 0xFEFEFEFEFEFEFEFEL;
    public static final long NOT_AB_FILE = NOT_A_FILE & (NOT_A_FILE >> 1);
    public static final long NOT_GH_FILE = NOT_H_FILE & (NOT_H_FILE << 1);
    public static final long KING_SIDE = NOT_AB_FILE & (NOT_AB_FILE >> 2);
    public static final long QUEEN_SIDE = NOT_GH_FILE & (NOT_GH_FILE << 2);

    public static final long H_FILE = 0x0101010101010101L;
    public static final long RANK_1 = 0X00000000000000FFL;
    public static final long RANK_8 = 0XFF00000000000000L;

    public static long getMask(int row, int col) {
        return FIRST_BIT_MASK >>> (col + Globals.BOARD_SIZE * row);
    }

    public static boolean checkBit(long dragFlags, int row, int col) {
        return (dragFlags & getMask(row, col)) != 0;
    }
}