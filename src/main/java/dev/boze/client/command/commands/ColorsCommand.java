package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ColorArgument;
import dev.boze.client.systems.modules.client.Colors;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class5903;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.minecraft.command.CommandSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;

public class ColorsCommand extends Command {
    public ColorsCommand() {
        super("colors", "Colors", "Manage colors");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("list").executes(this::lambda$build$0));
        builder.then(method403("delete").then(method402("color", ColorArgument.method984()).executes(this::lambda$build$1)));
        builder.then(
                method403("add")
                        .then(
                                method403("static")
                                        .then(
                                                method402("name", StringArgumentType.word())
                                                        .then(
                                                                method402("red", IntegerArgumentType.integer(0, 255))
                                                                        .then(
                                                                                method402("green", IntegerArgumentType.integer(0, 255))
                                                                                        .then(method402("blue", IntegerArgumentType.integer(0, 255)).executes(this::lambda$build$2))
                                                                        )
                                                        )
                                        )
                        )
        );
        builder.then(
                method403("add")
                        .then(
                                method403("changing")
                                        .then(
                                                method402("name", StringArgumentType.word())
                                                        .then(
                                                                method402("hsb", BoolArgumentType.bool())
                                                                        .then(
                                                                                method402("mirror", BoolArgumentType.bool())
                                                                                        .then(
                                                                                                method402("speed", FloatArgumentType.floatArg(-3.0F, 3.0F))
                                                                                                        .then(method402("colors", StringArgumentType.greedyString()).executes(this::lambda$build$3))
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
        );
        builder.then(
                method403("add")
                        .then(
                                method403("gradient")
                                        .then(
                                                method402("name", StringArgumentType.word())
                                                        .then(
                                                                method402("hsb", BoolArgumentType.bool())
                                                                        .then(
                                                                                method402("mirror", BoolArgumentType.bool())
                                                                                        .then(
                                                                                                method402("angle", FloatArgumentType.floatArg(0.0F, 1.0F))
                                                                                                        .then(
                                                                                                                method402("spin", FloatArgumentType.floatArg(-1.0F, 1.0F))
                                                                                                                        .then(
                                                                                                                                method402("scale", FloatArgumentType.floatArg(0.1F, 10.0F))
                                                                                                                                        .then(
                                                                                                                                                method402("motion", FloatArgumentType.floatArg(-10.0F, 10.0F))
                                                                                                                                                        .then(method402("colors", StringArgumentType.greedyString()).executes(this::lambda$build$4))
                                                                                                                                        )
                                                                                                                        )
                                                                                                        )
                                                                                        )
                                                                        )
                                                        )
                                        )
                        )
        );
    }

    private void method628(String var1, Class5903<?> var2) {
        this.method624(" - (highlight)%s (default)%s", var1, var2.toString());
    }

    private int lambda$build$4(CommandContext var1) throws CommandSyntaxException {
        String var5 = StringArgumentType.getString(var1, "name");
        if (Colors.INSTANCE.field2343.containsKey(var5)) {
            this.method626("Color (highlight)%s(default) already exists", var5);
            return 1;
        } else {
            boolean var6 = BoolArgumentType.getBool(var1, "hsb");
            boolean var7 = BoolArgumentType.getBool(var1, "mirror");
            float var8 = FloatArgumentType.getFloat(var1, "angle");
            float var9 = FloatArgumentType.getFloat(var1, "spin");
            if (var8 > 1.0F) {
                var8 /= 360.0F;
                var8 %= 1.0F;
            }

            float var10 = FloatArgumentType.getFloat(var1, "scale");
            float var11 = FloatArgumentType.getFloat(var1, "motion");
            String[] var12 = StringArgumentType.getString(var1, "colors").split(" ");
            File var13 = new File(mc.runDirectory, "versions/" + mc.getGameVersion() + "/" + mc.getGameVersion() + ".json");
            if (!var13.exists()) {
                // ???
                // var13 = new File(Paths.get(Knot.programArgs[5]).getParent().toString(), "patches" + File.separator + "dev.boze.json");
            }

            if (var12.length % 3 == 0 && var12.length >= 6 && var13.exists()) {
                int[] var14 = new int[var12.length];

                for (int var15 = 0; var15 < var12.length; var15++) {
                    try {
                        var14[var15] = Integer.parseInt(var12[var15]);
                    } catch (NumberFormatException var17) {
                        this.method626("Invalid color format. Please check the System > Colors docs.");
                        return 1;
                    }
                }

                GradientColor var19 = new GradientColor(var5);

                for (byte var16 = 0; var16 < var14.length; var16 += 3) {
                    var19.field422.add(new StaticColor(var14[var16], var14[var16 + 1], var14[var16 + 2]));
                }

                var19.field424 = var6;
                var19.field425 = var7;
                var19.field426 = var8;
                var19.field427 = var9;
                var19.field428 = var10;
                var19.field429 = var11;
                Colors.INSTANCE.field2343.put(var5, var19);
                this.method624("Added color (highlight)%s (default)%s", var5, var19.toString());
                return 1;
            } else {
                this.method626("Invalid color format. Please check the System > Colors docs.");
                return 1;
            }
        }
    }

    private int lambda$build$3(CommandContext var1) throws CommandSyntaxException {
        String var5 = StringArgumentType.getString(var1, "name");
        if (Colors.INSTANCE.field2343.containsKey(var5)) {
            this.method626("Color (highlight)%s(default) already exists", var5);
            return 1;
        } else {
            boolean var6 = BoolArgumentType.getBool(var1, "hsb");
            boolean var7 = BoolArgumentType.getBool(var1, "mirror");
            float var8 = FloatArgumentType.getFloat(var1, "speed");
            String[] var9 = StringArgumentType.getString(var1, "colors").split(" ");
            File var10 = new File(mc.runDirectory, "versions/" + mc.getGameVersion() + "/" + mc.getGameVersion() + ".json");
            if (!var10.exists()) {
                var10 = new File(Paths.get(Knot.programArgs[5]).getParent().toString(), "patches" + File.separator + "dev.boze.json");
            }

            if (var9.length % 3 == 0 && var9.length >= 6 && var10.exists()) {
                int[] var11 = new int[var9.length];

                for (int var12 = 0; var12 < var9.length; var12++) {
                    try {
                        var11[var12] = Integer.parseInt(var9[var12]);
                    } catch (NumberFormatException var14) {
                        this.method626("Invalid color format. Please check the System > Colors docs.");
                        return 1;
                    }
                }

                ChangingColor var15 = new ChangingColor();

                for (byte var13 = 0; var13 < var11.length; var13 += 3) {
                    var15.field416.add(new StaticColor(var11[var13], var11[var13 + 1], var11[var13 + 2]));
                }

                var15.field417 = var6;
                var15.field418 = var7;
                var15.field419 = var8;
                Colors.INSTANCE.field2343.put(var5, var15);
                this.method624("Added color (highlight)%s (default)%s", var5, var15.toString());
                return 1;
            } else {
                this.method626("Invalid color format. Please check the System > Colors docs.");
                return 1;
            }
        }
    }

    private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
        String var5 = StringArgumentType.getString(var1, "name");
        if (Colors.INSTANCE.field2343.containsKey(var5)) {
            this.method626("Color (highlight)%s(default) already exists", var5);
            return 1;
        } else {
            int var6 = IntegerArgumentType.getInteger(var1, "red");
            int var7 = IntegerArgumentType.getInteger(var1, "green");
            int var8 = IntegerArgumentType.getInteger(var1, "blue");
            StaticColor var9 = new StaticColor(var6, var7, var8);
            Colors.INSTANCE.field2343.put(var5, var9);
            this.method624("Added color (highlight)%s (default)%s", var5, var9.toString());
            return 1;
        }
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        String var5 = (String) var1.getArgument("color", String.class);
        if (Colors.INSTANCE.method1335(var5)) {
            this.method624("Deleted color (highlight)%s", var5);
        } else {
            this.method626("Could not delete color (highlight)%s, as it does not exist", var5);
        }

        return 1;
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        HashMap var5 = Colors.INSTANCE.field2343;
        if (!var5.isEmpty()) {
            this.method624("Colors (%d):", var5.size());
            var5.forEach(this::method628);
        } else {
            this.method624("No custom colors");
        }

        return 1;
    }
}
