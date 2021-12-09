package client.view;

import logic.math.Vector2;

import java.util.Arrays;

/**
 * Polygon geometry shape
 */
public class Polygon {
    private final Vector2[] vectors;

    /**
     * @param vectors All vectors of the polygon
     */
    public Polygon(Vector2... vectors) {
        this.vectors = vectors;
    }

    /**
     * Get a specific vector
     *
     * @param i The index of the vector
     * @return The vector of the index
     */
    public Vector2 getVector(int i) {
        return vectors[i];
    }

    /**
     * Get x axe of all vectors
     *
     * @param offset The offset of the vector
     * @return The x axe of all vectors
     */
    public int[] getXs(int offset) {
        int[] xs = new int[vectors.length];
        for (int i = 0; i < xs.length; i++)
            xs[i] = vectors[i].x() + offset;

        return xs;
    }

    /**
     * Get y axe of all vectors
     *
     * @param offset The offset of the vector
     * @return The y axe of all vectors
     */
    public int[] getYs(int offset) {
        int[] ys = new int[vectors.length];
        for (int i = 0; i < ys.length; i++)
            ys[i] = vectors[i].y() + offset;

        return ys;
    }

    /**
     * Get the number vectors
     *
     * @return The number of vectors
     */
    public int getVectorCount() {
        return vectors.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        if (polygon.getVectorCount() != getVectorCount())
            return false;

        for (int i = 0; i < vectors.length; i++) {
            if (vectors[i].x() == polygon.getVector(i).x() && vectors[i].y() == polygon.getVector(i).y()) {
                continue;
            }
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(vectors);
    }
}
