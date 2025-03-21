package dev.boze.client.systems.modules.misc;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class NoGhostBlocks extends Module {
    public static final NoGhostBlocks INSTANCE = new NoGhostBlocks();

    private NoGhostBlocks() {
        super("NoGhostBlocks", "Prevents ghost blocks from placements", Category.Misc);
    }
}
