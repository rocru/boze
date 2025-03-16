package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.InhibitMode;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.settings.TargetSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.autocrystal.CrystalEntityTracker;
import dev.boze.client.systems.modules.combat.autocrystal.CrystalHelper;
import dev.boze.client.systems.modules.combat.autocrystal.CrystalHitter;
import dev.boze.client.systems.modules.combat.autocrystal.CrystalProcessor;
import dev.boze.client.systems.modules.combat.autocrystal.CrystalSelectionHandler;
import dev.boze.client.systems.modules.combat.autocrystal.PlaceHelper;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalBasePlace;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalBreak;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalPlace;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalPrediction;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalRender;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalTargeting;
import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalTracker;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.Boze;
import mapped.Class2896;
import mapped.Class2923;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AutoCrystal extends Module {
   public static final AutoCrystal INSTANCE = new AutoCrystal();
   public static boolean field1038 = false;
   public static boolean field1039 = false;
   final AutoCrystalRender field1040 = new AutoCrystalRender(this);
   final PlaceHelper field1041 = new PlaceHelper(this);
   public final AutoCrystalTargeting field1042 = new AutoCrystalTargeting(this);
   final TargetSetting field1043 = new TargetSetting();
   final AutoCrystalBreak autoCrystalBreak = new AutoCrystalBreak(this);
   final AutoCrystalPlace autoCrystalPlace = new AutoCrystalPlace(this);
   final AutoCrystalBasePlace autoCrystalBasePlace = new AutoCrystalBasePlace(this);
   final BooleanSetting proMode = new BooleanSetting("ProMode", false, "Show advanced options", AutoCrystal::lambda$new$0);
   final SettingCategory advancedSettings = new SettingCategory(
      "Advanced", "Advanced options\nDon't touch them unless you know what you're doing\n.set autocrystal promode false to hide\n", this.proMode::method419
   );
   final BooleanSetting delaySync = new BooleanSetting("DelaySync", false, "Sync delays to server TPS", this.advancedSettings);
   final BooleanSetting placeSync = new BooleanSetting(
      "PlaceSync",
      true,
      "Sync placements to once a cycle\nFor context; cycle = targeting calculation to damage applied time\nImproves performance and consistency\n",
      this.advancedSettings
   );
   final IntSetting hitTicks = new IntSetting(
      "EnemyHitTicks",
      0,
      0,
      20,
      0,
      "Amount of ticks to assume the enemy hit their (new) crystal for\nWhen a crystal spawns, even if you don't hit it, it will be considered dead\n",
      this.advancedSettings
   );
   final BooleanSetting dontPredict = new BooleanSetting("DontPredict", false, "Don't predict enemy movement");
   final BooleanSetting protocol = new BooleanSetting("Protocol", false, "1.12 Placement", this.advancedSettings);
   final BooleanSetting damageSync = new BooleanSetting("DamageSync", false, "Sync damage calculations to server damage ticks", this.advancedSettings);
   final IntSetting extraLimit = new IntSetting("ExtraLimit", 0, 0, 3, 1, "Additional attack limit", this.advancedSettings);
   final FloatSetting ticksExisted = new FloatSetting(
      "TicksExisted", 0.0F, 0.0F, 25.0F, 0.1F, "Min ticks to wait after a crystal's spawn before attacking it", this.advancedSettings
   );
   final FloatSetting targetRange = new FloatSetting("TargetRange", 8.0F, 4.0F, 16.0F, 0.5F, "Range from which to select targets", this.advancedSettings);
   final FloatSetting crystalRange = new FloatSetting(
      "CrystalRange",
      6.0F,
      1.0F,
      16.0F,
      0.5F,
      "Max range from target to place crystals\nTechnical detail: union, not intersection with target range",
      this.advancedSettings
   );
   final BooleanSetting minDamage = new BooleanSetting("AutoMinDmg", false, "Automatically choose min damage", this.advancedSettings);
   final FloatSetting holeMinDamage = new FloatSetting(
      "HoleMinDmg", 1.2F, 0.0F, 20.0F, 0.1F, "Minimum amount of damage for players in holes", this.minDamage::method419, this.advancedSettings
   );
   final FloatSetting stillMinDamage = new FloatSetting(
      "StillMinDmg", 6.3F, 0.0F, 20.0F, 0.1F, "Minimum amount of damage for still players", this.minDamage::method419, this.advancedSettings
   );
   final FloatSetting movingMinDamage = new FloatSetting(
      "MovingMinDmg", 7.0F, 0.0F, 20.0F, 0.1F, "Minimum amount of damage for moving players", this.minDamage::method419, this.advancedSettings
   );
   final CrystalSelectionHandler field1044 = new CrystalSelectionHandler(this);
   final CrystalHelper field1045 = new CrystalHelper(this);
   final CrystalHitter field1046 = new CrystalHitter(this);
   public final Class2896 field1047 = new Class2896(this);
   CrystalEntityTracker aa = new CrystalEntityTracker();
   final AutoCrystalDisplayInfo ab = new AutoCrystalDisplayInfo(this);
   final AutoCrystalPrediction ac = new AutoCrystalPrediction(this);
   private CrystalProcessor ad = new CrystalProcessor(this);
   final AutoCrystalTracker autoCrystalTracker = new AutoCrystalTracker(this);

   private static void method1750(String var0) {
      if (field1038 && mc.player != null) {
         System.out.println("[AutoCrystal.Core @" + mc.player.age + "] " + var0);
      }
   }

   public AutoCrystal() {
      super(
         "AutoCrystal",
         "Automatically places and breaks crystals to attack enemies\nAdvanced settings are pre-configured to optimal options, only change if expert\n",
         Category.Combat
      );
      Boze.EVENT_BUS.subscribe(this.field1047);
      Boze.EVENT_BUS.subscribe(this.field1040);
      this.addSettings(this.field1040.field168);
   }

   @Override
   public void onEnable() {
      this.autoCrystalTracker.method1854();
      this.field1040.method2142();
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive() && InventoryUtil.method2114() && InventoryUtil.method532() == this) {
         InventoryUtil.method396(this);
      }
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      this.ac.method2071(event);
   }

   public void method488(Entity entity) {
      if (this.isEnabled() && this.autoCrystalBreak.field181.method461() != InhibitMode.Off) {
         long var5 = System.nanoTime();
         if (entity instanceof EndCrystalEntity) {
            BlockPos var7 = BlockPos.ofFloored(entity.getPos()).down();
            this.method489(var7, entity.getPos(), entity.getId());
         }

         long var9 = System.nanoTime();
         if (field1039) {
            System.out.println("[AutoCrystal.Time] (A) " + (float)(var9 - var5) / 1000000.0F + " ms");
         }
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof EntitySpawnS2CPacket var5
         && var5.getEntityType() == EntityType.END_CRYSTAL
         && this.autoCrystalBreak.field181.method461() == InhibitMode.Off) {
         long var15 = System.nanoTime();
         Vec3d var16 = new Vec3d(var5.getX(), var5.getY(), var5.getZ());
         BlockPos var17 = BlockPos.ofFloored(var16).down();
         this.method489(var17, var16, var5.getEntityId());
         long var11 = System.nanoTime();
         if (field1039) {
            System.out.println("[AutoCrystal.Time] (B) " + (float)(var11 - var15) / 1000000.0F + " ms");
         }

         return;
      }

      if (event.packet instanceof ExplosionS2CPacket var6) {
         long var14 = System.nanoTime();
         BlockPos var9 = BlockPos.ofFloored(var6.getX(), var6.getY() - 1.0, var6.getZ());
         this.autoCrystalTracker.remove(var9);
         long var10 = System.nanoTime();
         if (field1039) {
            System.out.println("[AutoCrystal.Time] (C) " + (float)(var10 - var14) / 1000000.0F + " ms");
         }
      }
   }

   private void method489(BlockPos var1, Vec3d var2, int var3) {
      long var7 = System.nanoTime();
      if (this.autoCrystalTracker.method2101(var1)) {
         this.autoCrystalTracker.method682(var1);
         this.ad.method489(var1, var2, var3);
      }

      long var9 = System.nanoTime();
      if (field1039) {
         System.out.println("[AutoCrystal.Time] (D) " + (float)(var9 - var7) / 1000000.0F + " ms");
      }
   }

   @EventHandler(
      priority = 50
   )
   public void method1885(ACRotationEvent event) {
      if (!this.field1041.method107(event)) {
         long var5 = System.nanoTime();
         this.method1904();
         long var7 = System.nanoTime();
         if (field1039) {
            System.out.println("[AutoCrystal.Time] (E) " + (float)(var7 - var5) / 1000000.0F + " ms");
         }

         if (!this.field1041.method108(event)) {
            var5 = System.nanoTime();
            this.method1854();
            var7 = System.nanoTime();
            if (field1039) {
               System.out.println("[AutoCrystal.Time] (F) " + (float)(var7 - var5) / 1000000.0F + " ms");
            }

            var5 = System.nanoTime();
            this.field1041.method1885(event);
            var7 = System.nanoTime();
            if (field1039) {
               System.out.println("[AutoCrystal.Time] (G) " + (float)(var7 - var5) / 1000000.0F + " ms");
            }
         }
      }
   }

   @EventHandler(
      priority = 50
   )
   public void method1883(RotationEvent event) {
      long var4 = System.nanoTime();
      this.field1041.method1883(event);
      long var6 = System.nanoTime();
      if (field1039) {
         System.out.println("[AutoCrystal.Time] (I) " + (float)(var6 - var4) / 1000000.0F + " ms");
      }
   }

   public boolean method2101(BlockPos pos) {
      return this.autoCrystalBasePlace.method2101(pos);
   }

   private void method1904() {
      this.aa.method2142();
      this.ac.method2142();
      this.ac.method1416();
      this.autoCrystalTracker.method1904();
      this.autoCrystalTracker.update();
   }

   private void method1854() {
      boolean var4 = false;
      boolean var5 = false;
      method1750("Ticking logic");
      this.field1042.method2142();
      if (!this.field1042.method1144().isEmpty()) {
         method1750("Target found");
         var4 = this.autoCrystalBreak.method2114();
         var5 = this.autoCrystalPlace.method2114();
         if (!var4 && !var5) {
            method1750("No place or break found");
            this.field1046.method2142();
         }
      }

      if (!var4 && !var5) {
         Class2923.method2142();
      }
   }

   @EventHandler
   private void method1812(MouseButtonEvent var1) {
      if (var1.action == KeyAction.Press) {
         this.field1046.method1812(var1);
      }
   }

   @EventHandler
   private void method1944(KeyEvent var1) {
      if (var1.action == KeyAction.Press) {
         this.field1046.method1944(var1);
      }
   }

   @Override
   public String method1322() {
      return this.ab.method210();
   }

   public float method1384() {
      return this.autoCrystalTracker.method1384();
   }

   private static boolean lambda$new$0() {
      return false;
   }
}
