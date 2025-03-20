package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.ArmorEnchantMode;
import dev.boze.client.enums.AutoArmorElytra;
import dev.boze.client.enums.AutoArmorMode;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.events.FlipFrameEvent;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PrePlayerTickEvent;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.AntiCheat;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.misc.autoarmor.nd;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.player.InvUtils;
import dev.boze.client.utils.player.InventoryUtil;
import dev.boze.client.utils.player.SlotUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.Vec2f;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

public class AutoArmor extends Module {
   public static final AutoArmor INSTANCE = new AutoArmor();
   private final EnumSetting<AutoArmorMode> mode = new EnumSetting<AutoArmorMode>("Mode", AutoArmorMode.Anarchy, "Mode for auto armor", AutoArmor::lambda$new$0);
   private final EnumSetting<AutoArmorElytra> elytra = new EnumSetting<AutoArmorElytra>(
      "Elytra", AutoArmorElytra.ElytraFly, "Put Elytra on instead of chestplate"
   );
   private final BooleanSetting bindState = new BooleanSetting("BindState", false, "Bind state", AutoArmor::lambda$new$1);
   private final BooleanSetting awaitLanding = new BooleanSetting("AwaitLanding", false, "Await landing before swapping from Elytra", this::lambda$new$2);
   private final BindSetting swap = new BindSetting("Swap", Bind.create(), "Key to swap chest armor", this::lambda$new$3);
   private final IntSetting interval = new IntSetting("Interval", 0, 0, 5, 1, "Interval for equipping armor", this::lambda$new$4);
   private final BooleanSetting awaitInventory = new BooleanSetting("AwaitInventory", true, "Await open inventory before replenishing", this::lambda$new$5);
   private final MinMaxDoubleSetting delay = new MinMaxDoubleSetting(
      "Delay", new double[]{2.0, 5.0}, 0.0, 20.0, 0.01, "Delay between replenishing slots", this::lambda$new$6
   );
   private final MinMaxSetting randomDelay = new MinMaxSetting("RandomDelay", 0.25, 0.0, 1.0, 0.01, "Random replenishing delay", this::lambda$new$7);
   private final MinMaxSetting initialDelay = new MinMaxSetting(
      "InitialDelay", 2.0, 0.0, 20.0, 0.01, "Initial delay before replenishing slots", this::lambda$new$8
   );
   private final EnumSetting<ArmorEnchantMode> preferHead = new EnumSetting<ArmorEnchantMode>(
      "PreferHead", ArmorEnchantMode.Prot, "Protection preference for head"
   );
   private final EnumSetting<ArmorEnchantMode> preferChest = new EnumSetting<ArmorEnchantMode>(
      "PreferChest", ArmorEnchantMode.Prot, "Protection preference for chest"
   );
   private final EnumSetting<ArmorEnchantMode> preferLegs = new EnumSetting<ArmorEnchantMode>(
      "PreferLegs", ArmorEnchantMode.Blast, "Protection preference for legs"
   );
   private final EnumSetting<ArmorEnchantMode> preferFeet = new EnumSetting<ArmorEnchantMode>(
      "PreferFeet", ArmorEnchantMode.Prot, "Protection preference for feet"
   );
   private final BooleanSetting preserve = new BooleanSetting("Preserve", false, "Take off armor pieces if they're about to break", this::lambda$new$9);
   private final BooleanSetting avoidBinding = new BooleanSetting("AvoidBinding", true, "Avoid the binding curse");
   private final dev.boze.client.utils.Timer timer = new dev.boze.client.utils.Timer();
   private boolean field2867 = true;
   private Vec2f field2868 = null;
   private long field2869 = 0L;
   public boolean field2870 = false;
   private final Object2IntMap<RegistryEntry<Enchantment>> field2871 = new Object2IntOpenHashMap();
   private final nd[] field2872 = new nd[4];
   private final nd field2873 = new nd(this, 3);
   private final nd field2874 = new nd(this, 2);
   private final nd field2875 = new nd(this, 1);
   private final nd field2876 = new nd(this, 0);
   private int field2877 = 0;

   private AutoArmorMode method1637() {
      return Options.INSTANCE.method1971() ? AutoArmorMode.Ghost : this.mode.getValue();
   }

   private AutoArmorElytra method1638() {
      return this.elytra.getValue();
   }

   public AutoArmor() {
      super("AutoArmor", "Automatically equips armor", Category.Misc);
      this.field2872[0] = this.field2873;
      this.field2872[1] = this.field2874;
      this.field2872[2] = this.field2875;
      this.field2872[3] = this.field2876;
      this.field435 = true;
   }

