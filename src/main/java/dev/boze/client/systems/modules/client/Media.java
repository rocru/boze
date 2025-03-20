package dev.boze.client.systems.modules.client;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ProtectedNamesSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

import java.util.Locale;

public class Media extends Module {
   public static final Media INSTANCE = new Media();
   private final BooleanSetting field2401 = new BooleanSetting("NameProtect", true, "Hide player's names in nametags");
   public final BooleanSetting field2402 = new BooleanSetting("SkinProtect", true, "Hide player's skins");
   public final ProtectedNamesSetting field2403 = new ProtectedNamesSetting("Usernames", "Protected names/skins");

   private Media() {
      super("Media", "Hide players' names/skins", Category.Client);
   }

   public static String method1341(String s) {
      return INSTANCE.isEnabled() && INSTANCE.field2401.getValue() && INSTANCE.field2403.getValue().containsKey(s.toLowerCase(Locale.ROOT))
         ? (String)INSTANCE.field2403.getValue().get(s.toLowerCase(Locale.ROOT))
         : s;
   }
}
