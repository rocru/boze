package dev.boze.client.utils.render.color;

import dev.boze.client.settings.WeirdColorSetting;
import mapped.Class3003;
import mapped.Class5903;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class GradientColor extends Class5903<GradientColor> {
    public static final HashMap<String, GradientColor> field420 = new HashMap();
    public final ArrayList<StaticColor> field422 = new ArrayList();
    public final String field423;
    private final Class3003 field421;
    public boolean field424;
    public boolean field425;
    public float field426;
    public float field427;
    public float field428;
    public float field429;

    public GradientColor(String name) {
        this.field421 = new Class3003(this);
        this.field423 = name;
    }

    public GradientColor(String name, NbtCompound tag) {
        this.fromTag(tag);
        this.field421 = new Class3003(this);
        this.field423 = name;
    }

    @Override
    public void method5875(WeirdColorSetting choice) {
        field420.put(this.field423, this);
        super.method5875(choice);
    }

    @Override
    public void method5876(WeirdColorSetting choice) {
        super.method5876(choice);
        if (!super.method2114()) {
            field420.remove(this.field423);
        }
    }

    @Override
    public StaticColor method208() {
        return this.field421;
    }

    public String toString() {
        String var10000 = this.method210();
        float var3 = this.field429;
        float var4 = this.field428;
        float var5 = this.field427;
        float var6 = this.field426;
        boolean var7 = this.field425;
        boolean var8 = this.field424;
        String var9 = var10000;
        return "GradientColor{colors=Colors{"
                + var9
                + ", hsb="
                + var8
                + ", mirror="
                + var7
                + ", angle="
                + var6
                + ", spin="
                + var5
                + ", scale="
                + var4
                + ", motion="
                + var3
                + "}";
    }

    private String method210() {
        StringBuilder var4 = new StringBuilder();

        for (StaticColor var6 : this.field422) {
            var4.append(var6.toString()).append(", ");
        }

        return var4.substring(0, var4.length() - 2);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            GradientColor var5 = (GradientColor) o;
            return this == var5 || this.field424 == var5.field424
                    && (this.field426 == var5.field426 || this.field427 == var5.field427)
                    && this.field425 == var5.field425
                    && Float.compare(this.field428, var5.field428) == 0
                    && Float.compare(this.field429, var5.field429) == 0
                    && Objects.equals(this.field422, var5.field422);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.field422, this.field424, this.field426, this.field425, this.field428, this.field429);
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound var4 = new NbtCompound();
        var4.putInt("type", 2);
        NbtList var5 = new NbtList();

        for (StaticColor var7 : this.field422) {
            var5.add(var7.toTag());
        }

        var4.put("colors", var5);
        var4.putBoolean("hsb", this.field424);
        var4.putBoolean("mirror", this.field425);
        var4.putFloat("angle", this.field426);
        var4.putFloat("spin", this.field427);
        var4.putFloat("scale", this.field428);
        var4.putFloat("motion", this.field429);
        return var4;
    }

    @Override
    public GradientColor fromTag(NbtCompound tag) {
        if (tag.contains("colors")) {
            NbtList var5 = tag.getList("colors", 10);

            for (int var6 = 0; var6 < var5.size(); var6++) {
                this.field422.add(new StaticColor(var5.getCompound(var6)));
            }
        }

        if (tag.contains("hsb")) {
            this.field424 = tag.getBoolean("hsb");
        }

        if (tag.contains("mirror")) {
            this.field425 = tag.getBoolean("mirror");
        }

        if (tag.contains("angle")) {
            this.field426 = tag.getFloat("angle");
        }

        if (tag.contains("spin")) {
            this.field427 = tag.getFloat("spin");
        }

        if (tag.contains("scale")) {
            this.field428 = tag.getFloat("scale");
        }

        if (tag.contains("motion")) {
            this.field429 = tag.getFloat("motion");
        }

        return this;
    }

    public GradientColor method214(String newName) {
        GradientColor var5 = new GradientColor(newName);

        for (StaticColor var7 : this.field422) {
            var5.field422.add(var7.method217());
        }

        var5.field424 = this.field424;
        var5.field425 = this.field425;
        var5.field426 = this.field426;
        var5.field427 = this.field427;
        var5.field428 = this.field428;
        var5.field429 = this.field429;
        return var5;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object fromTag(NbtCompound nbtCompound) {
    //   return this.method213(nbtCompound);
    //}
}
