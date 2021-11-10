package logic.config;

import logic.tile.TileFlags;

public final class TileDetails {
    public String model;
    public int count;
    public String expansion;
    public TileFlags[] flags;

    public TileDetails() {
    }

    public TileDetails(String model, int count, String expansion, TileFlags[] flags) {
        this.model = model;
        this.count = count;
        this.expansion = expansion;
        this.flags = flags;
    }
}
