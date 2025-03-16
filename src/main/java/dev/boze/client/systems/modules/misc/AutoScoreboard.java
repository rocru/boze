package dev.boze.client.systems.modules.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.OpenScreenEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.TargetTracker;
import java.util.HashMap;
import java.util.Map.Entry;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;

public class AutoScoreboard extends Module {
   public static final AutoScoreboard INSTANCE = new AutoScoreboard();
   private final BooleanSetting taunt = new BooleanSetting("Taunt", true, "Sends a message in chat when you kill");
   private final HashMap<String, Pair<Integer, Integer>> field2897 = new HashMap();

   public AutoScoreboard() {
      super("AutoScoreboard", "Keeps track of your scores with others", Category.Misc);
   }

   @EventHandler
   public void method1674(MovementEvent event) {
      for (Entity var6 : TargetTracker.method560()) {
         if (var6 instanceof LivingEntity) {
            LivingEntity var7 = (LivingEntity)var6;
            if (var7.getHealth() <= 0.0F) {
               if (this.field2897.containsKey(var7.getName().getString())) {
                  Pair var8 = (Pair)this.field2897.get(var7.getName().getString());
                  this.field2897.put(var7.getName().getString(), new Pair((Integer)var8.getLeft() + 1, (Integer)var8.getRight()));
                  ChatInstance.method624(this.method1676(var7.getName().getString(), (Integer)var8.getLeft(), (Integer)var8.getRight()));
               } else {
                  this.field2897.put(var7.getName().getString(), new Pair(1, 0));
                  ChatInstance.method624(this.method1676(var7.getName().getString(), 1, 0));
               }

               if (this.taunt.method419()) {
                  Pair var9 = (Pair)this.field2897.get(var7.getName().getString());
                  mc.player.sendMessage(Text.literal(var9.getRight() + "-" + var9.getLeft() + " " + var7.getName().getString()));
               }

               TargetTracker.method594(var7);
            }
         }
      }
   }

   @EventHandler
   public void method1675(OpenScreenEvent event) {
      if (event.screen instanceof DeathScreen) {
         for (Entity var6 : TargetTracker.method560()) {
            if (var6 instanceof LivingEntity) {
               LivingEntity var7 = (LivingEntity)var6;
               if (var7.getHealth() > 0.0F) {
                  if (this.field2897.containsKey(var7.getName().getString())) {
                     Pair var8 = (Pair)this.field2897.get(var7.getName().getString());
                     this.field2897.put(var7.getName().getString(), new Pair((Integer)var8.getLeft(), (Integer)var8.getRight() + 1));
                     ChatInstance.method624(this.method1676(var7.getName().getString(), (Integer)var8.getLeft(), (Integer)var8.getRight()));
                  } else {
                     this.field2897.put(var7.getName().getString(), new Pair(0, 1));
                     ChatInstance.method624(this.method1676(var7.getName().getString(), 0, 1));
                  }
               }
            }
         }
      }
   }

   private String method1676(String var1, int var2, int var3) {
      if (var2 == 0 && var3 == 0) {
         return "You've never killed or died to " + var1 + " before";
      } else if (var2 == 0 && var3 > 1) {
         return "You've never killed " + var1 + " and died to them " + var3 + " times";
      } else if (var3 == 0 && var2 > 1) {
         return "You've killed " + var1 + " " + var2 + " times and have never died to them before";
      } else if (var2 == 1 && var3 == 1) {
         return "You've killed " + var1 + " once and have died to them once";
      } else if (var2 == 1) {
         return "You've killed " + var1 + " once and have died to them " + var3 + " times";
      } else {
         return var3 == 1
            ? "You've killed " + var1 + " " + var2 + " times and have died to them once"
            : "You've killed " + var1 + " " + var2 + " times and have died to them " + var3 + " times";
      }
   }

   @Override
   public JsonObject serialize() {
      JsonObject var3 = super.serialize();
      JsonObject var4 = new JsonObject();
      this.field2897.forEach(AutoScoreboard::lambda$saveLocalData$0);
      var3.add("Scores", var4);
      return var3;
   }

   public AutoScoreboard method1677(JsonObject data) {
      super.method236(data);
      if (!data.has("Scores")) {
         return this;
      } else {
         JsonObject var5 = data.getAsJsonObject("Scores");
         var5.entrySet().forEach(this::lambda$loadLocalData$1);
         return this;
      }
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Module method236(JsonObject jsonObject) {
      return this.method1677(jsonObject);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object deserialize(JsonObject jsonObject) {
      return this.method1677(jsonObject);
   }

   private void lambda$loadLocalData$1(Entry var1) {
      JsonObject var4 = ((JsonElement)var1.getValue()).getAsJsonObject();
      this.field2897.put((String)var1.getKey(), new Pair(var4.get("kills").getAsInt(), var4.get("deaths").getAsInt()));
   }

   private static void lambda$saveLocalData$0(JsonObject var0, String var1, Pair var2) {
      JsonObject var5 = new JsonObject();
      var5.addProperty("kills", (Number)var2.getLeft());
      var5.addProperty("deaths", (Number)var2.getRight());
      var0.add(var1, var5);
   }
}
