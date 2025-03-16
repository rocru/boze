package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class AutoBridge extends Module {
   public static final AutoBridge INSTANCE = new AutoBridge();
   private final BooleanSetting field2726 = new BooleanSetting("AutoSneak", true, "Automatically sneaks at the edge of blocks");
   private final IntSetting field2727 = new IntSetting("Delay", 0, 0, 10, 1, "Auto Sneak delay", this.field2726);
   private final BooleanSetting field2728 = new BooleanSetting("AutoPlace", true, "Automatically places blocks at the edge of blocks");
   private final MinMaxDoubleSetting field2729 = new MinMaxDoubleSetting(
      "PlaceDelay", new double[]{1.5, 3.0}, 0.0, 10.0, 0.1, "Delay to place in ticks", this.field2728
   );
   private final Timer field2730 = new Timer();

   private AutoBridge() {
      super("AutoBridge", "Automatically bridges for you", Category.Legit);
   }

   @Override
   public void onDisable() {
      if (MinecraftUtils.isClientActive() && this.field2726.method419()) {
         mc.options.sneakKey.setPressed(false);
      }
   }

   @EventHandler
   public void method1584(RotationEvent event) {
      if (!Options.method477(false) && !event.method554(RotationMode.Vanilla)) {
         if (this.field2726.method419()) {
            Vec3d var5 = mc.player.getPos();
            Vec3d var6 = new Vec3d(mc.player.getX() - mc.player.prevX, mc.player.getY() - mc.player.prevY, mc.player.getZ() - mc.player.prevZ);
            var5 = var5.add(var6.multiply((double)this.field2727.method434().intValue()));
            mc.options.sneakKey.setPressed(mc.player.isOnGround() && mc.world.getBlockState(BlockPos.ofFloored(var5).down()).isAir());
         }

         if (this.field2728.method419()
            && mc.player.getMainHandStack().getItem() instanceof BlockItem
            && this.field2730.hasElapsed(this.field2729.method1295() * 50.0)
            && mc.crosshairTarget != null
            && mc.crosshairTarget.getType() == Type.BLOCK) {
            BlockHitResult var10 = (BlockHitResult)mc.crosshairTarget;
            BlockPos var11 = var10.getBlockPos().add(0, 0, 0);
            if (var10.getSide() == Direction.UP || var10.getSide() == Direction.DOWN) {
               return;
            }

            BlockPos var7 = var11.offset(var10.getSide());
            if (!mc.world.canPlace(mc.world.getBlockState(var7), var7, ShapeContext.absent())) {
               return;
            }

            if ((double)var11.getY() == Math.floor(mc.player.getY()) - 1.0 && mc.player.getPos().distanceTo(mc.crosshairTarget.getPos()) < 3.0) {
               ItemStack var8 = mc.player.getStackInHand(Hand.MAIN_HAND);
               if (var8.getItem() instanceof BlockItem) {
                  ((KeyBindingAccessor)mc.options.useKey).setTimesPressed(1);
                  this.field2730.reset();
                  this.field2729.method1296();
               }
            }
         }
      }
   }
}
