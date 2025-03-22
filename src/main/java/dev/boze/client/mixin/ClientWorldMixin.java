package dev.boze.client.mixin;

import dev.boze.client.Boze;
import dev.boze.client.events.EntityAddedEvent;
import dev.boze.client.events.EntityRemovedEvent;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.render.Tint;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {
    protected ClientWorldMixin(
            MutableWorldProperties properties,
            RegistryKey<World> registryRef,
            DynamicRegistryManager registryManager,
            RegistryEntry<DimensionType> dimensionEntry,
            Supplier<Profiler> profiler,
            boolean isClient,
            boolean debugWorld,
            long biomeAccess,
            int maxChainedNeighborUpdates
    ) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Inject(
            method = "addEntity",
            at = @At("HEAD")
    )
    private void onAddEntity(Entity var1, CallbackInfo var2) {
        AutoCrystal.INSTANCE.method488(var1);
    }

    @Inject(
            method = "addEntity",
            at = @At("TAIL")
    )
    private void onAddedEntity(Entity var1, CallbackInfo var2) {
        if (var1 != null) {
            Boze.EVENT_BUS.post(EntityAddedEvent.method1058(var1));
        }
    }

    @Inject(
            method = "removeEntity",
            at = @At("HEAD")
    )
    private void onRemoveEntity(int var1, RemovalReason var2, CallbackInfo var3) {
        if (this.getEntityById(var1) != null) {
            Boze.EVENT_BUS.post(EntityRemovedEvent.method1059(this.getEntityById(var1)));
        }
    }

    @Inject(
            method = "getSkyColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetSkyColor(Vec3d var1, float var2, CallbackInfoReturnable<Vec3d> var3) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3745.getValue()) {
            String var6 = this.getRegistryKey().getValue().getPath();
            switch (var6) {
                case "the_nether":
                    var3.setReturnValue(Tint.INSTANCE.field3747.getValue().method1954());
                    break;
                case "the_end":
                    var3.setReturnValue(Tint.INSTANCE.field3748.getValue().method1954());
                    break;
                default:
                    var3.setReturnValue(Tint.INSTANCE.field3746.getValue().method1954());
            }
        }
    }

    @Inject(
            method = "getCloudsColor",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onGetCloudsColor(float var1, CallbackInfoReturnable<Vec3d> var2) {
        if (Tint.INSTANCE.isEnabled() && Tint.INSTANCE.field3749.getValue()) {
            var2.setReturnValue(Tint.INSTANCE.field3750.getValue().method1954());
        }
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public Chunk getChunk(int i, int j) {
    //   return super.getChunk(i, j);
    //}
}
