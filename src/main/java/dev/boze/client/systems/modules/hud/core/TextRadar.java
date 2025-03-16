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
import dev.boze.client.systems.modules.hud.core.TextRadar.pt;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.TargetTracker;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import mapped.Class2894;
import mapped.Class2895;
import mapped.Class5926;
import mapped.Class5929;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;

public class TextRadar extends HUDModule implements Class5929 {
   private final IntSetting field626 = new IntSetting("Range", 64, 8, 256, 1, "Range to display players");
   private final BooleanSetting field627 = new BooleanSetting("Limit", false, "Limit players to display");
   private final IntSetting field628 = new IntSetting("MaxPlayers", 10, 1, 25, 1, "Max amount of nearest players to display", this.field627);
   private final BooleanSetting field629 = new BooleanSetting("Heads", true, "Show player heads");
   private final BooleanSetting field630 = new BooleanSetting("Health", true, "Show player health");
   private final BooleanSetting field631 = new BooleanSetting("ColorCode", true, "Color codes health", this.field630);
   private final BooleanSetting field632 = new BooleanSetting("Ping", true, "Show player ping");
   private final BooleanSetting field633 = new BooleanSetting("Pops", true, "Show player pops");
   private final BooleanSetting field634 = new BooleanSetting("Custom", false, "Use custom theme settings");
   private final ColorSetting field635 = new ColorSetting(
      "Name", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Name color", this.field634
   );
   private final ColorSetting field636 = new ColorSetting(
      "Text", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Text color", this.field634
   );
   private final ColorSetting field637 = new ColorSetting(
      "Info", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Info color", this.field634
   );
   private final ColorSetting field638 = new ColorSetting(
      "Brackets", new BozeDrawColor(100, 35, 250, 255, true, 0.3, 0.0, new double[]{0.0, -0.065}, new double[]{0.5, 0.6}), "Bracket color", this.field634
   );
   private final BooleanSetting field639 = new BooleanSetting("Shadow", false, "Text shadow", this.field634);
   private final MinMaxSetting field640 = new MinMaxSetting("Spacing", 1.5, 0.0, 3.0, 0.1, "Spacing between lines", this.field634);
   public static final TextRadar INSTANCE = new TextRadar();
   private final BozeDrawColor field641 = new BozeDrawColor(-47032);
   private final BozeDrawColor field642 = new BozeDrawColor(-12728);
   private final BozeDrawColor field643 = new BozeDrawColor(-14169088);
   float aa = 0.0F;
   private final java.util.ArrayList<PlayerEntity> ab = new java.util.ArrayList();
   private final LinkedList<Class2895> ac = new LinkedList();

   public TextRadar() {
      super("TextRadar", "Shows a list of nearby players", 40.0, 40.0);
      this.field595.setValue(0.75);
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      this.ab.clear();
      this.ab.addAll((Collection)mc.world.getPlayers().stream().filter(this::lambda$onSendMovementPackets$0).collect(Collectors.toList()));
   }

   @Override
   public void method295(DrawContext context) {
      int[] var5 = new int[]{0};
      int[] var6 = new int[]{1};
      boolean var7 = this.method2010() == 1 || this.method2010() == 2;
      boolean var8 = this.method2010() == 2 || this.method2010() == 4;
      List var9;
      if (this.field627.method419()) {
         var9 = (List)this.ab
            .stream()
            .filter(TextRadar::lambda$onRender$1)
            .sorted(Comparator.comparingDouble(mc.player::method_5739).reversed())
            .limit((long)this.field628.method434().intValue())
            .map(this::method337)
            .collect(Collectors.toList());
      } else {
         var9 = (List)this.ab.stream().map(this::method337).collect(Collectors.toList());
      }

      this.aa = (float)var9.stream().mapToDouble(this::lambda$onRender$2).max().orElse(0.0);
      this.method314((double)this.aa);
      var9.stream().sorted(Comparator.comparingDouble(this::lambda$onRender$3)).forEach(this::lambda$onRender$4);
      this.method316((double)var5[0]);
   }

