package dev.boze.client.systems.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Capes;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class BetterTab extends Module {
    public static final BetterTab INSTANCE = new BetterTab();
    public final IntSetting field2914 = new IntSetting("MaxSize", 250, 50, 500, 10, "Max players to show in tab");
    public final IntSetting field2915 = new IntSetting("MaxHeight", 30, 5, 50, 1, "Max players per column");
    public final BooleanSetting field2916 = new BooleanSetting("ShowPing", true, "Show players' ping instead of bars");
    public final RGBASetting field2918 = new RGBASetting("Users", new RGBAColor(-7046189), "Color for Boze users", Capes.INSTANCE::isEnabled);
    private final RGBASetting field2917 = new RGBASetting("Friends", new RGBAColor(-15277290), "Color for friends");

    public BetterTab() {
        super("BetterTab", "Better player tab", Category.Misc);
        this.field435 = true;
        this.addSettings(this.field2917, this.field2918);
    }

    private RGBAColor method1689(GameProfile var1, String var2) {
        if (Capes.INSTANCE.isEnabled() && Capes.field1290.containsKey(var1.getId().toString())) {
            if (Capes.field1290.get(var1.getId().toString()).equals("default")) {
                return this.field2918.getValue();
            }

            if (Capes.field1290.get(var1.getId().toString()).equals("beta")) {
                return RGBAColor.field407;
            }
        }

        return Friends.INSTANCE.isEnabled() && Friends.method346(var2) ? this.field2917.getValue() : null;
    }

    public Text method1690(PlayerListEntry playerListEntry) {
        Text var5 = playerListEntry.getDisplayName();
        if (var5 == null) {
            var5 = Text.literal(playerListEntry.getProfile().getName());
        }

        RGBAColor var6 = this.method1689(playerListEntry.getProfile(), var5.getString());
        if (var6 != null) {
            String var7 = var5.getString();

            for (Formatting var11 : Formatting.values()) {
                if (var11.isColor()) {
                    var7 = var7.replace(var11.toString(), "");
                }
            }

            var5 = Text.literal(var7).setStyle(var5.getStyle().withColor(TextColor.fromRgb(var6.method2010())));
        }

        return var5;
    }
}
