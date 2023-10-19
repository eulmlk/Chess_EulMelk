package ChessEulMelk.BitBoards;

import ChessEulMelk.Globals.Globals;

public class AmbiguityTester {

    private static long getFileMask(long mask) {
        long upMask, downMask;
        upMask = downMask = mask;

        upMask |= upMask << 8;
        upMask |= upMask << 16;
        upMask |= upMask << 32;

        downMask |= downMask >>> 8;
        downMask |= downMask >>> 16;
        downMask |= downMask >>> 32;

        return upMask | downMask;
    }

    private static long getFileMask(int index) {
        return BitBoards.H_FILE << (Globals.LAST_INDEX - index);
    }

    private static long getRankMask(int index) {
        index = Globals.LAST_INDEX - index;
        return BitBoards.RANK_1 << (index * 8);
    }

    private static long getRankMask(long mask) {
        long leftMask, rightMask;
        leftMask = rightMask = mask;

        leftMask |= (leftMask << 1) & BitBoards.NOT_H_FILE;
        leftMask |= (leftMask << 2) & BitBoards.NOT_GH_FILE;
        leftMask |= (leftMask << 4) & BitBoards.QUEEN_SIDE;

        rightMask |= (rightMask >>> 1) & BitBoards.NOT_A_FILE;
        rightMask |= (rightMask >>> 2) & BitBoards.NOT_AB_FILE;
        rightMask |= (rightMask >>> 4) & BitBoards.KING_SIDE;

        return leftMask | rightMask;
    }

    public static boolean testFileAmbiguity(long moveMasks, int col) {
        long fileMask = getFileMask(col);
        long conflictMask = getFileMask(moveMasks);

        return (moveMasks) != 0 && (conflictMask & fileMask) == 0;
    }

    public static boolean testRankAmbiguity(long moveMasks, int row) {
        long rankMask = getRankMask(row);
        long conflictMask = getRankMask(moveMasks);

        return (moveMasks) != 0 && (conflictMask & rankMask) == 0;
    }

}