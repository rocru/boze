package dev.boze.api.interaction;

/**
 * Rotate Interaction
 * <p>
 * Represents a rotate interaction with the server
 */
public interface RotateInteraction extends Interaction {

    /**
     *
     * @return Rotation to rotate to
     * @see Rotation
     */
    Rotation getRotation();
}
