package dev.boze.client.systems.modules.misc;

import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.MiddleClick;
import dev.boze.client.enums.MiddleClickMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PostPlayerTickEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.InventoryHelper;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.MinecraftUtils;
import mapped.Class5913;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult.Type;

public class MiddleClickAction extends Module {
   public static final MiddleClickAction INSTANCE = new MiddleClickAction();
   private final EnumSetting<MiddleClickMode> field490 = new EnumSetting<MiddleClickMode>(
      "Mode", MiddleClickMode.Anarchy, "Mode for middle click action", MiddleClickAction::lambda$new$0
   );
   private IntSetting field491 = new IntSetting("XpDelay", 0, 0, 5, 1, "Delay for throwing xp when middle clicking", this::method1972);
   public BooleanSetting field492 = new BooleanSetting("NoMCPick", true, "Cancel vanilla block middle click pick", this::method1972);
   private EnumSetting<MiddleClick> field493 = new EnumSetting<MiddleClick>("Air", MiddleClick.XP, "What to do when facing the air", this::method1972);
   private EnumSetting<MiddleClick> field494 = new EnumSetting<MiddleClick>("Entity", MiddleClick.Friend, "What to do when facing an entity", this::method1972);
   private EnumSetting<MiddleClick> field495 = new EnumSetting<MiddleClick>("Block", MiddleClick.XP, "What to do when facing a block", this::method1972);
   private EnumSetting<MiddleClick> field496 = new EnumSetting<MiddleClick>("Flying", MiddleClick.Rocket, "What to do when elytra flying", this::method1972);
   private EnumSetting<SwapMode> field497 = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Swap mode for item actions", this::lambda$new$1);
   private dev.boze.client.utils.Timer field498 = new dev.boze.client.utils.Timer();

   private boolean method1971() {
      return Options.INSTANCE.method1971() || this.field490.getValue() == MiddleClickMode.Ghost;
   }

   private boolean method1972() {
      return !this.method1971();
   }

   public MiddleClickAction() {
      super("MCA", "Middle Click Action\nIn Ghost mode, this is just middle click friend\n", Category.Misc);
      this.field435 = true;
   }

