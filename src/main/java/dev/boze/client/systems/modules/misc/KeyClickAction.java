package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.KeyClick;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.Bind;
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

public class KeyClickAction extends Module {
    public static final KeyClickAction INSTANCE = new KeyClickAction();
    private final BindSetting field2968 = new BindSetting("Key", Bind.fromKey(345), "Key to run actions");
    private final IntSetting field2969 = new IntSetting("XpDelay", 0, 0, 5, 1, "Delay for throwing xp when middle clicking");
    private final EnumSetting<KeyClick> field2970 = new EnumSetting<KeyClick>("Air", KeyClick.EP, "What to do when facing the air");
    private final EnumSetting<KeyClick> field2971 = new EnumSetting<KeyClick>("Entity", KeyClick.Friend, "What to do when facing an entity");
    private final EnumSetting<KeyClick> field2972 = new EnumSetting<KeyClick>("Block", KeyClick.XP, "What to do when facing a block");
    private final EnumSetting<KeyClick> field2973 = new EnumSetting<KeyClick>("Flying", KeyClick.Rocket, "What to do when elytra flying");
    private final EnumSetting<SwapMode> field2974 = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Swap mode for item actions", this::lambda$new$0);
    private final dev.boze.client.utils.Timer field2975 = new dev.boze.client.utils.Timer();

    public KeyClickAction() {
        super("KCA", "Key Click Action", Category.Misc);
    }

    @EventHandler
    public void method1723(KeyEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.action == KeyAction.Press && mc.currentScreen == null) {
                if (this.field2968.getValue().matches(true, event.key)) {
                    if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                        this.method1727(this.field2971.getValue());
                    } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                        this.method1727(this.field2972.getValue());
                    } else if (mc.player.isFallFlying()) {
                        this.method1727(this.field2973.getValue());
                    } else {
                        this.method1727(this.field2970.getValue());
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1724(MouseButtonEvent event) {
        if (MinecraftUtils.isClientActive()) {
            if (event.action == KeyAction.Press && mc.currentScreen == null) {
                if (this.field2968.getValue().matches(false, event.button)) {
                    if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                        this.method1727(this.field2971.getValue());
                    } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                        this.method1727(this.field2972.getValue());
                    } else if (mc.player.isFallFlying()) {
                        this.method1727(this.field2973.getValue());
                    } else {
                        this.method1727(this.field2970.getValue());
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1725(PostPlayerTickEvent event) {
        if (!(mc.currentScreen instanceof ClickGUI) && MinecraftUtils.isClientActive()) {
            if (this.field2968.getValue().isPressed()) {
                if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                    this.method1726(this.field2971.getValue());
                } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                    this.method1726(this.field2972.getValue());
                } else if (mc.player.isFallFlying()) {
                    this.method1726(this.field2973.getValue());
                } else {
                    this.method1726(this.field2970.getValue());
                }
            }
        }
    }

    private void method1726(KeyClick var1) {
        if (var1 == KeyClick.XP) {
            if (!this.field2975.hasElapsed(50 * this.field2969.getValue())) {
                return;
            }

            int var5 = InventoryHelper.method166(KeyClickAction::lambda$doActionIfXp$1, this.field2974.getValue());
            if (var5 != -1) {
                InventoryUtil.method534(this, 350, this.field2974.getValue(), var5);
                Class5913.method16(Hand.MAIN_HAND);
                this.field2975.reset();
                InventoryUtil.method396(this);
            }
        }
    }

    private void method1727(KeyClick keyClick) {
        switch (keyClick.ordinal()) {
            case 3: {
                int n = InventoryHelper.method166(KeyClickAction::lambda$doAction$2, this.field2974.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field2974.getValue(), n);
                if (!KeyClickAction.mc.player.getItemCooldownManager().isCoolingDown(KeyClickAction.mc.player.getMainHandStack().getItem())) {
                    Class5913.method16(Hand.MAIN_HAND);
                }
                InventoryUtil.method396(this);
                break;
            }
            case 2: {
                if (!this.field2975.hasElapsed(50 * this.field2969.getValue())) {
                    return;
                }
                int n = InventoryHelper.method166(KeyClickAction::lambda$doAction$3, this.field2974.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field2974.getValue(), n);
                Class5913.method16(Hand.MAIN_HAND);
                this.field2975.reset();
                InventoryUtil.method396(this);
                break;
            }
            case 1: {
                Entity entity;
                if (KeyClickAction.mc.targetedEntity == null || !((entity = KeyClickAction.mc.targetedEntity) instanceof PlayerEntity))
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
                int n = InventoryHelper.method166(KeyClickAction::lambda$doAction$4, this.field2974.getValue());
                if (n == -1) break;
                InventoryUtil.method534(this, 350, this.field2974.getValue(), n);
                if (!KeyClickAction.mc.player.getItemCooldownManager().isCoolingDown(KeyClickAction.mc.player.getMainHandStack().getItem())) {
                    Class5913.method16(Hand.MAIN_HAND);
                }
                InventoryUtil.method396(this);
            }
        }
    }

    private static boolean lambda$doAction$4(ItemStack var0) {
        return var0.getItem() == Items.FIREWORK_ROCKET;
    }

    private static boolean lambda$doAction$3(ItemStack var0) {
        return var0.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private static boolean lambda$doAction$2(ItemStack var0) {
        return var0.getItem() == Items.ENDER_PEARL;
    }

    private static boolean lambda$doActionIfXp$1(ItemStack var0) {
        return var0.getItem() == Items.EXPERIENCE_BOTTLE;
    }

    private boolean lambda$new$0() {
        return this.field2970.getValue().field1730
                || this.field2971.getValue().field1730
                || this.field2972.getValue().field1730
                || this.field2973.getValue().field1730;
    }
}
