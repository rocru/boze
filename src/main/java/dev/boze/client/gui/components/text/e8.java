package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class e8 extends TextBaseComponent {
   final RGBASettingComponent field1171;

   public e8(RGBASettingComponent var1, String var2, double var3, double var5, double var7, double var9) {
      super(var2, var3, var5, var7, var9);
      this.field1171 = var1;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         ClickGUI.field1335.method580(null);
      }
   }
}
