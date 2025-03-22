package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.MiddleClick;
import dev.boze.client.enums.MiddleClickMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult.Type;

public class MiddleClickAction extends Module {
    public static final MiddleClickAction INSTANCE = new MiddleClickAction();
    private final EnumSetting<MiddleClickMode> field490 = new EnumSetting<MiddleClickMode>(
            "Mode", MiddleClickMode.Anarchy, "Mode for middle click action", MiddleClickAction::lambda$new$0
    );
    private final IntSetting field491 = new IntSetting("XpDelay", 0, 0, 5, 1, "Delay for throwing xp when middle clicking", this::method1972);
    private final EnumSetting<MiddleClick> field493 = new EnumSetting<MiddleClick>("Air", MiddleClick.XP, "What to do when facing the air", this::method1972);
    private final EnumSetting<MiddleClick> field494 = new EnumSetting<MiddleClick>("Entity", MiddleClick.Friend, "What to do when facing an entity", this::method1972);
    private final EnumSetting<MiddleClick> field495 = new EnumSetting<MiddleClick>("Block", MiddleClick.XP, "What to do when facing a block", this::method1972);
    private final EnumSetting<MiddleClick> field496 = new EnumSetting<MiddleClick>("Flying", MiddleClick.Rocket, "What to do when elytra flying", this::method1972);
    private final EnumSetting<SwapMode> field497 = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Swap mode for item actions", this::lambda$new$1);
    private final dev.boze.client.utils.Timer field498 = new dev.boze.client.utils.Timer();
    public BooleanSetting field492 = new BooleanSetting("NoMCPick", true, "Cancel vanilla block middle click pick", this::method1972);

    public MiddleClickAction() {
        super("MCA", "Middle Click Action\nIn Ghost mode, this is just middle click friend\n", Category.Misc);
        this.field435 = true;
    }

    private static boolean lambda$doAction$5(ItemStack var0) {
        return var0.getItem() == Items.FIREWORK_ROCKET;
    }

    private static boolean lambda$doAction$4(ItemStack var0) {
        return var0.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private static boolean lambda$doAction$3(ItemStack var0) {
        return var0.getItem() == Items.ENDER_PEARL;
    }

    private static boolean lambda$doActionIfXp$2(ItemStack var0) {
        return var0.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }

    private boolean method1971() {
        return Options.INSTANCE.method1971() || this.field490.getValue() == MiddleClickMode.Ghost;
    }

    private boolean method1972() {
        return !this.method1971();
    }

    @EventHandler
    public void method1812(MouseButtonEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.action == KeyAction.Press && !(mc.currentScreen instanceof ClickGUI)) {
                if (event.button == 2) {
                    if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                        this.method267(this.method1971() ? MiddleClick.Friend : this.field494.getValue());
                    } else if (this.method1972()) {
                        if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                            this.method267(this.field495.getValue());
                        } else if (mc.player.isFallFlying()) {
                            this.method267(this.field496.getValue());
                        } else {
                            this.method267(this.field493.getValue());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1942(PostPlayerTickEvent event) {
        if (!(mc.currentScreen instanceof ClickGUI) && !this.method1971() && MinecraftUtils.isClientActive()) {
            if (mc.options.pickItemKey.isPressed()) {
                if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                    this.method266(this.field494.getValue());
                } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                    this.method266(this.field495.getValue());
                } else if (mc.player.isFallFlying()) {
                    this.method266(this.field496.getValue());
                } else {
                    this.method266(this.field493.getValue());
                }
            }
        }
    }

    private void method266(MiddleClick var1) {
        if (var1 == MiddleClick.XP) {
            if (!this.field498.hasElapsed(50 * this.field491.getValue())) {
                return;
            }

            int var5 = InventoryHelper.method166(MiddleClickAction::lambda$doActionIfXp$2, this.field497.getValue());
            if (var5 != -1) {
                InventoryUtil.method534(this, 350, this.field497.getValue(), var5);
                Class5913.method16(Hand.MAIN_HAND);
                this.field498.reset();
                InventoryUtil.method396(this);
            }
        }
    }

    private void method267(MiddleClick middleClick) {
        switch (middleClick.ordinal()) {
            case 3: {
                int n = InventoryHelper.method166(MiddleClickAction::lambda$doAction$3, this.field497.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field497.getValue(), n);
                if (!MiddleClickAction.mc.player.getItemCooldownManager().isCoolingDown(MiddleClickAction.mc.player.getMainHandStack().getItem())) {
                    Class5913.method16(Hand.MAIN_HAND);
                }
                InventoryUtil.method396(this);
                break;
            }
            case 2: {
                if (!this.field498.hasElapsed(50 * this.field491.getValue())) {
                    return;
                }
                int n = InventoryHelper.method166(MiddleClickAction::lambda$doAction$4, this.field497.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field497.getValue(), n);
                Class5913.method16(Hand.MAIN_HAND);
                this.field498.reset();
                InventoryUtil.method396(this);
                break;
            }
            case 1: {
                Entity entity;
                if (MiddleClickAction.mc.targetedEntity == null || !((entity = MiddleClickAction.mc.targetedEntity) instanceof PlayerEntity))
                    break;
                if (Friends.method2055(entity)) {
                    Friends.method1750(entity.getName().getString());
                    NotificationManager.method1151(new Notification(this.getName(), " Unfriended " + entity.getName().getString(), Notifications.PLAYERS_REMOVE, NotificationPriority.Normal));
                    break;
                }
                Friends.addFriend(entity.getName().getString());
                NotificationManager.method1151(new Notification(this.getName(), " Friended " + entity.getName().getString(), Notifications.PLAYERS_ADD, NotificationPriority.Normal));
                break;
            }
            case 4: {
                int n = InventoryHelper.method166(MiddleClickAction::lambda$doAction$5, this.field497.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field497.getValue(), n);
                if (!MiddleClickAction.mc.player.getItemCooldownManager().isCoolingDown(MiddleClickAction.mc.player.getMainHandStack().getItem())) {
                    Class5913.method16(Hand.MAIN_HAND);
                }
                InventoryUtil.method396(this);
            }
        }
    }

    private boolean lambda$new$1() {
        return this.method1972()
                && (
                this.field493.getValue().field1797
                        || this.field494.getValue().field1797
                        || this.field495.getValue().field1797
                        || this.field496.getValue().field1797
        );
    }
}
