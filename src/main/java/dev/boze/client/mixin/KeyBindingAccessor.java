package dev.boze.client.mixin;

import java.util.Map;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Key;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({KeyBinding.class})
public interface KeyBindingAccessor {
   @Accessor("CATEGORY_ORDER_MAP")
   static Map<String, Integer> getCategoryOrderMap() {
      return null;
   }

   @Accessor("boundKey")
   Key getKey();

   @Accessor
   void setTimesPressed(int var1);

   @Accessor
   int getTimesPressed();
}
