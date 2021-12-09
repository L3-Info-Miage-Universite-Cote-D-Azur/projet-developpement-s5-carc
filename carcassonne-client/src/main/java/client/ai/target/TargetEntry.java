package client.ai.target;

/**
 * Represents a target entry in the target list.
 */
public record TargetEntry<E>(E entry, int score) {

}