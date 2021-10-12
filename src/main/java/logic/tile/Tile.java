package logic.tile;

public abstract class Tile {
    private static final int START = 0;
    private static final int ROAD = 1;
    private static final int FIELD = 2;
    private static final int TOWN_CHUNK = 3;

    private int type;

    private int getType(){
        return this.type;
    }

    private TileType getTileType(int type) {
        this.type = type;
        return type; // I try but i don't know how to return a TileType. Instead of that i return an int.
    }
}
