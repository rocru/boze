package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.ClickMethod;
import dev.boze.client.enums.CritMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.nc;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.PiglinAggressiveness;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import mapped.Class5918;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class Trigger extends Module {
   public static final Trigger INSTANCE = new Trigger();
   private final FloatSetting field2833 = new FloatSetting("Jitter", 0.0F, 0.0F, 2.0F, 0.01F, "Mouse jitter amount");
   private final BooleanSetting field2834 = new BooleanSetting("OnlyWeapon", true, "Only attack when holding a weapon");
   private final BooleanSetting field2835 = new BooleanSetting("OnlyWhenHolding", false, "Only attack when holding left click");
   private final BooleanSetting field2836 = new BooleanSetting("NoShield", false, "Don't attack when blocking with a shield");
   private final EnumSetting<CritMode> field2837 = new EnumSetting<CritMode>(
      "AwaitCrits",
      CritMode.Off,
      "Only attack when you can crit\n - Off: Don't wait for crits\n - Normal: Wait for crits when in air\n - Force: Always wait for crits\n"
   );
   private final EnumSetting<ClickMethod> field2838 = new EnumSetting<ClickMethod>("Mode", ClickMethod.Normal, "Click Mode");
   private final IntArraySetting field2839 = new IntArraySetting("CPS", new int[]{6, 10}, 1, 20, 1, "Clicks per second");
   private final FloatSetting field2840 = new FloatSetting("CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$0);
   private final MinMaxSetting field2841 = new MinMaxSetting("MissChance", 0.0, 0.0, 100.0, 0.1, "Artificial miss chance (%)");
   private final MinMaxDoubleSetting field2842 = new MinMaxDoubleSetting(
      "BoxScale",
      new double[]{1.0, 1.0},
      0.1,
      1.5,
      0.01,
      "Hitbox scale factor (1.0 = vanilla)\nThis doesn't actually expand the hitbox but rather the box used for raycasting"
   );
   private final BooleanSetting field2843 = new BooleanSetting(
      "Extrapolate",
      false,
      "Extrapolate your position by 1 tick\nThis can help if you are missing hits when moving quickly\n(happens because input is polled before movement is applied)\n"
   );
   private final SettingCategory field2844 = new SettingCategory("Targets", "Entities to target");
   private final BooleanSetting field2845 = new BooleanSetting("CheckAggression", true, "Check if the entity is aggressive", this.field2844);
   private final BooleanSetting field2846 = new BooleanSetting("Players", true, "Target players", this.field2844);
   private final BooleanSetting field2847 = new BooleanSetting("Friends", false, "Target friends", this.field2844);
   private final BooleanSetting field2848 = new BooleanSetting("Animals", false, "Target animals", this.field2844);
   private final BooleanSetting field2849 = new BooleanSetting("Monsters", false, "Target monsters", this.field2844);
   private final BooleanSetting field2850 = new BooleanSetting("Crystals", false, "Target crystals", this.field2844);
   private final BooleanSetting field2851 = new BooleanSetting("Minecarts", false, "Target minecarts", this.field2844);
   private final BooleanSetting field2852 = new BooleanSetting("Boats", false, "Target boats", this.field2844);
   private final ClickManager field2853 = new ClickManager(this.field2838, this.field2839, this.field2840);
   private float field2854 = 0.0F;
   private Entity field2855 = null;
   private final Timer field2856 = new Timer();
   private boolean field2857 = false;

   private Trigger() {
      super("Trigger", "Automatically attacks when you look at an entity", Category.Legit);
   }

   @Override
   public void onEnable() {
      this.field2855 = null;
      this.field2854 = 0.0F;
      this.field2853.method2172();
      this.field2857 = false;
   }

   @Override
   public String method1322() {
      return this.field2855 != null && !this.field2856.hasElapsed(2500.0) ? this.field2855.getNameForScoreboard() : super.method1322();
   }

   @EventHandler(
      priority = 57
   )
   public void method1620(MouseUpdateEvent event) {
      if (this.field2854 > 0.0F && !event.method1022()) {
         double var5 = (double)(this.field2854 * this.field2833.method423()) * Math.random();
         double var7 = (double)(this.field2854 * this.field2833.method423()) * Math.random();
         if (Math.random() > 0.5) {
            var5 *= -1.0;
         }

         if (Math.random() > 0.5) {
            var7 *= -1.0;
         }

         event.deltaX += var5;
         event.deltaY += var7;
         event.method1021(true);
         this.field2854 = 0.0F;
      }
   }

   @EventHandler
   public void method1621(RotationEvent event) {
      if (!event.method554(RotationMode.Vanilla)) {
         if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) {
            this.field2853.method2172();
         }

         if (this.field2834.method419()) {
            Item var5 = mc.player.getMainHandStack().getItem();
            if (!(var5 instanceof SwordItem) && !(var5 instanceof AxeItem) && !(var5 instanceof TridentItem)) {
               this.field2853.method2172();
               return;
            }
         }

         if (this.field2835.method419() && !mc.options.attackKey.isPressed()) {
            this.field2853.method2172();
         } else if (this.field2836.method419() && mc.player.isBlocking()) {
            this.field2853.method2172();
         } else if (this.field2837.method461().method2114()) {
            this.field2853.method2172();
         } else {
            if (mc.interactionManager.isBreakingBlock()) {
               this.field2853.method2172();
            } else {
               List var13 = this.method1624();
               if (var13.isEmpty()) {
                  this.field2853.method2172();
                  return;
               }

               Vec3d var6 = mc.player.getCameraPosVec(mc.getRenderTickCounter().getTickDelta(true));
               if (this.field2843.method419()) {
                  Pair var7 = Class5918.method38(1, mc.player);
                  if (var7 != null) {
                     Vec3d var8 = ((ClientPlayerEntity)var7.getLeft()).getPos();
                     var6 = var8.add(0.0, (double)mc.player.getEyeHeight(mc.player.getPose()), 0.0);
                  }
               }

               Vec3d var14 = this.method1623(
                     mc.player.getPitch(mc.getRenderTickCounter().getTickDelta(true)), mc.player.getYaw(mc.getRenderTickCounter().getTickDelta(true))
                  )
                  .multiply(Reach.method1613());

               for (Entity var9 : var13) {
                  if (var9 != null) {
                     Box var10 = var9.getBoundingBox();
                     if (this.field2842.method1295() > 1.0) {
                        var10 = var10.expand(this.field2842.method1295() - 1.0);
                     } else if (this.field2842.method1295() < 1.0) {
                        var10 = var10.expand(-(1.0 - this.field2842.method1295()));
                     }

                     Optional var11 = var10.raycast(var6, var6.add(var14));
                     if (!var11.isEmpty()) {
                        BlockHitResult var12 = mc.world.raycast(new RaycastContext(var6, (Vec3d)var11.get(), ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
                        if (var12 == null || var12.getType() != Type.BLOCK) {
                           if (this.method1622(mc.options.attackKey, this.field2853, event)) {
                              if (this.field2841.getValue() > 0.0 && Math.random() * 100.0 < this.field2841.getValue()) {
                                 this.field2857 = true;
                              }

                              this.field2855 = var9;
                              this.field2856.reset();
                              this.field2842.method1296();
                           }

                           return;
                        }
                     }
                  }
               }

               if (this.field2857 && this.method1622(mc.options.attackKey, this.field2853, event)) {
                  this.field2857 = false;
               }
            }
         }
      }
   }

   private boolean method1622(KeyBinding var1, ClickManager var2, RotationEvent var3) {
      int var7 = var2.method2171();
      if (var7 > 0 && ((KeyBindingAccessor)var1).getTimesPressed() == 0) {
         ((KeyBindingAccessor)var1).setTimesPressed(var7);
         var3.method2142();
         if (this.field2833.method423() > 0.0F) {
            this.field2854++;
         }

         return true;
      } else {
         return false;
      }
   }

   private final Vec3d method1623(float var1, float var2) {
      float var3 = var1 * (float) (Math.PI / 180.0);
      float var4 = -var2 * (float) (Math.PI / 180.0);
      float var5 = MathHelper.cos(var4);
      float var6 = MathHelper.sin(var4);
      float var7 = MathHelper.cos(var3);
      float var8 = MathHelper.sin(var3);
      return new Vec3d((double)(var6 * var7), (double)(-var8), (double)(var5 * var7));
   }

   private List<Entity> method1624() {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();
      boolean var6 = true;

      for (Entity var8 : mc.world.getEntities()) {
         if (!(var8 instanceof LivingEntity)
            && (
               this.field2851.method419() && var8 instanceof MinecartEntity
                  || this.field2852.method419() && var8 instanceof BoatEntity
                  || this.field2850.method419() && var8 instanceof EndCrystalEntity
            )) {
            if (var8.getEyePos().distanceTo(mc.player.getEyePos()) > Reach.method1613() + 1.4) {
               continue;
            }

            var5.add(var8);
            var6 = false;
         }

         if (this.method1625(var8)
            && !(var8.getEyePos().distanceTo(mc.player.getEyePos()) > Reach.method1613() + 1.4)
            && !((LivingEntity)var8).isDead()
            && !(((LivingEntity)var8).getHealth() + ((LivingEntity)var8).getAbsorptionAmount() <= 0.0F)) {
            var4.add((LivingEntity)var8);
            var5.add(var8);
         }
      }

      return (List<Entity>)var5.stream().sorted(Comparator.comparing(Trigger::lambda$getTargetList$1)).collect(Collectors.toList());
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method1625(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (((PlayerEntity)var1).isCreative()) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else if (Friends.method2055(var1)) {
            return this.field2847.method419();
         } else {
            return AntiBots.method2055(var1) ? false : this.field2846.method419();
         }
      } else {
         if (this.field2845.method419()) {
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

         switch (nc.field2118[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.field2848.method419();
            case 5:
               return this.field2849.method419();
            default:
               return false;
         }
      }
   }

   private static Float lambda$getTargetList$1(Entity var0) {
      return var0.distanceTo(mc.player);
   }

   private boolean lambda$new$0() {
      return this.field2838.method461() == ClickMethod.Vanilla;
   }
}
