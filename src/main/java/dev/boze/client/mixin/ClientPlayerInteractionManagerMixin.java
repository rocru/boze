package dev.boze.client.mixin;

import dev.boze.client.events.PostAttackEntityEvent;
import dev.boze.client.events.PostBlockBreakEvent;
import dev.boze.client.events.PreAttackEntityEvent;
import dev.boze.client.events.PreBlockBreakEvent;
import dev.boze.client.systems.modules.combat.AutoMine;
import dev.boze.client.systems.modules.legit.AutoClicker;
import dev.boze.client.systems.modules.legit.NoMissDelay;
import dev.boze.client.systems.modules.misc.AutoTool;
import dev.boze.client.systems.modules.misc.FastUse;
import dev.boze.client.systems.modules.misc.MiningTweaks;
import dev.boze.client.systems.modules.misc.autotool.qG;
import dev.boze.client.utils.FoodUtil;
import dev.boze.client.utils.InventoryUtil;
import dev.boze.client.utils.world.BlockBreakingUtil;
import dev.boze.client.Boze;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin {
   @Shadow
   private int blockBreakingCooldown;
   @Shadow
   private float currentBreakingProgress;
   @Shadow
   @Final
   private MinecraftClient client;

   @Shadow
   protected abstract void syncSelectedSlot();

   @Inject(
      method = {"interactBlock"},
      at = {@At("HEAD")}
   )
   private void onInteractBlock(ClientPlayerEntity var1, Hand var2, BlockHitResult var3, CallbackInfoReturnable<ActionResult> var4) {
      if (AutoMine.INSTANCE.isEnabled()) {
         AutoMine.INSTANCE.method1466(var2, var3);
      }
   }

   @Redirect(
      method = {"tick"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V"
      )
   )
   private void onTickSyncSelectedSlot(ClientPlayerInteractionManager var1) {
      if (!InventoryUtil.method2114()) {
         this.syncSelectedSlot();
      }
   }

   @Redirect(
      method = {"updateBlockBreakingProgress"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V"
      )
   )
   private void onBlockBreakingSyncSelectedSlot(ClientPlayerInteractionManager var1) {
      if (!InventoryUtil.method2114()) {
         this.syncSelectedSlot();
      }
   }

   @Inject(
      method = {"interactItem"},
      at = {@At("HEAD")}
   )
   public void onInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      FastUse.field2947 = true;
   }

   @Inject(
      method = {"cancelBlockBreaking"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onCancelBlockBreaking(CallbackInfo var1) {
      if (qG.field1630) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"interactItem"},
      at = {@At("RETURN")}
   )
   public void onInteractPost(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
      FastUse.field2947 = false;
   }

   @Inject(
      method = {"stopUsingItem"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onStoppedUsingItemInject(PlayerEntity var1, CallbackInfo var2) {
      if (var1.equals(MinecraftClient.getInstance().player)
         && FastUse.INSTANCE.isEnabled()
         && FastUse.INSTANCE.field2957.method419()
         && FoodUtil.isFood(var1.getStackInHand(var1.getActiveHand()))) {
         this.syncSelectedSlot();
         var1.stopUsingItem();
         var2.cancel();
      }
   }

   @Inject(
      method = {"attackBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAttackBlock(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      PreBlockBreakEvent var4 = PreBlockBreakEvent.method1033(var1, var2, this.blockBreakingCooldown, this.currentBreakingProgress, true);
      Boze.EVENT_BUS.post(var4);
      this.blockBreakingCooldown = var4.method1028();
      this.currentBreakingProgress = var4.method1030();
      if (var4.method1022()) {
         var3.setReturnValue(false);
      }
   }

   @Redirect(
      method = {"breakBlock"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/block/BlockState;isAir()Z"
      )
   )
   private boolean onBreakBlockIsAir(BlockState var1) {
      return MiningTweaks.INSTANCE.isEnabled() && MiningTweaks.INSTANCE.noDesync.method419() ? true : var1.isAir();
   }

   @Inject(
      method = {"attackBlock"},
      at = {@At("RETURN")}
   )
   private void onAttackBlockPost(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      PostBlockBreakEvent var4 = PostBlockBreakEvent.method1032(var1, var2, this.blockBreakingCooldown, this.currentBreakingProgress);
      Boze.EVENT_BUS.post(var4);
   }

   @Inject(
      method = {"updateBlockBreakingProgress"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onUpdateBlockBreakingProgress(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      PreBlockBreakEvent var4 = PreBlockBreakEvent.method1033(var1, var2, this.blockBreakingCooldown, this.currentBreakingProgress, false);
      Boze.EVENT_BUS.post(var4);
      this.blockBreakingCooldown = var4.method1028();
      this.currentBreakingProgress = var4.method1030();
      if (var4.method1022()) {
         var3.setReturnValue(false);
      }
   }

   @Inject(
      method = {"hasLimitedAttackSpeed"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onDoAttack(CallbackInfoReturnable<Boolean> var1) {
      if (NoMissDelay.INSTANCE.isEnabled()
         && NoMissDelay.INSTANCE.method1611()
         && AutoClicker.INSTANCE.isEnabled()
         && AutoClicker.INSTANCE.field2732.method419()
         && AutoClicker.INSTANCE.field2734.method419()
         && this.client.options.attackKey.isPressed()) {
         var1.setReturnValue(false);
      }
   }

   @Inject(
      method = {"updateBlockBreakingProgress"},
      at = {@At("RETURN")}
   )
   private void onUpdateBlockBreakingProgressPost(BlockPos var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
      PostBlockBreakEvent var4 = PostBlockBreakEvent.method1032(var1, var2, this.blockBreakingCooldown, this.currentBreakingProgress);
      Boze.EVENT_BUS.post(var4);
   }

   @Redirect(
      method = {"updateBlockBreakingProgress"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/block/BlockState;calcBlockBreakingDelta(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F"
      )
   )
   private float onUpdateBlockBreakingProgress(BlockState var1, PlayerEntity var2, BlockView var3, BlockPos var4) {
      return AutoTool.INSTANCE.isEnabled() && AutoTool.INSTANCE.field2907 != -1
         ? Math.max(BlockBreakingUtil.method506(var4, var2.getInventory().getStack(AutoTool.INSTANCE.field2907)), var1.calcBlockBreakingDelta(var2, var3, var4))
         : var1.calcBlockBreakingDelta(var2, var3, var4);
   }

   @Inject(
      method = {"attackEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onAttackEntityPre(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      if (((PreAttackEntityEvent) Boze.EVENT_BUS.post(PreAttackEntityEvent.method1089(var2))).method1022()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"attackEntity"},
      at = {@At("TAIL")},
      cancellable = true
   )
   private void onAttackEntityPost(PlayerEntity var1, Entity var2, CallbackInfo var3) {
      if (((PostAttackEntityEvent) Boze.EVENT_BUS.post(PostAttackEntityEvent.method1084(var2))).method1022()) {
         var3.cancel();
      }
   }
}
