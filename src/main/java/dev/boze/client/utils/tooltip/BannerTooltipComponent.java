package dev.boze.client.utils.tooltip;

import dev.boze.client.systems.modules.render.Tooltips;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;

public class BannerTooltipComponent implements TooltipComponent, BozeTooltipData, IMinecraft {
   private final ItemStack field1287;
   private final ModelPart field1288;

   public BannerTooltipComponent(ItemStack banner) {
      this.field1287 = banner;
      this.field1288 = mc.getEntityModelLoader().getModelPart(EntityModelLayers.BANNER).getChild("flag");
   }

   @Override
   public TooltipComponent method498() {
      return this;
   }

   public int getHeight() {
      return (int)(160.0 * Tooltips.INSTANCE.field3762.getValue()) - 2;
   }

   public int getWidth(TextRenderer textRenderer) {
      return (int)(80.0 * Tooltips.INSTANCE.field3762.getValue());
   }

   public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
      DiffuseLighting.disableGuiDepthLighting();
      MatrixStack var8 = context.getMatrices();
      var8.push();
      var8.translate(
         (float)x + 8.0F * Tooltips.INSTANCE.field3762.getValue().floatValue(), (float)y + 8.0F * Tooltips.INSTANCE.field3762.getValue().floatValue(), 0.0F
      );
      var8.scale(Tooltips.INSTANCE.field3762.getValue().floatValue(), Tooltips.INSTANCE.field3762.getValue().floatValue(), 0.0F);
      var8.push();
      var8.translate(0.5, 16.0, 0.0);
      var8.scale(6.0F, -6.0F, 1.0F);
      var8.scale(2.0F, -2.0F, -2.0F);
      var8.push();
      var8.translate(2.5, 8.5, 0.0);
      var8.scale(5.0F, 5.0F, 5.0F);
      Immediate var9 = mc.getBufferBuilders().getEntityVertexConsumers();
      this.field1288.pitch = 0.0F;
      this.field1288.pivotY = -32.0F;
      BannerBlockEntityRenderer.renderCanvas(
         var8,
         var9,
         15728880,
         OverlayTexture.DEFAULT_UV,
         this.field1288,
         ModelLoader.BANNER_BASE,
         true,
         ((BannerItem)this.field1287.getItem()).getColor(),
         (BannerPatternsComponent)this.field1287.get(DataComponentTypes.BANNER_PATTERNS)
      );
      var8.pop();
      var8.pop();
      var9.draw();
      var8.pop();
      DiffuseLighting.enableGuiDepthLighting();
   }
}
