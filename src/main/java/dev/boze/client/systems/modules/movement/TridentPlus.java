package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.ItemEnchantmentUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class TridentPlus extends Module {
    public static final TridentPlus INSTANCE = new TridentPlus();
    public final MinMaxSetting field3360 = new MinMaxSetting("Velocity", 1.0, 0.1, 10.0, 0.1, "Velocity multiplier\nAbove 1.0 won't work on most servers");
    public final BooleanSetting field3361 = new BooleanSetting("AlwaysBoost", true, "Make trident work outside water/rain");
    private final BooleanSetting field3362 = new BooleanSetting("Spam", false, "Spam click trident");
    private final IntSetting field3363 = new IntSetting("Delay", 3, 0, 20, 1, "Delay between trident uses", this.field3362::getValue);
    public final BindSetting field3364 = new BindSetting("Hover", Bind.create(), "Hover in the air with trident");
    private int field3365 = 0;

    public TridentPlus() {
        super("TridentPlus", "Improve functionality of tridents", Category.Movement);
    }

    @EventHandler
    private void method1883(RotationEvent var1) {
        if (var1.field1284 == RotationMode.Vanilla) {
            if (this.field3365 > 0) {
                this.field3365--;
            } else {
                if (this.field3362.getValue()) {
                    ItemStack var5 = mc.player.getMainHandStack();
                    if (var5.getItem() != Items.TRIDENT || !ItemEnchantmentUtils.hasEnchantment(var5, Enchantments.RIPTIDE)) {
                        return;
                    }

                    ((KeyBindingAccessor) mc.options.useKey).setTimesPressed(1);
                    this.field3365 = this.field3363.getValue();
                }
            }
        }
    }
}
