package client.ai.heuristic;

import java.util.LinkedList;
import java.util.List;

public class TargetList {
    private static final int SIZE = 10;
    private final LinkedList<TargetEntry> entries;

    public TargetList() {
        this.entries = new LinkedList<>();
    }

    /**
     * Adds the given position to the pickable list.
     * @param entry The position to add.
     */
    public void add(TargetEntry entry) {
        this.entries.add(entry);

        /* As the added entry can be the most costly,
         * we need to throw away after the insertion.
         */
        if (this.entries.size() > SIZE) {
            throwEntry();
        }
    }

    /**
     * Returns the current pickable positions.
     * @return The current pickable positions.
     */
    public List<TargetEntry> getEntries() {
        return entries;
    }

    /**
     * Picks the best position from the list.
     * @return The best position from the list.
     */
    public TargetEntry pick() {
        TargetEntry best = null;

        for (TargetEntry position : entries) {
            if (best == null || position.score < best.score) {
                best = position;
            }
        }

        return best;
    }

    /**
     * Throws away the most costly entry from the list.
     */
    public void throwEntry() {
        int index = 0;
        int max = -1;
        int i = 0;

        for (TargetEntry position : entries) {
            if (position.score > max) {
                max = position.score;
                index = i;
            }

            i++;
        }

        entries.remove(index);
    }
}
