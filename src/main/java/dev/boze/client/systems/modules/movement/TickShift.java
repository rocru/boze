package dev.boze.client.systems.modules.movement;

import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import mapped.Class3076;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

import java.util.LinkedList;
import java.util.Queue;

public class TickShift extends Module {
   public static final TickShift INSTANCE = new TickShift();
   private final BooleanSetting field881 = new BooleanSetting("OnlyStill", true, "Only start burst when was standing still");
   private final BooleanSetting field882 = new BooleanSetting("OnGround", false, "Only start burst when on ground");
   private final IntSetting field883 = new IntSetting("MovingTicks", 0, 0, 10, 1, "Min ticks moving before burst");
   private final FloatSetting field884 = new FloatSetting("Speed", 2.1F, 1.1F, 50.0F, 0.1F, "Burst speed");
   private final IntSetting field885 = new IntSetting("Ticks", 20, 1, 100, 1, "Burst ticks");
   private final FloatSetting field886 = new FloatSetting("Cooldown", 2.0F, 0.0F, 60.0F, 0.1F, "Cooldown between bursts");
   private final BooleanSetting field887 = new BooleanSetting("Exploit", false, "Maximize burst time");
   private final IntSetting field888 = new IntSetting("StartDelay", 2, 0, 30, 1, "Exploit start delay", this.field887);
   private final BooleanSetting field889 = new BooleanSetting("Repeat", false, "Repeat exploit", this.field887);
   private final IntSetting field890 = new IntSetting("Frequency", 8, 0, 10, 1, "Exploit frequency", this.field887);
   private final BooleanSetting field891 = new BooleanSetting("Delayed", false, "Delay burst server side");
   private final FloatSetting field892 = new FloatSetting("Factor", 0.5F, 0.1F, 1.0F, 0.1F, "Delay factor", this.field891);
   private final BooleanSetting field893 = new BooleanSetting("OnlyOnce", false, "Disable after a burst");
   private final BooleanSetting field894 = new BooleanSetting("AutoDisable", true, "Disable on setback");
   private boolean field895 = false;
   private int field896 = 0;
   private Timer field897 = new Timer();
   public static boolean field898 = false;
   private Timer field899 = new Timer();
   private int field900 = 0;
   private Queue<Packet> field901 = new LinkedList();
   private boolean field902;
   private boolean field903;
   private int field904;

   public TickShift() {
      super("TickShift", "Go very fast for a short amount of time", Category.Movement);
   }

   @Override
   public String method1322() {
      if (this.field895) {
         return Integer.toString(this.field885.method434() - this.field896);
      } else {
         return this.field897.hasElapsed((double)(this.field886.method423() * 1000.0F)) ? "Ready" : "Charging";
      }
   }

   @Override
   public void onEnable() {
      this.field895 = false;
      this.field896 = 0;
      this.field897.setLastTime(0L);
      this.field899.reset();
      this.field900 = 0;
      this.field903 = false;
      this.field904 = 0;
   }

   @Override
   public void onDisable() {
      try {
         Class3076.method6025(this);
         if (!MinecraftUtils.isClientActive()) {
            return;
         }

         while (!this.field901.isEmpty()) {
            mc.player.networkHandler.sendPacket((Packet)this.field901.poll());
         }
      } catch (Exception var5) {
      }
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      if (this.method1971()) {
         this.field904++;
      } else {
         this.field904 = 0;
      }

      if (!this.method1971() || this.field896 >= (int)(this.field892.method423() * (float)this.field885.method434().intValue())) {
         while (!this.field901.isEmpty()) {
            mc.player.networkHandler.sendPacket((Packet)this.field901.poll());
         }
      }

      if (this.method1971() && mc.player.age > 50) {
         this.field896++;
         if (this.field896 > this.field885.method434()) {
            Class3076.method6025(this);
            if (this.field895) {
               this.field895 = false;
               this.field897.reset();
               if (this.field893.method419()) {
                  this.setEnabled(false);
                  return;
               }
            }
         } else if (this.field897.hasElapsed((double)(this.field886.method423() * 1000.0F))
            && (!this.field881.method419() || this.field902)
            && this.field904 > this.field883.method434()
            && (!this.field882.method419() || this.field903 || mc.player.isOnGround())) {
            Class3076.method6024(this, 100, this.field884.method423());
            this.field895 = true;
         }

         if (this.field889.method419()) {
            this.field899.reset();
         }
      } else {
         this.field896 = 0;
         Class3076.method6025(this);
         if (this.field895) {
            this.field895 = false;
            this.field897.reset();
         }
      }

      this.field902 = !this.method1971();
      this.field903 = mc.player.isOnGround();
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      try {
         if (event.packet instanceof PlayerMoveC2SPacket) {
            if (this.field887.method419() && event.packet instanceof LookAndOnGround) {
               if (field898) {
                  return;
               }

               event.method1020();
            }

            if (this.field887.method419() && !this.method1971()) {
               if (field898) {
                  return;
               }

               this.field900++;
               if (this.field890.method434() > 0 && this.field900 > 10 - this.field890.method434()
                  || !this.field899.hasElapsed((double)(this.field888.method434() * 1000))) {
                  this.field900 = 0;
                  event.method1020();
               }
            } else if (this.field891.method419()
               && mc.player.age > 50
               && this.field896 < (int)(this.field892.method423() * (float)this.field885.method434().intValue())) {
               this.field901.add(event.packet);
            }
         }
      } catch (Exception var6) {
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && this.field894.method419()) {
         this.setEnabled(false);
      }
   }

   private boolean method1971() {
      if (mc.player.getX() != mc.player.prevX) {
         return true;
      } else {
         return mc.player.getY() != mc.player.prevY ? true : mc.player.getZ() != mc.player.prevZ;
      }
   }
}