   private java.util.ArrayList<Class2894> method337(PlayerEntity var1) {
      java.util.ArrayList var5 = new java.util.ArrayList();
      if (this.field629.method419()) {
         PlayerListEntry var6 = mc.getNetworkHandler().getPlayerListEntry(var1.getUuid());
         if (var6 != null) {
            var5.add(new Class2895(var6));
         }
      }

      var5.add(new Class2894(var1.getNameForScoreboard(), this.field634.method419() ? this.field635.method1362() : HUD.INSTANCE.field2383.method1362()));
      var5.add(new Class2894("- [", this.field634.method419() ? this.field638.method1362() : HUD.INSTANCE.field2383.method1362()));
      var5.add(
         new Class2894(
            String.format("%.1f", mc.player.distanceTo(var1)), this.field634.method419() ? this.field636.method1362() : HUD.INSTANCE.field2383.method1362()
         )
      );
      if (this.field630.method419()) {
         var5.add(new Class2894("|", this.field634.method419() ? this.field638.method1362() : HUD.INSTANCE.field2383.method1362()));
         BozeDrawColor var9 = this.field634.method419() ? this.field636.method1362() : HUD.INSTANCE.field2383.method1362();
         double var7 = (double)var1.getHealth();
         if (this.field631.method419()) {
            if (var7 < 5.0) {
               var9 = (BozeDrawColor)this.field641.method196(var9.field411);
            } else if (var7 < 20.0) {
               var9 = (BozeDrawColor)this.field642.method196(var9.field411);
            } else {
               var9 = (BozeDrawColor)this.field643.method196(var9.field411);
            }
         }

         var5.add(new Class2894(String.format("%.1f", var7), var9));
         var5.add(new Class2894("hp", this.field634.method419() ? this.field637.method1362() : HUD.INSTANCE.field2383.method1362()));
      }

      if (this.field632.method419()) {
         var5.add(new Class2894("|", this.field634.method419() ? this.field638.method1362() : HUD.INSTANCE.field2383.method1362()));
         var5.add(
            new Class2894(
               Integer.toString(Class5926.method100(var1)), this.field634.method419() ? this.field636.method1362() : HUD.INSTANCE.field2383.method1362()
            )
         );
         var5.add(new Class2894("ms", this.field634.method419() ? this.field637.method1362() : HUD.INSTANCE.field2383.method1362()));
      }

      if (this.field633.method419() && TargetTracker.field1359.containsKey(var1.getNameForScoreboard())) {
         var5.add(new Class2894("|", this.field634.method419() ? this.field638.method1362() : HUD.INSTANCE.field2383.method1362()));
         var5.add(
            new Class2894(
               "-" + TargetTracker.field1359.get(var1.getNameForScoreboard()),
               this.field634.method419() ? this.field636.method1362() : HUD.INSTANCE.field2383.method1362()
            )
         );
         var5.add(new Class2894("pops", this.field634.method419() ? this.field637.method1362() : HUD.INSTANCE.field2383.method1362()));
      }

      var5.add(new Class2894("]", this.field634.method419() ? this.field638.method1362() : HUD.INSTANCE.field2383.method1362()));
      return var5;
   }

   private double method338(List<Class2894> var1) {
      double var5 = -IFontRender.method499().measureTextHeight(" ", this.field634.method419() ? this.field639.method419() : HUD.INSTANCE.field2384.method419());

      for (Class2894 var8 : var1) {
         var5 += var8.method5662();
      }

      return var5;
   }

   @Override
   public void method332(DrawContext context) {
      if (this.field629.method419()) {
         while (!this.ac.isEmpty()) {
            Class2895 var5 = (Class2895)this.ac.poll();
            var5.method5663(context);
         }
      }
   }

   private void lambda$onRender$4(boolean var1, int[] var2, int[] var3, List var4) {
      double var8 = this.method338(var4);
      if (HUD.INSTANCE.field2394.method419()) {
         HUD.INSTANCE
            .field2397
            .method2252(
               this.method1391() + (var1 ? this.method313() - var8 - 4.0 : 0.0),
               this.method305() + (double)var2[0],
               var8 + 4.0,
               IFontRender.method499().method502(this.field634.method419() ? this.field639.method419() : HUD.INSTANCE.field2384.method419())
                  + this.field634.method419() ? this.field640.getValue() : HUD.INSTANCE.field2385.getValue(),
               RGBAColor.field402
            );
      }

      double var10 = this.method305() + (double)var2[0] + 0.5;
      double var12 = var1 ? this.method313() - var8 - 2.0 : 2.0;

      for (Class2894 var15 : var4) {
         var15.method5661(this.method1391() + var12, var10);
         var12 += var15.method5662();
      }

      var2[0] = (int)(
         (double)var2[0]
            + IFontRender.method499().method502(this.field634.method419() ? this.field639.method419() : HUD.INSTANCE.field2384.method419())
            + this.field634.method419() ? this.field640.getValue() : HUD.INSTANCE.field2385.getValue()
      );
      var3[0]++;
   }

   private double lambda$onRender$3(boolean var1, List var2) {
      return var1 ? -this.method338(var2) : this.method338(var2);
   }

   private double lambda$onRender$2(List var1) {
      return this.method338(var1);
   }

   private static boolean lambda$onRender$1(PlayerEntity var0) {
      return !pt.method1561(var0);
   }

   private boolean lambda$onSendMovementPackets$0(AbstractClientPlayerEntity var1) {
      return var1 != mc.player && mc.player.distanceTo(var1) <= (float)this.field626.method434().intValue();
   }
}
