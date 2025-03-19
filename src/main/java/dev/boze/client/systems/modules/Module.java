package dev.boze.client.systems.modules;

import com.google.gson.JsonObject;
import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.enums.ModuleColors;
import dev.boze.client.enums.ModuleState;
import dev.boze.client.enums.NotificationLength;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.hud.core.ArrayListModuleInfo;
import dev.boze.client.systems.modules.misc.SoundFX;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.ColorUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.misc.IJsonSerializable2;
import dev.boze.client.utils.misc.ISerializable;
import mapped.Class5925;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public abstract class Module implements Class5925, ISerializable<Module>, Comparable<Module>, IMinecraft, IJsonSerializable2<Module> {
   protected boolean enabled = false;
   public final String internalName;
   private String name;
   public final String description;
   public boolean field433 = false;
   public final Category category;
   public final RGBAColor rgbaColor;
   public final BozeDrawColor color;
   public final ConfigCategory configCategory;
   public final Bind bind = Bind.create();
   private boolean onlyWhileHolding = false;
   private boolean visible = true;
   private boolean notify = false;
   private boolean field434 = false;
   public boolean field435 = false;
   public ModuleState moduleState = null;
   private NotificationLength notificationLength = NotificationLength.Normal;
   private Supplier<ScaledBaseComponent> field436 = null;
   private final List<Setting> field437 = new ArrayList();
   private final List<Setting> field438 = new ArrayList();

   public NotificationLength getNotificationLength() {
      return this.notificationLength;
   }

   public Supplier<ScaledBaseComponent> method218() {
      return this.field436;
   }

   protected void setNotificationLengthLimited() {
      this.notificationLength = NotificationLength.Limited;
   }

   protected void method219(Supplier<ScaledBaseComponent> popupSupplier) {
      this.notificationLength = NotificationLength.Popup;
      this.field436 = popupSupplier;
   }

   protected void addSettings(Setting<?>... settings) {
      this.field438.addAll(Arrays.asList(settings));
   }

   public Module(String name, String description, Category category) {
      this(name, description, category, category.configCategory);
   }

   public Module(String name, String description, Category category, ConfigCategory configCategory) {
      this.internalName = name;
      this.name = name;
      this.description = description;
      this.category = category;
      this.configCategory = configCategory;
      this.rgbaColor = ColorUtil.generateRandomColor(category);
      this.color = new BozeDrawColor(this.rgbaColor.field408, this.rgbaColor.field409, this.rgbaColor.field410, this.rgbaColor.field411);
      if (this.method221() != null
         || category == Category.Legit
         || category == Category.Client
         || category == Category.Render
         || category == Category.Graph
         || category == Category.Hud) {
         this.field435 = true;
      }
   }

   public void onEnable() {
   }

   public void onDisable() {
   }

   public boolean toggle() {
      return this.setEnabled(!this.enabled);
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public boolean setEnabled(boolean newState) {
      if (newState != this.enabled) {
         if (newState) {
            if (Options.INSTANCE.method1971() && !this.field435 && this.method221() == null) {
               return false;
            }

            Boze.LOG.debug("Enabling module " + this.internalName);
            this.enabled = true;
            Boze.EVENT_BUS.subscribe(this);
            this.onEnable();
         } else {
            Boze.LOG.debug("Disabling module " + this.internalName);
            this.enabled = false;
            Boze.EVENT_BUS.unsubscribe(this);
            this.onDisable();
         }

         if (dev.boze.client.systems.modules.hud.core.ArrayList.INSTANCE != null
            && dev.boze.client.systems.modules.hud.core.ArrayList.INSTANCE.isEnabled()
            && dev.boze.client.systems.modules.hud.core.ArrayList.INSTANCE.field651.method419()) {
            dev.boze.client.systems.modules.hud.core.ArrayList.INSTANCE
               .field659
               .put(new ArrayListModuleInfo(this.internalName, this.method1322(), this.enabled), System.currentTimeMillis());
         }

         if (SoundFX.INSTANCE.isEnabled()) {
            SoundFX.INSTANCE.method1771(newState);
         }

         return true;
      } else {
         return false;
      }
   }

   public String getName() {
      return this.name;
   }

   public void setName(String title) {
      this.name = title;
   }

   public boolean getHoldBind() {
      return this.onlyWhileHolding;
   }

   public void setHoldBind(boolean onlyWhileHolding) {
      this.onlyWhileHolding = onlyWhileHolding;
   }

   public boolean getVisibility() {
      return this.visible;
   }

   public void setVisibility(boolean visible) {
      this.visible = visible;
   }

   public boolean getNotify() {
      return this.notify;
   }

   public void setNotify(boolean notify) {
      this.notify = notify;
   }

   public String method1322() {
      return "";
   }

   public GhostModule method221() {
      return null;
   }

   public boolean method222() {
      return this.method221() != null && this.method221().isGhostMode();
   }

   public List<Setting> method1144() {
      return this.field437;
   }

   private void method223(NbtCompound var1) {
      var1.putBoolean("state", this.enabled);
      if (!this.field435 && this.moduleState != null) {
         var1.putBoolean("prevState", this.moduleState == ModuleState.On);
      }
   }

   private void method224(NbtCompound var1) {
      this.method223(var1);
      NbtCompound var5 = new NbtCompound();

      for (Setting var7 : this.field437) {
         if (!(var7 instanceof SettingBlock)
            && !this.field438.contains(var7)
            && (var7.parent == null || !this.field438.contains(var7.parent))
            && (var7.block == null || !this.field438.contains(var7.block))) {
            var5.put(
               (var7.method2117() ? "ghost." : "")
                  + (var7.block == null ? "" : var7.block.name + ".")
                  + (var7.parent == null ? var7.name : var7.parent.name + "." + var7.name),
               var7.toTag()
            );
         }
      }

      if (!var5.isEmpty()) {
         var1.put("settings", var5);
      }
   }

   @Override
   public NbtCompound method225() {
      if (this.configCategory != ConfigCategory.Visuals && this.configCategory != ConfigCategory.Client) {
         NbtCompound var4 = new NbtCompound();
         this.method224(var4);
         return var4;
      } else {
         return null;
      }
   }

   @Override
   public NbtCompound method226() {
      NbtCompound var4 = new NbtCompound();
      var4.putBoolean("visible", this.visible);
      var4.putBoolean("notify", this.notify);
      if (this.configCategory == ConfigCategory.Visuals) {
         this.method223(var4);
         NbtCompound var5 = new NbtCompound();

         for (Setting var7 : this.field437) {
            if (!(var7 instanceof SettingBlock)) {
               var5.put(
                  (var7.method2117() ? "ghost." : "")
                     + (var7.block == null ? "" : var7.block.name + ".")
                     + (var7.parent == null ? var7.name : var7.parent.name + "." + var7.name),
                  var7.toTag()
               );
            }
         }

         if (!var5.isEmpty()) {
            var4.put("settings", var5);
         }
      } else {
         NbtCompound var8 = new NbtCompound();

         for (Setting var10 : this.field437) {
            if (!(var10 instanceof SettingBlock)
               && (
                  this.field438.contains(var10)
                     || var10.parent != null && this.field438.contains(var10.parent)
                     || var10.block != null && this.field438.contains(var10.block)
               )) {
               var8.put(
                  (var10.method2117() ? "ghost." : "")
                     + (var10.block == null ? "" : var10.block.name + ".")
                     + (var10.parent == null ? var10.name : var10.parent.name + "." + var10.name),
                  var10.toTag()
               );
            }
         }

         if (!var8.isEmpty()) {
            var4.put("settings", var8);
         }
      }

      return var4;
   }

   @Override
   public NbtCompound method227() {
      NbtCompound var4 = new NbtCompound();
      var4.put("bind", this.bind.toTag());
      var4.putBoolean("onlyWhenHolding", this.onlyWhileHolding);
      NbtCompound var5 = new NbtCompound();

      for (Setting var7 : this.method1144()) {
         if (var7 instanceof BindSetting) {
            var5.put(var7.name, var7.toTag());
         }
      }

      if (!var5.isEmpty()) {
         var4.put("settings", var5);
      }

      return var4;
   }

   @Override
   public NbtCompound method228() {
      NbtCompound var4 = new NbtCompound();
      if (this.configCategory == ConfigCategory.Client) {
         this.method224(var4);
      }

      var4.putString("title", this.name);
      if (OldColors.method1344() == ModuleColors.Consistent) {
         var4.put("moduleColor", this.rgbaColor.toTag());
      }

      var4.putBoolean("expanded", this.field434);
      return var4;
   }

   private void method229(NbtCompound var1) {
      if (var1.contains("state")) {
         this.setEnabled(var1.getBoolean("state"));
      }

      if (!this.field435 && var1.contains("prevState")) {
         this.moduleState = var1.getBoolean("prevState") ? ModuleState.On : ModuleState.Off;
      }
   }

   private void method230(NbtCompound var1, Setting<?> var2) {
      String var6 = (var2.method2117() ? "ghost." : "")
         + (var2.block == null ? "" : var2.block.name + ".")
         + (var2.parent == null ? var2.name : var2.parent.name + "." + var2.name);
      if (var1.contains(var6)) {
         var2.fromTag(var1.getCompound(var6));
      }
   }

   private void method231(NbtCompound var1) {
      this.method229(var1);
      if (var1.contains("settings")) {
         NbtCompound var5 = var1.getCompound("settings");

         for (Setting var7 : this.field437) {
            if (!(var7 instanceof SettingBlock)
               && !this.field438.contains(var7)
               && (var7.parent == null || !this.field438.contains(var7.parent))
               && (var7.block == null || !this.field438.contains(var7.block))) {
               this.method230(var5, var7);
            }
         }
      }
   }

   @Override
   public void method232(NbtCompound tag) {
      if (this.configCategory != ConfigCategory.Visuals && this.configCategory != ConfigCategory.Client) {
         this.method231(tag);
      }
   }

   @Override
   public void method394(NbtCompound tag) {
      if (tag.contains("visible")) {
         this.visible = tag.getBoolean("visible");
      }

      if (tag.contains("notify")) {
         this.notify = tag.getBoolean("notify");
      }

      if (this.configCategory == ConfigCategory.Visuals) {
         this.method229(tag);
         if (tag.contains("settings")) {
            NbtCompound var5 = tag.getCompound("settings");

            for (Setting var7 : this.field437) {
               if (!(var7 instanceof SettingBlock)) {
                  this.method230(var5, var7);
               }
            }
         }
      } else if (tag.contains("settings")) {
         NbtCompound var8 = tag.getCompound("settings");

         for (Setting var10 : this.field437) {
            if (!(var10 instanceof SettingBlock)
               && (
                  this.field438.contains(var10)
                     || var10.parent != null && this.field438.contains(var10.parent)
                     || var10.block != null && this.field438.contains(var10.block)
               )) {
               this.method230(var8, var10);
            }
         }
      }
   }

   @Override
   public void method233(NbtCompound tag) {
      if (tag.contains("bind")) {
         this.bind.method180(tag.getCompound("bind"));
      }

      if (tag.contains("onlyWhenHolding")) {
         this.onlyWhileHolding = tag.getBoolean("onlyWhenHolding");
      }

      if (tag.contains("settings")) {
         NbtCompound var5 = tag.getCompound("settings");

         for (Setting var7 : this.method1144()) {
            if (var7 instanceof BindSetting) {
               this.method230(var5, var7);
            }
         }
      }
   }

   @Override
   public void method234(NbtCompound tag) {
      if (this.configCategory == ConfigCategory.Client) {
         this.method231(tag);
      }

      if (tag.contains("title")) {
         this.name = tag.getString("title");
      }

      if (tag.contains("moduleColor")) {
         this.rgbaColor.fromTag(tag.getCompound("moduleColor"));
      }

      if (tag.contains("visible")) {
         this.visible = tag.getBoolean("visible");
      }

      if (tag.contains("notify")) {
         this.notify = tag.getBoolean("notify");
      }

      if (tag.contains("expanded")) {
         this.field434 = tag.getBoolean("expanded");
      }
   }

   @Override
   public NbtCompound toTag() {
      NbtCompound var4 = new NbtCompound();
      var4.putBoolean("State", this.enabled);
      if (!this.field435 && this.moduleState != null) {
         var4.putBoolean("PrevState", this.moduleState == ModuleState.On);
      }

      var4.putString("Title", this.name);
      if (OldColors.method1344() == ModuleColors.Consistent) {
         var4.put("ModuleColor", this.rgbaColor.toTag());
      }

      var4.put("Keybind", this.bind.toTag());
      var4.putBoolean("OnlyWhileHolding", this.onlyWhileHolding);
      var4.putBoolean("Visible", this.visible);
      var4.putBoolean("Notify", this.notify);
      var4.putBoolean("Expanded", this.field434);

      for (Setting var6 : this.field437) {
         if (!(var6 instanceof SettingBlock)) {
            var4.put((var6.method2117() ? "Ghost." : "") + (var6.parent == null ? var6.name : var6.parent.name + "." + var6.name), var6.toTag());
         }
      }

      return var4;
   }

   @Override
   public Module fromTag(NbtCompound tag) {
      if (tag.contains("State")) {
         try {
            this.setEnabled(tag.getBoolean("State"));
         } catch (Exception var16) {
            ErrorLogger.log(var16);
            Boze.LOG.warn("Unable to load module state from config");
         }
      }

      if (!this.field435 && tag.contains("PrevState")) {
         this.moduleState = tag.getBoolean("PrevState") ? ModuleState.On : ModuleState.Off;
      }

      if (tag.contains("Title")) {
         try {
            this.name = tag.getString("Title");
         } catch (Exception var15) {
            ErrorLogger.log(var15);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s title from config");
         }
      }

      if (tag.contains("ModuleColor")) {
         try {
            this.rgbaColor.fromTag(tag.getCompound("ModuleColor"));
         } catch (Exception var14) {
            ErrorLogger.log(var14);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s color from config");
         }
      }

      if (tag.contains("OnlyWhileHolding")) {
         try {
            this.onlyWhileHolding = tag.getBoolean("OnlyWhileHolding");
         } catch (Exception var13) {
            ErrorLogger.log(var13);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s keybind type from config");
         }
      }

      if (tag.contains("Visible")) {
         try {
            this.visible = tag.getBoolean("Visible");
         } catch (Exception var12) {
            ErrorLogger.log(var12);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s visibility from config");
         }
      }

      if (tag.contains("Notify")) {
         try {
            this.notify = tag.getBoolean("Notify");
         } catch (Exception var11) {
            ErrorLogger.log(var11);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s notifiability from config");
         }
      }

      if (tag.contains("Expanded")) {
         try {
            this.field434 = tag.getBoolean("Expanded");
         } catch (Exception var10) {
            ErrorLogger.log(var10);
            Boze.LOG.warn("Unable to load module " + this.internalName + "'s gui state from config");
         }
      }

      for (Setting var6 : this.field437) {
         if (!(var6 instanceof SettingBlock)) {
            String var7 = (var6.method2117() ? "Ghost." : "") + (var6.parent == null ? var6.name : var6.parent.name + "." + var6.name);
            if (tag.contains(var7)) {
               try {
                  var6.fromTag(tag.getCompound(var7));
               } catch (Exception var9) {
                  ErrorLogger.log(var9);
                  Boze.LOG.warn("Setting " + var6.name + " in module " + this.internalName + " could not be loaded from config");
               }
            }
         }
      }

      return this;
   }

   @Override
   public JsonObject serialize() {
      JsonObject var4 = new JsonObject();
      var4.addProperty("DescriptionSeen", this.field433);

      for (Setting var6 : this.field437) {
         var4.add((var6.method2117() ? "Ghost." : "") + (var6.parent == null ? var6.name : var6.parent.name + "." + var6.name), var6.serialize());
      }

      return var4;
   }

   @Override
   public Module deserialize(JsonObject data) {
      if (data.has("DescriptionSeen")) {
         this.field433 = data.get("DescriptionSeen").getAsBoolean();
      }

      for (Setting var6 : this.field437) {
         String var7 = (var6.method2117() ? "Ghost." : "") + (var6.parent == null ? var6.name : var6.parent.name + "." + var6.name);
         if (data.has(var7)) {
            try {
               var6.deserialize(data.getAsJsonObject(var7));
            } catch (Exception var9) {
               ErrorLogger.log(var9);
               Boze.LOG.warn("Setting " + var6.name + " in module " + this.internalName + " could not be loaded from local data");
            }
         }
      }

      return this;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         Module var5 = (Module)o;
         return Objects.equals(this.internalName, var5.internalName);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.internalName});
   }

   @Override
   public int compareTo(@NotNull Module o) {
      return this.internalName.compareTo(o.internalName);
   }

   // $VF: synthetic method
   // $VF: bridge method
   //@Override
   //public Object fromTag(NbtCompound nbtCompound) {
   //   return this.method235(nbtCompound);
   //}

   // $VF: synthetic method
   // $VF: bridge method
   //public int compareTo(@NotNull Object object) {
   //   return this.compareModule((Module)object);
   //}

   // $VF: synthetic method
   // $VF: bridge method
  // @Override
   //public Object deserialize(JsonObject jsonObject) {
   //   return this.method236(jsonObject);
   //}
}
