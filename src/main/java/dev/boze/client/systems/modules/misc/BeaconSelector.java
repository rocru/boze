package dev.boze.client.systems.modules.misc;

import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class BeaconSelector extends Module {
    public static final BeaconSelector INSTANCE = new BeaconSelector();

    public BeaconSelector() {
        super("BeaconSelector", "Lets you select any beacon effect, regardless of pyramid size", Category.Misc);
    }
}
