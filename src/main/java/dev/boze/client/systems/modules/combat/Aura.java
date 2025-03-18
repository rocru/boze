package dev.boze.client.systems.modules.combat;

import baritone.api.BaritoneAPI;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AuraSwapMode;
import dev.boze.client.enums.AutoSwapMode;
import dev.boze.client.enums.CritMode;
import dev.boze.client.enums.DelayMode;
import dev.boze.client.enums.InteractionMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.enums.TargetMode;
import dev.boze.client.enums.TargetPriority;
import dev.boze.client.enums.TpsSyncMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PreTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.events.eJ;
import dev.boze.client.jumptable.ms;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.GhostModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.aura.AuraGhost;
import dev.boze.client.systems.modules.misc.AutoEat;
import dev.boze.client.systems.modules.misc.FakePlayer;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.PiglinAggressiveness;
import dev.boze.client.utils.PositionUtils;
import dev.boze.client.utils.RaycastUtil;
import dev.boze.client.utils.TargetTracker;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.player.RotationHandler;
import dev.boze.client.utils.trackers.TickRateTracker;
import java.util.ArrayList;
import java.util.Comparator;
import mapped.Class2839;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Aura extends Module {
   public static final Aura INSTANCE = new Aura();
   public final EnumSetting<InteractionMode> mode = new EnumSetting<InteractionMode>("Mode", InteractionMode.NCP, "Interaction mode", Aura::lambda$new$0);
   private final AuraGhost field2459 = new AuraGhost(this);
   public final BooleanSetting render = new BooleanSetting("Render", true, "Render target");
   public final ColorSetting color = new ColorSetting("Color", new BozeDrawColor(1691624484), "Color for fill", this.render);
   public final ColorSetting outline = new ColorSetting("Outline", new BozeDrawColor(-2874332), "Color for outline", this.render);
   private final BooleanSetting multitask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Rotate");
   private final BooleanSetting grimSilent = new BooleanSetting("GrimSilent", false, "Silent rotate mode for Grim", this::lambda$new$1);
   private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing");
   private final BooleanSetting infinite = new BooleanSetting(
      "Infinite", false, "Infinite mode\nUses paper clip exploit to tp to entities and attack them\nThis won't work on most servers with anti-cheats\n"
   );
   private final MinMaxSetting clipRange = new MinMaxSetting("ClipRange", 20.0, 8.0, 200.0, 0.1, "Range for paper clip exploit", this.infinite::method419);
   private final MinMaxSetting range = new MinMaxSetting("Range", 4.5, 0.5, 6.0, 0.1, "Max range");
   private final MinMaxSetting wallsRange = new MinMaxSetting("WallsRange", 1.5, 0.5, 6.0, 0.1, "Max range through walls", this::lambda$new$2);
   private final BooleanSetting raycast = new BooleanSetting("RayCast", true, "Ray cast look to check if looking at target entity", this::lambda$new$3);
   public final BooleanSetting yawStep = new BooleanSetting("YawStep", false, "Slow down rotations");
   public final FloatSetting yawAngle = new FloatSetting("YawAngle", 0.3F, 0.1F, 1.0F, 0.05F, "Maximum angle fraction to rotate by per tick", this.yawStep);
   public final IntSetting yawTicks = new IntSetting("YawTicks", 1, 1, 5, 1, "Rotate slower by this amount of ticks", this.yawStep);
   private final EnumSetting<TargetMode> target = new EnumSetting<TargetMode>("Target", TargetMode.Health, "Target algorithm");
   private final EnumSetting<TargetPriority> priority = new EnumSetting<TargetPriority>("Priority", TargetPriority.Lowest, "Target priority");
   private final EnumSetting<DelayMode> delay = new EnumSetting<DelayMode>("Delay", DelayMode.Dynamic, "Delay mode");
   private final EnumSetting<CritMode> awaitCrits = new EnumSetting<CritMode>(
      "AwaitCrits",
      CritMode.Off,
      "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n"
   );
   private final EnumSetting<TpsSyncMode> tpsSync = new EnumSetting<TpsSyncMode>("TPSSync", TpsSyncMode.Avg, "Sync delay to tick rate", this.delay);
   private final IntSetting ticks = new IntSetting("Ticks", 5, 0, 50, 1, "Tick delay for hitting enemies", this::lambda$new$4, this.delay);
   private final EnumSetting<AuraSwapMode> swap = new EnumSetting<AuraSwapMode>("Swap", AuraSwapMode.Off, "Auto Swap mode");
   public final FloatSetting swapDelay = new FloatSetting("Delay", 10.0F, 0.0F, 20.0F, 1.0F, "Delay to wait after swapping", this::lambda$new$5, this.swap);
   private final EnumSetting<AutoSwapMode> swapWeapon = new EnumSetting<AutoSwapMode>("Weapon", AutoSwapMode.Sword, "Weapon to swap to", this.swap);
   private final BooleanSetting pauseBaritone = new BooleanSetting("PauseBaritone", true, "Pause Baritone if it's pathfinding when fighting");
   private final BooleanSetting disableOnDeath = new BooleanSetting("DisableOnDeath", true, "Disable when you die");
   private final BooleanSetting checkAggression = new BooleanSetting("CheckAggression", true, "Checks if mob is angry at you before attacking");
   private final SettingCategory targets = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting players = new BooleanSetting("Players", true, "Target players", this.targets);
   private final BooleanSetting friends = new BooleanSetting("Friends", false, "Target friends", this.targets);
   private final BooleanSetting animals = new BooleanSetting("Animals", false, "Target animals", this.targets);
   private final BooleanSetting monsters = new BooleanSetting("Monsters", false, "Target monsters", this.targets);
   private final BooleanSetting armorStands = new BooleanSetting("ArmorStands", false, "Target armor stands", this.targets);
   private final BooleanSetting boots = new BooleanSetting("Boats", false, "Target boats", this.targets);
   private final BooleanSetting minecarts = new BooleanSetting("Minecarts", false, "Target minecarts", this.targets);
   private Timer ai = new Timer();
   private Timer aj = new Timer();
   private boolean ak;
   private Entity al;
   private Timer am = new Timer();
   private float[] an = null;
   private boolean ao = false;

   @Override
   public GhostModule method221() {
      return this.field2459;
   }

   private double method1397() {
      return this.infinite.method419() ? this.clipRange.getValue() : this.range.getValue();
   }

   private double method1398() {
      return this.infinite.method419() ? this.clipRange.getValue() : this.wallsRange.getValue();
   }

   private boolean method1399() {
      return this.infinite.method419() ? false : this.raycast.method419();
   }

   public Aura() {
      super("Aura", "Attacks nearby enemies with sword/axe/hand", Category.Combat);
      this.addSettings(this.render, this.field2459.field2462);
   }

   @Override
   public String method1322() {
      return this.al != null ? this.al.getName().getString() : "";
   }

   @EventHandler(
      priority = 45
   )
   public void method1400(ACRotationEvent event) {
      if (!this.field2459.isGhostMode()) {
         if (!event.method1018(this.mode.method461().interactMode, this.rotate.method419())) {
            if (!AutoEat.method1663()) {
               this.method1407();
               if (this.rotate.method419()
                  && this.an != null
                  && (
                     this.method1413(mc.player.getMainHandStack().getItem())
                        || this.swap.method461() != AuraSwapMode.Off && this.swap.method461() != AuraSwapMode.OnlyWeapon && this.method1412() != -1
                  )) {
                  if (this.mode.method461() == InteractionMode.Grim && this.grimSilent.method419() && !this.method1409()) {
                     return;
                  }

                  if (this.yawStep.method419()) {
                     float var5 = MathHelper.wrapDegrees(this.an[0] - ((ClientPlayerEntityAccessor)mc.player).getLastYaw());
                     if (Math.abs(var5) > 180.0F * this.yawAngle.method423()) {
                        this.an[0] = ((ClientPlayerEntityAccessor)mc.player).getLastYaw() + var5 * (180.0F * this.yawAngle.method423() / Math.abs(var5));
                     }
                  }

                  event.method1021(true);
                  event.yaw = this.an[0];
                  event.pitch = this.an[1];
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 45
   )
   public void method1401(eJ event) {
      if (this.field2459.isGhostMode()) {
         this.field2459.method1417(event);
      }
   }

   @EventHandler
   public void method1402(PreTickEvent event) {
      if (this.field2459.isGhostMode()) {
         this.field2459.method1420(event);
      }
   }

   @EventHandler(
      priority = 45
   )
   public void method1403(RotationEvent event) {
      if (!AutoEat.method1663()) {
         if (this.field2459.isGhostMode()) {
            if (event.field1284 == RotationMode.Vanilla) {
               this.field2459.method1421(event);
            }
         } else if (!event.method555(RotationMode.Sequential, this.rotate.method419())) {
            this.method1408();
         }
      }
   }

   @EventHandler
   private void method1404(Render3DEvent var1) {
      if (this.field2459.isGhostMode()) {
         this.field2459.method1422(var1);
      } else {
         if (this.render.method419() && this.al != null && !this.am.hasElapsed(2500.0)) {
            double var5 = MathHelper.lerp((double)var1.field1951, this.al.lastRenderX, this.al.getX()) - this.al.getX();
            double var7 = MathHelper.lerp((double)var1.field1951, this.al.lastRenderY, this.al.getY()) - this.al.getY();
            double var9 = MathHelper.lerp((double)var1.field1951, this.al.lastRenderZ, this.al.getZ()) - this.al.getZ();
            Box var11 = this.al.getBoundingBox();
            if (var11 != null) {
               var1.field1950
                  .method1271(
                     var5 + var11.minX,
                     var7 + var11.minY,
                     var9 + var11.minZ,
                     var5 + var11.maxX,
                     var7 + var11.maxY,
                     var9 + var11.maxZ,
                     this.color.method1362(),
                     this.outline.method1362(),
                     ShapeMode.Full,
                     0
                  );
            }
         }
      }
   }

   @Override
   public void onEnable() {
      this.field2459.method1415();
   }

   @Override
   public void onDisable() {
      if (this.ak) {
         try {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
         } catch (Exception var5) {
         }
      }

      this.ak = false;
      this.an = null;
      this.field2459.method1416();
   }

   @EventHandler
   public void method1405(PacketBundleEvent event) {
      if (this.field2459.isGhostMode()) {
         this.field2459.method1419(event);
      }
   }

   @EventHandler
   public void method1406(OpenScreenEvent event) {
      if (this.field2459.isGhostMode()) {
         this.field2459.method1418(event);
      } else {
         if (event.screen instanceof DeathScreen && this.disableOnDeath.method419()) {
            this.setEnabled(false);
         }
      }
   }

   private void method1407() {
      Entity var4 = this.method1410();
      this.al = var4;
      if (var4 == null) {
         if (this.ak) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("resume");
            this.ak = false;
         }

         this.an = null;
      } else {
         TargetTracker.method488(var4);
         this.an = EntityUtil.method2146(var4.getEyePos());
      }
   }

   private boolean method1408() {
      if (Options.method477(this.multitask.method419())) {
         return false;
      } else if (this.al == null) {
         return false;
      } else {
         Vec3d var4 = mc.player.getEyePos();
         double var5 = var4.distanceTo(this.al.getEyePos());
         if (this.mode.method461().interactMode == AnticheatMode.Grim && this.rotate.method419() && this.method1399()) {
            Vec3d var7 = RotationHandler.method1954().multiply(this.method1397());
            Vec3d var8 = var4.add(var7);
            if (var5 > this.method1398()) {
               Box var9 = mc.player.getBoundingBox().stretch(var7).expand(1.0, 1.0, 1.0);
               EntityHitResult var10 = ProjectileUtil.raycast(mc.player, var4, var8, var9, Aura::lambda$handle$6, this.method1397());
               if (var10 == null) {
                  return false;
               }

               if (var10.getEntity() != this.al) {
                  return false;
               }
            } else {
               Box var17 = this.al.getBoundingBox();
               if (!var17.intersects(var4, var8)) {
                  return false;
               }
            }
         }

         int var14 = -1;
         if (this.swap.method461() != AuraSwapMode.Off && this.swap.method461() != AuraSwapMode.OnlyWeapon) {
            var14 = this.method1412();
            if (mc.player.getInventory().selectedSlot != var14 && var14 != -1 && this.swap.method461() != AuraSwapMode.Silent) {
               if (this.swap.method461() == AuraSwapMode.Normal) {
                  Class2839.field111 = var14;
               }

               mc.player.getInventory().selectedSlot = var14;
               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
               var14 = -1;
            }
         }

         if (!this.method1409()) {
            return false;
         } else {
            if (this.pauseBaritone.method419() && BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().isPathing() && !this.ak) {
               BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("pause");
               this.ak = true;
            }

            int var16 = -1;
            if (var14 != -1 && this.swap.method461() == AuraSwapMode.Silent) {
               var16 = mc.player.getInventory().selectedSlot;
               mc.player.getInventory().selectedSlot = var14;
               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
               byte var15 = -1;
            }

            BlockPos var18 = mc.player.getBlockPos();
            Vec3d var19 = mc.player.getPos();
            if (this.infinite.method419() && var5 > this.range.getValue()) {
               BlockPos var11 = BlockPos.ofFloored(this.al.getPos()).down();
               Vec3d var12 = new Vec3d((double)var11.getX() + 0.5, (double)var11.getY() + 1.0, (double)var11.getZ() + 0.5);
               BlockHitResult var13 = new BlockHitResult(var12, Direction.UP, var11, false);
               PositionUtils.method393(var13, false);
            }

            if (this.al == FakePlayer.INSTANCE.fakePlayer) {
               mc.player.attack(this.al);
            } else {
               mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(this.al, mc.player.isSneaking()));
            }

            if (this.infinite.method419() && var5 > this.range.getValue()) {
               BlockHitResult var20 = new BlockHitResult(var19, Direction.UP, var18, false);
               PositionUtils.method393(var20, true);
            }

            this.aj.reset();
            this.am.reset();
            if (this.swing.method419()) {
               mc.player.swingHand(Hand.MAIN_HAND);
            }

            mc.player.resetLastAttackedTicks();
            if (var16 != -1) {
               mc.player.getInventory().selectedSlot = var16;
            }

            return true;
         }
      }
   }

   private boolean method1409() {
      if (!this.ai.hasElapsed((double)(this.swapDelay.method423() * 50.0F))) {
         return false;
      } else if (!this.method1413(mc.player.getMainHandStack().getItem())) {
         return false;
      } else if (this.delay.method461() == DelayMode.Tick && !this.aj.hasElapsed((double)((float)this.ticks.method434().intValue() * 50.0F))) {
         return false;
      } else {
         if (this.delay.method461() == DelayMode.Dynamic) {
            float var4 = 0.0F;
            if (this.tpsSync.method461() == TpsSyncMode.Avg) {
               var4 = 20.0F - TickRateTracker.getAverageTickRate();
            } else if (this.tpsSync.method461() == TpsSyncMode.Min) {
               var4 = 20.0F - TickRateTracker.getMinTickRate();
            } else if (this.tpsSync.method461() == TpsSyncMode.Last) {
               var4 = 20.0F - TickRateTracker.getLastTickRate();
            }

            if (mc.player.getAttackCooldownProgress(-var4) < 1.0F) {
               return false;
            }
         }

         return !this.awaitCrits.method461().method2114();
      }
   }

   private Entity method1410() {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      boolean var6 = true;

      for (Entity var8 : mc.world.getEntities()) {
         if (!(var8 instanceof LivingEntity)
            && (this.minecarts.method419() && var8 instanceof MinecartEntity || this.boots.method419() && var8 instanceof BoatEntity)) {
            if (var8.getEyePos().distanceTo(mc.player.getEyePos()) > this.method1397()
               || var8.getEyePos().distanceTo(mc.player.getEyePos()) > this.method1398() && !RaycastUtil.method2055(var8)) {
               continue;
            }

            var5.add(var8);
            var6 = false;
         }

         if (this.method1411(var8)
            && !(var8.getEyePos().distanceTo(mc.player.getEyePos()) > this.method1397())
            && (!(var8.getEyePos().distanceTo(mc.player.getEyePos()) > this.method1398()) || RaycastUtil.method2055(var8))
            && !((LivingEntity)var8).isDead()
            && !(((LivingEntity)var8).getHealth() + ((LivingEntity)var8).getAbsorptionAmount() <= 0.0F)) {
            var4.add((LivingEntity)var8);
            var5.add(var8);
         }
      }

      if (this.priority.method461() == TargetPriority.Highest) {
         return this.target.method461() == TargetMode.Health && var6
            ? (Entity)var4.stream().max(Comparator.comparing(Aura::lambda$getTarget$7)).orElse(null)
            : (Entity)var5.stream().max(Comparator.comparing(Aura::lambda$getTarget$8)).orElse(null);
      } else {
         return this.target.method461() == TargetMode.Health && var6
            ? (Entity)var4.stream().min(Comparator.comparing(Aura::lambda$getTarget$9)).orElse(null)
            : (Entity)var5.stream().min(Comparator.comparing(Aura::lambda$getTarget$10)).orElse(null);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1411(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (((PlayerEntity)var1).isCreative()) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else {
            return Friends.method2055(var1) ? this.friends.method419() : this.players.method419();
         }
      } else {
         if (this.checkAggression.method419()) {
            if (var1 instanceof EndermanEntity var5 && !var5.isAngryAt(mc.player)) {
               return false;
            }

            if (var1 instanceof ZombifiedPiglinEntity var6 && !PiglinAggressiveness.field3917) {
               return false;
            }

            if (var1 instanceof WolfEntity var7 && !var7.isAttacking()) {
               return false;
            }

            if (var1 instanceof LlamaEntity var8 && !var8.isAttacking()) {
               return false;
            }
         }

         if (var1 instanceof ArmorStandEntity && this.armorStands.method419()) {
            return true;
         } else {
             return switch (ms.field2109[var1.getType().getSpawnGroup().ordinal()]) {
                 case 1, 2, 3, 4 -> this.animals.method419();
                 case 5 -> this.monsters.method419();
                 default -> false;
             };
         }
      }
   }

   private int method1412() {
      int var4 = -1;
      ItemStack var5 = mc.player.getMainHandStack();
      if (var5 != ItemStack.EMPTY && this.method1414(var5.getItem())) {
         var4 = mc.player.getInventory().selectedSlot;
      }

      if (var4 == -1) {
         for (int var6 = 0; var6 < 9; var6++) {
            ItemStack var7 = mc.player.getInventory().getStack(var6);
            if (var7 != ItemStack.EMPTY && this.method1414(var7.getItem())) {
               var4 = var6;
               break;
            }
         }
      }

      return var4;
   }

   private boolean method1413(Item var1) {
      return this.swap.method461() != AuraSwapMode.OnlyWeapon
         || var1 instanceof SwordItem && this.swapWeapon.method461() != AutoSwapMode.Axe
         || var1 instanceof AxeItem && this.swapWeapon.method461() != AutoSwapMode.Sword;
   }

   private boolean method1414(Item var1) {
      return var1 instanceof SwordItem && this.swapWeapon.method461() != AutoSwapMode.Axe
         || var1 instanceof AxeItem && this.swapWeapon.method461() != AutoSwapMode.Sword;
   }

   private static Float lambda$getTarget$10(Entity var0) {
      return var0.distanceTo(mc.player);
   }

   private static Float lambda$getTarget$9(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private static Float lambda$getTarget$8(Entity var0) {
      return var0.distanceTo(mc.player);
   }

   private static Float lambda$getTarget$7(LivingEntity var0) {
      return var0.getHealth() + var0.getAbsorptionAmount();
   }

   private static boolean lambda$handle$6(Entity var0) {
      return !var0.isSpectator() && var0.canHit();
   }

   private boolean lambda$new$5() {
      return this.swap.method461() != AuraSwapMode.Silent;
   }

   private boolean lambda$new$4() {
      return this.delay.method461() == DelayMode.Tick;
   }

   private boolean lambda$new$3() {
      return !this.infinite.method419();
   }

   private boolean lambda$new$2() {
      return !this.infinite.method419();
   }

   private boolean lambda$new$1() {
      return this.rotate.method419() && this.mode.method461() == InteractionMode.Grim;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
