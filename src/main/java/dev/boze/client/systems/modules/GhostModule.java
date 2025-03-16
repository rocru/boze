package dev.boze.client.systems.modules;

import dev.boze.client.core.ErrorLogger;
import dev.boze.client.enums.InteractionMode;
import dev.boze.client.settings.Setting;
import dev.boze.client.utils.IMinecraft;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import dev.boze.client.Boze;

public abstract class GhostModule implements IMinecraft {
   public abstract InteractionMode getInteractionMode();

   public boolean isGhostMode() {
      return this.getInteractionMode() == InteractionMode.Ghost;
   }

   public List<Setting> getSettings() {
      ArrayList var4 = new ArrayList();

      for (Field var8 : this.getClass().getDeclaredFields()) {
         try {
            if (Setting.class.isAssignableFrom(var8.getType())) {
               var8.setAccessible(true);
               Setting var9 = (Setting)var8.get(this);
               var9.method404(this, true);
               var4.add(var9);
            }
         } catch (Exception var10) {
            ErrorLogger.log(var10);
            Boze.LOG.error("Unable to register a setting for a module, this can lead to instability and crashes");
         }
      }

      return var4;
   }
}
