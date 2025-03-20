package dev.boze.client.systems.modules.render;

import dev.boze.client.events.BossBarIteratorEvent;
import dev.boze.client.events.BossBarNameEvent;
import dev.boze.client.events.BossBarSpacingEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.WeakHashMap;

public class BossStack extends Module {
   public static final BossStack INSTANCE = new BossStack();
   private BooleanSetting field3419 = new BooleanSetting("Stack", true, "Stack the bars");
   private BooleanSetting field3420 = new BooleanSetting("HideName", true, "Hide the boss' names");
   public static final WeakHashMap<ClientBossBar, Integer> field3421 = new WeakHashMap();

   public BossStack() {
      super("BossStack", "Stacks bo$$ bars", Category.Render);
   }

   @EventHandler
   private void method1907(BossBarNameEvent var1) {
      if (this.field3420.getValue()) {
         var1.text = Text.of("");
      } else if (!field3421.isEmpty() && this.field3419.getValue()) {
         ClientBossBar var5 = var1.field1906;
         Integer var6 = (Integer)field3421.get(var5);
         field3421.remove(var5);
         if (var6 != null && !this.field3420.getValue()) {
            var1.text = var1.text.copyContentOnly().append(" (" + var6 + ")");
         }
      }
   }

   @EventHandler
   private void method1908(BossBarSpacingEvent var1) {
      if (this.field3420.getValue()) {
         var1.field1908 = 0;
      }
   }

   @EventHandler
   private void method1909(BossBarIteratorEvent var1) {
      if (this.field3419.getValue()) {
         HashMap var4 = new HashMap();
         var1.field1904.forEachRemaining(BossStack::lambda$onGetBars$1);
         var1.field1904 = var4.values().iterator();
      }
   }

   private static void lambda$onGetBars$1(HashMap var0, ClientBossBar var1) {
      String var5 = var1.getName().getString();
      if (var0.containsKey(var5)) {
         field3421.compute((ClientBossBar)var0.get(var5), BossStack::lambda$onGetBars$0);
      } else {
         var0.put(var5, var1);
      }
   }

   private static Integer lambda$onGetBars$0(ClientBossBar var0, Integer var1) {
      return var1 == null ? 2 : var1 + 1;
   }
}
