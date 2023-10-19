package ChessEulMelk.BitBoards;

@FunctionalInterface
public interface MoveMaskGetter {

    public long getMoveMasks(long enemyOccupancy, long friendOccupancy, int i, int j);
}