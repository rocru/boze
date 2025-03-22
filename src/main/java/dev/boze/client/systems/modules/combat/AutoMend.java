package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.movement.ElytraRecast;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class3069;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.util.Hand;

public class AutoMend extends Module {
    public static final AutoMend INSTANCE = new AutoMend();
    public final BooleanSetting mendRemove = new BooleanSetting("MendRemove", true, "Remove armor when mending using AutoArmor");
    private final EnumSetting<AnticheatMode> interactionMode = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Interaction mode");
    private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing");
    private final EnumSetting<SwapMode> swapMode = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Auto swap mode");
    private final IntSetting delay = new IntSetting("Delay", 1, 0, 4, 1, "Delay for throwing XP");
    private final BooleanSetting damageDisable = new BooleanSetting("DamageDisable", true, "Disable on damage");
    private final BooleanSetting crystalDisable = new BooleanSetting("CrystalDisable", true, "Disable when crystals placed nearby");
    private float field684;
    private int field685;

    public AutoMend() {
        super("AutoMend", "Automatically mends your armor", Category.Combat);
    }

    private static boolean lambda$getSlot$0(ItemStack var0) {
        return var0.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    @Override
    public void onEnable() {
        if (!MinecraftUtils.isClientActive()) {
            this.setEnabled(false);
        } else {
            this.field684 = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        }
    }

    @Override
    public boolean setEnabled(boolean newState) {
        if (newState && MinecraftUtils.isClientActive()) {
            boolean var5 = false;

            for (int var6 = 0; var6 <= 3; var6++) {
                ItemStack var7 = mc.player.getInventory().getArmorStack(var6);
                if (!var7.isEmpty() && var7.isDamaged()) {
                    var5 = true;
                    break;
                }
            }

            if (!var5) {
                this.setEnabled(false);
                return false;
            }
        }

        return super.setEnabled(newState);
    }

    @EventHandler(
            priority = 5
    )
    public void method1885(ACRotationEvent event) {
        if (event.method1017() == this.interactionMode.getValue()) {
            if (this.field685 >= this.delay.getValue()) {
                if (!ElytraRecast.INSTANCE.isEnabled()) {
                    if (!this.method1971()) {
                        this.setEnabled(false);
                    } else {
                        event.pitch = 90.0F;
                        event.method1021(true);
                    }
                }
            }
        }
    }

    @EventHandler(
            priority = 5
    )
    public void method1883(RotationEvent event) {
        if (!event.method554(RotationMode.Sequential)) {
            if (this.field685 < this.delay.getValue()) {
                this.field685++;
            } else if (this.method1971()) {
                int var5 = this.method2010();
                if (var5 == -1) {
                    NotificationManager.method1151(new Notification(this.getName(), "No XP bottles found", Notifications.WARNING, NotificationPriority.Yellow));
                    this.setEnabled(false);
                } else if (InventoryUtil.method534(this, 5, this.swapMode.getValue(), var5)) {
                    Class5913.method16(var5 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                    if (this.swing.getValue()) {
                        mc.player.swingHand(var5 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                    }

                    this.field685 = 0;
                    InventoryUtil.method396(this);
                    if (AntiCheat.INSTANCE.field2322.getValue() && !dev.boze.client.utils.player.InventoryUtil.isInventoryOpen()) {
                        mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                    }
                }
            }
        }
    }

    private boolean method1971() {
        if (!MinecraftUtils.isClientActive()) {
            return false;
        } else {
            boolean var4 = false;

            for (int var5 = 0; var5 <= 3; var5++) {
                ItemStack var6 = mc.player.getInventory().getArmorStack(var5);
                if (!var6.isEmpty() && var6.isDamaged()) {
                    var4 = true;
                    break;
                }
            }

            return var4;
        }
    }

    public int method2010() {
        return InventoryHelper.method166(AutoMend::lambda$getSlot$0, this.swapMode.getValue());
    }

    @EventHandler
    private void method1942(PostPlayerTickEvent var1) {
        float var5 = mc.player.getHealth() + mc.player.getAbsorptionAmount();
        if (this.damageDisable.getValue() && var5 < this.field684) {
            this.setEnabled(false);
        } else {
            if (this.crystalDisable.getValue()) {
                for (Entity var7 : mc.world.getEntities()) {
                    if (var7 instanceof EndCrystalEntity
                            && var7.distanceTo(mc.player) < 6.0F
                            && Class3069.method6004(mc.player, var7.getPos()) >= (double) Math.min(2.0F, var5)) {
                        this.setEnabled(false);
                    }
                }
            }

            this.field684 = var5;
        }
    }
}
