package ChessEulMelk.Logics;

import java.util.ArrayList;

public class Game {
    private ArrayList<String> positions;
    private int current;

    public Game() {
        positions = new ArrayList<>();
    }

    public void addPosition(String position) {
        positions.add(position);
        ++current;
    }

    public void popBack() {
        if (positions.size() == 1)
            return;

        int last = positions.size() - 1;
        positions.remove(last);
        current = current < last ? current : last;
    }

    public String getPosition(int index) {
        return positions.get(index);
    }

    public int getPositionCount() {
        return positions.size();
    }

    public void toNext() {
        ++current;
        if (current == positions.size())
            --current;
    }

    public void toPrev() {
        --current;
        if (current < 0)
            current = 0;
    }

    public void toFirst() {
        current = 0;
    }

    public void toLast() {
        current = positions.size() - 1;
    }

    public String getCurrent() {
        return positions.get(current);
    }
}