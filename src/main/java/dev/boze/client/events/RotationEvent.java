package dev.boze.client.events;

import dev.boze.client.enums.AttackMode;
import dev.boze.client.enums.PlaceMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.trackers.EntityTracker;
import mapped.Class2812;
import mapped.Class5913;
import mapped.Class5924;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RotationEvent implements IMinecraft {
   private static final RotationEvent field1283 = new RotationEvent();
   public RotationMode field1284;
   private float yaw;
   private float pitch;
   private Vec3d rotationVector;
   private boolean field1285;
   private boolean field1286;

   public static RotationEvent method553(RotationMode var0, float var1, float var2, Vec3d var3, boolean var4) {
      field1283.field1284 = var0;
      field1283.yaw = var1;
      field1283.pitch = var2;
      field1283.rotationVector = var3;
      field1283.field1285 = var4;
      field1283.field1286 = false;
      return field1283;
   }

   public boolean method554(RotationMode var1) {
      return var1 != this.field1284 || this.method2114();
   }

   public boolean method555(RotationMode var1, boolean var2) {
      return var1 != this.field1284 || this.method2114();
   }

   public boolean method556(Module var1, PlaceMode var2, SwapMode var3, int var4, int var5, BlockHitResult var6, Hand var7) {
      ItemStack var11 = mc.player.getStackInHand(var7);
      boolean var12 = false;
      if (var2 != PlaceMode.Vanilla) {
         if (var5 != -1 && !InventoryUtil.method534(var1, var4, var3, var5)) {
            return false;
         } else {
            if (Class5924.method2088(mc.world.getBlockState(var6.getBlockPos()).getBlock()) && !((ClientPlayerEntityAccessor)mc.player).getLastSneaking()) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
               mc.player.setSneaking(true);
               var12 = true;
            }

            Class2812.field110.put(var6.getBlockPos().offset(var6.getSide()), System.currentTimeMillis());
            Class5913.method17(var7, var6);
            mc.player.swingHand(var7);
            if (var5 != -1) {
               InventoryUtil.method396(var1);
            }

            if (var12) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.player.setSneaking(false);
            }

            field1283.field1286 = true;
            return true;
         }
      } else if (var5 != -1 && InventoryUtil.method2114() && InventoryUtil.method2010() > var4) {
         return false;
      } else {
         if (Class5924.method2088(mc.world.getBlockState(var6.getBlockPos()).getBlock()) && !((ClientPlayerEntityAccessor)mc.player).getLastSneaking()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
            mc.player.setSneaking(true);
            var12 = true;
         }

         int var13 = -1;
         if (var5 >= 0) {
            var13 = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = var5;
         }

         HitResult var14 = mc.crosshairTarget;
         mc.crosshairTarget = var6;
         int var15 = var11.getCount();
         ActionResult var16 = mc.interactionManager.interactBlock(mc.player, var7, var6);
         if (!var16.isAccepted()) {
            if (var13 != -1) {
               mc.player.getInventory().selectedSlot = var13;
            }

            if (var12) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.player.setSneaking(false);
            }

            return false;
         } else {
            if (var16.shouldSwingHand()) {
               mc.player.swingHand(var7);
               if (!var11.isEmpty() && (var11.getCount() != var15 || mc.interactionManager.hasCreativeInventory())) {
                  mc.gameRenderer.firstPersonRenderer.resetEquipProgress(var7);
               }
            }

            if (var13 != -1) {
               mc.player.getInventory().selectedSlot = var13;
            }

            if (var12) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.player.setSneaking(false);
            }

            mc.crosshairTarget = var14;
            return true;
         }
      }
   }

   public boolean method557(Module var1, AttackMode var2, BlockHitResult var3, Hand var4) {
      ItemStack var8 = mc.player.getStackInHand(var4);
      boolean var9 = false;
      if (var2 == AttackMode.Packet) {
         if (Class5924.method2088(mc.world.getBlockState(var3.getBlockPos()).getBlock()) && !((ClientPlayerEntityAccessor)mc.player).getLastSneaking()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
            mc.player.setSneaking(true);
            var9 = true;
         }

         EntityTracker.field3914.put(var3.getBlockPos().offset(var3.getSide()), System.currentTimeMillis());
         Class5913.method17(var4, var3);
         mc.player.swingHand(var4);
         if (var9) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
            mc.player.setSneaking(false);
         }

         field1283.field1286 = true;
         return true;
      } else {
         if (Class5924.method2088(mc.world.getBlockState(var3.getBlockPos()).getBlock()) && !((ClientPlayerEntityAccessor)mc.player).getLastSneaking()) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.PRESS_SHIFT_KEY));
            mc.player.setSneaking(true);
            var9 = true;
         }

         HitResult var10 = mc.crosshairTarget;
         mc.crosshairTarget = var3;
         int var11 = var8.getCount();
         BlockPos var12 = var3.getBlockPos().offset(var3.getSide());
         ActionResult var13 = mc.interactionManager.interactBlock(mc.player, var4, var3);
         if (!var13.isAccepted()) {
            if (var9) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.player.setSneaking(false);
            }

            return false;
         } else {
            EntityTracker.field3914.put(var12, System.currentTimeMillis());
            if (var13.shouldSwingHand()) {
               mc.player.swingHand(var4);
               if (!var8.isEmpty() && (var8.getCount() != var11 || mc.interactionManager.hasCreativeInventory())) {
                  mc.gameRenderer.firstPersonRenderer.resetEquipProgress(var4);
               }
            }

            if (var9) {
               mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.RELEASE_SHIFT_KEY));
               mc.player.setSneaking(false);
            }

            mc.crosshairTarget = var10;
            return true;
         }
      }
   }

   public void method2142() {
      this.field1286 = true;
   }

   public boolean method2114() {
      return this.field1286;
   }
}
