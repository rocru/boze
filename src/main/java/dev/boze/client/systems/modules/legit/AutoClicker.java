package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.ClickMethod;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.*;

import java.util.HashMap;

public class AutoClicker extends Module {
    public static final AutoClicker INSTANCE = new AutoClicker();
    public final BooleanSetting field2732 = new BooleanSetting("LeftClick", true, "Left Click");
    public final BooleanSetting field2734 = new BooleanSetting("OnlyWhenHolding", false, "Only click when holding left click", this.field2732);
    private final MinMaxDoubleSetting field2735 = new MinMaxDoubleSetting(
            "StartDelay", new double[]{0.0, 0.0}, 0.0, 10.0, 0.1, "Delay before clicking on hold for OnlyWhenHolding", this.field2734::getValue, this.field2732
    );
    private final FloatSetting field2731 = new FloatSetting("Jitter", 0.0F, 0.0F, 2.0F, 0.01F, "Mouse jitter amount");
    private final BooleanSetting field2733 = new BooleanSetting("OnlyWeapon", false, "Only left click when holding a weapon", this.field2732);
    private final EnumSetting<ClickMethod> field2736 = new EnumSetting<ClickMethod>("Mode", ClickMethod.Normal, "Left Click Mode", this.field2732);
    private final FloatSetting field2738 = new FloatSetting(
            "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$0, this.field2732
    );
    private final IntArraySetting field2737 = new IntArraySetting("CPS", new int[]{6, 10}, 1, 20, 1, "Left clicks per second", this.field2732);
    private final ClickManager field2747 = new ClickManager(this.field2736, this.field2737, this.field2738);
    private final BooleanSetting field2739 = new BooleanSetting("RightClick", false, "Right Click");
    private final BooleanSetting field2740 = new BooleanSetting("NoPearls", false, "Don't right click when holding pearls", this.field2739);
    private final BooleanSetting field2741 = new BooleanSetting("NoRods", false, "Don't right click when holding fishing rods", this.field2739);
    private final BooleanSetting field2742 = new BooleanSetting("OnlyWhenHolding", false, "Only click when holding right click", this.field2739);
    private final MinMaxDoubleSetting field2743 = new MinMaxDoubleSetting(
            "StartDelay", new double[]{0.0, 0.0}, 0.0, 10.0, 0.1, "Delay before clicking on hold for OnlyWhenHolding", this.field2742::getValue, this.field2739
    );
    private final EnumSetting<ClickMethod> field2744 = new EnumSetting<ClickMethod>("RightMode", ClickMethod.Normal, "Right Click Mode", this.field2739);
    private final FloatSetting field2746 = new FloatSetting(
            "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$1, this.field2739
    );
    private final IntArraySetting field2745 = new IntArraySetting("CPS", new int[]{6, 10}, 1, 20, 1, "Right clicks per second", this.field2739);
    private final ClickManager field2748 = new ClickManager(this.field2744, this.field2745, this.field2746);
    private final HashMap<KeyBinding, Timer> field2750 = new HashMap();
    private final HashMap<KeyBinding, MinMaxDoubleSetting> field2751;
    private float field2749 = 0.0F;

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you", Category.Legit);
        this.field2750.put(mc.options.attackKey, new Timer());
        this.field2750.put(mc.options.useKey, new Timer());
        this.field2751 = new HashMap();
        this.field2751.put(mc.options.attackKey, this.field2735);
        this.field2751.put(mc.options.useKey, this.field2743);
    }

    @Override
    public void onEnable() {
        this.field2749 = 0.0F;
        this.field2747.method2172();
        this.field2748.method2172();
    }

    @EventHandler(
            priority = 55
    )
    public void method1585(MouseUpdateEvent event) {
        if (this.field2749 > 0.0F && !event.method1022()) {
            double var5 = (double) (this.field2749 * this.field2731.getValue()) * Math.random();
            double var7 = (double) (this.field2749 * this.field2731.getValue()) * Math.random();
            if (Math.random() > 0.5) {
                var5 *= -1.0;
            }

            if (Math.random() > 0.5) {
                var7 *= -1.0;
            }

            event.deltaX += var5;
            event.deltaY += var7;
            event.method1021(true);
            this.field2749 = 0.0F;
        }
    }

    @EventHandler
    public void method1586(RotationEvent event) {
        if (!event.method554(RotationMode.Vanilla)) {
            if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) {
                this.field2747.method2172();
                this.field2748.method2172();
            } else {
                if (!this.field2739.getValue()
                        || mc.player.isUsingItem()
                        || this.field2740.getValue() && mc.player.getMainHandStack().getItem() == Items.ENDER_PEARL
                        || this.field2741.getValue() && mc.player.getMainHandStack().getItem() == Items.FISHING_ROD) {
                    this.field2748.method2172();
                } else {
                    this.method1587(mc.options.useKey, this.field2748, event, this.field2742.getValue());
                }

                if (this.field2732.getValue() && !mc.interactionManager.isBreakingBlock()) {
                    if (this.field2733.getValue()) {
                        Item var5 = mc.player.getMainHandStack().getItem();
                        if (!(var5 instanceof SwordItem) && !(var5 instanceof AxeItem) && !(var5 instanceof TridentItem)) {
                            this.field2747.method2172();
                            return;
                        }
                    }

                    this.method1587(mc.options.attackKey, this.field2747, event, this.field2734.getValue());
                } else {
                    this.field2747.method2172();
                }
            }
        }
    }

    private void method1587(KeyBinding var1, ClickManager var2, RotationEvent var3, boolean var4) {
        if (!var1.isPressed()) {
            this.field2750.get(var1).reset();
        }

        if ((!var1.isPressed() || !this.field2750.get(var1).hasElapsed(1000.0 * this.field2751.get(var1).method1295())) && var4) {
            var2.method2172();
        } else {
            int var8 = var2.method2171();
            if (var8 > 0 && ((KeyBindingAccessor) var1).getTimesPressed() == 0) {
                ((KeyBindingAccessor) var1).setTimesPressed(var8);
                var3.method2142();
                if (this.field2731.getValue() > 0.0F) {
                    this.field2749++;
                }

                this.field2751.get(var1).method1296();
            }
        }
    }

    private boolean lambda$new$1() {
        return this.field2744.getValue() == ClickMethod.Vanilla;
    }

    private boolean lambda$new$0() {
        return this.field2736.getValue() == ClickMethod.Vanilla;
    }
}
