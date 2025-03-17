package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import dev.boze.client.Boze;
import dev.boze.client.command.Command;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;

import java.util.ArrayList;
import java.util.Collections;

public class PluginsCommand extends Command {
    private final Timer field1384 = new Timer();

    public PluginsCommand() {
        super("plugins", "Plugins", "Reveals server plugins by requesting command suggestions\nSome servers may block/flag this\n");
    }

    @Override
    public void method621(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(this::lambda$build$0);
    }

    @EventHandler
    private void method1810(PostTickEvent var1) {
        if (this.field1384.hasElapsed(5000.0)) {
            this.method626("Request timed out");
            Boze.EVENT_BUS.unsubscribe(this);
        }
    }

    @EventHandler
    private void method2042(PacketBundleEvent var1) {
        try {
            if (var1.packet instanceof CommandSuggestionsS2CPacket var5) {
                ArrayList<String> var13 = new ArrayList();
                Suggestions var7 = var5.getSuggestions();
                if (var7 == null) {
                    this.method626("No plugins found");
                    Boze.EVENT_BUS.unsubscribe(this);
                    return;
                }

                for (Suggestion var9 : var7.getList()) {
                    String[] var10 = var9.getText().split(":");
                    if (var10.length > 1) {
                        String var11 = var10[0].replace("/", "");
                        if (!var13.contains(var11)) {
                            var13.add(var11);
                        }
                    }
                }

                Collections.sort(var13);
                if (!var13.isEmpty()) {
                    this.method624("Found plugins (%d):", var13.size());
                    var13.forEach(this::lambda$onReadPacket$1);
                } else {
                    this.method626("No plugins found");
                }

                Boze.EVENT_BUS.unsubscribe(this);
            }
        } catch (Exception var12) {
            this.method626("No plugins found");
            Boze.EVENT_BUS.unsubscribe(this);
        }
    }

    private void lambda$onReadPacket$1(String var1) {
        this.method624(" - (highlight)%s", var1);
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        this.method624("Requesting suggestions...");
        Boze.EVENT_BUS.subscribe(this);
        mc.player.networkHandler.sendPacket(new RequestCommandCompletionsC2SPacket(0, "/"));
        this.field1384.reset();
        return 1;
    }
}
