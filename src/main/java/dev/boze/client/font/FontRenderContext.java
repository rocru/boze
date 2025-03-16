package dev.boze.client.font;

import dev.boze.client.api.BozeDrawColor;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.util.BufferAllocator;

class FontRenderContext {
   BufferAllocator field1970;
   Immediate field1971;
   BozeDrawColor field1972;
   final FontRenderer field1973;

   FontRenderContext(FontRenderer var1) {
      this.field1973 = var1;
      this.field1972 = null;
      this.field1970 = new BufferAllocator(2048);
      this.field1971 = VertexConsumerProvider.immediate(this.field1970);
   }

   FontRenderContext method1108(BozeDrawColor var1) {
      this.field1972 = var1;
      return this;
   }
}
