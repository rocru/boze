package dev.boze.client.settings;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.boze.client.systems.modules.GhostModule;
import dev.boze.client.utils.misc.IJsonSerializable2;
import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public abstract class Setting<T> implements ISerializable<T>, IJsonSerializable2<Setting<T>> {
   public final String name;
   public final String desc;
   public boolean descriptionSeen = false;
   private boolean expanded;
   public SettingBlock block = null;
   private BooleanSupplier visibility = Setting::lambda$new$0;
   public final Setting parent;
   protected Consumer<T> callback = null;
   private final ArrayList<Setting<?>> children = new ArrayList();
   private boolean field914 = false;
   private GhostModule field915 = null;

   public Setting(String name, String description) {
      this.name = name;
      this.desc = description;
      this.parent = null;
   }

   public Setting(String name, String description, BooleanSupplier visibility) {
      this.name = name;
      this.desc = description;
      this.visibility = visibility;
      this.parent = null;
   }

   public Setting(String name, String description, Setting parent) {
      this.name = name;
      this.desc = description;
      this.parent = parent;
      if (parent != null) {
         parent.addChild(this);
      }
   }

   public Setting(String name, String description, BooleanSupplier visibility, Setting parent) {
      this.name = name;
      this.desc = description;
      this.visibility = visibility;
      this.parent = parent;
      if (parent != null) {
         parent.addChild(this);
      }
   }

   public boolean hasChildren() {
      return !this.children.isEmpty();
   }

   public void addChild(Setting<?> setting) {
      this.children.add(setting);
   }

   protected String method210() {
      if (this.parent != null) {
         return this.parent.method210() + "." + this.name;
      } else {
         return this.field914 ? "Ghost." + this.name : this.name;
      }
   }

   public boolean isExpanded() {
      return this.expanded;
   }

   public void setExpanded(boolean expanded) {
      this.expanded = expanded;
   }

   public void setVisibility(BooleanSupplier visibility) {
      this.visibility = visibility;
   }

   public boolean method2116() {
      return (this.parent == null || this.parent.isExpanded() && this.parent.method2116())
         && (this.block == null || this.block.method2116())
         && this.visibility.getAsBoolean()
         && (this.field914 ? this.field915.isGhostMode() : this.field915 == null || !this.field915.isGhostMode());
   }

   public void hide() {
      this.visibility = Setting::lambda$hide$1;
   }

   public boolean method2117() {
      return this.field914;
   }

   public final void method401(Consumer<T> callback) {
      this.callback = callback;
   }

   public abstract T getValue();

   public abstract T resetValue();

   public abstract T setValue(T var1);

   public abstract NbtCompound save(NbtCompound var1);

   public abstract T load(NbtCompound var1);

   public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
      return false;
   }

   protected static <T> RequiredArgumentBuilder<CommandSource, T> method402(String name, ArgumentType<T> type) {
      return RequiredArgumentBuilder.argument(name, type);
   }

   protected static LiteralArgumentBuilder<CommandSource> method403(String name) {
      return LiteralArgumentBuilder.literal(name);
   }

   public void method404(GhostModule ghostModule, boolean isGhost) {
      this.field915 = ghostModule;
      this.field914 = isGhost;
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var3 = new NbtCompound();
      var3.putBoolean("Expanded", this.expanded);
      return this.save(var3);
   }

   @Override
   public T fromTag(NbtCompound tag) {
      if (tag.contains("Expanded")) {
         this.expanded = tag.getBoolean("Expanded");
      }

      return this.load(tag);
   }

   @Override
   public JsonObject serialize() {
      JsonObject var3 = new JsonObject();
      var3.addProperty("DescriptionSeen", this.descriptionSeen);
      return var3;
   }

   // $VF: synthetic method
   // $VF: bridge method
   @Override
   public Setting<T> deserialize(JsonObject jsonObject) {
      if (jsonObject.has("DescriptionSeen")) {
         this.descriptionSeen = jsonObject.get("DescriptionSeen").getAsBoolean();
      }

      return this;
   }

   private static boolean lambda$hide$1() {
      return false;
   }

   private static boolean lambda$new$0() {
      return true;
   }
}
