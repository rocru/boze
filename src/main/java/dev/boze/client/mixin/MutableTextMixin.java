package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IMutableText;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({MutableText.class})
public class MutableTextMixin implements IMutableText {
   @Unique
   private int id;

   @Override
   public void boze$setId(int id) {
      this.id = id;
   }

   @Override
   public int boze$getId() {
      return this.id;
   }
}
