package dev.boze.client.systems.modules.combat.autocrystal;

import dev.boze.client.Boze;
import dev.boze.client.mixininterfaces.IEndCrystalEntity;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.util.math.Vec3d;

import java.util.LinkedList;

public class CrystalEntityTracker implements IMinecraft {
    private final LinkedList<Runnable> field1238 = new LinkedList();

    public void method2142() {
        while (!this.field1238.isEmpty()) {
            this.field1238.poll().run();
        }
    }

    void markAsDeadOnTick(Vec3d var1) {
        this.field1238.add(() -> lambda$markAsDeadOnTick$0(var1));
    }

    public void method530(Vec3d var1) {
        for (Entity var6 : mc.world.getEntities()) {
            if (var6 instanceof EndCrystalEntity && var6.getPos().distanceTo(var1) <= 6.0 && var6 != null) {
                IEndCrystalEntity var7 = (IEndCrystalEntity) var6;
                if ((double) (System.currentTimeMillis() - var7.boze$getLastAttackTime()) > Boze.getModules().field905.field1519) {
                    var7.boze$setHitsSinceLastAttack(1);
                } else {
                    var7.boze$setHitsSinceLastAttack(var7.boze$getHitsSinceLastAttack() + 1);
                }

                var7.boze$setLastAttackTime(System.currentTimeMillis());
            }
        }
    }

    private void lambda$markAsDeadOnTick$0(Vec3d var1) {
        this.method530(var1);
    }
}
