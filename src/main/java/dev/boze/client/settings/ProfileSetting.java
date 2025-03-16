package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.ProfileArgument;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.client.Profiles;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import mapped.Class1201;
import dev.boze.client.Boze;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

public class ProfileSetting extends Setting<String> {
   public ProfileSetting(String name, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
   }

   public String method1322() {
      return "";
   }

   public String method1562() {
      return "";
   }

   public String method1341(String newVal) {
      return "";
   }

   private void method1800(String var1) {
      ChatInstance.method740("Profiles - Legacy", var1);
   }

   private void method1750(String var1) {
      ChatInstance.method743("Profiles - Legacy", var1);
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(this::lambda$build$0)));
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("migrate").then(method402("profile", ProfileArgument.method1009()).executes(this::lambda$build$1)))
      );
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("delete").then(method402("profile", ProfileArgument.method1009()).executes(this::lambda$build$2)))
      );
      return true;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      return tag;
   }

   public String method1286(NbtCompound tag) {
      return "";
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

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      String var5 = ProfileArgument.method1010(var1, "profile");
      if (!Class1201.field57.contains(var5)) {
         this.method1750("Profile (highlight)" + var5 + "(default) doesn't exist");
         return 1;
      } else {
         ConfigManager.delete(var5, ConfigType.PROFILE);
         Class1201.field57.remove(var5);
         this.method1800("Deleted profile (highlight)" + var5);
         return 1;
      }
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      String var5 = ProfileArgument.method1010(var1, "profile");
      if (!Class1201.field57.contains(var5)) {
         this.method1750("Profile (highlight)" + var5 + "(default) doesn't exist");
         return 1;
      } else {
         String var6 = "v2.main." + var5;
         String var7 = "v2.visuals." + var5;
         String var8 = "v2.binds." + var5;
         String var9 = "v2.client." + var5;
         Profiles.INSTANCE.field762.method1341(var6);
         Profiles.INSTANCE.field763.method1341(var7);
         Profiles.INSTANCE.field764.method1341(var8);
         Profiles.INSTANCE.field765.method1341(var9);
         Boze.getModules().method398(ConfigManager.downloadConfig(var5, ConfigType.PROFILE), true);
         Class1201.method2384(false, var6, var7, var8, var9);
         this.method1800("Migrated profile " + var5);
         return 1;
      }
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      this.method1800("Legacy Profiles: " + Class1201.field57.size());

      for (String var6 : Class1201.field57) {
         this.method1800(" - (highlight)" + var6);
      }

      return 1;
   }
}
