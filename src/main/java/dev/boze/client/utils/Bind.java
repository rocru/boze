package dev.boze.client.utils;

import com.google.gson.JsonObject;
import dev.boze.client.utils.misc.ICopyable;
import dev.boze.client.utils.misc.IJsonSerializable;
import dev.boze.client.utils.misc.ISerializable;
import mapped.Class5928;
import net.minecraft.nbt.NbtCompound;

public class Bind implements ISerializable<Bind>, IJsonSerializable<Bind>, ICopyable<Bind>, IMinecraft {
    private boolean isKey;
    private int value;

    public Bind(boolean isKey, int value) {
        this.set(isKey, value);
    }

    public static Bind create() {
        return new Bind(true, -1);
    }

    public static Bind fromKey(int key) {
        return new Bind(true, key);
    }

    public static Bind fromButton(int button) {
        return new Bind(false, button);
    }

    public int getBind() {
        return this.value;
    }

    public boolean isValid() {
        return this.value != -1;
    }

    public boolean canUnbind(boolean isKey, int value) {
        return isKey ? value != 256 : value != 0 && value != 1;
    }

    public void set(boolean isKey, int value) {
        this.isKey = isKey;
        this.value = value;
    }

    @Override
    public Bind set(Bind value) {
        this.isKey = value.isKey;
        this.value = value.value;
        return this;
    }

    public boolean matches(boolean isKey, int value) {
        return this.isKey == isKey && this.value == value;
    }

    public boolean isBound() {
        return this.value != -1;
    }

    public boolean isKey() {
        return this.isKey;
    }

    public boolean isPressed() {
        if (mc.currentScreen != null) {
            return false;
        } else {
            return this.isKey ? Class5928.method159(this.value) : Class5928.method518(this.value);
        }
    }

    @Override
    public Bind copy() {
        return new Bind(this.isKey, this.value);
    }

    public String toString() {
        if (this.value == -1) {
            return "None";
        } else {
            return this.isKey ? KeyboardUtil.getKeyName(this.value) : KeyboardUtil.getButtonName(this.value);
        }
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound var4 = new NbtCompound();
        var4.putInt("key", this.value);
        var4.putBoolean("mouse", !this.isKey);
        return var4;
    }

    @Override
    public Bind fromTag(NbtCompound tag) {
        this.value = tag.getInt("key");
        this.isKey = !tag.getBoolean("mouse");
        return this;
    }

    @Override
    public JsonObject serialize() {
        JsonObject var4 = new JsonObject();
        var4.addProperty("key", this.value);
        var4.addProperty("mouse", !this.isKey);
        return var4;
    }

    @Override
    public Bind deserialize(JsonObject tag) {
        this.value = tag.get("key").getAsInt();
        this.isKey = !tag.get("mouse").getAsBoolean();
        return this;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object fromTag(NbtCompound nbtCompound) {
    //   return this.method180(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object deserialize(JsonObject jsonObject) {
    //   return this.method181(jsonObject);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public ICopyable copy() {
    //   return this.method179();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public ICopyable set(ICopyable arg) {
    //  return this.copy((Bind)arg);
    //}
}
