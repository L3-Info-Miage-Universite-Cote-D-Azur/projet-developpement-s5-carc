package client.ai.target;

/**
 * Represents a target entry in the target list.
 */
public final class TargetEntry<E> {
    private final E entry;
    private final int score;

    public TargetEntry(E entry, int score) {
        this.entry = entry;
        this.score = score;
    }

    /**
     * Gets the entry of the target.
     * @return the position of the target
     */
    public E getEntry() {
        return entry;
    }

    /**
     * Gets the cost of the target.
     * @return the cost of the target
     */
    public int getScore() {
        return score;
    }
}
