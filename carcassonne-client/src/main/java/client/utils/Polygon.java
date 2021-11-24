package client.utils;

import logic.math.Vector2;

import java.util.Arrays;

public class Polygon {
    private Vector2[] vectors;

    public Polygon(Vector2... vectors) {
        this.vectors = vectors;
    }

    public Vector2 getVector(int i) {
        return vectors[i];
    }

    public int[] getXs(int offset) {
        int[] xs = new int[vectors.length];
        for (int i = 0; i < xs.length; i++)
            xs[i] = vectors[i].getX() + offset;

        return xs;
    }

    public int[] getYs(int offset) {
        int[] ys = new int[vectors.length];
        for (int i = 0; i < ys.length; i++)
            ys[i] = vectors[i].getY() + offset;

        return ys;
    }

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
            if (vectors[i].getX() == polygon.getVector(i).getX() && vectors[i].getY() == polygon.getVector(i).getY()) {
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
