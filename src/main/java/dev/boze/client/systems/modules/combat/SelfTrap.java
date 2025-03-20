package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.ac.Ghost;
import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.modules.movement.Step;
import dev.boze.client.systems.render.PlacementRenderer;
import dev.boze.client.utils.*;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class1202;
import mapped.Class2784;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class SelfTrap extends Module {
   public static SelfTrap INSTANCE = new SelfTrap();
   private final PlacementRenderer field542 = new PlacementRenderer();
   private final PlaceHandler field543 = new PlaceHandler();
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "SelfTrap while already using items");
   private final EnumSetting<WorldReactMode> react = new EnumSetting<WorldReactMode>(
      "React",
      WorldReactMode.Tick,
      "When to react to changes in the world\n - Tick: React to everything during game ticks, most consistent\n - Packet: Reacts to everything instantly, as soon as it's received from the server\n"
   );
   private final BooleanSetting autoCenter = new BooleanSetting("AutoCenter", true, "Automatically center on block", this::lambda$new$0);
   private final BooleanSetting coverHead = new BooleanSetting("CoverHead", true, "Cover head");
   private final SettingCategory autoDisable = new SettingCategory("AutoDisable", "Auto disable settings");
   private final BooleanSetting onJump = new BooleanSetting("OnJump", false, "Disable when you jump", this.autoDisable);
   private final BooleanSetting onStep = new BooleanSetting("OnStep", false, "Disable when you step", this.autoDisable);
   private final BooleanSetting onTP = new BooleanSetting("OnTP", false, "Disable when you teleport/chorus", this.autoDisable);
   private final BooleanSetting whenDone = new BooleanSetting("WhenDone", false, "Disable when done selfTraping", this.autoDisable);
   private final SwapHandler field544 = new SwapHandler(this, 149);
   private HitResult[] field545 = null;
   private BlockHitResult field546 = null;
   private RotationHelper field547;
   private final Timer field548 = new Timer();
   private final Timer field549 = new Timer();

   private SelfTrap() {
      super(
         "SelfTrap",
         "Traps yourself with obsidian, which protects you from explosions\nIf no Obsidian, will use Ender Chests or other blast proof blocks\n",
         Category.Combat
      );
      Boze.EVENT_BUS.subscribe(this.field542);
      this.field435 = true;
      this.addSettings(this.field542.field224);
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field545 = null;
         this.field543.field230.method1296();
         if (this.autoCenter.method419() && this.field543.method147() == Anticheat.NCP) {
            Class5924.method2142();
         }
      }
   }

   @EventHandler
   public void method1877(PlayerPositionEvent event) {
      if (this.onStep.method419() && Step.INSTANCE.isEnabled()) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (!(event.packet instanceof PlayerPositionLookS2CPacket) || !this.onTP.method419()) {
         if (event.packet instanceof BlockUpdateS2CPacket var5 && this.react.method461() == WorldReactMode.Packet && this.field543.method2114()) {
            try {
               if (var5.getState().isAir()) {
                  int var10 = InventoryHelper.method174(
                     BlastResistanceCalculator.method2130(this.field543.field249.method461(), this.field543.field250), this.field543.method149()
                  );
                  if (var10 == -1) {
                     return;
                  }

                  Box var7 = this.field543.method1953();
                  BlockPos[] var8 = TrapUtil.method586(var7, this.coverHead.method419() ? TrapMode.Top : TrapMode.Tall);
                  this.field543.method152(var5, this.field544, var10, var8);
               }
            } catch (Exception var9) {
            }
         }
      } else if (MinecraftUtils.isClientActive()
         && mc.player
               .getPos()
               .distanceTo(
                  new Vec3d(
                     ((PlayerPositionLookS2CPacket)event.packet).getX(),
                     ((PlayerPositionLookS2CPacket)event.packet).getY(),
                     ((PlayerPositionLookS2CPacket)event.packet).getZ()
                  )
               )
            > 1.0) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method1693(HandleInputEvent event) {
      if (!this.field543.method2114()) {
         if (this.field547 != null) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field543.field249.method461(), this.field543.field250));
            if (this.field548.hasElapsed(this.field543.field232.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
               ((KeyBindingAccessor)mc.options.hotbarKeys[var5]).setTimesPressed(1);
               this.field548.reset();
               this.field543.field232.method1296();
            }
         } else {
            this.field548.reset();
         }
      }
   }

   private boolean method1971() {
      if (this.onJump.method419() && mc.player.getVelocity().y > 0.0 && !mc.player.isOnGround()) {
         this.setEnabled(false);
         return false;
      } else if (Options.method477(this.multiTask.method419())) {
         return false;
      } else {
         int var4 = InventoryHelper.method174(
            BlastResistanceCalculator.method2130(this.field543.field249.method461(), this.field543.field250), this.field543.method149()
         );
         if (var4 == -1) {
            return false;
         } else {
            Box var5 = this.field543.method1953();
            this.field545 = TrapUtil.method584(this.field543, var5, this.coverHead.method419() ? TrapMode.Top : TrapMode.Tall);
            if (this.field545.length == 0) {
               if (this.whenDone.method419()) {
                  this.setEnabled(false);
               }

               return false;
            } else {
               return true;
            }
         }
      }
   }

   private boolean method1972() {
      Ghost.field1313.method569(this.field543.field228, this.field543.field230, this.field543.field231);
      if (!this.method1971()) {
         this.field547 = null;
         return false;
      } else if (this.field545 != null && this.field545.length != 0 && this.field545[0] instanceof BlockHitResult) {
         this.field546 = (BlockHitResult)this.field545[0];
         RotationHelper var4 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
         RotationHelper var5 = Class1202.method2391(mc.player.getEyePos(), this.field546.getPos());
         this.field547 = var4.method603(var5, this.field543.field229.method1287());
         return true;
      } else {
         this.field547 = null;
         return false;
      }
   }

   @EventHandler(
      priority = 149
   )
   public void method1694(eJ event) {
      if (!this.field543.method2114() && this.field543.field227.method461() != BlockPlaceMode.Mouse && !event.method1101()) {
         if (this.method1972()) {
            event.method1099(this.field547.method600(this::lambda$onGhostRotate$1));
         }
      }
   }

   @EventHandler(
      priority = 149
   )
   public void method1695(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (!this.field543.method2114() && this.field543.field227.method461() != BlockPlaceMode.Normal) {
               if (this.method1972()) {
                  RotationHelper var5 = new RotationHelper(mc.player);
                  RotationHelper var6 = this.field547.method1600();
                  RotationHelper var7 = var6.method606(var5);
                  Pair[] var8 = RotationHelper.method614(var7);
                  Pair var9 = var8[0];

                  for (Pair var13 : var8) {
                     BlockHitResult var14 = RaycastUtil.method575(Reach.method1614(), RotationHelper.method613(var5, var13), true);
                     if (var14.getType() != Type.MISS && var14.getBlockPos() == this.field546.getBlockPos() && var14.getSide() == this.field546.getSide()) {
                        var9 = var13;
                     }
                  }

                  event.deltaX = event.deltaX + (Double)var9.getLeft();
                  event.deltaY = event.deltaY + (Double)var9.getRight();
                  event.method1021(true);
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 149
   )
   public void method1885(ACRotationEvent event) {
      if (!this.field543.method2115()) {
         if (!event.method1018(this.field543.method147().interactMode, this.field543.method2116())) {
            if (this.method1971()) {
               if (this.field543.method2116()) {
                  HitResult var5 = this.field545[0];
                  Vec3d var6 = var5.getPos();
                  event.method1019(var6);
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 149
   )
   public void method1883(RotationEvent event) {
      if (!Options.method477(this.multiTask.method419()) && !event.method555(this.field543.method147().type, this.field543.method2116())) {
         if (this.field545 != null) {
            if (!this.field543.method2115()) {
               byte var15 = 0;
               HashMap var16 = null;
               if (this.field543.field247.method419()) {
                  var16 = BlockBreakingTracker.field1511.method666(this.field543.field248.method419());
               }

               for (int var17 = 0; var17 < this.field545.length; var17++) {
                  HitResult var8 = this.field545[var17];
                  if (var8.getType() == Type.BLOCK) {
                     int var9 = InventoryHelper.method174(
                        BlastResistanceCalculator.method2130(this.field543.field249.method461(), this.field543.field250), this.field543.method149()
                     );
                     if (var9 == -1) {
                        this.field545 = null;
                        break;
                     }

                     BlockHitResult var10 = (BlockHitResult)var8;
                     BlockPos var11 = var10.getBlockPos().offset(var10.getSide());
                     if (!this.field543.method153(var10.getBlockPos(), var11, mc.player)) {
                        if (!this.field544.method723(this.field543.method149(), var9)) {
                           this.field545 = null;
                           break;
                        }

                        AttackMode var12 = var15 > 0 ? AttackMode.Packet : this.field543.field234.method461();
                        boolean var13 = false;
                        if (this.field543.field247.method419() && var16.containsKey(var11)) {
                           var12 = AttackMode.Packet;
                           var13 = true;
                        }

                        if (var17 > 0 && this.field543.method2116()) {
                           if (this.field543.method2117()) {
                              this.field544.method1416();
                              this.field545 = null;
                              return;
                           }

                           float[] var14 = EntityUtil.method2146(var8.getPos());
                           ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var14[0], var14[1]);
                        }

                        if (var12 == AttackMode.Vanilla) {
                           this.field544.method2142();
                        }

                        event.method557(this, var12, var10, var9 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                        this.field542.method146(var10);
                        if (var12 == AttackMode.Vanilla) {
                           Class2784.method1801(var10.getBlockPos().offset(var10.getSide()));
                        }

                        if (var13 || var15 == 2) {
                           EntityTracker.field3914.remove(var10.getBlockPos().offset(var10.getSide()));
                           if (var15 == 2) {
                              var15 = 1;
                           }
                        }
                     }
                  } else if (var8.getType() == Type.ENTITY) {
                     Entity var18 = ((EntityHitResult)var8).getEntity();
                     if (var17 > 0 && this.field543.method2116()) {
                        if (!this.field543.method2117()) {
                           this.field544.method1416();
                           this.field545 = null;
                           return;
                        }

                        float[] var19 = EntityUtil.method2146(var8.getPos());
                        ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var19[0], var19[1]);
                     }

                     mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var18, mc.player.isSneaking()));
                     mc.player.swingHand(Hand.MAIN_HAND);
                     IEndCrystalEntity var20 = (IEndCrystalEntity)var18;
                     var20.boze$setLastAttackTime(System.currentTimeMillis());
                     var15 = 2;
                  }
               }

               this.field544.method1416();
               this.field545 = null;
            } else if (this.field546 != null && this.field547 != null) {
               int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field543.field249.method461(), this.field543.field250));
               if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                  RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                  BlockHitResult var7 = RaycastUtil.method575(Reach.method1614(), var6, true);
                  if (Class2784.method5444(this.field546, var7)) {
                     if (this.field549.hasElapsed(this.field543.field233.method1295() * 50.0)) {
                        ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                        this.field549.reset();
                        this.field543.field233.method1296();
                        this.field542.method146(this.field546);
                     }
                  } else {
                     this.field549.reset();
                  }

                  this.field545 = null;
                  this.field546 = null;
               } else {
                  this.field545 = null;
                  this.field546 = null;
                  this.field549.reset();
               }
            }
         }
      }
   }

   private Boolean lambda$onGhostRotate$1(RotationHelper var1) {
      BlockHitResult var5 = RaycastUtil.method575(Reach.method1614(), var1, true);
      if (var5.getType() == Type.MISS) {
         return false;
      } else {
         return var5.getBlockPos() != this.field546.getBlockPos() ? false : var5.getSide() == this.field546.getSide();
      }
   }

   private boolean lambda$new$0() {
      return this.field543.method147() == Anticheat.NCP;
   }
}
