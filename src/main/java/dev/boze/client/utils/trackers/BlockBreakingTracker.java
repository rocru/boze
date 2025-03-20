package dev.boze.client.utils.trackers;

import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.BlockMiningUtils;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.world.BlockBreakingUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.BlockBreakingInfo;
import net.minecraft.network.packet.s2c.play.BlockBreakingProgressS2CPacket;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BlockBreakingTracker implements IMinecraft {
    public static final BlockBreakingTracker field1511 = new BlockBreakingTracker();
    private final HashMap<BlockPos, Float> field1512 = new HashMap();
    private final ConcurrentHashMap<BlockPos, Long> field1513 = new ConcurrentHashMap();
    private final ConcurrentHashMap<BlockPos, Float> field1514 = new ConcurrentHashMap();
    private final ConcurrentHashMap<Integer, BlockPos> field1515 = new ConcurrentHashMap();
    private final HashMap<BlockPos, Float> field1516 = new HashMap();
    private final HashMap<BlockPos, Float> field1517 = new HashMap();

    private BlockBreakingTracker() {
    }

    public HashMap<BlockPos, Float> method666(boolean var1) {
        if (var1) {
            if (this.field1517.isEmpty()) {
                this.field1517.putAll(this.field1514);
            }

            return this.field1517;
        } else {
            if (this.field1516.isEmpty()) {
                this.field1516.putAll(this.field1512);
            }

            return this.field1516;
        }
    }

    public int method667(BlockPos var1) {
        for (Entry var6 : this.field1515.entrySet()) {
            if (var6.getValue().equals(var1)) {
                return (Integer) var6.getKey();
            }
        }

        return -1;
    }

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        if (var1.packet instanceof BlockBreakingProgressS2CPacket var5) {
            if (mc.player != null && var5.getEntityId() == mc.player.getId()) {
                return;
            }

            if (this.field1515.containsKey(var5.getEntityId())) {
                BlockPos var7 = this.field1515.get(var5.getEntityId());
                if (!var7.equals(var5.getPos())) {
                    this.field1513.remove(var7);
                    this.field1514.remove(var7);
                }
            }

            this.field1513.put(var5.getPos(), System.currentTimeMillis());
            this.field1514.put(var5.getPos(), this.field1514.getOrDefault(var5.getPos(), 0.0F));
            this.field1515.put(var5.getEntityId(), var5.getPos());
        }
    }

    @EventHandler
    public void method2041(MovementEvent var1) {
        this.field1516.clear();
        this.field1517.clear();
        this.field1512.clear();
        ((WorldRendererAccessor) mc.worldRenderer).getBlockBreakingInfos().values().forEach(this::lambda$onSendMovementPackets$0);
        this.field1513.entrySet().forEach(this::lambda$onSendMovementPackets$1);
        this.field1514.replaceAll(this::method668);
    }

    private float method668(BlockPos var1, float var2) {
        if (this.field1512.containsKey(var1)) {
            return var2;
        } else {
            BlockState var5 = mc.world.getBlockState(var1);
            int var6 = BlockMiningUtils.method593(var5, SwapMode.Alt);
            float var7 = BlockBreakingUtil.method506(var1, mc.player.getInventory().getStack(var6));
            return Math.min(1.0F, var2 + var7 * (1.0F / Options.INSTANCE.field990.getValue()));
        }
    }

    private void lambda$onSendMovementPackets$1(Entry var1) {
        if (mc.world.getBlockState((BlockPos) var1.getKey()).isAir()
                || System.currentTimeMillis() - (Long) var1.getValue() > 2500L
                || this.method667((BlockPos) var1.getKey()) == mc.player.getId() && !mc.interactionManager.isBreakingBlock()) {
            this.field1513.remove(var1.getKey());
            this.field1514.remove(var1.getKey());
        }
    }

    private void lambda$onSendMovementPackets$0(BlockBreakingInfo var1) {
        float var2 = Math.min(1.0F, Math.max((float) var1.getStage() / 8.0F, 0.125F));
        this.field1512.put(var1.getPos(), var2);
        this.field1513.put(var1.getPos(), System.currentTimeMillis());
        this.field1514.put(var1.getPos(), var2);
        this.field1515.put(var1.getActorId(), var1.getPos());
    }
}
