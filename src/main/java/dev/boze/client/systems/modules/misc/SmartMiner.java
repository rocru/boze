package dev.boze.client.systems.modules.misc;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritone;
import baritone.api.Settings;
import baritone.api.process.ICustomGoalProcess;
import baritone.api.process.IMineProcess;
import dev.boze.client.enums.SmartMineInventoryMode;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.StringModeSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.ItemEnchantmentUtils;
import mapped.Class2839;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Predicate;

public class SmartMiner extends Module {
   public static final SmartMiner INSTANCE = new SmartMiner();
   private final StringModeSetting field475 = new StringModeSetting("Blocks", "Blocks to mine");
   private final EnumSetting<SmartMineInventoryMode> field476 = new EnumSetting<SmartMineInventoryMode>(
      "WhenDone", SmartMineInventoryMode.Logout, "What to do when inventory is full"
   );
   private final BooleanSetting field477 = new BooleanSetting("Repair", true, "Automatically mend tools");
   private final IntSetting field478 = new IntSetting("Durability", 30, 1, 90, 1, "Durability at which to start repairing tools", this.field477);
   private final BooleanSetting field479 = new BooleanSetting("Coal", true, "Mine coal ore to mend tools", this.field477);
   private final BooleanSetting field480 = new BooleanSetting("Redstone", true, "Mine redstone ore to mend tools", this.field477);
   private final BooleanSetting field481 = new BooleanSetting("NetherQuartz", true, "Mine quartz ore to mend tools", this.field477);
   private final IBaritone field482 = BaritoneAPI.getProvider().getPrimaryBaritone();
   private final Settings field483 = BaritoneAPI.getSettings();
   private BlockPos field484 = null;
   private boolean field485;
   private boolean field486;
   private final HashMap<Block, String> field487 = new HashMap();

   public SmartMiner() {
      super("SmartMiner", "Automatically mines and mends tools", Category.Misc);
      this.field475.field951 = false;
   }

   @Override
   public void onEnable() {
      if (mc.player == null) {
         this.setEnabled(false);
      } else {
         this.field485 = (Boolean)this.field483.mineScanDroppedItems.value;
         this.field483.mineScanDroppedItems.value = true;
         this.field484 = mc.player.getBlockPos();
         this.field486 = false;
      }
   }

   @Override
   public void onDisable() {
      this.field482.getPathingBehavior().cancelEverything();
      this.field483.mineScanDroppedItems.value = this.field485;
   }

