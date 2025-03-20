package dev.boze.client.systems.modules.movement;

import baritone.api.BaritoneAPI;
import com.google.common.collect.Streams;
import dev.boze.client.enums.JesusMode;
import dev.boze.client.enums.JesusNoSetback;
import dev.boze.client.events.CanWalkOnFluidEvent;
import dev.boze.client.events.CollisionEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.mixin.LivingEntityAccessor;
import dev.boze.client.mixininterfaces.IVec3d;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.Full;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Jesus extends Module {
   public static final Jesus INSTANCE = new Jesus();
   private BooleanSetting field3284 = new BooleanSetting("Water", true, "Walk on water");
   private EnumSetting<JesusMode> field3285 = new EnumSetting<JesusMode>("Mode", JesusMode.Normal, "Water jesus mode", this.field3284);
   private BooleanSetting field3286 = new BooleanSetting("Extinguish", true, "Extinguish fire when walking on water", this.field3284);
   private BooleanSetting field3287 = new BooleanSetting("Lava", true, "Walk on lava");
   private EnumSetting<JesusMode> field3288 = new EnumSetting<JesusMode>("Mode", JesusMode.Normal, "Lava jesus mode", this.field3287);
   private EnumSetting<JesusNoSetback> field3289 = new EnumSetting<JesusNoSetback>(
      "NoSetback", JesusNoSetback.Water, "Don't jesus when falling into liquid at high speeds"
   );
   private IntSetting field3290 = new IntSetting("Height", 10, 1, 50, 1, "Min height for NoSetback", this.field3289);
   public BooleanSetting field3291 = new BooleanSetting("Snow", true, "Walk on powdered snow");
   private final Mutable field3292 = new Mutable();
   private int field3293 = 10;
   private int field3294 = 0;
   private boolean field3295;
   private boolean field3296;

   public Jesus() {
      super("Jesus", "Allows you to walk on water and more", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3295 = (Boolean)BaritoneAPI.getSettings().assumeWalkOnWater.value;
      this.field3296 = (Boolean)BaritoneAPI.getSettings().assumeWalkOnLava.value;
      BaritoneAPI.getSettings().assumeWalkOnWater.value = this.field3285.getValue() == JesusMode.Normal;
      BaritoneAPI.getSettings().assumeWalkOnLava.value = this.field3288.getValue() == JesusMode.Normal;
   }

   @Override
   public void onDisable() {
      BaritoneAPI.getSettings().assumeWalkOnWater.value = this.field3295;
      BaritoneAPI.getSettings().assumeWalkOnLava.value = this.field3296;
   }

   @EventHandler
   private void method1839(PostTickEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (this.field3285.getValue() == JesusMode.Strict && mc.player.isTouchingWater()
            || this.field3288.getValue() == JesusMode.Strict && mc.player.isInLava()) {
            double var5;
            if (mc.player.isInLava()) {
               var5 = mc.player.getFluidHeight(FluidTags.LAVA);
            } else {
               var5 = mc.player.getFluidHeight(FluidTags.WATER);
            }

            double var7 = mc.player.getSwimHeight();
            if (mc.player.isTouchingWater() && var5 > var7) {
               ((LivingEntityAccessor)mc.player).swimUpwards(FluidTags.WATER);
            } else if (mc.player.isOnGround() && var5 <= var7 && ((LivingEntityAccessor)mc.player).getJumpCooldown() == 0) {
               mc.player.jump();
               ((LivingEntityAccessor)mc.player).setJumpCooldown(10);
            } else {
               ((LivingEntityAccessor)mc.player).swimUpwards(FluidTags.LAVA);
            }
         }

         if (!mc.player.isTouchingWater() || this.method1844()) {
            if (!mc.player.isInLava() || this.method1845()) {
               if (!mc.player.isTouchingWater() && !mc.player.isInLava()) {
                  if (this.field3293 == 0) {
                     ((IVec3d)mc.player.getVelocity()).boze$setY(0.3);
                  } else if (this.field3293 == 1) {
                     ((IVec3d)mc.player.getVelocity()).boze$setY(0.0);
                  }

                  this.field3293++;
               } else {
                  ((IVec3d)mc.player.getVelocity()).boze$setY(0.11);
                  this.field3293 = 0;
               }
            }
         }
      }
   }

   @EventHandler
   private void method1840(CanWalkOnFluidEvent var1) {
      if ((var1.fluidState.getFluid() == Fluids.WATER || var1.fluidState.getFluid() == Fluids.FLOWING_WATER) && this.method1844()) {
         var1.method1021(true);
      } else if ((var1.fluidState.getFluid() == Fluids.LAVA || var1.fluidState.getFluid() == Fluids.FLOWING_LAVA) && this.method1845()) {
         var1.method1021(true);
      }
   }

   @EventHandler
   private void method1841(CollisionEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (!var1.field1910.getFluidState().isEmpty()) {
            if (var1.field1910.getBlock() == Blocks.WATER | var1.field1910.getFluidState().getFluid() == Fluids.WATER
               && !mc.player.isTouchingWater()
               && this.method1844()) {
               var1.voxelShape = VoxelShapes.fullCube();
            } else if (var1.field1910.getBlock() == Blocks.LAVA && !mc.player.isInLava() && this.method1845()) {
               var1.voxelShape = VoxelShapes.fullCube();
            }
         }
      }
   }

   @EventHandler
   private void method1842(PrePacketSendEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (var1.packet instanceof PlayerMoveC2SPacket var5) {
            if (!mc.player.isTouchingWater() || this.method1844()) {
               if (!mc.player.isInLava() || this.method1845()) {
                  if (var5 instanceof PositionAndOnGround || var5 instanceof Full) {
                     if (!mc.player.isTouchingWater() && !mc.player.isInLava() && !(mc.player.fallDistance > 3.0F) && this.method1843()) {
                        if (mc.player.input.movementForward == 0.0F && mc.player.input.movementSideways == 0.0F) {
                           var1.method1020();
                        } else if (this.field3294++ >= 4) {
                           this.field3294 = 0;
                           var1.method1020();
                           double var13 = var5.getX(0.0);
                           double var8 = var5.getY(0.0) + 0.05;
                           double var10 = var5.getZ(0.0);
                           Object var12;
                           if (var5 instanceof PositionAndOnGround) {
                              var12 = new PositionAndOnGround(var13, var8, var10, true);
                           } else {
                              var12 = new Full(var13, var8, var10, var5.getYaw(0.0F), var5.getPitch(0.0F), true);
                           }

                           mc.player.networkHandler.sendPacket((Packet)var12);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private boolean method1843() {
      boolean var4 = false;
      boolean var5 = false;

      for (Box var8 : (List)Streams.stream(mc.world.getBlockCollisions(mc.player, mc.player.getBoundingBox().offset(0.0, -0.5, 0.0)))
         .map(VoxelShape::method_1107)
         .collect(Collectors.toCollection(ArrayList::new))) {
         this.field3292.set(MathHelper.lerp(0.5, var8.minX, var8.maxX), MathHelper.lerp(0.5, var8.minY, var8.maxY), MathHelper.lerp(0.5, var8.minZ, var8.maxZ));
         BlockState var9 = mc.world.getBlockState(this.field3292);
         if (var9.getBlock() == Blocks.WATER | var9.getFluidState().getFluid() == Fluids.WATER || var9.getBlock() == Blocks.LAVA) {
            var4 = true;
         } else if (!var9.isAir()) {
            var5 = true;
         }
      }

      return var4 && !var5;
   }

   private boolean method1844() {
      if (this.field3286.getValue() && mc.player.isOnFire()) {
         return false;
      } else if (mc.options.sneakKey.isPressed()) {
         return false;
      } else {
         return (this.field3289.getValue() == JesusNoSetback.Water || this.field3289.getValue() == JesusNoSetback.Both)
               && mc.player.fallDistance > (float)this.field3290.method434().intValue()
            ? false
            : this.field3285.getValue() == JesusMode.Normal;
      }
   }

   private boolean method1845() {
      if (mc.options.sneakKey.isPressed()) {
         return false;
      } else {
         return (this.field3289.getValue() == JesusNoSetback.Lava || this.field3289.getValue() == JesusNoSetback.Both)
               && mc.player.fallDistance > (float)this.field3290.method434().intValue()
            ? false
            : this.field3288.getValue() == JesusMode.Normal;
      }
   }
}
