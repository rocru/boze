package dev.boze.client.gui.components.scaled.bottomrow;

import com.google.common.io.Files;
import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.Font;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.FontSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.files.FileUtil;
import dev.boze.client.utils.render.RenderUtil;
import java.io.File;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class FontSelectComponent extends BottomRowScaledComponent {
   private final FontSetting field1428;
   private final ArrayList<Font> field1429 = new ArrayList();
   private static final String[] field1430 = new String[]{"lexend", "vanilla"};

   public FontSelectComponent(FontSetting setting) {
      super(setting.name, BottomRow.AddClose, 0.1, 0.4);
      this.field1428 = setting;
      this.field1429.add(new Font("vanilla", null));

      for (File var8 : ConfigManager.fonts.listFiles()) {
         if (Files.getFileExtension(var8.getName()).equalsIgnoreCase("ttf")) {
            String var9 = Files.getNameWithoutExtension(var8.getName());
            this.field1429.add(new Font(var9, var8));
         }
      }
   }

   @Override
   protected int method2010() {
      return this.field1429.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      Font var13 = (Font)this.field1429.get(index);
      RenderUtil.field3963
         .method2257(
            itemX,
            itemY,
            itemWidth,
            itemHeight,
            15,
            24,
            Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0,
            var13.field1964.equals(this.field1428.method1276()) ? Theme.method1347().method2025(Theme.method1391()) : Theme.method1347()
         );
      IFontRender.method499()
         .drawShadowedText(
            var13.field1964, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
      if (var13.field1965 != null && !this.method1701(var13.field1964)) {
         Notifications.DELETE
            .render(
               itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0,
               itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5,
               Theme.method1350()
            );
      }
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      Font var18 = (Font)this.field1429.get(index);
      double var19 = itemX + itemWidth - Notifications.DELETE.method2091() - BaseComponent.scaleFactor * 6.0;
      double var21 = itemY + itemHeight * 0.5 - IconManager.method1116() * 0.5;
      if (var18.field1965 != null
         && !this.method1701(var18.field1964)
         && isMouseWithinBounds(mouseX, mouseY, var19, var21, Notifications.DELETE.method2091(), IconManager.method1116())) {
         if (var18.field1964.equals(this.field1428.method1276())) {
            this.field1428.method1278("vanilla");
         }

         var18.field1965.delete();
         this.field1429.remove(index);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         this.field1428.method1278(var18.field1964);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      }
   }

   @Override
   protected void method1904() {
      FileUtil.openFile(ConfigManager.fonts);
   }

   private boolean method1701(String var1) {
      for (String var8 : field1430) {
         if (var1.equalsIgnoreCase(var8)) {
            return true;
         }
      }

      return false;
   }
}