   @EventHandler
   private void method1810(PostTickEvent param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: invokestatic dev/boze/client/utils/MinecraftUtils.isClientActive ()Z
      // 003: ifne 007
      // 006: return
      // 007: aload 0
      // 008: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1975 ()Z
      // 00b: ifeq 10d
      // 00e: aload 0
      // 00f: getfield dev/boze/client/systems/modules/misc/SmartMiner.field476 Ldev/boze/client/settings/EnumSetting;
      // 012: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 015: checkcast dev/boze/client/enums/SmartMineInventoryMode
      // 018: invokevirtual dev/boze/client/enums/SmartMineInventoryMode.ordinal ()I
      // 01b: tableswitch 241 0 3 51 29 97 97
      // 038: aload 0
      // 039: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 03c: ldc "Inventory full, done mining"
      // 03e: bipush 0
      // 03f: anewarray 230
      // 042: invokestatic dev/boze/client/instances/impl/ChatInstance.method740 (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      // 045: aload 0
      // 046: bipush 0
      // 047: invokevirtual dev/boze/client/systems/modules/Module.setEnabled (Z)Z
      // 04a: pop
      // 04b: goto 10c
      // 04e: aload 0
      // 04f: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 052: ldc "Inventory full, logging out"
      // 054: bipush 0
      // 055: anewarray 230
      // 058: invokestatic dev/boze/client/instances/impl/ChatInstance.method740 (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      // 05b: getstatic dev/boze/client/systems/modules/misc/SmartMiner.mc Lnet/minecraft/client/MinecraftClient;
      // 05e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 061: getfield net/minecraft/client/network/ClientPlayerEntity.networkHandler Lnet/minecraft/client/network/ClientPlayNetworkHandler;
      // 064: new net/minecraft/network/packet/s2c/common/DisconnectS2CPacket
      // 067: dup
      // 068: ldc "[SmartMiner] Inventory full, logged out out"
      // 06a: invokestatic net/minecraft/text/Text.literal (Ljava/lang/String;)Lnet/minecraft/text/MutableText;
      // 06d: invokespecial net/minecraft/network/packet/s2c/common/DisconnectS2CPacket.<init> (Lnet/minecraft/text/Text;)V
      // 070: invokevirtual net/minecraft/client/network/ClientPlayNetworkHandler.sendPacket (Lnet/minecraft/network/packet/Packet;)V
      // 073: aload 0
      // 074: bipush 0
      // 075: invokevirtual dev/boze/client/systems/modules/Module.setEnabled (Z)Z
      // 078: pop
      // 079: goto 10c
      // 07c: aload 0
      // 07d: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1973 ()Z
      // 080: ifeq 0b1
      // 083: aload 0
      // 084: getfield dev/boze/client/systems/modules/misc/SmartMiner.field484 Lnet/minecraft/util/math/BlockPos;
      // 087: ifnull 0b1
      // 08a: aload 0
      // 08b: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 08e: ldc_w "Returning to start position"
      // 091: bipush 0
      // 092: anewarray 230
      // 095: invokestatic dev/boze/client/instances/impl/ChatInstance.method740 (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      // 098: aload 0
      // 099: getfield dev/boze/client/systems/modules/misc/SmartMiner.field482 Lbaritone/api/IBaritone;
      // 09c: invokeinterface baritone/api/IBaritone.getCustomGoalProcess ()Lbaritone/api/process/ICustomGoalProcess; 1
      // 0a1: new baritone/api/pathing/goals/GoalBlock
      // 0a4: dup
      // 0a5: aload 0
      // 0a6: getfield dev/boze/client/systems/modules/misc/SmartMiner.field484 Lnet/minecraft/util/math/BlockPos;
      // 0a9: invokespecial baritone/api/pathing/goals/GoalBlock.<init> (Lnet/minecraft/util/math/BlockPos;)V
      // 0ac: invokeinterface baritone/api/process/ICustomGoalProcess.setGoalAndPath (Lbaritone/api/pathing/goals/Goal;)V 2
      // 0b1: getstatic dev/boze/client/systems/modules/misc/SmartMiner.mc Lnet/minecraft/client/MinecraftClient;
      // 0b4: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0b7: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getBlockPos ()Lnet/minecraft/util/math/BlockPos;
      // 0ba: aload 0
      // 0bb: getfield dev/boze/client/systems/modules/misc/SmartMiner.field484 Lnet/minecraft/util/math/BlockPos;
      // 0be: invokevirtual net/minecraft/util/math/BlockPos.equals (Ljava/lang/Object;)Z
      // 0c1: ifeq 10c
      // 0c4: aload 0
      // 0c5: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 0c8: ldc_w "Returned to start position"
      // 0cb: bipush 0
      // 0cc: anewarray 230
      // 0cf: invokestatic dev/boze/client/instances/impl/ChatInstance.method740 (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      // 0d2: aload 0
      // 0d3: bipush 0
      // 0d4: invokevirtual dev/boze/client/systems/modules/Module.setEnabled (Z)Z
      // 0d7: pop
      // 0d8: aload 0
      // 0d9: getfield dev/boze/client/systems/modules/misc/SmartMiner.field476 Ldev/boze/client/settings/EnumSetting;
      // 0dc: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0df: getstatic dev/boze/client/enums/SmartMineInventoryMode.ReturnLog Ldev/boze/client/enums/SmartMineInventoryMode;
      // 0e2: if_acmpne 10c
      // 0e5: aload 0
      // 0e6: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 0e9: ldc_w "Returned to start position, logging out"
      // 0ec: bipush 0
      // 0ed: anewarray 230
      // 0f0: invokestatic dev/boze/client/instances/impl/ChatInstance.method740 (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
      // 0f3: getstatic dev/boze/client/systems/modules/misc/SmartMiner.mc Lnet/minecraft/client/MinecraftClient;
      // 0f6: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0f9: getfield net/minecraft/client/network/ClientPlayerEntity.networkHandler Lnet/minecraft/client/network/ClientPlayNetworkHandler;
      // 0fc: new net/minecraft/network/packet/s2c/common/DisconnectS2CPacket
      // 0ff: dup
      // 100: ldc_w "[SmartMiner] Returned to start position, logged out"
      // 103: invokestatic net/minecraft/text/Text.literal (Ljava/lang/String;)Lnet/minecraft/text/MutableText;
      // 106: invokespecial net/minecraft/network/packet/s2c/common/DisconnectS2CPacket.<init> (Lnet/minecraft/text/Text;)V
      // 109: invokevirtual net/minecraft/client/network/ClientPlayNetworkHandler.sendPacket (Lnet/minecraft/network/packet/Packet;)V
      // 10c: return
      // 10d: aload 0
      // 10e: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1971 ()Z
      // 111: ifne 125
      // 114: ldc_w "No pickaxe found in hotbar, disabling"
      // 117: bipush 0
      // 118: anewarray 230
      // 11b: invokestatic dev/boze/client/instances/impl/ChatInstance.method625 (Ljava/lang/String;[Ljava/lang/Object;)V
      // 11e: aload 0
      // 11f: bipush 0
      // 120: invokevirtual dev/boze/client/systems/modules/Module.setEnabled (Z)Z
      // 123: pop
      // 124: return
      // 125: aload 0
      // 126: getfield dev/boze/client/systems/modules/misc/SmartMiner.field486 Z
      // 129: ifeq 155
      // 12c: aload 0
      // 12d: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1972 ()Z
      // 130: ifne 147
      // 133: ldc_w "Done repairing, continuing to mine"
      // 136: bipush 0
      // 137: anewarray 230
      // 13a: invokestatic dev/boze/client/instances/impl/ChatInstance.method624 (Ljava/lang/String;[Ljava/lang/Object;)V
      // 13d: aload 0
      // 13e: bipush 0
      // 13f: putfield dev/boze/client/systems/modules/misc/SmartMiner.field486 Z
      // 142: aload 0
      // 143: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1904 ()V
      // 146: return
      // 147: aload 0
      // 148: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1974 ()Z
      // 14b: ifeq 188
      // 14e: aload 0
      // 14f: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1854 ()V
      // 152: goto 188
      // 155: aload 0
      // 156: getfield dev/boze/client/systems/modules/misc/SmartMiner.field477 Ldev/boze/client/settings/BooleanSetting;
      // 159: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 15c: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 15f: ifeq 17d
      // 162: aload 0
      // 163: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1972 ()Z
      // 166: ifeq 17d
      // 169: ldc_w "Repairing"
      // 16c: bipush 0
      // 16d: anewarray 230
      // 170: invokestatic dev/boze/client/instances/impl/ChatInstance.method624 (Ljava/lang/String;[Ljava/lang/Object;)V
      // 173: aload 0
      // 174: bipush 1
      // 175: putfield dev/boze/client/systems/modules/misc/SmartMiner.field486 Z
      // 178: aload 0
      // 179: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1854 ()V
      // 17c: return
      // 17d: aload 0
      // 17e: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1974 ()Z
      // 181: ifeq 188
      // 184: aload 0
      // 185: invokevirtual dev/boze/client/systems/modules/misc/SmartMiner.method1904 ()V
      // 188: return
   }

