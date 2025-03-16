package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

class fd extends TextBaseComponent {
   final StaticColor field1179;
   final NewColorComponent field1180;

   fd(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, StaticColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field1180 = var1;
      this.field1179 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         NewColorComponent.field1488 = new StaticColor(this.field1179.field430, this.field1179.field431, this.field1179.field432);
      }
   }
}
