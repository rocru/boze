package dev.boze.client.mixininterfaces;

import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;

public interface IWorldRenderer {
    void boze$renderEntitiesForChams(float var1, MatrixStack var2, Camera var3);
}
