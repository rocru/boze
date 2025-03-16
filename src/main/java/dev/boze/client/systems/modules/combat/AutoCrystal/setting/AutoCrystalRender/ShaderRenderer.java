package dev.boze.client.systems.modules.combat.AutoCrystal.setting.AutoCrystalRender;

import dev.boze.client.enums.AutoCrystalShaderMode;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.StringSetting;
import dev.boze.client.utils.render.ByteTexture;
import java.io.File;
import java.io.FileInputStream;

public class ShaderRenderer {
   private final EnumSetting<AutoCrystalShaderMode> field2514;
   private final StringSetting field2515;
   private ByteTexture field2516;
   private String field2517;

   public ShaderRenderer(EnumSetting<AutoCrystalShaderMode> setting, StringSetting image) {
      this.field2514 = setting;
      this.field2515 = image;
   }

   public ByteTexture method1453() {
      return this.field2516;
   }

   public ShaderMode method1454() {
      if (this.field2514.method461() == AutoCrystalShaderMode.Image) {
         if (!this.field2515.method1322().isEmpty() && (!this.field2515.method1322().equals(this.field2517) || this.field2516 == null)) {
            File var4 = new File(ConfigManager.images, this.field2515.method1322() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.field2516 = ByteTexturePacker.method493(var5);
               if (this.field2516 != null) {
                  this.field2517 = this.field2515.method1322();
               } else {
                  this.field2517 = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification("Shaders", "Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field2515.method1341("");
               this.field2517 = "";
            }
         }

         if (this.field2516 != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }
}
