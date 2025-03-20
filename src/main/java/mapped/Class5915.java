package mapped;

import dev.boze.client.enums.BottomRow;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.BottomRowScaledComponent;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.settings.FriendsSetting;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.render.RenderUtil;
import java.util.ArrayList;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.lwjgl.glfw.GLFW;

public class Class5915 extends BottomRowScaledComponent {
   private final FriendsSetting field5;
   private String field6;
   private final ArrayList<Class3063> field7;

   public Class5915(FriendsSetting setting) {
      super(setting.name, BottomRow.TextAddClose, 0.1, 0.4);
      this.field5 = setting;
      this.field6 = "";
      this.field7 = new ArrayList(setting.getValue());
   }

   @Override
   protected int method2010() {
      return this.field7.size();
   }

   @Override
   protected void method639(DrawContext context, int index, double itemX, double itemY, double itemWidth, double itemHeight) {
      Class3063 var13 = (Class3063)this.field7.get(var5931);
      String var14 = var13.method5992();
      RenderUtil.field3963
         .method2257(var5932, var5933, var5934, var5935, 15, 24, Theme.method1387() ? BaseComponent.scaleFactor * 2.0 : 0.0, Theme.method1347());
      IFontRender.method499()
         .drawShadowedText(
            var14, var5932 + BaseComponent.scaleFactor * 6.0, var5933 + var5935 * 0.5 - IFontRender.method499().method1390() * 0.5, Theme.method1350()
         );
      double var15 = var5932 + var5934 - BaseComponent.scaleFactor * 6.0 - Notifications.PLAYERS_REMOVE.method2091();
      Notifications.PLAYERS_REMOVE.render(var15, var5933 + var5935 * 0.5 - Notifications.PLAYERS_REMOVE.method1614() * 0.5, Theme.method1350());
   }

   @Override
   protected boolean handleItemClick(int index, int button, double itemX, double itemY, double itemWidth, double itemHeight, double mouseX, double mouseY) {
      Class3063 var18 = (Class3063)this.field7.get(index);
      double var19 = itemX + itemWidth - BaseComponent.scaleFactor * 6.0 - Notifications.PLAYERS_REMOVE.method2091();
      if (isMouseWithinBounds(
         mouseX,
         mouseY,
         var19,
         itemY + itemHeight * 0.5 - Notifications.PLAYERS_REMOVE.method1614() * 0.5,
         Notifications.PLAYERS_REMOVE.method2091(),
         Notifications.PLAYERS_REMOVE.method1614()
      )) {
         this.field7.remove(index);
         this.field5.getValue().remove(index);
         mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void method1904() {
      this.method1800(this.field6);
   }

   private void method1800(String var1) {
      if (var1 != null && !var1.isEmpty()) {
         Class3063 var5 = new Class3063(var1);
         if (!this.field7.contains(var5)) {
            this.field7.add(var5);
            this.field5.getValue().add(var5);
            this.field6 = "";
            mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
         }
      }
   }

   @Override
   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      try {
         if (keyCode == 259 && this.field6.length() > 0) {
            this.field6 = this.field6.substring(0, this.field6.length() - 1);
         } else if (keyCode == 257) {
            if (this.field6.length() > 0) {
               this.method1800(this.field6);
            }
         } else if (keyCode == 86 && Class5928.method159(341)) {
            String var7 = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
            if (var7 != null && !var7.isEmpty()) {
               var7.chars().forEach(this::lambda$keyPressed$0);
            }
         }
      } catch (Exception var8) {
      }

      return super.keyPressed(keyCode, scanCode, modifiers);
   }

   @Override
   public void method583(char c) {
      if (this.field6.length() < 26 && var5936 >= 'a' && var5936 <= 'z'
         || var5936 >= 'A' && var5936 <= 'Z'
         || var5936 >= '0' && var5936 <= '9'
         || var5936 == '_') {
         this.field6 = this.field6 + var5936;
      }
   }

   private void lambda$keyPressed$0(int var1) {
      char var5 = (char)var1;
      if (var5 == '\n') {
         if (this.field6.length() > 0) {
            this.method1800(this.field6);
         }
      } else {
         this.method583(var5);
      }
   }
}
