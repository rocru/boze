package mapped;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameMode;

public class Class5926 implements IMinecraft {
    public Class5926() {
        super();
    }

    public static float method99(final LivingEntity entity) {
        if (entity == null) {
            return 0.0f;
        }
        return entity.getHealth() + entity.getAbsorptionAmount();
    }

    public static int method100(final PlayerEntity player) {
        if (Class5926.mc.getNetworkHandler() == null) {
            return 0;
        }
        final PlayerListEntry playerListEntry = Class5926.mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return 0;
        }
        return playerListEntry.getLatency();
    }

    public static GameMode method101(final PlayerEntity player) {
        if (player == null) {
            return null;
        }
        final PlayerListEntry playerListEntry = Class5926.mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        if (playerListEntry == null) {
            return null;
        }
        return playerListEntry.getGameMode();
    }
}
