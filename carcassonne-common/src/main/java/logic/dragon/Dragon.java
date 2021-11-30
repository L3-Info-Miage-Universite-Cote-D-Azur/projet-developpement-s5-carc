package logic.dragon;

import logic.math.Vector2;

import java.util.ArrayList;

/**
 * Class representing the dragon spawned by the volcano.
 */
public final class Dragon {
    private final ArrayList<Vector2> path;

    public Dragon() {
        this.path = new ArrayList<>();
    }

    /**
     * Moves the dragon to the specified position.
     * @param position the position to move to
     */
    public void moveTo(Vector2 position) {
        if (path.contains(position)) {
            throw new IllegalArgumentException("Dragon cannot move to position " + position + " because it is already on the path.");
        }

        path.add(position);
    }

    /**
     * Gets the current dragon position.
     * @return the current dragon position
     */
    public Vector2 getPosition() {
        return path.get(path.size() - 1);
    }
}