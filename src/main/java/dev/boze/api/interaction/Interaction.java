package dev.boze.api.interaction;

/**
 * Interaction
 * <p>
 * Represents an interaction with the server
 * <p>
 * This can be used to rotate, place, or break blocks, interact with entities, etc.
 */
public interface Interaction {

    /**
     * Executes the interaction
     */
    void execute();
}
