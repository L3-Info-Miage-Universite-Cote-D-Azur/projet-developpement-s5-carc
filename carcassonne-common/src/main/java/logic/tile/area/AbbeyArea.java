package logic.tile.area;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;
import logic.tile.chunk.Chunk;
import logic.tile.chunk.ChunkType;

import java.util.List;
import java.util.Set;

public class AbbeyArea extends Area {
    /**
     * Constructor for the area.
     *
     * @param chunks
     */
    public AbbeyArea(List<Chunk> chunks) {
        super(chunks);
    }

    /**
     * Gets the area type.
     * @return The area type.
     */
    @Override
    public ChunkType getType() {
        return ChunkType.ABBEY;
    }

    /**
     * Checks if the given area can be merged.
     * @param other The other area to merge with.
     * @return True if the areas can be merged, false otherwise.
     */
    @Override
    public boolean canBeMerged(Area other) {
        return other.getType() == ChunkType.ABBEY;
    }

    /**
     * Gets the remaining tile edges that can be used to continue the area including the tile to place.
     * @return The remaining tile edges.
     */
    @Override
    protected int getFreeEdges(Set<Tile> tiles, Set<Chunk> chunks) {
        Tile abbeyTile = tiles.iterator().next();
        GameBoard board = abbeyTile.getGame().getBoard();

        int freeEdges = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Vector2 position = abbeyTile.getPosition().add(new Vector2(i, j));

                if (!board.hasTileAt(position) && tiles.stream().noneMatch(tile -> tile.getPosition().equals(position))) {
                    ++freeEdges;
                }
            }
        }

        return freeEdges;
    }
}
