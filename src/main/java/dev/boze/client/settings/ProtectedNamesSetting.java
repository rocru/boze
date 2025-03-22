package dev.boze.client.settings;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.PlayerListArgument;
import dev.boze.client.instances.impl.ChatInstance;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.HashMap;
import java.util.Locale;

public class ProtectedNamesSetting extends Setting<HashMap<String, String>> {
    private HashMap<String, String> field976 = new HashMap();

    public ProtectedNamesSetting(String name, String description) {
        super(name, description);
    }

    private static void lambda$addValueToTag$4(NbtList var0, String var1, String var2) {
        String var3 = var1 + ":" + var2;
        var0.add(NbtString.of(var3));
    }

    private static void lambda$build$2(String var0, String var1) {
        ChatInstance.method740("Media", " - (highlight)%s(default) - %s", var0, var1);
    }

    @Override
    public HashMap<String, String> getValue() {
        return this.field976;
    }

    @Override
    public HashMap<String, String> resetValue() {
        this.field976.clear();
        return this.field976;
    }

    @Override
    public HashMap<String, String> setValue(HashMap<String, String> newVal) {
        return this.field976 = newVal;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(
                                method403("add")
                                        .then(method402("player", PlayerListArgument.method678()).then(method402("name", StringArgumentType.word()).executes(this::lambda$build$0)))
                        )
        );
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(method403("del").then(method402("player", StringArgumentType.string()).executes(this::lambda$build$1)))
        );
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(this::lambda$build$3)));
        return true;
    }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object load(NbtCompound nbtCompound) {
    //   return this.method471(nbtCompound);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object setValue(Object object) {
    //    return this.method470((HashMap<String, String>)object);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //   return this.method469();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //public Object getValue() {
    //   return this.method1282();
    // }

    @Override
    public NbtCompound save(NbtCompound tag) {
        NbtList nbtList = new NbtList();
        this.field976.forEach((arg_0, arg_1) -> ProtectedNamesSetting.lambda$addValueToTag$4(nbtList, arg_0, arg_1));
        tag.put("Names", nbtList);
        return tag;
    }

    @Override
    public HashMap<String, String> load(NbtCompound tag) {
        if (tag.contains("Names")) {
            NbtList var5 = tag.getList("Names", 8);
            this.field976.clear();

            for (NbtElement var7 : var5) {
                if (var7 instanceof NbtString) {
                    String[] var8 = var7.asString().split(":");
                    if (var8.length == 2) {
                        this.field976.put(var8[0], var8[1]);
                    }
                }
            }
        }

        return this.field976;
    }

    private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
        ChatInstance.method624("Protected Names: " + this.field976.size());
        this.field976.forEach(ProtectedNamesSetting::lambda$build$2);
        return 1;
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        this.field976.remove(StringArgumentType.getString(var1, "player").toLowerCase(Locale.ROOT));
        return 1;
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        GameProfile var4 = PlayerListArgument.method679(var1).getProfile();
        this.field976.put(var4.getName().toLowerCase(Locale.ROOT), StringArgumentType.getString(var1, "name"));
        return 1;
    }
}
