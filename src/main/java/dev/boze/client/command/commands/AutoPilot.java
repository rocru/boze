package dev.boze.client.command.commands;

import baritone.api.BaritoneAPI;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.systems.modules.movement.ElytraAutoPilot;
import dev.boze.client.systems.modules.movement.elytraautopilot.pi;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.Vec3d;

public class AutoPilot extends Command {
    public AutoPilot() {
        super("autopilot", "AutoPilot", "Elytra Auto Pilot", "ap");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("flyforward").then(method402("getDistance", IntegerArgumentType.integer()).executes(this::lambda$build$0)));
        builder.then(
                method403("flyto")
                        .then(method402("X", IntegerArgumentType.integer()).then(method402("Z", IntegerArgumentType.integer()).executes(this::lambda$build$1)))
        );
        builder.then(method403("land").executes(this::lambda$build$2));
        builder.then(method403("seed").then(method402("value", LongArgumentType.longArg()).executes(AutoPilot::lambda$build$3)));
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        ElytraAutoPilot.INSTANCE.field3206.elytraNetherSeed.value = LongArgumentType.getLong(var0, "value");
        return 1;
    }

    private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
        if (!ElytraAutoPilot.INSTANCE.isEnabled()) {
            this.method625("AutoPilot is not enabled!");
            return 1;
        } else if (mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("stop");
            return 1;
        } else {
            if (pi.field1107) {
                if (mc.player == null) {
                    return 1;
                }

                this.method624("Landing...");
                mc.options.useKey.setPressed(false);
                pi.field1123 = true;
            }

            return 1;
        }
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        if (!ElytraAutoPilot.INSTANCE.isEnabled()) {
            this.method625("AutoPilot is not enabled!");
            return 1;
        } else if (mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
            ElytraAutoPilot.INSTANCE.field3206.elytraAutoJump.value = true;
            BaritoneAPI.getProvider()
                    .getPrimaryBaritone()
                    .getCommandManager()
                    .execute("goto " + IntegerArgumentType.getInteger(var1, "X") + " " + IntegerArgumentType.getInteger(var1, "Z"));
            BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("elytra");
            return 1;
        } else {
            if (mc.player.isFallFlying() && pi.field1127 > (double) ElytraAutoPilot.INSTANCE.field3205.method2010()) {
                pi.field1107 = true;
                pi.field1119 = IntegerArgumentType.getInteger(var1, "X");
                pi.field1120 = IntegerArgumentType.getInteger(var1, "Z");
                pi.field1122 = true;
                pi.field1110 = 3.0;
            } else {
                pi.field1119 = IntegerArgumentType.getInteger(var1, "X");
                pi.field1120 = IntegerArgumentType.getInteger(var1, "Z");
                pi.field1121 = true;
                pi.method1416();
            }

            return 1;
        }
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        if (!ElytraAutoPilot.INSTANCE.isEnabled()) {
            this.method625("AutoPilot is not enabled!");
            return 1;
        } else {
            Vec3d var5 = mc.player
                    .getPos()
                    .add(
                            Vec3d.fromPolar(0.0F, mc.player.getYaw(mc.getRenderTickCounter().getTickDelta(true)))
                                    .normalize()
                                    .multiply(((Integer) var1.getArgument("getDistance", Integer.class)).intValue())
                    );
            if (mc.world.getRegistryKey().getValue().getPath().equals("the_nether")) {
                ElytraAutoPilot.INSTANCE.field3206.elytraAutoJump.value = true;
                BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("goto " + (int) var5.x + " " + (int) var5.z);
                BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute("elytra");
                return 1;
            } else {
                if (mc.player.isFallFlying() && pi.field1127 > (double) ElytraAutoPilot.INSTANCE.field3205.method2010()) {
                    pi.field1107 = true;
                    pi.field1119 = (int) var5.x;
                    pi.field1120 = (int) var5.z;
                    pi.field1122 = true;
                    pi.field1110 = 3.0;
                } else {
                    pi.field1119 = (int) var5.x;
                    pi.field1120 = (int) var5.z;
                    pi.field1121 = true;
                    pi.method1416();
                }

                return 1;
            }
        }
    }
}
