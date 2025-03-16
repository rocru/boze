package dev.boze.client.gui.components.text;

import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

class eY extends TextBaseComponent {
   final String field1195;
   final ScaledBaseComponent field1196;
   final AddColorComponent field1197;

   eY(AddColorComponent var1, String var2, double var3, double var5, double var7, double var9, String var11, ScaledBaseComponent var12) {
      super(var2, var3, var5, var7, var9);
      this.field1197 = var1;
      this.field1195 = var11;
      this.field1196 = var12;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         GradientColor var5 = new GradientColor(this.field1195);
         var5.field422.add(new StaticColor(148, 123, 211));
         var5.field422.add(new StaticColor(123, 148, 211));
         ClickGUI.field1335.method580(new EditGradientColorComponent(this.field1195, var5, this.field1196));
      }
   }
}
