package dev.boze.client.systems.modules.misc.autoarmor;

import dev.boze.client.systems.modules.combat.AutoMend;
import dev.boze.client.systems.modules.misc.AutoArmor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

class nd {
   private final int field2878;
   private int field2879;
   private int field2880;
   private int field2881;
   private int field2882;
   final AutoArmor field2883;

   public nd(final AutoArmor arg, int id) {
      this.field2883 = arg;
      this.field2878 = id;
   }

   public void method1650() {
      this.field2879 = -1;
      this.field2880 = -1;
      this.field2881 = -1;
      this.field2882 = Integer.MAX_VALUE;
   }

   public void method1651(ItemStack itemStack, int slot) {
      int var6 = this.field2883.method1645(itemStack);
      if (var6 > this.field2880) {
         this.field2880 = var6;
         this.field2879 = slot;
      }
   }

   public void method1652() {
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
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 0
      // 001: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 004: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor.method1647 ()Z
      // 007: ifeq 00b
      // 00a: return
      // 00b: getstatic dev/boze/client/utils/IMinecraft.mc Lnet/minecraft/client/MinecraftClient;
      // 00e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 011: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getInventory ()Lnet/minecraft/entity/player/PlayerInventory;
      // 014: aload 0
      // 015: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2878 I
      // 018: invokevirtual net/minecraft/entity/player/PlayerInventory.getArmorStack (I)Lnet/minecraft/item/ItemStack;
      // 01b: astore 4
      // 01d: aload 4
      // 01f: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 022: getstatic net/minecraft/item/Items.ELYTRA Lnet/minecraft/item/Item;
      // 025: if_acmpne 0d6
      // 028: aload 4
      // 02a: invokestatic net/minecraft/item/ElytraItem.isUsable (Lnet/minecraft/item/ItemStack;)Z
      // 02d: ifne 038
      // 030: aload 0
      // 031: bipush -1
      // 032: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 035: goto 0c6
      // 038: aload 0
      // 039: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 03c: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor.method1638 ()Ldev/boze/client/enums/AutoArmorElytra;
      // 03f: invokevirtual dev/boze/client/enums/AutoArmorElytra.ordinal ()I
      // 042: tableswitch 132 0 3 30 38 76 67
      // 060: aload 0
      // 061: bipush -1
      // 062: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 065: goto 0c6
      // 068: aload 0
      // 069: aload 0
      // 06a: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 06d: getfield dev/boze/client/systems/modules/misc/AutoArmor.bindState Ldev/boze/client/settings/BooleanSetting;
      // 070: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 073: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 076: ifeq 07e
      // 079: ldc 690420
      // 07b: goto 07f
      // 07e: bipush -1
      // 07f: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 082: goto 0c6
      // 085: aload 0
      // 086: ldc 690420
      // 088: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 08b: goto 0c6
      // 08e: aload 0
      // 08f: getstatic dev/boze/client/systems/modules/movement/ElytraFly.INSTANCE Ldev/boze/client/systems/modules/movement/ElytraFly;
      // 092: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 095: ifne 0bd
      // 098: getstatic dev/boze/client/systems/modules/movement/ElytraBoost.INSTANCE Ldev/boze/client/systems/modules/movement/ElytraBoost;
      // 09b: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 09e: ifne 0bd
      // 0a1: aload 0
      // 0a2: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 0a5: getfield dev/boze/client/systems/modules/misc/AutoArmor.awaitLanding Ldev/boze/client/settings/BooleanSetting;
      // 0a8: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 0ab: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0ae: ifeq 0c2
      // 0b1: getstatic dev/boze/client/utils/IMinecraft.mc Lnet/minecraft/client/MinecraftClient;
      // 0b4: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0b7: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isFallFlying ()Z
      // 0ba: ifeq 0c2
      // 0bd: ldc 690420
      // 0bf: goto 0c3
      // 0c2: bipush -1
      // 0c3: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 0c6: aload 0
      // 0c7: aload 4
      // 0c9: invokevirtual net/minecraft/item/ItemStack.getMaxDamage ()I
      // 0cc: aload 4
      // 0ce: invokevirtual net/minecraft/item/ItemStack.getDamage ()I
      // 0d1: isub
      // 0d2: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2882 I
      // 0d5: return
      // 0d6: aload 4
      // 0d8: aload 0
      // 0d9: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 0dc: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 0df: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.populateEnchantments (Lnet/minecraft/item/ItemStack;Lit/unimi/dsi/fastutil/objects/Object2IntMap;)V
      // 0e2: aload 0
      // 0e3: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 0e6: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 0e9: getstatic net/minecraft/enchantment/Enchantments.BINDING_CURSE Lnet/minecraft/registry/RegistryKey;
      // 0ec: invokeinterface it/unimi/dsi/fastutil/objects/Object2IntMap.containsKey (Ljava/lang/Object;)Z 2
      // 0f1: ifeq 0fb
      // 0f4: aload 0
      // 0f5: ldc 2147483647
      // 0f7: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 0fa: return
      // 0fb: aload 0
      // 0fc: aload 0
      // 0fd: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2883 Ldev/boze/client/systems/modules/misc/AutoArmor;
      // 100: aload 4
      // 102: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor.method1645 (Lnet/minecraft/item/ItemStack;)I
      // 105: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 108: aload 0
      // 109: aload 0
      // 10a: aload 0
      // 10b: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 10e: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor/nd.method1656 (I)I
      // 111: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 114: aload 0
      // 115: aload 0
      // 116: aload 0
      // 117: getfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 11a: aload 4
      // 11c: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor/nd.method1657 (ILnet/minecraft/item/ItemStack;)I
      // 11f: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2881 I
      // 122: aload 4
      // 124: invokevirtual net/minecraft/item/ItemStack.isEmpty ()Z
      // 127: ifne 139
      // 12a: aload 0
      // 12b: aload 4
      // 12d: invokevirtual net/minecraft/item/ItemStack.getMaxDamage ()I
      // 130: aload 4
      // 132: invokevirtual net/minecraft/item/ItemStack.getDamage ()I
      // 135: isub
      // 136: putfield dev/boze/client/systems/modules/misc/AutoArmor/nd.field2882 I
      // 139: return
   }

