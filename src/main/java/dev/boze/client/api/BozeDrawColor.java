package dev.boze.client.api;

import com.google.gson.JsonObject;
import dev.boze.api.render.DrawColor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.nbt.NbtCompound;

import java.util.Arrays;
import java.util.Objects;

public class BozeDrawColor extends RGBAColor implements DrawColor {
    public static final BozeDrawColor field1841 = new BozeDrawColor();
    public boolean field1842 = false;
    public double field1843 = 0.0;
    public double field1844 = 0.0;
    public double[] field1845 = new double[]{0.0, 0.0};
    public double[] field1846 = new double[]{0.0, 1.0};
    private static final double[] field1847 = new double[]{0.0, 0.0};

    public BozeDrawColor() {
    }

    public BozeDrawColor(int packed) {
        super(packed);
    }

    public BozeDrawColor(int r, int g, int b, int a) {
        super(r, g, b, a);
    }

    public BozeDrawColor(int packed, boolean gradient, double speed, double offset, double strength) {
        super(packed);
        this.field1842 = gradient;
        this.field1843 = speed;
        this.field1844 = offset;
        this.field1845 = new double[]{-strength, strength};
    }

    public BozeDrawColor(int r, int g, int b, int a, boolean gradient, double speed, double offset, double strength) {
        super(r, g, b, a);
        this.field1842 = gradient;
        this.field1843 = speed;
        this.field1844 = offset;
        this.field1845 = new double[]{-strength, strength};
    }

    public BozeDrawColor(int packed, boolean gradient, double speed, double offset, double[] strength) {
        super(packed);
        this.field1842 = gradient;
        this.field1843 = speed;
        this.field1844 = offset;
        this.field1845 = strength;
    }

    public BozeDrawColor(int r, int g, int b, int a, boolean gradient, double speed, double offset, double[] strength) {
        super(r, g, b, a);
        this.field1842 = gradient;
        this.field1843 = speed;
        this.field1844 = offset;
        this.field1845 = strength;
    }

    public BozeDrawColor(int r, int g, int b, int a, boolean gradient, double speed, double offset, double[] strength, double[] hues) {
        super(r, g, b, a);
        this.field1842 = gradient;
        this.field1843 = speed;
        this.field1844 = offset;
        this.field1845 = strength;
        this.field1846 = hues;
    }

    public BozeDrawColor(BozeDrawColor color) {
        super(color);
        this.field1842 = color.field1842;
        this.field1843 = color.field1843;
        this.field1844 = color.field1844;
        this.field1845 = color.field1845;
    }

    public BozeDrawColor(RGBAColor color) {
        super(color);
    }

    public BozeDrawColor method954(boolean gradient) {
        this.field1842 = gradient;
        return this;
    }

    public BozeDrawColor method955(double speed) {
        this.field1843 = speed;
        return this;
    }

    public BozeDrawColor method956(double offset) {
        this.field1844 = offset;
        return this;
    }

    public BozeDrawColor method957(double[] strength) {
        this.field1845 = strength;
        return this;
    }

    public double method958() {
        double var4 = (double) (System.currentTimeMillis() - Renderer3D.field2172) * this.field1843 * -0.001;
        if (this.field1842 || this.field1843 > 0.0) {
            var4 += this.field1844 + 0.01;
        }

        return var4;
    }

    public double[] method959() {
        if (!this.field1842) {
            return field1847;
        } else {
            double var4 = Math.max(this.getMaxHue() - this.getMinHue(), 0.1);
            return new double[]{this.field1845[0] / var4, this.field1845[1] / var4};
        }
    }

    public double method960() {
        return this.field1846[0] == 0.0 && this.field1846[1] == 1.0 ? 1.0 : 2.0;
    }

    public double getMinHue() {
        return this.field1846[0];
    }

    public double getMaxHue() {
        return this.field1846[1] < this.field1846[0] ? 1.0 + this.field1846[1] : this.field1846[1];
    }

