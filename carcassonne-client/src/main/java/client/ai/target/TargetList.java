package client.ai.target;

import java.util.LinkedList;
import java.util.List;

/**
 * List of targets with a maximum size.
 * If the list is full, the less scored target is removed.
 */
public class TargetList<E> {
    private final int capacity;
    private final LinkedList<TargetEntry> entries;

    public TargetList(int size) {
        this.capacity = size;
        this.entries = new LinkedList<>();
    }

    /**
     * Adds the given position to the pickable list.
     *
     * @param entry The position to add.
     */
    public void add(E entry, int cost) {
        this.entries.add(new TargetEntry(entry, cost));

        /* As the added entry can be the most costly,
         * we need to throw away after the insertion.
         */
        if (this.entries.size() > this.capacity) {
            throwEntry();
        }
    }

    /**
     * Returns the current entries in the list.
     *
     * @return The current entries in the list.
     */
    public List<TargetEntry> getEntries() {
        return entries;
    }

    /**
     * Picks the best target from the list.
     *
     * @return The best target from the list.
     */
    public E pick() {
        TargetEntry<E> best = null;

        for (TargetEntry<E> entry : entries) {
            if (best == null || entry.getScore() > best.getScore()) {
                best = entry;
            }
        }

        return best != null ? best.getEntry() : null;
    }

    /**
     * Throws away the less scored entry from the list.
     */
    public void throwEntry() {
        int index = 0;
        int max = -1;
        int i = 0;

        for (TargetEntry entry : entries) {
            if (entry.getScore() < max) {
                max = entry.getScore();
                index = i;
            }

            i++;
        }

        entries.remove(index);
    }
}
