package dev.boze.client.mixin;

import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({CrossbowItem.class})
public interface CrossbowItemAccessor {
   @Invoker("getSpeed")
   static float getSpeed(ChargedProjectilesComponent stack) {
      return 0.0F;
   }
}
