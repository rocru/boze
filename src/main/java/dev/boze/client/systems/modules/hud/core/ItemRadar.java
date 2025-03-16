package dev.boze.client.systems.modules.hud.core;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import mapped.Class2893;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;

public class ItemRadar extends HUDModule {
   private final IntSetting field2629 = new IntSetting("Range", 64, 8, 256, 1, "Range to display players");
   private final BooleanSetting field2630 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field2631 = new ColorSetting(
      "Name", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Name color", this.field2630
   );
   private final ColorSetting field2632 = new ColorSetting(
      "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field2630
   );
   private final ColorSetting field2633 = new ColorSetting(
      "Brackets", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Info color", this.field2630
   );
   private final BooleanSetting field2634 = new BooleanSetting("Shadow", false, "Text shadow", this.field2630);
   private final MinMaxSetting field2635 = new MinMaxSetting("Spacing", 1.5, 0.0, 3.0, 0.1, "Spacing between lines", this.field2630);
   public static final ItemRadar INSTANCE = new ItemRadar();
   float field2636 = 0.0F;
   private final java.util.ArrayList<ItemEntity> field2637 = new java.util.ArrayList();

   public ItemRadar() {
      super("ItemRadar", "Shows a list of nearby items", 40.0, 40.0);
      this.field595.setValue(0.75);
   }

   @EventHandler
   public void method1556(MovementEvent event) {
      this.field2637.clear();
      StreamSupport.stream(mc.world.getEntities().spliterator(), false)
         .filter(ItemRadar::lambda$onSendMovementPackets$0)
         .map(ItemRadar::lambda$onSendMovementPackets$1)
         .filter(this::lambda$onSendMovementPackets$2)
         .forEach(this.field2637::add);
   }

   @Override
   public void method295(DrawContext context) {
      int[] var5 = new int[]{0};
      int[] var6 = new int[]{1};
      boolean var7 = this.method2010() == 1 || this.method2010() == 2;
      boolean var8 = this.method2010() == 2 || this.method2010() == 4;
      List var9 = (List)this.field2637.stream().map(this::method1557).collect(Collectors.toList());
      this.field2636 = (float)var9.stream().mapToDouble(this::lambda$onRender$3).max().orElse(0.0);
      this.method314((double)this.field2636);
      var9.stream().sorted(Comparator.comparingDouble(this::lambda$onRender$4)).forEach(this::lambda$onRender$5);
      this.method316((double)var5[0]);
   }

   private java.util.ArrayList<Class2893> method1557(ItemEntity var1) {
      java.util.ArrayList var4 = new java.util.ArrayList();
      var4.add(
         new Class2893(
            var1.getStack().getName().getString() + " x" + var1.getStack().getCount(),
            this.field2630.method419() ? this.field2631.method1362() : HUD.INSTANCE.field2383.method1362()
         )
      );
      var4.add(new Class2893("- [", this.field2630.method419() ? this.field2633.method1362() : HUD.INSTANCE.field2383.method1362()));
      var4.add(
         new Class2893(
            String.format("%.1f", mc.player.distanceTo(var1)), this.field2630.method419() ? this.field2632.method1362() : HUD.INSTANCE.field2383.method1362()
         )
      );
      var4.add(new Class2893("]", this.field2630.method419() ? this.field2633.method1362() : HUD.INSTANCE.field2383.method1362()));
      return var4;
   }

   private double method1558(List<Class2893> var1) {
      double var5 = -IFontRender.method499()
         .measureTextHeight(" ", this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419());

      for (Class2893 var8 : var1) {
         var5 += IFontRender.method499()
            .measureTextHeight(var8.field115, this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419());
         var5 += IFontRender.method499().measureTextHeight(" ", this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419());
      }

      return var5;
   }

   private void lambda$onRender$5(boolean var1, int[] var2, int[] var3, List var4) {
      double var8 = this.method1558(var4);
      if (HUD.INSTANCE.field2394.method419()) {
         HUD.INSTANCE
            .field2397
            .method2252(
               this.method1391() + (var1 ? this.method313() - var8 - 4.0 : 0.0),
               this.method305() + (double)var2[0],
               var8 + 4.0,
               IFontRender.method499().method502(this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419())
                  + this.field2630.method419() ? this.field2635.getValue() : HUD.INSTANCE.field2385.getValue(),
               RGBAColor.field402
            );
      }

      double var10 = this.method305() + (double)var2[0] + 0.5;
      double var12 = var1 ? this.method313() - var8 - 2.0 : 2.0;

      for (Class2893 var15 : var4) {
         IFontRender.method499()
            .drawShadowedText(
               var15.field115,
               this.method1391() + var12,
               var10,
               var15.field116,
               this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419()
            );
         var12 += IFontRender.method499()
            .measureTextHeight(var15.field115 + " ", this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419());
      }

      var2[0] = (int)(
         (double)var2[0]
            + IFontRender.method499().method502(this.field2630.method419() ? this.field2634.method419() : HUD.INSTANCE.field2384.method419())
            + this.field2630.method419() ? this.field2635.getValue() : HUD.INSTANCE.field2385.getValue()
      );
      var3[0]++;
   }

   private double lambda$onRender$4(boolean var1, List var2) {
      return var1 ? -this.method1558(var2) : this.method1558(var2);
   }

   private double lambda$onRender$3(List var1) {
      return this.method1558(var1);
   }

   private boolean lambda$onSendMovementPackets$2(ItemEntity var1) {
      return mc.player.distanceTo(var1) <= (float)this.field2629.method434().intValue();
   }

   private static ItemEntity lambda$onSendMovementPackets$1(Entity var0) {
      return (ItemEntity)var0;
   }

   private static boolean lambda$onSendMovementPackets$0(Entity var0) {
      return var0 instanceof ItemEntity;
   }
}