   @EventHandler
   public void method1812(MouseButtonEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.action == KeyAction.Press && !(mc.currentScreen instanceof ClickGUI)) {
            if (event.button == 2) {
               if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
                  this.method267(this.method1971() ? MiddleClick.Friend : this.field494.getValue());
               } else if (this.method1972()) {
                  if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
                     this.method267(this.field495.getValue());
                  } else if (mc.player.isFallFlying()) {
                     this.method267(this.field496.getValue());
                  } else {
                     this.method267(this.field493.getValue());
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1942(PostPlayerTickEvent event) {
      if (!(mc.currentScreen instanceof ClickGUI) && !this.method1971() && MinecraftUtils.isClientActive()) {
         if (mc.options.pickItemKey.isPressed()) {
            if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.ENTITY && mc.targetedEntity instanceof PlayerEntity) {
               this.method266(this.field494.getValue());
            } else if (mc.crosshairTarget != null && mc.crosshairTarget.getType() == Type.BLOCK) {
               this.method266(this.field495.getValue());
            } else if (mc.player.isFallFlying()) {
               this.method266(this.field496.getValue());
            } else {
               this.method266(this.field493.getValue());
            }
         }
      }
   }

   private void method266(MiddleClick var1) {
      if (var1 == MiddleClick.XP) {
         if (!this.field498.hasElapsed((double)(50 * this.field491.getValue()))) {
            return;
         }

         int var5 = InventoryHelper.method166(MiddleClickAction::lambda$doActionIfXp$2, this.field497.getValue());
         if (var5 != -1) {
            InventoryUtil.method534(this, 350, this.field497.getValue(), var5);
            Class5913.method16(Hand.MAIN_HAND);
            this.field498.reset();
            InventoryUtil.method396(this);
         }
      }
   }

   private void method267(MiddleClick param1) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 000: aload 1
      // 001: invokevirtual dev/boze/client/enums/MiddleClick.ordinal ()I
      // 004: tableswitch 439 1 4 211 119 32 355
      // 024: invokedynamic test ()Ljava/util/function/Predicate; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Z, dev/boze/client/systems/modules/misc/MiddleClickAction.lambda$doAction$3 (Lnet/minecraft/item/ItemStack;)Z, (Lnet/minecraft/item/ItemStack;)Z ]
      // 029: aload 0
      // 02a: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 02d: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 030: checkcast dev/boze/client/enums/SwapMode
      // 033: invokestatic dev/boze/client/utils/InventoryHelper.method166 (Ljava/util/function/Predicate;Ldev/boze/client/enums/SwapMode;)I
      // 036: istore 5
      // 038: iload 5
      // 03a: bipush -1
      // 03b: if_icmpeq 078
      // 03e: aload 0
      // 03f: sipush 350
      // 042: aload 0
      // 043: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 046: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 049: checkcast dev/boze/client/enums/SwapMode
      // 04c: iload 5
      // 04e: invokestatic dev/boze/client/utils/InventoryUtil.method534 (Ldev/boze/client/systems/modules/Module;ILdev/boze/client/enums/SwapMode;I)Z
      // 051: pop
      // 052: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 055: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 058: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getItemCooldownManager ()Lnet/minecraft/entity/player/ItemCooldownManager;
      // 05b: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 05e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 061: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getMainHandStack ()Lnet/minecraft/item/ItemStack;
      // 064: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 067: invokevirtual net/minecraft/entity/player/ItemCooldownManager.isCoolingDown (Lnet/minecraft/item/Item;)Z
      // 06a: ifne 074
      // 06d: getstatic net/minecraft/util/Hand.MAIN_HAND Lnet/minecraft/util/Hand;
      // 070: invokestatic mapped/Class5913.method16 (Lnet/minecraft/util/Hand;)Lnet/minecraft/network/packet/c2s/play/PlayerInteractItemC2SPacket;
      // 073: pop
      // 074: aload 0
      // 075: invokestatic dev/boze/client/utils/InventoryUtil.method396 (Ldev/boze/client/systems/modules/Module;)V
      // 078: goto 1bb
      // 07b: aload 0
      // 07c: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field498 Ldev/boze/client/utils/Timer;
      // 07f: bipush 50
      // 081: aload 0
      // 082: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field491 Ldev/boze/client/settings/IntSetting;
      // 085: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
      // 088: invokevirtual java/lang/Integer.intValue ()I
      // 08b: imul
      // 08c: i2d
      // 08d: invokevirtual dev/boze/client/utils/Timer.hasElapsed (D)Z
      // 090: ifne 094
      // 093: return
      // 094: invokedynamic test ()Ljava/util/function/Predicate; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Z, dev/boze/client/systems/modules/misc/MiddleClickAction.lambda$doAction$4 (Lnet/minecraft/item/ItemStack;)Z, (Lnet/minecraft/item/ItemStack;)Z ]
      // 099: aload 0
      // 09a: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 09d: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0a0: checkcast dev/boze/client/enums/SwapMode
      // 0a3: invokestatic dev/boze/client/utils/InventoryHelper.method166 (Ljava/util/function/Predicate;Ldev/boze/client/enums/SwapMode;)I
      // 0a6: istore 5
      // 0a8: iload 5
      // 0aa: bipush -1
      // 0ab: if_icmpeq 0d4
      // 0ae: aload 0
      // 0af: sipush 350
      // 0b2: aload 0
      // 0b3: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 0b6: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 0b9: checkcast dev/boze/client/enums/SwapMode
      // 0bc: iload 5
      // 0be: invokestatic dev/boze/client/utils/InventoryUtil.method534 (Ldev/boze/client/systems/modules/Module;ILdev/boze/client/enums/SwapMode;I)Z
      // 0c1: pop
      // 0c2: getstatic net/minecraft/util/Hand.MAIN_HAND Lnet/minecraft/util/Hand;
      // 0c5: invokestatic mapped/Class5913.method16 (Lnet/minecraft/util/Hand;)Lnet/minecraft/network/packet/c2s/play/PlayerInteractItemC2SPacket;
      // 0c8: pop
      // 0c9: aload 0
      // 0ca: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field498 Ldev/boze/client/utils/Timer;
      // 0cd: invokevirtual dev/boze/client/utils/Timer.reset ()V
      // 0d0: aload 0
      // 0d1: invokestatic dev/boze/client/utils/InventoryUtil.method396 (Ldev/boze/client/systems/modules/Module;)V
      // 0d4: goto 1bb
      // 0d7: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 0da: getfield net/minecraft/client/MinecraftClient.targetedEntity Lnet/minecraft/entity/Entity;
      // 0dd: ifnull 1bb
      // 0e0: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 0e3: getfield net/minecraft/client/MinecraftClient.targetedEntity Lnet/minecraft/entity/Entity;
      // 0e6: astore 5
      // 0e8: aload 5
      // 0ea: instanceof net/minecraft/entity/player/PlayerEntity
      // 0ed: ifeq 164
      // 0f0: aload 5
      // 0f2: invokestatic dev/boze/client/systems/modules/client/Friends.method2055 (Lnet/minecraft/entity/Entity;)Z
      // 0f5: ifeq 12f
      // 0f8: aload 5
      // 0fa: invokevirtual net/minecraft/entity/Entity.getName ()Lnet/minecraft/text/Text;
      // 0fd: invokeinterface net/minecraft/text/Text.getString ()Ljava/lang/String; 1
      // 102: invokestatic dev/boze/client/systems/modules/client/Friends.method1750 (Ljava/lang/String;)V
      // 105: new dev/boze/client/gui/notification/Notification
      // 108: dup
      // 109: aload 0
      // 10a: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 10d: aload 5
      // 10f: invokevirtual net/minecraft/entity/Entity.getName ()Lnet/minecraft/text/Text;
      // 112: invokeinterface net/minecraft/text/Text.getString ()Ljava/lang/String; 1
      // 117: ldc_w " Unfriended "
      // 11a: swap
      // 11b: invokedynamic makeConcatWithConstants (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; bsm=java/lang/invoke/StringConcatFactory.makeConcatWithConstants (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ "\u0001\u0001" ]
      // 120: getstatic dev/boze/client/gui/notification/Notifications.PLAYERS_REMOVE Ldev/boze/client/gui/notification/Notifications;
      // 123: getstatic dev/boze/client/gui/notification/NotificationPriority.Normal Ldev/boze/client/gui/notification/NotificationPriority;
      // 126: invokespecial dev/boze/client/gui/notification/Notification.<init> (Ljava/lang/String;Ljava/lang/String;Ldev/boze/client/gui/notification/Notifications;Ldev/boze/client/gui/notification/NotificationPriority;)V
      // 129: invokestatic dev/boze/client/manager/NotificationManager.method1151 (Ldev/boze/client/gui/notification/INotification;)V
      // 12c: goto 164
      // 12f: aload 5
      // 131: invokevirtual net/minecraft/entity/Entity.getName ()Lnet/minecraft/text/Text;
      // 134: invokeinterface net/minecraft/text/Text.getString ()Ljava/lang/String; 1
      // 139: invokestatic dev/boze/client/systems/modules/client/Friends.addFriend (Ljava/lang/String;)Z
      // 13c: pop
      // 13d: new dev/boze/client/gui/notification/Notification
      // 140: dup
      // 141: aload 0
      // 142: invokevirtual dev/boze/client/systems/modules/Module.getName ()Ljava/lang/String;
      // 145: aload 5
      // 147: invokevirtual net/minecraft/entity/Entity.getName ()Lnet/minecraft/text/Text;
      // 14a: invokeinterface net/minecraft/text/Text.getString ()Ljava/lang/String; 1
      // 14f: ldc_w " Friended "
      // 152: swap
      // 153: invokedynamic makeConcatWithConstants (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; bsm=java/lang/invoke/StringConcatFactory.makeConcatWithConstants (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ "\u0001\u0001" ]
      // 158: getstatic dev/boze/client/gui/notification/Notifications.PLAYERS_ADD Ldev/boze/client/gui/notification/Notifications;
      // 15b: getstatic dev/boze/client/gui/notification/NotificationPriority.Normal Ldev/boze/client/gui/notification/NotificationPriority;
      // 15e: invokespecial dev/boze/client/gui/notification/Notification.<init> (Ljava/lang/String;Ljava/lang/String;Ldev/boze/client/gui/notification/Notifications;Ldev/boze/client/gui/notification/NotificationPriority;)V
      // 161: invokestatic dev/boze/client/manager/NotificationManager.method1151 (Ldev/boze/client/gui/notification/INotification;)V
      // 164: goto 1bb
      // 167: invokedynamic test ()Ljava/util/function/Predicate; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ (Ljava/lang/Object;)Z, dev/boze/client/systems/modules/misc/MiddleClickAction.lambda$doAction$5 (Lnet/minecraft/item/ItemStack;)Z, (Lnet/minecraft/item/ItemStack;)Z ]
      // 16c: aload 0
      // 16d: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 170: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 173: checkcast dev/boze/client/enums/SwapMode
      // 176: invokestatic dev/boze/client/utils/InventoryHelper.method166 (Ljava/util/function/Predicate;Ldev/boze/client/enums/SwapMode;)I
      // 179: istore 5
      // 17b: iload 5
      // 17d: bipush -1
      // 17e: if_icmpeq 1bb
      // 181: aload 0
      // 182: sipush 350
      // 185: aload 0
      // 186: getfield dev/boze/client/systems/modules/misc/MiddleClickAction.field497 Ldev/boze/client/settings/EnumSetting;
      // 189: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
      // 18c: checkcast dev/boze/client/enums/SwapMode
      // 18f: iload 5
      // 191: invokestatic dev/boze/client/utils/InventoryUtil.method534 (Ldev/boze/client/systems/modules/Module;ILdev/boze/client/enums/SwapMode;I)Z
      // 194: pop
      // 195: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 198: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 19b: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getItemCooldownManager ()Lnet/minecraft/entity/player/ItemCooldownManager;
      // 19e: getstatic dev/boze/client/systems/modules/misc/MiddleClickAction.mc Lnet/minecraft/client/MinecraftClient;
      // 1a1: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
      // 1a4: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getMainHandStack ()Lnet/minecraft/item/ItemStack;
      // 1a7: invokevirtual net/minecraft/item/ItemStack.getItem ()Lnet/minecraft/item/Item;
      // 1aa: invokevirtual net/minecraft/entity/player/ItemCooldownManager.isCoolingDown (Lnet/minecraft/item/Item;)Z
      // 1ad: ifne 1b7
      // 1b0: getstatic net/minecraft/util/Hand.MAIN_HAND Lnet/minecraft/util/Hand;
      // 1b3: invokestatic mapped/Class5913.method16 (Lnet/minecraft/util/Hand;)Lnet/minecraft/network/packet/c2s/play/PlayerInteractItemC2SPacket;
      // 1b6: pop
      // 1b7: aload 0
      // 1b8: invokestatic dev/boze/client/utils/InventoryUtil.method396 (Ldev/boze/client/systems/modules/Module;)V
      // 1bb: return
   }

   private static boolean lambda$doAction$5(ItemStack var0) {
      return var0.getItem() == Items.FIREWORK_ROCKET;
   }

   private static boolean lambda$doAction$4(ItemStack var0) {
      return var0.getItem() == Items.EXPERIENCE_BOTTLE;
   }

   private static boolean lambda$doAction$3(ItemStack var0) {
      return var0.getItem() == Items.ENDER_PEARL;
   }

   private static boolean lambda$doActionIfXp$2(ItemStack var0) {
      return var0.getItem() == Items.EXPERIENCE_BOTTLE;
   }

   private boolean lambda$new$1() {
      return this.method1972()
         && (
            this.field493.getValue().field1797
               || this.field494.getValue().field1797
               || this.field495.getValue().field1797
               || this.field496.getValue().field1797
         );
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
