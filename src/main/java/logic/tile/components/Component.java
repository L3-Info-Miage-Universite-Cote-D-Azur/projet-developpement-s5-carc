package logic.tile.components;

import logic.tile.Tile;

public abstract class Component {
    private final Tile parent;

    public Component(Tile parent) {
        this.parent = parent;
    }

    public Tile getParent() {
        return parent;
    }
}
