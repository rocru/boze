package dev.boze.client.settings;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.FriendArgument;
import dev.boze.client.command.arguments.PlayerListArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Friends;
import mapped.Class3063;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class FriendsSetting extends Setting<ArrayList<Class3063>> {
    private ArrayList<Class3063> field916;

    public FriendsSetting(String name, ArrayList<Class3063> value, String description) {
        super(name, description);
        this.field916 = value;
    }

    public FriendsSetting(String name, ArrayList<Class3063> value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field916 = value;
    }

    public FriendsSetting(String name, ArrayList<Class3063> value, String description, Setting parent) {
        super(name, description, parent);
        this.field916 = value;
    }

    public FriendsSetting(String name, ArrayList<Class3063> value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field916 = value;
    }

    @Override
    public ArrayList<Class3063> getValue() {
        return this.field916;
    }

    @Override
    public ArrayList<Class3063> resetValue() {
        this.field916.clear();
        return this.field916;
    }

    @Override
    public ArrayList<Class3063> setValue(ArrayList<Class3063> newVal) {
        return this.field916 = newVal;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(method403("add").then(method402("player", PlayerListArgument.method678()).executes(FriendsSetting::lambda$build$0)))
        );
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(method403("del").then(method402("player", FriendArgument.method994()).executes(FriendsSetting::lambda$build$1)))
        );
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(FriendsSetting::lambda$build$3)));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        NbtList var4 = new NbtList();
        this.field916.forEach(v -> lambda$addValueToTag$4(var4, v));
        tag.put("Friends", var4);
        return tag;
    }

    @Override
    public ArrayList<Class3063> load(NbtCompound tag) {
        if (tag.contains("Friends")) {
            NbtList var5 = tag.getList("Friends", 8);
            this.field916.clear();

            for (NbtElement var7 : var5) {
                if (var7 instanceof NbtString) {
                    this.field916.add(new Class3063(var7.asString()));
                }
            }
        }

        return this.field916;
    }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object load(NbtCompound nbtCompound) {
    //    return this.method407(nbtCompound);
    //  }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object setValue(Object object) {
    //    return this.method406((ArrayList<Class3063>)object);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //    return this.method405();
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method2120();
    //}

    private static void lambda$addValueToTag$4(NbtList var0, Class3063 var1) {
        if (!var0.contains(NbtString.of(var1.method5992()))) {
            var0.add(NbtString.of(var1.method5992()));
        }
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        ChatInstance.method624("Friends: " + Friends.method2120().size());
        Friends.method2120().forEach(FriendsSetting::lambda$build$2);
        return 1;
    }

    private static void lambda$build$2(Class3063 var0) {
        ChatInstance.method740("Friends", " - (highlight)%s", var0.method5992());
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        try {
            Class3063 var4 = FriendArgument.method995(var0, "player");
            if (Friends.method345(var4)) {
                ChatInstance.method740("Friends", "Unfriended (highlight)%s", var4.method5992());
            } else {
                ChatInstance.method626("That person is already unfriended.");
            }
        } catch (Exception var5) {
            ChatInstance.method626("That person is already unfriended.");
        }

        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        try {
            GameProfile var4 = PlayerListArgument.method679(var0).getProfile();
            Class3063 var5 = new Class3063(var4.getName());
            if (var5.method5992().equalsIgnoreCase(MinecraftClient.getInstance().player.getName().getString())) {
                ChatInstance.method626("You cannot friend yourself!");
            } else if (Friends.method343(var5)) {
                ChatInstance.method740("Friends", "Friended (highlight)%s", var5.method5992());
            } else {
                ChatInstance.method626("That person is already friended.");
            }
        } catch (Exception var6) {
            ChatInstance.method626("Error friending player");
        }

        return 1;
    }
}
