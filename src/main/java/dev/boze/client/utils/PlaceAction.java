package dev.boze.client.utils;

import dev.boze.api.interaction.PlaceInteraction;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import mapped.Class5913;
import mapped.Class5924;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class PlaceAction {
    private static final MinecraftClient field3918 = MinecraftClient.getInstance();
    private final float field3922;
    private final boolean field3924;
    private final boolean field3925;
    private BlockPos field3919;
    private Direction field3920;
    private float field3921;
    private Hand field3923;
    private int field3926;
    private boolean field3927;
    private PlaceInteraction field3928 = null;

    public PlaceAction(PlaceInteraction interaction) {
        this(
                interaction.pos,
                interaction.direction,
                interaction.getRotation() == null ? 0.0F : interaction.getRotation().yaw,
                interaction.getRotation() == null ? 0.0F : interaction.getRotation().pitch,
                interaction.hand,
                interaction.swing,
                interaction.getRotation() != null,
                interaction.slot
        );
        this.field3928 = interaction;
    }

    public PlaceAction(BlockPos neighbour, Direction opposite, float yaw, float pitch, Hand hand, boolean swing, boolean rotate, int slot) {
        this.field3919 = neighbour;
        this.field3920 = opposite;
        this.field3921 = yaw;
        this.field3922 = pitch;
        this.field3923 = hand;
        this.field3924 = swing;
        this.field3925 = rotate;
        this.field3926 = slot;
    }

    public BlockPos method2155() {
        return this.field3919;
    }

    public Direction method2156() {
        return this.field3920;
    }

    public float method2157() {
        return this.field3921;
    }

    public void method2158(float yaw) {
        this.field3921 = yaw;
    }

    public float method2159() {
        return this.field3922;
    }

    public Hand method2160() {
        return this.field3923;
    }

    public void method2161(Hand hand) {
        this.field3923 = hand;
    }

    public int method2162() {
        return this.field3926;
    }

    public void method2163(int slot) {
        this.field3926 = slot;
    }

    public void method2164(BlockPos neighbour) {
        this.field3919 = neighbour;
    }

    public void method2165(Direction opposite) {
        this.field3920 = opposite;
    }

    public void method2166(boolean alternative) {
        this.field3927 = alternative;
    }

    public Runnable method2167() {
        return this::lambda$getAction$0;
    }

    public boolean method2168() {
        return this.field3925;
    }

    private void lambda$getAction$0() {
        int var4 = -1;
        if (this.field3923 == Hand.MAIN_HAND && this.field3926 != -1 && field3918.player.getInventory().selectedSlot != this.field3926) {
            if (this.field3927) {
                field3918.interactionManager.clickSlot(0, 36 + this.field3926, field3918.player.getInventory().selectedSlot, SlotActionType.SWAP, field3918.player);
                var4 = this.field3926;
            } else {
                field3918.player.getInventory().selectedSlot = this.field3926;
                ((ClientPlayerInteractionManagerAccessor) field3918.interactionManager).callSyncSelectedSlot();
            }
        }

        if (((ClientPlayerEntityAccessor) field3918.player).getLastSprinting()) {
            field3918.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(field3918.player, Mode.STOP_SPRINTING));
            ((ClientPlayerEntityAccessor) field3918.player).setLastSprinting(false);
        }

        if (!((ClientPlayerEntityAccessor) field3918.player).getLastSneaking()
                && Class5924.method2088(field3918.world.getBlockState(this.method2155()).getBlock())) {
            field3918.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(field3918.player, Mode.PRESS_SHIFT_KEY));
            ((ClientPlayerEntityAccessor) field3918.player).setLastSneaking(true);
        }

        Vec3d var5 = new Vec3d((double) this.method2155().getX() + 0.5, (double) this.method2155().getY() + 0.5, (double) this.method2155().getZ() + 0.5)
                .add(new Vec3d(this.method2156().getUnitVector()).multiply(0.5));
        Class5913.method17(this.field3923, new BlockHitResult(var5, this.method2156(), this.method2155(), false));
        if (this.field3924) {
            field3918.getNetworkHandler().sendPacket(new HandSwingC2SPacket(this.field3923));
        }

        if (var4 != -1) {
            field3918.interactionManager.clickSlot(0, 36 + this.field3926, field3918.player.getInventory().selectedSlot, SlotActionType.SWAP, field3918.player);
        }

        if (this.field3928 != null) {
            this.field3928.execute();
        }
    }
}
