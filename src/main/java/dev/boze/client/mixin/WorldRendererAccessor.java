package dev.boze.client.mixin;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.entity.player.BlockBreakingInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {
    @Accessor("blockBreakingInfos")
    Int2ObjectMap<BlockBreakingInfo> getBlockBreakingInfos();

    @Accessor("entityOutlinesFramebuffer")
    Framebuffer getEntityOutlinesFramebuffer();

    @Accessor
    void setEntityOutlinesFramebuffer(Framebuffer var1);

    @Accessor("frustum")
    Frustum getFrustum();
}
