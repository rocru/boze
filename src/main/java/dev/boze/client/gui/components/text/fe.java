package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

class fe extends TextBaseComponent {
   final StaticColor field1181;
   final NewColorComponent field1182;

   fe(NewColorComponent var1, String var2, double var3, double var5, double var7, double var9, StaticColor var11) {
      super(var2, var3, var5, var7, var9);
      this.field1182 = var1;
      this.field1181 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.field1181.field430 = NewColorComponent.field1488.field430;
         this.field1181.field431 = NewColorComponent.field1488.field431;
         this.field1181.field432 = NewColorComponent.field1488.field432;
      }
   }
}
