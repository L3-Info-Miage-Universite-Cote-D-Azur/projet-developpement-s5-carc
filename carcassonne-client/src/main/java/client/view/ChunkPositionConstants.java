package client.view;

import logic.math.Vector2;


/**
 * Contains all chunk position
 */
public abstract class ChunkPositionConstants {
    // A--B--C--D
    // E--F--G--H
    // I--J--K--L
    // M--N--O--P

    public static final Vector2 A = new Vector2(0, 0);
    public static final Vector2 B = new Vector2(53, 0);
    public static final Vector2 C = new Vector2(107, 0);
    public static final Vector2 D = new Vector2(160, 0);
    public static final Vector2 E = new Vector2(0, 53);
    public static final Vector2 F = new Vector2(53, 53);
    public static final Vector2 G = new Vector2(107, 53);
    public static final Vector2 H = new Vector2(160, 53);
    public static final Vector2 I = new Vector2(0, 107);
    public static final Vector2 J = new Vector2(53, 107);
    public static final Vector2 K = new Vector2(107, 107);
    public static final Vector2 L = new Vector2(160, 107);
    public static final Vector2 M = new Vector2(0, 160);
    public static final Vector2 N = new Vector2(53, 160);
    public static final Vector2 O = new Vector2(107, 160);
    public static final Vector2 P = new Vector2(160, 160);

    private ChunkPositionConstants() {
        // ignored
    }
}
