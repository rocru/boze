package dev.boze.client.utils.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.systems.modules.render.Tooltips;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Identifier;

public class MapTooltipComponent implements TooltipComponent, BozeTooltipData, IMinecraft {
   private static final Identifier field1060 = Identifier.of("textures/map/map_background.png");
   private final int field1061;

   public MapTooltipComponent(int mapId) {
      this.field1061 = mapId;
   }

   @Override
   public TooltipComponent method498() {
      return this;
   }

   public int getHeight() {
      return (int)(72.0 * Tooltips.INSTANCE.field3760.getValue()) + 2;
   }

   public int getWidth(TextRenderer textRenderer) {
      return (int)(72.0 * Tooltips.INSTANCE.field3760.getValue());
   }

   public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
      double var8 = Tooltips.INSTANCE.field3760.getValue();
      MatrixStack var10 = context.getMatrices();
      var10.push();
      var10.translate((float)x, (float)y, 0.0F);
      var10.scale((float)var8 * 2.0F, (float)var8 * 2.0F, 0.0F);
      var10.scale(1.125F, 1.125F, 0.0F);
      RenderSystem.setShader(GameRenderer::method_34542);
      context.drawTexture(field1060, 0, 0, 0, 0.0F, 0.0F, 64, 64, 64, 64);
      var10.pop();
      Immediate var11 = mc.getBufferBuilders().getEntityVertexConsumers();
      MapState var12 = FilledMapItem.getMapState(new MapIdComponent(this.field1061), mc.world);
      if (var12 != null) {
         var10.push();
         var10.translate((float)x, (float)y, 0.0F);
         var10.scale((float)var8, (float)var8, 0.0F);
         var10.translate(8.0F, 8.0F, 0.0F);
         mc.gameRenderer.getMapRenderer().draw(var10, var11, new MapIdComponent(this.field1061), var12, false, 15728880);
         var11.draw();
         var10.pop();
      }
   }
}
