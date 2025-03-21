package dev.boze.client.systems.modules.render;

import dev.boze.client.enums.HandTweaksHideShield;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;

public class HandTweaks extends Module {
    public static final HandTweaks INSTANCE = new HandTweaks();
    public final BooleanSetting field3570 = new BooleanSetting(
            "ForceOpposite", false, "Force opposite hand\n(e.g. left hand for right-hand only servers like hypixel)\n"
    );
    public final BooleanSetting field3571 = new BooleanSetting("OldAnimations", false, "Use old swinging animations");
    public final BooleanSetting field3572 = new BooleanSetting("OldBlocking", false, "Use old blocking animation");
    public final EnumSetting<HandTweaksHideShield> field3573 = new EnumSetting<HandTweaksHideShield>(
            "HideShield", HandTweaksHideShield.Always, "When to hide shield", this.field3572
    );
    public final IntSetting field3574 = new IntSetting("SwingDuration", 6, 1, 20, 1, "Swing duration");
    public final BooleanSetting field3575 = new BooleanSetting("Eating", false, "Modify eating (and drinking) animation");
    public final IntSetting field3576 = new IntSetting("Duration", 2, 1, 10, 1, "Duration of animation", this.field3575);
    public final FloatSetting field3577 = new FloatSetting("Scale", 0.1F, 0.0F, 1.0F, 0.01F, "Scale of animation", this.field3575);
    public final BooleanSetting field3578 = new BooleanSetting("Flip", false, "Flip the animation", this.field3575);
    public final BooleanSetting field3579 = new BooleanSetting("NoSwitchAnim", false, "Disable switching animation");
    public final BooleanSetting field3580 = new BooleanSetting("NoXSway", false, "Disable X-axis sway");
    public final BooleanSetting field3581 = new BooleanSetting("NoYSway", false, "Disable Y-axis sway");
    public final BooleanSetting field3582 = new BooleanSetting("NoXSwing", false, "Disable X-axis swinging animation");
    public final BooleanSetting field3583 = new BooleanSetting("NoYSwing", false, "Disable Y-axis swinging animation");
    public final BooleanSetting field3584 = new BooleanSetting("NoZSwing", false, "Disable Z-axis swinging animation");

    public HandTweaks() {
        super("HandTweaks", "Modifies hand rendering", Category.Render);
    }

    public static boolean method1960(LivingEntity entity) {
        return entity.isUsingItem() && method1961(entity);
    }

    public static boolean method1961(LivingEntity entity) {
        if (INSTANCE.isEnabled() && INSTANCE.field3572.getValue()) {
            Item var4 = entity.getMainHandStack().getItem();
            Item var5 = entity.getOffHandStack().getItem();
            return (var4 instanceof SwordItem || var4 instanceof AxeItem || var4 instanceof MaceItem) && var5 instanceof ShieldItem
                    || (var5 instanceof SwordItem || var5 instanceof AxeItem || var5 instanceof MaceItem) && var4 instanceof ShieldItem;
        } else {
            return false;
        }
    }
}
