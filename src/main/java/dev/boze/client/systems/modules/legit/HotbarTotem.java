package dev.boze.client.systems.modules.legit;

import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.mixin.EntityStatusS2CPacketAccessor;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.OffHand;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

public class HotbarTotem extends Module {
    public static final HotbarTotem INSTANCE = new HotbarTotem();
    private final FloatSetting field2797 = new FloatSetting(
            "OnceHealth", 6.0F, 1.0F, 20.0F, 0.1F, "Health to swap to a totem once\nWhen you reach this health, it will swap to a totem once\n"
    );
    private final FloatSetting field2798 = new FloatSetting(
            "StayHealth",
            0.0F,
            1.0F,
            20.0F,
            0.1F,
            "Health to swap to totem permanently\nWhen you reach this health, it will swap to a totem permanently\nYou won't be able to use other items until your health is above this value\nSetting this to 0 will disable this feature\n"
    );
    private final MinMaxDoubleSetting field2799 = new MinMaxDoubleSetting("Delay", new double[]{0.75, 2.5}, 0.0, 20.0, 0.25, "Delay in ticks between swaps");
    private final BooleanSetting field2800 = new BooleanSetting(
            "SwapBack", false, "Swap back once health goes above once health\nOr Offhand has Totem if IgnoreOffhand disabled\n"
    );
    private final BooleanSetting field2801 = new BooleanSetting("IgnoreOffhand", false, "Still swaps even when holding a totem in Offhand");
    private final BooleanSetting field2802 = new BooleanSetting(
            "PopAwait", false, "Swap on pop until Offhand confirms swap\nWorks when Offhand is enabled in Anarchy mode\n", this::lambda$new$0
    );
    private final Timer field2803 = new Timer();
    private boolean field2804 = false;
    private int field2805 = -1;
    private final Timer field2806 = new Timer();

    public HotbarTotem() {
        super("HotbarTotem", "Swaps to a totem in your hotbar when you're low on health", Category.Legit);
    }

    @Override
    public void onEnable() {
        this.field2804 = false;
        this.field2805 = -1;
    }

    @EventHandler
    public void method1605(HandleInputEvent event) {
        if (this.field2805 != -1 && mc.player.getMainHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
            this.field2805 = mc.player.getInventory().selectedSlot;
        }

        if (this.field2803.hasElapsed(this.field2799.method1295() * 50.0)) {
            float var5 = mc.player.getHealth() + mc.player.getAbsorptionAmount();
            if (!this.field2806.hasElapsed(1000.0)) {
                if (OffHand.INSTANCE.ab == null) {
                    this.field2806.setLastTime(0L);
                } else {
                    var5 = 0.0F;
                }
            }

            boolean var6 = !this.field2801.getValue() && mc.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING;
            if (mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING) {
                if (var5 > this.field2797.getValue() || var6) {
                    if (this.field2800.getValue() && this.field2805 != -1) {
                        ((KeyBindingAccessor) mc.options.hotbarKeys[this.field2805]).setTimesPressed(1);
                        this.field2805 = -1;
                        this.field2803.reset();
                        this.field2799.method1296();
                    }

                    this.field2804 = false;
                }
            } else {
                if (var5 > this.field2797.getValue() || var6) {
                    this.field2805 = -1;
                }

                if (!var6) {
                    boolean var7 = false;
                    if ((double) this.field2798.getValue().floatValue() > 0.0 && var5 <= this.field2798.getValue()) {
                        var7 = true;
                    } else if (var5 <= this.field2797.getValue() && !this.field2804) {
                        var7 = true;
                    } else if (var5 > this.field2797.getValue() && this.field2804) {
                        this.field2804 = false;
                    }

                    if (var7) {
                        for (int var8 = 0; var8 < 9; var8++) {
                            if (mc.player.getInventory().getStack(var8).getItem() == Items.TOTEM_OF_UNDYING) {
                                if (this.field2800.getValue()) {
                                    this.field2805 = mc.player.getInventory().selectedSlot;
                                }

                                ((KeyBindingAccessor) mc.options.hotbarKeys[var8]).setTimesPressed(1);
                                this.field2804 = true;
                                this.field2803.reset();
                                this.field2799.method1296();
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1606(PacketBundleEvent event) {
        if (event.packet instanceof EntityStatusS2CPacket var5
                && this.field2802.getValue()
                && !this.field2801.getValue()
                && OffHand.INSTANCE.method1971()
                && OffHand.INSTANCE.isEnabled()
                && var5.getStatus() == 35) {
            try {
                if (((EntityStatusS2CPacketAccessor) var5).getEntityId() == mc.player.getId()) {
                    this.field2806.reset();
                }
            } catch (Exception var7) {
            }
        }
    }

    private boolean lambda$new$0() {
        return !this.field2801.getValue() && OffHand.INSTANCE.method1971();
    }
}
