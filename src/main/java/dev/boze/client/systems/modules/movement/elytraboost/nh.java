package dev.boze.client.systems.modules.movement.elytraboost;

import dev.boze.client.Boze;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.systems.modules.movement.ElytraBoost;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;

public class nh {
   private final Timer field3207;
   public boolean field3208;
   public boolean field3209;
   public boolean field3210;
   public boolean field3211;
   public boolean field3212;
   private final ni field3213;
   final ElytraBoost field3214;

   public nh(final ElytraBoost this$0) {
      this.field3214 = this$0;
      this.field3207 = new Timer();
      this.field3208 = false;
      this.field3209 = false;
      this.field3210 = false;
      this.field3211 = false;
      this.field3212 = false;
      this.field3213 = new ni(this);
   }

   public void method1819() {
      if (MinecraftUtils.isClientActive()) {
         if (this.method1822() != -1) {
            if (!this.field3212) {
               Boze.EVENT_BUS.subscribe(this.field3214.field1011);
               Boze.EVENT_BUS.subscribe(this.field3213);
               this.field3213.field3215.setLastTime(0L);
               this.field3212 = true;
            }
         }
      }
   }

   public void method1820() {
      Boze.EVENT_BUS.unsubscribe(this.field3214.field1011);
      this.field3212 = false;
      this.field3213.field3215.reset();
      if (this.field3208 && this.field3210 && IMinecraft.mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA) {
         this.field3211 = true;
      }

      this.field3208 = false;
   }

   public void method1821() {
      if (!(
            Math.pow(IMinecraft.mc.player.getVelocity().getX(), 2.0) + Math.pow(IMinecraft.mc.player.getVelocity().getZ(), 2.0)
               > Math.pow(this.field3214.field1006.getValue(), 2.0)
         )
         && this.field3207.hasElapsed(1000.0)) {
         float var4 = (float)Math.toRadians((double)IMinecraft.mc.player.getYaw());
         IMinecraft.mc
            .player
            .addVelocity(
               (double)MathHelper.sin(var4) * -(this.field3214.field1005.getValue() * 0.02),
               0.0,
               (double)MathHelper.cos(var4) * this.field3214.field1005.getValue() * 0.02
            );
      }
   }

   public int method1822() {
      return InventoryHelper.method169(nh::lambda$getElytraId$0);
   }

   @EventHandler(
      priority = 50
   )
   public void method1823(PacketBundleEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.packet instanceof EntityTrackerUpdateS2CPacket var5
            && this.field3209
            && (
               IMinecraft.mc.player.isFallFlying()
                     && IMinecraft.mc.player != null
                     && var5.id() == IMinecraft.mc.player.getId()
                     && IMinecraft.mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA
                  || this.field3210
            )) {
            event.method1020();
            return;
         }

         if (event.packet instanceof PlayerPositionLookS2CPacket) {
            this.field3207.reset();
         }
      }
   }

   private static boolean lambda$getElytraId$0(ItemStack var0) {
      return var0.getItem() == Items.ELYTRA;
   }
}
