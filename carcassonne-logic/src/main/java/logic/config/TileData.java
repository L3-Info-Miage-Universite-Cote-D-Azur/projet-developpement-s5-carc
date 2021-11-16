package logic.config;

import logic.tile.TileFlags;

import java.util.EnumSet;

public final class TileData {
    public String model;
    public String expansion;
    public EnumSet<TileFlags> flags;
    public int count;

    public TileData() {
    }

    public TileData(String model, int count, String expansion, EnumSet<TileFlags> flags) {
        this.model = model;
        this.count = count;
        this.expansion = expansion;
        this.flags = flags;
    }
}
