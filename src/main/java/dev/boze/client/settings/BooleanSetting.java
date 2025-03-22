package dev.boze.client.settings;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Options;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class BooleanSetting extends Setting<Boolean> {
    private final boolean defaultValue;
    private boolean value;

    public BooleanSetting(String name, boolean value, String description) {
        super(name, description);
        this.value = value;
        this.defaultValue = value;
    }

    public BooleanSetting(String name, boolean value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.value = value;
        this.defaultValue = value;
    }

    public BooleanSetting(String name, boolean value, String description, Setting parent) {
        super(name, description, parent);
        this.value = value;
        this.defaultValue = value;
    }

    public BooleanSetting(String name, boolean value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.value = value;
        this.defaultValue = value;
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    @Override
    public Boolean resetValue() {
        return this.value = this.defaultValue;
    }

    @Override
    public Boolean setValue(Boolean newVal) {
        this.value = newVal;
        if (this.callback != null) {
            this.callback.accept(this.value);
        }

        return this.value;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method402("value", BoolArgumentType.bool()).executes(this::lambda$build$0)));
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("toggle").executes(this::lambda$build$1)));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putBoolean("Value", this.value);
        return tag;
    }

    @Override
    public Boolean load(NbtCompound tag) {
        if (tag.contains("Value")) {
            this.value = tag.getBoolean("Value");
        }

        return this.value;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object load(NbtCompound nbtCompound) {
    //   return this.method422(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //public Object setValue(Object object) {
    //   return this.method421((Boolean)object);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //   return this.method420();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method419();
    //}

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        this.setValue(!this.getValue());
        if (Options.INSTANCE.field991.getValue()) {
            ChatInstance.method624(this.name + " is now (highlight)" + (this.getValue() ? "on" : "off"));
        }

        return 1;
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        this.setValue((Boolean) var1.getArgument("value", Boolean.class));
        if (Options.INSTANCE.field991.getValue()) {
            ChatInstance.method624(this.name + " is now (highlight)" + (this.getValue() ? "on" : "off"));
        }

        return 1;
    }
}
