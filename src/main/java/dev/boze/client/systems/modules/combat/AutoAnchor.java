package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.misc.autotool.qG;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.PlaceAction;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class2811;
import mapped.Class2812;
import mapped.Class3069;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class AutoAnchor extends Module {
   public static final AutoAnchor INSTANCE = new AutoAnchor();
   private final BooleanSetting multitask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final EnumSetting<AnticheatMode> mode = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Interaction Mode");
   private final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Rotate");
   private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing hand");
   private final BooleanSetting strictDirection = new BooleanSetting("StrictDirection", true, "Strict direction");
   private final BooleanSetting sequential = new BooleanSetting("Sequential", true, "Sequential placements");
   private final IntSetting charges = new IntSetting("Charges", 0, 0, 3, 1, "Charges to start placing at", this.sequential::method419);
   private final BooleanSetting instant = new BooleanSetting("Instant", false, "Instantly explode anchors after one charge");
   private final FloatSetting speed = new FloatSetting("Speed", 20.0F, 1.0F, 20.0F, 0.1F, "Anchoring speed");
   private final FloatSetting range = new FloatSetting("Range", 4.0F, 2.0F, 8.0F, 0.5F, "Range within which to target players");
   private final FloatSetting placeRange = new FloatSetting("PlaceRange", 4.5F, 3.0F, 6.0F, 0.1F, "Place range");
   private final BooleanSetting mineObstruction = new BooleanSetting("MineObstructions", true, "Mine damage obstructing blocks in/around target");
   private final FloatSetting minDamage = new FloatSetting("MinDamage", 5.0F, 0.0F, 10.0F, 0.5F, "Min damage for anchors");
   private final FloatSetting minHealth = new FloatSetting("MinHealth", 2.0F, 0.0F, 10.0F, 0.5F, "Min health for AutoAnchor to work");
   private final FloatSetting maxSelfDamage = new FloatSetting("MaxSelfDamage", 12.0F, 0.0F, 30.0F, 0.5F, "Max self damage");
   private final Timer field2501 = new Timer();
   private Vec3d field2502;
   private Timer field2503 = new Timer();
   private PlayerEntity field2504 = null;
   private final Timer field2505 = new Timer();
   private Runnable field2506 = null;
   private Runnable field2507 = null;
   private Runnable field2508 = null;

   public AutoAnchor() {
      super("AutoAnchor", "Automatically attacks enemies using anchor explosions", Category.Combat);
   }

   @Override
   public String method1322() {
      return this.field2504 != null && !this.field2505.hasElapsed(1000.0) ? this.field2504.getNameForScoreboard() : "";
   }

   @EventHandler(
      priority = 48
   )
   private void method1431(ACRotationEvent var1) {
      if (!var1.method1018(this.mode.method461(), this.rotate.method419())) {
         if (!mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            if (!Options.method477(this.multitask.method419())) {
               if (!(mc.player.getHealth() + mc.player.getAbsorptionAmount() < this.minHealth.method423())) {
                  if (this.field2501.hasElapsed((double)(1000.0F - this.speed.method423() * 50.0F))) {
                     this.field2504 = null;
                     int var5 = InventoryHelper.method163(Blocks.GLOWSTONE);
                     int var6 = InventoryHelper.method163(Blocks.RESPAWN_ANCHOR);
                     if (var5 != -1 && var6 != -1) {
                        List var7 = this.method1434();
                        if (!var7.isEmpty()) {
                           this.method1433(var7, var5, var6);
                           if (this.field2508 == null && this.field2506 == null && this.mineObstruction.method419()) {
                              BlockPos var8 = ((PlayerEntity)var7.get(0)).getBlockPos().up(2);
                              Vec3d var9 = qG.method735(var8, (double)this.range.method423().floatValue(), true);
                              if (var9 != null) {
                                 this.field2502 = var9;
                                 this.field2503.reset();
                              }
                           }

                           if (this.rotate.method419() && this.field2502 != null && !this.field2503.hasElapsed(500.0)) {
                              float[] var10 = EntityUtil.method2146(this.field2502);
                              var1.yaw = var10[0];
                              var1.pitch = var10[1];
                              var1.method1021(true);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1432(RotationEvent event) {
      if (!event.method554(RotationMode.Sequential)) {
         if (this.field2506 != null) {
            this.field2506.run();
            this.field2506 = null;
            this.field2505.reset();
         }

         if (this.field2508 != null) {
            this.field2508.run();
            this.field2508 = null;
            this.field2505.reset();
         }
      }
   }

   private boolean method1433(List<PlayerEntity> var1, int var2, int var3) {
      Vec3d var7 = EntityUtil.method2144(mc.player);

      for (PlayerEntity var9 : var1) {
         Box var10 = var9.getBoundingBox();
         ArrayList var11 = new ArrayList(
            Arrays.asList(
               BlockPos.ofFloored(var10.minX - 1.0, var10.minY, var10.minZ),
               BlockPos.ofFloored(var10.minX, var10.minY, var10.minZ - 1.0),
               BlockPos.ofFloored(var10.maxX + 1.0, var10.minY, var10.minZ),
               BlockPos.ofFloored(var10.maxX, var10.minY, var10.minZ - 1.0),
               BlockPos.ofFloored(var10.minX - 1.0, var10.minY, var10.maxZ),
               BlockPos.ofFloored(var10.minX, var10.minY, var10.maxZ + 1.0),
               BlockPos.ofFloored(var10.maxX + 1.0, var10.minY, var10.maxZ),
               BlockPos.ofFloored(var10.maxX, var10.minY, var10.maxZ + 1.0),
               BlockPos.ofFloored(var10.minX - 1.0, var10.minY + 1.0, var10.minZ),
               BlockPos.ofFloored(var10.minX, var10.minY + 1.0, var10.minZ - 1.0),
               BlockPos.ofFloored(var10.maxX + 1.0, var10.minY + 1.0, var10.minZ),
               BlockPos.ofFloored(var10.maxX, var10.minY + 1.0, var10.minZ - 1.0),
               BlockPos.ofFloored(var10.minX - 1.0, var10.minY + 1.0, var10.maxZ),
               BlockPos.ofFloored(var10.minX, var10.minY + 1.0, var10.maxZ + 1.0),
               BlockPos.ofFloored(var10.maxX + 1.0, var10.minY + 1.0, var10.maxZ),
               BlockPos.ofFloored(var10.maxX, var10.minY + 1.0, var10.maxZ + 1.0),
               BlockPos.ofFloored(var10.minX, var10.minY + 2.0, var10.minZ),
               BlockPos.ofFloored(var10.minX, var10.minY + 2.0, var10.maxZ),
               BlockPos.ofFloored(var10.maxX, var10.minY + 2.0, var10.minZ),
               BlockPos.ofFloored(var10.maxX, var10.minY + 2.0, var10.maxZ)
            )
         );
         BlockPos var12 = null;
         Vec3d var13 = null;
         Direction var14 = null;
         PlaceAction var15 = null;
         double var16 = (double)this.minDamage.method423().floatValue() - 0.1;

         for (BlockPos var19 : var11) {
            if (mc.world.getBlockState(var19).getBlock() == Blocks.RESPAWN_ANCHOR || Class2812.method5504(var19, this.strictDirection.method419())) {
               Vec3d var20 = new Vec3d((double)var19.getX() + 0.5, (double)var19.getY() + 0.5, (double)var19.getZ() + 0.5);
               double var21 = Class3069.method6006(mc.player, var20, var19);
               if (!(
                  var21
                     > (double)Math.min(mc.player.getHealth() + mc.player.getAbsorptionAmount() - this.minHealth.method423(), this.maxSelfDamage.method423())
               )) {
                  Class2811.field109 = true;
                  PlaceAction var23 = Class2812.method5502(
                     var19, this.rotate.method419(), this.swing.method419(), this.strictDirection.method419(), false, Hand.MAIN_HAND, var3
                  );
                  Class2811.field109 = false;
                  if (var23 != null) {
                     double var24 = Class3069.method6006(var9, var20, var19);
                     if (var12 == null || var24 > var16) {
                        for (Direction var29 : Direction.values()) {
                           Vec3d var30 = var20.add(new Vec3d(var29.getUnitVector()).multiply(0.5));
                           double var31 = var7.distanceTo(var30);
                           if (!(var31 > (double)this.placeRange.method423().floatValue())) {
                              var12 = var19;
                              var13 = var30;
                              var14 = var29;
                              var15 = var23;
                              var16 = var24;
                           }
                        }
                     }
                  }
               }
            }
         }

         if (var12 != null) {
            this.field2501.reset();
            if (this.rotate.method419()) {
               this.field2502 = var13;
               this.field2503.reset();
            }

            this.field2504 = var9;
            if (mc.world.getBlockState(var12).getBlock() == Blocks.RESPAWN_ANCHOR) {
               if (this.instant.method419() && this.field2507 != null) {
                  this.field2506 = this.field2507;
                  this.field2507 = null;
               } else {
                  this.field2506 = this::lambda$generateActions$0;
                  if (this.instant.method419()) {
                     this.field2507 = this::lambda$generateActions$1;
                  }
               }
            }

            if (mc.world.getBlockState(var12).getBlock() != Blocks.RESPAWN_ANCHOR
               || this.sequential.method419() && (Integer)mc.world.getBlockState(var12).get(RespawnAnchorBlock.CHARGES) >= this.charges.method434()) {
               this.field2508 = AutoAnchor::lambda$generateActions$2;
            }

            return true;
         }
      }

      return false;
   }

   private List<PlayerEntity> method1434() {
      ArrayList var4 = new ArrayList();

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof PlayerEntity
            && this.method1435(var6)
            && !(var6.distanceTo(mc.player) > this.range.method423())
            && !((PlayerEntity)var6).isDead()
            && !(((PlayerEntity)var6).getHealth() + ((PlayerEntity)var6).getAbsorptionAmount() <= 0.0F)) {
            var4.add((PlayerEntity)var6);
         }
      }

      return (List<PlayerEntity>)var4.stream().sorted(Comparator.comparing(AutoAnchor::lambda$getTargetsInRange$3)).limit(3L).collect(Collectors.toList());
   }

   private boolean method1435(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else {
            return var1 instanceof FakePlayerEntity ? false : !Friends.method2055(var1);
         }
      } else {
         return false;
      }
   }

   private int method1436() {
      int var4 = -1;

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = mc.player.getInventory().getStack(var5);
         if (var6.getItem() != Items.RESPAWN_ANCHOR && var6.getItem() != Items.GLOWSTONE) {
            var4 = var5;
            break;
         }
      }

      return var4;
   }

   private static Float lambda$getTargetsInRange$3(PlayerEntity var0) {
      return var0.distanceTo(mc.player);
   }

   private static void lambda$generateActions$2(PlaceAction var0) {
      var0.method2167().run();
   }

   private void lambda$generateActions$1(Vec3d var1, Direction var2, BlockPos var3) {
      boolean var7 = false;
      if (mc.player.getMainHandStack().getItem() == Items.GLOWSTONE || mc.player.getMainHandStack().getItem() == Items.RESPAWN_ANCHOR) {
         if (mc.player.getOffHandStack().getItem() != Items.GLOWSTONE && mc.player.getOffHandStack().getItem() != Items.RESPAWN_ANCHOR) {
            var7 = true;
         } else {
            int var8 = this.method1436();
            if (var8 != -1) {
               mc.player.getInventory().selectedSlot = var8;
               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
            }
         }
      }

      mc.getNetworkHandler().sendPacket(Class5913.method17(var7 ? Hand.OFF_HAND : Hand.MAIN_HAND, new BlockHitResult(var1, var2, var3, false)));
      if (this.swing.method419()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(var7 ? Hand.OFF_HAND : Hand.MAIN_HAND));
      }
   }

   private void lambda$generateActions$0(int var1, Vec3d var2, Direction var3, BlockPos var4) {
      if (mc.player.getInventory().selectedSlot != var1) {
         mc.player.getInventory().selectedSlot = var1;
         ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
      }

      mc.getNetworkHandler().sendPacket(Class5913.method17(Hand.MAIN_HAND, new BlockHitResult(var2, var3, var4, false)));
      if (this.swing.method419()) {
         mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
      }
   }
}
