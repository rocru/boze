package dev.boze.client.mixin;

import java.util.List;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.toast.ToastManager.Entry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ToastManager.class})
public interface ToastManagerAccessor {
   @Accessor
   List<Entry<?>> getVisibleEntries();
}
