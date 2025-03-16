package dev.boze.client.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.Command;
import dev.boze.client.command.arguments.PlayerArgument;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerMoveEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.systems.modules.movement.Jesus;
import dev.boze.client.systems.modules.movement.NoFall;
import dev.boze.client.systems.pathfinding.Path;
import dev.boze.client.systems.pathfinding.PathBuilder;
import dev.boze.client.systems.pathfinding.PathFinder;
import dev.boze.client.systems.pathfinding.PathRules;
import dev.boze.client.Boze;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class FlytoCommand extends Command {
   private PathFinder field1374;
   private boolean field1375;
   private boolean field1376;
   private Path field1377 = null;

   public FlytoCommand() {
      super("flyto", "FlyTo", "Flies to specified location");
   }

   @Override
   public void method621(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(
         method402("X", IntegerArgumentType.integer())
            .then(method402("Y", IntegerArgumentType.integer()).then(method402("Z", IntegerArgumentType.integer()).executes(this::lambda$build$0)))
      );
      builder.then(method402("player", PlayerArgument.method731()).executes(this::lambda$build$1));
   }

   private void method627(BlockPos var1, PathRules var2) {
      if (this.field1375) {
         Boze.EVENT_BUS.unsubscribe(this);
         this.field1375 = false;
         this.field1376 = false;
      }

      this.field1377 = null;
      this.field1374 = new PathFinder(var1, var2);
      this.field1375 = true;
      this.field1376 = true;
      Boze.EVENT_BUS.subscribe(this);
   }

   @EventHandler
   public void method1831(PrePlayerTickEvent event) {
      if (this.field1376) {
         this.field1374.method2098();
         boolean var5 = this.field1374.method2117();
         if (var5 || this.field1374.method2118()) {
            if (var5) {
               this.field1377 = PathBuilder.method616(this.field1374.method2120(), 3.0, 5.0);
               this.field1374 = null;
            } else {
               this.method626("Unable to find path", new Object[0]);
               Boze.EVENT_BUS.unsubscribe(this);
            }

            this.field1376 = false;
         }
      }
   }

   @EventHandler
   public void method1893(PlayerMoveEvent event) {
      if (this.field1377 != null) {
         Vec3d var5 = this.field1377.method2094();
         if (var5 == null) {
            this.field1377 = null;
            Boze.EVENT_BUS.unsubscribe(this);
         } else {
            event.vec3 = var5;
            mc.player.setVelocity(Vec3d.ZERO);
            event.field1892 = true;
         }
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && this.field1377 != null) {
         this.field1377 = null;
         Boze.EVENT_BUS.unsubscribe(this);
         this.method626("Path interrupted", new Object[0]);
      }
   }

   private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
      AbstractClientPlayerEntity var4 = PlayerArgument.method732(var1);
      BlockPos var5 = BlockPos.ofFloored(var4.getPos());
      this.method627(var5, new PathRules(true, Jesus.INSTANCE.isEnabled(), NoFall.INSTANCE.isEnabled(), false));
      return 1;
   }

   private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
      BlockPos var4 = new BlockPos(
         (Integer)var1.getArgument("X", Integer.class), (Integer)var1.getArgument("Y", Integer.class), (Integer)var1.getArgument("Z", Integer.class)
      );
      this.method627(var4, new PathRules(true, Jesus.INSTANCE.isEnabled(), NoFall.INSTANCE.isEnabled(), false));
      return 1;
   }
}
