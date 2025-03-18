package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ShaderSettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.ShaderSetting;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class e4 extends TextBaseComponent {
   final ShaderSetting field1163;
   final ShaderSettingComponent field1164;

   public e4(ShaderSettingComponent var1, String var2, double var3, double var5, double var7, double var9, ShaderSetting var11) {
      super(var2, var3, var5, var7, var9);
      this.field1164 = var1;
      this.field1163 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.field1132 = "Compiling...";
         this.field1163.method459(this.field1164.field1479);
         ClickGUI.field1335.method580(null);
      }
   }
}
