package dev.boze.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Command implements IMinecraft {
    private final String field1370;
    private final String field1371;
    private final String field1372;
    private final List<String> field1373 = new ArrayList();

    public Command(String name, String title, String description, String... aliases) {
        this.field1370 = name;
        this.field1371 = title;
        this.field1372 = description;
        Collections.addAll(this.field1373, aliases);
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> method402(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static LiteralArgumentBuilder<CommandSource> method403(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public final void method619(CommandDispatcher<CommandSource> dispatcher) {
        this.method620(dispatcher, this.field1370);

        for (String var6 : this.field1373) {
            this.method620(dispatcher, var6);
        }
    }

    public void method620(CommandDispatcher<CommandSource> dispatcher, String name) {
        LiteralArgumentBuilder var3 = LiteralArgumentBuilder.literal(name);
        this.method621(var3);
        dispatcher.register(var3);
    }

    public abstract void method621(LiteralArgumentBuilder<CommandSource> var1);

    public String method210() {
        return this.field1370;
    }

    public String method1322() {
        return this.field1372;
    }

    public List<String> method1144() {
        return this.field1373;
    }

    public String toString() {
        return Options.method1563() + this.field1370;
    }

    public String method622(String... args) {
        StringBuilder var5 = new StringBuilder(this.toString());

        for (String var9 : args) {
            var5.append(' ').append(var9);
        }

        return var5.toString();
    }

    public void method623(Text message) {
        ChatInstance.method744(this.field1371, message);
    }

    public void method624(String message, Object... args) {
        ChatInstance.method740(this.field1371, message, args);
    }

    public void method625(String message, Object... args) {
        ChatInstance.method742(this.field1371, message, args);
    }

    public void method626(String message, Object... args) {
        ChatInstance.method743(this.field1371, message, args);
    }
}
