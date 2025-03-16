package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.AnchorTriggerDefaultItem;
import dev.boze.client.enums.ClickMethod;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;

public class AnchorTrigger extends Module {
   public static final AnchorTrigger INSTANCE = new AnchorTrigger();
   private final FloatSetting field2704 = new FloatSetting("Jitter", 0.0F, 0.0F, 2.0F, 0.01F, "Mouse jitter amount");
   private final BooleanSetting field2705 = new BooleanSetting("AutoSwap", true, "Automatically swap");
   private final EnumSetting<AnchorTriggerDefaultItem> field2706 = new EnumSetting<AnchorTriggerDefaultItem>(
      "DefaultItem", AnchorTriggerDefaultItem.Any, "Default item to swap back to"
   );
   private final IntSetting field2707 = new IntSetting("Slot", 0, 0, 8, 1, "Default slot to swap back to", this::lambda$new$0);
   private final BooleanSetting field2708 = new BooleanSetting("Break", true, "Break anchors (right click without glowstone)");
   private final BooleanSetting field2709 = new BooleanSetting("Glowstone", true, "Glowstone (right click with glowstone)");
   private final BooleanSetting field2710 = new BooleanSetting("OnlyOwn", false, "Only break/glowstone anchors that you placed");
   private final MinMaxSetting field2711 = new MinMaxSetting("BreakDelay", 10.0, 0.0, 20.0, 0.01, "Delay after breaking to break again");
   private final MinMaxSetting field2712 = new MinMaxSetting("GlowstoneDelay", 10.0, 0.0, 20.0, 0.01, "Delay after placing glowstone to place again");
   private final BooleanSetting field2713 = new BooleanSetting("OnlyWhenHolding", false, "Only place when holding right click");
   private final EnumSetting<ClickMethod> field2714 = new EnumSetting<ClickMethod>("PlaceClicking", ClickMethod.Normal, "Place (right) click mode");
   private final IntArraySetting field2715 = new IntArraySetting("CPS", new int[]{4, 8}, 1, 20, 1, "Right clicks per second");
   private final FloatSetting field2716 = new FloatSetting("CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$1);
   private final IntArraySetting field2717 = new IntArraySetting("SwapDelay", new int[]{2, 5}, 0, 20, 1, "Delay after swapping to place");
   private final MinMaxDoubleSetting field2718 = new MinMaxDoubleSetting(
      "OnsetDelay", new double[]{1.0, 3.0}, 0.0, 10.0, 0.01, "Delay after looking at obsidian/bedrock to place"
   );
   private float field2719 = 0.0F;
   private final ClickManager field2720 = new ClickManager(this.field2714, this.field2715, this.field2716);
   private final Timer field2721 = new Timer();
   private final Timer field2722 = new Timer();
   private final Timer field2723 = new Timer();
   private final Timer field2724 = new Timer();
   private final ConcurrentHashMap<BlockPos, Long> field2725 = new ConcurrentHashMap();

   public AnchorTrigger() {
      super("AnchorTrigger", "Automatically clicks anchors", Category.Legit);
   }

   @Override
   public void onEnable() {
      this.field2719 = 0.0F;
      this.field2720.method2172();
   }

   @EventHandler
   public void method1577(MovementEvent event) {
      if (this.field2710.method419()) {
         this.field2725.entrySet().removeIf(AnchorTrigger::lambda$onSendMovementPackets$2);
      }
   }

   @EventHandler(
      priority = 53
   )
   public void method1578(MouseUpdateEvent event) {
      if (this.field2719 > 0.0F && !event.method1022()) {
         double var5 = (double)(this.field2719 * this.field2704.method423()) * Math.random();
         double var7 = (double)(this.field2719 * this.field2704.method423()) * Math.random();
         if (Math.random() > 0.5) {
            var5 *= -1.0;
         }

         if (Math.random() > 0.5) {
            var7 *= -1.0;
         }

         event.deltaX += var5;
         event.deltaY += var7;
         event.method1021(true);
         this.field2719 = 0.0F;
      }
   }

   @EventHandler
   public void method1579(HandleInputEvent event) {
      if (this.field2705.method419()) {
         if (!this.field2713.method419() || mc.options.useKey.isPressed()) {
            Item var5 = null;
            if (mc.crosshairTarget != null
               && mc.crosshairTarget instanceof BlockHitResult var6
               && mc.world.getBlockState(var6.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR) {
               if (!this.field2710.method419() || this.field2725.containsKey(var6.getBlockPos())) {
                  int var8 = (Integer)mc.world.getBlockState(var6.getBlockPos()).get(RespawnAnchorBlock.CHARGES);
                  if (var8 == 0) {
                     if (!this.field2709.method419()) {
                        return;
                     }

                     var5 = Items.GLOWSTONE;
                  } else if (var8 > 0 && !this.field2708.method419()) {
                     return;
                  }

                  if (var5 != null && mc.player.getInventory().getMainHandStack().getItem() != var5 && mc.player.getOffHandStack().getItem() != var5) {
                     for (int var10 = 0; var10 < 9; var10++) {
                        if (mc.player.getInventory().getStack(var10).getItem() == var5) {
                           ((KeyBindingAccessor)mc.options.hotbarKeys[var10]).setTimesPressed(1);
                           this.field2721.reset();
                           this.field2717.method1376();
                           break;
                        }
                     }
                  } else if (var5 == null && mc.player.getInventory().getMainHandStack().getItem() == Items.GLOWSTONE) {
                     if (this.field2706.method461() == AnchorTriggerDefaultItem.Slot) {
                        ((KeyBindingAccessor)mc.options.hotbarKeys[this.field2707.method434()]).setTimesPressed(1);
                        this.field2721.reset();
                        this.field2717.method1376();
                     } else {
                        for (int var9 = 0; var9 < 9; var9++) {
                           if (this.field2706.method461() == AnchorTriggerDefaultItem.Any
                                 && mc.player.getInventory().getStack(var9).getItem() != Items.GLOWSTONE
                              || this.field2706.method461() == AnchorTriggerDefaultItem.Crystal
                                 && mc.player.getInventory().getStack(var9).getItem() == Items.END_CRYSTAL
                              || this.field2706.method461() == AnchorTriggerDefaultItem.Gapple
                                 && (
                                    mc.player.getInventory().getStack(var9).getItem() == Items.GOLDEN_APPLE
                                       || mc.player.getInventory().getStack(var9).getItem() == Items.ENCHANTED_GOLDEN_APPLE
                                 )
                              || this.field2706.method461() == AnchorTriggerDefaultItem.Sword
                                 && mc.player.getInventory().getStack(var9).getItem() instanceof SwordItem
                              || this.field2706.method461() == AnchorTriggerDefaultItem.Totem
                                 && mc.player.getInventory().getStack(var9).getItem() == Items.TOTEM_OF_UNDYING
                              || this.field2706.method461() == AnchorTriggerDefaultItem.Anchor
                                 && mc.player.getInventory().getStack(var9).getItem() == Items.RESPAWN_ANCHOR) {
                              ((KeyBindingAccessor)mc.options.hotbarKeys[var9]).setTimesPressed(1);
                              this.field2721.reset();
                              this.field2717.method1376();
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 53
   )
   public void method1580(RotationEvent event) {
      if (!event.method554(RotationMode.Sequential)) {
         if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) {
            this.field2720.method2172();
         } else {
            if (this.field2721.hasElapsed((double)(this.field2717.method1367() * 50)) && !mc.player.isUsingItem() && this.method1582()) {
               this.method1583(mc.options.useKey, this.field2720, event, this.field2713.method419());
            }
         }
      }
   }

   @EventHandler
   public void method1581(PrePacketSendEvent event) {
      if (this.field2710.method419()
         && event.packet instanceof PlayerInteractBlockC2SPacket var5
         && mc.player.getStackInHand(var5.getHand()).getItem() == Items.RESPAWN_ANCHOR) {
         this.field2725.put(var5.getBlockHitResult().getBlockPos().offset(var5.getBlockHitResult().getSide()), System.currentTimeMillis());
      }
   }

   private boolean method1582() {
      if (mc.crosshairTarget == null
         || !(mc.crosshairTarget instanceof BlockHitResult var4)
         || mc.world.getBlockState(var4.getBlockPos()).getBlock() != Blocks.RESPAWN_ANCHOR) {
         this.field2722.reset();
         this.field2718.method1296();
         return false;
      } else if (this.field2710.method419() && !this.field2725.containsKey(var4.getBlockPos())) {
         this.field2722.reset();
         this.field2718.method1296();
         return false;
      } else if ((Integer)mc.world.getBlockState(var4.getBlockPos()).get(RespawnAnchorBlock.CHARGES) == 0) {
         if ((!this.field2709.method419() || mc.player.getInventory().getMainHandStack().getItem() != Items.GLOWSTONE)
            && mc.player.getOffHandStack().getItem() != Items.GLOWSTONE) {
            this.field2722.reset();
            this.field2718.method1296();
            return false;
         } else {
            return this.field2722.hasElapsed(this.field2718.method1295() * 50.0) && this.field2724.hasElapsed(this.field2712.getValue() * 50.0);
         }
      } else if ((Integer)mc.world.getBlockState(var4.getBlockPos()).get(RespawnAnchorBlock.CHARGES) <= 0) {
         return this.field2722.hasElapsed(this.field2718.method1295() * 50.0);
      } else if (this.field2708.method419()
         && mc.player.getInventory().getMainHandStack().getItem() != Items.GLOWSTONE
         && mc.player.getOffHandStack().getItem() != Items.GLOWSTONE) {
         return this.field2722.hasElapsed(this.field2718.method1295() * 50.0) && this.field2723.hasElapsed(this.field2711.getValue() * 50.0);
      } else {
         this.field2722.reset();
         this.field2718.method1296();
         return false;
      }
   }

   private void method1583(KeyBinding var1, ClickManager var2, RotationEvent var3, boolean var4) {
      if (!var1.isPressed() && var4) {
         var2.method2172();
      } else {
         int var8 = var2.method2171();
         if (var8 > 0 && ((KeyBindingAccessor)var1).getTimesPressed() == 0) {
            ((KeyBindingAccessor)var1).setTimesPressed(1);
            var3.method2142();
            if (this.field2704.method423() > 0.0F) {
               this.field2719++;
            }

            if (mc.player.getMainHandStack().getItem() != Items.GLOWSTONE && mc.player.getOffHandStack().getItem() != Items.GLOWSTONE) {
               this.field2723.reset();
            } else {
               this.field2724.reset();
            }
         }
      }
   }

   private static boolean lambda$onSendMovementPackets$2(Entry var0) {
      return System.currentTimeMillis() - (Long)var0.getValue() > 10000L;
   }

   private boolean lambda$new$1() {
      return this.field2714.method461() == ClickMethod.Vanilla;
   }

   private boolean lambda$new$0() {
      return this.field2706.method461() == AnchorTriggerDefaultItem.Slot;
   }
}
