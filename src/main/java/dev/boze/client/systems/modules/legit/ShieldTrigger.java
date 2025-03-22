package dev.boze.client.systems.modules.legit;

import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.PreAttackEntityEvent;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;

public class ShieldTrigger extends Module {
    public static final ShieldTrigger INSTANCE = new ShieldTrigger();
    private final BooleanSetting field2821 = new BooleanSetting("SwapBack", true, "Swaps back to previous slot after attacking");
    private final IntArraySetting field2822 = new IntArraySetting("SwapDelay", new int[]{4, 6}, 0, 20, 1, "Delay to swap back", this.field2821);
    private final Timer field2823 = new Timer();
    private long field2824 = -1L;
    private int field2825 = -1;

    public ShieldTrigger() {
        super("ShieldTrigger", "Automatically swaps to axe when hitting a shielded player", Category.Legit);
    }

    private static boolean lambda$onAttackEntityPre$0(ItemStack var0) {
        return var0.getItem() instanceof AxeItem;
    }

    @Override
    public void onEnable() {
        this.field2825 = -1;
    }

    @EventHandler
    public void method1615(HandleInputEvent event) {
        if (this.field2821.getValue() && this.field2825 != -1 && this.field2823.hasElapsed((double) this.field2824)) {
            ((KeyBindingAccessor) mc.options.hotbarKeys[this.field2825]).setTimesPressed(1);
            this.field2825 = -1;
        }
    }

    @EventHandler
    public void method1616(PreAttackEntityEvent event) {
        if (!event.method1022()) {
            if (event.entity instanceof LivingEntity var5 && var5.isBlocking() && !(mc.player.getInventory().getMainHandStack().getItem() instanceof AxeItem)) {
                int var7 = InventoryHelper.method168(ShieldTrigger::lambda$onAttackEntityPre$0);
                if (var7 != -1 && var7 != mc.player.getInventory().selectedSlot) {
                    this.field2825 = mc.player.getInventory().selectedSlot;
                    mc.player.getInventory().selectedSlot = var7;
                    this.field2823.reset();
                    this.field2824 = (long) this.field2822.method1376() * 50L;
                }
            }
        }
    }
}
