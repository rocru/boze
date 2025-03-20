package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.BlockEntityTypeArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class BlockEntitySetting extends Setting<List<String>> implements IMinecraft {
    private final List<BlockEntityType<?>> field946 = new ArrayList();
    private List<String> field947 = new ArrayList();
    public boolean field948 = true;

    public BlockEntitySetting(String name, String description) {
        super(name, description);
    }

    public BlockEntitySetting(String name, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
    }

    public BlockEntitySetting(String name, String description, Setting parent) {
        super(name, description, parent);
    }

    public BlockEntitySetting(String name, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
    }

    public void method439(ArrayList<String> blockEntityNames) {
        for (String var6 : blockEntityNames) {
            if (!this.field947.contains(var6.toUpperCase(Locale.ENGLISH)) && method440(var6) != null) {
                this.field947.add(var6.toUpperCase(Locale.ENGLISH));
            }
        }
    }

    public boolean method1701(String blockName) {
        if (!this.field947.contains(blockName.toUpperCase(Locale.ENGLISH)) && method440(blockName) != null) {
            this.field947.add(blockName.toUpperCase(Locale.ENGLISH));
            return true;
        } else {
            return false;
        }
    }

    public boolean method346(String blockName) {
        return this.field947.remove(blockName.toUpperCase(Locale.ENGLISH));
    }

    public void method1416() {
        this.field946.clear();
        this.field947.forEach(this::lambda$refreshBlockEntities$0);
    }

    public List<String> method1144() {
        ArrayList<String> var1 = new ArrayList();
        this.field946.forEach(v -> lambda$getBlockEntitiesAsString$1(var1, v));
        return var1;
    }

    public static BlockEntityType<?> method440(String name) {
        for (BlockEntityType var5 : Registries.BLOCK_ENTITY_TYPE) {
            if (Registries.BLOCK_ENTITY_TYPE.getId(var5).getPath().equalsIgnoreCase(name)) {
                return var5;
            }
        }

        return null;
    }

    public List<BlockEntityType<?>> method2032() {
        return this.field946;
    }

    @Override
    public List<String> resetValue() {
        this.field946.clear();
        this.field947.clear();
        return this.field947;
    }

    @Override
    public List<String> getValue() {
        return this.field947;
    }

    @Override
    public List<String> setValue(List<String> newVal) {
        this.field947 = newVal;
        this.method1416();
        if (this.callback != null) {
            this.callback.accept(newVal);
        }

        return this.field947;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403("blockentities").then(method403("add").then(method402("blockentity", BlockEntityTypeArgument.method978()).executes(this::lambda$build$2)))
        );
        builder.then(
                method403("blockentities").then(method403("del").then(method402("blockentity", BlockEntityTypeArgument.method978()).executes(this::lambda$build$3)))
        );
        builder.then(method403("blockentities").then(method403("list").executes(this::lambda$build$5)));
        builder.then(method403("clear").executes(this::lambda$build$6));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        NbtList var4 = new NbtList();
        this.field947.forEach(v -> lambda$addValueToTag$7(var4, v));
        tag.put("Blocks", var4);
        return tag;
    }

    @Override
    public List<String> load(NbtCompound tag) {
        if (tag.contains("Blocks")) {
            NbtList var5 = tag.getList("Blocks", 8);
            this.field947.clear();

            for (NbtElement var7 : var5) {
                if (var7 instanceof NbtString) {
                    this.field947.add(var7.asString());
                }
            }

            this.method1416();
        }

        return this.field947;
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    ///public Object load(NbtCompound nbtCompound) {
    //   return this.method444(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object setValue(Object object) {
    //   return this.method443((List<String>)object);
    // }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public List<String> resetValue() {
    //   return this.method2033();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method442();
    //}

    private static void lambda$addValueToTag$7(NbtList var0, String var1) {
        if (!var0.contains(NbtString.of(var1))) {
            var0.add(NbtString.of(var1));
        }
    }

    private int lambda$build$6(CommandContext var1) throws CommandSyntaxException {
        ChatInstance.method624("Clearing all blocks...");
        this.resetValue();
        if (this.field948) {
            mc.worldRenderer.reload();
        }

        return 1;
    }

    private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
        ChatInstance.method624("Block Entities: " + this.method2032().size());
        this.method2032().forEach(BlockEntitySetting::lambda$build$4);
        return 1;
    }

    private static void lambda$build$4(BlockEntityType var0) {
        ChatInstance.method624(" - (highlight)" + Registries.BLOCK_ENTITY_TYPE.getId(var0).getPath());
    }

    private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
        try {
            BlockEntityType var5 = BlockEntityTypeArgument.method980(var1, "blockentity");
            ChatInstance.method624("Removed " + Registries.BLOCK_ENTITY_TYPE.getId(var5).getPath());
            this.method346(Registries.BLOCK_ENTITY_TYPE.getId(var5).getPath());
            this.method1416();
            if (this.field948) {
                mc.worldRenderer.reload();
            }

            return 1;
        } catch (Exception var6) {
            ChatInstance.method626("Block entity not found!");
            return 0;
        }
    }

    private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
        try {
            BlockEntityType var5 = BlockEntityTypeArgument.method980(var1, "blockentity");
            ChatInstance.method624("Added " + Registries.BLOCK_ENTITY_TYPE.getId(var5).getPath());
            this.method1701(Registries.BLOCK_ENTITY_TYPE.getId(var5).getPath());
            this.method1416();
            if (this.field948) {
                mc.worldRenderer.reload();
            }

            return 1;
        } catch (Exception var6) {
            ChatInstance.method626("Block entity not found!");
            return 0;
        }
    }

    private static void lambda$getBlockEntitiesAsString$1(List<String> var0, BlockEntityType var1) {
        var0.add(Registries.BLOCK_ENTITY_TYPE.getId(var1).getPath());
    }

    private void lambda$refreshBlockEntities$0(String var1) {
        BlockEntityType var4 = method440(var1);
        if (var4 != null) {
            this.field946.add(var4);
        }
    }
}