   @Override
   public boolean setEnabled(boolean newState) {
      return super.setEnabled(newState);
   }

   @Override
   public void onEnable() {
      this.field2877 = 0;
      this.timer.reset();
      this.field2867 = true;
      this.field2868 = null;
      this.field2869 = 0L;
      this.field2870 = false;
   }

   @EventHandler
   private void method1639(KeyEvent var1) {
      if (this.method1638() == AutoArmorElytra.Bind
         && this.swap.getValue().matches(true, var1.key)
         && var1.action == KeyAction.Press
         && mc.currentScreen == null) {
         this.bindState.setValue(!this.bindState.getValue());
      }
   }

   @EventHandler
   private void method1640(MouseButtonEvent var1) {
      if (this.method1638() == AutoArmorElytra.Bind
         && this.swap.getValue().matches(false, var1.button)
         && var1.action == KeyAction.Press
         && mc.currentScreen == null) {
         this.bindState.setValue(!this.bindState.getValue());
      }
   }

   @EventHandler
   public void method1641(FlipFrameEvent event) {
      if (MinecraftUtils.isClientActive() && this.method1637() != AutoArmorMode.Anarchy) {
         if (!event.method1022()) {
            if (!(mc.currentScreen instanceof DownloadingTerrainScreen)) {
               if ((!(mc.currentScreen instanceof AbstractInventoryScreen) || !(mc.currentScreen instanceof HandledScreen)) && this.awaitInventory.getValue()) {
                  this.timer.reset();
                  this.field2867 = true;
                  this.field2868 = null;
               } else {
                  this.field2870 = false;
                  PlayerScreenHandler var5 = mc.player != null ? mc.player.playerScreenHandler : null;
                  if (var5 == null || var5.getCursorStack().isEmpty()) {
                     if (this.field2868 == null) {
                        this.field2868 = new Vec2f((float)mc.getWindow().getScaledWidth() / 2.0F, (float)mc.getWindow().getScaledHeight() / 2.0F);
                     }

                     Slot var6 = null;
                     if (this.timer
                        .hasElapsed(this.field2867 && !this.awaitInventory.getValue() ? this.initialDelay.getValue() * 50.0 : (double)this.field2869)) {
                        this.field2867 = false;
                        this.timer.reset();

                        for (nd var10 : this.field2872) {
                           var10.method1650();
                        }

                        for (int var11 = 0; var11 < mc.player.getInventory().main.size(); var11++) {
                           ItemStack var15 = mc.player.getInventory().getStack(var11);
                           if (!var15.isEmpty()
                              && !(var15.getItem() instanceof AnimalArmorItem)
                              && (var15.getItem() instanceof ArmorItem || var15.getItem() instanceof ElytraItem && ElytraItem.isUsable(var15))
                              && (!this.preserve.getValue() || !var15.isDamageable() || var15.getMaxDamage() - var15.getDamage() > 15)) {
                              ItemEnchantmentUtils.populateEnchantments(var15, this.field2871);
                              if (!this.method1643()) {
                                 switch (this.method1644(var15)) {
                                    case 0:
                                       this.field2876.method1651(var15, var11);
                                       break;
                                    case 1:
                                       this.field2875.method1651(var15, var11);
                                       break;
                                    case 2:
                                       this.field2874.method1651(var15, var11);
                                       break;
                                    case 3:
                                       this.field2873.method1651(var15, var11);
                                 }
                              }
                           }
                        }

                        for (nd var22 : this.field2872) {
                           var22.method1652();
                        }

                        Arrays.sort(this.field2872, Comparator.comparingInt(nd::method1653));

                        for (nd var23 : this.field2872) {
                           var6 = var23.method1655();
                           if (var6 != null) {
                              break;
                           }
                        }

                        if (var6 != null) {
                           Vec2f var14 = SlotUtils.method663(null, var6)
                              .add(
                                 new Vec2f(
                                    this.randomDelay.getValue() == 0.0
                                       ? 0.0F
                                       : (float)ThreadLocalRandom.current().nextDouble(-this.randomDelay.getValue() * 30.0, this.randomDelay.getValue() * 30.0),
                                    this.randomDelay.getValue() == 0.0
                                       ? 0.0F
                                       : (float)ThreadLocalRandom.current().nextDouble(-this.randomDelay.getValue() * 30.0, this.randomDelay.getValue() * 30.0)
                                 )
                              );
                           float var18 = this.field2868.distanceSquared(var14);
                           this.field2868 = var14;
                           float var21 = (float)Math.sqrt((double)var18) / new Vec2f(176.0F, 166.0F).length();
                           this.field2869 = (long)(this.delay.method1296() * (double)var21);
                           mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, var6.id, 0, SlotActionType.QUICK_MOVE, mc.player);
                           event.method1021(true);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1642(PrePlayerTickEvent event) {
      if (this.method1637() == AutoArmorMode.Anarchy) {
         if (this.field2877 > 0) {
            this.field2877--;
         } else if (mc.player.playerScreenHandler == mc.player.currentScreenHandler) {
            for (nd var8 : this.field2872) {
               var8.method1650();
            }

            for (int var9 = 0; var9 < mc.player.getInventory().main.size(); var9++) {
               ItemStack var13 = mc.player.getInventory().getStack(var9);
               if (!var13.isEmpty()
                  && !(var13.getItem() instanceof AnimalArmorItem)
                  && (var13.getItem() instanceof ArmorItem || var13.getItem() instanceof ElytraItem && ElytraItem.isUsable(var13))
                  && (!this.preserve.getValue() || !var13.isDamageable() || var13.getMaxDamage() - var13.getDamage() > 15)) {
                  ItemEnchantmentUtils.populateEnchantments(var13, this.field2871);
                  if (!this.method1643()) {
                     switch (this.method1644(var13)) {
                        case 0:
                           this.field2876.method1651(var13, var9);
                           break;
                        case 1:
                           this.field2875.method1651(var13, var9);
                           break;
                        case 2:
                           this.field2874.method1651(var13, var9);
                           break;
                        case 3:
                           this.field2873.method1651(var13, var9);
                     }
                  }
               }
            }

            if (XCarry.INSTANCE.isEnabled()) {
               for (int var10 = 1; var10 <= 4; var10++) {
                  Slot var14 = mc.player.playerScreenHandler.getSlot(var10);
                  ItemStack var17 = var14.getStack();
                  if (!var17.isEmpty()
                     && !(var17.getItem() instanceof AnimalArmorItem)
                     && (var17.getItem() instanceof ArmorItem || var17.getItem() instanceof ElytraItem && ElytraItem.isUsable(var17))
                     && (!this.preserve.getValue() || !var17.isDamageable() || var17.getMaxDamage() - var17.getDamage() > 15)) {
                     ItemEnchantmentUtils.populateEnchantments(var17, this.field2871);
                     if (!this.method1643()) {
                        switch (this.method1644(var17)) {
                           case 0:
                              this.field2876.method1651(var17, 420 + var10);
                              break;
                           case 1:
                              this.field2875.method1651(var17, 420 + var10);
                              break;
                           case 2:
                              this.field2874.method1651(var17, 420 + var10);
                              break;
                           case 3:
                              this.field2873.method1651(var17, 420 + var10);
                        }
                     }
                  }
               }
            }

            for (nd var20 : this.field2872) {
               var20.method1652();
            }

            Arrays.sort(this.field2872, Comparator.comparingInt(nd::method1653));

            for (nd var21 : this.field2872) {
               var21.method1654();
            }
         }
      }
   }

   private boolean method1643() {
      return this.avoidBinding.getValue() && this.field2871.containsKey(Enchantments.BINDING_CURSE);
   }

   private int method1644(ItemStack var1) {
      if (var1.getItem() instanceof ElytraItem && ElytraItem.isUsable(var1)) {
         return 2;
      } else {
         return !(var1.getItem() instanceof AnimalArmorItem) && var1.getItem() instanceof ArmorItem
            ? ((ArmorItem)var1.getItem()).getSlotType().getEntitySlotId()
            : -1;
      }
   }

   private int method1645(ItemStack param1) {
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
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 1
      // 001: invokevirtual net/minecraft/item/ItemStack.isEmpty ()Z
      // 004: ifeq 009
      // 007: bipush 0
      // 008: ireturn
      // 009: getstatic dev/boze/client/systems/modules/combat/AutoMend.INSTANCE Ldev/boze/client/systems/modules/combat/AutoMend;
      // 00c: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 00f: ifeq 031
      // 012: getstatic dev/boze/client/systems/modules/combat/AutoMend.INSTANCE Ldev/boze/client/systems/modules/combat/AutoMend;
      // 015: getfield dev/boze/client/systems/modules/combat/AutoMend.mendRemove Ldev/boze/client/settings/BooleanSetting;
      // 018: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 01b: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 01e: ifeq 031
      // 021: aload 1
      // 022: invokevirtual net/minecraft/item/ItemStack.isDamageable ()Z
      // 025: ifeq 031
      // 028: aload 1
      // 029: invokevirtual net/minecraft/item/ItemStack.isDamaged ()Z
      // 02c: ifne 031
      // 02f: bipush -1
      // 030: ireturn
      // 031: bipush 0
      // 032: istore 5
      // 034: aload 1
      // 035: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 038: getstatic net/minecraft/item/Items.ELYTRA Lnet/minecraft/item/Item;
      // 03b: if_acmpne 0ca
      // 03e: aload 1
      // 03f: invokestatic net/minecraft/item/ElytraItem.isUsable (Lnet/minecraft/item/ItemStack;)Z
      // 042: ifne 047
      // 045: bipush -1
      // 046: ireturn
      // 047: aload 0
      // 048: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor.method1638 ()Ldev/boze/client/enums/AutoArmorElytra;
      // 04b: invokevirtual dev/boze/client/enums/AutoArmorElytra.ordinal ()I
      // 04e: tableswitch 121 0 3 30 36 69 61
      // 06c: bipush -1
      // 06d: istore 5
      // 06f: goto 0c7
      // 072: aload 0
      // 073: getfield dev/boze/client/systems/modules/misc/AutoArmor.bindState Ldev/boze/client/settings/BooleanSetting;
      // 076: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 079: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 07c: ifeq 085
      // 07f: ldc_w 690420
      // 082: goto 086
      // 085: bipush -1
      // 086: istore 5
      // 088: goto 0c7
      // 08b: ldc_w 690420
      // 08e: istore 5
      // 090: goto 0c7
      // 093: getstatic dev/boze/client/systems/modules/movement/ElytraFly.INSTANCE Ldev/boze/client/systems/modules/movement/ElytraFly;
      // 096: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 099: ifne 0be
      // 09c: getstatic dev/boze/client/systems/modules/movement/ElytraBoost.INSTANCE Ldev/boze/client/systems/modules/movement/ElytraBoost;
      // 09f: invokevirtual dev/boze/client/systems/modules/Module.isEnabled ()Z
      // 0a2: ifne 0be
      // 0a5: aload 0
      // 0a6: getfield dev/boze/client/systems/modules/misc/AutoArmor.awaitLanding Ldev/boze/client/settings/BooleanSetting;
      // 0a9: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
      // 0ac: invokevirtual java/lang/Boolean.booleanValue ()Z
      // 0af: ifeq 0c4
      // 0b2: getstatic dev/boze/client/systems/modules/misc/AutoArmor.mc Lnet/minecraft/client/MinecraftClient;
      // 0b5: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 0b8: invokevirtual net/minecraft/client/network/ClientPlayerEntity.isFallFlying ()Z
      // 0bb: ifeq 0c4
      // 0be: ldc_w 690420
      // 0c1: goto 0c5
      // 0c4: bipush -1
      // 0c5: istore 5
      // 0c7: iload 5
      // 0c9: ireturn
      // 0ca: aload 0
      // 0cb: aload 1
      // 0cc: invokevirtual dev/boze/client/systems/modules/misc/AutoArmor.method1646 (Lnet/minecraft/item/ItemStack;)Lnet/minecraft/registry/RegistryKey;
      // 0cf: astore 6
      // 0d1: iload 5
      // 0d3: bipush 3
      // 0d4: aload 0
      // 0d5: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 0d8: aload 6
      // 0da: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 0dd: imul
      // 0de: iadd
      // 0df: istore 5
      // 0e1: iload 5
      // 0e3: aload 0
      // 0e4: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 0e7: getstatic net/minecraft/enchantment/Enchantments.PROTECTION Lnet/minecraft/registry/RegistryKey;
      // 0ea: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 0ed: iadd
      // 0ee: istore 5
      // 0f0: iload 5
      // 0f2: aload 0
      // 0f3: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 0f6: getstatic net/minecraft/enchantment/Enchantments.BLAST_PROTECTION Lnet/minecraft/registry/RegistryKey;
      // 0f9: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 0fc: iadd
      // 0fd: istore 5
      // 0ff: iload 5
      // 101: aload 0
      // 102: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 105: getstatic net/minecraft/enchantment/Enchantments.FIRE_PROTECTION Lnet/minecraft/registry/RegistryKey;
      // 108: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 10b: iadd
      // 10c: istore 5
      // 10e: iload 5
      // 110: aload 0
      // 111: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 114: getstatic net/minecraft/enchantment/Enchantments.PROJECTILE_PROTECTION Lnet/minecraft/registry/RegistryKey;
      // 117: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 11a: iadd
      // 11b: istore 5
      // 11d: iload 5
      // 11f: aload 0
      // 120: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 123: getstatic net/minecraft/enchantment/Enchantments.UNBREAKING Lnet/minecraft/registry/RegistryKey;
      // 126: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 129: iadd
      // 12a: istore 5
      // 12c: iload 5
      // 12e: bipush 2
      // 12f: aload 0
      // 130: getfield dev/boze/client/systems/modules/misc/AutoArmor.field2871 Lit/unimi/dsi/fastutil/objects/Object2IntMap;
      // 133: getstatic net/minecraft/enchantment/Enchantments.MENDING Lnet/minecraft/registry/RegistryKey;
      // 136: invokestatic dev/boze/client/utils/ItemEnchantmentUtils.getEnchantmentLevel (Lit/unimi/dsi/fastutil/objects/Object2IntMap;Lnet/minecraft/registry/RegistryKey;)I
      // 139: imul
      // 13a: iadd
      // 13b: istore 5
      // 13d: iload 5
      // 13f: aload 1
      // 140: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 143: instanceof net/minecraft/item/ArmorItem
      // 146: ifeq 156
      // 149: aload 1
      // 14a: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 14d: checkcast net/minecraft/item/ArmorItem
      // 150: invokevirtual net/minecraft/item/ArmorItem.getProtection ()I
      // 153: goto 157
      // 156: bipush 0
      // 157: iadd
      // 158: istore 5
      // 15a: iload 5
      // 15c: aload 1
      // 15d: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 160: instanceof net/minecraft/item/ArmorItem
      // 163: ifeq 174
      // 166: aload 1
      // 167: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 16a: checkcast net/minecraft/item/ArmorItem
      // 16d: invokevirtual net/minecraft/item/ArmorItem.getToughness ()F
      // 170: f2i
      // 171: goto 175
      // 174: bipush 0
      // 175: iadd
      // 176: istore 5
      // 178: iload 5
      // 17a: ireturn
   }

   private RegistryKey<Enchantment> method1646(ItemStack var1) {
      return switch (this.method1644(var1)) {
         case 0 -> this.preferFeet.getValue().field1809;
         case 1 -> this.preferLegs.getValue().field1809;
         case 2 -> this.preferChest.getValue().field1809;
         case 3 -> this.preferHead.getValue().field1809;
         default -> ArmorEnchantMode.Prot.field1809;
      };
   }

   private boolean method1647() {
      return this.field2877 > 0;
   }

   private void method1648(int var1, int var2) {
      if (var1 >= 420) {
         InvUtils.method2201().method2206(var1 - 420).method2218(var2);
      } else {
         InvUtils.method2201().method2207(var1).method2218(var2);
      }

      this.field2877 = this.interval.getValue();
      if (AntiCheat.INSTANCE.field2322.getValue() && !InventoryUtil.isInventoryOpen()) {
         mc.player.networkHandler.sendPacket(new CloseHandledScreenC2SPacket(0));
      }
   }

   private void method1649(int var1) {
      for (int var5 = 0; var5 < mc.player.getInventory().main.size(); var5++) {
         if (mc.player.getInventory().getStack(var5).isEmpty()) {
            InvUtils.method2201().method2211(var1).method2213(var5);
            this.field2877 = this.interval.getValue();
            break;
         }
      }
   }

   private boolean lambda$new$9() {
      return this.method1637() == AutoArmorMode.Anarchy;
   }

   private boolean lambda$new$8() {
      return this.method1637() == AutoArmorMode.Ghost;
   }

   private boolean lambda$new$7() {
      return this.method1637() == AutoArmorMode.Ghost;
   }

   private boolean lambda$new$6() {
      return this.method1637() == AutoArmorMode.Ghost;
   }

   private boolean lambda$new$5() {
      return this.method1637() == AutoArmorMode.Ghost;
   }

   private boolean lambda$new$4() {
      return this.method1637() == AutoArmorMode.Anarchy;
   }

   private boolean lambda$new$3() {
      return this.elytra.getValue() == AutoArmorElytra.Bind;
   }

   private boolean lambda$new$2() {
      return this.elytra.getValue() == AutoArmorElytra.ElytraFly;
   }

   private static boolean lambda$new$1() {
      return false;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
