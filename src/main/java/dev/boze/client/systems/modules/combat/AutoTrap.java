package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.ac.Ghost;
import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.BlockPlaceMode;
import dev.boze.client.enums.TrapMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.mv;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.GhostRotations;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.legit.Reach;
import dev.boze.client.systems.modules.movement.Step;
import dev.boze.client.systems.render.PlacementRenderer;
import dev.boze.client.utils.*;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class1202;
import mapped.Class2784;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AutoTrap extends Module {
   public static AutoTrap INSTANCE = new AutoTrap();
   private final PlacementRenderer field2537 = new PlacementRenderer();
   private final PlaceHandler field2538 = new PlaceHandler();
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "AutoTrap while already using items");
   private final BooleanSetting feetOnly = new BooleanSetting("FeetOnly", false, "Only trap feet");
   private final BooleanSetting coverHead = new BooleanSetting("CoverHead", true, "Cover head", this::lambda$new$0);
   private final BooleanSetting noFeetReplace = new BooleanSetting(
      "NoFeetReplace", false, "Don't replace blocks around feet\nOnly applies to blocks placed after option's enabled\n"
   );
   private final SettingCategory autoDisable = new SettingCategory("AutoDisable", "Auto disable settings");
   private final BooleanSetting onJump = new BooleanSetting("OnJump", false, "Disable when you jump", this.autoDisable);
   private final BooleanSetting onStep = new BooleanSetting("OnStep", false, "Disable when you step", this.autoDisable);
   private final BooleanSetting onTP = new BooleanSetting("OnTP", false, "Disable when you teleport/chorus", this.autoDisable);
   private final BooleanSetting whenDone = new BooleanSetting("WhenDone", false, "Disable when done auto trapping", this.autoDisable);
   private final SettingCategory targets = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting players = new BooleanSetting("Players", true, "Target players", this.targets);
   private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends", this.targets);
   private final BooleanSetting animals = new BooleanSetting("Animals", false, "Target animals", this.targets);
   private final BooleanSetting monsters = new BooleanSetting("Monsters", false, "Target monsters", this.targets);
   private final SwapHandler field2539 = new SwapHandler(this, 75);
   private HitResult[] field2540 = null;
   private BlockHitResult field2541 = null;
   private RotationHelper field2542;
   private final Timer field2543 = new Timer();
   private final Timer field2544 = new Timer();
   private final ArrayList<BlockPos> field2545 = new ArrayList();
   private final ArrayList<BlockPos> field2546 = new ArrayList();
   private Entity field2547 = null;

   private AutoTrap() {
      super("AutoTrap", "Traps enemies with Obsidian\nIf no Obsidian, will use Ender Chests or other blast proof blocks\n", Category.Combat);
      Boze.EVENT_BUS.subscribe(this.field2537);
      this.field435 = true;
      this.addSettings(this.field2537.field224);
   }

   @Override
   public String method1322() {
      return this.field2547 != null ? this.field2547.getName().getString() : super.method1322();
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field2540 = null;
         this.field2538.field230.method1296();
         this.field2547 = null;
      }
   }

   @Override
   public void onDisable() {
      this.field2545.clear();
      this.field2546.clear();
   }

   @EventHandler
   public void method1479(PlayerPositionEvent event) {
      if (this.onStep.getValue() && Step.INSTANCE.isEnabled()) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method1480(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket
         && this.onTP.getValue()
         && MinecraftUtils.isClientActive()
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
   public void method1481(HandleInputEvent event) {
      if (!this.field2538.method2114()) {
         if (this.field2542 != null) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field2538.field249.getValue(), this.field2538.field250));
            if (this.field2543.hasElapsed(this.field2538.field232.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
               ((KeyBindingAccessor)mc.options.hotbarKeys[var5]).setTimesPressed(1);
               this.field2543.reset();
               this.field2538.field232.method1296();
            }
         } else {
            this.field2543.reset();
         }
      }
   }

   private boolean method1482() {
      if (this.onJump.getValue() && mc.player.getVelocity().y > 0.0 && !mc.player.isOnGround()) {
         this.setEnabled(false);
         return false;
      } else if (Options.method477(this.multiTask.getValue())) {
         return false;
      } else {
         int var4 = InventoryHelper.method174(
            BlastResistanceCalculator.method2130(this.field2538.field249.getValue(), this.field2538.field250), this.field2538.method149()
         );
         if (var4 == -1) {
            return false;
         } else {
            List var5 = this.method1488();
            this.field2547 = null;
            if (this.noFeetReplace.getValue()) {
               for (BlockPos var7 : this.field2545) {
                  if (!this.field2546.contains(var7) && !mc.world.getBlockState(var7).isAir()) {
                     this.field2546.add(var7);
                  }
               }
            }

            for (LivingEntity var10 : var5) {
               Box var8 = this.field2538.method155(var10);
               this.field2540 = TrapUtil.method585(
                  this.field2538,
                  var8,
                  this.feetOnly.getValue() ? TrapMode.Flat : (this.coverHead.getValue() ? TrapMode.Top : TrapMode.Tall),
                  this::lambda$calculate$1
               );
               this.field2547 = var10;
               if (this.field2540.length > 0) {
                  break;
               }
            }

            if (this.field2540 != null && this.field2540.length != 0) {
               return true;
            } else {
               if (this.whenDone.getValue()) {
                  this.setEnabled(false);
               }

               return false;
            }
         }
      }
   }

   private boolean method1483() {
      Ghost.field1313.method569(this.field2538.field228, this.field2538.field230, this.field2538.field231);
      if (!this.method1482()) {
         this.field2542 = null;
         return false;
      } else if (this.field2540 != null && this.field2540.length != 0 && this.field2540[0] instanceof BlockHitResult) {
         this.field2541 = (BlockHitResult)this.field2540[0];
         RotationHelper var4 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
         RotationHelper var5 = Class1202.method2391(mc.player.getEyePos(), this.field2541.getPos());
         this.field2542 = var4.method603(var5, this.field2538.field229.getValue());
         return true;
      } else {
         this.field2542 = null;
         return false;
      }
   }

   @EventHandler(
      priority = 76
   )
   public void method1484(eJ event) {
      if (!this.field2538.method2114() && this.field2538.field227.getValue() != BlockPlaceMode.Mouse && !event.method1101()) {
         if (this.method1483()) {
            event.method1099(this.field2542.method600(this::lambda$onGhostRotate$2));
         }
      }
   }

   @EventHandler(
      priority = 76
   )
   public void method1485(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (!this.field2538.method2114() && this.field2538.field227.getValue() != BlockPlaceMode.Normal) {
               if (this.method1483()) {
                  RotationHelper var5 = new RotationHelper(mc.player);
                  RotationHelper var6 = this.field2542.method1600();
                  RotationHelper var7 = var6.method606(var5);
                  Pair[] var8 = RotationHelper.method614(var7);
                  Pair var9 = var8[0];

                  for (Pair var13 : var8) {
                     BlockHitResult var14 = RaycastUtil.method575(Reach.method1614(), RotationHelper.method613(var5, var13), true);
                     if (var14.getType() != Type.MISS && var14.getBlockPos() == this.field2541.getBlockPos() && var14.getSide() == this.field2541.getSide()) {
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
      priority = 76
   )
   public void method1486(ACRotationEvent event) {
      if (!this.field2538.method2115()) {
         if (!event.method1018(this.field2538.method147().interactMode, this.field2538.method2116())) {
            if (this.method1482()) {
               if (this.field2538.method2116()) {
                  HitResult var5 = this.field2540[0];
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
   public void method1487(RotationEvent event) {
      if (!Options.method477(this.multiTask.getValue()) && !event.method555(this.field2538.method147().type, this.field2538.method2116())) {
         if (this.field2540 != null) {
            if (!this.field2538.method2115()) {
               byte var15 = 0;
               HashMap var16 = null;
               if (this.field2538.field247.getValue()) {
                  var16 = BlockBreakingTracker.field1511.method666(this.field2538.field248.getValue());
               }

               for (int var17 = 0; var17 < this.field2540.length; var17++) {
                  HitResult var18 = this.field2540[var17];
                  if (var18.getType() == Type.BLOCK) {
                     int var9 = InventoryHelper.method174(
                        BlastResistanceCalculator.method2130(this.field2538.field249.getValue(), this.field2538.field250), this.field2538.method149()
                     );
                     if (var9 == -1) {
                        this.field2540 = null;
                        break;
                     }

                     BlockHitResult var10 = (BlockHitResult)var18;
                     BlockPos var11 = var10.getBlockPos().offset(var10.getSide());
                     if (!this.field2538.method153(var10.getBlockPos(), var11, this.field2547)) {
                        if (!this.field2539.method723(this.field2538.method149(), var9)) {
                           this.field2540 = null;
                           break;
                        }

                        AttackMode var12 = var15 > 0 ? AttackMode.Packet : this.field2538.field234.getValue();
                        boolean var13 = false;
                        if (this.field2538.field247.getValue() && var16.containsKey(var11)) {
                           var12 = AttackMode.Packet;
                           var13 = true;
                        }

                        if (var17 > 0 && this.field2538.method2116()) {
                           if (this.field2538.method2117()) {
                              this.field2539.method1416();
                              this.field2540 = null;
                              return;
                           }

                           float[] var14 = EntityUtil.method2146(var18.getPos());
                           ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var14[0], var14[1]);
                        }

                        if (var12 == AttackMode.Vanilla) {
                           this.field2539.method2142();
                        }

                        event.method557(this, var12, var10, var9 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                        this.field2537.method146(var10);
                        if (var12 == AttackMode.Vanilla) {
                           Class2784.method1801(var11);
                        }

                        if (var13 || var15 == 2) {
                           EntityTracker.field3914.remove(var11);
                           if (var15 == 2) {
                              var15 = 1;
                           }
                        }

                        if (this.field2547 != null && this.noFeetReplace.getValue() && var11.getY() == (int)Math.floor(this.field2547.getY())) {
                           this.field2545.add(var11);
                        }
                     }
                  } else if (var18.getType() == Type.ENTITY) {
                     Entity var19 = ((EntityHitResult)var18).getEntity();
                     if (var17 > 0 && this.field2538.method2116()) {
                        if (!this.field2538.method2117()) {
                           this.field2539.method1416();
                           this.field2540 = null;
                           return;
                        }

                        float[] var20 = EntityUtil.method2146(var18.getPos());
                        ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var20[0], var20[1]);
                     }

                     mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(var19, mc.player.isSneaking()));
                     mc.player.swingHand(Hand.MAIN_HAND);
                     IEndCrystalEntity var21 = (IEndCrystalEntity)var19;
                     var21.boze$setLastAttackTime(System.currentTimeMillis());
                     var15 = 2;
                  }
               }

               this.field2539.method1416();
               this.field2540 = null;
            } else if (this.field2541 != null && this.field2542 != null) {
               int var5 = InventoryHelper.method176(BlastResistanceCalculator.method2130(this.field2538.field249.getValue(), this.field2538.field250));
               if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                  RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                  BlockHitResult var7 = RaycastUtil.method575(Reach.method1614(), var6, true);
                  if (Class2784.method5444(this.field2541, var7)) {
                     if (this.field2544.hasElapsed(this.field2538.field233.method1295() * 50.0)) {
                        ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                        this.field2544.reset();
                        this.field2538.field233.method1296();
                        this.field2537.method146(this.field2541);
                        BlockPos var8 = this.field2541.getBlockPos().offset(this.field2541.getSide());
                        if (this.field2547 != null && this.noFeetReplace.getValue() && var8.getY() == (int)Math.floor(this.field2547.getY())) {
                           this.field2545.add(var8);
                        }
                     }
                  } else {
                     this.field2544.reset();
                  }

                  this.field2540 = null;
                  this.field2541 = null;
               } else {
                  this.field2540 = null;
                  this.field2541 = null;
                  this.field2544.reset();
               }
            }
         }
      }
   }

   private List<LivingEntity> method1488() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof LivingEntity
            && this.method1489(var6)
            && !(var6.getEyePos().distanceTo(mc.player.getEyePos()) > this.field2538.field236.getValue() + 2.5)
            && !((LivingEntity)var6).isDead()
            && !(((LivingEntity)var6).getHealth() + ((LivingEntity)var6).getAbsorptionAmount() <= 0.0F)) {
            var4.add((LivingEntity)var6);
         }
      }

      return (List<LivingEntity>)var4.stream().sorted(Comparator.comparing(AutoTrap::lambda$getTargets$3)).collect(Collectors.toList());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1489(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else {
            return Friends.method2055(var1) ? this.friends.getValue() : this.players.getValue();
         }
      } else {
         switch (mv.field2112[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.animals.getValue();
            case 5:
               return this.monsters.getValue();
            default:
               return false;
         }
      }
   }

   private static Float lambda$getTargets$3(LivingEntity var0) {
      return var0.distanceTo(mc.player);
   }

   private Boolean lambda$onGhostRotate$2(RotationHelper var1) {
      BlockHitResult var5 = RaycastUtil.method575(Reach.method1614(), var1, true);
      if (var5.getType() == Type.MISS) {
         return false;
      } else {
         return var5.getBlockPos() != this.field2541.getBlockPos() ? false : var5.getSide() == this.field2541.getSide();
      }
   }

   private boolean lambda$calculate$1(LivingEntity var1, BlockPos var2) {
      return this.noFeetReplace.getValue() && var2.getY() == (int)Math.floor(var1.getY()) ? !this.field2546.contains(var2) : true;
   }

   private boolean lambda$new$0() {
      return !this.feetOnly.getValue();
   }
}
