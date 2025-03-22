package dev.boze.client.instances.impl;

import dev.boze.api.internal.interfaces.IChat;
import dev.boze.client.mixininterfaces.IChatHud;
import dev.boze.client.systems.modules.client.OldColors;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

public class ChatInstance implements IMinecraft, IChat {
    public static MessageIndicator field1636;
    private static Text field1635;

    public static void method2142() {
        field1635 = Text.literal("[Boze] ").setStyle(Style.EMPTY.withColor(-7046189));
        field1636 = new MessageIndicator(9731027, null, Text.literal("Boze message. Cannot be reported."), "Boze");
    }

    public static void method1800(String message) {
        mc.inGameHud.getChatHud().addToMessageHistory(message);
        if (message.startsWith("/")) {
            mc.player.networkHandler.sendCommand(message.substring(1));
        } else {
            mc.player.networkHandler.sendChatMessage(message);
        }
    }

    public static void method624(String message, Object... args) {
        method746(Formatting.GRAY, message, args);
    }

    public static void method740(String prefix, String message, Object... args) {
        method748(0, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), Formatting.GRAY, message, args);
    }

    public static void method741(int id, String prefix, String message, Object... args) {
        method748(id, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), Formatting.GRAY, message, args);
    }

    public static void method625(String message, Object... args) {
        method746(Formatting.YELLOW, message, args);
    }

    public static void method742(String prefix, String message, Object... args) {
        method748(0, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), Formatting.YELLOW, message, args);
    }

    public static void method626(String message, Object... args) {
        method746(Formatting.RED, message, args);
    }

    public static void method743(String prefix, String message, Object... args) {
        method748(0, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), Formatting.RED, message, args);
    }

    public static void method623(Text message) {
        method744(null, message);
    }

    public static void method744(String prefix, Text message) {
        method750(0, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), message);
    }

    public static void method745(int id, String prefix, Text message) {
        method750(id, prefix, Style.EMPTY.withColor(OldColors.method1342().method2010()), message);
    }

    public static void method746(Formatting color, String message, Object... args) {
        method748(0, null, null, color, message, args);
    }

    public static void method747(int id, Formatting color, String message, Object... args) {
        method748(id, null, null, color, message, args);
    }

    public static void method748(
            int id, @Nullable String prefixTitle, @Nullable Style prefixStyle, Formatting messageColor, String messageContent, Object... args
    ) {
        method749(id, prefixTitle, prefixStyle, method755(messageContent, messageColor, args), messageColor);
    }

    public static void method749(int id, @Nullable String prefixTitle, @Nullable Style prefixStyle, String messageContent, Formatting messageColor) {
        MutableText var8 = Text.literal(messageContent);
        var8.setStyle(var8.getStyle().withFormatting(messageColor));
        method750(id, prefixTitle, prefixStyle, var8);
    }

    public static void method750(int id, @Nullable String prefixTitle, @Nullable Style prefixStyle, Text msg) {
        if (mc.world != null) {
            MutableText var7 = Text.literal("");
            var7.append(method754());
            if (prefixTitle != null) {
                var7.append(method753(prefixTitle, prefixStyle));
            }

            var7.append(msg);
            ((IChatHud) mc.inGameHud.getChatHud()).boze$addMessage(var7, id);
        }
    }

    public static void method751(int id, @Nullable String prefixTitle, @Nullable Style prefixStyle, String messageContent) {
        if (mc.world != null) {
            MutableText var7 = Text.literal(method755(messageContent, Formatting.GRAY, (Object) null));
            MutableText var8 = Text.literal("");
            var8.append(method754());
            if (prefixTitle != null) {
                var8.append(method752(prefixTitle, prefixStyle));
            }

            var8.append(var7);
            ((IChatHud) mc.inGameHud.getChatHud()).boze$addMessage(var8, id);
        }
    }

    private static MutableText method752(String var0, Style var1) {
        MutableText var2 = Text.literal("");
        var2.setStyle(var2.getStyle().withFormatting(Formatting.GRAY));
        MutableText var3 = Text.literal(var0);
        var3.setStyle(var1);
        var2.append(var3);
        return var2;
    }

    private static MutableText method753(String var0, Style var1) {
        MutableText var4 = Text.literal("");
        var4.setStyle(var4.getStyle().withFormatting(Formatting.GRAY));
        var4.append("[");
        MutableText var5 = Text.literal(var0);
        var5.setStyle(var1);
        var4.append(var5);
        var4.append("] ");
        return var4;
    }

    private static Text method754() {
        return field1635;
    }

    private static String method755(String var0, Formatting var1, Object... var2) {
        String var5 = String.format(var0, var2);
        var5 = var5.replaceAll("\\(default\\)", var1.toString());
        var5 = var5.replaceAll("\\(highlight\\)", Formatting.WHITE.toString());
        var5 = var5.replaceAll("\\(underline\\)", Formatting.UNDERLINE.toString());
        var5 = var5.replaceAll("\\(green\\)", Formatting.GREEN.toString());
        return var5.replaceAll("\\(red\\)", Formatting.RED.toString());
    }

    public void sendMsg(String message) {
        method624(message);
    }

    public void sendMsg(String title, String message) {
        method740(title, message);
    }

    public void sendWarning(String warning) {
        method625(warning);
    }

    public void sendWarning(String title, String warning) {
        method742(title, warning);
    }

    public void sendError(String error) {
        method626(error);
    }

    public void sendError(String title, String error) {
        method743(title, error);
    }
}
