package dev.boze.client.manager;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.BozeCommandSource;
import dev.boze.client.command.Command;
import dev.boze.client.command.commands.*;
import dev.boze.client.core.Cache;
import dev.boze.client.core.Version;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;

import java.io.File;
import java.util.*;

public class CommandManager implements IMinecraft {
   private final CommandDispatcher<CommandSource> field2133 = new CommandDispatcher();
   private final CommandSource field2134 = new BozeCommandSource(mc);
   private final List<Command> field2135 = new ArrayList();
   private final Map<Class<? extends Command>, Command> field2136 = new HashMap();

   public void method1137() {
      this.method1142(new AddonsCommand());
      this.method1142(new BaritoneCommand());
      this.method1142(new ModuleBindCommand());
      this.method1142(new BreadcrumbsCommand());
      this.method1142(new CommandInfoCommand());
      this.method1142(new ConfigCommand());
      this.method1142(new DisconnectCommand());
      if (Version.isBeta) {
         this.method1142(new DebugCommand());
      }

      this.method1142(new ArraylistDrawCommand());
      this.method1142(new HClipCommand());
      this.method1142(new PrefixCommand());
      this.method1142(new ModuleRenameCommand());
      this.method1142(new DeactivateModuleCommand());
      this.method1142(new VClipCommand());
      this.method1142(new WatermarkCommand());
      this.method1142(new LocateCommand());
      this.method1142(new SoundReloadCommand());
      this.method1142(new OpenFolderCommand());
      this.method1142(new PeekCommand());
      this.method1142(new ActivateMacroCommand());
      this.method1142(new MacroToggleCommand());
      this.method1142(new LoadCommand());
      this.method1142(new ConfigResetCommand());
      this.method1142(new AutoPilot());
      this.method1142(new EyedropperCommand());
      this.method1142(new NewsCommand());
      this.method1142(new FlytoCommand());
      this.method1142(new TeleportCommand());
      this.method1142(new PaperclipCommand());
      this.method1142(new PluginsCommand());
      this.method1142(new ColorsCommand());
      this.method1142(new ServerInfoCommand());
      this.method1142(new SetSettingsCommand());
      this.method1142(new AutoCraftCommand());
      this.method1142(new FriendsCommand());
      this.method1142(new ProfilesCommand());
      this.method1142(new SearchCommand());
      this.method1142(new Xray());
      this.method1142(new WaypointCommand());
      this.method1142(new WelcomeCommand());
      this.method1142(new SuffixCommand());
      this.method1142(new PathCommand());
      this.field2135.sort(Comparator.comparing(Command::method210));
   }

   public void method1138(String message) throws CommandSyntaxException {
      this.method1139(message, new BozeCommandSource(mc));
   }

   public void method1139(String message, CommandSource source) throws CommandSyntaxException {
      ParseResults var3 = this.field2133.parse(message, source);
      this.field2133.execute(var3);
   }

   public CommandDispatcher<CommandSource> method1140() {
      return this.field2133;
   }

   public CommandSource method1141() {
      return this.field2134;
   }

   public void method1142(Command command) {
      this.field2135.removeIf(CommandManager::lambda$add$0);
      this.field2136.values().removeIf(CommandManager::lambda$add$1);
      if (new File(System.getProperty("user.home"), "Boze").exists() || Cache.get().exists()) {
         command.method619(this.field2133);
      }

      this.field2135.add(command);
      this.field2136.put(command.getClass(), command);
   }

   public int method1143() {
      return this.field2135.size();
   }

   public List<Command> method1144() {
      return this.field2135;
   }

   public <T extends Command> T method1145(Class<T> klass) {
      return (T)this.field2136.get(klass);
   }

   private static boolean lambda$add$1(Command var0, Command var1) {
      return var1.method210().equals(var0.method210());
   }

   private static boolean lambda$add$0(Command var0, Command var1) {
      return var1.method210().equals(var0.method210());
   }
}
