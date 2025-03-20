package dev.boze.client.settings;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class FloatSetting extends Setting<Float> {
    private float field928;
    private final float field929;
    public final float field930;
    public final float field931;
    public final float field932;
    public final boolean field933;

    public FloatSetting(String name, float value, float min, float max, float step, String description) {
        super(name, description);
        this.field928 = value;
        this.field929 = value;
        this.field930 = min;
        this.field931 = max;
        this.field932 = step;
        this.field933 = false;
    }

    public FloatSetting(String name, float value, float min, float max, float step, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field928 = value;
        this.field929 = value;
        this.field930 = min;
        this.field931 = max;
        this.field932 = step;
        this.field933 = false;
    }

    public FloatSetting(String name, float value, float min, float max, float step, String description, Setting parent) {
        super(name, description, parent);
        this.field928 = value;
        this.field929 = value;
        this.field930 = min;
        this.field931 = max;
        this.field932 = step;
        this.field933 = false;
    }

    public FloatSetting(String name, float value, float min, float max, float step, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field928 = value;
        this.field929 = value;
        this.field930 = min;
        this.field931 = max;
        this.field932 = step;
        this.field933 = false;
    }

    public FloatSetting(String name, float value, String description) {
        super(name, description);
        this.field928 = value;
        this.field929 = value;
        this.field930 = Float.MIN_VALUE;
        this.field931 = Float.MAX_VALUE;
        this.field932 = 1.0E-4F;
        this.field933 = true;
    }

    public FloatSetting(String name, float value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field928 = value;
        this.field929 = value;
        this.field930 = Float.MIN_VALUE;
        this.field931 = Float.MAX_VALUE;
        this.field932 = 1.0E-4F;
        this.field933 = true;
    }

    public FloatSetting(String name, float value, String description, Setting parent) {
        super(name, description, parent);
        this.field928 = value;
        this.field929 = value;
        this.field930 = Float.MIN_VALUE;
        this.field931 = Float.MAX_VALUE;
        this.field932 = 1.0E-4F;
        this.field933 = true;
    }

    public FloatSetting(String name, float value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field928 = value;
        this.field929 = value;
        this.field930 = Float.MIN_VALUE;
        this.field931 = Float.MAX_VALUE;
        this.field932 = 1.0E-4F;
        this.field933 = true;
    }

    @Override
    public Float getValue() {
        return this.field928;
    }

    @Override
    public Float resetValue() {
        return this.field928 = this.field929;
    }

    @Override
    public Float setValue(Float newVal) {
        return this.field928 = newVal;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method402("value", FloatArgumentType.floatArg()).executes(this::lambda$build$0)));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putFloat("Value", this.field928);
        return tag;
    }

    @Override
    public Float load(NbtCompound tag) {
        if (tag.contains("Value")) {
            this.field928 = tag.getFloat("Value");
        }

        return this.field928;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object load(NbtCompound nbtCompound) {
    //    return this.method426(nbtCompound);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    // public Object setValue(Object object) {
    //    return this.method425((Float)object);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //   return this.method424();
    // }
//
    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //  return this.method423();
    // }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        this.setValue((Float) var1.getArgument("value", Float.class));
        return 1;
    }
}
