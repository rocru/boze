package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.FoodUtil;
import dev.boze.client.utils.InventoryUtil;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.*;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

public class FastUse extends Module {
    public static final FastUse INSTANCE = new FastUse();
    public static boolean field2947 = false;
    public final BooleanSetting field2948 = new BooleanSetting("NCPStrict", true, "NCP Strict");
    private final BooleanSetting field2949 = new BooleanSetting("GhostFix", true, "Ghost Fix");
    private final IntSetting field2950 = new IntSetting("Delay", 0, 0, 5, 0, "Delay for fast use");
    private final IntSetting field2951 = new IntSetting("StartDelay", 0, 0, 10, 0, "Delay to start fast use");
    private final BooleanSetting field2952 = new BooleanSetting("FastXP", true, "XP faster");
    private final BooleanSetting field2953 = new BooleanSetting("FastPlace", false, "Place blocks faster");
    private final BooleanSetting field2954 = new BooleanSetting("FastCrystal", false, "Place crystals faster");
    private final BooleanSetting field2955 = new BooleanSetting("FastEat", false, "Remove eat delay");
    private final BooleanSetting field2956 = new BooleanSetting("NoCrystalPlace", false, "Prevent manual crystal placing");
    public final BooleanSetting field2957 = new BooleanSetting("PacketEat", false, "Keep eating if you stop right clicking");
    private int field2958;
    private int field2959;
    public static boolean field2960 = false;

    public FastUse() {
        super("FastUse", "Removes item use delay", Category.Misc);
    }

    @EventHandler
    public void method1715(PrePlayerTickEvent event) {
        if (mc.world != null && mc.player != null) {
            this.field2958++;
            if (this.field2958 > this.field2950.getValue()) {
                if (this.method1718()) {
                    this.field2959++;
                    this.field2958 = 0;
                    if (((MinecraftClientAccessor) mc).getItemUseCooldown() != 0 && this.field2959 > this.field2951.getValue()) {
                        ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                    }
                } else {
                    this.field2959 = 0;
                }
            }
        }
    }

    @EventHandler
    public void method1716(PrePacketSendEvent event) {
        if (mc.player != null && mc.world != null) {
            if (this.field2949.getValue() && this.method1717(InventoryUtil.method1774().getItem())) {
                if (event.packet instanceof PlayerInteractBlockC2SPacket var5) {
                    if (Class5924.method2101(var5.getBlockHitResult().getBlockPos())) {
                        return;
                    }

                    event.method1020();
                }
            } else if (this.field2956.getValue()
                    && event.packet instanceof PlayerInteractBlockC2SPacket
                    && mc.player.getStackInHand(((PlayerInteractBlockC2SPacket) event.packet).getHand()).getItem() instanceof EndCrystalItem) {
                if (field2960) {
                    field2960 = false;
                } else {
                    event.method1021(true);
                }
            }
        }
    }

    private boolean method1717(Item var1) {
        return var1 instanceof ExperienceBottleItem || var1 instanceof BoatItem;
    }

    private boolean method1718() {
        Item var4 = mc.player.getMainHandStack().getItem();
        Item var5 = mc.player.getOffHandStack().getItem();
        if (!this.field2952.getValue() || !(var4 instanceof ExperienceBottleItem) && !(var5 instanceof ExperienceBottleItem)) {
            if (!this.field2953.getValue() || !(var4 instanceof BlockItem) && !(var5 instanceof BlockItem)) {
                if (!this.field2954.getValue() || !(var4 instanceof EndCrystalItem) && !(var5 instanceof EndCrystalItem)) {
                    if (this.field2955.getValue() && FoodUtil.isFood(mc.player.getMainHandStack())) {
                        ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                    return true;
                }
            } else {
                ((MinecraftClientAccessor) mc).setItemUseCooldown(0);
                return true;
            }
        } else {
            return true;
        }
    }
}
