package dev.boze.client.systems.modules.client;

import dev.boze.client.events.EntityAddedEvent;
import dev.boze.client.events.EntityRemovedEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.NotificationType;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.misc.SoundFX;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.fakeplayer.FakeClientPlayerEntity;
import mapped.Class1204;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;

public class Notifications extends Module {
    public static final Notifications INSTANCE = new Notifications();
    public final EnumSetting<NotificationType> field837 = new EnumSetting<NotificationType>("Notifs", NotificationType.Toasts, "Notifications notifications");
    public final BooleanSetting field838 = new BooleanSetting("KeepInChat", false, "Keep messages in chat", this::lambda$new$0);
    public final RGBASetting field839 = new RGBASetting("Color", new RGBAColor(-14737633), "Notification color");
    public final RGBASetting field840 = new RGBASetting("TextColor", new RGBAColor(-1), "Color for text/icons");
    public final IntSetting field841 = new IntSetting("Roundness", 5, 0, 10, 1, "Notification roundness");
    public final FloatSetting field842 = new FloatSetting("Duration", 1.0F, 0.5F, 10.0F, 0.1F, "Notification duration in seconds");
    public final FloatSetting field843 = new FloatSetting("Slide", 0.3F, 0.05F, 3.0F, 0.05F, "Notification slide duration in seconds");
    public final FloatSetting field844 = new FloatSetting("Scale", 0.6F, 0.3F, 1.0F, 0.05F, "Notification scale");
    public final SettingCategory field845 = new SettingCategory("VisualRange", "Visual range notifications");
    public final BooleanSetting field846 = new BooleanSetting("Entering", false, "Notify when player enters visual range", this.field845);
    public final BooleanSetting field847 = new BooleanSetting("Leaving", false, "Notify when player leaves visual range", this.field845);
    private final BooleanSetting field848 = new BooleanSetting("FriendIgnore", true, "Ignore friends", this.field845);
    private final BooleanSetting field849 = new BooleanSetting("JoinIgnore", true, "Ignore within 5s of joining server", this.field845);
    private final BooleanSetting field850 = new BooleanSetting("TotemPops", false, "Notify when other players pop totems");

    private Notifications() {
        super("Notifications", "Displays notifications", Category.Client);
        this.setEnabled(true);
    }

    @EventHandler
    private void method382(EntityAddedEvent var1) {
        if (mc.player != null) {
            if (!this.field849.getValue() || mc.player.age >= 100) {
                if (var1.field1913 instanceof PlayerEntity
                        && !(var1.field1913 instanceof FakePlayerEntity)
                        && !(var1.field1913 instanceof FakeClientPlayerEntity)
                        && !var1.field1913.getUuid().equals(mc.player.getUuid())
                        && this.field846.getValue()) {
                    if (!this.field848.getValue() || !Friends.method346(var1.field1913.getNameForScoreboard())) {
                        NotificationManager.method1151(
                                new Notification(
                                        null,
                                        var1.field1913.getNameForScoreboard() + " entered visual range",
                                        dev.boze.client.gui.notification.Notifications.CATEGORY_RENDER,
                                        NotificationPriority.Normal
                                )
                        );
                    }

                    if (SoundFX.INSTANCE.isEnabled() && !SoundFX.INSTANCE.field3121.getValue().isEmpty()) {
                        SoundFX.INSTANCE.method1775(new File(ConfigManager.sounds, SoundFX.INSTANCE.field3121.getValue() + ".wav"));
                    }
                }
            }
        }
    }

    @EventHandler
    private void method383(EntityRemovedEvent var1) {
        if (mc.player != null) {
            if (!this.field849.getValue() || mc.player.age >= 100) {
                if (var1.field1915 instanceof PlayerEntity
                        && !(var1.field1915 instanceof FakePlayerEntity)
                        && !(var1.field1915 instanceof FakeClientPlayerEntity)
                        && !var1.field1915.getUuid().equals(mc.player.getUuid())
                        && this.field847.getValue()
                        && (!this.field848.getValue() || !Friends.method346(var1.field1915.getNameForScoreboard()))) {
                    NotificationManager.method1151(
                            new Notification(
                                    null,
                                    var1.field1915.getNameForScoreboard() + " left visual range",
                                    dev.boze.client.gui.notification.Notifications.VISIBLE_OFF,
                                    NotificationPriority.Normal
                            )
                    );
                    if (SoundFX.INSTANCE.isEnabled() && !SoundFX.INSTANCE.field3122.getValue().isEmpty()) {
                        SoundFX.INSTANCE.method1775(new File(ConfigManager.sounds, SoundFX.INSTANCE.field3122.getValue() + ".wav"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void method384(Class1204 event) {
        if (this.field850.getValue() && event.field65 != mc.player) {
            String var5 = "th";
            if (event.field66 != 3 && (event.field66 <= 22 || !Integer.toString(event.field66).endsWith("3"))) {
                if (event.field66 != 2 && (event.field66 <= 21 || !Integer.toString(event.field66).endsWith("2"))) {
                    if (event.field66 == 1 || event.field66 > 20 && Integer.toString(event.field66).endsWith("1")) {
                        var5 = "st";
                    }
                } else {
                    var5 = "nd";
                }
            } else {
                var5 = "rd";
            }

            String var10003 = event.field65.getName().getString();
            int var7 = event.field66;
            NotificationManager.method1151(
                    new Notification(
                            null, var10003 + " popped " + var7 + var5 + " totem", dev.boze.client.gui.notification.Notifications.PRIORITY, NotificationPriority.Normal
                    )
            );
        }
    }

    private boolean lambda$new$0() {
        return this.field837.getValue() == NotificationType.Chat;
    }
}
