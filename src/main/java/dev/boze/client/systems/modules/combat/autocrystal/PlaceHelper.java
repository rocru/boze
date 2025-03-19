package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.enums.*;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoWeb;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import mapped.Class2923;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlaceHelper implements IMinecraft, SettingsGroup, IPlace {
   public final EnumSetting<AnticheatMode> field205 = new EnumSetting<AnticheatMode>(
      "AntiCheat", AnticheatMode.Grim, "The anti-cheat mode to use\n - Grim: For 2b2t.org, 2bpvp.com, etc.\n - NCP: For crystalpvp.cc, 6b6t.org, etc."
   );
   private final BooleanSetting field206 = new BooleanSetting("MultiTask", false, "Whether or not to multi-task", this.field205);
   final BooleanSetting field207 = new BooleanSetting(
      "Rotate", true, "Rotate\nOn NCP, toggles place and break\nOn Grim, toggles place (break is always on)\n", this.field205
   );
   private final EnumSetting<CrystalAuraYawStep> field208 = new EnumSetting<CrystalAuraYawStep>(
      "YawStep", CrystalAuraYawStep.Off, "Mode for slowing down rotations", this::lambda$new$0, this.field205
   );
   private final FloatSetting field209 = new FloatSetting(
      "YawAngle", 0.3F, 0.1F, 1.0F, 0.05F, "Maximum angle fraction to rotate by per tick", this::lambda$new$1, this.field205
   );
   public final BooleanSetting field210 = new BooleanSetting(
      "StrictDirection", true, "Ensure placements pass Strict Direction check\nMost servers check for this\n", this.field205
   );
   public final BooleanSetting field211 = new BooleanSetting(
      "AssumeMaxArmor", true, "Assume armor has max enchants\n2b2t hides this\n", this::lambda$new$2, this.field205
   );
   final EnumSetting<CrystalAuraUpdateMode> field212 = new EnumSetting<CrystalAuraUpdateMode>(
      "React",
      CrystalAuraUpdateMode.Packet,
      "When to react to changes in the world\n - Tick: React to everything during game ticks, most consistent\n - Spawn: Same as tick, but reacts to entity spawns right away\n - Packet: Reacts to everything instantly, as soon as it's received from the server"
   );
   public final EnumSetting<AutoMineSwapMode> field213 = new EnumSetting<AutoMineSwapMode>(
      "Swap",
      AutoMineSwapMode.Alt,
      "Auto Swap mode\n - Off: Don't auto swap\nHot-bar only modes:\n - Normal: Vanilla swap\n - Silent: Instantaneously swap to crystal/sword and back\nWhole inventory modes (you don't need to keep the tool in your hot-bar):\n - Alt: Alternative silent swap mode, may work where mode silent is patched\nNote: Whole inventory modes may not work on some servers\n"
   );
   final BooleanSetting field214 = new BooleanSetting("AntiWeakness", false, "Auto swap to sword to break crystals when weak", this.field213);
   final IntSetting field215 = new IntSetting("Penalty", 10, 0, 20, 1, "Tick penalty for swapping\n10 on most NCP servers", this::lambda$new$3, this.field213);
   final BooleanSetting field216 = new BooleanSetting("GrimBypass", true, "Bypass 2b2t/Grim's swap patch", this::lambda$new$4, this.field213);
   private final Setting<?>[] field217;
   private final AutoCrystal field218;
   final GrimPlace grim = new GrimPlace();
   final NCPPlace ncp;

   PlaceHelper(AutoCrystal var1) {
      this.field218 = var1;
      this.ncp = new NCPPlace(var1, this);
      this.field217 = new Setting[]{
         this.field205,
         this.field206,
         this.field207,
         this.field208,
         this.field209,
         this.field210,
         this.field211,
         this.field212,
         this.field213,
         this.field214,
         this.field215,
         this.field216
      };
   }

   @Override
   public Setting<?>[] get() {
      return this.field217;
   }

   boolean method107(ACRotationEvent var1) {
      return var1.method1018(this.field205.method461(), false) ? true : !AutoWeb.INSTANCE.targets.field927;
   }

   boolean method108(ACRotationEvent var1) {
      if (!Options.method477(this.field206.method419()) && !var1.method1018(this.field205.method461(), this.method2114())) {
         return false;
      } else {
         Class2923.method2142();
         return true;
      }
   }

   private boolean method109(RotationEvent var1) {
      return var1.method554(RotationMode.Sequential) || Options.method477(this.field206.method419());
   }

   void method1885(ACRotationEvent var1) {
      float[] var5 = this.method111();
      if (var5 != null) {
         this.method112(var5);
         var1.method1021(true);
         var1.yaw = var5[0];
         var1.pitch = var5[1];
      }

      if (this.field205.method461() == AnticheatMode.Grim) {
         this.method2142();
      }
   }

   void method1883(RotationEvent var1) {
      if (!this.method109(var1)) {
         if (!this.field218.field1046.method109(var1)) {
            if (this.field205.method461() == AnticheatMode.NCP) {
               this.method2142();
            }
         }
      }
   }

   private void method2142() {
      if (this.field212.method461() == CrystalAuraUpdateMode.Tick) {
         this.field218.autoCrystalBreak.method2142();
      } else if (!this.field218.autoCrystalTracker.field1531) {
         this.field218.autoCrystalBreak.method2142();
      } else {
         this.field218.autoCrystalTracker.field1531 = false;
      }

      this.field218.autoCrystalPlace.method1198();
   }

   private float[] method111() {
      float[] var4 = null;
      if (this.method2114()) {
         if (this.field207.method419() && this.field218.field1046.method1954() != null) {
            return EntityUtil.method2146(this.field218.field1046.method1954());
         }

         Vec3d var5 = this.field218.autoCrystalPlace.method1954();
         if (var5 != null) {
            var4 = EntityUtil.method2146(var5);
         }

         boolean var6 = false;
         if (var4 != null && this.field218.autoCrystalTracker.field1529 != null && this.field205.method461() == AnticheatMode.Grim && this.field207.method419()
            )
          {
            var6 = !this.method118(this.field218.autoCrystalTracker.field1529.getPos(), var4);
         }

         if (var4 == null || var6) {
            var5 = this.field218.autoCrystalBreak.method1954();
            if (var5 != null) {
               var4 = EntityUtil.method2146(var5);
            }

            if (var6) {
               this.field218.autoCrystalPlace.method2142();
            }
         }

         if (var5 != null) {
            this.field218.field1040.field176 = var5;
         }
      }

      return var4;
   }

   private void method112(float[] var1) {
      if (this.field205.method461() == AnticheatMode.NCP
         && (
            this.field208.method461() == CrystalAuraYawStep.Break && this.field218.autoCrystalTracker.field1529 != null
               || this.field208.method461() == CrystalAuraYawStep.Full
         )) {
         float var5 = MathHelper.wrapDegrees(var1[0] - ((ClientPlayerEntityAccessor)mc.player).getLastYaw());
         if (Math.abs(var5) > 180.0F * this.field209.method423()) {
            var1[0] = ((ClientPlayerEntityAccessor)mc.player).getLastYaw() + var5 * (180.0F * this.field209.method423() / Math.abs(var5));
            this.field218.autoCrystalBreak.method1416();
            this.field218.autoCrystalPlace.method1416();
         }
      }
   }

   @Override
   public BlockHitResult method113(BlockPos candidatePos) {
      return this.method120().method113(candidatePos);
   }

   @Override
   public BlockHitResult method114(BlockPos candidatePos) {
      return this.method120().method114(candidatePos);
   }

   @Override
   public Vec3d method115(Vec3d endCrystalPos) {
      return this.method120().method115(endCrystalPos);
   }

   @Override
   public boolean method116(Vec3d endCrystalPos) {
      return this.method120().method116(endCrystalPos);
   }

   @Override
   public boolean method117(Vec3d endCrystalPos, Vec3d playerPos) {
      return this.method120().method117(endCrystalPos, playerPos);
   }

   @Override
   public boolean method118(Vec3d endCrystalPos, float[] rotations) {
      return this.method120().method118(endCrystalPos, rotations);
   }

   @Override
   public boolean method119(Vec3d endCrystalPos, Vec3d playerPos, float[] rotations) {
      return this.method120().method119(endCrystalPos, playerPos, rotations);
   }

   boolean method2114() {
      return this.field205.method461() == AnticheatMode.Grim || this.field207.method419();
   }

   boolean method2115() {
      return this.field207.method419();
   }

   private IPlace method120() {
      return (IPlace)(this.field205.method461() == AnticheatMode.Grim ? this.grim : this.ncp);
   }

   private boolean lambda$new$4() {
      return this.field205.method461() == AnticheatMode.Grim
         && (this.field213.method461() == AutoMineSwapMode.Normal || this.field213.method461() == AutoMineSwapMode.Silent);
   }

   private boolean lambda$new$3() {
      return this.field205.method461() == AnticheatMode.NCP;
   }

   private boolean lambda$new$2() {
      return this.field205.method461() == AnticheatMode.Grim;
   }

   private boolean lambda$new$1() {
      return this.field205.method461() == AnticheatMode.NCP && this.field208.method461() != CrystalAuraYawStep.Off;
   }

   private boolean lambda$new$0() {
      return this.field205.method461() == AnticheatMode.NCP;
   }
}
