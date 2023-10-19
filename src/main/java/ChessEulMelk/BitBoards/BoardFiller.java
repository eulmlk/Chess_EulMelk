package ChessEulMelk.BitBoards;

import ChessEulMelk.Logics.Direction;

public class BoardFiller {

    private static Filler[] fillers = {
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Left
                    long fillLeftMask = mask;

                    fillLeftMask |= (mask << 1) & BitBoards.NOT_H_FILE;
                    fillLeftMask |= (fillLeftMask << 2) & BitBoards.NOT_GH_FILE;
                    fillLeftMask |= (fillLeftMask << 4) & BitBoards.QUEEN_SIDE;

                    return fillLeftMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Right
                    long fillRightMask = mask;

                    fillRightMask |= (mask >> 1) & BitBoards.NOT_A_FILE;
                    fillRightMask |= (fillRightMask >> 2) & BitBoards.NOT_AB_FILE;
                    fillRightMask |= (fillRightMask >> 4) & BitBoards.KING_SIDE;

                    return fillRightMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Up
                    long fillUpMask = mask;

                    fillUpMask |= mask << 8;
                    fillUpMask |= fillUpMask << 16;
                    fillUpMask |= fillUpMask << 32;

                    return fillUpMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Down
                    long fillDownMask = mask;

                    fillDownMask |= mask >>> 8;
                    fillDownMask |= fillDownMask >>> 16;
                    fillDownMask |= fillDownMask >>> 32;

                    return fillDownMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Up Left
                    long fillUpLeftMask = mask;

                    fillUpLeftMask |= (mask << 9) & BitBoards.NOT_H_FILE;
                    fillUpLeftMask |= (fillUpLeftMask << 18) & BitBoards.NOT_GH_FILE;
                    fillUpLeftMask |= (fillUpLeftMask << 36) & BitBoards.QUEEN_SIDE;

                    return fillUpLeftMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Up Right
                    long fillUpRightMask = mask;

                    fillUpRightMask |= (mask << 7) & BitBoards.NOT_A_FILE;
                    fillUpRightMask |= (fillUpRightMask << 14) & BitBoards.NOT_AB_FILE;
                    fillUpRightMask |= (fillUpRightMask << 28) & BitBoards.KING_SIDE;

                    return fillUpRightMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Down Left
                    long fillDownLeftMask = mask;

                    fillDownLeftMask |= (mask >>> 7) & BitBoards.NOT_H_FILE;
                    fillDownLeftMask |= (fillDownLeftMask >>> 14) & BitBoards.NOT_GH_FILE;
                    fillDownLeftMask |= (fillDownLeftMask >>> 28) & BitBoards.QUEEN_SIDE;

                    return fillDownLeftMask & ~mask;
                }
            },
            new Filler() {
                @Override
                public long fill(long mask) { // Fill Down Right
                    long fillDownRightMask = mask;

                    fillDownRightMask |= (mask >>> 9) & BitBoards.NOT_A_FILE;
                    fillDownRightMask |= (fillDownRightMask >>> 18) & BitBoards.NOT_AB_FILE;
                    fillDownRightMask |= (fillDownRightMask >>> 36) & BitBoards.KING_SIDE;

                    return fillDownRightMask & ~mask;
                }
            }
    };

    public static long fill(long mask, Direction direction) {
        return fillers[direction.ordinal()].fill(mask);
    }

    public static long getBlocker(long mask, Direction direction) {
        boolean isHighest = Direction.isHighest(direction);

        return isHighest ? Long.highestOneBit(mask) : Long.lowestOneBit(mask);
    }

}