package dev.boze.client.systems.modules.misc;

import dev.boze.client.events.Render2DEvent;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.mixin.ShulkerBoxScreenHandlerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.KitSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.InventoryUtil;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.screen.ShulkerBoxScreenHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class Regear extends Module {
   public static final Regear INSTANCE = new Regear();
   private final KitSetting field3055 = new KitSetting("Kit", "", "Current kit");
   private final FloatSetting field3056 = new FloatSetting("Delay", 0.5F, 0.0F, 5.0F, 0.1F, "Delay for moving items");
   private final BooleanSetting field3057 = new BooleanSetting("ReplaceItems", false, "Replace items in inventory if they don't match kit");
   private dev.boze.client.utils.Timer field3058 = new dev.boze.client.utils.Timer();

   public Regear() {
      super("ReGear", "Automatically re-gears you when you open a shulker", Category.Misc);
   }

   @Override
   public void onEnable() {
      if (this.field3055.method1283().isEmpty()) {
         ChatInstance.method740(this.internalName, "Please select a kit using the regear command before using the module");
         this.setEnabled(false);
      }
   }

   @EventHandler
   private void method1749(Render2DEvent var1) {
      if (mc.currentScreen instanceof ShulkerBoxScreen var5
         && !this.field3055.method1283().isEmpty()
         && this.field3055.method1282() != null
         && !this.field3055.method1282().isEmpty()) {
         ShulkerBoxScreenHandler var11 = (ShulkerBoxScreenHandler)var5.getScreenHandler();
         AtomicBoolean var7 = new AtomicBoolean(false);

         for (int var8 = 0; var8 < ((ShulkerBoxScreenHandlerAccessor)var11).getInventory().size(); var8++) {
            ItemStack var9 = ((ShulkerBoxScreenHandlerAccessor)var11).getInventory().getStack(var8);
            if (!var9.isEmpty()) {
               this.field3055.method1282().forEach(this::lambda$onRender2D$0);
            }
         }

         if (var7.get() && AntiCheat.INSTANCE.field2322.method419() && !InventoryUtil.isInventoryOpen()) {
            mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
         }
      }
   }

   private void lambda$onRender2D$0(ItemStack var1, int var2, AtomicBoolean var3, String var4, Integer var5) {
      if (this.field3058.hasElapsed((double)(this.field3056.method423() * 50.0F))) {
         ItemStack var9 = mc.player.getInventory().getStack(var5);
         if (var9.isEmpty() || var9.getItem() == Items.AIR || this.field3057.method419()) {
            if (!this.field3057.method419()
               || var9.isEmpty() && var9.getItem() == Items.AIR
               || var9.getCount() < var9.getMaxCount()
               || !var9.getItem().equals(var1.getItem())) {
               int var10 = var4.indexOf(">");
               if (var4.substring(0, var10).equalsIgnoreCase(var1.getItem().getName().getString())) {
                  if (var9.getCount() >= var9.getMaxCount() && var9.getItem().equals(var1.getItem())) {
                     return;
                  }

                  InvUtils.method2201().method2206(var2).method2213(var5);
                  var3.set(true);
                  this.field3058.reset();
               }
            }
         }
      }
   }
}
