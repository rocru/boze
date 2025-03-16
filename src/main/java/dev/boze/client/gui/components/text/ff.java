package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.screens.ClickGUI;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

class ff extends TextBaseComponent {
   final ScaledBaseComponent field1183;
   final NewColorComponent field1184;

   ff(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, ScaledBaseComponent var11) {
      super(var2, var3, var5, var7, var9);
      this.field1184 = var1;
      this.field1183 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         ClickGUI.field1335.method580(this.field1183);
      }
   }
}
