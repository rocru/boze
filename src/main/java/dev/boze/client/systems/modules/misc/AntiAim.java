package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.AntiAimYaw;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.EntityUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class AntiAim extends Module {
    public static final AntiAim INSTANCE = new AntiAim();
    private final EnumSetting<AntiAimYaw> yaw = new EnumSetting<AntiAimYaw>("Yaw", AntiAimYaw.Jitter, "Mode for spoofing your yaw");
    private final FloatSetting rawRange = new FloatSetting("Range", 180.0F, 0.0F, 180.0F, 0.1F, "Range for yaw jitter", this::lambda$new$0, this.yaw);
    private final FloatSetting yawLock = new FloatSetting("YawLock", 0.0F, -180.0F, 180.0F, 0.1F, "Yaw to look at", this::lambda$new$1, this.yaw);
    private final FloatSetting yawAdd = new FloatSetting("YawAdd", 0.0F, -180.0F, 180.0F, 0.1F, "Amount of yaw to add to your yaw", this::lambda$new$2, this.yaw);
    private final EnumSetting<AntiAimYaw> pitch = new EnumSetting<AntiAimYaw>("Pitch", AntiAimYaw.Lock, "Mode for spoofing your pitch");
    private final FloatSetting pitchRange = new FloatSetting("Range", 90.0F, 0.0F, 90.0F, 0.1F, "Range for pitch jitter", this::lambda$new$3, this.pitch);
    private final FloatSetting pitchLock = new FloatSetting("PitchLock", 0.0F, -90.0F, 90.0F, 0.1F, "Pitch to look at", this::lambda$new$4, this.pitch);
    private final FloatSetting pitchAdd = new FloatSetting(
            "PitchAdd", 0.0F, -90.0F, 90.0F, 0.1F, "Amount of pitch to add to your pitch", this::lambda$new$5, this.pitch
    );
    private final IntSetting interval = new IntSetting("Interval", 0, 0, 20, 1, "Interval for rotation updates");
    private int field2863;
    private float field2864;
    private float field2865;
    private boolean field2866;

    public AntiAim() {
        super("AntiAim", "Spoof your rotations in various different ways", Category.Misc);
    }

    @Override
    public void onEnable() {
        this.field2863 = 0;
        if (mc.player != null) {
            this.field2864 = mc.player.getYaw();
            this.field2865 = mc.player.getPitch();
        }
    }

    @EventHandler(
            priority = -999
    )
    public void method1632(MovementEvent event) {
        if (!event.method1022()) {
            this.method1633(event);
        }
    }

    private void method1633(MovementEvent movementEvent) {
        ++this.field2863;
        if (this.yaw.getValue() == AntiAimYaw.Off && this.pitch.getValue() == AntiAimYaw.Off) {
            return;
        }
        if (this.field2863 > this.interval.getValue()) {
            this.field2863 = 0;
            float f = movementEvent.yaw;
            switch (this.yaw.getValue().ordinal()) {
                case 1: {
                    f = this.field2864 + (float)((Math.random() * 2.0 - 1.0) * (double)this.rawRange.getValue());
                    break;
                }
                case 2: {
                    f = AntiAim.mc.player.getYaw() + (float)((Math.random() * 2.0 - 1.0) * (double)this.rawRange.getValue());
                    break;
                }
                case 3: {
                    f = (float)((Math.random() * 2.0 - 1.0) * (double)this.rawRange.getValue());
                    break;
                }
                case 4: {
                    f = this.yawLock.getValue();
                    break;
                }
                case 5: {
                    f += this.yawAdd.getValue();
                    break;
                }
                case 6: {
                    Entity object = null;
                    for (Entity object2 : AntiAim.mc.world.getEntities()) {
                        if (!(object2 instanceof PlayerEntity) || object2 == AntiAim.mc.player || Friends.method2055((Entity)object2) || object != null && !(object.distanceTo((Entity)AntiAim.mc.player) > object2.distanceTo((Entity)AntiAim.mc.player))) continue;
                        object = object2;
                    }
                    if (object == null) break;
                    f = EntityUtil.method2146(object.getEyePos())[0];
                    f += this.yawAdd.getValue();
                    break;
                }
                case 7: {
                    f = this.field2864 + this.yawAdd.getValue();
                }
            }
            this.field2864 = f = MathHelper.wrapDegrees((float)f);
            float f2 = movementEvent.pitch;
            switch (this.pitch.getValue().ordinal()) {
                case 1: {
                    f2 = this.field2865 + (float)((Math.random() * 2.0 - 1.0) * (double)this.pitchRange.getValue());
                    break;
                }
                case 2: {
                    f2 = AntiAim.mc.player.getPitch() + (float)((Math.random() * 2.0 - 1.0) * (double)this.pitchRange.getValue());
                    break;
                }
                case 3: {
                    f2 = (float)((Math.random() * 2.0 - 1.0) * (double)this.pitchRange.getValue());
                    break;
                }
                case 4: {
                    f2 = this.pitchLock.getValue();
                    break;
                }
                case 5: {
                    f2 += this.pitchAdd.getValue();
                    break;
                }
                case 6: {
                    Entity entity = null;
                    for (Entity entity2 : AntiAim.mc.world.getEntities()) {
                        if (!(entity2 instanceof PlayerEntity) || entity2 == AntiAim.mc.player || Friends.method2055(entity2) || entity != null && !(entity.distanceTo((Entity)AntiAim.mc.player) > entity2.distanceTo((Entity)AntiAim.mc.player))) continue;
                        entity = entity2;
                    }
                    if (entity == null) break;
                    f2 = EntityUtil.method2146(entity.getEyePos())[1];
                    f2 += this.pitchAdd.getValue();
                    break;
                }
                case 7: {
                    float f3 = Math.abs(this.pitchAdd.getValue());
                    f2 = this.field2866 ? this.field2865 - f3 : this.field2865 + f3;
                    if (f2 > 90.0f) {
                        this.field2866 = true;
                        break;
                    }
                    if (!(f2 < -90.0f)) break;
                    this.field2866 = false;
                }
            }
            if (f2 > 90.0f) {
                f2 = 90.0f;
            } else if (f2 < -90.0f) {
                f2 = -90.0f;
            }
            this.field2865 = f2;
            movementEvent.method1074(new ActionWrapper(AntiAim::lambda$handleEvent$6, f, f2));
        } else {
            movementEvent.method1074(new ActionWrapper(AntiAim::lambda$handleEvent$7, this.field2864, this.field2865));
        }
    }

    private static void lambda$handleEvent$7() {
    }

    private static void lambda$handleEvent$6() {
    }

    private boolean lambda$new$5() {
        return this.pitch.getValue() == AntiAimYaw.Offset || this.pitch.getValue() == AntiAimYaw.Stare || this.pitch.getValue() == AntiAimYaw.Spin;
    }

    private boolean lambda$new$4() {
        return this.pitch.getValue() == AntiAimYaw.Lock;
    }

    private boolean lambda$new$3() {
        return this.pitch.getValue() == AntiAimYaw.Jitter || this.pitch.getValue() == AntiAimYaw.Random || this.pitch.getValue() == AntiAimYaw.FOVJitter;
    }

    private boolean lambda$new$2() {
        return this.yaw.getValue() == AntiAimYaw.Offset || this.yaw.getValue() == AntiAimYaw.Stare || this.yaw.getValue() == AntiAimYaw.Spin;
    }

    private boolean lambda$new$1() {
        return this.yaw.getValue() == AntiAimYaw.Lock;
    }

    private boolean lambda$new$0() {
        return this.yaw.getValue() == AntiAimYaw.Jitter || this.yaw.getValue() == AntiAimYaw.Random || this.yaw.getValue() == AntiAimYaw.FOVJitter;
    }
}
