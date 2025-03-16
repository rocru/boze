package dev.boze.client.systems.modules.legit;

import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import net.minecraft.entity.attribute.EntityAttributes;

public class Reach extends Module {
   public static final Reach INSTANCE = new Reach();
   private final MinMaxSetting field2819 = new MinMaxSetting("AttackReach", 3.1F, 0.0, 6.0, 0.1F, "Attack reach length");
   private final FloatSetting field2820 = new FloatSetting("BlockReach", 5.0F, 0.0F, 10.0F, 0.1F, "Block reach length");

   public Reach() {
      super("Reach", "Reach further", Category.Legit);
   }

   public static double method1613() {
      return !INSTANCE.isEnabled() ? mc.player.getAttributeValue(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE) : INSTANCE.field2819.getValue();
   }

   public static double method1614() {
      return !INSTANCE.isEnabled()
         ? mc.player.getAttributeValue(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE)
         : (double)INSTANCE.field2820.method423().floatValue();
   }
}
