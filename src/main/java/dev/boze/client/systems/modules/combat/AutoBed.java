package dev.boze.client.systems.modules.combat;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.BedTargetMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.YPriority;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.jumptable.mt;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.autobed.mu;
import dev.boze.client.systems.modules.render.PlaceRender;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.*;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.InventoryUtil;
import dev.boze.client.utils.player.RotationHandler;
import dev.boze.client.utils.player.SlotUtils;
import dev.boze.client.utils.trackers.LatencyTracker;
import dev.boze.client.utils.trackers.TargetTracker;
import mapped.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BedItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AutoBed extends Module {
   public static final AutoBed INSTANCE = new AutoBed();
   private final BooleanSetting renderPlacements = new BooleanSetting("Render", true, "Render placements");
   private final ColorSetting fillColor = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill", this.renderPlacements);
   private final ColorSetting outlineColor = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline", this.renderPlacements);
   private final BooleanSetting multitask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final EnumSetting<AnticheatMode> interactionMode = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Interaction mode");
   private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing hand");
   public final BooleanSetting strictDirection = new BooleanSetting("StrictDirection", true, "Strict direction");
   private final BooleanSetting strictPlace = new BooleanSetting("StrictPlace", false, "Stricter placing checks");
   private final BooleanSetting sequential = new BooleanSetting("Sequential", true, "Sequential");
   public final FloatSetting wallRange = new FloatSetting("WallsRange", 2.0F, 0.0F, 6.0F, 0.1F, "Walls range");
   private final BooleanSetting autoMove = new BooleanSetting("AutoMove", true, "Automatically move beds to your hotbar");
   private final BooleanSetting autoCraft = new BooleanSetting("AutoCraft", true, "Automatically craft beds with nearby crafting tables");
   private final BooleanSetting strictAutoCraft = new BooleanSetting("Strict", false, "Strict Auto Craft", this.autoCraft);
   private final BooleanSetting openTable = new BooleanSetting("OpenTable", true, "Automatically open tables", this.autoCraft);
   private final BooleanSetting placeTable = new BooleanSetting("PlaceTable", true, "Automatically place tables", this.openTable::getValue, this.autoCraft);
   private final EnumSetting<YPriority> yPriority = new EnumSetting<YPriority>(
      "YPriority", YPriority.EyeLevel, "Y-Level priority for placing tables", this.openTable::getValue, this.autoCraft
   );
   private final BooleanSetting airPlace = new BooleanSetting("AirPlace", false, "Air place crafting tables", this.openTable::getValue, this.autoCraft);
   public final SettingCategory breakSettings = new SettingCategory("Break", "Break settings");
   private final BooleanSetting breakRotate = new BooleanSetting("Rotate", false, "Rotate when breaking beds", this.breakSettings);
   public final FloatSetting breakSpeed = new FloatSetting("Speed", 20.0F, 1.0F, 20.0F, 0.1F, "Bed break speed", this.breakSettings);
   public final FloatSetting breakRange = new FloatSetting("Range", 4.3F, 1.0F, 6.0F, 0.1F, "Break range for breaking visible bed", this.breakSettings);
   private final SettingCategory placeSettings = new SettingCategory("Place", "Place settings");
   private final BooleanSetting placeRotate = new BooleanSetting("Rotate", false, "Rotate when placing beds", this.placeSettings);
   private final FloatSetting placeSpeed = new FloatSetting("Speed", 20.0F, 1.0F, 20.0F, 0.1F, "Place speed", this.placeSettings);
   private final FloatSetting placeRange = new FloatSetting("Range", 4.0F, 1.0F, 6.0F, 0.1F, "Place range for visible blocks", this.placeSettings);
   private final FloatSetting strictPlaceRange = new FloatSetting(
      "StrictRange", 0.0F, 0.0F, 6.0F, 0.1F, "Strict place range for visible blocks", this.placeSettings
   );
   public final SettingCategory targetSettings = new SettingCategory("Targeting", "Targeting settings");
   public final EnumSetting<BedTargetMode> targetMode = new EnumSetting<BedTargetMode>(
      "Inside", BedTargetMode.Full, "Tries placing beds inside players", this.targetSettings
   );
   private final BooleanSetting antiSuicide = new BooleanSetting("AntiSuicide", true, "Prevents suicide", this.targetSettings);
   public final FloatSetting bedRange = new FloatSetting("BedRange", 6.0F, 1.0F, 16.0F, 0.5F, "Max range between placements and targets", this.targetSettings);
   public final FloatSetting targetRange = new FloatSetting("TargetRange", 8.0F, 4.0F, 16.0F, 0.5F, "Range from which to select targets", this.targetSettings);
   public final FloatSetting breakSelfDamage = new FloatSetting(
      "BreakSelfDmg", 0.0F, 0.0F, 20.0F, 0.1F, "Maximum self damage for breaking enemy beds", this.targetSettings
   );
   public final FloatSetting placeSelfDamage = new FloatSetting(
      "PlaceSelfDmg", 0.0F, 0.0F, 20.0F, 0.1F, "Maximum amount of self damage for placing beds", this.targetSettings
   );
   public final FloatSetting minDamage = new FloatSetting(
      "MinDamage", 6.0F, 0.0F, 20.0F, 0.1F, "Minimum amount of damage for placing beds", this.targetSettings
   );
   public final FloatSetting lethalHP = new FloatSetting("LethalHP", 4.0F, 0.0F, 20.0F, 0.1F, "Health at which to ignore min damage", this.targetSettings);
   public final BindSetting forceBind = new BindSetting("Force", Bind.fromKey(342), "Keybind to ignore min damage", this.targetSettings);
   private final SettingCategory targets = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting players = new BooleanSetting("Players", true, "Target players", this.targets);
   private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends", this.targets);
   private final BooleanSetting animals = new BooleanSetting("Animals", false, "Target animals", this.targets);
   private final BooleanSetting monsters = new BooleanSetting("Monsters", false, "Target monsters", this.targets);
   private final Timer am = new Timer();
   public final Timer an = new Timer();
   private final Timer ao = new Timer();
   private final Timer ap = new Timer();
   private String aq;
   private final Timer ar = new Timer();
   private boolean as = false;
   private ActionWrapper at = null;
   private final Queue<Runnable> au = new LinkedList();
   private mu av = null;
   private final ConcurrentHashMap<mu, Long> aw = new ConcurrentHashMap();
   private final LinkedList<ActionWrapper> ax = new LinkedList();

   public AutoBed() {
      super("AutoBed", "Attacks players by blowing up beds", Category.Combat);
      this.addSettings(this.renderPlacements);
   }

   @EventHandler
   public void method1437(Render3DEvent event) {
      if (this.renderPlacements.getValue()) {
         this.aw.forEach(this::lambda$onRender$0);
      }
   }

   @Override
   public String method1322() {
      return !this.ar.hasElapsed(2500.0) && this.aq != null ? this.aq : "";
   }

   @EventHandler(
      priority = 49
   )
   private void method1438(ACRotationEvent var1) {
      if (!var1.method1018(this.interactionMode.getValue(), this.breakRotate.getValue() || this.placeRotate.getValue())) {
         this.ax.clear();
         if (!mc.world.getRegistryKey().getValue().getPath().equals("overworld")) {
            if (!Options.method477(this.multitask.getValue())) {
               if (this.strictAutoCraft.getValue() && !this.au.isEmpty()) {
                  this.au.poll().run();
                  if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                     mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                  }
               } else {
                  if (!(mc.player.currentScreenHandler instanceof CraftingScreenHandler)) {
                     this.as = false;
                  }

                  if (this.as && this.au.isEmpty() && InventoryHelper.method169(AutoBed::lambda$onRotateEvent$1) != -1) {
                     mc.player.closeHandledScreen();
                     this.as = false;
                  }

                  List var5 = this.method1441();
                  ActionWrapper var6 = this.method1443(var5, null);
                  if (var6 != null) {
                     this.ax.add(var6);
                     if (var6.field3900) {
                        var1.method1020();
                        var1.yaw = var6.field3902;
                        var1.pitch = var6.field3903;
                     }
                  }

                  if ((var6 == null || this.sequential.getValue()) && this.method1444()) {
                     mu var7 = this.method1440(var5);
                     if (var7 != null) {
                        if (this.method1445()) {
                           var7.field2511.method2161(Hand.OFF_HAND);
                        } else {
                           var7.field2511.method2161(Hand.MAIN_HAND);
                           var7.field2511.method2163(this.method1446());
                        }

                        this.ax.add(new ActionWrapper(var7.field2511));
                        if (!var1.method1022() && this.placeRotate.getValue()) {
                           var1.method1020();
                           var1.yaw = var7.field2511.method2157();
                           var1.pitch = var7.field2511.method2159();
                        }

                        this.av = var7;
                        if (this.renderPlacements.getValue()) {
                           this.aw.put(var7, System.currentTimeMillis());
                        }

                        this.am.reset();
                     }
                  } else if (this.at != null) {
                     this.ax.add(this.at);
                     this.at = null;
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 49
   )
   public void method1439(RotationEvent event) {
      if (!event.method554(RotationMode.Sequential)) {
         while (!this.ax.isEmpty()) {
            ActionWrapper var5 = this.ax.poll();
            if (var5.field3900 && (RotationHandler.method215() != var5.field3902 || RotationHandler.method520() != var5.field3903)) {
               ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var5.field3902, var5.field3903);
            }

            var5.field3904.run();
         }
      }
   }

   private mu method1440(List<LivingEntity> var1) {
      if (var1.isEmpty()) {
         return null;
      } else {
         if (this.am.hasElapsed(1000.0 - (double)(this.placeSpeed.getValue() * 50.0F))) {
            ArrayList<mu> var5 = this.targetMode.getValue() == BedTargetMode.Full ? null : this.method1447();
            mu var6 = null;
            LivingEntity var7 = null;
            double var8 = 0.0;

            label82:
            for (LivingEntity var11 : var1) {
               for (mu var13 : this.targetMode.getValue() == BedTargetMode.Full ? this.method1448(var11) : var5) {
                  double var14 = 0.0;
                  Vec3d var16 = new Vec3d((double)var13.field2509.getX() + 0.5, var13.field2509.getY(), (double)var13.field2509.getZ() + 0.5);
                  Vec3d var17 = new Vec3d((double)var13.field2510.getX() + 0.5, var13.field2510.getY(), (double)var13.field2510.getZ() + 0.5);
                  if (!(var11.getPos().distanceTo(var16) > (double)this.bedRange.getValue().floatValue())) {
                     boolean var18 = false;
                     double var19 = Math.max(
                        Class3069.method6005(var11, var16, var13.field2509.down(), true), Class3069.method6005(var11, var17, var13.field2510.down(), true)
                     );
                     if (var19 >= 0.5
                        && (
                           (double)(var11.getHealth() + var11.getAbsorptionAmount()) - var19 + 2.0 <= 0.0
                              || var11.getHealth() + var11.getAbsorptionAmount() < this.lethalHP.getValue()
                        )) {
                        var18 = true;
                     }

                     if (this.forceBind.getValue().isPressed()) {
                        var18 = true;
                     }

                     if (var19 >= (double)this.minDamage.getValue().floatValue() || var18) {
                        var14 = var19;
                     }

                     if (var14 > var8) {
                        var8 = var14;
                        var6 = var13;
                        var7 = var11;
                        if (var14 == 4200.0) {
                           break label82;
                        }
                     }
                  }
               }

               if (var7 != null) {
                  break;
               }
            }

            if (var7 != null) {
               TargetTracker.method488(var7);
               this.aq = var7.getNameForScoreboard();
               this.ar.reset();
            } else {
               this.aq = null;
            }

             return var6;
         }

         return null;
      }
   }

   public List<LivingEntity> method1441() {
      ArrayList<Entity> var4 = new ArrayList<>();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof LivingEntity
            && this.method1442(var6)
            && !(var6.distanceTo(mc.player) > this.targetRange.getValue())
            && !((LivingEntity)var6).isDead()
            && !(((LivingEntity)var6).getHealth() + ((LivingEntity)var6).getAbsorptionAmount() <= 0.0F)) {
            var4.add(var6);
         }
      }

      return (List<LivingEntity>)var4.stream().sorted(Comparator.comparing(AutoBed::lambda$getTargetsInRange$2)).limit(3L).collect(Collectors.toList());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1442(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else {
            return Friends.method2055(var1) ? this.friends.getValue() : this.players.getValue();
         }
      } else {
          return switch (mt.field2110[var1.getType().getSpawnGroup().ordinal()]) {
              case 1, 2, 3, 4 -> this.animals.getValue();
              case 5 -> this.monsters.getValue();
              default -> false;
          };
      }
   }

   public ActionWrapper method1443(List<LivingEntity> targets, BlockPos pos) {
      if (!this.an.hasElapsed(1000.0 - (double)(this.breakSpeed.getValue() * 50.0F))) {
         return null;
      } else {
         BlockPos var6 = null;
         double var7 = 0.0;

         for (BlockEntity var10 : Class5914.method19()) {
            if (var10 instanceof BedBlockEntity) {
               BlockPos var11 = var10.getPos();
               if (pos == null || pos.equals(var11)) {
                  Vec3d var12 = new Vec3d((double)var11.getX() + 0.5, var11.getY(), (double)var11.getZ() + 0.5);
                  double var13 = Class3069.method6005(mc.player, var12, var11.down(), true);
                  if (var12.distanceTo(mc.player.getEyePos()) < (double)this.breakRange.getValue().floatValue()
                     && (!this.antiSuicide.getValue() || var13 + 2.0 < (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()))
                     && (this.breakSelfDamage.getValue() == 0.0F || var13 < (double)this.breakSelfDamage.getValue().floatValue())) {
                     if (pos != null) {
                        var6 = var11;
                        break;
                     }

                     double var15 = 0.0;

                     for (LivingEntity var18 : targets) {
                        if (!(var18.getPos().distanceTo(var12) > (double)(this.bedRange.getValue() + 1.0F))) {
                           var15 += Class3069.method6005(var18, var12, var11.down(), true);
                        }
                     }

                     if (var15 > var7) {
                        var7 = var15;
                        var6 = var11;
                     }
                  }
               }
            }
         }

         if (var6 == null && pos == null && this.sequential.getValue() && this.av != null && !this.am.hasElapsed(500.0)) {
            for (int var19 = 0; var19 < 2; var19++) {
               BlockPos var20 = var19 == 0 ? this.av.field2509 : this.av.field2510;
               Vec3d var22 = new Vec3d((double)var20.getX() + 0.5, var20.getY(), (double)var20.getZ() + 0.5);
               double var24 = Class3069.method6005(mc.player, var22, var20.down(), true);
               if (var22.distanceTo(mc.player.getEyePos()) < (double)this.breakRange.getValue().floatValue()
                  && (!this.antiSuicide.getValue() || var24 + 2.0 < (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()))
                  && (this.breakSelfDamage.getValue() == 0.0F || var24 < (double)this.breakSelfDamage.getValue().floatValue())) {
                  double var14 = 0.0;

                  for (LivingEntity var25 : targets) {
                     if (!(var25.getPos().distanceTo(var22) > (double)(this.bedRange.getValue() + 1.0F))) {
                        var14 += Class3069.method6005(var25, var22, var20.down(), true);
                     }
                  }

                  if (var14 > var7) {
                     var7 = var14;
                     var6 = var20;
                  }
               }
            }
         }

         if (var6 != null) {
            Vec3d var21 = new Vec3d((double)var6.getX() + 0.5, var6.getY(), (double)var6.getZ() + 0.5);
            this.an.reset();
            if (this.breakRotate.getValue()) {
               float[] var23 = EntityUtil.method2146(var21);
               return new ActionWrapper(this::lambda$generateBreak$3, var23[0], var23[1]);
            } else {
               return new ActionWrapper(this::lambda$generateBreak$4);
            }
         } else {
            return null;
         }
      }
   }

   private boolean method1444() {
      return this.method1445() || mc.player.getInventory().getMainHandStack().getItem() instanceof BedItem || this.method1446() != -1;
   }

   private boolean method1445() {
      return mc.player.getOffHandStack().getItem() instanceof BedItem;
   }

   private int method1446() {
      if (!(mc.player.currentScreenHandler instanceof CraftingScreenHandler)) {
         if (mc.player.getMainHandStack().getItem() instanceof BedItem) {
            return mc.player.getInventory().selectedSlot;
         }

         for (int var4 = 0; var4 < 9; var4++) {
            if (mc.player.getInventory().getStack(var4).getItem() instanceof BedItem) {
               return var4;
            }
         }

         if (this.autoMove.getValue()) {
            int var21 = InventoryHelper.method169(AutoBed::lambda$getBedSlot$5);
            int var5 = InventoryHelper.method2010();
            if (var21 != -1 && var5 != -1) {
               InvUtils.method2202().method2207(var21).method2214(var5);
               if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                  mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
               }

               return var5;
            }
         }
      }

      if (this.autoCraft.getValue() && InventoryHelper.method2010() != -1) {
         int var22 = InventoryHelper.method169(AutoBed::lambda$getBedSlot$6);
         int var23 = InventoryHelper.method169(AutoBed::lambda$getBedSlot$7);
         if (var22 == -1 || var23 == -1) {
            return -1;
         }

         if (mc.player.currentScreenHandler instanceof CraftingScreenHandler) {
            if (!this.as) {
               this.as = true;
               this.au.add(AutoBed::lambda$getBedSlot$8);
               this.au.add(AutoBed::lambda$getBedSlot$9);
               this.au.add(AutoBed::lambda$getBedSlot$10);
               if (this.strictAutoCraft.getValue()) {
                  if (!this.au.isEmpty()) {
                     this.au.poll().run();
                  }

                  if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                     mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                  }
               } else {
                  while (!this.au.isEmpty()) {
                     this.au.poll().run();
                  }

                  if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                     mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                  }
               }
            }
         } else if (this.openTable.getValue() && this.ao.hasElapsed(this.strictAutoCraft.getValue() ? 350.0 : (double) LatencyTracker.INSTANCE.field1308)) {
            BlockPos var6 = mc.player.getBlockPos();
            int var7 = (int)Math.ceil(this.placeRange.getValue());
            int var8 = (int)Math.ceil(this.placeRange.getValue());
            if (this.strictPlaceRange.getValue() == 0.0F) {
               var7++;
            }

            for (int var9 = var6.getX() - var7; var9 < var6.getX() + var7; var9++) {
               for (int var10 = var6.getY() - var8; var10 < var6.getY() + var8; var10++) {
                  for (int var11 = var6.getZ() - var7; var11 < var6.getZ() + var7; var11++) {
                     BlockPos var12 = new BlockPos(var9, var10, var11);
                     if (mc.world.getBlockState(var12).getBlock() == Blocks.CRAFTING_TABLE) {
                        Vec3d var13 = new Vec3d((double)var12.getX() + 0.5, (double)var12.getY() + 1.0, (double)var12.getZ() + 0.5);
                        if (this.placeRotate.getValue()) {
                           float[] var14 = EntityUtil.method2146(var13);
                           this.at = new ActionWrapper(this::lambda$getBedSlot$11, var14[0], var14[1]);
                        } else {
                           this.at = new ActionWrapper(this::lambda$getBedSlot$12);
                        }

                        this.ao.reset();
                        return -1;
                     }
                  }
               }
            }

            if (this.placeTable.getValue() && this.ap.hasElapsed(this.strictAutoCraft.getValue() ? 350.0 : (double)LatencyTracker.INSTANCE.field1308)) {
               int var24 = InventoryHelper.method169(AutoBed::lambda$getBedSlot$13);
               if (var24 == -1) {
                  return -1;
               }

               if (var24 > 8) {
                  int var25 = InventoryHelper.method2010();
                  if (var25 == -1) {
                     return -1;
                  }

                  InvUtils.method2202().method2207(var24).method2214(var25);
                  if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
                     mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
                  }

                  var24 = var25;
               }

               PlaceAction var26 = null;
               double var27 = 9999.0;
               boolean var28 = Class2811.field101;
               Class2811.field101 = this.airPlace.getValue();

               for (int var29 = var6.getX() - var7; var29 < var6.getX() + var7; var29++) {
                  for (int var15 = var6.getY() - var8; var15 < var6.getY() + var8; var15++) {
                     for (int var16 = var6.getZ() - var7; var16 < var6.getZ() + var7; var16++) {
                        BlockPos var17 = new BlockPos(var29, var15, var16);
                        PlaceAction var18 = Class2812.method5501(var17, this.placeRotate.getValue(), this.swing.getValue(), false, Hand.MAIN_HAND, var24);
                        if (var18 != null) {
                           double var19 = mc.player
                              .getPos()
                              .add(0.0, this.yPriority.getValue().field1704, 0.0)
                              .distanceTo(new Vec3d((double)var17.getX() + 0.5, (double)var17.getY() + 0.5, (double)var17.getZ() + 0.5));
                           if (var26 == null || var19 < var27) {
                              var26 = var18;
                              var27 = var19;
                           }
                        }
                     }
                  }
               }

               Class2811.field101 = var28;
               if (var26 != null) {
                  this.at = new ActionWrapper(var26);
                  this.ap.reset();
                  return -1;
               }
            }
         }
      }

      return -1;
   }

   private ArrayList<mu> method1447() {
      ArrayList var4 = new ArrayList();
      BlockPos var5 = mc.player.getBlockPos();
      int var6 = (int)Math.ceil(this.placeRange.getValue().floatValue());
      int var7 = (int)Math.ceil(this.placeRange.getValue().floatValue());
      if (this.strictPlaceRange.getValue() == 0.0F) {
         var6++;
      }

      Class2811.field103 = true;
      Class2811.field104 = true;
      Class2811.field99 = true;
      Class2811.field107 = false;

      for (int var8 = var5.getX() - var6; var8 < var5.getX() + var6; var8++) {
         for (int var9 = var5.getY() - var7; var9 < var5.getY() + var7; var9++) {
            for (int var10 = var5.getZ() - var6; var10 < var5.getZ() + var6; var10++) {
               BlockPos var11 = new BlockPos(var8, var9, var10);
               ArrayList var12 = this.method1449(var11, null);
               if (var12 != null) {
                  var4.addAll(var12);
               }
            }
         }
      }

      Class2811.field103 = false;
      Class2811.field104 = false;
      Class2811.field99 = false;
      Class2811.field107 = true;
      return var4;
   }

   private ArrayList<mu> method1448(LivingEntity var1) {
      ArrayList var5 = new ArrayList();
      if ((double)var1.distanceTo(mc.player) > this.method1450() + 2.5) {
         return var5;
      } else {
         Class2811.field103 = true;
         Class2811.field104 = true;
         Class2811.field99 = true;
         Class2811.field107 = false;

         for (int var6 = 0; var6 < 2; var6++) {
            BlockPos var7 = BlockPos.ofFloored(var1.getX(), var1.getY() + (double)var6, var1.getZ());
            ArrayList var8 = this.method1449(var7, null);
            if (var8 != null) {
               var5.addAll(var8);
            }

            for (Direction var12 : Direction.values()) {
               if (var12.getAxis() != Axis.Y) {
                  var8 = this.method1449(var7.offset(var12), var12.getOpposite());
                  if (var8 != null) {
                     var5.addAll(var8);
                  }
               }
            }
         }

         Class2811.field103 = false;
         Class2811.field104 = false;
         Class2811.field99 = false;
         Class2811.field107 = true;
         return var5;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public ArrayList<mu> method1449(BlockPos blockPos, Direction filter) {
      Vec3d var6 = EntityUtil.method2144(mc.player);
      PlaceAction var7 = Class2812.method5501(blockPos, true, true, this.strictDirection.getValue(), Hand.MAIN_HAND, -1);
      if (var7 == null) {
         return null;
      } else if (!mc.world.getBlockState(blockPos).isReplaceable() && !(mc.world.getBlockState(blockPos).getBlock() instanceof BedBlock)) {
         return null;
      } else {
         Vec3d var8 = new Vec3d((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5);
         if (var6.distanceTo(var8) > (double)this.breakRange.getValue().floatValue()) {
            return null;
         } else if (var6.distanceTo(var8) > this.method1450()) {
            return null;
         } else if (!RaycastUtil.method116(var8) && var6.distanceTo(var8) > (double)this.wallRange.getValue().floatValue()) {
            return null;
         } else {
            ArrayList var9 = new ArrayList();
            Direction var10 = Direction.fromRotation(var7.method2157());

            for (Direction var14 : Direction.values()) {
               if ((filter == null || var14 == filter) && var14.getAxis() != Axis.Y && (!this.placeRotate.getValue() || var10 == var14)) {
                  BlockPos var15 = blockPos.offset(var14);
                  boolean var16 = Class2811.field101;
                  if (!this.strictPlace.getValue()) {
                     Class2811.field101 = true;
                  }

                  if (!Class2812.method5504(var15, false)) {
                     Class2811.field101 = var16;
                  } else {
                     Class2811.field101 = var16;
                     if (mc.world.getBlockState(var15).isReplaceable() || mc.world.getBlockState(var15).getBlock() instanceof BedBlock) {
                        Vec3d var17 = new Vec3d((double)blockPos.getX() + 0.5, blockPos.getY(), (double)blockPos.getZ() + 0.5);
                        Vec3d var18 = new Vec3d((double)var15.getX() + 0.5, var15.getY(), (double)var15.getZ() + 0.5);
                        double var19 = Math.max(
                           Class3069.method6005(mc.player, var17, blockPos.down(), true), Class3069.method6005(mc.player, var18, var15.down(), true)
                        );
                        if ((!this.antiSuicide.getValue() || !((double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) <= var19 + 2.0))
                           && (!(this.placeSelfDamage.getValue() > 0.0F) || !(var19 > (double)this.placeSelfDamage.getValue().floatValue()))) {
                           if (!this.placeRotate.getValue()) {
                              float var21 = switch (mt.field2111[var14.ordinal()]) {
                                 case 1 -> -90.0F;
                                 case 2 -> 0.0F;
                                 case 3 -> 90.0F;
                                 default -> 180.0F;
                              };
                              var7.method2158(var21);
                           }

                           var9.add(new mu(this, blockPos, var15, var7, var19));
                        }
                     }
                  }
               }
            }

            return var9.isEmpty() ? null : var9;
         }
      }
   }

   private double method1450() {
      return (double)this.strictPlaceRange.getValue().floatValue() > 0.0
         ? (double)this.strictPlaceRange.getValue().floatValue()
         : (double)this.placeRange.getValue().floatValue();
   }

   private static boolean lambda$getBedSlot$13(ItemStack var0) {
      return var0.getItem() instanceof BlockItem && ((BlockItem)var0.getItem()).getBlock() == Blocks.CRAFTING_TABLE;
   }

   private void lambda$getBedSlot$12(Vec3d var1, BlockPos var2) {
      Class5913.method17(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND, new BlockHitResult(var1, Direction.UP, var2, false));
      if (this.swing.getValue()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND));
      }
   }

   private void lambda$getBedSlot$11(Vec3d var1, BlockPos var2) {
      Class5913.method17(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND, new BlockHitResult(var1, Direction.UP, var2, false));
      if (this.swing.getValue()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND));
      }
   }

   private static void lambda$getBedSlot$10() {
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 0, 0, SlotActionType.QUICK_MOVE, mc.player);
   }

   private static void lambda$getBedSlot$9(int var0) {
      boolean var4 = mc.player.currentScreenHandler.getCursorStack().isEmpty();
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(0, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 4, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 5, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 6, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(2, 0), SlotActionType.QUICK_CRAFT, mc.player);
      if (var4 && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      }
   }

   private static void lambda$getBedSlot$8(int var0) {
      boolean var4 = mc.player.currentScreenHandler.getCursorStack().isEmpty();
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(0, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 1, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 2, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 3, ScreenHandler.packQuickCraftData(1, 0), SlotActionType.QUICK_CRAFT, mc.player);
      mc.interactionManager
         .clickSlot(mc.player.currentScreenHandler.syncId, -999, ScreenHandler.packQuickCraftData(2, 0), SlotActionType.QUICK_CRAFT, mc.player);
      if (var4 && !mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
         mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, SlotUtils.method1541(var0), 0, SlotActionType.PICKUP, mc.player);
      }
   }

   private static boolean lambda$getBedSlot$7(ItemStack var0) {
      if (var0.getCount() < 3) {
         return false;
      } else if (!(var0.getItem() instanceof BlockItem)) {
         return false;
      } else {
         Block var4 = ((BlockItem)var0.getItem()).getBlock();
         return var4 == Blocks.OAK_PLANKS
            || var4 == Blocks.ACACIA_PLANKS
            || var4 == Blocks.BIRCH_PLANKS
            || var4 == Blocks.CRIMSON_PLANKS
            || var4 == Blocks.JUNGLE_PLANKS
            || var4 == Blocks.DARK_OAK_PLANKS
            || var4 == Blocks.SPRUCE_PLANKS
            || var4 == Blocks.WARPED_PLANKS;
      }
   }

   private static boolean lambda$getBedSlot$6(ItemStack var0) {
      return var0.getItem() instanceof BlockItem
         && var0.getCount() >= 3
         && ((BlockItem)var0.getItem()).getBlock().getDefaultState().getSoundGroup() == BlockSoundGroup.WOOL;
   }

   private static boolean lambda$getBedSlot$5(ItemStack var0) {
      return var0.getItem() instanceof BedItem;
   }

   private void lambda$generateBreak$4(Vec3d var1, BlockPos var2) {
      Class5913.method17(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND, new BlockHitResult(var1, Direction.UP, var2, false));
      if (this.swing.getValue()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND));
      }
   }

   private void lambda$generateBreak$3(Vec3d var1, BlockPos var2) {
      Class5913.method17(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND, new BlockHitResult(var1, Direction.UP, var2, false));
      if (this.swing.getValue()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(this.method1445() ? Hand.MAIN_HAND : Hand.OFF_HAND));
      }
   }

   private static Float lambda$getTargetsInRange$2(LivingEntity var0) {
      return var0.distanceTo(mc.player);
   }

   private static boolean lambda$onRotateEvent$1(ItemStack var0) {
      return var0.getItem() instanceof BedItem;
   }

   private void lambda$onRender$0(Render3DEvent var1, mu var2, Long var3) {
      if (System.currentTimeMillis() - var3 > (long)PlaceRender.method2010()) {
         this.aw.remove(var2);
      } else {
         PlaceRender.INSTANCE
            .method2014(
               var1,
               new Box(var2.field2509).shrink(0.0, 0.5625, 0.0).union(new Box(var2.field2510).shrink(0.0, 0.5625, 0.0)),
               var3,
               this.fillColor.getValue(),
               this.outlineColor.getValue()
            );
      }
   }
}
