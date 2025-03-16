package dev.boze.client.systems.modules.misc;

import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.SoundStringSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Notifications;
import dev.boze.client.utils.TargetTracker;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class SoundFX extends Module {
   public static final SoundFX INSTANCE = new SoundFX();
   private final FloatSetting field3116 = new FloatSetting("Volume", 2.5F, 0.1F, 5.0F, 0.1F, "Volume for sound effects");
   private final SoundStringSetting field3117 = new SoundStringSetting("Kill", "", "Kill sound");
   private final SoundStringSetting field3118 = new SoundStringSetting("Shoot", "", "Bow shoot sound");
   public final SoundStringSetting field3119 = new SoundStringSetting("Enable", "", "Module enable sound");
   public final SoundStringSetting field3120 = new SoundStringSetting("Disable", "", "Module disable sound");
   public final SoundStringSetting field3121 = new SoundStringSetting("VisualEnter", "", "Player entering visual range sound", SoundFX::lambda$new$0);
   public final SoundStringSetting field3122 = new SoundStringSetting("VisualLeave", "", "Player leaving visual range sound", SoundFX::lambda$new$1);

   public SoundFX() {
      super("SoundFX", "Plays custom sound effects", Category.Misc);
   }

   public void method1771(boolean state) {
      if (mc.player != null) {
         if (state) {
            if (this.field3119.method1322().isEmpty()) {
               return;
            }

            this.method1775(new File(ConfigManager.sounds, this.field3119.method1322() + ".wav"));
         } else {
            if (this.field3120.method1322().isEmpty()) {
               return;
            }

            this.method1775(new File(ConfigManager.sounds, this.field3120.method1322() + ".wav"));
         }
      }
   }

   public void method1772(float health, LivingEntity entity) {
      if (!this.field3117.method1322().isEmpty() && mc.world != null && health <= 0.0F && entity != null && TargetTracker.method2055(entity)) {
         this.method1775(new File(ConfigManager.sounds, this.field3117.method1322() + ".wav"));
      }
   }

   public void method1773(ItemStack stack, int remainingUseTicks) {
      if (!this.field3118.method1322().isEmpty()) {
         if (!((double)BowItem.getPullProgress(Items.BOW.getMaxUseTime(stack, mc.player) - remainingUseTicks) < 0.1)) {
            this.method1775(new File(ConfigManager.sounds, this.field3118.method1322() + ".wav"));
         }
      }
   }

   private ItemStack method1774() {
      if (mc.player.getMainHandStack().getItem() instanceof BowItem) {
         return mc.player.getMainHandStack();
      } else {
         return mc.player.getOffHandStack().getItem() instanceof BowItem ? mc.player.getOffHandStack() : null;
      }
   }

   public void method1775(File file) {
      if (file.exists()) {
         try {
            AudioInputStream var4 = AudioSystem.getAudioInputStream(file);
            Clip var5 = AudioSystem.getClip();
            var5.open(var4);
            FloatControl var6 = (FloatControl)var5.getControl(Type.MASTER_GAIN);
            var6.setValue(-50.0F + this.field3116.method423() * 10.0F);
            var5.start();
         } catch (Exception var7) {
         }
      }
   }

   private static boolean lambda$new$1() {
      return Notifications.INSTANCE.isEnabled() && Notifications.INSTANCE.field847.method419();
   }

   private static boolean lambda$new$0() {
      return Notifications.INSTANCE.isEnabled() && Notifications.INSTANCE.field846.method419();
   }
}
