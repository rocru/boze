package dev.boze.client.settings;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.ProfileListArgument;
import dev.boze.client.core.Version;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.network.BozeExecutor;
import mapped.Class1201;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;
import java.util.Locale;

public class CurrentProfileSetting extends Setting<String> implements IMinecraft {
   public final HashSet<String> field968;
   public final String field969;
   private String field970;
   private final String field971;

   public CurrentProfileSetting(HashSet<String> list, String prefix, String name, String value, String description) {
      super(name, description);
      this.field970 = value;
      this.field971 = value;
      this.field968 = list;
      this.field969 = prefix;
   }

   @Override
   public String getValue() {
      return this.field970;
   }

   @Override
   public String resetValue() {
      return this.field970;
   }

   @Override
   public String setValue(String newVal) {
      return this.field970 = newVal;
   }

   private void method1800(String var1) {
      ChatInstance.method740("Profiles - " + this.name, var1);
   }

   private void method1750(String var1) {
      ChatInstance.method742("Profiles - " + this.name, var1);
   }

   private void method1337(String var1) {
      ChatInstance.method743("Profiles - " + this.name, var1);
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("list").executes(this::lambda$build$0)));
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("load").then(CurrentProfileSetting.method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$1))));
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("current").executes(this::lambda$build$2)));
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("save").then(CurrentProfileSetting.method402("profile", StringArgumentType.word()).executes(this::lambda$build$3))));
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("delete").then(CurrentProfileSetting.method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$5))));
      builder.then(CurrentProfileSetting.method403(this.method210().toLowerCase(Locale.ROOT)).then(CurrentProfileSetting.method403("share").then(CurrentProfileSetting.method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$7))));
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   @Override
   public String load(NbtCompound tag) {
      return this.field970;
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object load(NbtCompound nbtCompound) {
   //   return this.method1286(nbtCompound);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object setValue(Object object) {
   //   return this.method1341((String)object);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object resetValue() {
   //   return this.method1562();
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object getValue() {
   //   return this.method1322();
   //}

   private int lambda$build$7(CommandContext commandContext) throws CommandSyntaxException {
      String string = ProfileListArgument.method1014(commandContext, "profile");
      String string2 = this.field969 + string;
      if (this.field968.contains(string2)) {
         BozeExecutor.method2200(() -> this.lambda$build$6(string, string2));
      } else {
         this.method1337("Profile (highlight)" + string + "(default) doesn't exist");
      }
      return 1;
   }

   private void lambda$build$6(String string, String string2) {
      NbtCompound nbtCompound = new NbtCompound();
      nbtCompound.putString("name", string);
      nbtCompound.putString("prefix", this.field969);
      nbtCompound.putString("version", Version.tag);
      nbtCompound.putLong("timestamp", System.currentTimeMillis());
      NbtCompound nbtCompound2 = ConfigManager.downloadConfig(string2, ConfigType.PROFILE);
      NbtCompound nbtCompound3 = new NbtCompound();
      nbtCompound3.put("v2.info", nbtCompound);
      nbtCompound3.put("v2.profile", nbtCompound2);
      String string3 = ConfigManager.publishConfig(string2, nbtCompound3, "PR");
      if (string3 == null || string3.isEmpty()) {
         this.method1337("Error sharing (highlight)" + string);
      } else {
         this.method1800("Shared profile (highlight)" + string);
         this.method1800("Load it using the command (highlight)" + Options.method1563() + "load " + string3);
         GLFW.glfwSetClipboardString((long)mc.getWindow().getHandle(), (CharSequence)string3);
      }
   }

   private int lambda$build$5(CommandContext commandContext) throws CommandSyntaxException {
      String string = ProfileListArgument.method1014(commandContext, "profile");
      String string2 = this.field969 + string;
      if (string2.equals(this.field970)) {
         this.method1337("Profile (highlight)" + string + "(default) is loaded, so it can't be deleted");
      } else if (this.field968.contains(string2)) {
         this.field968.remove(string2);
         BozeExecutor.method2200(() -> CurrentProfileSetting.lambda$build$4(string2));
         this.method1800("Deleted profile (highlight)" + string);
      } else {
         this.method1337("Profile (highlight)" + string + "(default) doesn't exist");
      }
      return 1;
   }

   private static void lambda$build$4(String string) {
      ConfigManager.delete(string, ConfigType.PROFILE);
   }

   private int lambda$build$3(CommandContext commandContext) throws CommandSyntaxException {
      String string = StringArgumentType.getString((CommandContext)commandContext, (String)"profile");
      String string2 = this.field969 + string;
      if (this.field968.contains(string2)) {
         this.method1337("Profile (highlight)" + string + "(default) already exists");
      } else {
         this.field968.add(string2);
         this.setValue(string2);
         Class1201.method2382(true, this, string2);
         this.method1800("Saved profile (highlight)" + string);
      }
      return 1;
   }

   private int lambda$build$2(CommandContext commandContext) throws CommandSyntaxException {
      this.method1800("Current " + this.name.toLowerCase(Locale.ROOT) + " profile: (highlight)" + this.field970.substring(this.field970.lastIndexOf(46) + 1));
      return 1;
   }

   private int lambda$build$1(CommandContext commandContext) throws CommandSyntaxException {
      String string = ProfileListArgument.method1014(commandContext, "profile");
      String string2 = this.field969 + string;
      if (string2.equals(this.field970)) {
         this.method1750("Profile (highlight)" + string + "(default) already loaded");
      } else {
         Class1201.method2381(this, string2);
         this.method1800("Loaded profile (highlight)" + string);
      }
      return 1;
   }

   private int lambda$build$0(CommandContext commandContext) throws CommandSyntaxException {
      this.method1800(this.name + " Profiles: " + this.field968.size());
      for (String string : this.field968) {
         this.method1800(" - (highlight)" + string.substring(string.lastIndexOf(46) + 1));
      }
      return 1;
   }
}
