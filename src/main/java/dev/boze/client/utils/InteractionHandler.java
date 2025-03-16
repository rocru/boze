package dev.boze.client.utils;

import dev.boze.client.ac.Grim;
import dev.boze.client.ac.NCP;
import dev.boze.client.enums.Anticheat;
import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.BlockPlaceMode;
import dev.boze.client.enums.CrystalAttackMode;
import dev.boze.client.enums.PlayerAimPoint;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.client.Options;
import java.util.ArrayList;
import java.util.HashSet;
import mapped.Class2811;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class InteractionHandler implements SettingsGroup, IMinecraft {
   final EnumSetting<Anticheat> field226 = new EnumSetting<Anticheat>(
      "Mode",
      Anticheat.Grim,
      "The anti-cheat mode to use\n - NCP: For crystalpvp.cc, 6b6t.org, etc.\n - Grim: For 2b2t.org, 2bpvp.com, etc.\n - Ghost: For legit servers\n"
   );
   public final EnumSetting<BlockPlaceMode> field227 = new EnumSetting<BlockPlaceMode>(
      "GhostRotate", BlockPlaceMode.Normal, "Ghost rotation mode\n - Normal: Rotate normally\n - Mouse: Rotate like AimAssist, using mouse\n", this::method2115
   );
   public final EnumSetting<PlayerAimPoint> field228 = new EnumSetting<PlayerAimPoint>(
      "AimPoint",
      PlayerAimPoint.Distance,
      "Aim point to us\n - Distance: Point closest to you\n - Angle: Point with the smallest angle to you\n",
      this::method2115
   );
   public final MinMaxDoubleSetting field229 = new MinMaxDoubleSetting("AimSpeed", new double[]{1.0, 2.0}, 0.1, 5.0, 0.1, "Aim speed", this::method2115);
   public final MinMaxDoubleSetting field230 = new MinMaxDoubleSetting(
      "BoxScale", new double[]{0.87, 0.93}, 0.1, 0.95, 0.05, "Scale block's bounding box to aim within\nRecommended to keep close to max\n", this::method2115
   );
   public final IntSetting field231 = new IntSetting(
      "Resolution", 3, 2, 8, 1, "Resolution of ray casting checks\nHigher = better, but more CPU usage\n", this::method2115
   );
   public final MinMaxDoubleSetting field232 = new MinMaxDoubleSetting(
      "SwapDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to swap in ticks", this::method2115
   );
   public final MinMaxDoubleSetting field233 = new MinMaxDoubleSetting(
      "PlaceDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to place in ticks", this::method2115
   );
   public final EnumSetting<AttackMode> field234 = new EnumSetting<AttackMode>(
      "Type",
      AttackMode.Packet,
      "Place type\n - Vanilla: Place blocks like Vanilla, may cause ghost blocks\n - Packet: Place blocks using packets, will never cause de-sync\n",
      this::method2114
   );
   public final BooleanSetting field235 = new BooleanSetting("Rotate", true, "Rotate to place blocks", this::lambda$new$0);
   public final MinMaxSetting field236 = new MinMaxSetting(
      "Reach", 4.5, 3.0, 6.0, 0.1, "Reach to place blocks within\n4.5 is vanilla\n5-6 may work on some servers\n", this::lambda$new$1
   );
   public final MinMaxSetting field237 = new MinMaxSetting("WallsReach", 0.0, 0.0, 6.0, 0.1, "Reach to place through walls within", this::lambda$new$2);
   protected final BooleanSetting field238 = new BooleanSetting("StrictDirection", true, "Strict direction check\nNeeded on most servers\n", this::lambda$new$3);
   public final BooleanSetting field239 = new BooleanSetting(
      "StrictVec", false, "Strict grim block placement\nNot the same as StrictDirection\nNot needed on most servers\n", this::lambda$new$4
   );
   protected final IntSetting field240 = new IntSetting(
      "PlaceRate", 4, 1, 8, 1, "Maximum blocks to place per tick\n1 is most consistent, but 3-4 tends to work on most servers\n", this::method2114
   );
   public final EnumSetting<SwapMode> field241 = new EnumSetting<SwapMode>(
      "Swap",
      SwapMode.Silent,
      "Mode for swapping to blocks\nHot-bar only modes:\n - Normal: Vanilla swap, hot-bar only\n - Silent: Instantaneously swap to block and back\nWhole inventory modes (you don't need to keep the block(s) in your hot-bar):\n - Alt: Alternative silent swap mode, may work where mode silent is patched\nNote: Whole inventory modes may not work on some servers\n",
      this::lambda$new$5
   );
   protected final EnumSetting<CrystalAttackMode> field242 = new EnumSetting<CrystalAttackMode>(
      "AttackCrystals",
      CrystalAttackMode.Sequential,
      "Attacks crystals in the way of blocks\n - Ignore: Ignore crystals in the way, place anyways\n - Off: Don't attack crystals\n - Sequential: Attack right before placing, don't rotate separately\n - SeqStrict: Attack right before placing, rotate separately\n - Strict: Attack and place in separate ticks\n",
      this::method2114
   );
   private final SettingBlock field243 = new SettingBlock(
      "Placement",
      "Block placement settings\nIn ghost mode, you can fine-tune the rotations in the GhostRotations module\n",
      this.field226,
      this.field227,
      this.field228,
      this.field229,
      this.field230,
      this.field231,
      this.field234,
      this.field235,
      this.field236,
      this.field237,
      this.field239,
      this.field238,
      this.field240,
      this.field241,
      this.field242
   );

   public Anticheat method147() {
      return Options.INSTANCE.method1971() ? Anticheat.Ghost : this.field226.method461();
   }

   public boolean method2114() {
      return this.method147() != Anticheat.Ghost;
   }

   public boolean method2115() {
      return this.method147() == Anticheat.Ghost;
   }

   public int method2010() {
      return this.method2114() && MovementUtils.method2114() ? this.field240.method434() : 1;
   }

   public final CrystalAttackMode method148() {
      return this.method2114() ? this.field242.method461() : CrystalAttackMode.Ignore;
   }

   @Override
   public Setting<?>[] get() {
      return this.field243.method472();
   }

   public boolean method2116() {
      return this.field235.method419();
   }

   public SwapMode method149() {
      if (this.method2115()) {
         return SwapMode.Normal;
      } else {
         return this.field234.method461() != AttackMode.Packet && this.field241.method461() != SwapMode.Normal && this.field241.method461() != SwapMode.Silent
            ? SwapMode.Silent
            : this.field241.method461();
      }
   }

   public HitResult method150(HashSet<BlockPos> context, BlockPos pos) {
      dev.boze.client.ac.Anticheat var6 = this.method147().ac;
      ArrayList var7 = new ArrayList(6);

      for (Direction var11 : Direction.values()) {
         BlockPos var12 = pos.offset(var11);
         if (this.method151(context, var12) && var6.method567(var12, var11.getOpposite(), this.field238.method419(), this.field239.method419())) {
            var7.add(var11);
         }
      }

      ArrayList var13 = new ArrayList(var7.size());
      if (var6 instanceof NCP var14) {
         var14.method941(this.field236.getValue(), this.field237.getValue());
      } else if (var6 instanceof Grim var17) {
         var17.method938(this.field236.getValue());
      }

      for (Direction var18 : var7) {
         BlockHitResult var19 = var6.method566(pos.offset(var18), var18.getOpposite());
         if (var19 != null) {
            var13.add(var19);
         }
      }

      if (var6 instanceof NCP var16) {
         var16.method942();
      }

      return var6.method565(var13);
   }

   public boolean method151(HashSet<BlockPos> context, BlockPos pos) {
      BlockState var6 = mc.world.getBlockState(pos);
      if (context != null && context.contains(pos)) {
         return true;
      } else {
         return EntityTracker.field3914.containsKey(pos)
            ? true
            : Class2811.field101 && var6.isAir() || Class2811.field102 && !var6.getFluidState().isEmpty() || !var6.isAir() && var6.getFluidState().isEmpty();
      }
   }

   public boolean method2117() {
      if (!this.field235.method419()) {
         return false;
      } else if (mc.options.jumpKey.isPressed()) {
         return true;
      } else if (mc.options.forwardKey.isPressed()) {
         return true;
      } else if (mc.options.backKey.isPressed()) {
         return true;
      } else {
         return mc.options.leftKey.isPressed() ? true : mc.options.rightKey.isPressed();
      }
   }

   private boolean lambda$new$5() {
      return this.method2114() && this.field234.method461() == AttackMode.Packet;
   }

   private boolean lambda$new$4() {
      return this.method147() == Anticheat.Grim;
   }

   private boolean lambda$new$3() {
      return this.method147() != Anticheat.Ghost;
   }

   private boolean lambda$new$2() {
      return this.method147() == Anticheat.NCP;
   }

   private boolean lambda$new$1() {
      return this.method147() != Anticheat.Ghost;
   }

   private boolean lambda$new$0() {
      return this.method147() != Anticheat.Ghost;
   }
}
