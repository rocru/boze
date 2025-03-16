package dev.boze.client.utils.render;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.ShulkerColorUtil;
import dev.boze.client.utils.StackDeserializer;
import mapped.Class27;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.util.Identifier;

public class PeekScreen extends ShulkerBoxScreen implements IMinecraft {
   private static PeekScreen field1354;
   private final Identifier field1355 = Identifier.of("textures/gui/container/shulker_box.png");
   private final ItemStack[] field1356;
   private final ItemStack field1357;

   @EventHandler
   private static void method2041(MovementEvent var0) {
      if (field1354 != null && mc.currentScreen == null) {
         mc.setScreen(field1354);
         field1354 = null;
         Class27.EVENT_BUS.unsubscribe(PeekScreen.class);
      }
   }

   public PeekScreen(ItemStack storageBlock, ItemStack[] contents) {
      super(new ShulkerBoxScreenHandler(0, mc.player.getInventory(), new SimpleInventory(contents)), mc.player.getInventory(), storageBlock.getName());
      this.field1356 = contents;
      this.field1357 = storageBlock;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      return button == 2 && this.focusedSlot != null && !this.focusedSlot.getStack().isEmpty() && mc.player.currentScreenHandler.getCursorStack().isEmpty()
         ? method589(this.focusedSlot.getStack(), this.field1356, false)
         : false;
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      return false;
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         this.close();
         return true;
      } else {
         return false;
      }
   }

   protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
      RGBAColor var8 = ShulkerColorUtil.method2169(this.field1357);
      RenderSystem.setShader(GameRenderer::method_34542);
      RenderSystem.setShaderColor((float)var8.field408 / 255.0F, (float)var8.field409 / 255.0F, (float)var8.field410 / 255.0F, 1.0F);
      int var9 = (this.width - this.backgroundWidth) / 2;
      int var10 = (this.height - this.backgroundHeight) / 2;
      context.drawTexture(this.field1355, var9, var10, 0, 0, this.backgroundWidth, this.backgroundHeight);
      RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
   }

   public static boolean method589(ItemStack itemStack, ItemStack[] contents, boolean pause) {
      if (!StackDeserializer.method1756(itemStack) && itemStack.getItem() != Items.ENDER_CHEST) {
         return false;
      } else {
         StackDeserializer.method670(itemStack, contents);
         StackDeserializer.method670(itemStack, contents);
         if (pause) {
            field1354 = new PeekScreen(itemStack, contents);
            Class27.EVENT_BUS.subscribe(PeekScreen.class);
         } else {
            mc.setScreen(new PeekScreen(itemStack, contents));
         }

         return true;
      }
   }
}
