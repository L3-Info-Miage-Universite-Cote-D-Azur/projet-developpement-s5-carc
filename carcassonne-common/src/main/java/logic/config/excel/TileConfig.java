package logic.config.excel;

import excel.ExcelNode;
import logic.Game;
import logic.tile.Tile;
import logic.tile.TileFlags;
import logic.tile.area.AreaFactory;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;

import java.util.*;

/**
 * Represents a tile excel configuration.
 */
public class TileConfig {
    private TileChunkConfig[] chunks;
    private String model;
    private String expansion;
    private Set<TileFlags> flags;
    private int count;

    /**
     * Creates a tile excel configuration from the given parameters.
     *
     * @param chunks    The chunks configuration.
     * @param model     The model of tile.
     * @param expansion The expansion of tile.
     * @param flags     The flags of tile.
     * @param count     The count of tile in the stack.
     */
    public TileConfig(TileChunkConfig[] chunks, String model, String expansion, Set<TileFlags> flags, int count) {
        this.chunks = chunks;
        this.model = model;
        this.expansion = expansion;
        this.flags = flags;
        this.count = count;
    }

    /**
     * Returns whether the tile has the given flag.
     * @param flag The flag.
     * @return Whether the tile has the given flag.
     */
    public boolean hasFlag(TileFlags flag) {
        return flags.contains(flag);
    }

    /**
     * Gets the flags of the tile.
     * @return The flags of the tile.
     */
    public Set<TileFlags> getFlags() {
        return flags;
    }

    /**
     * Gets the sprite model of the tile.
     * @return The sprite model of the tile.
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the number of this tile in the stack.
     * @return The number of this tile in the stack.
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the number of this tile in the stack.
     * @param count The number of this tile in the stack.
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Creates a tile excel configuration from the given excel node.
     *
     * @param node The excel node.
     */
    public TileConfig(ExcelNode node) {
        ExcelNode chunkExcel = node.getChild("Chunks");
        ExcelNode chunkTypesExcel = chunkExcel.getChild("Types");
        ExcelNode chunkReferencesExcel = chunkExcel.getChild("References");
        ExcelNode dataExcel = node.getChild("Data");

        loadData(dataExcel);
        loadChunks(chunkTypesExcel, chunkReferencesExcel);
    }

    private static String getCellValue(ExcelNode node, ChunkId chunkId) {
        int row;
        int column;

        switch (chunkId) {
            case NORTH_LEFT -> {
                row = 0;
                column = 1;
            }
            case NORTH_MIDDLE -> {
                row = 0;
                column = 2;
            }
            case NORTH_RIGHT -> {
                row = 0;
                column = 3;
            }
            case EAST_TOP -> {
                row = 1;
                column = 4;
            }
            case EAST_MIDDLE -> {
                row = 2;
                column = 4;
            }
            case EAST_BOTTOM -> {
                row = 3;
                column = 4;
            }
            case SOUTH_LEFT -> {
                row = 4;
                column = 1;
            }
            case SOUTH_MIDDLE -> {
                row = 4;
                column = 2;
            }
            case SOUTH_RIGHT -> {
                row = 4;
                column = 3;
            }
            case WEST_TOP -> {
                row = 1;
                column = 0;
            }
            case WEST_MIDDLE -> {
                row = 2;
                column = 0;
            }
            case WEST_BOTTOM -> {
                row = 3;
                column = 0;
            }
            case CENTER_MIDDLE -> {
                row = 2;
                column = 2;
            }
            default -> throw new IllegalArgumentException("Invalid chunk id: " + chunkId);
        }

        return node.getRowAt(row).getValueAt(column);
    }

    /**
     * Loads the chunks configuration from the given excel nodes.
     *
     * @param typeNode      The chunk types excel node.
     * @param referenceNode The chunk references excel node.
     */
    private void loadChunks(ExcelNode typeNode, ExcelNode referenceNode) {
        ChunkType[] chunkTypes = new ChunkType[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            chunkTypes[chunkId.ordinal()] = ChunkType.valueOf(getCellValue(typeNode, chunkId));
        }

        chunks = new TileChunkConfig[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = chunkTypes[chunkId.ordinal()];

            chunks[chunkId.ordinal()] = new TileChunkConfig(type);
        }

        HashMap<String, ArrayList<ChunkId>> zones = new HashMap<>();

        for (ChunkId chunkId : ChunkId.values()) {
            String referenceId = getCellValue(referenceNode, chunkId);

            zones.computeIfAbsent(referenceId, k -> new ArrayList<>()).add(chunkId);
        }

        for (ArrayList<ChunkId> chunkIds : zones.values()) {
            TileChunkAreaConfig areaConfig = new TileChunkAreaConfig(chunks[chunkIds.get(0).ordinal()].getType(), chunkIds);

            for (ChunkId chunkId : chunkIds) {
                chunks[chunkId.ordinal()].setArea(areaConfig);
            }
        }
    }

    /**
     * Loads the tile data from the given excel node.
     *
     * @param node The excel node.
     */
    private void loadData(ExcelNode node) {
        String value = "Value";
        model = node.getRow("Model").getValue(value);
        expansion = node.getRow("Expansion").getValue(value);
        flags = EnumSet.noneOf(TileFlags.class);


        for (String flag : node.getRow("Flags").getValue(value).split(",")) {
            if (flag.length() != 0) {
                flags.add(TileFlags.valueOf(flag));
            }
        }

        count = Integer.parseInt(node.getRow("Count").getValue(value));
    }

    /**
     * Instantiate a tile with the current configuration.
     *
     * @return The tile instantiated.
     */
    public Tile createTile(Game game) {
        Tile tile = new Tile(this, game);

        for (ChunkId chunkId : ChunkId.values()) {
            tile.setChunk(chunkId, chunks[chunkId.ordinal()].createChunk(tile));
        }

        for (TileChunkAreaConfig areaConfig : Arrays.stream(chunks).map(TileChunkConfig::getArea).distinct().toList()) {
            AreaFactory.create((areaConfig.getChunkIds().stream().map(tile::getChunk).toList()));
        }

        return tile;
    }
}
