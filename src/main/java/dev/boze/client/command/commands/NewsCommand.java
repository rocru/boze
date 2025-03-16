package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.screens.NewsScreen;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.utils.http.NewsUtil;
import dev.boze.client.utils.network.BozeExecutor;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import dev.boze.client.Boze;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.CommandSource;

public class NewsCommand extends Command {
   private String[] field1379 = null;

   public NewsCommand() {
      super("news", "News", "Get the latest news from the 2b2t times");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$build$2);
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      if (mc.currentScreen == null && this.field1379 != null) {
         mc.setScreen(new NewsScreen(this.field1379));
         Boze.EVENT_BUS.unsubscribe(this);
         this.field1379 = null;
      }
   }

   private int lambda$build$2(CommandContext var1) throws CommandSyntaxException {
      BozeExecutor.method2200(this::lambda$build$1);
      return 1;
   }

   private void lambda$build$1() {
      List var1 = NewsUtil.getNews();
      var1.removeIf(NewsCommand::lambda$build$0);
      this.field1379 = (String[])var1.toArray(new String[0]);
      Boze.EVENT_BUS.subscribe(this);
   }

   private static boolean lambda$build$0(String var0) {
      String var4;
      try {
         var4 = NewsUtil.method2190(var0);
      } catch (NoSuchAlgorithmException var13) {
         var4 = var0;
      }

      File var5 = new File(ConfigManager.news, var4 + ".png");
      if (!var5.exists()) {
         try {
            URL var6 = new URL(var0);
            BufferedInputStream var7 = new BufferedInputStream(var6.openStream());
            ByteArrayOutputStream var8 = new ByteArrayOutputStream();
            byte[] var9 = new byte[1024];
            int var10 = 0;

            while (-1 != (var10 = var7.read(var9))) {
               var8.write(var9, 0, var10);
            }

            var8.close();
            var7.close();
            byte[] var11 = var8.toByteArray();
            FileOutputStream var12 = new FileOutputStream(var5);
            var12.write(var11);
            var12.close();
            return false;
         } catch (Exception var14) {
            return true;
         }
      } else {
         return true;
      }
   }
}
