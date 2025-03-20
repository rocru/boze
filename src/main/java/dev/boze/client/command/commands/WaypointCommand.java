package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.PlayerListArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Waypoints;
import dev.boze.client.systems.waypoints.WayPoint;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;

public class WaypointCommand extends Command {
    public WaypointCommand() {
        super("waypoints", "Waypoints", "Manage waypoints");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403("add")
                        .then(
                                method402("name", StringArgumentType.string())
                                        .then(
                                                method402("x", IntegerArgumentType.integer())
                                                        .then(
                                                                method402("y", IntegerArgumentType.integer())
                                                                        .then(method402("z", IntegerArgumentType.integer()).executes(WaypointCommand::lambda$build$0))
                                                        )
                                        )
                        )
        );
        builder.then(method403("add").then(method402("name", StringArgumentType.string()).executes(WaypointCommand::lambda$build$1)));
        builder.then(method403("del").then(method402("name", StringArgumentType.string()).executes(WaypointCommand::lambda$build$3)));
        builder.then(
                method403("share")
                        .then(
                                method402("name", StringArgumentType.string())
                                        .then(method402("player", PlayerListArgument.method678()).executes(WaypointCommand::lambda$build$5))
                        )
        );
        builder.then(method403("list").executes(this::lambda$build$7));
        builder.then(method403("clear").executes(this::lambda$build$9));
    }

    private int lambda$build$9(CommandContext commandContext) throws CommandSyntaxException {
        this.method624("Clearing all waypoints for the current server/dimension...");
        String string = WaypointCommand.mc.world.getRegistryKey().getValue().getPath();
        String string2 = WaypointCommand.mc.getCurrentServerEntry().address;
        Waypoints.INSTANCE.field2437.getValue().removeIf(arg_0 -> WaypointCommand.lambda$build$8(string, string2, arg_0));
        return 1;
    }

    private static boolean lambda$build$8(String var0, String var1, WayPoint var2) {
        return var2.field912.equals(var0) && var2.field913.equalsIgnoreCase(var1);
    }

    private int lambda$build$7(CommandContext var1) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null && !mc.getCurrentServerEntry().address.isEmpty() && mc.world != null
        ) {
            this.method624("Waypoints: ");
            Waypoints.INSTANCE.field2437.getValue().forEach(this::lambda$build$6);
            return 1;
        } else {
            return 0;
        }
    }

    private void lambda$build$6(WayPoint var1) {
        if (var1.field912.equals(mc.world.getRegistryKey().getValue().getPath()) && var1.field913.equalsIgnoreCase(mc.getCurrentServerEntry().address)) {
            int var5 = var1.field911;
            int var6 = var1.field910;
            int var7 = var1.field909;
            String var8 = var1.field908;
            this.method624(" - " + var8 + " X" + var7 + " Y" + var6 + " Z" + var5);
        }
    }

    private static int lambda$build$5(CommandContext var0) throws CommandSyntaxException {
        PlayerListEntry var3 = PlayerListArgument.method679(var0);
        String var4 = var3.getProfile().getName();
        String var5 = (String) var0.getArgument("name", String.class);
        WayPoint var6 = Waypoints.INSTANCE.field2437.getValue().stream().filter(arg_0 -> WaypointCommand.lambda$build$4(var5, arg_0)).findFirst().orElse(null);
        if (var6 != null) {
            int var10001 = var6.field909;
            int var10002 = var6.field910;
            int var10003 = var6.field911;
            String var7 = var6.field912.replace("_", " ");
            int var8 = var10003;
            int var9 = var10002;
            int var10 = var10001;
            ChatInstance.method1800("/msg " + var4 + " I shared a waypoint at X" + var10 + " Y" + var9 + " Z" + var8 + " in " + var7 + " with you");
        }

        return 1;
    }

    private static boolean lambda$build$4(String var0, WayPoint var1) {
        return var1.field908.equalsIgnoreCase(var0) && var1.field913.equalsIgnoreCase(mc.getCurrentServerEntry().address);
    }

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        String var3 = (String) var0.getArgument("name", String.class);
        Waypoints.INSTANCE.field2437.getValue().removeIf(arg_0 -> WaypointCommand.lambda$build$2(var3, arg_0));
        return 1;
    }

    private static boolean lambda$build$2(String var0, WayPoint var1) {
        return var1.field908.equalsIgnoreCase(var0);
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null
                && mc.getCurrentServerEntry().address != null
                && mc.player != null
                && !mc.getCurrentServerEntry().address.isEmpty()
                && mc.world != null) {
            Waypoints.INSTANCE
                    .field2437
                    .getValue()
                    .add(
                            new WayPoint(
                                    (String) var0.getArgument("name", String.class),
                                    (int) mc.player.getX(),
                                    (int) mc.player.getY(),
                                    (int) mc.player.getZ(),
                                    mc.world.getRegistryKey().getValue().getPath(),
                                    mc.getCurrentServerEntry().address
                            )
                    );
            return 1;
        } else {
            return 0;
        }
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null && !mc.getCurrentServerEntry().address.isEmpty() && mc.world != null
        ) {
            Waypoints.INSTANCE
                    .field2437
                    .getValue()
                    .add(
                            new WayPoint(
                                    (String) var0.getArgument("name", String.class),
                                    (Integer) var0.getArgument("x", Integer.class),
                                    (Integer) var0.getArgument("y", Integer.class),
                                    (Integer) var0.getArgument("z", Integer.class),
                                    mc.world.getRegistryKey().getValue().getPath(),
                                    mc.getCurrentServerEntry().address
                            )
                    );
            return 1;
        } else {
            return 0;
        }
    }
}
