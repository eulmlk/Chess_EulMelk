package ChessEulMelk.UserInterfaces;

import ChessEulMelk.BitBoards.BitBoards;
import ChessEulMelk.Globals.Globals;

public class BoardFlags {
    public long dragFlags, legalFlags, checkFlags, blockFlags, dangerFlags, specialFlags;
    public long[] pinFlags = new long[Globals.DIRECTION_COUNT];
    public long[] pinnedFlags = new long[Globals.DIRECTION_COUNT];
    public boolean isSpecialPin;

    public BoardFlags() {
        dragFlags = legalFlags = checkFlags = dangerFlags = specialFlags = BitBoards.EMPTY_BITBOARD;
        blockFlags = BitBoards.UNIVERSAL_BITBOARD;
        isSpecialPin = false;

        for (int i = 0; i < Globals.DIRECTION_COUNT; ++i) {
            pinFlags[i] = pinnedFlags[i] = BitBoards.EMPTY_BITBOARD;
        }
    }

    public void resetBlockPinFlags() {
        blockFlags = BitBoards.UNIVERSAL_BITBOARD;
        checkFlags = dangerFlags = BitBoards.EMPTY_BITBOARD;
        isSpecialPin = false;

        pinFlags = new long[Globals.DIRECTION_COUNT];
        pinnedFlags = new long[Globals.DIRECTION_COUNT];
        for (int i = 0; i < Globals.DIRECTION_COUNT; ++i) {
            pinFlags[1] = pinnedFlags[1] = BitBoards.EMPTY_BITBOARD;
        }
    }

    public boolean isDragged(int row, int col) {
        return BitBoards.checkBit(dragFlags, row, col);
    }

    public boolean isLegal(int row, int col) {
        return BitBoards.checkBit(legalFlags, row, col);
    }

    public boolean isCheck(int row, int col) {
        return BitBoards.checkBit(checkFlags, row, col);
    }

    public boolean isSpecial(int row, int col) {
        return BitBoards.checkBit(specialFlags, row, col);
    }

}