package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.enums.ColorTypes;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.components.FloatSliderComponent;
import dev.boze.client.gui.components.scaled.AddColorComponent;
import dev.boze.client.gui.components.scaled.ColorPickerComponent;
import dev.boze.client.gui.components.scaled.NewColorComponent;
import dev.boze.client.gui.components.slider.floats.fs;
import dev.boze.client.gui.components.slider.floats.ft;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.WeirdColorSetting;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class2776;
import mapped.Class2777;
import mapped.Class2778;
import mapped.Class5903;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class WeirdColorSettingComponent extends BottomRowScaledComponent {
   private final WeirdColorSetting field1458;
   private final FloatSliderComponent field1459;
   private final FloatSliderComponent field1460;

   public WeirdColorSettingComponent(WeirdColorSetting setting) {
      super("Choose " + setting.name, BottomRow.TextAddClose, 0.125, 0.4, setting.opacity ? 1 : 2);
      this.field1458 = setting;
      this.field1459 = new fs(this, "Fill Opacity", 0.0, 0.0, 0.0, 0.0, setting);
      if (!setting.opacity) {
         this.field1460 = new ft(this, "Outline Opacity", 0.0, 0.0, 0.0, 0.0, setting);
      } else {
         this.field1460 = null;
      }
   }

   private List<String> method1144() {
      return (List<String>)Colors.INSTANCE
         .field2343
         .entrySet()
         .stream()
         .filter(this::lambda$getSortedKeys$0)
         .map(Entry::getKey)
         .sorted()
         .collect(Collectors.toList());
   }

   @Override
   protected int method2010() {
      int var4 = this.field1458.method429(this.field1458.field936) ? 1 : 0;
      return var4 + this.method1144().size();
   }

   private String method1554(int var1) {
      if (!this.field1458.method429(this.field1458.field936)) {
         return (String)this.method1144().get(var1);
      } else {
         return var1 == 0 ? "Default" : (String)this.method1144().get(var1 - 1);
      }
   }

   private Class5903<?> method644(int var1) {
      if (!this.field1458.method429(this.field1458.field936)) {
         return (Class5903<?>)Colors.INSTANCE.field2343.get(this.method1144().get(var1));
      } else {
         return var1 == 0 ? this.field1458.field936 : (Class5903)Colors.INSTANCE.field2343.get(this.method1144().get(var1 - 1));
      }
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var14 = this.method1554(index);
      Class5903 var15 = this.method644(index);
      boolean var16 = index == 0 && this.field1458.method429(this.field1458.field936)
         ? this.field1458.method430().field3909.startsWith("_default")
         : this.field1458.method430().field3909.equals(var14);
      Class2776.method5432(itemX, itemY, itemWidth, itemHeight, var16);
      Class2776.method5430(itemX, itemY, itemHeight, var15);
      Class2776.method5431(var14, itemX, itemY, itemHeight);
      if (index == 0 && this.field1458.method429(this.field1458.field936)) {
         Class2777.method5433(Notifications.DUPLICATE, Class2778.method5439(itemX, itemWidth, 0), Class2778.method5440(itemY, itemHeight));
      } else {
         double var17 = Class2778.method5440(itemY, itemHeight);
         ArrayList var19 = new ArrayList();
         var19.add(Notifications.DELETE);
         var19.add(Notifications.DUPLICATE);
         if (this.field1458.method428() == ColorTypes.ALL || this.field1458.method428() == ColorTypes.SIMPLE && !(var15 instanceof GradientColor)) {
            var19.add(Notifications.EDIT);
         }

         var19.add(Notifications.SHARE);
         Class2777.method5434(itemX, itemWidth, var17, (Notifications[])var19.toArray(new Notifications[0]));
      }
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      String var18 = index == 0 && this.field1458.method429(this.field1458.field936) ? "_default" : this.method1554(index);
      Class5903 var19 = this.method644(index);
      double var20 = Class2778.method5440(itemY, itemHeight);
      if (index == 0 && this.field1458.method429(this.field1458.field936)) {
         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 0), var20)) {
            String var24 = this.field1427;
            if (this.field1427.isEmpty()) {
               var24 = "_default_";

               while (Colors.INSTANCE.field2343.containsKey(var24)) {
                  var24 = var24 + "_";
               }
            }

            if (!Colors.INSTANCE.field2343.containsKey(var24)) {
               Object var25;
               if (var19 instanceof StaticColor) {
                  var25 = ((StaticColor)var19).method217();
               } else if (var19 instanceof ChangingColor) {
                  var25 = ((ChangingColor)var19).method212();
               } else {
                  if (!(var19 instanceof GradientColor)) {
                     this.field1427 = "";
                     return true;
                  }

                  var25 = ((GradientColor)var19).method214(var24);
               }

               Colors.INSTANCE.field2343.put(var24, var25);
               Class2778.method5437();
            }

            this.field1427 = "";
            return true;
         }
      } else {
         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 0), var20)) {
            var19.method2142();
            Colors.INSTANCE.field2343.remove(var18);
            if (this.field1458.method430().field3909.equals(var18)) {
               this.field1458.method431();
            }

            return true;
         }

         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 1), var20)) {
            String var22 = this.field1427;
            if (this.field1427.isEmpty()) {
               var22 = var18 + "_";

               while (Colors.INSTANCE.field2343.containsKey(var22)) {
                  var22 = var22 + "_";
               }
            }

            if (!Colors.INSTANCE.field2343.containsKey(var22)) {
               Object var23;
               if (var19 instanceof StaticColor) {
                  var23 = ((StaticColor)var19).method217();
               } else if (var19 instanceof ChangingColor) {
                  var23 = ((ChangingColor)var19).method212();
               } else {
                  if (!(var19 instanceof GradientColor)) {
                     this.field1427 = "";
                     return true;
                  }

                  var23 = ((GradientColor)var19).method214(var22);
               }

               Colors.INSTANCE.field2343.put(var22, var23);
               Class2778.method5437();
            }

            this.field1427 = "";
            return true;
         }

         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 2), var20)) {
            if (var19 instanceof StaticColor) {
               ClickGUI.field1335.method580(new NewColorComponent(var18, ((StaticColor)var19).method217(), this));
            } else if (var19 instanceof ChangingColor) {
               ClickGUI.field1335.method580(new ColorPickerComponent(var18, ((ChangingColor)var19).method212(), this));
            } else if (var19 instanceof GradientColor) {
               ClickGUI.field1335.method580(new EditGradientColorComponent(var18, ((GradientColor)var19).method214(var18), this));
            }

            return true;
         }

         if (Class2778.method5441(mouseX, mouseY, Class2778.method5439(itemX, itemWidth, 3), var20)) {
            return true;
         }
      }

      if (Class2778.method5438(mouseX, mouseY, itemX, itemY, itemWidth, itemHeight)) {
         this.field1458.method432(new ColorWrapper(var18, var19, this.field1458.method430().field3911, this.field1458.method430().field3912));
         Class2778.method5437();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void method641(DrawContext context, double startY, double height) {
      this.field1459.field1133 = this.field1388 + BaseComponent.scaleFactor * 12.0;
      this.field1459.field1134 = startY;
      this.field1459.field1135 = this.field1390 - BaseComponent.scaleFactor * 24.0;
      this.field1459.field1136 = height;
      this.field1459.render(context, 0, 0, 0.0F);
      if (!this.field1458.opacity) {
         this.field1460.field1133 = this.field1388 + BaseComponent.scaleFactor * 12.0;
         this.field1460.field1134 = startY + height + BaseComponent.scaleFactor * 2.0;
         this.field1460.field1135 = this.field1390 - BaseComponent.scaleFactor * 24.0;
         this.field1460.field1136 = height;
         this.field1460.render(context, 0, 0, 0.0F);
      }
   }

   @Override
   public boolean isMouseOver(double mouseX, double mouseY, int button) {
      if (this.field1459.mouseClicked(mouseX, mouseY, button)) {
         return true;
      } else {
         return !this.field1458.opacity && this.field1460.mouseClicked(mouseX, mouseY, button) ? true : super.isMouseOver(mouseX, mouseY, button);
      }
   }

   @Override
   public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.field1459.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
         return true;
      } else {
         return !this.field1458.opacity && this.field1460.onDrag(mouseX, mouseY, button, deltaX, deltaY)
            ? true
            : super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   @Override
   protected void method1904() {
      if (!this.field1427.isEmpty() && !Colors.INSTANCE.field2343.containsKey(this.field1427)) {
         if (this.field1458.method428() == ColorTypes.STATIC) {
            StaticColor var4 = new StaticColor(148, 123, 211);
            ClickGUI.field1335.method580(new NewColorComponent(this.field1427, var4, this));
            this.field1427 = "";
         } else {
            ClickGUI.field1335.method580(new AddColorComponent(this, this.field1427));
            this.field1427 = "";
         }
      }
   }

   public WeirdColorSetting method645() {
      return this.field1458;
   }

   private boolean lambda$getSortedKeys$0(Entry var1) {
      return this.field1458.method429((Class5903<?>)var1.getValue());
   }
}
