package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.settings.ColorSetting;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class fa extends TextBaseComponent {
   final ColorSetting field1174;
   final ColorSettingComponent field1175;

   fa(ColorSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ColorSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field1175 = var1;
      this.field1174 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0 && ColorSettingComponent.field1396 != null) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.field1174.method1374().set(ColorSettingComponent.field1396.copy());
      }
   }
}
