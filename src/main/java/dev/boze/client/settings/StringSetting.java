package dev.boze.client.settings;

import net.minecraft.nbt.NbtCompound;

import java.util.function.BooleanSupplier;

public class StringSetting extends Setting<String> {
    private String field925;
    private final String field926;

    public StringSetting(String name, String value, String description) {
        super(name, description);
        this.field925 = value;
        this.field926 = value;
    }

    public StringSetting(String name, String value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field925 = value;
        this.field926 = value;
    }

    public StringSetting(String name, String value, String description, Setting parent) {
        super(name, description, parent);
        this.field925 = value;
        this.field926 = value;
    }

    public StringSetting(String name, String value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field925 = value;
        this.field926 = value;
    }

    @Override
    public String getValue() {
        return this.field925;
    }

    @Override
    public String resetValue() {
        return this.field925 = this.field926;
    }

    @Override
    public String setValue(String newVal) {
        return this.field925 = newVal;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putString("Value", this.field925);
        return tag;
    }

    @Override
    public String load(NbtCompound tag) {
        if (tag.contains("Value")) {
            this.field925 = tag.getString("Value");
        }

        return this.field925;
    }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object load(NbtCompound nbtCompound) {
    //    return this.method1286(nbtCompound);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object setValue(Object object) {
    //   return this.method1341((String)object);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //    return this.method1562();
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method1322();
    //}
}
