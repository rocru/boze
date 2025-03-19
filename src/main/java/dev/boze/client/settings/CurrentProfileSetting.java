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

   public String method1322() {
      return this.field970;
   }

   public String method1562() {
      return this.field970;
   }

   public String method1341(String newVal) {
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
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(this::lambda$build$0)));
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("load").then(method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$1)))
      );
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("current").executes(this::lambda$build$2)));
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("save").then(method402("profile", StringArgumentType.word()).executes(this::lambda$build$3)))
      );
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("delete").then(method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$5)))
      );
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("share").then(method402("profile", ProfileListArgument.method1012(this)).executes(this::lambda$build$7)))
      );
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   public String method1286(NbtCompound tag) {
      return this.field970;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method1286(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method1341((String)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method1562();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method1322();
   }

   private int lambda$build$7(CommandContext var1) throws CommandSyntaxException {
      String var5 = ProfileListArgument.method1014(var1, "profile");
      String var6 = this.field969 + var5;
      if (this.field968.contains(var6)) {
         BozeExecutor.method2200(this::lambda$build$6);
      } else {
         this.method1337("Profile (highlight)" + var5 + "(default) doesn't exist");
      }

      return 1;
   }

   private void lambda$build$6(String var1, String var2) {
      NbtCompound var6 = new NbtCompound();
      var6.putString("name", var1);
      var6.putString("prefix", this.field969);
      var6.putString("version", Version.tag);
      var6.putLong("timestamp", System.currentTimeMillis());
      NbtCompound var7 = ConfigManager.downloadConfig(var2, ConfigType.PROFILE);
      NbtCompound var8 = new NbtCompound();
      var8.put("v2.info", var6);
      var8.put("v2.profile", var7);
      String var9 = ConfigManager.publishConfig(var2, var8, "PR");
      if (var9 != null && !var9.isEmpty()) {
         this.method1800("Shared profile (highlight)" + var1);
         this.method1800("Load it using the command (highlight)" + Options.method1563() + "load " + var9);
         GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var9);
      } else {
         this.method1337("Error sharing (highlight)" + var1);
      }
   }

   private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
      String var5 = ProfileListArgument.method1014(var1, "profile");
      String var6 = this.field969 + var5;
      if (var6.equals(this.field970)) {
         this.method1337("Profile (highlight)" + var5 + "(default) is loaded, so it can't be deleted");
      } else if (this.field968.contains(var6)) {
         this.field968.remove(var6);
         BozeExecutor.method2200(CurrentProfileSetting::lambda$build$4);
         this.method1800("Deleted profile (highlight)" + var5);
      } else {
         this.method1337("Profile (highlight)" + var5 + "(default) doesn't exist");
      }

      return 1;
   }

   private static void lambda$build$4(String var0) {
      ConfigManager.delete(var0, ConfigType.PROFILE);
   }

   private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
      String var5 = StringArgumentType.getString(var1, "profile");
      String var6 = this.field969 + var5;
      if (this.field968.contains(var6)) {
         this.method1337("Profile (highlight)" + var5 + "(default) already exists");
      } else {
         this.field968.add(var6);
         this.method1341(var6);
         Class1201.method2382(true, this, var6);
         this.method1800("Saved profile (highlight)" + var5);
      }

      return 1;
   }

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      this.method1800("Current " + this.name.toLowerCase(Locale.ROOT) + " profile: (highlight)" + this.field970.substring(this.field970.lastIndexOf(46) + 1));
      return 1;
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      String var5 = ProfileListArgument.method1014(var1, "profile");
      String var6 = this.field969 + var5;
      if (var6.equals(this.field970)) {
         this.method1750("Profile (highlight)" + var5 + "(default) already loaded");
      } else {
         Class1201.method2381(this, var6);
         this.method1800("Loaded profile (highlight)" + var5);
      }

      return 1;
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      this.method1800(this.name + " Profiles: " + this.field968.size());

      for (String var6 : this.field968) {
         this.method1800(" - (highlight)" + var6.substring(var6.lastIndexOf(46) + 1));
      }

      return 1;
   }
}
