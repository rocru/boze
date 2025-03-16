package dev.boze.client.gui.components.scaled.bottomrow;

import com.google.common.io.Files;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.SoundStringSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.files.FileUtil;
import dev.boze.client.utils.render.RenderUtil;
import java.io.File;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class SoundSettingComponent extends BottomRowScaledComponent {
   private final SoundStringSetting field1454;
   private ArrayList<File> field1455 = new ArrayList();

   public SoundSettingComponent(SoundStringSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.1, 0.4);
      this.field1454 = setting;

      for (File var8 : ConfigManager.sounds.listFiles()) {
         if (Files.getFileExtension(var8.getName()).equalsIgnoreCase("wav")) {
            this.field1455.add(var8);
         }
      }
   }

   @Override
   protected int method2010() {
      return this.field1455.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var13 = Files.getNameWithoutExtension(((File)this.field1455.get(index)).getName());
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var13.equals(this.field1454.method1322()) ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            var13, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
      Notifications.DELETE
         .render(
            itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0,
            itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5,
            Theme.method1350()
         );
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      String var18 = Files.getNameWithoutExtension(((File)this.field1455.get(index)).getName());
      double var19 = itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0;
      double var21 = itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5;
      if (isMouseWithinBounds(mouseX, mouseY, var19, var21, Notifications.DELETE.method2091(), IconManager.method1116())) {
         if (var18.equals(this.field1454.method1322())) {
            this.field1454.method1341("");
         }

         ((File)this.field1455.get(index)).delete();
         this.field1455.remove(index);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         this.field1454.method1341(var18);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      }
   }

   @Override
   protected void method1904() {
      FileUtil.openFile(ConfigManager.sounds);
   }
}
