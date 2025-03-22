package dev.boze.client.settings;

import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class SoundStringSetting extends Setting<String> {
    private final String field967;
    private String field966;

    public SoundStringSetting(String name, String value, String description) {
        super(name, description);
        this.field966 = value;
        this.field967 = value;
    }

    public SoundStringSetting(String name, String value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field966 = value;
        this.field967 = value;
    }

    public SoundStringSetting(String name, String value, String description, Setting parent) {
        super(name, description, parent);
        this.field966 = value;
        this.field967 = value;
    }

    public SoundStringSetting(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field966 = value;
        this.field967 = value;
    }

    @Override
    public String getValue() {
        return this.field966;
    }

    @Override
    public String resetValue() {
        return this.field966 = this.field967;
    }

    @Override
    public String setValue(String newVal) {
        return this.field966 = newVal;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putString("Value", this.field966);
        return tag;
    }

    @Override
    public String load(NbtCompound tag) {
        if (tag.contains("Value")) {
            this.field966 = tag.getString("Value");
        }

        return this.field966;
    }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //public Object load(NbtCompound nbtCompound) {
    //     return this.method1286(nbtCompound);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object setValue(Object object) {
    //    return this.method1341((String)object);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object resetValue() {
    //   return this.method1562();
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method1322();
    //}
}
