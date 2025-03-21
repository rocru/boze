package dev.boze.client.systems.modules.render.tunnelesp;

import dev.boze.client.enums.ShapeMode;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.systems.modules.render.TunnelESP;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.ints.IntSet;
import mapped.Class3083;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class TunnelRenderer {
    public final int field3833;
    public final int field3834;
    public IntSet field3835;
    public boolean field3836;
    final TunnelESP field3837;

    public TunnelRenderer(final TunnelESP arg, int x, int z) {
        this.field3837 = arg;
        this.field3833 = x;
        this.field3834 = z;
        this.field3836 = true;
    }

    public void method2082(Renderer3D renderer) {
        if (this.field3835 != null) {
            IntIterator var5 = this.field3835.iterator();

            while (var5.hasNext()) {
                int var6 = var5.nextInt();
                int var7 = TunnelESP.method2068(var6);
                short var8 = TunnelESP.method2069(var6);
                int var9 = TunnelESP.method2070(var6);
                byte var10 = 0;

                for (Direction var14 : Direction.values()) {
                    if (var14.getAxis() != Axis.Y && this.field3837.method2079(this, var7 + var14.getOffsetX(), var8, var9 + var14.getOffsetZ())) {
                        var10 |= Class3083.method6049(var14);
                    }
                }

                var7 += this.field3833 * 16;
                var9 += this.field3834 * 16;
                if (this.field3837.field3818.getValue()) {
                    renderer.method1268(
                            var7,
                            var8,
                            var9,
                            var7 + 1,
                            (double) var8 + this.field3837.field3817.getValue(),
                            var9 + 1,
                            this.field3837.field3815.getValue(),
                            var10
                    );
                } else {
                    renderer.method1271(
                            var7,
                            var8,
                            var9,
                            var7 + 1,
                            (double) var8 + this.field3837.field3817.getValue(),
                            var9 + 1,
                            this.field3837.field3815.getValue(),
                            this.field3837.field3816.getValue(),
                            ShapeMode.Full,
                            var10
                    );
                }
            }
        }
    }

    public long method2083() {
        return ChunkPos.toLong(this.field3833, this.field3834);
    }
}
