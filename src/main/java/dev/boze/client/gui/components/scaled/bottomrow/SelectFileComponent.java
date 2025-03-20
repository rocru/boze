package dev.boze.client.gui.components.scaled.bottomrow;

import com.google.common.io.Files;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.ListSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.files.FileUtil;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.io.File;
import java.util.ArrayList;

public class SelectFileComponent extends BottomRowScaledComponent {
   private final ListSetting field1461;
   private ArrayList<File> field1462 = new ArrayList();

   public SelectFileComponent(ListSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.1, 0.4);
      this.field1461 = setting;

      for (File var8 : ConfigManager.spammer.listFiles()) {
         if (Files.getFileExtension(var8.getName()).equalsIgnoreCase("txt")) {
            this.field1462.add(var8);
         }
      }
   }

   @Override
   protected int method2010() {
      return this.field1462.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var13 = Files.getNameWithoutExtension(((File)this.field1462.get(index)).getName());
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            this.field1461.getValue().contains(var13) ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
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
      String var18 = Files.getNameWithoutExtension(((File)this.field1462.get(index)).getName());
      double var19 = itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0;
      double var21 = itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5;
      if (isMouseWithinBounds(mouseX, mouseY, var19, var21, Notifications.DELETE.method2091(), IconManager.method1116())) {
         if (this.field1461.getValue().contains(var18)) {
            this.field1461.getValue().remove(var18);
         }

         ((File)this.field1462.get(index)).delete();
         this.field1462.remove(index);
         this.field1461.method206(true);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         if (this.field1461.getValue().contains(var18)) {
            this.field1461.getValue().remove(var18);
         } else {
            this.field1461.getValue().add(var18);
         }

         this.field1461.method206(true);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      }
   }

   @Override
   protected void method1904() {
      FileUtil.openFile(ConfigManager.spammer);
   }
}
