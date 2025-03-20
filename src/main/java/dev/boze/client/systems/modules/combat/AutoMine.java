package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.*;
import dev.boze.client.events.*;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.combat.automine.*;
import dev.boze.client.systems.modules.misc.autotool.qG;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class AutoMine extends Module {
   public static AutoMine INSTANCE = new AutoMine();
   public static boolean field2518 = false;
   final Render render = new Render();
   public final AutoSelect autoSelect = new AutoSelect();
   public final Miner miner = new Miner(this);
   private final SettingCategory remineSettings = new SettingCategory("ReMine", "Re-mine options");
   final BooleanSetting instantRemine = new BooleanSetting("Instant", true, "Instantly re-mine blocks", this.remineSettings);
   private final BooleanSetting autoRemine = new BooleanSetting("Auto", false, "Automatically always re-mine blocks", this.remineSettings);
   private final BindSetting remineBind = new BindSetting("Bind", Bind.create(), "Re-mine keybind", () -> !this.autoRemine.getValue(), this.remineSettings);
   private final FloatSetting remineDelay = new FloatSetting(
      "Delay", 0.05F, 0.0F, 1.0F, 0.01F, "Re-mine delay in seconds", () -> this.autoRemine.getValue() || this.remineBind.method476().isValid(), this.remineSettings
   );
   private final BooleanSetting ignoreSelf = new BooleanSetting(
      "IgnoreSelf", false, "Don't re-mine self-placed blocks", () -> this.autoRemine.getValue() || this.remineBind.method476().isValid(), this.remineSettings
   );
   final EnumSetting<AutoMineSwapMode> swapMode = new EnumSetting<AutoMineSwapMode>(
      "Swap",
      AutoMineSwapMode.Silent,
      "Mode for swapping to pickaxe/tool\nHot-bar only modes:\n - Normal: Vanilla swap, hot-bar only\n - Silent: Instantaneously swap to tool and back\nWhole inventory modes (you don't need to keep the tool in your hot-bar):\n - Alt: Alternative silent swap mode, may work where mode silent is patched\nNote: Whole inventory modes may not work on some servers\n"
   );
   final IntSetting swapDelay = new IntSetting("Delay", 0, 0, 20, 1, "Swap tick delay", this.swapMode);
   private final BooleanSetting onlyPickaxe = new BooleanSetting("OnlyPickaxe", false, "Only Packet Mine when holding a pickaxe", () -> this.swapMode.getValue() == AutoMineSwapMode.Off);
   public final StringModeSetting blocks = new StringModeSetting("Blocks", "Blocks to filter");
   final EnumSetting<FilterMode> filter = new EnumSetting<FilterMode>(
      "Filter",
      FilterMode.Off,
      "Block filter\nOnly mine blocks in the list or mine all blocks except the ones in the list\nUse '.set automine blocks add <block>' to add blocks to the list\nUse '.set automine blocks remove <block>' to remove blocks from the list\nUse '.set automine blocks list to list all blocks in the list"
   );
   public final BooleanSetting advanced = new BooleanSetting("Advanced", false, "Enable advanced features and settings");
   final Queue queue = new Queue(this);
   private final AntiRegear antiRegear = new AntiRegear(this);
   private final ProneEscape proneEscape = new ProneEscape(this);
   final BooleanSetting rangeAbort = new BooleanSetting("RangeAbort", true, "Abort mining if block goes out of range", this.advanced::getValue);
   BlockPos field2519 = null;
   boolean field2520 = false;
   private final Timer timer = new Timer();
   private boolean field2521 = false;
   private boolean field2522 = false;

   private static void method1455(String var0) {
      if (field2518 && mc.player != null) {
         System.out.println("[AutoMine @" + mc.player.age + "] " + var0);
      }
   }

   private AutoMine() {
      super("AutoMine", "Automatically mines blocks using packets", Category.Combat);
      this.addSettings(this.render.field267);
   }

   @EventHandler
   public void method1456(PrePacketSendEvent event) {
      if (event.packet instanceof PlayerActionC2SPacket var5) {
         BlockPos var8 = var5.getPos();
         if (var8 == null) {
            return;
         }

         if (mc.world.getBlockState(var8).getBlock() == Blocks.BEDROCK) {
            event.method1020();
            return;
         }

         if (var5.getAction() == Action.START_DESTROY_BLOCK && !var8.equals(this.field2519)) {
            if (BlockUtil.method2101(var8)) {
               this.field2519 = var8;
            } else {
               this.field2519 = null;
            }

            this.field2520 = false;
            this.field2522 = false;
            this.miner.method1416();
            this.timer.reset();
         }
      } else if (event.packet instanceof PlayerInteractBlockC2SPacket var6
         && this.ignoreSelf.getValue()
         && this.field2519 != null
         && this.field2519.equals(var6.getBlockHitResult().getBlockPos().offset(var6.getBlockHitResult().getSide()))) {
         this.field2522 = true;
      }
   }

   @EventHandler
   public void method1457(PacketBundleEvent event) {
      if (event.packet instanceof BlockUpdateS2CPacket var5
         && this.field2519 != null
         && !this.field2520
         && this.field2519.equals(var5.getPos())
         && var5.getState().isAir()) {
         method1455("Mine at " + this.field2519.toShortString() + " confirmed by server");
         this.field2520 = true;
      }
   }

   @Override
   public void onEnable() {
      this.autoSelect.method2142();
      this.antiRegear.method2142();
      this.queue.method2142();
      this.field2519 = null;
      this.field2520 = false;
      this.field2521 = false;
      this.field2522 = false;
   }

   @EventHandler
   public void method1458(Render3DEvent event) {
      this.render.method2071(event);
   }

   @EventHandler
   public void method1459(PreBlockBreakEvent event) {
      if (event.method1024() != null && !qG.field1631) {
         if (!this.onlyPickaxe.getValue()
            || this.swapMode.getValue() != AutoMineSwapMode.Off
            || mc.player.getMainHandStack().getItem() instanceof PickaxeItem) {
            BlockPos var5 = event.method1024();
            Direction var6 = event.method1026();
            if (BlockUtil.method2101(var5) && !this.miner.method2101(event.method1024())) {
               event.method1021(true);
               if (event.field1890 || this.queue.field90.getValue() && this.queue.field88.method434() > 0) {
                  if (this.queue.field88.method434() == 0) {
                     if (!this.miner.field204.isEmpty()) {
                        return;
                     }

                     if (this.miner.method2114()) {
                        this.miner.method99(new BlockDirectionInfo(var5, var6, AutoMineMode.Manual));
                     } else {
                        if (this.miner.field187.getValue() == AnticheatMode.NCP && !this.miner.field188.getValue()) {
                           return;
                        }

                        TaskLogger var7 = null;
                        if (this.miner.field197.getValue() && !this.miner.field201.isEmpty()) {
                           for (TaskLogger var9 : this.miner.field201) {
                              if (var7 == null || var9.field2533 > var7.field2533) {
                                 var7 = var9;
                              }
                           }
                        }

                        for (TaskLogger var11 : new ArrayList<>(this.miner.field201)) {
                           if (var11 != var7) {
                              this.miner.method101(var11);
                           }
                        }

                        this.miner.field201.clear();
                        if (var7 != null) {
                           this.miner.field201.add(var7);
                        }

                        this.miner.method99(new BlockDirectionInfo(var5, var6, AutoMineMode.Manual));
                     }
                  } else {
                     this.queue.method71(new BlockDirectionInfo(var5, var6, AutoMineMode.Manual));
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void method1460(RotationEvent event) {
      if (!event.method555(RotationMode.Sequential, false)) {
         this.method1461();
         this.miner.method2142();
      }
   }

   private void method1461() {
      for (int var4 = 0; this.miner.method2114() && var4 < 3; var4++) {
         BlockDirectionInfo var5 = this.method1462();
         if (var5 == null) {
            break;
         }

         if (this.queue.field88.method434() == 0) {
            this.queue.method1416();
         }

         this.miner.method99(var5);
      }
   }

   private BlockDirectionInfo method1462() {
      if (this.queue.field89.getValue() != AutoMineManualPriorityMode.Off && !this.queue.method2114()) {
         BlockDirectionInfo var4 = this.queue.method1462();
         if (var4 != null && BlockUtil.method2101(var4.field2523) && !this.miner.method2101(var4.field2523)) {
            return var4;
         }
      }

      if (this.proneEscape.field1632.getValue()) {
         BlockDirectionInfo var7 = this.proneEscape.method1462();
         if (var7 != null && BlockUtil.method2101(var7.field2523) && !this.miner.method2101(var7.field2523)) {
            return var7;
         }
      }

      if (this.antiRegear.field131.getValue()) {
         BlockDirectionInfo var8 = this.antiRegear.method1462();
         if (var8 != null && BlockUtil.method2101(var8.field2523) && !this.miner.method2101(var8.field2523)) {
            return var8;
         }
      }

      BlockDirectionInfo var9 = this.autoSelect.method61();
      if (var9 != null && BlockUtil.method2101(var9.field2523) && !this.miner.method2101(var9.field2523)) {
         this.autoSelect.field85 = false;
         return var9;
      } else {
         BlockDirectionInfo var5 = null;
         if (this.autoSelect.field71.getValue() == AutoSelectPriority.Bomber && this.autoSelect.method2114()) {
            int var10 = this.autoSelect.method2010();
            if (var10 == 0) {
               var5 = this.autoSelect.method60();
            } else if (var10 == 2) {
               var5 = this.autoSelect.method1462();
            }
         } else {
            var5 = this.autoSelect.method60();
            if (var5 == null && this.autoSelect.method2114()) {
               int var6 = this.autoSelect.method2010();
               if (var6 == 2) {
                  var5 = this.autoSelect.method1462();
               }
            } else {
               this.autoSelect.method1416();
            }
         }

         if (var5 != null && BlockUtil.method2101(var5.field2523) && !this.miner.method2101(var5.field2523)) {
            if (var5.field2526 == AutoMineMode.Auto) {
               this.autoSelect.field84++;
            }

            this.autoSelect.field85 = false;
            return var5;
         } else {
            this.autoSelect.field85 = false;
            if ((this.autoRemine.getValue() || this.field2521)
               && this.queue.method2114()
               && this.field2519 != null
               && BlockUtil.method2101(this.field2519)
               && !this.field2522) {
               if (this.timer.hasElapsed((double)this.remineDelay.getValue().floatValue()) && !this.miner.method2101(this.field2519)) {
                  BlockDirectionInfo var11 = new BlockLocationInfo(this.field2519, false).method1467();
                  if (BlockUtil.method2101(var11.field2523) && !this.miner.method2101(var11.field2523)) {
                     return var11;
                  }
               }
            } else {
               this.field2521 = false;
            }

            if (!this.queue.method2114()) {
               BlockDirectionInfo var12 = this.queue.method1462();
               if (var12 != null && BlockUtil.method2101(var12.field2523) && !this.miner.method2101(var12.field2523)) {
                  return var12;
               }
            }

            return null;
         }
      }
   }

   @EventHandler(
      priority = 150
   )
   public void method1463(ACRotationEvent event) {
      if (!event.method1018(this.miner.field187.getValue(), true)) {
         Vec3d var5 = this.miner.method1954();
         if (var5 != null) {
            float[] var6 = EntityUtil.method2146(var5);
             event.method1021(true);
             event.yaw = var6[0];
             event.pitch = var6[1];
         }
      }
   }

   @Override
   public String method1322() {
      String var4 = String.valueOf(this.queue.method2010());
      if (this.autoSelect.field60.getValue()) {
         if (this.autoSelect.field79 != null) {
            var4 = var4 + "(CB)";
         } else {
            var4 = var4 + "(AS)";
         }
      }

      return var4;
   }

   @EventHandler
   private void method1464(MouseButtonEvent var1) {
      if (var1.action == KeyAction.Press) {
         if (this.autoSelect.field64.method476().matches(false, var1.button)) {
            this.autoSelect.field85 = true;
         } else if (this.autoSelect.field63.method476().matches(false, var1.button)) {
            this.autoSelect.field60.setValue(!this.autoSelect.field60.getValue());
         } else if (this.autoSelect.field69.method476().matches(false, var1.button)) {
            this.autoSelect.field68.setValue(!this.autoSelect.field68.getValue());
         } else if (this.antiRegear.field132.method476().matches(false, var1.button)) {
            this.antiRegear.field131.setValue(!this.antiRegear.field131.getValue());
            if (this.antiRegear.field131.getValue()) {
               this.antiRegear.method2142();
            }
         } else if (this.proneEscape.field1633.method476().matches(false, var1.button)) {
            this.proneEscape.field1632.setValue(!this.proneEscape.field1632.getValue());
         } else if (this.remineBind.method476().matches(false, var1.button)) {
            this.field2521 = true;
         }
      }
   }

   @EventHandler
   private void method1465(KeyEvent var1) {
      if (var1.action == KeyAction.Press) {
         if (this.autoSelect.field64.method476().matches(true, var1.key)) {
            this.autoSelect.field85 = true;
         } else if (this.autoSelect.field63.method476().matches(true, var1.key)) {
            this.autoSelect.field60.setValue(!this.autoSelect.field60.getValue());
         } else if (this.autoSelect.field69.method476().matches(true, var1.key)) {
            this.autoSelect.field68.setValue(!this.autoSelect.field68.getValue());
         } else if (this.antiRegear.field132.method476().matches(true, var1.key)) {
            this.antiRegear.field131.setValue(!this.antiRegear.field131.getValue());
            if (this.antiRegear.field131.getValue()) {
               this.antiRegear.method2142();
            }
         } else if (this.proneEscape.field1633.method476().matches(true, var1.key)) {
            this.proneEscape.field1632.setValue(!this.proneEscape.field1632.getValue());
         } else if (this.remineBind.method476().matches(true, var1.key)) {
            this.field2521 = true;
         }
      }
   }

   public void method1466(Hand hand, BlockHitResult hitResult) {
      if (mc.player.getStackInHand(hand).getItem() instanceof BlockItem var6 && var6.getBlock() instanceof ShulkerBoxBlock) {
         this.antiRegear.method1801(hitResult.getBlockPos().offset(hitResult.getSide()));
      }
   }

}
