package dev.boze.client.utils.render.color;

import mapped.Class3071;
import mapped.Class5903;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.Objects;

public class ChangingColor extends Class5903<ChangingColor> {
    public final ArrayList<StaticColor> field416 = new ArrayList();
    public boolean field417;
    public boolean field418;
    public float field419;

    public ChangingColor() {
    }

    public ChangingColor(NbtCompound tag) {
        this.fromTag(tag);
    }

    public StaticColor method209(float progress) {
        int var5 = this.field416.size();
        StaticColor[] var6 = new StaticColor[var5 + (this.field418 ? var5 - 1 : 1)];

        int var7;
        for (var7 = 0; var7 < var5; var7++) {
            var6[var7] = this.field416.get(var7);
        }

        if (this.field418) {
            for (int var8 = var5 - 2; var7 < var6.length; var7++) {
                var6[var7] = this.field416.get(var8--);
            }
        } else {
            var6[var5] = this.field416.get(0);
        }

        float var12 = progress * (float) (var6.length - 1);
        int var9 = (int) Math.floor(var12);
        int var10 = (int) Math.ceil(var12);
        var9 = Math.min(var9, var6.length - 1);
        var10 = Math.min(var10, var6.length - 1);
        float var11 = var12 - (float) var9;
        return this.field417 ? Class3071.method6018(var6[var9], var6[var10], var11) : Class3071.method6017(var6[var9], var6[var10], var11);
    }

    @Override
    public StaticColor method208() {
        long var4 = System.currentTimeMillis();
        float var6 = (float) (var4 - 4786041977722L) / 1000.0F;
        float var7 = var6 * this.field419 % 1.0F;
        if (var7 < 0.0F) {
            var7++;
        }

        int var8 = this.field416.size();
        StaticColor[] var9 = new StaticColor[var8 + (this.field418 ? var8 - 1 : 1)];

        int var10;
        for (var10 = 0; var10 < var8; var10++) {
            var9[var10] = this.field416.get(var10);
        }

        if (this.field418) {
            for (int var11 = var8 - 2; var10 < var9.length; var10++) {
                var9[var10] = this.field416.get(var11--);
            }
        } else {
            var9[var8] = this.field416.get(0);
        }

        float var15 = var7 * (float) (var9.length - 1);
        int var12 = (int) Math.floor(var15);
        int var13 = (int) Math.ceil(var15);
        var12 = Math.min(var12, var9.length - 1);
        var13 = Math.min(var13, var9.length - 1);
        float var14 = var15 - (float) var12;
        return this.field417 ? Class3071.method6018(var9[var12], var9[var13], var14) : Class3071.method6017(var9[var12], var9[var13], var14);
    }

    public String toString() {
        String var10000 = this.method210();
        float var3 = this.field419;
        boolean var4 = this.field418;
        boolean var5 = this.field417;
        String var6 = var10000;
        return "ChangingColor{colors=Colors{" + var6 + ", hsb=" + var5 + "}, mirror=" + var4 + ", speed=" + var3 + "}";
    }

    private String method210() {
        StringBuilder var4 = new StringBuilder();

        for (StaticColor var6 : this.field416) {
            var4.append(var6.toString()).append(", ");
        }

        return var4.substring(0, var4.length() - 2);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            ChangingColor var5 = (ChangingColor) o;
            return this.field417 == var5.field417
                    && this.field418 == var5.field418
                    && Float.compare(this.field419, var5.field419) == 0
                    && Objects.equals(this.field416, var5.field416);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.field416, this.field417, this.field418, this.field419);
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound var4 = new NbtCompound();
        var4.putInt("type", 1);
        NbtList var5 = new NbtList();

        for (StaticColor var7 : this.field416) {
            var5.add(var7.toTag());
        }

        var4.put("colors", var5);
        var4.putBoolean("hsb", this.field417);
        var4.putBoolean("mirror", this.field418);
        var4.putFloat("speed", this.field419);
        return var4;
    }

    @Override
    public ChangingColor fromTag(NbtCompound tag) {
        if (tag.contains("colors")) {
            NbtList var5 = tag.getList("colors", 10);

            for (int var6 = 0; var6 < var5.size(); var6++) {
                this.field416.add(new StaticColor(var5.getCompound(var6)));
            }
        }

        if (tag.contains("hsb")) {
            this.field417 = tag.getBoolean("hsb");
        }

        if (tag.contains("mirror")) {
            this.field418 = tag.getBoolean("mirror");
        }

        if (tag.contains("speed")) {
            this.field419 = tag.getFloat("speed");
        }

        return this;
    }

    public ChangingColor method212() {
        ChangingColor var4 = new ChangingColor();

        for (StaticColor var6 : this.field416) {
            var4.field416.add(var6.method217());
        }

        var4.field417 = this.field417;
        var4.field418 = this.field418;
        var4.field419 = this.field419;
        return var4;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object fromTag(NbtCompound nbtCompound) {
    //   return this.method211(nbtCompound);
    //}
}
