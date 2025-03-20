package dev.boze.client.systems.modules.combat;

import dev.boze.client.Boze;
import dev.boze.client.core.BozeLogger;
import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.EntityStatusS2CPacketAccessor;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.movement.NoSlow;
import dev.boze.client.systems.modules.movement.PacketFly;
import dev.boze.client.utils.*;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import mapped.Class1204;
import mapped.Class3069;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class OffHand extends Module {
   public static OffHand INSTANCE = new OffHand();
   private final BooleanSetting notify = new BooleanSetting("Notify", true, "Notify you when you pop");
   private final EnumSetting<OffhandMode> offhandMode = new EnumSetting<OffhandMode>(
      "Mode", OffhandMode.Anarchy, "Mode for Offhand\n - Anarchy: For anarchy servers\n - Ghost: For legit servers\n", OffHand::lambda$new$0
   );
   private final EnumSetting<InventoryMode> inventoryMode = new EnumSetting<InventoryMode>(
      "Inventory",
      InventoryMode.Await,
      "How to handle inventory\n - Ignore: Don't check if inventory open\n - Await: Wait for inventory to be open\n - Auto: Automatically open and close inventory\nIgnore is not recommended, it's somewhat blatant\n",
      this::method1972
   );
   private final BooleanSetting keepMovement = new BooleanSetting(
      "KeepMovement", false, "Keep movement after swapping\nSlightly increases chances of getting detected\n", this::lambda$new$1
   );
   private final MinMaxDoubleSetting openDelay = new MinMaxDoubleSetting(
      "OpenDelay", new double[]{0.1, 0.3}, 0.0, 1.0, 0.01, "Delay in seconds before opening inventory", this::lambda$new$2
   );
   private final MinMaxDoubleSetting swapDelay = new MinMaxDoubleSetting(
      "SwapDelay", new double[]{0.2, 0.4}, 0.0, 1.0, 0.01, "Delay in seconds before swapping offhand item", this::lambda$new$3
   );
   private final MinMaxDoubleSetting closeDelay = new MinMaxDoubleSetting(
      "CloseDelay", new double[]{0.15, 0.3}, 0.0, 1.0, 0.01, "Delay in seconds before closing inventory", this::lambda$new$4
   );
   private final BooleanSetting inInventory = new BooleanSetting("InInventory", true, "Swap while in inventory", this::method1971);
   final EnumSetting<ReactMode> reactMode = new EnumSetting<ReactMode>(
      "React",
      ReactMode.Tick,
      "When to react to pops\n - Tick: React to pops on the next tick\n - Packet: Reacts to pops instantly, as soon as they're received from the server",
      this::method1971
   );
   private final SettingCategory cancel = new SettingCategory("Cancel", "What to cancel when swapping\nThis is not necessary on most servers", this::method1971);
   private final BooleanSetting movement = new BooleanSetting("Movement", false, "Cancel movement when swapping", this.cancel);
   private final BooleanSetting eating = new BooleanSetting("Eating", false, "Cancel eating when swapping", this.cancel);
   private final BooleanSetting attacks = new BooleanSetting("Attacks", false, "Cancel attacks when swapping", this.cancel);
   private final FloatSetting health = new FloatSetting("Health", 12.0F, 1.0F, 20.0F, 0.5F, "Health at which to put a totem into your offhand");
   private final EnumSetting<SafetyMode> safety = new EnumSetting<SafetyMode>(
      "Safety",
      SafetyMode.Lethal,
      "Additional safety check for crystals in range\n - None: No additional safety check\n - Lethal: Only put a totem in your offhand if you would die\n - Health: Only put a totem in your offhand if you would go below the health specified",
      this.health
   );
   private final EnumSetting<OffhandItem> offhandItem = new EnumSetting<OffhandItem>("Item", OffhandItem.Totem, "Offhand Item");
   private final BooleanSetting autoCrystal = new BooleanSetting("AutoCrystal", true, "Auto Crystal Integration", this::lambda$new$5);
   private final BooleanSetting antiWeakness = new BooleanSetting("AntiWeakness", false, "AntiWeakness Integration", this::lambda$new$6);
   private final BooleanSetting swordGap = new BooleanSetting("SwordGap", true, "Put Golden Apple in Offhand while swording", this::lambda$new$7);
   private final BooleanSetting totemGap = new BooleanSetting(
      "TotemGap", false, "Put Golden Apple in Offhand when you right click while holding Totem\nUseful for dual-totem 2b2t meta\n", this::lambda$new$8
   );
   private final EnumSetting<GoldenAppleMode> goldenApple = new EnumSetting<GoldenAppleMode>(
      "GApple", GoldenAppleMode.Enc, "Which type of golden apple to use", this::lambda$new$9
   );
   private final BooleanSetting allowInteract = new BooleanSetting("AllowInteract", true, "Don't gap when looking at chests/etc", this::lambda$new$10);
   private final BindSetting totem = new BindSetting("Totem", Bind.create(), "Bind to put totem into your Offhand", this::lambda$new$11);
   private final BindSetting gapple = new BindSetting("GApple", Bind.create(), "Bind to put golden apple into your Offhand", this::lambda$new$12);
   private final BindSetting egapple = new BindSetting("EncGApple", Bind.create(), "Bind to put enchanted golden apple into your Offhand", this::lambda$new$13);
   private final BindSetting crystal = new BindSetting("Crystal", Bind.create(), "Bind to put crystal into your Offhand", this::lambda$new$14);
   private final BindSetting potion = new BindSetting("Potion", Bind.create(), "Bind to put potion into your Offhand");
   private final BooleanSetting force = new BooleanSetting("Force", true, "Always put an item into your Offhand, even when not needed");
   private final EnumSetting<IgnoreHotbarMode> ignoreHotbar = new EnumSetting<IgnoreHotbarMode>(
      "IgnoreHotbar",
      IgnoreHotbarMode.Semi,
      "Don't check hotbar for items\n - Off: Check hotbar for all items\n - Semi: Only check hotbar for totems\n - SemiLast: Only check hotbar for totems when none in inv\n - Full: Don't check hotbar for items at all"
   );
   private final Timer aa = new Timer();
   public Item ab = null;
   public int ac = -1;
   private boolean ad = false;
   private boolean ae = false;
   private Item af = null;
   private Bind ag = null;
   private int ah = -1;
   private final Timer ai = new Timer();
   private final Timer aj = new Timer();
   private final Timer ak = new Timer();
   private boolean al = false;
   private boolean am = false;
   private boolean an = false;
   private boolean ao = false;
   private boolean ap = false;
   private boolean aq = false;
   private boolean ar = false;

   private static void method1750(String var0) {
      if (mc.player != null) {
         BozeLogger.method529(INSTANCE, var0);
      }
   }

   public boolean method1971() {
      return this.offhandMode.getValue() == OffhandMode.Anarchy && !Options.INSTANCE.method1971();
   }

   private boolean method1972() {
      return this.offhandMode.getValue() == OffhandMode.Ghost || Options.INSTANCE.method1971();
   }

   private OffHand() {
      super(
         "Offhand",
         "Automatically places totems and other items in your offhand\nHaving a totem in your HotBar will make you less likely to fail",
         Category.Combat
      );
      this.field435 = true;
   }

   @Override
   public void onEnable() {
      this.al = false;
      this.ad = false;
      this.ag = null;
      this.ab = null;
      this.ac = -1;
      this.ah = -1;
   }

   private void method1904() {
      this.am = mc.options.forwardKey.isPressed();
      this.an = mc.options.backKey.isPressed();
      this.ao = mc.options.leftKey.isPressed();
      this.ap = mc.options.rightKey.isPressed();
      this.aq = mc.options.sprintKey.isPressed();
      this.ar = mc.options.sneakKey.isPressed();
   }

   @Override
   public String method1322() {
      if (!MinecraftUtils.isClientActive()) {
         return super.method1322();
      } else {
         Item var4 = this.method447();
         return var4 != null ? var4.getName().getString() : super.method1322();
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (!this.method1972() && MinecraftUtils.isClientActive()) {
         if (event.packet instanceof EntityStatusS2CPacket var5 && this.reactMode.getValue() == ReactMode.Packet) {
            if (var5.getStatus() == 35) {
               try {
                  if (((EntityStatusS2CPacketAccessor)var5).getEntityId() == mc.player.getId()) {
                     NoSlow.INSTANCE.field3325 = true;
                     method1750("Received totem pop packet");
                     this.method482(Items.TOTEM_OF_UNDYING);
                     if (this.ae) {
                        this.method1201();
                        this.ae = false;
                     }

                     NoSlow.INSTANCE.field3325 = false;
                     return;
                  }
               } catch (Exception var8) {
               }

               return;
            }

            return;
         }

         if (event.packet instanceof ScreenHandlerSlotUpdateS2CPacket var6
            && this.ab != null
            && var6.getSlot() == 45
            && var6.getStack().getItem().equals(this.ab)) {
            method1750("Confirmed item swap for " + this.ab.getName().getString());
            this.aa.setLastTime(0L);
            this.ab = null;
            this.ac = -1;
            this.ad = true;
         }
      }
   }

   @EventHandler
   public void method384(Class1204 event) {
      if (MinecraftUtils.isClientActive()) {
         if (this.notify.getValue() && event.field65 == mc.player) {
            String var5 = "th";
            if (event.field66 != 3 && (event.field66 <= 22 || !Integer.toString(event.field66).endsWith("3"))) {
               if (event.field66 != 2 && (event.field66 <= 21 || !Integer.toString(event.field66).endsWith("2"))) {
                  if (event.field66 == 1 || event.field66 > 20 && Integer.toString(event.field66).endsWith("1")) {
                     var5 = "st";
                  }
               } else {
                  var5 = "nd";
               }
            } else {
               var5 = "rd";
            }

            String var10002 = this.getName();
            int var7 = event.field66;
            NotificationManager.method1151(new Notification(var10002, "Popped " + var7 + var5 + " totem", Notifications.PRIORITY, NotificationPriority.Red));
         }
      }
   }

   @EventHandler
   public void method1752(FlipFrameEvent event) {
      if (!this.method1971() && !event.method1022() && MinecraftUtils.isClientActive()) {
         int var5 = this.method2010();
         if (var5 == -1) {
            if (mc.currentScreen instanceof AbstractInventoryScreen) {
               if (this.inventoryMode.getValue() == InventoryMode.Auto && this.al && this.ak.hasElapsed(this.closeDelay.method1295() * 1000.0)) {
                  mc.player.closeHandledScreen();
                  this.closeDelay.method1296();
                  this.ak.reset();
                  this.al = false;
                  if (this.keepMovement.getValue()) {
                     mc.options.forwardKey.setPressed(this.am);
                     mc.options.backKey.setPressed(this.an);
                     mc.options.leftKey.setPressed(this.ao);
                     mc.options.rightKey.setPressed(this.ap);
                     mc.options.sprintKey.setPressed(this.aq);
                     mc.options.sneakKey.setPressed(this.ar);
                  }
               }
            } else {
               this.ai.reset();
            }
         } else if (this.inventoryMode.getValue() != InventoryMode.Ignore && !(mc.currentScreen instanceof AbstractInventoryScreen)) {
            this.aj.reset();
            if (this.inventoryMode.getValue() == InventoryMode.Auto && this.ai.hasElapsed(this.openDelay.method1295() * 1000.0)) {
               this.al = true;
               this.openDelay.method1296();
               if (this.keepMovement.getValue()) {
                  this.method1904();
               }

               ((KeyBindingAccessor)mc.options.inventoryKey).setTimesPressed(1);
            }
         } else {
            PlayerScreenHandler var6 = mc.player != null ? mc.player.playerScreenHandler : null;
            if (var6 == null || var6.getCursorStack().isEmpty()) {
               if (this.aj.hasElapsed(this.swapDelay.method1295() * 1000.0)) {
                  mc.interactionManager.clickSlot(var6.syncId, var5 < 9 ? var5 + 36 : var5, 40, SlotActionType.SWAP, mc.player);
                  this.swapDelay.method1296();
                  this.aj.reset();
                  this.ak.reset();
                  event.method1020();
               }
            }
         }
      }
   }

   private int method2010() {
      Item var4 = this.method447();
      if (var4 != null && var4 != mc.player.getOffHandStack().getItem()) {
         byte var5 = 0;
         if (this.ignoreHotbar.getValue() == IgnoreHotbarMode.Full
            || this.ignoreHotbar.getValue() == IgnoreHotbarMode.SemiLast
            || this.ignoreHotbar.getValue() == IgnoreHotbarMode.Semi && var4 != Items.TOTEM_OF_UNDYING) {
            var5 = 9;
         }

         int var6 = InventoryHelper.method170(OffHand::lambda$getSlot$15, var5, mc.player.getInventory().size());
         if (var4 == Items.TOTEM_OF_UNDYING && var6 == -1 && this.ignoreHotbar.getValue() == IgnoreHotbarMode.SemiLast) {
            var5 = 0;
            var6 = InventoryHelper.method170(OffHand::lambda$getSlot$16, var5, mc.player.getInventory().size());
         }

         return var6;
      } else {
         return -1;
      }
   }

   @EventHandler
   public void method1812(MouseButtonEvent event) {
      if (event.action == KeyAction.Release && this.ag != null && this.ag.matches(false, event.button)) {
         this.ag = null;
      }
   }

   @EventHandler
   public void method1944(KeyEvent event) {
      if (event.action == KeyAction.Release && this.ag != null && this.ag.matches(true, event.key)) {
         this.ag = null;
      }
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      if (MinecraftUtils.isClientActive() && !this.method1972()) {
         if (this.ab != null) {
            if (!this.aa.hasElapsed(1000.0)) {
               return;
            }

            this.ab = null;
         }

         if (this.ad) {
            this.ad = false;
         } else {
            Item var5 = this.method447();
            if (var5 != null) {
               if (var5.equals(mc.player.getOffHandStack().getItem())) {
                  return;
               }

               this.method482(var5);
            }

            if (this.ae) {
               this.method1201();
               this.ae = false;
            }
         }
      }
   }

   private void method482(Item var1) {
      if (mc.player.playerScreenHandler == mc.player.currentScreenHandler && !(mc.currentScreen instanceof AbstractInventoryScreen) && !mc.player.isCreative()
         || this.inInventory.getValue() && mc.currentScreen instanceof InventoryScreen) {
         byte var5 = 0;
         if (this.ignoreHotbar.getValue() == IgnoreHotbarMode.Full
            || this.ignoreHotbar.getValue() == IgnoreHotbarMode.SemiLast
            || this.ignoreHotbar.getValue() == IgnoreHotbarMode.Semi && var1 != Items.TOTEM_OF_UNDYING) {
            var5 = 9;
         }

         int var6 = -1;
         if (this.ah != -1 && mc.player.getInventory().getStack(this.ah).getItem().equals(var1)) {
            var6 = this.ah;
         } else {
            var6 = InventoryHelper.method171(this::lambda$doOffhand$17, var5, mc.player.getInventory().size());
         }

         if (var6 == -1) {
            var6 = InventoryHelper.method172(this::lambda$doOffhand$18);
         }

         if (var1 == Items.TOTEM_OF_UNDYING && var6 == -1 && this.ignoreHotbar.getValue() == IgnoreHotbarMode.SemiLast) {
            var5 = 0;
            var6 = InventoryHelper.method171(this::lambda$doOffhand$19, var5, mc.player.getInventory().size());
         }

         if (var6 >= 0) {
            this.method483(var6, var1);
         }
      }
   }

   private void method483(int var1, Item var2) {
      if (var1 >= 0) {
         this.ah = -1;
         NoSlow.INSTANCE.field3325 = true;
         this.aa.reset();
         this.ab = var2;
         this.ac = var1;
         if (var1 < 9) {
            int var6 = Boze.getModules().field906.field1616;
            if (var6 != var1) {
               mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(var1));
            }

            mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ORIGIN, Direction.DOWN));
            this.ah = var1;
            if (var6 != var1) {
               mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(var6));
            }

            method1750("Swapped to slot " + var1 + " in hotbar" + mc.player.getInventory().getStack(var1).getName().getString());
         } else {
            if (!this.ae) {
               this.method1854();
            }

            if (var1 >= 420) {
               var1 -= 420;
            } else {
               this.ah = var1;
            }

            ScreenHandler var8 = mc.player.currentScreenHandler;
            Int2ObjectArrayMap var7 = new Int2ObjectArrayMap();
            var7.put(var1, var8.getSlot(var1).getStack());
            mc.player
               .networkHandler
               .sendPacket(
                  new ClickSlotC2SPacket(
                     mc.player.currentScreenHandler.syncId, var8.getRevision(), var1, 40, SlotActionType.SWAP, var8.getCursorStack().copy(), var7
                  )
               );
            method1750("Swapped to slot " + var1 + " in inventory with item " + var8.getSlot(var1).getStack().getItem().getName().getString());
         }

         NoSlow.INSTANCE.field3325 = false;
      }
   }

   private void method1854() {
      this.ae = true;
      if (((ClientPlayerEntityAccessor)mc.player).getLastSprinting()) {
         mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
         short var10000 = 5487;
         ((ClientPlayerEntityAccessor)mc.player).setLastSprinting(false);
         method1750("Cancelled sprinting");
      }

      if (this.eating.getValue() && mc.player.isUsingItem()) {
         mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
         short var1 = 12415;
         mc.player.stopUsingItem();
         method1750("Cancelled eating");
      }

      if (this.movement.getValue() && mc.player.getVelocity().length() > 0.0) {
         mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
         method1750("Cancelled movement");
      }
   }

   private void method1201() {
      if (this.attacks.getValue()) {
         mc.player.resetLastAttackedTicks();
         method1750("Cancelled attacks");
      }
   }

   private Item method447() {
      if (mc.player.getHealth() + mc.player.getAbsorptionAmount() <= this.health.getValue()) {
         return Items.TOTEM_OF_UNDYING;
      } else {
         if (this.safety.getValue() != SafetyMode.None) {
            if (mc.player.fallDistance > 8.0F && !PacketFly.INSTANCE.isEnabled()) {
               return Items.TOTEM_OF_UNDYING;
            }

            for (Entity var5 : mc.world.getEntities()) {
               if (var5 instanceof EndCrystalEntity
                  && var5.distanceTo(mc.player) < 6.0F
                  && (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) - Class3069.method6004(mc.player, var5.getPos())
                     <= (double)(this.safety.getValue() == SafetyMode.Lethal ? 1.0F : this.health.getValue())) {
                  return Items.TOTEM_OF_UNDYING;
               }
            }
         }

         if (this.potion.method476().isPressed()) {
            return Items.POTION;
         } else if (mc.player.getOffHandStack().getItem() == Items.POTION) {
            return Items.POTION;
         } else {
            Item var6 = mc.player.getOffHandStack().getItem();
            if (this.offhandItem.getValue() == OffhandItem.Totem) {
               return Items.TOTEM_OF_UNDYING;
            } else if (this.offhandItem.getValue() == OffhandItem.GApple) {
               return this.method487();
            } else if (this.offhandItem.getValue() == OffhandItem.Crystal) {
               return Items.END_CRYSTAL;
            } else {
               if (this.offhandItem.getValue() == OffhandItem.Binds && this.ag == null && mc.currentScreen == null) {
                  if (this.totem.method476().isPressed() && var6 != Items.TOTEM_OF_UNDYING) {
                     this.af = Items.TOTEM_OF_UNDYING;
                     this.ag = this.totem.method476();
                     return Items.TOTEM_OF_UNDYING;
                  }

                  if (this.gapple.method476().isPressed() && var6 != Items.GOLDEN_APPLE) {
                     this.af = Items.GOLDEN_APPLE;
                     this.ag = this.gapple.method476();
                     return Items.GOLDEN_APPLE;
                  }

                  if (this.egapple.method476().isPressed() && var6 != Items.ENCHANTED_GOLDEN_APPLE) {
                     this.af = Items.ENCHANTED_GOLDEN_APPLE;
                     this.ag = this.egapple.method476();
                     return Items.ENCHANTED_GOLDEN_APPLE;
                  }

                  if (this.crystal.method476().isPressed() && var6 != Items.END_CRYSTAL) {
                     this.af = Items.END_CRYSTAL;
                     this.ag = this.crystal.method476();
                     return Items.END_CRYSTAL;
                  }

                  if (this.af != null) {
                     return this.af;
                  }
               }

               Item var7 = null;
               if (this.offhandItem.getValue() == OffhandItem.Integration) {
                  if (mc.player.isFallFlying()) {
                     return Items.TOTEM_OF_UNDYING;
                  }

                  if (mc.player.getMainHandStack().getItem() instanceof SwordItem
                     && (this.method1972() || mc.options.useKey.isPressed())
                     && this.swordGap.getValue()
                     && (
                        !this.allowInteract.getValue()
                           || mc.crosshairTarget == null
                           || mc.crosshairTarget.getType() != Type.BLOCK
                           || !Class5924.method2088(mc.world.getBlockState(((BlockHitResult)mc.crosshairTarget).getBlockPos()).getBlock())
                     )) {
                     return this.method487();
                  }

                  if (mc.player.getMainHandStack().getItem() == Items.TOTEM_OF_UNDYING
                     && mc.options.useKey.isPressed()
                     && this.totemGap.getValue()
                     && (
                        !this.allowInteract.getValue()
                           || mc.crosshairTarget == null
                           || mc.crosshairTarget.getType() != Type.BLOCK
                           || !Class5924.method2088(mc.world.getBlockState(((BlockHitResult)mc.crosshairTarget).getBlockPos()).getBlock())
                     )) {
                     return this.method487();
                  }

                  if (AutoCrystal.INSTANCE.isEnabled()
                     && this.antiWeakness.getValue()
                     && mc.player.hasStatusEffect(StatusEffects.WEAKNESS)
                     && (var7 = this.method485()) != null) {
                     return var7;
                  }

                  if (AutoCrystal.INSTANCE.isEnabled() && this.autoCrystal.getValue()) {
                     return Items.END_CRYSTAL;
                  }
               }

               return this.force.getValue() ? Items.TOTEM_OF_UNDYING : null;
            }
         }
      }
   }

   private Item method485() {
      if (mc.player.getOffHandStack().getItem() instanceof SwordItem) {
         return mc.player.getOffHandStack().getItem();
      } else {
         for (int var4 = 44; var4 >= 9; var4--) {
            Item var6 = mc.player.getInventory().getStack(var4 >= 36 ? var4 - 36 : var4).getItem();
            if (var6 instanceof SwordItem) {
               return (SwordItem)var6;
            }
         }

         return null;
      }
   }

   private boolean method486(Item var1) {
      if (var1 == Items.GOLDEN_APPLE) {
         return this.goldenApple.getValue() != GoldenAppleMode.Enc;
      } else {
         return var1 == Items.ENCHANTED_GOLDEN_APPLE ? this.goldenApple.getValue() != GoldenAppleMode.Crap : false;
      }
   }

   private Item method487() {
      if (this.goldenApple.getValue() == GoldenAppleMode.Enc) {
         return Items.ENCHANTED_GOLDEN_APPLE;
      } else if (this.goldenApple.getValue() == GoldenAppleMode.Crap) {
         return Items.GOLDEN_APPLE;
      } else if (this.method486(mc.player.getOffHandStack().getItem())) {
         return mc.player.getOffHandStack().getItem();
      } else {
         for (int var4 = this.ignoreHotbar.getValue() != IgnoreHotbarMode.Off ? 36 : 44; var4 >= 9; var4--) {
            Item var5 = mc.player.getInventory().getStack(var4 >= 36 ? var4 - 36 : var4).getItem();
            if (var5 == Items.GOLDEN_APPLE) {
               return Items.GOLDEN_APPLE;
            }

            if (var5 == Items.ENCHANTED_GOLDEN_APPLE) {
               return Items.ENCHANTED_GOLDEN_APPLE;
            }
         }

         return Items.ENCHANTED_GOLDEN_APPLE;
      }
   }

   private boolean lambda$doOffhand$19(Item var1, ItemStack var2, Integer var3) {
      return var2.getItem() == var1 && var3 != this.ac && var3 != 40 && !InventoryUtil.method159(var3);
   }

   private boolean lambda$doOffhand$18(Item var1, ItemStack var2, Integer var3) {
      return var2.getItem() == var1 && var3 != this.ac && var3 != 40;
   }

   private boolean lambda$doOffhand$17(Item var1, ItemStack var2, Integer var3) {
      return var2.getItem() == var1 && var3 != this.ac && var3 != 40 && !InventoryUtil.method159(var3);
   }

   private static boolean lambda$getSlot$16(Item var0, ItemStack var1) {
      return var1.getItem() == var0;
   }

   private static boolean lambda$getSlot$15(Item var0, ItemStack var1) {
      return var1.getItem() == var0;
   }

   private boolean lambda$new$14() {
      return this.offhandItem.getValue() == OffhandItem.Binds;
   }

   private boolean lambda$new$13() {
      return this.offhandItem.getValue() == OffhandItem.Binds;
   }

   private boolean lambda$new$12() {
      return this.offhandItem.getValue() == OffhandItem.Binds;
   }

   private boolean lambda$new$11() {
      return this.offhandItem.getValue() == OffhandItem.Binds;
   }

   private boolean lambda$new$10() {
      return this.swordGap.getValue() || this.totemGap.getValue();
   }

   private boolean lambda$new$9() {
      return this.swordGap.getValue() || this.totemGap.getValue();
   }

   private boolean lambda$new$8() {
      return this.offhandItem.getValue() == OffhandItem.Integration;
   }

   private boolean lambda$new$7() {
      return this.offhandItem.getValue() == OffhandItem.Integration;
   }

   private boolean lambda$new$6() {
      return this.offhandItem.getValue() == OffhandItem.Integration;
   }

   private boolean lambda$new$5() {
      return this.offhandItem.getValue() == OffhandItem.Integration;
   }

   private boolean lambda$new$4() {
      return this.method1972() && this.inventoryMode.getValue() == InventoryMode.Auto;
   }

   private boolean lambda$new$3() {
      return this.method1972() && this.inventoryMode.getValue() != InventoryMode.Ignore;
   }

   private boolean lambda$new$2() {
      return this.method1972() && this.inventoryMode.getValue() == InventoryMode.Auto;
   }

   private boolean lambda$new$1() {
      return this.method1972() && this.inventoryMode.getValue() == InventoryMode.Auto;
   }

   private static boolean lambda$new$0() {
      return !Options.INSTANCE.method1971();
   }
}
