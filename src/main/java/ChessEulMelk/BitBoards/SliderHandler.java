package ChessEulMelk.BitBoards;

import ChessEulMelk.Logics.Direction;

public class SliderHandler {

    public static long getSliderMoves(int i, int j, long totalOccupancy, boolean isBishop) {
        long bishopLocation = BitBoards.getMask(i, j);
        long attackMask = BitBoards.EMPTY_BITBOARD;

        Direction[] directions = isBishop ? Direction.BISHOP_DIRECTIONS : Direction.ROOK_DIRECTIONS;

        for (Direction direction : directions) {
            long miniMask = BoardFiller.fill(bishopLocation, direction);
            long blocker = BoardFiller.getBlocker(miniMask & totalOccupancy, direction);
            miniMask &= ~BoardFiller.fill(blocker, direction);

            attackMask |= miniMask;
        }

        return attackMask;
    }

}