package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.enums.InhibitMode;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.math.NumberUtils;
import dev.boze.client.utils.trackers.TickRateTracker;
import mapped.Class3087;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map.Entry;

public class AutoCrystalTracker implements IMinecraft {
    private final AutoCrystal field1524;
    public float field1525 = 1.0F;
    private final float[] field1526 = new float[20];
    private int field1527 = 0;
    private long field1528;
    EndCrystalEntity field1529 = null;
    Entity[] field1530 = null;
    boolean field1531 = false;
    private final Timer field1532 = new Timer();
    BlockPos field1533 = null;
    Class3087 field1534 = null;
    boolean field1535 = false;
    double field1536 = 0.0;
    double field1537 = 0.0;
    LivingEntity field1538 = null;
    private final HashMap<BlockPos, Long> field1539 = new HashMap();
    private final int[] field1540 = new int[10];
    private int field1541 = 0;
    private final HashMap<BlockPos, Long> field1542 = new HashMap();
    private final HashMap<BlockPos, Integer> field1543 = new HashMap();
    private final int[] field1544 = new int[10];
    private int field1545 = 0;

    private static void method1800(String var0) {
        if (AutoCrystal.field1038 && mc.player != null) {
            System.out.println("[AutoCrystal.Tracker @" + mc.player.age + "] " + var0);
        }
    }

    AutoCrystalTracker(AutoCrystal var1) {
        this.field1524 = var1;
    }

    void update() {
        if (this.field1524.delaySync.getValue()) {
            this.field1525 = 20.0F / MathHelper.clamp(TickRateTracker.getLastTickRate(), 10.0F, 20.0F);
        } else {
            this.field1525 = 1.0F;
        }

        try {
            this.field1539.entrySet().removeIf(AutoCrystalTracker::lambda$update$0);
        } catch (ConcurrentModificationException var6) {
        }

        try {
            this.field1542.entrySet().removeIf(this::lambda$update$1);
        } catch (ConcurrentModificationException var5) {
        }

        if (this.field1532.hasElapsed(this.method215() * 3.0F)) {
            this.field1533 = null;
        }
    }

    void remove(BlockPos var1) {
        try {
            long var5 = this.field1542.remove(var1);
            this.field1543.remove(var1);
            int var7 = (int) (System.currentTimeMillis() - var5);
            if (var7 > 350) {
                return;
            }

            this.field1544[this.field1545 % this.field1544.length] = var7;
            this.field1545++;
        } catch (Exception var8) {
        }
    }

    void method681(BlockPos var1, int var2) {
        try {
            this.field1542.putIfAbsent(var1, System.currentTimeMillis());
            this.field1543.putIfAbsent(var1, var2);
        } catch (Exception var4) {
        }
    }

    void method682(BlockPos var1) {
        try {
            long var5 = this.field1539.remove(var1);
            int var7 = (int) (System.currentTimeMillis() - var5);
            if (var7 > 350) {
                return;
            }

            this.field1540[this.field1541 % this.field1540.length] = var7;
            this.field1541++;
        } catch (Exception var8) {
        }

        if (var1.equals(this.field1533)) {
            this.field1533 = null;
        }

        if (this.field1528 != -1L) {
            float var9 = (float) (System.currentTimeMillis() - this.field1528);
            this.field1526[this.field1527 % this.field1526.length] = var9;
            this.field1527++;
        }

        this.field1528 = System.currentTimeMillis();
    }

    void method683(BlockPos var1) {
        this.field1524.autoCrystalTracker.field1539.putIfAbsent(var1, System.currentTimeMillis());
        if (this.field1524.placeSync.getValue() && !var1.equals(this.field1533)) {
            this.field1533 = var1;
            this.field1532.reset();
        }
    }

    boolean method2101(BlockPos var1) {
        return this.field1539.containsKey(var1);
    }

    void method684(Class3087 var1, double var2, double var4, LivingEntity var6) {
        this.field1534 = var1;
        this.field1536 = var2;
        this.field1537 = var4;
        this.field1538 = var6;
    }

    void method1416() {
        this.field1529 = null;
        this.field1530 = null;
    }

    void method1198() {
        this.field1534 = null;
        this.field1535 = false;
        this.field1536 = 0.0;
        this.field1537 = 0.0;
        this.field1538 = null;
    }

    void method1904() {
        this.method1416();
        this.method1198();
    }

    void method1854() {
        this.method1904();
        this.field1528 = -1L;
    }

    float method1384() {
        if (this.field1538 == null) {
            return 0.0F;
        } else {
            float var4 = 0.0F;
            float var5 = 0.0F;

            for (float var9 : this.field1526) {
                if (var9 > 0.0F) {
                    var5 += var9;
                    var4++;
                }
            }

            return var4 == 0.0F ? 0.0F : NumberUtils.method2197(20.0F / (var5 / var4 / 50.0F), 1);
        }
    }

    public float method1385() {
        return this.method685(this.field1544);
    }

    float method215() {
        return this.method685(this.field1540);
    }

    private float method685(int[] var1) {
        float var5 = 0.0F;
        int var6 = 0;

        for (int var10 : var1) {
            if ((float) var10 > 0.0F) {
                var5 += (float) var10;
                var6++;
            }
        }

        return var6 < 1 ? 100.0F : var5 / (float) var6;
    }

    private boolean lambda$update$1(Entry var1) {
        if (System.currentTimeMillis() - (Long) var1.getValue() > 1000L) {
            if (this.field1524.autoCrystalBreak.field181.getValue() == InhibitMode.Strict) {
                Integer var5 = this.field1543.remove(var1.getKey());
                if (var5 != null) {
                    Entity var6 = mc.world.getEntityById(var5);
                    if (var6 != null && var6 instanceof EndCrystalEntity) {
                        ((IEndCrystalEntity) var6).boze$setAbandoned();
                    }
                }
            }

            this.field1543.remove(var1.getKey());
            return true;
        } else {
            return false;
        }
    }

    private static boolean lambda$update$0(Entry var0) {
        return System.currentTimeMillis() - (Long) var0.getValue() > 1000L;
    }
}
