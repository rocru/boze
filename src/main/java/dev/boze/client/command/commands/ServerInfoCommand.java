package dev.boze.client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import java.net.InetAddress;
import java.net.UnknownHostException;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandSource;

public class ServerInfoCommand extends Command {
   public ServerInfoCommand() {
      super("server", "Server", "See server info");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.executes(this::lambda$build$0);
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      if (mc.isIntegratedServerRunning()) {
         this.method626("You're not connected to a server", new Object[0]);
      }

      ServerInfo var5 = mc.getCurrentServerEntry();
      if (var5 == null) {
         this.method626("Couldn't get current server info", new Object[0]);
         return 1;
      } else {
         String var6 = "";

         try {
            var6 = InetAddress.getByName(var5.address).getHostAddress();
         } catch (UnknownHostException var8) {
            var6 = var5.address;
         }

         this.method624("IP: " + var6, new Object[0]);
         this.method624("Port: " + ServerAddress.parse(var5.address).getPort(), new Object[0]);
         if (mc.getNetworkHandler().getBrand() != null) {
            this.method624("Brand: " + mc.getNetworkHandler().getBrand(), new Object[0]);
         }

         return 1;
      }
   }
}
