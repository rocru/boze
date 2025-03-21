package mapped;

import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.ClientWorldAccessor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.network.SequencedPacketCreator;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;

public class Class5913 implements IMinecraft {
    public Class5913() {
        super();
    }

    public static PlayerInteractItemC2SPacket method16(Hand var2) {
        return (PlayerInteractItemC2SPacket) Class5913.method18(arg_0 -> Class5913.lambda$interactItem$0(var2, arg_0));
    }

    public static PlayerInteractBlockC2SPacket method17(Hand var3, BlockHitResult var4) {
        return (PlayerInteractBlockC2SPacket) Class5913.method18(arg_0 -> Class5913.lambda$interactBlock$1(var3, var4, arg_0));
    }

    public static Packet<ServerPlayPacketListener> method18(final SequencedPacketCreator packetCreator) {
        final PendingUpdateManager incrementSequence = ((ClientWorldAccessor) Class5913.mc.world).callGetPendingUpdateManager().incrementSequence();
        try {
            final Packet predict = packetCreator.predict(incrementSequence.getSequence());
            Class5913.mc.player.networkHandler.sendPacket(predict);
            incrementSequence.close();
            return (Packet<ServerPlayPacketListener>) predict;
        } catch (final Throwable t) {
            final PendingUpdateManager pendingUpdateManager = incrementSequence;
            try {
                pendingUpdateManager.close();
            } catch (final Throwable exception) {
                t.addSuppressed(exception);
            }
            final Packet predict2 = packetCreator.predict(0);
            Class5913.mc.player.networkHandler.sendPacket(predict2);
            return (Packet<ServerPlayPacketListener>) predict2;
        }
    }

    private static Packet lambda$interactBlock$1(final Hand hand, final BlockHitResult blockHitResult, final int n) {
        return new PlayerInteractBlockC2SPacket(hand, blockHitResult, n);
    }

    private static Packet lambda$interactItem$0(final Hand hand, final int n) {
        return new PlayerInteractItemC2SPacket(hand, n, ((ClientPlayerEntityAccessor) Class5913.mc.player).getLastYaw(), ((ClientPlayerEntityAccessor) Class5913.mc.player).getLastPitch());
    }
}
