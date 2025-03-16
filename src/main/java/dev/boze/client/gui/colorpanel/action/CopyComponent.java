package dev.boze.client.gui.colorpanel.action;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.manager.ColorManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

class CopyComponent extends TextBaseComponent {
   final ColorManager field1155;
   final ColorPanel field1156;

   CopyComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1156 = var1;
      this.field1155 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         ColorSettingComponent.field1396 = this.field1155.method1374().method964();
      }
   }
}
