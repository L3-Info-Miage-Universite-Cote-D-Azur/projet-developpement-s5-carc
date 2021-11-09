package logic.tile;

import logic.math.Vector2;
import logic.tile.components.Component;

import java.util.ArrayList;

public class Tile {
    private Vector2 position;
    private ArrayList<Component> components;

    public Tile() {
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }
}
