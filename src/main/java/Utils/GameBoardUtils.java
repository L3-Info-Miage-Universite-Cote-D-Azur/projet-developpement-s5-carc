package Utils;

import logic.board.GameBoard;
import logic.math.Vector2;
import logic.tile.Tile;

public interface GameBoardUtils {


    static int maxSizeBoard(GameBoard gameBoard) {
        int sizeMax = 0;
        for (Tile tile : gameBoard.getTiles()) {
            sizeMax = Math.max(sizeMax, Math.abs(tile.getPosition().getX()));
            sizeMax = Math.max(sizeMax, Math.abs(tile.getPosition().getY()));
        }

        return sizeMax * 2;
    }

    static Vector2 getCoordinateImage(Vector2 position, int finalSize, int imgSize) {
        int x = position.getX() * imgSize + finalSize / 2 - imgSize / 2;
        int y = -position.getY() * imgSize + finalSize / 2 - imgSize / 2;
        return new Vector2(x, y);
    }

    static Vector2 getCoordinateImageNoCenter(Vector2 position, int finalSize, int imgSize) {
        int x = position.getX() * imgSize + finalSize / 2;
        int y = -position.getY() * imgSize + finalSize / 2;
        return new Vector2(x, y);
    }
}
