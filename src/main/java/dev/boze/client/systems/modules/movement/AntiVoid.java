package dev.boze.client.systems.modules.movement;

import dev.boze.client.enums.AntiVoidMode;
import dev.boze.client.enums.AntiVoidType;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.*;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.movement.antivoid.ne;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import mapped.Class1202;
import mapped.Class3064;
import mapped.Class5919;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class AntiVoid extends Module {
   public static final AntiVoid INSTANCE = new AntiVoid();
   private final EnumSetting<AntiVoidMode> field550 = new EnumSetting<AntiVoidMode>(
      "Mode",
      AntiVoidMode.Ghost,
      "Anti void mode\n - Ghost: Use this on legit/pvp servers - be cautious, staff may notice you're using AntiVoid\n - Anarchy: Use this on anarchy servers (like 2b2t)\n",
      AntiVoid::lambda$new$0
   );
   private final FloatSetting field551 = new FloatSetting(
      "FallDistance",
      0.0F,
      0.0F,
      3.0F,
      0.1F,
      "Fall distance to activate at\n0 is highly recommended in order to pearl before it's too late",
      this::lambda$new$1
   );
   private final MinMaxDoubleSetting field552 = new MinMaxDoubleSetting("AimSpeed", new double[]{1.5, 2.5}, 0.1, 10.0, 0.1, "Aim speed", this::lambda$new$2);
   private final EnumSetting<AntiVoidType> field553 = new EnumSetting<AntiVoidType>(
      "Type",
      AntiVoidType.Packet,
      "Anti void type\n - Packet: Sends invalid position packets\n - PacketFloat: \"Floats\" above bedrock using packets\n - Hover: Stops all vertical movement\n - Motion: Adds upwards motion\n - Flight: Toggles flight module",
      this::lambda$new$3
   );
   private final IntSetting field554 = new IntSetting("Offset", 0, 0, 20, 1, "Activates this amount of blocks under void", this::lambda$new$4);
   private final ne field555 = new ne(this);
   private final Class5919 field556 = new Class5919();
   private final Class3064<Vector3d> field557 = new Class3064<Vector3d>(Vector3d::new);
   private final Timer field558 = new Timer();
   private final Timer field559 = new Timer();
   private final Timer field560 = new Timer();
   private BlockPos field561 = null;
   private boolean field562 = false;

   private AntiVoidMode method280() {
      return Options.INSTANCE.method1971() ? AntiVoidMode.Ghost : this.field550.getValue();
   }

   public AntiVoid() {
      super("AntiVoid", "Tries to prevent you from falling into the void", Category.Movement);
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      this.field562 = false;
   }

   @EventHandler
   public void method1693(HandleInputEvent event) {
      if (this.method280() == AntiVoidMode.Ghost
         && mc.player.fallDistance >= this.field551.getValue()
         && !this.field562
         && mc.world.isSpaceEmpty(mc.player.getBoundingBox().withMinY((double)mc.world.getBottomY()))
         && !mc.player.isCreative()) {
         ItemStack var5 = mc.player.getMainHandStack();
         if (var5.isEmpty() || var5.getItem() != Items.ENDER_PEARL) {
            var5 = mc.player.getOffHandStack();
         }

         if (var5.isEmpty() || var5.getItem() != Items.ENDER_PEARL) {
            for (int var6 = 0; var6 < 9; var6++) {
               if (mc.player.getInventory().getStack(var6).getItem() == Items.ENDER_PEARL) {
                  ((KeyBindingAccessor)mc.options.hotbarKeys[var6]).setTimesPressed(1);
                  break;
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 200
   )
   public void method1695(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive()
         && !event.method1022()
         && this.method280() == AntiVoidMode.Ghost
         && !this.field562
         && this.field560.hasElapsed(300.0)
         && !mc.player.isCreative()) {
         if (mc.player.fallDistance >= this.field551.getValue() && mc.world.isSpaceEmpty(mc.player.getBoundingBox().withMinY((double)mc.world.getBottomY()))) {
            ItemStack var5 = mc.player.getMainHandStack();
            if (var5.isEmpty() || var5.getItem() != Items.ENDER_PEARL) {
               var5 = mc.player.getOffHandStack();
            }

            if (var5.isEmpty() || var5.getItem() != Items.ENDER_PEARL) {
               return;
            }

            RotationHelper var6 = new RotationHelper(mc.player);
            if (var6 == null) {
               return;
            }

            Vec3d var7 = this.method1954();
            if (var7 == null) {
               return;
            }

            RotationHelper var8 = Class1202.method2391(mc.player.getEyePos(), mc.player.getEyePos().add(var7));
            RotationHelper var9 = var6.method603(var8, this.field552.getValue()).method1600();
            RotationHelper var10 = var9.method606(var6);
            Pair[] var11 = RotationHelper.method614(var10);
            event.deltaX = event.deltaX + (Double)var11[0].getLeft();
            event.deltaY = event.deltaY + (Double)var11[0].getRight();
            event.method1021(true);
         }
      }
   }

   @EventHandler
   public void method1883(RotationEvent event) {
      if (this.field562 && this.field559.hasElapsed(5000.0)) {
         this.field562 = false;
      }

      if (this.method280() == AntiVoidMode.Ghost
         && !event.method554(RotationMode.Vanilla)
         && !this.field562
         && this.field560.hasElapsed(300.0)
         && !mc.player.isCreative()) {
         if (mc.player.fallDistance >= this.field551.getValue() && mc.world.isSpaceEmpty(mc.player.getBoundingBox().withMinY((double)mc.world.getBottomY()))) {
            this.field555.method1782();
            ItemStack var7 = mc.player.getMainHandStack();
            if (var7.isEmpty() || var7.getItem() != Items.ENDER_PEARL) {
               var7 = mc.player.getOffHandStack();
            }

            if (var7.isEmpty() || var7.getItem() != Items.ENDER_PEARL) {
               return;
            }

            if (!this.field556.method44(mc.player, var7, 0.0, 0.0)) {
               return;
            }

            this.field555.method1783();
            if (this.field555.field3141 == null && this.field555.field3142 == null) {
               this.field558.reset();
            } else {
               if (this.field555.field3141 != null && this.field555.field3141.getSide() != Direction.UP) {
                  return;
               }

               if (this.field558.hasElapsed(Math.random() * 100.0)) {
                  KeyBinding var6 = mc.options.useKey;
                  if (((KeyBindingAccessor)var6).getTimesPressed() == 0) {
                     ((KeyBindingAccessor)var6).setTimesPressed(1);
                     event.method2142();
                     this.field562 = true;
                     this.field559.reset();
                  }
               }
            }
         } else if (mc.player.isOnGround()) {
            BlockPos var5 = BlockPos.ofFloored(mc.player.getPos()).down();
            if (!mc.world.getBlockState(var5).isAir()) {
               this.field561 = var5;
            }
         }
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket) {
         this.field562 = false;
         this.field560.reset();
      }
   }

   @EventHandler(
      priority = 200
   )
   public void method1893(PlayerMoveEvent event) {
      if (this.method280() == AntiVoidMode.Anarchy) {
         int var5 = mc.world.getBottomY() - this.field554.getValue();
         if (mc.player.getPos().y + event.vec3.y <= (double)var5) {
            if (this.field553.getValue() == AntiVoidType.Packet) {
               mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), 999.0, mc.player.getZ(), true));
               mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), 999.0, mc.player.getZ(), false));
               mc.player.networkHandler.sendPacket(new PositionAndOnGround(Math.random() * 5.8E7 - 2.9E7, 999.0, Math.random() * 5.8E7 - 2.9E7, true));
               mc.player.networkHandler.sendPacket(new PositionAndOnGround(Math.random() * 5.8E7 - 2.9E7, 999.0, Math.random() * 5.8E7 - 2.9E7, false));
            } else if (this.field553.getValue() == AntiVoidType.Motion) {
               event.vec3 = new Vec3d(event.vec3.x, 0.4, event.vec3.z);
               event.field1892 = true;
            } else if (this.field553.getValue() == AntiVoidType.Flight) {
               Flight.INSTANCE.setEnabled(true);
            } else if (this.field553.getValue() == AntiVoidType.Hover) {
               event.vec3 = new Vec3d(event.vec3.x, 0.0, event.vec3.z);
               event.field1892 = true;
            } else if (this.field553.getValue() == AntiVoidType.PacketFloat) {
               for (int var6 = 0; var6 < 20; var6++) {
                  mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
               }
            }
         }
      }
   }

   private Vec3d method1954() {
      if (this.field561 == null) {
         return null;
      } else {
         Vec3d var8 = new Vec3d((double)this.field561.getX() + 0.5, (double)(this.field561.getY() + 1), (double)this.field561.getZ() + 0.5);
         float var9 = (float)(-Math.toDegrees((double)this.method281(var8, 1.5, 0.03)));
         if (Float.isNaN(var9)) {
            return null;
         } else {
            float var10 = (float)Math.toDegrees(Math.atan2(var8.getZ() - mc.player.getZ(), var8.getX() - mc.player.getX())) - 90.0F;
            return this.method283(var9, var10);
         }
      }
   }

   private float method281(Vec3d var1, double var2, double var4) {
      double var8 = var1.getY() - (mc.player.getY() + (double)mc.player.getEyeHeight(mc.player.getPose()));
      double var10 = var1.getX() - mc.player.getX();
      double var12 = var1.getZ() - mc.player.getZ();
      double var14 = Math.sqrt(var10 * var10 + var12 * var12);
      return this.method282(var2, var4, var14, var8);
   }

   private float method282(double var1, double var3, double var5, double var7) {
      double var9 = var3 * var5 * var5;
      var7 = 2.0 * var7 * var1 * var1;
      var7 = var3 * (var9 + var7);
      var7 = Math.sqrt(var1 * var1 * var1 * var1 - var7);
      var1 = var1 * var1 - var7;
      var7 = Math.atan2(var1 * var1 + var7, var3 * var5);
      var1 = Math.atan2(var1, var3 * var5);
      return (float)Math.min(var7, var1);
   }

   private Vec3d method283(float var1, float var2) {
      float var3 = var1 * (float) (Math.PI / 180.0);
      float var4 = -var2 * (float) (Math.PI / 180.0);
      float var5 = MathHelper.cos(var4);
      float var6 = MathHelper.sin(var4);
      float var7 = MathHelper.cos(var3);
      float var8 = MathHelper.sin(var3);
      return new Vec3d((double)(var6 * var7), (double)(-var8), (double)(var5 * var7));
   }

   private boolean lambda$new$4() {
      return this.method280() == AntiVoidMode.Anarchy;
   }

   private boolean lambda$new$3() {
      return this.method280() == AntiVoidMode.Anarchy;
   }

   private boolean lambda$new$2() {
      return this.method280() == AntiVoidMode.Ghost;
   }

   private boolean lambda$new$1() {
      return this.method280() == AntiVoidMode.Ghost;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
