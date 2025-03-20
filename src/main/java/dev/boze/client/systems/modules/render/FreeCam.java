package dev.boze.client.systems.modules.render;

import dev.boze.client.enums.FreeCamInteract;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.NoSlowInvMove;
import dev.boze.client.events.*;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.movement.NoSlow;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.option.Perspective;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class FreeCam extends Module {
   public static final FreeCam INSTANCE = new FreeCam();
   private final BooleanSetting field3529 = new BooleanSetting("Rotate", true, "Rotate");
   private final MinMaxSetting field3530 = new MinMaxSetting("Speed", 1.0, 0.1, 10.0, 0.1, "Horizontal camera speed");
   private final MinMaxSetting field3531 = new MinMaxSetting("VSpeed", 1.0, 0.1, 10.0, 0.1, "Vertical camera speed");
   private final BooleanSetting field3532 = new BooleanSetting("ScrollSpeed", false, "Change speed by scrolling");
   private final MinMaxSetting field3533 = new MinMaxSetting("Sensitivity", 0.1, 0.01, 1.0, 0.01, "Scroll sensitivity", this.field3532);
   private final BooleanSetting field3534 = new BooleanSetting("Vertical", true, "Scroll up and down to change vertical speed", this.field3532);
   public final EnumSetting<FreeCamInteract> field3535 = new EnumSetting<FreeCamInteract>("Interact", FreeCamInteract.Dynamic, "Interact");
   public final BindSetting field3536 = new BindSetting("Control", Bind.fromKey(342), "Control the player while in freecam");
   private final BooleanSetting field3537 = new BooleanSetting("LockOn", false, "Always look at the player");
   private final BooleanSetting field3538 = new BooleanSetting("Follow", false, "Follow the player");
   private final BooleanSetting field3539 = new BooleanSetting("SoftReload", true, "Soft reload the world");
   public double field3540;
   public double field3541;
   public double field3542;
   private float field3543;
   private float field3544;
   private double field3545;
   private double field3546;
   private double field3547;
   private float field3548;
   private float field3549;
   private Perspective field3550;
   private boolean field3551;
   private boolean field3552;
   private boolean field3553;
   private boolean field3554;
   private boolean field3555;
   private boolean field3556;
   private boolean field3557;

   public FreeCam() {
      super("Freecam", "Out of body experience", Category.Render);
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field3543 = this.field3548 = mc.player.getYaw();
         this.field3544 = this.field3549 = mc.player.getPitch();
         this.field3550 = mc.options.getPerspective();
         this.field3551 = (Boolean)mc.options.getBobView().getValue();
         this.field3540 = this.field3545 = mc.player.getX();
         this.field3541 = this.field3546 = mc.player.getEyeY();
         this.field3542 = this.field3547 = mc.player.getZ();
         this.field3548 = this.field3543;
         this.field3549 = this.field3544;
         this.field3552 = false;
         this.field3553 = false;
         this.field3554 = false;
         this.field3555 = false;
         this.field3556 = false;
         this.field3557 = false;
         mc.options.forwardKey.setPressed(false);
         mc.options.backKey.setPressed(false);
         mc.options.rightKey.setPressed(false);
         mc.options.leftKey.setPressed(false);
         mc.options.jumpKey.setPressed(false);
         mc.options.sneakKey.setPressed(false);
         if (this.field3539.getValue()) {
            mc.worldRenderer.reload();
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.field3550 != null) {
         mc.options.setPerspective(this.field3550);
         mc.options.getBobView().setValue(this.field3551);
         this.field3550 = null;
      }

      if (MinecraftUtils.isClientActive()) {
         if (this.field3539.getValue() && MinecraftUtils.isClientReadyForSinglePlayer()) {
            mc.worldRenderer.reload();
         }

         mc.options.forwardKey.setPressed(false);
         mc.options.backKey.setPressed(false);
         mc.options.rightKey.setPressed(false);
         mc.options.leftKey.setPressed(false);
         mc.options.jumpKey.setPressed(false);
         mc.options.sneakKey.setPressed(false);
      }
   }

   @EventHandler
   public void method1938(PreTickEvent event) {
      if (mc.currentScreen instanceof DownloadingTerrainScreen) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method1939(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket) {
         this.setEnabled(false);
      }
   }

   @EventHandler(
      priority = 3
   )
   public void method1940(MovementEvent event) {
      if (this.field3529.getValue() && !event.method1022() && this.method1941() && mc.crosshairTarget != null && mc.crosshairTarget.getPos() != null) {
         float[] var5 = EntityUtil.method2146(mc.crosshairTarget.getPos());
         event.method1074(new ActionWrapper(var5[0], var5[1]));
      }
   }

   public boolean method1941() {
      return this.field3535.getValue() == FreeCamInteract.Camera
         || INSTANCE.field3535.getValue() == FreeCamInteract.Dynamic && !INSTANCE.field3536.getValue().isPressed();
   }

   @EventHandler
   private void method1942(PostPlayerTickEvent var1) {
      if (!this.field3550.isFirstPerson()) {
         mc.options.setPerspective(Perspective.FIRST_PERSON);
      }

      mc.options.getBobView().setValue(false);
      if (mc.cameraEntity.isInsideWall()) {
         mc.getCameraEntity().noClip = true;
      }

      if (this.method1941()) {
         Vec3d var5 = Vec3d.fromPolar(0.0F, this.method1951(1.0F));
         Vec3d var6 = Vec3d.fromPolar(0.0F, this.method1951(1.0F) + 90.0F);
         double var7 = 0.0;
         double var9 = 0.0;
         double var11 = 0.0;
         double var13 = 1.0;
         if (mc.options.sprintKey.isPressed()) {
            var13 = 1.2;
         }

         boolean var15 = false;
         if (this.field3552) {
            var7 += var5.x * var13 * this.field3530.getValue();
            var11 += var5.z * var13 * this.field3530.getValue();
            var15 = true;
         }

         if (this.field3553) {
            var7 -= var5.x * var13 * this.field3530.getValue();
            var11 -= var5.z * var13 * this.field3530.getValue();
            var15 = true;
         }

         boolean var16 = false;
         if (this.field3554) {
            var7 += var6.x * var13 * this.field3530.getValue();
            var11 += var6.z * var13 * this.field3530.getValue();
            var16 = true;
         }

         if (this.field3555) {
            var7 -= var6.x * var13 * this.field3530.getValue();
            var11 -= var6.z * var13 * this.field3530.getValue();
            var16 = true;
         }

         if (var15 && var16) {
            double var17 = 1.0 / Math.sqrt(2.0);
            var7 *= var17;
            var11 *= var17;
         }

         if (this.field3556) {
            var9 += var13 * this.field3531.getValue();
         }

         if (this.field3557) {
            var9 -= var13 * this.field3531.getValue();
         }

         this.field3545 = this.field3540;
         this.field3546 = this.field3541;
         this.field3547 = this.field3542;
         this.field3540 += var7;
         this.field3541 += var9;
         this.field3542 += var11;
      } else {
         this.field3545 = this.field3540;
         this.field3546 = this.field3541;
         this.field3547 = this.field3542;
         this.field3548 = this.field3543;
         this.field3549 = this.field3544;
      }

      if (this.field3538.getValue()) {
         this.field3540 = this.field3540 + (mc.player.getX() - mc.player.prevX);
         this.field3541 = this.field3541 + (mc.player.getY() - mc.player.prevY);
         this.field3542 = this.field3542 + (mc.player.getZ() - mc.player.prevZ);
      }

      if (this.field3537.getValue()) {
         this.field3548 = this.field3543;
         this.field3549 = this.field3544;
         float[] var19 = EntityUtil.method2147(
            new Vec3d(this.field3540, this.field3541, this.field3542), mc.player.getPos().add(0.0, (double)mc.player.getHeight() * 0.5, 0.0)
         );
         this.field3543 = var19[0];
         this.field3544 = var19[1];
      }
   }

   @EventHandler
   private void method1943(MouseScrollEvent var1) {
      if (this.field3532.getValue() && mc.currentScreen == null) {
         this.field3530.setValue(this.field3530.getValue() + var1.vertical * this.field3533.getValue());
         if (this.field3534.getValue()) {
            this.field3531.setValue(this.field3531.getValue() + var1.vertical * this.field3533.getValue());
         }

         var1.method1020();
      }
   }

   @EventHandler
   public void method1944(KeyEvent event) {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else if (!this.method1941()) {
         this.field3552 = false;
         this.field3553 = false;
         this.field3554 = false;
         this.field3555 = false;
         this.field3556 = false;
         this.field3557 = false;
      } else {
         mc.options.forwardKey.setPressed(false);
         mc.options.backKey.setPressed(false);
         mc.options.rightKey.setPressed(false);
         mc.options.leftKey.setPressed(false);
         mc.options.jumpKey.setPressed(false);
         mc.options.sneakKey.setPressed(false);
         if (mc.currentScreen == null
            || event.action == KeyAction.Release
            || NoSlow.INSTANCE.isEnabled() && NoSlow.INSTANCE.field3312.getValue() != NoSlowInvMove.Off) {
            if (mc.options.forwardKey.matchesKey(event.key, 0) || mc.options.forwardKey.matchesMouse(event.key)) {
               this.field3552 = event.action != KeyAction.Release;
               event.method1020();
            } else if (mc.options.backKey.matchesKey(event.key, 0) || mc.options.backKey.matchesMouse(event.key)) {
               this.field3553 = event.action != KeyAction.Release;
               event.method1020();
            } else if (mc.options.rightKey.matchesKey(event.key, 0) || mc.options.rightKey.matchesMouse(event.key)) {
               this.field3554 = event.action != KeyAction.Release;
               event.method1020();
            } else if (mc.options.leftKey.matchesKey(event.key, 0) || mc.options.leftKey.matchesMouse(event.key)) {
               this.field3555 = event.action != KeyAction.Release;
               event.method1020();
            } else if (mc.options.jumpKey.matchesKey(event.key, 0) || mc.options.jumpKey.matchesMouse(event.key)) {
               this.field3556 = event.action != KeyAction.Release;
               event.method1020();
            } else if (mc.options.sneakKey.matchesKey(event.key, 0) || mc.options.sneakKey.matchesMouse(event.key)) {
               this.field3557 = event.action != KeyAction.Release;
               event.method1020();
            }
         }
      }
   }

   @EventHandler
   private void method1945(OcclusionEvent var1) {
      var1.method1020();
   }

   public void method1946(double dX, double dY) {
      if (!this.field3537.getValue()) {
         this.field3548 = this.field3543;
         this.field3549 = this.field3544;
         this.field3543 = (float)((double)this.field3543 + dX);
         this.field3544 = (float)((double)this.field3544 + dY);
         this.field3544 = MathHelper.clamp(this.field3544, -90.0F, 90.0F);
      }
   }

   public double method1947(float tickDelta) {
      return MathHelper.lerp((double)tickDelta, this.field3545, this.field3540);
   }

   public double method1948(float tickDelta) {
      return MathHelper.lerp((double)tickDelta, this.field3546, this.field3541);
   }

   public double method1949(float tickDelta) {
      return MathHelper.lerp((double)tickDelta, this.field3547, this.field3542);
   }

   public float method1950(float tickDelta) {
      return tickDelta == 1.0F ? this.field3544 : MathHelper.lerp(tickDelta, this.field3549, this.field3544);
   }

   public float method1951(float tickDelta) {
      return tickDelta == 1.0F ? this.field3543 : MathHelper.lerp(tickDelta, this.field3548, this.field3543);
   }

   public final Vec3d method1952(float tickDelta) {
      double var2 = MathHelper.lerp((double)tickDelta, this.field3545, this.field3540);
      double var4 = MathHelper.lerp((double)tickDelta, this.field3546, this.field3541);
      double var6 = MathHelper.lerp((double)tickDelta, this.field3547, this.field3542);
      return new Vec3d(var2, var4, var6);
   }

   public final Box method1953() {
      return new Box(this.field3540 - 0.3, this.field3541 - 1.6, this.field3542 - 0.3, this.field3540 + 0.3, this.field3541 + 0.2, this.field3542 + 0.3);
   }

   public final Vec3d method1954() {
      float var1 = this.field3544 * (float) (Math.PI / 180.0);
      float var2 = -this.field3543 * (float) (Math.PI / 180.0);
      float var3 = MathHelper.cos(var2);
      float var4 = MathHelper.sin(var2);
      float var5 = MathHelper.cos(var1);
      float var6 = MathHelper.sin(var1);
      return new Vec3d((double)(var4 * var5), (double)(-var6), (double)(var3 * var5));
   }
}
