package logic.config.excel;

import excel.ExcelNode;
import logic.tile.chunk.AreaChunk;
import logic.tile.chunk.Chunk;
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
    public List<ArrayList<ChunkId>> areaChunks;
    public String model;
    public String expansion;
    public EnumSet<TileFlags> flags;
    public int count;

    /**
     * Creates a tile excel configuration from the given parameters.
     * @param chunks The chunks configuration.
     * @param areaChunks The area chunks configuration.
     * @param model The model of tile.
     * @param expansion The expansion of tile.
     * @param flags The flags of tile.
     * @param count The count of tile in the stack.
     */
    public TileExcelConfig(TileChunkExcelConfig[] chunks, List<ArrayList<ChunkId>> areaChunks, String model, String expansion, EnumSet<TileFlags> flags, int count) {
        this.chunks = chunks;
        this.areaChunks = areaChunks;
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

        loadChunks(chunkTypesExcel, chunkReferencesExcel);
        loadData(dataExcel);
    }

    /**
     * Loads the chunks configuration from the given excel nodes.
     * @param typeNode The chunk types excel node.
     * @param referenceNode The chunk references excel node.
     */
    private void loadChunks(ExcelNode typeNode, ExcelNode referenceNode) {
        ChunkType[] chunkTypes = new ChunkType[ChunkId.values().length];
        ChunkId[][] chunkReferences = new ChunkId[ChunkId.values().length][];

        HashMap<String, ArrayList<ChunkId>> chunkGroups = new HashMap<>();

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = ChunkType.valueOf(getCellValue(typeNode, chunkId));
            String reference = getCellValue(referenceNode, chunkId);

            if (reference.length() != 0) {
                ArrayList<ChunkId> ids = chunkGroups.getOrDefault(reference, null);

                if (ids == null) {
                    ids = new ArrayList<>();
                    chunkGroups.put(reference, ids);
                }

                ids.add(chunkId);
            }

            chunkTypes[chunkId.ordinal()] = type;
        }

        for (Map.Entry<String, ArrayList<ChunkId>> group : chunkGroups.entrySet()) {
            ArrayList<ChunkId> ids = group.getValue();

            for (ChunkId id : ids) {
                chunkReferences[id.ordinal()] = Arrays.stream(id.getNeighbours()).filter(ids::contains).toArray(ChunkId[]::new);
            }
        }

        areaChunks = chunkGroups.entrySet().stream().map(e -> e.getValue()).toList();
        chunks = new TileChunkExcelConfig[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = chunkTypes[chunkId.ordinal()];

            chunks[chunkId.ordinal()] = new TileChunkExcelConfig(type);
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

        for (ArrayList<ChunkId> chunkList : areaChunks) {
            AreaChunk areaChunk = new AreaChunk();

            for (ChunkId chunkId : chunkList) {
                areaChunk.addChunk(tile.getChunk(chunkId));
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
