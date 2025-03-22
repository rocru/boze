package dev.boze.client.command.commands;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.core.BozeLogger;
import dev.boze.client.core.Version;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.systems.modules.ModulePrinter;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.systems.modules.combat.OffHand;
import dev.boze.client.systems.modules.combat.Surround;
import dev.boze.client.systems.modules.misc.FakePlayer;
import dev.boze.client.systems.modules.misc.Replenish;
import dev.boze.client.utils.InventoryDebugger;
import net.minecraft.command.CommandSource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DebugCommand extends Command {
    public static boolean field1378 = false;

    public DebugCommand() {
        super("debug", "Debug", "Debug command");
    }

    private static int lambda$build$9(CommandContext var0) throws CommandSyntaxException {
        if (InventoryDebugger.field1628) {
            Boze.EVENT_BUS.unsubscribe(InventoryDebugger.class);
            InventoryDebugger.field1628 = false;
        } else {
            Boze.EVENT_BUS.subscribe(InventoryDebugger.class);
            InventoryDebugger.field1628 = true;
        }

        return 1;
    }

    private static int lambda$build$8(CommandContext var0) throws CommandSyntaxException {
        try {
            FileWriter var3 = new FileWriter(new File(Boze.FOLDER, "features.md"));
            ModulePrinter var4 = new ModulePrinter(var3);
            var4.method1328();
            var3.close();
        } catch (IOException var5) {
        }

        return 1;
    }

    private static int lambda$build$2(CommandContext var0) throws CommandSyntaxException {
        AutoMine.field2518 = !AutoMine.field2518;
        return 1;
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        AutoCrystal.field1039 = !AutoCrystal.field1039;
        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        AutoCrystal.field1038 = !AutoCrystal.field1038;
        return 1;
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        if (Version.isBeta) {
            builder.then(method403("autocrystal").executes(DebugCommand::lambda$build$0));
            builder.then(method403("autocrystal-time").executes(DebugCommand::lambda$build$1));
            builder.then(method403("automine").executes(DebugCommand::lambda$build$2));
            builder.then(method403("clip-offhand").then(method402("seconds", IntegerArgumentType.integer(1)).executes(this::lambda$build$3)));
            builder.then(method403("clip-replenish").then(method402("seconds", IntegerArgumentType.integer(1)).executes(this::lambda$build$4)));
            builder.then(method403("clip-surround").then(method402("seconds", IntegerArgumentType.integer(1)).executes(this::lambda$build$5)));
            builder.then(
                    ((LiteralArgumentBuilder) method403("fakeplayerpath").then(method403("save").executes(this::lambda$build$6)))
                            .then(method403("load").executes(this::lambda$build$7))
            );
            builder.then(method403("features").executes(DebugCommand::lambda$build$8));
            builder.then(method403("inventory").executes(DebugCommand::lambda$build$9));
        }
    }

    private int lambda$build$7(CommandContext var1) throws CommandSyntaxException {
        try {
            JsonObject var4 = ConfigManager.readFile(Boze.FOLDER, "fakeplayer");
            FakePlayer.INSTANCE.method1714(var4);
        } catch (Exception var5) {
            this.method626("Error loading path file, make sure it's .minecraft/Boze/fakeplayer.json");
        }

        return 1;
    }

    private int lambda$build$6(CommandContext var1) throws CommandSyntaxException {
        JsonObject var4 = FakePlayer.INSTANCE.recordPositions();
        ConfigManager.writeFile(Boze.FOLDER, "fakeplayer", var4);
        this.method624("Saved path to .minecraft/Boze");
        return 1;
    }

    private int lambda$build$5(CommandContext var1) throws CommandSyntaxException {
        int var4 = IntegerArgumentType.getInteger(var1, "seconds");
        String var5 = BozeLogger.method523(Surround.INSTANCE, var4);
        this.method624("Debug log for last " + var4 + " seconds:");
        this.method624(var5);
        return 1;
    }

    private int lambda$build$4(CommandContext var1) throws CommandSyntaxException {
        int var4 = IntegerArgumentType.getInteger(var1, "seconds");
        String var5 = BozeLogger.method523(Replenish.INSTANCE, var4);
        this.method624("Debug log for last " + var4 + " seconds:");
        this.method624(var5);
        return 1;
    }

    private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
        int var4 = IntegerArgumentType.getInteger(var1, "seconds");
        String var5 = BozeLogger.method523(OffHand.INSTANCE, var4);
        this.method624("Debug log for last " + var4 + " seconds:");
        this.method624(var5);
        return 1;
    }
}
