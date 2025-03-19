package dev.boze.client.gui.components.scaled.bottomrow;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.ConfigNBTSerializer;
import dev.boze.client.utils.render.RenderUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class ConfigComponent extends BottomRowScaledComponent {
   private final Module field1992;
   private String field1993;
   private final ArrayList<String> field1994;

   public ConfigComponent(Module module) {
      super(module.getName(), BottomRow.TextAddClose, 0.1, 0.4);
      this.field1992 = module;
      this.field1993 = "";
      this.field1994 = new ArrayList(
         (Collection)Arrays.asList(ConfigManager.get(ConfigType.CONFIG)).stream().filter(ConfigComponent::lambda$new$0).collect(Collectors.toList())
      );
   }

   @Override
   protected int method2010() {
      return this.field1994.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      String var13 = (String)this.field1994.get(index);
      String var14 = var13.substring(0, var13.length() - this.field1992.internalName.length() - 1);
      RenderUtil.field3963
         .method2257(itemX, itemY, itemWidth, itemHeight, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            var14, itemX + BaseComponent.scaleFactor * 6.0, itemY + itemHeight * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
      double var15 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
      Notifications.DELETE.render(var15, itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5, Theme.method1350());
      double var17 = var15 - BaseComponent.scaleFactor * 6.0 - Notifications.SHARE.method2091();
      Notifications.SHARE.render(var17, itemY + itemHeight * 0.5 - Notifications.SHARE.method1614() * 0.5, Theme.method1350());
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      String var18 = (String)this.field1994.get(index);
      double var19 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.DELETE.method2091();
      double var21 = var19 - BaseComponent.scaleFactor * 6.0 - Notifications.SHARE.method2091();
      if (isMouseWithinBounds(
         mouseX,
         mouseY,
         var19,
         itemY + itemHeight * 0.5 - Notifications.DELETE.method1614() * 0.5,
         Notifications.DELETE.method2091(),
         Notifications.DELETE.method1614()
      )) {
         this.method1117(var18);
         return true;
      } else if (isMouseWithinBounds(
         mouseX,
         mouseY,
         var21,
         itemY + itemHeight * 0.5 - Notifications.SHARE.method1614() * 0.5,
         Notifications.SHARE.method2091(),
         Notifications.SHARE.method1614()
      )) {
         this.method1118(var18);
         return true;
      } else {
         this.method1119(var18);
         return true;
      }
   }

   private void method1117(String var1) {
      ConfigManager.delete(var1, ConfigType.CONFIG);
      this.field1994.remove(var1);
      mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   private void method1118(String var1) {
      NbtCompound var5 = ConfigManager.downloadConfig(var1, ConfigType.CONFIG);
      var5.putString("ConfigName", var1);
      String var6 = ConfigManager.publishConfig(var1, var5, "CF");
      if (var6 != null && !var6.isEmpty()) {
         ChatInstance.method624("Config " + var1 + " has been shared with code " + var6);
         ChatInstance.method624("Load it using the command " + Options.method1563() + "load " + var6);
         GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var6);
      } else {
         ChatInstance.method626("Error sharing config: " + var1);
      }

      mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   private void method1119(String var1) {
      NbtCompound var5 = ConfigManager.downloadConfig(var1, ConfigType.CONFIG);
      if (var5.contains("v2.data")) {
         NbtCompound var6 = var5.getCompound("v2.data");
         ConfigNBTSerializer.method2137(this.field1992, var6);
      } else {
         this.field1992.fromTag(var5);
      }

      mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
   }

   @Override
   protected void method1904() {
      this.method1120(this.field1427);
   }

   private void method1120(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         String var5 = var1 + "." + this.field1992.internalName;
         if (!this.field1994.contains(var5)) {
            this.field1994.add(var5);
            ConfigNBTSerializer.method2138(this.field1992, var1);
            this.field1427 = "";
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }
   }

   private static boolean lambda$new$0(Module var0, String var1) {
      return var1.endsWith(var0.internalName);
   }
}
