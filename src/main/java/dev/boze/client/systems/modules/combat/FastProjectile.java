package dev.boze.client.systems.modules.combat;

import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;

public class FastProjectile extends Module {
    public static final FastProjectile INSTANCE = new FastProjectile();
    public final MinMaxSetting factor = new MinMaxSetting("Factor", 1.0, 0.1, 20.0, 0.1, "Factor");
    private final BooleanSetting rotate = new BooleanSetting("Rotate", false, "Rotate");
    private final BooleanSetting minimize = new BooleanSetting("Minimize", false, "Minimizes movement");
    private final MinMaxSetting cooldown = new MinMaxSetting("Cooldown", 5.0, 0.0, 10.0, 0.1, "Cooldown in seconds");
    private final SettingCategory selection = new SettingCategory("Selection", "Projectile selection");
    private final BooleanSetting bows = new BooleanSetting("Bows", true, "Apply on bows", this.selection);
    private final BooleanSetting tridents = new BooleanSetting("Tridents", true, "Apply on tridents", this.selection);
    private final BooleanSetting pearls = new BooleanSetting("EPearls", false, "Apply on ender pearls", this.selection);
    private final BooleanSetting xp = new BooleanSetting("Xp", false, "Apply on xp", this.selection);
    private final BooleanSetting eggs = new BooleanSetting("Eggs", false, "Apply on eggs", this.selection);
    private final BooleanSetting splashPotions = new BooleanSetting("SplashPotions", false, "Apply on splash potions", this.selection);
    private final BooleanSetting snowballs = new BooleanSetting("Snowballs", false, "Apply on snowballs", this.selection);
    private final Timer field2556 = new Timer();

    public FastProjectile() {
        super("FastProjectile", "Shoot and throw projectiles faster", Category.Combat);
    }

    @EventHandler
    public void method1504(PrePacketSendEvent event) {
        if (event.packet instanceof PlayerActionC2SPacket
                && ((PlayerActionC2SPacket) event.packet).getAction().equals(Action.RELEASE_USE_ITEM)
                && this.method1506()
                || event.packet instanceof PlayerInteractItemC2SPacket
                && this.method1505(mc.player.getStackInHand(((PlayerInteractItemC2SPacket) event.packet).getHand()).getItem())) {
            if (!this.field2556.hasElapsed(this.cooldown.getValue() * 1000.0)) {
                return;
            }

            for (int var5 = 0; var5 < (int) (this.factor.getValue() * 10.0); var5++) {
                if (this.rotate.getValue()) {
                    mc.player
                            .networkHandler
                            .sendPacket(
                                    new Full(
                                            mc.player.getX(),
                                            mc.player.getY() + (this.minimize.getValue() ? 1.3E-13 : 4.3E-13),
                                            mc.player.getZ(),
                                            ((ClientPlayerEntityAccessor) mc.player).getLastYaw(),
                                            ((ClientPlayerEntityAccessor) mc.player).getLastPitch(),
                                            true
                                    )
                            );
                    mc.player
                            .networkHandler
                            .sendPacket(
                                    new Full(
                                            mc.player.getX(),
                                            mc.player.getY() + (this.minimize.getValue() ? 2.7E-13 : 8.4E-13),
                                            mc.player.getZ(),
                                            ((ClientPlayerEntityAccessor) mc.player).getLastYaw(),
                                            ((ClientPlayerEntityAccessor) mc.player).getLastPitch(),
                                            false
                                    )
                            );
                } else {
                    mc.player
                            .networkHandler
                            .sendPacket(
                                    new PositionAndOnGround(mc.player.getX(), mc.player.getY() + (this.minimize.getValue() ? 1.3E-13 : 4.3E-13), mc.player.getZ(), true)
                            );
                    mc.player
                            .networkHandler
                            .sendPacket(
                                    new PositionAndOnGround(mc.player.getX(), mc.player.getY() + (this.minimize.getValue() ? 2.7E-13 : 8.4E-13), mc.player.getZ(), false)
                            );
                }
            }
        }
    }

    private boolean method1505(Item var1) {
        return var1 instanceof EnderPearlItem && this.pearls.getValue()
                || var1 instanceof ExperienceBottleItem && this.xp.getValue()
                || var1 instanceof EggItem && this.eggs.getValue()
                || var1 instanceof SplashPotionItem && this.splashPotions.getValue()
                || var1 instanceof SnowballItem && this.snowballs.getValue();
    }

    private boolean method1506() {
        return this.bows.getValue()
                && (mc.player.getMainHandStack().getItem() instanceof BowItem || mc.player.getOffHandStack().getItem() instanceof BowItem) || this.tridents.getValue()
                && (mc.player.getMainHandStack().getItem() instanceof TridentItem || mc.player.getOffHandStack().getItem() instanceof TridentItem);
    }
}
