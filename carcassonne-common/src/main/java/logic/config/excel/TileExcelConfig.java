package logic.config.excel;

import excel.ExcelNode;
import logic.tile.chunk.ChunkArea;
import logic.tile.chunk.ChunkId;
import logic.tile.chunk.ChunkType;
import logic.tile.Tile;
import logic.tile.TileFlags;

import java.util.*;

/**
 * Represents a tile excel configuration.
 */
public class TileExcelConfig {
    public TileChunkExcelConfig[] chunks;
    public String model;
    public String expansion;
    public EnumSet<TileFlags> flags;
    public int count;

    /**
     * Creates a tile excel configuration from the given parameters.
     * @param chunks The chunks configuration.
     * @param model The model of tile.
     * @param expansion The expansion of tile.
     * @param flags The flags of tile.
     * @param count The count of tile in the stack.
     */
    public TileExcelConfig(TileChunkExcelConfig[] chunks, String model, String expansion, EnumSet<TileFlags> flags, int count) {
        this.chunks = chunks;
        this.model = model;
        this.expansion = expansion;
        this.flags = flags;
        this.count = count;
    }

    /**
     * Creates a tile excel configuration from the given excel node.
     * @param node The excel node.
     */
    public TileExcelConfig(ExcelNode node) {
        ExcelNode chunkExcel = node.getChild("Chunks");
        ExcelNode chunkTypesExcel = chunkExcel.getChild("Types");
        ExcelNode chunkReferencesExcel = chunkExcel.getChild("References");
        ExcelNode dataExcel = node.getChild("Data");

        loadData(dataExcel);
        loadChunks(chunkTypesExcel, chunkReferencesExcel);
    }

    /**
     * Loads the chunks configuration from the given excel nodes.
     * @param typeNode The chunk types excel node.
     * @param referenceNode The chunk references excel node.
     */
    private void loadChunks(ExcelNode typeNode, ExcelNode referenceNode) {
        ChunkType[] chunkTypes = new ChunkType[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            chunkTypes[chunkId.ordinal()] = ChunkType.valueOf(getCellValue(typeNode, chunkId));
        }

        chunks = new TileChunkExcelConfig[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = chunkTypes[chunkId.ordinal()];

            chunks[chunkId.ordinal()] = new TileChunkExcelConfig(type);
        }

        HashMap<String, ArrayList<ChunkId>> zones = new HashMap<>();

        for (ChunkId chunkId : ChunkId.values()) {
            String referenceId = getCellValue(referenceNode, chunkId);

            if (!zones.containsKey(referenceId)) {
                zones.put(referenceId, new ArrayList<>());
            }

            zones.get(referenceId).add(chunkId);
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
     * @param node The excel node.
     */
    private void loadData(ExcelNode node) {
        model = node.getRow("Model").getValue("Value");
        expansion = node.getRow("Expansion").getValue("Value");
        flags = EnumSet.noneOf(TileFlags.class);

        for (String flag : node.getRow("Flags").getValue("Value").split(",")) {
            if (flag.length() != 0) {
                flags.add(TileFlags.valueOf(flag));
            }
        }

        count = Integer.parseInt(node.getRow("Count").getValue("Value"));
    }

    /**
     * Instantiate a tile with the current configuration.
     * @return The tile instantiated.
     */
    public Tile createTile() {
        Tile tile = new Tile(this);

        for (ChunkId chunkId : ChunkId.values()) {
            tile.setChunk(chunkId, chunks[chunkId.ordinal()].createChunk(tile));
        }

        for (TileChunkAreaConfig areaConfig : Arrays.stream(chunks).map(c -> c.getArea()).distinct().toList()) {
            ChunkArea area = new ChunkArea(areaConfig.getChunkType());

            for (ChunkId chunkInArea : areaConfig.getChunkIds()) {
                area.addChunk(tile.getChunk(chunkInArea));
            }
        }

        return tile;
    }

    private static String getCellValue(ExcelNode node, ChunkId chunkId) {
        int row;
        int column;

        switch (chunkId) {
            case NORTH_LEFT:
                row = 0;
                column = 1;
                break;
            case NORTH_MIDDLE:
                row = 0;
                column = 2;
                break;
            case NORTH_RIGHT:
                row = 0;
                column = 3;
                break;
            case EAST_TOP:
                row = 1;
                column = 4;
                break;
            case EAST_MIDDLE:
                row = 2;
                column = 4;
                break;
            case EAST_BOTTOM:
                row = 3;
                column = 4;
                break;
            case SOUTH_LEFT:
                row = 4;
                column = 1;
                break;
            case SOUTH_MIDDLE:
                row = 4;
                column = 2;
                break;
            case SOUTH_RIGHT:
                row = 4;
                column = 3;
                break;
            case WEST_TOP:
                row = 1;
                column = 0;
                break;
            case WEST_MIDDLE:
                row = 2;
                column = 0;
                break;
            case WEST_BOTTOM:
                row = 3;
                column = 0;
                break;
            case CENTER_MIDDLE:
                row = 2;
                column = 2;
                break;
            default:
                throw new IllegalArgumentException("Invalid chunk id: " + chunkId);
        }

        return node.getRowAt(row).getValueAt(column);
    }
}
