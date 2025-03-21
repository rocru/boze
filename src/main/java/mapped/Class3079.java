package mapped;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.math.BlockPos;

class Class3079 extends ClientPlayerEntity {
    Class3079(final MinecraftClient minecraftClient, final ClientWorld clientWorld, final ClientPlayNetworkHandler clientPlayNetworkHandler, final StatHandler statHandler, final ClientRecipeBook clientRecipeBook, final boolean b, final boolean b2) {
        super(minecraftClient, clientWorld, clientPlayNetworkHandler, statHandler, clientRecipeBook, b, b2);
    }

    public float getHealth() {
        return this.getMaxHealth();
    }

    public void tickMovement() {
        this.fallDistance = 0.0f;
        super.tickMovement();
    }

    public void fall(final double heightDifference, final boolean onGround, final BlockState state, final BlockPos landedPosition) {
    }

    public boolean isCamera() {
        return true;
    }

    public void playSound(final SoundEvent sound, final float volume, final float pitch) {
    }

    public void playSoundToPlayer(final SoundEvent event, final SoundCategory category, final float volume, final float pitch) {
    }
}