   private boolean method1971() {
      Predicate var4 = this::lambda$findPickaxe$0;
      int var5 = InventoryHelper.method168(var4);
      if (var5 != -1) {
         if (var5 != mc.player.getInventory().selectedSlot) {
            Class2839.field111 = var5;
            mc.player.getInventory().selectedSlot = var5;
            ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
         }

         return true;
      } else {
         return false;
      }
   }

   private void method1904() {
      Block[] var1 = new Block[this.field475.method2032().size()];
      this.field482.getPathingBehavior().cancelEverything();
      this.field482.getMineProcess().mine((Block[])this.field475.method2032().toArray(var1));
   }

   private void method1854() {
      ArrayList var4 = new ArrayList();
      if (this.field479.getValue()) {
         var4.add(Blocks.COAL_ORE);
      }

      if (this.field480.getValue()) {
         var4.add(Blocks.REDSTONE_ORE);
      }

      if (this.field481.getValue()) {
         var4.add(Blocks.NETHER_QUARTZ_ORE);
      }

      Block[] var5 = new Block[var4.size()];
      this.field482.getPathingBehavior().cancelEverything();
      this.field482.getMineProcess().mine((Block[])var4.toArray(var5));
   }

   private boolean method1972() {
      ItemStack var4 = mc.player.getMainHandStack();
      double var5 = (double)((float)(var4.getMaxDamage() - var4.getDamage()) * 100.0F / (float)var4.getMaxDamage());
      return !(var5 > 95.0) && (!(var5 > (double)this.field478.getValue().intValue()) || this.field486);
   }

