package dev.boze.client.systems.modules.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.enums.ExtraChatMode;
import dev.boze.client.events.AddMessageEvent;
import dev.boze.client.mixininterfaces.IChatHudLine;
import dev.boze.client.mixininterfaces.IChatHudLineVisible;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.RGBAColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtraChat extends Module {
   public static final ExtraChat INSTANCE = new ExtraChat();
   private final EnumSetting<ExtraChatMode> field2929 = new EnumSetting<ExtraChatMode>(
      "Mode", ExtraChatMode.Anarchy, "Mode for ExtraChat", ExtraChat::lambda$new$0
   );
   public final BooleanSetting field2930 = new BooleanSetting("Suffix", true, "Boze client suffix", this::method1699);
   public final WeirdSettingString field2931 = new WeirdSettingString("SuffixText", " | boze.dev", "Client suffix text");
   public final BooleanSetting field2932 = new BooleanSetting("GreenText", false, "Green text prefix", this::method1699);
   public final RGBASetting field2933 = new RGBASetting("Color", new RGBAColor(-13421569), "Chat color");
   public final BooleanSetting field2934 = new BooleanSetting("Fade", true, "Fade in messages");
   public final IntSetting field2935 = new IntSetting("Duration", 10, 1, 50, 1, "Fade duration", this.field2934);
   public final BooleanSetting field2936 = new BooleanSetting("Slide", false, "Slide in messages");
   public final IntSetting field2937 = new IntSetting("Duration", 10, 1, 50, 1, "Fade duration", this.field2936);
   public final BooleanSetting field2938 = new BooleanSetting("Timestamps", true, "Show timestamps in chat");
   public final BooleanSetting field2939 = new BooleanSetting("Heads", true, "Show heads in chat");
   public final BooleanSetting field2940 = new BooleanSetting("PortalChat", true, "Chat in portals", this::method1699);
   public final BooleanSetting field2941 = new BooleanSetting("FriendNotify", true, "Notify when you friend people", this::method1699);
   public final BooleanSetting field2942 = new BooleanSetting("CoordProtect", true, "Protect from sending coords in chat");
   public final IntSetting field2943 = new IntSetting("MinDigits", 5, 4, 8, 1, "Minimum digits per coordinate to flag", this.field2942);
   public final SimpleDateFormat field2944 = new SimpleDateFormat("HH:mm");
   private static final Pattern field2945 = Pattern.compile("^(?:<[0-9]{2}:[0-9]{2}>\\s)?<(.*?)>.*");

   public boolean method1698() {
      return Options.INSTANCE.method1971() || this.field2929.method461() == ExtraChatMode.Ghost;
   }

   public boolean method1699() {
      return !this.method1698();
   }

   public ExtraChat() {
      super("ExtraChat", "Customize chat", Category.Misc);
      this.field2933.method206(true);
      this.field435 = true;
      this.addSettings(this.field2933);
   }

   @EventHandler
   private void method1700(AddMessageEvent var1) {
      if (this.field2938.method419()) {
         Text var4 = var1.method1041();
         MutableText var5 = Text.literal("<" + this.field2944.format(new Date()) + "> ").formatted(Formatting.GRAY);
         MutableText var6 = Text.empty().append(var5).append(var4);
         var1.method1043(var6);
      }
   }

   public static boolean method1701(String var0) {
      int var4 = 0;

      for (char var8 : var0.toCharArray()) {
         if (Character.isDigit(var8)) {
            var4++;
         }
      }

      return var4 >= INSTANCE.field2943.method434();
   }

   public void method1702(DrawContext var1, Visible var2, int var3, int var4) {
      if (this.isEnabled() && this.field2939.method419()) {
         if (((IChatHudLineVisible)var2).boze$isFirst()) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float)RGBAColor.method188(var4) / 255.0F);
            this.method1703(var1, (IChatHudLine)var2, var3);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
         }

         var1.getMatrices().translate(10.0F, 0.0F, 0.0F);
      }
   }

   private void method1703(DrawContext v1, IChatHudLine v2, int i3) {
      String v7 = v2.boze$getText().trim();
      GameProfile v8 = this.method1704(v2, v7);
      if (v8 != null) {
         PlayerListEntry v9 = mc.getNetworkHandler().getPlayerListEntry(v8.getId());
         if (v9 != null) {
            Identifier v10 = v9.getSkinTextures().texture();
            v1.drawTexture(v10, 0, i3, 8, 8, 8.0F, 8.0F, 8, 8, 64, 64);
            v1.drawTexture(v10, 0, i3, 8, 8, 40.0F, 8.0F, 8, 8, 64, 64);
         }
      }
   }

   private GameProfile method1704(IChatHudLine var1, String var2) {
      GameProfile var6 = var1.boze$getSenderProfile();
      if (var6 == null) {
         Matcher var7 = field2945.matcher(var2);
         if (var7.matches()) {
            String var8 = var7.group(1);
            PlayerListEntry var9 = mc.getNetworkHandler().getPlayerListEntry(var8);
            if (var9 != null) {
               var6 = var9.getProfile();
            }
         }
      }

      return var6;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
