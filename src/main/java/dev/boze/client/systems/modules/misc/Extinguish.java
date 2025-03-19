package dev.boze.client.systems.modules.misc;

import dev.boze.client.Boze;
import dev.boze.client.ac.Ghost;
import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.BlockPlaceMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.render.PlacementRenderer;
import dev.boze.client.utils.*;
import mapped.Class1202;
import mapped.Class2784;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Extinguish extends Module {
   public static final Extinguish INSTANCE = new Extinguish();
   private final PlacementRenderer field2919 = new PlacementRenderer();
   private final InteractionHandler field2920 = new InteractionHandler();
   private final BooleanSetting field2921 = new BooleanSetting("MultiTask", false, "Extinguish while already using items");
   private final SwapHandler field2922 = new SwapHandler(this, 250);
   private HitResult[] field2923 = null;
   private BlockHitResult field2924 = null;
   private RotationHelper field2925;
   private final dev.boze.client.utils.Timer field2926 = new dev.boze.client.utils.Timer();
   private final dev.boze.client.utils.Timer field2927 = new dev.boze.client.utils.Timer();
   private Entity field2928 = null;

   private Extinguish() {
      super("Extinguish", "Automatically places water at feet when on fire", Category.Misc);
      Boze.EVENT_BUS.subscribe(this.field2919);
      this.field435 = true;
      this.addSettings(this.field2919.field224);
   }

   private boolean method1691() {
      if (Options.method477(this.field2921.method419())) {
         return false;
      } else {
         int var4 = InventoryHelper.method174(BlastResistanceCalculator.field3905, this.field2920.method149());
         if (var4 == -1) {
            return false;
         } else {
            if (mc.player.isOnFire()) {
               BlockPos var5 = mc.player.getBlockPos();
               HitResult var6 = this.field2920.method150(null, var5);
               if (var6 != null) {
                  this.field2923 = new HitResult[]{var6};
               } else {
                  this.field2923 = null;
               }
            } else {
               this.field2923 = null;
            }

            return this.field2923 != null;
         }
      }
   }

   private boolean method1692() {
      Ghost.field1313.method569(this.field2920.field228, this.field2920.field230, this.field2920.field231);
      if (!this.method1691()) {
         this.field2925 = null;
         return false;
      } else if (this.field2923 != null && this.field2923.length != 0 && this.field2923[0] instanceof BlockHitResult) {
         this.field2924 = (BlockHitResult)this.field2923[0];
         RotationHelper var4 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
         RotationHelper var5 = Class1202.method2391(mc.player.getEyePos(), this.field2924.getPos());
         RotationHelper var6 = var4.method603(var5, this.field2920.field229.method1287());
         this.field2925 = new RotationHelper(var4.method1384(), var6.method1385());
         return true;
      } else {
         this.field2925 = null;
         return false;
      }
   }

   @Override
   public String method1322() {
      return this.field2928 != null ? this.field2928.getName().getString() : super.method1322();
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field2923 = null;
         this.field2920.field230.method1296();
         this.field2928 = null;
      }
   }

   @EventHandler
   public void method1693(HandleInputEvent event) {
      if (!this.field2920.method2114()) {
         if (this.field2925 != null) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
            if (this.field2926.hasElapsed(this.field2920.field232.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
               ((KeyBindingAccessor)mc.options.hotbarKeys[var5]).setTimesPressed(1);
               this.field2926.reset();
               this.field2920.field232.method1296();
            }
         } else {
            this.field2926.reset();
         }
      }
   }

   @EventHandler(
      priority = 78
   )
   public void method1694(eJ event) {
      if (!this.field2920.method2114() && this.field2920.field227.method461() != BlockPlaceMode.Mouse && !event.method1101()) {
         if (this.method1692()) {
            event.method1099(this.field2925.method600(this::lambda$onGhostRotate$0));
         }
      }
   }

   @EventHandler(
      priority = 78
   )
   public void method1695(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (!this.field2920.method2114() && this.field2920.field227.method461() != BlockPlaceMode.Normal) {
               if (this.method1692()) {
                  RotationHelper var5 = new RotationHelper(mc.player);
                  RotationHelper var6 = this.field2925.method1600();
                  RotationHelper var7 = var6.method606(var5);
                  Pair[] var8 = RotationHelper.method614(var7);
                  Pair var9 = var8[0];

                  for (Pair var13 : var8) {
                     BlockHitResult var14 = RaycastUtil.method574(Reach.method1614(), RotationHelper.method613(var5, var13));
                     if (var14.getType() != Type.MISS && var14.getBlockPos() == this.field2924.getBlockPos() && var14.getSide() == this.field2924.getSide()) {
                        var9 = var13;
                     }
                  }

                  event.deltaY = event.deltaY + (Double)var9.getRight();
                  event.method1021(true);
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 78
   )
   public void method1696(ACRotationEvent event) {
      if (!this.field2920.method2115()) {
         if (!event.method1018(this.field2920.method147().interactMode, this.field2920.method2116())) {
            if (this.method1691()) {
               if (this.field2920.method2116()) {
                  HitResult var5 = this.field2923[0];
                  Vec3d var6 = var5.getPos();
                  event.method1019(var6);
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 76
   )
   public void method1697(RotationEvent event) {
      if (!Options.method477(this.field2921.method419()) && !event.method554(this.field2920.method147().type)) {
         if (this.field2923 != null) {
            if (!this.field2920.method2115()) {
               byte var14 = 0;

               for (int var15 = 0; var15 < this.field2923.length; var15++) {
                  HitResult var16 = this.field2923[var15];
                  if (var16.getType() == Type.BLOCK) {
                     int var8 = InventoryHelper.method174(BlastResistanceCalculator.field3905, this.field2920.method149());
                     if (var8 == -1) {
                        this.field2923 = null;
                        break;
                     }

                     BlockHitResult var9 = (BlockHitResult)var16;
                     if (!this.field2922.method723(this.field2920.method149(), var8)) {
                        this.field2923 = null;
                        break;
                     }

                     AttackMode var10 = var14 > 0 ? AttackMode.Packet : this.field2920.field234.method461();
                     BlockPos var12 = var9.getBlockPos().offset(var9.getSide());
                     if (var15 > 0 && this.field2920.method2116()) {
                        if (this.field2920.method2117()) {
                           this.field2922.method1416();
                           this.field2923 = null;
                           return;
                        }

                        float[] var13 = EntityUtil.method2146(var16.getPos());
                        ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var13[0], var13[1]);
                     }

                     if (var10 == AttackMode.Vanilla) {
                        this.field2922.method2142();
                     }

                     event.method557(this, var10, var9, var8 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                     this.field2919.method146(var9);
                     if (var10 == AttackMode.Vanilla) {
                        Class2784.method1801(var12);
                     }

                     if (var14 == 2) {
                        EntityTracker.field3914.remove(var12);
                        if (var14 == 2) {
                           var14 = 1;
                        }
                     }
                  } else if (var16.getType() == Type.ENTITY) {
                     Entity var17 = ((EntityHitResult)var16).getEntity();
                     mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var17, mc.player.isSneaking()));
                     mc.player.swingHand(Hand.MAIN_HAND);
                     IEndCrystalEntity var18 = (IEndCrystalEntity)var17;
                     var18.boze$setLastAttackTime(System.currentTimeMillis());
                     var14 = 2;
                  }
               }

               this.field2922.method1416();
               this.field2923 = null;
            } else if (this.field2924 != null && this.field2925 != null) {
               int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3905);
               if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                  RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                  BlockHitResult var7 = RaycastUtil.method574(Reach.method1614(), var6);
                  if (Class2784.method5444(this.field2924, var7)) {
                     if (this.field2927.hasElapsed(this.field2920.field233.method1295() * 50.0)) {
                        ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                        this.field2927.reset();
                        this.field2920.field233.method1296();
                        this.field2919.method146(this.field2924);
                     }
                  } else {
                     this.field2927.reset();
                  }

                  this.field2923 = null;
                  this.field2924 = null;
               } else {
                  this.field2923 = null;
                  this.field2924 = null;
                  this.field2927.reset();
               }
            }
         }
      }
   }

   private Boolean lambda$onGhostRotate$0(RotationHelper var1) {
      BlockHitResult var5 = RaycastUtil.method574(Reach.method1614(), var1);
      if (var5.getType() == Type.MISS) {
         return false;
      } else {
         return var5.getBlockPos() != this.field2924.getBlockPos() ? false : var5.getSide() == this.field2924.getSide();
      }
   }
}
