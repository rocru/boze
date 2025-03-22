package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.EnumArgument;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;

import java.util.Locale;
import java.util.function.BooleanSupplier;

public class EnumSetting<T extends Enum> extends Setting<T> {
    private final T field973;
    public T field972;

    public EnumSetting(String name, T value, String description) {
        super(name, description);
        this.field972 = value;
        this.field973 = value;
    }

    public EnumSetting(String name, T value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field972 = value;
        this.field973 = value;
    }

    public EnumSetting(String name, T value, String description, Setting parent) {
        super(name, description, parent);
        this.field972 = value;
        this.field973 = value;
    }

    public EnumSetting(String name, T value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field972 = value;
        this.field973 = value;
    }

    @Override
    public T getValue() {
        return this.field972;
    }

    @Override
    public T resetValue() {
        return this.field972 = this.field973;
    }

    @Override
    public T setValue(T newVal) {
        this.field972 = newVal;
        if (this.callback != null) {
            this.callback.accept(this.field972);
        }

        return this.field972;
    }

    public int method464(String input) {
        for (int var5 = 0; var5 < this.field972.getClass().getEnumConstants().length; var5++) {
            Enum var6 = ((Enum[]) this.field972.getClass().getEnumConstants())[var5];
            if (var6.name().equalsIgnoreCase(input)) {
                return var5;
            }
        }

        return -1;
    }

    public void method1800(String value) {
        if (this.method465(value) != null) {
            this.field972 = (T) this.method465(value);
        }
    }

    public Enum method465(String value) {
        for (Enum var8 : this.field972.getClass().getEnumConstants()) {
            if (var8.name().equalsIgnoreCase(value)) {
                return var8;
            }
        }

        return null;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method402("value", new EnumArgument(this)).executes(this::lambda$build$0)));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        tag.putString("Value", this.field972.name());
        return tag;
    }

    @Override
    public T load(NbtCompound tag) {
        if (tag.contains("Value")) {
            this.method1800(tag.getString("Value"));
        }

        return this.field972;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object load(NbtCompound nbtCompound) {
    //   return this.method466(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object setValue(Object object) {
    //   return this.method463((T)object);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //   return this.method462();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@//Override
    //public Object getValue() {
    //   return this.method461();
    //}

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        this.setValue((T) var1.getArgument("value", Enum.class));
        return 1;
    }
}
