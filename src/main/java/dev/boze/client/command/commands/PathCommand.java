package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.command.Command;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.systems.modules.movement.Jesus;
import dev.boze.client.systems.modules.movement.NoFall;
import dev.boze.client.systems.pathfinding.Path;
import dev.boze.client.systems.pathfinding.PathBuilder;
import dev.boze.client.systems.pathfinding.PathFinder;
import dev.boze.client.systems.pathfinding.PathRules;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

public class PathCommand extends Command {
    private PathFinder field1380;
    private boolean field1381;
    private boolean field1382;
    private long field1383;

    public PathCommand() {
        super("path", "Path", "Shows the path to the specified coordinates");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403("walking")
                        .then(
                                method402("X", IntegerArgumentType.integer())
                                        .then(method402("Y", IntegerArgumentType.integer()).then(method402("Z", IntegerArgumentType.integer()).executes(this::lambda$build$0)))
                        )
        );
        builder.then(
                method403("flying")
                        .then(
                                method402("X", IntegerArgumentType.integer())
                                        .then(method402("Y", IntegerArgumentType.integer()).then(method402("Z", IntegerArgumentType.integer()).executes(this::lambda$build$1)))
                        )
        );
        builder.then(
                method403("boat")
                        .then(
                                method402("X", IntegerArgumentType.integer())
                                        .then(method402("Y", IntegerArgumentType.integer()).then(method402("Z", IntegerArgumentType.integer()).executes(this::lambda$build$2)))
                        )
        );
    }

    private void method630(BlockPos var1, PathRules var2) {
        if (this.field1381) {
            Boze.EVENT_BUS.unsubscribe(this);
            this.field1381 = false;
            this.field1382 = false;
        }

        this.field1380 = new PathFinder(var1, var2);
        this.field1381 = true;
        this.field1382 = true;
        Boze.EVENT_BUS.subscribe(this);
        this.field1383 = System.nanoTime();
    }

    @EventHandler
    public void method1831(PrePlayerTickEvent event) {
        if (this.field1382) {
            double var5 = (double) (System.nanoTime() - this.field1383) / 1000000.0;
            this.field1380.method2098();
            boolean var7 = this.field1380.method2117();
            if (var7 || this.field1380.method2118()) {
                if (var7) {
                    this.method624("Path found in " + var5 + "ms");
                } else {
                    this.method626("Unable to find path");
                    Boze.EVENT_BUS.unsubscribe(this);
                }

                this.field1382 = false;
            }
        }
    }

    @EventHandler
    public void method2071(Render3DEvent event) {
        if (this.field1380.method2117()) {
            Path var5 = PathBuilder.method616(this.field1380.method2120(), 19.9, 19.9);
            var5.method2097(event, BozeDrawColor.field1841);
        }
    }

    private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
        BlockPos var2 = new BlockPos(
                (Integer) var1.getArgument("X", Integer.class), (Integer) var1.getArgument("Y", Integer.class), (Integer) var1.getArgument("Z", Integer.class)
        );
        this.method630(var2, new PathRules(true, true, true, true));
        return 1;
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        BlockPos var4 = new BlockPos(
                (Integer) var1.getArgument("X", Integer.class), (Integer) var1.getArgument("Y", Integer.class), (Integer) var1.getArgument("Z", Integer.class)
        );
        this.method630(var4, new PathRules(true, Jesus.INSTANCE.isEnabled(), NoFall.INSTANCE.isEnabled(), false));
        return 1;
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        BlockPos var4 = new BlockPos(
                (Integer) var1.getArgument("X", Integer.class), (Integer) var1.getArgument("Y", Integer.class), (Integer) var1.getArgument("Z", Integer.class)
        );
        this.method630(var4, new PathRules(false, Jesus.INSTANCE.isEnabled(), NoFall.INSTANCE.isEnabled(), false));
        return 1;
    }
}
