package dev.boze.client.gui.colorpanel.action;

import dev.boze.client.gui.colorpanel.ColorPanel;
import dev.boze.client.gui.components.TextBaseComponent;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.manager.ColorManager;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class PasteComponent extends TextBaseComponent {
   final ColorManager field1157;
   final ColorPanel field1158;

   public PasteComponent(ColorPanel var1, String var2, double var3, double var5, double var7, double var9, ColorManager var11) {
      super(var2, var3, var5, var7, var9);
      this.field1158 = var1;
      this.field1157 = var11;
   }

   @Override
   protected void method1649(int button) {
      if (button == 0 && ColorSettingComponent.field1396 != null) {
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         this.field1157.method1374().set(ColorSettingComponent.field1396.copy());
      }
   }
}
