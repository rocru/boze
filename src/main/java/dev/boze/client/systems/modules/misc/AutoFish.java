package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.SoundPlayEvent;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.util.math.Vec3d;

public class AutoFish extends Module {
   public static final AutoFish INSTANCE = new AutoFish();
   private BooleanSetting autoCast = new BooleanSetting("AutoCast", true, "Auto cast fishing rod");
   private BooleanSetting antiTech = new BooleanSetting(
      "AntiTechExtremist", true, "Prevents tech extremists from stealing your rare fish and mongolian fishing boots"
   );
   private IntSetting castDelay = new IntSetting("CastDelay", 10, 0, 20, 1, "Delay for casting rod");
   private IntSetting catchDelay = new IntSetting("CatchDelay", 5, 0, 20, 1, "Delay for catching fish");
   private MinMaxSetting soundRadius = new MinMaxSetting("SoundRadius", 2.5, 0.1, 10.0, 0.1, "Radius for detecting fish sounds");
   private final IntSetting timeout = new IntSetting("Timeout", 0, 0, 240, 10, "Timeout in seconds for catching fish\nSet to 0 to disable check\n");
   private boolean field2890;
   private boolean field2891;
   private int field2892;
   private int field2893;
   private int field2894;
   private int field2895;
   private final dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();

   public AutoFish() {
      super("AutoFish", "Automatically fish", Category.Misc);
   }

   @Override
   public void onEnable() {
      this.field2890 = false;
      this.field2891 = false;
      this.field2893 = 0;
   }

   @EventHandler
   private void method1666(SoundPlayEvent var1) {
      if (MinecraftUtils.isClientActive() && mc.player.fishHook != null) {
         SoundInstance var5 = var1.sound;
         FishingBobberEntity var6 = mc.player.fishHook;
         if (var5.getId().getPath().equals("entity.fishing_bobber.splash")
            && var6.getPos().distanceTo(new Vec3d(var5.getX(), var5.getY(), var5.getZ())) <= this.soundRadius.getValue()) {
            this.field2890 = true;
            this.field2894 = this.catchDelay.method434();
            this.field2895 = 0;
         }
      }
   }

   @EventHandler
   private void method1667(PostPlayerTickEvent var1) {
      if (MinecraftUtils.isClientActive()) {
         if (mc.player.fishHook != null && this.timeout.method434() > 0 && this.timer.hasElapsed((double)(this.timeout.method434() * 1000))) {
            this.field2890 = true;
            this.field2894 = this.catchDelay.method434();
            this.field2895 = 0;
            this.timer.reset();
         }

         if (this.field2893 <= 0) {
            this.field2893 = 30;
            if (this.autoCast.method419()
               && !this.field2890
               && !this.field2891
               && mc.player.fishHook == null
               && mc.player.getMainHandStack().getItem() instanceof FishingRodItem) {
               this.field2892 = 0;
               this.field2891 = true;
            }
         } else {
            this.field2893--;
         }

         if (this.field2891) {
            this.field2892++;
            if (this.field2892 > this.castDelay.method434()) {
               this.field2891 = false;
               ((MinecraftClientAccessor)mc).callDoItemUse();
               this.timer.reset();
            }
         }

         if (this.field2890 && this.field2894 <= 0) {
            if (this.field2895 == 0) {
               ((MinecraftClientAccessor)mc).callDoItemUse();
               this.field2894 = this.castDelay.method434();
               this.field2895 = 1;
               this.timer.reset();
            } else if (this.field2895 == 1) {
               ((MinecraftClientAccessor)mc).callDoItemUse();
               this.field2890 = false;
               this.timer.reset();
            }
         }

         this.field2894--;
      }
   }

   @EventHandler
   private void method1668(KeyEvent var1) {
      if (mc.options.useKey.isPressed()) {
         this.field2890 = false;
         this.timer.reset();
      }
   }
}
