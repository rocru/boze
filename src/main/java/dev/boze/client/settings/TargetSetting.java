package dev.boze.client.settings;

import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.player.PlayerEntity;

public class TargetSetting implements IMinecraft, SettingsGroup {
    private final SettingCategory field1317 = new SettingCategory("Targets", "Target selection");
    private final BooleanSetting field1318 = new BooleanSetting("Players", true, "Target players", this.field1317);
    private final BooleanSetting field1319 = new BooleanSetting("Friends", false, "Target friends", this.field1317);
    private final BooleanSetting field1320 = new BooleanSetting("Animals", false, "Target animals", this.field1317);
    private final BooleanSetting field1321 = new BooleanSetting("Monsters", false, "Target monsters", this.field1317);
    private final BooleanSetting field1322 = new BooleanSetting("Invisible", true, "Target invisible entities\nApplies to all target types\n", this.field1317);
    private final Setting<?>[] field1323 = new Setting[]{this.field1317, this.field1318, this.field1319, this.field1320, this.field1321, this.field1322};

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    public boolean method2055(Entity entity) {
        if (!this.field1322.getValue() && entity.isInvisibleTo(mc.player)) {
            return false;
        } else if (entity instanceof PlayerEntity) {
            if (entity == mc.player) {
                return false;
            } else if (entity instanceof FakePlayerEntity) {
                return false;
            } else {
                return Friends.method2055(entity) ? this.field1319.getValue() : this.field1318.getValue();
            }
        } else {
            return switch (entity.getType().getSpawnGroup()) {
                case SpawnGroup.CREATURE, SpawnGroup.AMBIENT, SpawnGroup.WATER_AMBIENT, SpawnGroup.WATER_CREATURE -> this.field1320.getValue();
                case SpawnGroup.MONSTER -> this.field1321.getValue();
                default -> false;
            };
        }
    }

    @Override
    public Setting<?>[] get() {
        return this.field1323;
    }
}
