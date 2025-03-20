package mapped;

import dev.boze.client.font.IFontRender;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.hud.core.TextRadar;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;

public class Class2895 extends Class2894 {
   private final PlayerListEntry field119;
   private double field120;
   private double field121;
   private int field122;

   public Class2895(PlayerListEntry entry) {
      super(null, null);
      this.field119 = entry;
   }

   @Override
   void method5661(double var1, double var3) {
      this.field120 = var1;
      double var8 = IFontRender.method499()
         .method502(TextRadar.INSTANCE.field634.getValue() ? TextRadar.INSTANCE.field639.getValue() : HUD.INSTANCE.field2384.getValue());
      if (this.field122 == 0) {
         this.field122 = (int)var8;
         if (this.field122 % 2 == 0) {
            this.field122 -= 2;
         } else {
            this.field122--;
         }
      }

      this.field121 = var3 + var8 / 2.0 - (double)this.field122 / 2.0;
      TextRadar.INSTANCE.ac.add(this);
   }

   void method5663(DrawContext var1) {
      if (this.field119 != null) {
         Identifier var5 = this.field119.getSkinTextures().texture();
         var1.drawTexture(var5, (int)this.field120 + 1, (int)this.field121 + 1, this.field122, this.field122, 8.0F, 8.0F, 8, 8, 64, 64);
         var1.drawTexture(var5, (int)this.field120 + 1, (int)this.field121 + 1, this.field122, this.field122, 40.0F, 8.0F, 8, 8, 64, 64);
      }
   }

   @Override
   double method5662() {
      if (this.field122 == 0) {
         double var4 = IFontRender.method499()
            .method502(TextRadar.INSTANCE.field634.getValue() ? TextRadar.INSTANCE.field639.getValue() : HUD.INSTANCE.field2384.getValue());
         this.field122 = (int)var4;
         if (this.field122 % 2 == 0) {
            this.field122 -= 2;
         } else {
            this.field122--;
         }
      }

      return (double)(this.field122 + 2);
   }
}
