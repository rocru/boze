package dev.boze.client.systems.modules.hud.core;

import dev.boze.api.BozeInstance;
import dev.boze.api.addon.module.ToggleableModule;
import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ArrayList extends HUDModule {
   private final BooleanSetting field649 = new BooleanSetting("OnlyBound", false, "Only show modules with binds");
   private final BooleanSetting field650 = new BooleanSetting("Addons", true, "Show addon modules in the arraylist");
   public final BooleanSetting field651 = new BooleanSetting(
      "Animations", false, "Enables animations\nNote: For this to work, ArrayList has to be on one side of the screen"
   );
   private final MinMaxSetting field652 = new MinMaxSetting("Duration", 0.25, 0.1, 5.0, 0.1, "Animation duration in seconds", this.field651::getValue);
   private final BooleanSetting field653 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field654 = new ColorSetting(
      "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field653
   );
   private final ColorSetting field655 = new ColorSetting(
      "Info", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Bind color", this.field653
   );
   private final ColorSetting field656 = new ColorSetting(
      "Brackets", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Info color", this.field653
   );
   private final BooleanSetting field657 = new BooleanSetting("Shadow", false, "Text shadow", this.field653);
   private final MinMaxSetting field658 = new MinMaxSetting("Spacing", 1.5, 0.0, 3.0, 0.1, "Spacing between lines", this.field653);
   public static final ArrayList INSTANCE = new ArrayList();
   public final HashMap<ArrayListModuleInfo, Long> field659 = new HashMap();
   public final HashMap<ToggleableModule, Boolean> field660 = new HashMap();
   float field661 = 0.0F;

   public ArrayList() {
      super("ArrayList", "Shows a list of enabled modules", Category.Hud, 0.0, 0.0, 2, 40.0, 40.0);
      this.field595.setValue(0.75);
      this.setEnabled(true);
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      if (this.isEnabled() && this.field651.getValue() && this.field650.getValue()) {
         for (ToggleableModule var6 : BozeInstance.INSTANCE.getModules()) {
            if ((Boolean)this.field660.getOrDefault(var6, var6.getState()) != var6.getState()) {
               this.field659.put(new ArrayListModuleInfo(var6.getTitle(), "", var6.getState()), System.currentTimeMillis());
            }

            this.field660.put(var6, var6.getState());
         }
      }
   }

   @Override
   public void method295(DrawContext context) {
      Iterator var5 = this.field659.entrySet().iterator();

      while (var5.hasNext()) {
         Entry var6 = (Entry)var5.next();
         long var7 = (Long)var6.getValue();
         long var9 = System.currentTimeMillis();
         long var11 = var9 - var7;
         if ((double)var11 > this.field652.getValue() * 1000.0) {
            var5.remove();
         }
      }

      int[] var13 = new int[]{0};
      int[] var14 = new int[]{1};
      boolean var8 = this.method2010() == 1 || this.method2010() == 2;
      boolean var15 = this.method2010() == 2 || this.method2010() == 4;
      List var10 = (List)this.method2120().stream().filter(this::lambda$onRender$0).collect(Collectors.toList());
      this.field661 = (float)var10.stream().mapToDouble(this::lambda$onRender$1).max().orElse(0.0);
      this.method314((double)this.field661);
      var10.stream().sorted(Comparator.comparingDouble(this::lambda$onRender$2)).forEach(this::lambda$onRender$3);
      this.method316((double)var13[0]);
   }

   private java.util.ArrayList<ArrayListModuleInfo> method2120() {
      java.util.ArrayList var4 = (java.util.ArrayList) Boze.getModules()
         .modules
         .stream()
         .filter(Module::getVisibility)
         .filter(this::lambda$getLines$4)
         .filter(ArrayList::lambda$getLines$5)
         .map(ArrayList::lambda$getLines$6)
         .collect(Collectors.toCollection(java.util.ArrayList::new));
      if (this.field650.getValue()) {
         BozeInstance.INSTANCE.getModules().stream().map(ArrayList::lambda$getLines$7).forEach(var4::add);
      }

      if (var4.isEmpty()) {
         var4.add(new ArrayListModuleInfo("HudEditor", "", true));
      }

      return var4;
   }

   private String method340(ArrayListModuleInfo var1) {
      return !var1.field2594.isEmpty() ? var1.field2593 + " [" + var1.field2594 + "]" : var1.field2593;
   }

   private static ArrayListModuleInfo lambda$getLines$7(ToggleableModule var0) {
      return new ArrayListModuleInfo(var0.getTitle(), "", var0.getState());
   }

   private static ArrayListModuleInfo lambda$getLines$6(Module var0) {
      return new ArrayListModuleInfo(var0.getName(), var0.method1322(), var0.isEnabled());
   }

   private static boolean lambda$getLines$5(Module var0) {
      return var0.category != Category.Hud && var0.category != Category.Graph;
   }

   private boolean lambda$getLines$4(Module var1) {
      return !this.field649.getValue() || var1.bind.isValid();
   }

   private void lambda$onRender$3(boolean var1, int[] var2, int[] var3, ArrayListModuleInfo var4) {
      String var8 = this.method340(var4);
      double var9 = IFontRender.method499().measureTextHeight(var8, this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue());
      if (HUD.INSTANCE.field2394.getValue()) {
         HUD.INSTANCE
            .field2397
            .method2252(
               this.method1391() + (var1 ? this.method313() - var9 - 4.0 : 0.0),
               this.method305() + (double)var2[0],
               var9 + 4.0,
               IFontRender.method499().method502(this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue())
                  + this.field653.getValue() ? this.field658.getValue() : HUD.INSTANCE.field2385.getValue(),
               RGBAColor.field402
            );
      }

      double var11 = 0.0;
      if (this.field651.getValue() && this.field659.containsKey(var4)) {
         long var13 = (Long)this.field659.get(var4);
         long var15 = System.currentTimeMillis();
         long var17 = var15 - var13;
         double var19 = (double)var17 / (this.field652.getValue() * 1000.0);
         var11 = (var1 ? var9 : -var9) * (var4.field2595 ? 1.0 - var19 : var19);
      }

      IFontRender.method499()
         .drawShadowedText(
            var4.field2593,
            this.method1391() + var11 + (var1 ? this.method313() - var9 - 2.0 : 2.0),
            this.method305() + (double)var2[0] + 0.5,
            this.field653.getValue() ? this.field654.getValue() : HUD.INSTANCE.field2383.getValue(),
            this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()
         );
      if (!var4.field2594.isEmpty()) {
         IFontRender.method499()
            .drawShadowedText(
               "[",
               this.method1391()
                  + var11
                  + (var1 ? this.method313() - var9 - 2.0 : 2.0)
                  + IFontRender.method499()
                     .measureTextHeight(var4.field2593 + " ", this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()),
               this.method305() + (double)var2[0] + 0.5,
               this.field653.getValue() ? this.field656.getValue() : HUD.INSTANCE.field2383.getValue(),
               this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()
            );
         IFontRender.method499()
            .drawShadowedText(
               var4.field2594,
               this.method1391()
                  + var11
                  + (var1 ? this.method313() - var9 - 2.0 : 2.0)
                  + IFontRender.method499()
                     .measureTextHeight(var4.field2593 + " [", this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()),
               this.method305() + (double)var2[0] + 0.5,
               this.field653.getValue() ? this.field655.getValue() : HUD.INSTANCE.field2383.getValue(),
               this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()
            );
         IFontRender.method499()
            .drawShadowedText(
               "]",
               this.method1391()
                  + var11
                  + (var1 ? this.method313() - var9 - 2.0 : 2.0)
                  + IFontRender.method499()
                     .measureTextHeight(
                        var4.field2593 + " [" + var4.field2594, this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()
                     ),
               this.method305() + (double)var2[0] + 0.5,
               this.field653.getValue() ? this.field656.getValue() : HUD.INSTANCE.field2383.getValue(),
               this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue()
            );
      }

      var2[0] = (int)(
         (double)var2[0]
            + IFontRender.method499().method502(this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue())
            + this.field653.getValue() ? this.field658.getValue() : HUD.INSTANCE.field2385.getValue()
      );
      var3[0]++;
   }

   private double lambda$onRender$2(boolean var1, ArrayListModuleInfo var2) {
      return var1
         ? -IFontRender.method499()
            .measureTextHeight(this.method340(var2), this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue())
         : IFontRender.method499()
            .measureTextHeight(this.method340(var2), this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue());
   }

   private double lambda$onRender$1(ArrayListModuleInfo var1) {
      return IFontRender.method499()
         .measureTextHeight(this.method340(var1), this.field653.getValue() ? this.field657.getValue() : HUD.INSTANCE.field2384.getValue());
   }

   private boolean lambda$onRender$0(ArrayListModuleInfo var1) {
      return var1.field2595 || this.field651.getValue() && this.field659.containsKey(var1);
   }
}
