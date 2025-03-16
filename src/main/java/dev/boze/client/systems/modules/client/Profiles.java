package dev.boze.client.systems.modules.client;

import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.settings.ProfileSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import mapped.Class1201;

public class Profiles extends Module {
   public static final Profiles INSTANCE = new Profiles();
   public final CurrentProfileSetting field762 = new CurrentProfileSetting(
      Class1201.field58, "v2.main.", "Main", "v2.main.default", "Main profile\nEverything unless otherwise stated in the other 3 descriptions\n"
   );
   public final CurrentProfileSetting field763 = new CurrentProfileSetting(
      Class1201.field59,
      "v2.visuals.",
      "Visuals",
      "v2.visuals.default",
      "Visuals profile\n - All modules in Render\n - Render options for Combat/Misc\n - Gui/HUD\n"
   );
   public final CurrentProfileSetting field764 = new CurrentProfileSetting(
      Class1201.field60, "v2.binds.", "Binds", "v2.binds.default", "Binds profile\nAll binds\n"
   );
   public final CurrentProfileSetting field765 = new CurrentProfileSetting(
      Class1201.field61,
      "v2.client.",
      "Client",
      "v2.client.default",
      "Client profile\nAll modules in Client, apart from:\n - Gui/HUD (in visuals)\n - GhostRotations (in main)\n - Options (in main)\nFor all modules:\n - Title\n - Show in ArrayList state\n - Toggle Notifications state\nDon't share unless you know what you're doing!\n"
   );
   public final ProfileSetting field766 = new ProfileSetting(
      "Legacy", "Legacy (1.0) profiles\nSelecting one will migrate it, splitting it into 4 categories\n", Class1201::method2390
   );

   public Profiles() {
      super("Profiles", "Config profile system", Category.Client);
      this.setNotificationLengthLimited();
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return false;
   }
}
