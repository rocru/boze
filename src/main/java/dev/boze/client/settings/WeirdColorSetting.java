package dev.boze.client.settings;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.ColorArgument;
import dev.boze.client.enums.ColorTypes;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.utils.ColorWrapper;
import mapped.Class5903;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class WeirdColorSetting extends Setting<ColorWrapper> {
   private ColorTypes field934 = ColorTypes.ALL;
   public boolean opacity = false;
   private ColorWrapper field935;
   public final Class5903<?> field936;

   public WeirdColorSetting(String name, ColorWrapper value, String description) {
      super(name, description);
      this.field935 = value.method2135();
      this.field936 = value.field3910;
      this.field935.field3910.method5875(this);
   }

   public WeirdColorSetting(String name, ColorWrapper value, String description, BooleanSupplier visibility) {
      super(name, description, visibility);
      this.field935 = value.method2135();
      this.field936 = value.field3910;
      this.field935.field3910.method5875(this);
   }

   public WeirdColorSetting(String name, ColorWrapper value, String description, Setting<?> parent) {
      super(name, description, parent);
      this.field935 = value.method2135();
      this.field936 = value.field3910;
      this.field935.field3910.method5875(this);
   }

   public WeirdColorSetting(String name, ColorWrapper value, String description, BooleanSupplier visibility, Setting<?> parent) {
      super(name, description, visibility, parent);
      this.field935 = value.method2135();
      this.field936 = value.field3910;
      this.field935.field3910.method5875(this);
   }

   public void method427(ColorTypes types) {
      this.field934 = types;
   }

   public ColorTypes method428() {
      return this.field934;
   }

   public boolean method429(Class5903<?> color) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: getfield dev/boze/client/settings/WeirdColorSetting.field934 Ldev/boze/client/enums/ColorTypes;
      // 04: invokevirtual dev/boze/client/enums/ColorTypes.ordinal ()I
      // 07: tableswitch 52 0 2 25 27 47
      // 20: bipush 1
      // 21: ireturn
      // 22: aload 1
      // 23: instanceof dev/boze/client/utils/render/color/StaticColor
      // 26: ifne 30
      // 29: aload 1
      // 2a: instanceof dev/boze/client/utils/render/color/ChangingColor
      // 2d: ifeq 34
      // 30: bipush 1
      // 31: goto 35
      // 34: bipush 0
      // 35: ireturn
      // 36: aload 1
      // 37: instanceof dev/boze/client/utils/render/color/StaticColor
      // 3a: ireturn
      // 3b: bipush 1
      // 3c: ireturn
   }

   @Override
   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         method403(this.method210().toLowerCase(Locale.ROOT))
            .then(method403("color").then(method402("color", ColorArgument.method984()).executes(this::lambda$build$0)))
      );
      if (this.opacity) {
         builder.then(
            method403(this.method210().toLowerCase(Locale.ROOT))
               .then(method403("opacity").then(method402("opacity", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(this::lambda$build$1)))
         );
         return true;
      } else {
         builder.then(
            method403(this.method210().toLowerCase(Locale.ROOT))
               .then(method403("fill").then(method402("fill", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(this::lambda$build$2)))
         );
         builder.then(
            method403(this.method210().toLowerCase(Locale.ROOT))
               .then(method403("outline").then(method402("outline", FloatArgumentType.floatArg(0.0F, 1.0F)).executes(this::lambda$build$3)))
         );
         return true;
      }
   }

   public ColorWrapper method430() {
      return this.field935;
   }

   public ColorWrapper method431() {
      this.field935.field3910.method5876(this);
      this.field936.method5875(this);
      this.field935 = new ColorWrapper("_default", this.field936, this.field935.field3911, this.field935.field3912);
      return this.field935;
   }

   public ColorWrapper method432(ColorWrapper newVal) {
      this.field935.field3910.method5876(this);
      this.field935 = newVal;
      this.field935.field3910.method5875(this);
      return this.field935;
   }

   @Override
   public NbtCompound save(NbtCompound tag) {
      tag.putString("color", this.field935.field3909);
      tag.putFloat("fillOpacity", this.field935.field3911);
      tag.putFloat("outlineOpacity", this.field935.field3912);
      return tag;
   }

   public ColorWrapper method433(NbtCompound tag) {
      if (tag.contains("color")) {
         String var5 = tag.getString("color");
         if (var5.equals("_default")) {
            this.method431();
            return this.field935;
         }

         Class5903 var6 = (Class5903)Colors.INSTANCE.field2343.get(var5);
         if (var6 != null) {
            this.method432(new ColorWrapper(var5, var6, this.field935.field3911, this.field935.field3912));
         }
      }

      if (tag.contains("fillOpacity")) {
         this.field935.field3911 = tag.getFloat("fillOpacity");
      }

      if (tag.contains("outlineOpacity")) {
         this.field935.field3912 = tag.getFloat("outlineOpacity");
      }

      return this.field935;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object load(NbtCompound nbtCompound) {
      return this.method433(nbtCompound);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object setValue(Object object) {
      return this.method432((ColorWrapper)object);
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object resetValue() {
      return this.method431();
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Object getValue() {
      return this.method430();
   }

   private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
      float var4 = FloatArgumentType.getFloat(var1, "outline");
      this.method430().field3912 = var4;
      return 1;
   }

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      float var4 = FloatArgumentType.getFloat(var1, "fill");
      this.method430().field3911 = var4;
      return 1;
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      float var4 = FloatArgumentType.getFloat(var1, "opacity");
      this.method430().field3911 = var4;
      return 1;
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      String var4 = (String)var1.getArgument("color", String.class);
      Class5903 var5 = (Class5903)Colors.INSTANCE.field2343.get(var4);
      this.method432(new ColorWrapper(var4, var5, this.method430().field3911, this.method430().field3912));
      return 1;
   }
}
