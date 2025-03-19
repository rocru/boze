package dev.boze.client.mixin;

import dev.boze.client.utils.tooltip.BozeTooltipData;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mixin({DrawContext.class})
public abstract class DrawContextMixin {
   @Shadow
   protected abstract void drawTooltip(TextRenderer var1, List<TooltipComponent> var2, int var3, int var4, TooltipPositioner var5);

   @Inject(
      method = {"drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDrawTooltip(TextRenderer var1, List<Text> var2, Optional<TooltipData> var3, int var4, int var5, CallbackInfo var6) {
      if (var3.isPresent() && var3.get() instanceof BozeTooltipData var9) {
         var6.cancel();
         List var11 = (List)var2.stream().map(Text::method_30937).map(TooltipComponent::method_32662).collect(Collectors.toList());
         var11.add(var9.method498());
         this.drawTooltip(var1, var11, var4, var5, HoveredTooltipPositioner.INSTANCE);
      }
   }
}
