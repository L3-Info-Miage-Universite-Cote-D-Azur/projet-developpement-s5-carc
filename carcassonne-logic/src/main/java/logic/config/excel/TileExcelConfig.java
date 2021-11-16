package logic.config.excel;

import excel.ExcelDocument;
import logic.tile.ChunkId;
import logic.tile.ChunkType;
import logic.tile.Tile;
import logic.tile.TileFlags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.stream.Collectors;

public class TileExcelConfig {
    public TileChunkExcelConfig[] chunks;
    public String model;
    public String expansion;
    public EnumSet<TileFlags> flags;
    public int count;

    public TileExcelConfig(TileChunkExcelConfig[] chunks, String model, String expansion, EnumSet<TileFlags> flags, int count) {
        this.chunks = chunks;
        this.model = model;
        this.expansion = expansion;
        this.flags = flags;
        this.count = count;
    }

    public TileExcelConfig(ExcelDocument reader) {
        ExcelDocument tileChunkMapExcel = reader.getDocument("chunks");
        ExcelDocument tileChunkReferenceExcel = reader.getDocument("references");
        ExcelDocument tileDetailsExcel = reader.getDocument("data");

        loadChunks(tileChunkMapExcel, tileChunkReferenceExcel);
        loadDetails(tileDetailsExcel);
    }

    private void loadChunks(ExcelDocument mapDocument, ExcelDocument referenceDocument) {
        ChunkType[] chunkTypes = new ChunkType[ChunkId.values().length];
        ChunkId[][] chunkReferences = new ChunkId[ChunkId.values().length][];

        HashMap<String, ArrayList<ChunkId>> chunkGroups = new HashMap<>();

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = ChunkType.valueOf(getCellValue(mapDocument, chunkId));
            String reference = getCellValue(referenceDocument, chunkId);

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

        chunks = new TileChunkExcelConfig[ChunkId.values().length];

        for (ChunkId chunkId : ChunkId.values()) {
            ChunkType type = chunkTypes[chunkId.ordinal()];
            ChunkId[] references = chunkReferences[chunkId.ordinal()];

            chunks[chunkId.ordinal()] = new TileChunkExcelConfig(type, references);
        }
    }

    private void loadDetails(ExcelDocument document) {
        model = document.getCell("Model", "Value");
        expansion = document.getCell("Expansion", "Value");
        flags = Arrays.stream(document.getCell("Flags", "Value").split(",")).map(TileFlags::valueOf).collect(Collectors.toCollection(() -> EnumSet.allOf(TileFlags.class)));
    }

    public Tile createTile() {
        Tile tile = new Tile(this);

        for (ChunkId chunkId : ChunkId.values()) {
            tile.setChunk(chunkId, chunks[chunkId.ordinal()].createChunk(tile));
        }

        return tile;
    }

    private static String getCellValue(ExcelDocument document, ChunkId chunkId) {
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

        return document.getCell(row, column);
    }
}