    @Override
    public BozeDrawColor set(RGBAColor value) {
        super.set(value);
        if (value instanceof BozeDrawColor) {
            this.field1842 = ((BozeDrawColor) value).field1842;
            this.field1843 = ((BozeDrawColor) value).field1843;
            this.field1844 = ((BozeDrawColor) value).field1844;
            this.field1845 = ((BozeDrawColor) value).field1845;
            this.field1846 = ((BozeDrawColor) value).field1846;
        }

        return this;
    }

    @Override
    public BozeDrawColor method2025(double factor) {
        return new BozeDrawColor(
                Math.max((int) ((double) this.field408 * factor), 0),
                Math.max((int) ((double) this.field409 * factor), 0),
                Math.max((int) ((double) this.field410 * factor), 0),
                this.field411,
                this.field1842,
                this.field1843,
                this.field1844,
                this.field1845,
                this.field1846
        );
    }

    @Override
    public BozeDrawColor method183(double factor) {
        int var6 = (int) (1.0 / (1.0 - factor));
        if (this.field408 == 0 && this.field409 == 0 && this.field410 == 0) {
            return new BozeDrawColor(var6, var6, var6, this.field411, this.field1842, this.field1843, this.field1844, this.field1845, this.field1846);
        } else {
            if (this.field408 > 0 && this.field408 < var6) {
                this.field408 = var6;
            }

            if (this.field409 > 0 && this.field409 < var6) {
                this.field409 = var6;
            }

            if (this.field410 > 0 && this.field410 < var6) {
                this.field410 = var6;
            }

            return new BozeDrawColor(
                    Math.min((int) ((double) this.field408 / factor), 255),
                    Math.min((int) ((double) this.field409 / factor), 255),
                    Math.min((int) ((double) this.field410 / factor), 255),
                    this.field411,
                    this.field1842,
                    this.field1843,
                    this.field1844,
                    this.field1845,
                    this.field1846
            );
        }
    }

    @Override
    public BozeDrawColor copy() {
        return new BozeDrawColor(
                this.field408,
                this.field409,
                this.field410,
                this.field411,
                this.field1842,
                this.field1843,
                this.field1844,
                this.field1845.clone(),
                this.field1846.clone()
        );
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound var3 = super.toTag();
        var3.putBoolean("gradient", this.field1842);
        var3.putDouble("speed", this.field1843);
        var3.putDouble("offset", this.field1844);
        var3.putDouble("strengthX", this.field1845[0]);
        var3.putDouble("strengthY", this.field1845[1]);
        var3.putDouble("minHue", this.field1846[0]);
        var3.putDouble("maxHue", this.field1846[1]);
        return var3;
    }

    public JsonObject toJson() {
        JsonObject var4 = new JsonObject();
        var4.addProperty("r", this.field408);
        var4.addProperty("g", this.field409);
        var4.addProperty("b", this.field410);
        var4.addProperty("a", this.field411);
        var4.addProperty("gradient", this.field1842);
        var4.addProperty("speed", this.field1843);
        var4.addProperty("offset", this.field1844);
        var4.addProperty("strengthX", this.field1845[0]);
        var4.addProperty("strengthY", this.field1845[1]);
        var4.addProperty("minHue", this.field1846[0]);
        var4.addProperty("maxHue", this.field1846[1]);
        return var4;
    }

    @Override
    public DrawColor fromJson(JsonObject object) {
        this.field408 = object.get("r").getAsInt();
        this.field409 = object.get("g").getAsInt();
        this.field410 = object.get("b").getAsInt();
        this.field411 = object.get("a").getAsInt();
        this.field1842 = object.get("gradient").getAsBoolean();
        this.field1843 = object.get("speed").getAsDouble();
        this.field1844 = object.get("offset").getAsDouble();
        this.field1845[0] = object.get("strengthX").getAsDouble();
        this.field1845[1] = object.get("strengthY").getAsDouble();
        this.field1846[0] = object.get("minHue").getAsDouble();
        this.field1846[1] = object.get("maxHue").getAsDouble();
        return this;
    }

