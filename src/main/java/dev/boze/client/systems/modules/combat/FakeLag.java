package dev.boze.client.systems.modules.combat;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class3069;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.*;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

public class FakeLag extends Module {
   public static final FakeLag INSTANCE = new FakeLag();
   private final BooleanSetting render = new BooleanSetting("Render", true, "Render server position");
   private final BooleanSetting allowInteract = new BooleanSetting("AllowInteract", false, "Allow interactions");
   private final BooleanSetting pulse = new BooleanSetting("Pulse", false, "Only blink periodically");
   private final BooleanSetting reduceExposure = new BooleanSetting("ReduceExposure", false, "Send packets when least exposed", this.pulse);
   private final FloatSetting factor = new FloatSetting("Factor", 0.5F, 0.1F, 10.0F, 0.1F, "Seconds to blink for", this.pulse);
   private final BooleanSetting strict = new BooleanSetting("Strict", false, "Always send packets when in the air", this.pulse);
   private final Queue<Packet<?>> field471 = new LinkedList();
   private final AtomicBoolean field472 = new AtomicBoolean(false);
   private float field473 = 999.0F;
   private FakePlayerEntity field474;

   public FakeLag() {
      super("FakeLag", "Fakes lag and high latency", Category.Combat);
   }

   @Override
   public String method1322() {
      return Integer.toString(this.field471.size());
   }

   @EventHandler
   private void method1853(PrePacketSendEvent var1) {
      Packet var5 = var1.packet;
      if (!this.field472.get()) {
         if (this.pulse.getValue()) {
            if (var1.packet instanceof PlayerMoveC2SPacket) {
               if (this.strict.getValue() && !((PlayerMoveC2SPacket)var1.packet).isOnGround()) {
                  this.field472.set(true);

                  while (!this.field471.isEmpty()) {
                     Packet var6 = (Packet)this.field471.poll();
                     mc.player.networkHandler.sendPacket(var6);
                  }

                  this.field472.set(false);
                  this.field471.clear();
                  if (this.render.getValue() && this.field474 != null) {
                     RenderSystem.recordRenderCall(this::lambda$onPacketSend$0);
                  }
               } else {
                  var1.method1020();
                  this.field471.add(var1.packet);
               }
            }
         } else if (!(var5 instanceof ChatMessageC2SPacket)
            && !(var5 instanceof TeleportConfirmC2SPacket)
            && (!(var5 instanceof HandSwingC2SPacket) || !this.allowInteract.getValue())
            && (!(var5 instanceof PlayerInteractBlockC2SPacket) || !this.allowInteract.getValue())
            && (!(var5 instanceof PlayerInteractItemC2SPacket) || !this.allowInteract.getValue())
            && !(var5 instanceof KeepAliveC2SPacket)
            && !(var5 instanceof RequestCommandCompletionsC2SPacket)
            && !(var5 instanceof ClientStatusC2SPacket)) {
            var1.method1020();
            this.field471.add(var1.packet);
         }
      }
   }

   @EventHandler
   private void method1942(PostPlayerTickEvent var1) {
      if (this.pulse.getValue()
         && MinecraftUtils.isClientActive()
         && ((float)this.field471.size() >= this.factor.getValue() * 10.0F || this.reduceExposure.getValue() && this.method1384() <= this.field473)) {
         this.field473 = this.method1384();
         this.field472.set(true);

         while (!this.field471.isEmpty()) {
            Packet var5 = (Packet)this.field471.poll();
            mc.player.networkHandler.sendPacket(var5);
         }

         if (this.render.getValue()) {
            if (this.field474.isRemoved()) {
               this.field474.method2142();
            }

            this.field474.method547(mc.player);
         }

         this.field472.set(false);
         this.field471.clear();
      }
   }

   @Override
   public void onEnable() {
      if (MinecraftUtils.isClientActive() && !mc.isIntegratedServerRunning()) {
         this.field472.set(false);
         this.field471.clear();
         this.field473 = 999.0F;
         this.field474 = new FakePlayerEntity(mc.player, mc.player.getGameProfile().getName(), 20.0F, true);
         this.field474.field1265 = true;
         if (this.render.getValue()) {
            this.field474.method2142();
         }
      } else {
         this.setEnabled(false);
      }
   }

   @Override
   public void onDisable() {
      if (!MinecraftUtils.isClientActive()) {
         this.field471.clear();
         this.field474 = null;
      } else {
         while (!this.field471.isEmpty()) {
            mc.player.networkHandler.sendPacket((Packet)this.field471.poll());
         }

         if (this.field474 != null) {
            this.field474.method1416();
            this.field474 = null;
         }
      }
   }

   private float method1384() {
      float var4 = 0.0F;

      for (Entity var6 : mc.world.getEntities()) {
         if (var6 instanceof EndCrystalEntity && var6.distanceTo(mc.player) < 6.0F) {
            var4 = (float)((double)var4 + Class3069.method6004(mc.player, var6.getPos()));
         }
      }

      return var4;
   }

   private void lambda$onPacketSend$0() {
      if (this.field474.isRemoved()) {
         this.field474.method2142();
      }

      this.field474.method547(mc.player);
   }
}
