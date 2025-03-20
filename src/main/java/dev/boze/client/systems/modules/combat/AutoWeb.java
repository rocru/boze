package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.ac.Ghost;
import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.BlockPlaceMode;
import dev.boze.client.enums.CheckEntityMode;
import dev.boze.client.events.*;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.mw;
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
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class1202;
import mapped.Class2784;
import mapped.Class5924;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext.ShapeType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AutoWeb extends Module {
   public static final AutoWeb INSTANCE = new AutoWeb();
   private final PlacementRenderer field1016 = new PlacementRenderer();
   private final InteractionHandler field1017 = new InteractionHandler();
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "AutoWeb while already using items");
   private final BooleanSetting predictive = new BooleanSetting("Predictive", false, "Only web when target leaving hole");
   private final BooleanSetting doubleWeb = new BooleanSetting("Double", false, "Double (tall) web");
   private final SettingCategory autoDisableSettings = new SettingCategory("AutoDisable", "Auto disable settings");
   private final BooleanSetting onJump = new BooleanSetting("OnJump", false, "Disable when you jump", this.autoDisableSettings);
   private final BooleanSetting onStep = new BooleanSetting("OnStep", false, "Disable when you step", this.autoDisableSettings);
   private final BooleanSetting onTP = new BooleanSetting("OnTP", false, "Disable when you teleport/chorus", this.autoDisableSettings);
   private final BooleanSetting whenDone = new BooleanSetting("WhenDone", false, "Disable when done webbing", this.autoDisableSettings);
   public final SettingCategory targets = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting players = new BooleanSetting("Players", true, "Target players", this.targets);
   private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends", this.targets);
   private final BooleanSetting animals = new BooleanSetting("Animals", false, "Target animals", this.targets);
   private final BooleanSetting monsters = new BooleanSetting("Monsters", false, "Target monsters", this.targets);
   private final SwapHandler field1018 = new SwapHandler(this, 76);
   private HitResult[] field1019 = null;
   private BlockHitResult field1020 = null;
   private RotationHelper field1021;
   private final Timer field1022 = new Timer();
   private final Timer field1023 = new Timer();
   private Entity field1024 = null;

   private AutoWeb() {
      super("AutoWeb", "Automatically places webs inside enemies", Category.Combat);
      Boze.EVENT_BUS.subscribe(this.field1016);
      this.field435 = true;
      this.addSettings(this.field1016.field224);
   }

   private boolean method1971() {
      if (this.onJump.getValue() && mc.player.getVelocity().y > 0.0 && !mc.player.isOnGround()) {
         this.setEnabled(false);
         return false;
      } else if (Options.method477(this.multiTask.getValue())) {
         return false;
      } else {
         int var4 = InventoryHelper.method174(BlastResistanceCalculator.field3906, this.field1017.method149());
         if (var4 == -1) {
            return false;
         } else {
            List var5 = this.method2032();
            this.field1024 = null;
            ArrayList var6 = new ArrayList();

            for (LivingEntity var8 : var5) {
               BlockPos var9 = BlockPos.ofFloored(var8.getPos());
               if (!this.predictive.getValue() || Class5924.method77(true, var8) && !(var8.prevY >= var8.getY())) {
                  HashSet var10 = new HashSet();
                  if (EntityTracker.method2143(var10, var9, CheckEntityMode.Off)) {
                     HitResult var11 = this.field1017.method150(var10, var9);
                     if (var11 != null) {
                        var6.add(var11);
                        var10.add(var9);
                        if (var6.size() >= this.field1017.method2010()) {
                           break;
                        }
                     }
                  }

                  if (this.doubleWeb.getValue()) {
                     BlockPos var13 = var9.up();
                     if (EntityTracker.method2143(var10, var13, CheckEntityMode.Off)) {
                        if (this.field1017.method2115()) {
                           RaycastUtil.field1324 = ShapeType.OUTLINE;
                        }

                        HitResult var12 = this.field1017.method150(var10, var13);
                        if (this.field1017.method2115()) {
                           RaycastUtil.field1324 = ShapeType.COLLIDER;
                        }

                        if (var12 != null) {
                           var6.add(var12);
                           var10.add(var13);
                           if (var6.size() >= this.field1017.method2010()) {
                              break;
                           }
                        }
                     }
                  }
               }
            }

            if (var6.isEmpty()) {
               if (this.whenDone.getValue()) {
                  this.setEnabled(false);
               }

               return false;
            } else {
               this.field1019 = (HitResult[])var6.toArray(new HitResult[0]);
               return true;
            }
         }
      }
   }

   private boolean method1972() {
      Ghost.field1313.method569(this.field1017.field228, this.field1017.field230, this.field1017.field231);
      if (!this.method1971()) {
         this.field1021 = null;
         return false;
      } else if (this.field1019 != null && this.field1019.length != 0 && this.field1019[0] instanceof BlockHitResult) {
         this.field1020 = (BlockHitResult)this.field1019[0];
         RotationHelper var4 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
         RotationHelper var5 = Class1202.method2391(mc.player.getEyePos(), this.field1020.getPos());
         this.field1021 = var4.method603(var5, this.field1017.field229.getValue());
         return true;
      } else {
         this.field1021 = null;
         return false;
      }
   }

   @Override
   public String method1322() {
      return this.field1024 != null ? this.field1024.getName().getString() : super.method1322();
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.field1019 = null;
         this.field1017.field230.method1296();
         this.field1024 = null;
      }
   }

   @EventHandler
   public void method1877(PlayerPositionEvent event) {
      if (this.onStep.getValue() && Step.INSTANCE.isEnabled()) {
         this.setEnabled(false);
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
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
   public void method1693(HandleInputEvent event) {
      if (!this.field1017.method2114()) {
         if (this.field1021 != null) {
            int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3906);
            if (this.field1022.hasElapsed(this.field1017.field232.method1295() * 50.0) && var5 != -1 && mc.player.getInventory().selectedSlot != var5) {
               ((KeyBindingAccessor)mc.options.hotbarKeys[var5]).setTimesPressed(1);
               this.field1022.reset();
               this.field1017.field232.method1296();
            }
         } else {
            this.field1022.reset();
         }
      }
   }

   @EventHandler(
      priority = 78
   )
   public void method1694(eJ event) {
      if (!this.field1017.method2114() && this.field1017.field227.getValue() != BlockPlaceMode.Mouse && !event.method1101()) {
         if (this.method1972()) {
            event.method1099(this.field1021.method600(this::lambda$onGhostRotate$0));
         }
      }
   }

   @EventHandler(
      priority = 78
   )
   public void method1695(MouseUpdateEvent event) {
      if (MinecraftUtils.isClientActive() && !event.method1022()) {
         if (mc.currentScreen == null || mc.currentScreen instanceof ClickGUI) {
            if (!this.field1017.method2114() && this.field1017.field227.getValue() != BlockPlaceMode.Normal) {
               if (this.method1972()) {
                  RotationHelper var5 = new RotationHelper(mc.player);
                  RotationHelper var6 = this.field1021.method1600();
                  RotationHelper var7 = var6.method606(var5);
                  Pair[] var8 = RotationHelper.method614(var7);
                  Pair var9 = var8[0];

                  for (Pair var13 : var8) {
                     BlockHitResult var14 = RaycastUtil.method575(Reach.method1614(), RotationHelper.method613(var5, var13), true);
                     if (var14.getType() != Type.MISS && var14.getBlockPos() == this.field1020.getBlockPos() && var14.getSide() == this.field1020.getSide()) {
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
      priority = 78
   )
   public void method1885(ACRotationEvent event) {
      if (!this.field1017.method2115()) {
         if (!event.method1018(this.field1017.method147().interactMode, this.field1017.method2116())) {
            if (this.method1971()) {
               if (this.field1017.method2116()) {
                  HitResult var5 = this.field1019[0];
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
   public void method1883(RotationEvent event) {
      if (!Options.method477(this.multiTask.getValue()) && !event.method555(this.field1017.method147().type, this.field1017.method2116())) {
         if (this.field1019 != null) {
            if (!this.field1017.method2115()) {
               byte var14 = 0;

               for (int var15 = 0; var15 < this.field1019.length; var15++) {
                  HitResult var16 = this.field1019[var15];
                  if (var16.getType() == Type.BLOCK) {
                     int var8 = InventoryHelper.method174(BlastResistanceCalculator.field3906, this.field1017.method149());
                     if (var8 == -1) {
                        this.field1019 = null;
                        break;
                     }

                     BlockHitResult var9 = (BlockHitResult)var16;
                     if (!this.field1018.method723(this.field1017.method149(), var8)) {
                        this.field1019 = null;
                        break;
                     }

                     AttackMode var10 = var14 > 0 ? AttackMode.Packet : this.field1017.field234.getValue();
                     BlockPos var12 = var9.getBlockPos().offset(var9.getSide());
                     if (var15 > 0 && this.field1017.method2116()) {
                        if (this.field1017.method2117()) {
                           this.field1018.method1416();
                           this.field1019 = null;
                           return;
                        }

                        float[] var13 = EntityUtil.method2146(var16.getPos());
                        ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var13[0], var13[1]);
                     }

                     if (var10 == AttackMode.Vanilla) {
                        this.field1018.method2142();
                     }

                     event.method557(this, var10, var9, var8 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND);
                     this.field1016.method146(var9);
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

               this.field1018.method1416();
               this.field1019 = null;
            } else if (this.field1020 != null && this.field1021 != null) {
               int var5 = InventoryHelper.method176(BlastResistanceCalculator.field3906);
               if (var5 != -1 && mc.player.getInventory().selectedSlot == var5) {
                  RotationHelper var6 = GhostRotations.INSTANCE.field760 == null ? new RotationHelper(mc.player) : GhostRotations.INSTANCE.field760;
                  BlockHitResult var7 = RaycastUtil.method575(Reach.method1614(), var6, true);
                  if (Class2784.method5444(this.field1020, var7)) {
                     if (this.field1023.hasElapsed(this.field1017.field233.method1295() * 50.0)) {
                        ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                        this.field1023.reset();
                        this.field1017.field233.method1296();
                        this.field1016.method146(this.field1020);
                     }
                  } else {
                     this.field1023.reset();
                  }

                  this.field1019 = null;
                  this.field1020 = null;
               } else {
                  this.field1019 = null;
                  this.field1020 = null;
                  this.field1023.reset();
               }
            }
         }
      }
   }

   private List<LivingEntity> method2032() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof LivingEntity
            && this.method2055(var6)
            && !(var6.getEyePos().distanceTo(mc.player.getEyePos()) > this.field1017.field236.getValue() + 2.5)
            && !((LivingEntity)var6).isDead()
            && !(((LivingEntity)var6).getHealth() + ((LivingEntity)var6).getAbsorptionAmount() <= 0.0F)) {
            var4.add((LivingEntity)var6);
         }
      }

      return (List<LivingEntity>)var4.stream().sorted(Comparator.comparing(AutoWeb::lambda$getTargets$1)).collect(Collectors.toList());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method2055(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else {
            return Friends.method2055(var1) ? this.friends.getValue() : this.players.getValue();
         }
      } else {
         switch (mw.field2113[var1.getType().getSpawnGroup().ordinal()]) {
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

   private static Float lambda$getTargets$1(LivingEntity var0) {
      return var0.distanceTo(mc.player);
   }

   private Boolean lambda$onGhostRotate$0(RotationHelper var1) {
      BlockHitResult var5 = RaycastUtil.method575(Reach.method1614(), var1, true);
      if (var5.getType() == Type.MISS) {
         return false;
      } else {
         return var5.getBlockPos() != this.field1020.getBlockPos() ? false : var5.getSide() == this.field1020.getSide();
      }
   }
}
