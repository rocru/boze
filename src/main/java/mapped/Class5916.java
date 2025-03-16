package mapped;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.components.scaled.bottomrow.EditGradientColorComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.gui.DrawContext;

public class Class5916 extends BottomRowScaledComponent {
   private final HashMap<String, Class5903<?>> field8;

   public Class5916(String title, HashMap<String, Class5903<?>> colors) {
      super(title, BottomRow.TextAddClose, 0.125, 0.4);
      this.field8 = colors;
   }

   private List<String> method1144() {
      return (List<String>)this.field8.keySet().stream().sorted().collect(Collectors.toList());
   }

   @Override
   protected int method2010() {
      return this.field8.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var14 = (String)this.method1144().get(var5938);
      Class5903 var15 = (Class5903)this.field8.get(var14);
      Class2776.method5432(var5939, var5940, var5941, var5942, false);
      Class2776.method5430(var5939, var5940, var5942, var15);
      Class2776.method5431(var14 + (var15.method2010() > 0 ? " [" + var15.method2010() + "]" : ""), var5939, var5940, var5942);
      double var16 = Class2778.method5440(var5940, var5942);
      Class2777.method5434(var5939, var5941, var16, Notifications.DELETE, Notifications.DUPLICATE, Notifications.EDIT, Notifications.SHARE);
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      String var18 = (String)this.method1144().get(index);
      double var19 = Class2778.method5440(itemY, itemHeight);
      if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 0), var19)) {
         Class5903 var25 = (Class5903)this.field8.get(var18);
         var25.method2142();
         Colors.INSTANCE.field2343.remove(var18);
         return true;
      } else if (!Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 1), var19)) {
         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 2), var19)) {
            Class5903 var24 = (Class5903)this.field8.get(var18);
            if (var24 instanceof StaticColor) {
               ClickGUI.field1335.method580(new NewColorComponent(var18, ((StaticColor)var24).method217(), this));
            } else if (var24 instanceof ChangingColor) {
               ClickGUI.field1335.method580(new ColorPickerComponent(var18, ((ChangingColor)var24).method212(), this));
            } else if (var24 instanceof GradientColor) {
               ClickGUI.field1335.method580(new EditGradientColorComponent(var18, ((GradientColor)var24).method214(var18), this));
            }

            return true;
         } else {
            return Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 3), var19);
         }
      } else {
         String var21 = this.field1427;
         if (this.field1427.isEmpty()) {
            var21 = var18 + "_";

            while (Colors.INSTANCE.field2343.containsKey(var21)) {
               var21 = var21 + "_";
            }
         }

         if (!Colors.INSTANCE.field2343.containsKey(var21)) {
            Class5903 var22 = (Class5903)this.field8.get(var18);
            Object var23;
            if (var22 instanceof StaticColor) {
               var23 = ((StaticColor)var22).method217();
            } else if (var22 instanceof ChangingColor) {
               var23 = ((ChangingColor)var22).method212();
            } else {
               if (!(var22 instanceof GradientColor)) {
                  this.field1427 = "";
                  return true;
               }

               var23 = ((GradientColor)var22).method214(var21);
            }

            Colors.INSTANCE.field2343.put(var21, var23);
            Class2778.method5437();
         }

         this.field1427 = "";
         return true;
      }
   }

   @Override
   protected void method1904() {
      if (!this.field1427.isEmpty() && !Colors.INSTANCE.field2343.containsKey(this.field1427)) {
         ClickGUI.field1335.method580(new AddColorComponent(this, this.field1427));
         this.field1427 = "";
      }
   }
}
