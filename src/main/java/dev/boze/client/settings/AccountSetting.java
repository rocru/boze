package dev.boze.client.settings;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.Boze;
import dev.boze.client.command.arguments.AccountArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.accounts.Account;
import dev.boze.client.systems.accounts.types.MicrosoftAccount;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import org.lwjgl.glfw.GLFW;

import java.util.Base64;

public class AccountSetting extends Setting<String> implements IMinecraft {
    private String field981;
    private final String field982;

    public AccountSetting(String name, String description) {
        super(name, description);

        try {
            this.field981 = mc.getSession().getUsername();
        } catch (Exception var4) {
            if (mc.player != null) {
                this.field981 = mc.player.getName().getString();
            }
        }

        this.field982 = this.field981;
    }

    @Override
    public String getValue() {
        return this.field981;
    }

    @Override
    public String resetValue() {
        return this.field981 = this.field982;
    }

    @Override
    public String setValue(String newVal) {
        this.field981 = newVal;
        return newVal;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(method403("share").then(method402("account", AccountArgument.method972()).executes(AccountSetting::lambda$build$0)));
        builder.then(method403("import").executes(AccountSetting::lambda$build$1));
        builder.then(method403("list").executes(AccountSetting::lambda$build$3));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        return tag;
    }

    @Override
    public String load(NbtCompound tag) {
        return "";
    }

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object load(NbtCompound nbtCompound) {
    //   return this.method1286(nbtCompound);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object setValue(Object object) {
    //   return this.method1341((String)object);
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object resetValue() {
    //   return this.method1562();
    //}

    // $VF: synthetic method
    // $VF: bridge method
    //@Override
    //public Object getValue() {
    //   return this.method1322();
    //}

    private static int lambda$build$3(CommandContext var0) throws CommandSyntaxException {
        ChatInstance.method624("Accounts: " + Boze.getAccounts().method1133());
        Boze.getAccounts().method1135().forEach(AccountSetting::lambda$build$2);
        return 1;
    }

    private static void lambda$build$2(Account var0) {
        ChatInstance.method740("AltManager", " - (highlight)%s", var0.method210());
    }

    private static int lambda$build$1(CommandContext var0) throws CommandSyntaxException {
        String var4 = GLFW.glfwGetClipboardString(mc.getWindow().getHandle());
        if (var4 != null && var4.length() > 0) {
            byte[] var5 = Base64.getDecoder().decode(var4);
            String var6 = new String(var5);
            MicrosoftAccount var7 = new MicrosoftAccount(var6);
            if (var7.method2114()) {
                Boze.getAccounts().method1130(var7);
                ChatInstance.method624("Added account " + var7.method210() + " from clipboard");
                return 1;
            }
        }

        ChatInstance.method626("Unable to add account from clipboard");
        return 1;
    }

    private static int lambda$build$0(CommandContext var0) throws CommandSyntaxException {
        Account var4 = AccountArgument.method974(var0, "account");
        if (var4 instanceof MicrosoftAccount) {
            var4.method2114();
            ChatInstance.method624("Copied account token to clipboard");
            String var5 = Base64.getEncoder().encodeToString(var4.method210().getBytes());
            GLFW.glfwSetClipboardString(mc.getWindow().getHandle(), var5);
        } else {
            ChatInstance.method625(var4.method210() + " is not a microsoft account");
        }

        return 1;
    }
}
