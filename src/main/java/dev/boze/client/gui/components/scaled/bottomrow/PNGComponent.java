package dev.boze.client.gui.components.scaled.bottomrow;

import com.google.common.io.Files;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.StringSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.files.FileUtil;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.io.File;
import java.util.ArrayList;

public class PNGComponent extends BottomRowScaledComponent {
   private final StringSetting field1445;
   private final ArrayList<File> field1446 = new ArrayList();

   public PNGComponent(StringSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.1, 0.4);
      this.field1445 = setting;

      for (File var8 : ConfigManager.images.listFiles()) {
         if (Files.getFileExtension(var8.getName()).equalsIgnoreCase("png")) {
            this.field1446.add(var8);
         }
      }
   }

   @Override
   protected int method2010() {
      return this.field1446.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var13 = Files.getNameWithoutExtension(((File)this.field1446.get(index)).getName());
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var13.equals(this.field1445.getValue()) ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
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
      String var18 = Files.getNameWithoutExtension(((File)this.field1446.get(index)).getName());
      double var19 = itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0;
      double var21 = itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5;
      if (isMouseWithinBounds(mouseX, mouseY, var19, var21, Notifications.DELETE.method2091(), IconManager.method1116())) {
         if (var18.equals(this.field1445.getValue())) {
            this.field1445.setValue("");
         }

         ((File)this.field1446.get(index)).delete();
         this.field1446.remove(index);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         this.field1445.setValue(var18);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      }
   }

   @Override
   protected void method1904() {
      FileUtil.openFile(ConfigManager.images);
   }
}
