package dev.boze.client.enums;

import dev.boze.client.systems.modules.combat.autocrystal.setting.AutoCrystalPrediction;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public enum AutoCrystalAction {
    Place,
    Full,
    Break;

    public int ticks;
    private int field1775;
    private final HashMap<LivingEntity, Vec3d> field1776 = new HashMap();
    private final HashMap<LivingEntity, Box> field1777 = new HashMap();
    private static final AutoCrystalAction[] field1778 = method889();

    private int method886(LivingEntity var1) {
        return var1.equals(IMinecraft.mc.player) ? this.field1775 : this.ticks;
    }

    private Vec3d method887(PlayerEntity var1) {
        if (this.field1776.containsKey(var1)) {
            return this.field1776.get(var1);
        } else {
            Vec3d var5 = AutoCrystalPrediction.method512(var1, var1.equals(IMinecraft.mc.player) ? this.field1775 : this.ticks);
            this.field1776.put(var1, var5);
            return var5;
        }
    }

    private Box method888(PlayerEntity var1) {
        if (this.field1777.containsKey(var1)) {
            return this.field1777.get(var1);
        } else {
            Vec3d var5 = this.method887(var1);
            Box var6 = var1.getDimensions(var1.getPose()).getBoxAt(var5);
            this.field1777.put(var1, var6);
            return var6;
        }
    }

    private static AutoCrystalAction[] method889() {
        return new AutoCrystalAction[]{Place, Full, Break};
    }
}