   private boolean method1973() {
      return !(this.field482.getPathingControlManager().mostRecentInControl().orElse(null) instanceof ICustomGoalProcess);
   }

   private boolean method1974() {
      return !(this.field482.getPathingControlManager().mostRecentInControl().orElse(null) instanceof IMineProcess);
   }

   private boolean method1975() {
      StringBuilder v4 = new StringBuilder();

      for (Block v6 : this.field475.method2032()) {
         v4.append(this.method259(v6));
      }

      String var9 = v4.toString();

      for (int i6 = 0; i6 <= 35; i6++) {
         ItemStack v7 = mc.player.getInventory().getStack(i6);
         if (v7.isEmpty()) {
            return false;
         }

         if (v7.getCount() < v7.getMaxCount() && var9.contains(((RegistryKey)v7.getItem().getRegistryEntry().getKey().get()).getValue().getPath())) {
            return false;
         }
      }

      return true;
   }

   private String method259(Block var1) {
      if (this.field487.containsKey(var1)) {
         return (String)this.field487.get(var1);
      } else {
         try {
            Identifier var5 = Registries.BLOCK.getId(var1);
            String var6 = "data/minecraft/loot_tables/blocks/" + var5.getPath() + ".json";
            InputStream var7 = MinecraftClient.class.getClassLoader().getResourceAsStream(var6);
            BufferedReader var8 = new BufferedReader(new InputStreamReader(var7));
            StringBuilder var9 = new StringBuilder();

            while (var8.ready()) {
               String var10 = var8.readLine();
               if (var10.contains("\"name\"")) {
                  var9.append(var10, var10.lastIndexOf(":") + 1, var10.lastIndexOf("\"")).append(":");
               }
            }

            String var12 = var9.toString();
            this.field487.put(var1, var12);
            return var12;
         } catch (Exception var11) {
            return "";
         }
      }
   }

   private boolean lambda$findPickaxe$0(ItemStack var1) {
      return var1.getItem() instanceof PickaxeItem
         && (
            !this.field477.getValue()
               || ItemEnchantmentUtils.hasEnchantment(var1, Enchantments.MENDING) && !ItemEnchantmentUtils.hasEnchantment(var1, Enchantments.SILK_TOUCH)
         );
   }
}