    @Override
    public BozeDrawColor fromTag(NbtCompound tag) {
        super.fromTag(tag);
        this.field1842 = tag.getBoolean("gradient");
        this.field1843 = tag.getDouble("speed");
        this.field1844 = tag.getDouble("offset");
        if (tag.contains("strengthX") && tag.contains("strengthY")) {
            this.field1845[0] = tag.getDouble("strengthX");
            this.field1845[1] = tag.getDouble("strengthY");
        }

        if (tag.contains("minHue") && tag.contains("maxHue")) {
            this.field1846[0] = tag.getDouble("minHue");
            this.field1846[1] = tag.getDouble("maxHue");
        }

        if (this.field1846[0] == 0.0 && this.field1846[1] == 0.0) {
            this.field1846[1] = 1.0;
        }

        return this;
    }

    public void setRGBA(int r, int g, int b, int a) {
        this.field408 = r;
        this.field409 = g;
        this.field410 = b;
        this.field411 = a;
    }

    public int getRGBA() {
        return method184(this.field408, this.field409, this.field410, this.field411);
    }

    public int getR() {
        return this.field408;
    }

    public int getG() {
        return this.field409;
    }

    public int getB() {
        return this.field410;
    }

    public int getA() {
        return this.field411;
    }

    public void setR(int r) {
        this.field408 = r;
    }

    public void setG(int g) {
        this.field409 = g;
    }

    public void setB(int b) {
        this.field410 = b;
    }

    public void setA(int a) {
        this.field411 = a;
    }

    public double getSpeed() {
        return this.field1843;
    }

    public void setSpeed(double speed) {
        this.field1843 = speed;
    }

    public double getHueOffset() {
        return this.field1844;
    }

    public void setHueOffset(double hueOffset) {
        this.field1844 = hueOffset;
    }

    public double getGradientX() {
        return this.field1845[0];
    }

    public void setGradientX(double gradientX) {
        this.field1845[0] = gradientX;
    }

    public double getGradientY() {
        return this.field1845[1];
    }

    public void setGradientY(double gradientY) {
        this.field1845[1] = gradientY;
    }

    public void setMinHue(double minHue) {
        this.field1846[0] = minHue;
    }

    public void setMaxHue(double maxHue) {
        this.field1846[1] = maxHue;
    }

    public DrawColor clone() {
        return this.copy();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof BozeDrawColor)) {
            return false;
        } else if (!super.equals(o)) {
            return false;
        } else {
            BozeDrawColor var5 = (BozeDrawColor) o;
            return this.field408 == var5.field408
                    && this.field409 == var5.field409
                    && this.field410 == var5.field410
                    && this.field411 == var5.field411
                    && this.field1842 == var5.field1842
                    && Double.compare(var5.field1843, this.field1843) == 0
                    && Double.compare(var5.field1844, this.field1844) == 0
                    && Arrays.equals(this.field1845, var5.field1845)
                    && Arrays.equals(this.field1846, var5.field1846);
        }
    }

    public int hashCode() {
        return Objects.hash(
                this.field408,
                this.field409,
                this.field410,
                this.field411,
                this.field1842,
                this.field1843,
                this.field1844,
                this.field1845[0],
                this.field1845[1],
                this.field1846[0],
                this.field1846[1]);
    }

    // $VF: synthetic method
    // $VF: bridge method
    //public RGBAColor fromTag(NbtCompound nbtCompound) {
    //  return this.fromTag(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //public RGBAColor copy() {
    //   return this.copy();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public RGBAColor set(RGBAColor arg) {
    //   return this.set(arg);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public RGBAColor method183(double d) {
    //   return this.method963(d);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public RGBAColor method2025(double d) {
    //   return this.method962(d);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public ICopyable copy() {
    //   return this.method964();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public ICopyable set(ICopyable arg) {
    //   return this.method961((RGBAColor)arg);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object fromTag(NbtCompound nbtCompound) {
    //   return this.method966(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //public Object clone() throws CloneNotSupportedException {
    //   return this.clone();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //public Object fromJson(JsonObject jsonObject) {
    //   return this.method965(jsonObject);
    //}
}
