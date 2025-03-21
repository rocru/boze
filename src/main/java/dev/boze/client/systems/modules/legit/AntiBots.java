package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AntiBotTabList;
import dev.boze.client.mixininterfaces.IOtherClientPlayerEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.misc.FakePlayer;
import mapped.Class5926;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class AntiBots extends Module {
    public static final AntiBots INSTANCE = new AntiBots();
    private final BooleanSetting field996 = new BooleanSetting("TicksExisted", true, "Check if entity has existed for a certain amount of ticks");
    private final IntSetting field997 = new IntSetting("Ticks", 20, 1, 100, 1, "Amount of ticks entity has to exist for before attacking", this.field996);
    private final BooleanSetting field998 = new BooleanSetting("Moving", true, "Check if entity has moved within visual range");
    private final EnumSetting<AntiBotTabList> field999 = new EnumSetting<AntiBotTabList>(
            "TabList",
            AntiBotTabList.On,
            "Tab list check mode\n - Off: Don't check tab list\n - On: Ignore players not present in tab list\n - ZeroPing: Ignore zero-ping players in tab list\n"
    );

    private AntiBots() {
        super("AntiBots", "Detects bots and prevents modules from attacking them\nOnly applies to ghost modules\n", Category.Legit);
    }

    public static boolean method1617(PlayerEntity player) {
        if (!INSTANCE.isEnabled()) {
            return false;
        } else if (INSTANCE.field996.getValue() && player.age < INSTANCE.field997.getValue()) {
            return true;
        } else if (INSTANCE.field998.getValue() && player instanceof OtherClientPlayerEntity var4) {
            return !((IOtherClientPlayerEntity) var4).boze$hasMoved();
        } else if (INSTANCE.field999.getValue() != AntiBotTabList.On) {
            return INSTANCE.field999.getValue() == AntiBotTabList.ZeroPing && Class5926.method100(player) == 0;
        } else {
            return player instanceof OtherClientPlayerEntity
                    && (FakePlayer.INSTANCE.fakePlayer == null || player.getId() != FakePlayer.INSTANCE.fakePlayer.getId())
                    && !player.getName().getString().contains("-");
        }
    }

    public static boolean method2055(Entity entity) {
        return entity instanceof PlayerEntity var4 && method1617(var4);
    }
}
