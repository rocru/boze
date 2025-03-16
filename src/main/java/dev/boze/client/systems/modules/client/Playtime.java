package dev.boze.client.systems.modules.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.boze.client.events.GameJoinEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import java.util.HashMap;
import java.util.Map.Entry;
import mapped.Class27;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientStatusC2SPacket.Mode;
import net.minecraft.stat.Stats;

public class Playtime extends Module {
   public static final Playtime INSTANCE = new Playtime();
   public final HashMap<String, Long> field2406 = new HashMap();
   private boolean field2407 = true;

   public Playtime() {
      super("Playtime", "Keeps track of server playtimes", Category.Client);
      this.setEnabled(true);
   }

   @Override
   public void onEnable() {
      this.field2407 = true;
   }

   @EventHandler
   public void method1345(GameJoinEvent event) {
      this.field2407 = true;
   }

   @EventHandler
   public void method1346(MovementEvent event) {
      if (mc.player.age % 20 == 0) {
         if (this.field2407) {
            mc.getNetworkHandler().sendPacket(new ClientStatusC2SPacket(Mode.REQUEST_STATS));
            this.field2407 = false;
         }

         if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null) {
            String var5 = mc.getCurrentServerEntry().address;
            if (mc.player.getStatHandler() != null && mc.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME)) / 20 > 20) {
               this.field2406.put(var5, (long)(mc.player.getStatHandler().getStat(Stats.CUSTOM.getOrCreateStat(Stats.PLAY_TIME)) / 20));
            }
         }
      }
   }

   @Override
   public NbtCompound toTag() {
      try {
         JsonObject var3 = new JsonObject();
         this.field2406.forEach(var3::addProperty);
         ConfigManager.writeFile(Class27.FOLDER, "playtime", var3);
      } catch (Exception var4) {
      }

      return super.toTag();
   }

   @Override
   public Module method235(NbtCompound tag) {
      try {
         JsonObject var4 = ConfigManager.readFile(Class27.FOLDER, "playtime");
         var4.entrySet().forEach(this::lambda$fromTag$0);
      } catch (Exception var5) {
      }

      return super.method235(tag);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object fromTag(NbtCompound nbtCompound) {
      return this.method235(nbtCompound);
   }

   private void lambda$fromTag$0(Entry var1) {
      this.field2406.put((String)var1.getKey(), ((JsonElement)var1.getValue()).getAsLong());
   }
}
