package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.ModuleArgument;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.screens.BindSelectorScreen;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;

public class ModuleBindCommand extends Command {
    private Module module;

    public ModuleBindCommand() {
        super("bind", "Bind", "Binds modules");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method402("module", ModuleArgument.method1003()).executes(this::lambda$build$0));
    }

    @EventHandler
    public void method2071(Render3DEvent event) {
        if (mc.currentScreen == null) {
            mc.setScreen(new BindSelectorScreen(this.module));
            Boze.EVENT_BUS.unsubscribe(this);
        }
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        this.module = ModuleArgument.method1004(var1, "module");
        if (this.module != null) {
            Boze.EVENT_BUS.subscribe(this);
        }

        return 1;
    }
}
