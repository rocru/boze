package dev.boze.client.systems.modules.legit;

import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Teams extends Module {
   public static final Teams INSTANCE = new Teams();
   private final BooleanSetting field2826 = new BooleanSetting("Vanilla", true, "Detect team members using vanilla team system");
   private final BooleanSetting field2827 = new BooleanSetting("NameStyle", false, "Detect team members using their name formatting style");
   private final BooleanSetting field2828 = new BooleanSetting("NameColor", false, "Detect team members using their name color");
   private static final Pattern field2829 = Pattern.compile("(?i)§[0-9A-F]");

   public Teams() {
      super("Teams", "Automatically marks your team members as friends", Category.Legit);
   }

   public static boolean method1617(PlayerEntity player) {
      if (INSTANCE.field2826.method419() && player.isTeammate(mc.player)) {
         return true;
      } else {
         if (INSTANCE.field2827.method419() || INSTANCE.field2828.method419()) {
            PlayerListEntry var4 = null;
            PlayerListEntry var5 = null;
            if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerList() != null) {
               for (PlayerListEntry var7 : mc.getNetworkHandler().getPlayerList()) {
                  if (var7.getProfile().equals(mc.player.getGameProfile())) {
                     var4 = var7;
                  } else if (var7.getProfile().equals(player.getGameProfile())) {
                     var5 = var7;
                  }

                  if (var4 != null && var5 != null) {
                     break;
                  }
               }
            }

            if (var4 != null && var5 != null) {
               Text var15 = mc.inGameHud.getPlayerListHud().getPlayerName(var4);
               Text var16 = mc.inGameHud.getPlayerListHud().getPlayerName(var5);
               if (var15 != null && var16 != null) {
                  if (INSTANCE.field2827.method419()) {
                     if (var15.getStyle() != null && var16.getStyle() != null && var15.getStyle().equals(var16.getStyle())) {
                        return true;
                     }

                     Style var8 = null;
                     Style var9 = null;

                     for (Text var11 : var15.getSiblings()) {
                        if (var11.getStyle().getColor() != null) {
                           var8 = var11.getStyle();
                           break;
                        }
                     }

                     for (Text var21 : var16.getSiblings()) {
                        if (var21.getStyle().getColor() != null) {
                           var9 = var21.getStyle();
                           break;
                        }
                     }

                     if (var8 != null && var9 != null && var8.equals(var9)) {
                        return true;
                     }
                  }

                  if (INSTANCE.field2828.method419()) {
                     String var17 = var15.getString();
                     String var18 = var16.getString();
                     Matcher var20 = field2829.matcher(var17);
                     Matcher var22 = field2829.matcher(var18);
                     if (var20.find() && var22.find()) {
                        if (var20.groupCount() <= 1 || var22.groupCount() <= 1) {
                           return false;
                        }

                        try {
                           String var12 = var20.group(1);
                           String var13 = var22.group(1);
                           if (var12.equals(var13)) {
                              return true;
                           }
                        } catch (Exception var14) {
                           return false;
                        }
                     }
                  }
               }
            }
         }

         return false;
      }
   }
}