   public int method1653() {
      return this.field2883.preserve.method419() && this.field2882 <= 15 ? -1 : this.field2880;
   }

   public void method1654() {
      if (!this.field2883.method1647() && this.field2881 != Integer.MAX_VALUE) {
         ItemStack var4 = IMinecraft.mc.player.getInventory().getArmorStack(this.field2878);
         if (AutoMend.INSTANCE.isEnabled() && AutoMend.INSTANCE.mendRemove.method419() && !var4.isEmpty() && var4.isDamageable() && !var4.isDamaged()) {
            this.field2883.method1649(this.field2878);
         } else if (this.field2880 > this.field2881) {
            this.field2883.method1648(this.field2879, this.field2878);
         } else if (this.field2883.preserve.method419() && this.field2882 <= 15) {
            this.field2883.method1649(this.field2878);
         }
      }
   }

   public Slot method1655() {
      if (!this.field2883.method1647() && this.field2881 != Integer.MAX_VALUE) {
         return this.field2880 > this.field2881 && IMinecraft.mc.player.getInventory().getArmorStack(this.field2878).isEmpty()
            ? IMinecraft.mc.player.currentScreenHandler.getSlot(this.field2879 < 9 ? 36 + this.field2879 : this.field2879)
            : null;
      } else {
         return null;
      }
   }

   private int method1656(int var1) {
      if (this.field2883.avoidBinding.method419() && this.field2883.field2871.containsKey(Enchantments.BINDING_CURSE)) {
         var1 -= 2;
      }

      return var1;
   }

   private int method1657(int var1, ItemStack var2) {
      return this.field2883.preserve.method419() && var2.isDamageable() && var2.getMaxDamage() - var2.getDamage() <= 15 ? -1 : var1;
   }
}
