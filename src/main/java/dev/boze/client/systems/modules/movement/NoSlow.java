package dev.boze.client.systems.modules.movement;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.NoSlowInvMove;
import dev.boze.client.enums.NoSlowItems;
import dev.boze.client.enums.NoSlowWebs;
import dev.boze.client.events.*;
import dev.boze.client.gui.components.scaled.ColorSettingComponent;
import dev.boze.client.gui.components.scaled.RGBASettingComponent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.mixin.ClientPlayerEntityAccessor;
import dev.boze.client.mixin.EntityAccessor;
import dev.boze.client.mixin.MinecraftClientAccessor;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.movement.elytraboost.nh;
import dev.boze.client.systems.modules.render.FreeCam;
import dev.boze.client.systems.modules.render.FreeLook;
import dev.boze.client.utils.ItemEnchantmentUtils;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import mapped.Class5913;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.*;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket.Action;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.OnGroundOnly;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.PositionAndOnGround;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import java.util.LinkedList;

public class NoSlow extends Module {
   public static final NoSlow INSTANCE = new NoSlow();
   public EnumSetting<NoSlowItems> field3304 = new EnumSetting<NoSlowItems>(
      "Items",
      NoSlowItems.On,
      "Mode to use for items\n - Off: Don't NoSlow items\n - On: Vanilla NoSlow\n - GrimV3: Grim V3 NoSlow\n;  * requires Elytra\n;  * won't work in Air\n - NCP: NCP NoSlow\n - NCPStrict: NCP Updated NoSlow\n - GrimV2: Grim V2 NoSlow\n - GrimV2Old: Grim V2 old version NoSlow\n - Blink: Blink NoSlow\n"
   );
   private BooleanSetting field3305 = new BooleanSetting("OnlyOffhand", false, "Only apply noslow to offhand items", this.field3304);
   private final BooleanSetting field3306 = new BooleanSetting("GroundCheck", false, "Only start noslowing when on ground", this::lambda$new$0, this.field3304);
   private final BooleanSetting field3307 = new BooleanSetting("Render", false, "Render server position", this::lambda$new$1, this.field3304);
   public final EnumSetting<NoSlowWebs> field3308 = new EnumSetting<NoSlowWebs>(
      "Webs", NoSlowWebs.Off, "Mode to use for webs\n - Off: Don't NoSlow in webs\n - Vanilla: Vanilla web NoSlow\n - Grim: Grim web NoSlow\n"
   );
   private BooleanSetting field3309 = new BooleanSetting("SoulSand", false, "Prevents Soul Sand from slowing you down");
   private BooleanSetting field3310 = new BooleanSetting("SlimeBlocks", false, "Prevents Slime Blocks from slowing you down");
   private BooleanSetting field3311 = new BooleanSetting("Ladders", false, "Prevents Ladders from slowing you down");
   public EnumSetting<NoSlowInvMove> field3312 = new EnumSetting<NoSlowInvMove>(
      "InvMove",
      NoSlowInvMove.On,
      "Lets you move around while in inventories\n - Off: No inventory move\n - On: Normal inventory move\n - NCPStrict: NCP Updated inventory move\n"
   );
   private BooleanSetting field3313 = new BooleanSetting(
      "OnlyPickup", false, "Only apply strict to pickups, not swaps or quickmoves", this::lambda$new$2, this.field3312
   );
   private BooleanSetting field3314 = new BooleanSetting("Sneak", false, "Allows you to sneak while in inventories", this.field3312);
   private BooleanSetting field3315 = new BooleanSetting("Look", true, "Lets you use arrow keys to look around while in inventories", this.field3312);
   public BooleanSetting field3316 = new BooleanSetting("Sneaking", false, "Prevents sneaking from slowing you down");
   public BooleanSetting field3317 = new BooleanSetting("Crawling", false, "Prevents crawling from slowing you down");
   public BlockPos field3318 = null;
   private long field3319 = 0L;
   private LinkedList<Packet<?>> field3320 = new LinkedList();
   private boolean field3321 = false;
   private boolean field3322 = false;
   private FakePlayerEntity field3323;
   private nh field3324 = null;
   public boolean field3325 = false;

   public NoSlow() {
      super("NoSlow", "Prevents items and blocks from slowing you down", Category.Movement);
   }

   @Override
   public void onEnable() {
      this.field3321 = false;
      this.field3324 = ElytraBoost.INSTANCE.field1011;
   }

   @Override
   public void onDisable() {
      this.field3324.method1820();
      this.field3318 = null;
   }

   @EventHandler
   public void method1849(ACRotationEvent event) {
      if (this.field3304.method461() == NoSlowItems.GrimV3 && !event.method1018(AnticheatMode.Grim, false)) {
         if (!ElytraBoost.INSTANCE.isEnabled() && !ElytraRecast.INSTANCE.isEnabled() && !ElytraFly.INSTANCE.isEnabled()) {
            if (mc.player.isFallFlying()) {
               this.field3324.method1820();
            } else if (mc.player.isUsingItem() && this.method1859()) {
               this.field3324.field3208 = true;
               this.field3324.field3209 = true;
               this.field3324.field3210 = true;
               this.field3324.method1819();
            } else {
               this.field3324.method1820();
               if (mc.player != null) {
                  mc.player.stopFallFlying();
               }
            }
         }
      }
   }

   @EventHandler
   public void method1850(MovementEvent event) {
      if (this.field3304.method461() == NoSlowItems.NCPStrict
         && mc.player.isUsingItem()
         && (mc.player.getActiveHand() == Hand.OFF_HAND || !this.field3305.method419())) {
         event.isSneaking = true;
      }

      if (!this.field3320.isEmpty() && !mc.player.isUsingItem() && !this.field3321) {
         while (!this.field3320.isEmpty()) {
            mc.player.networkHandler.sendPacket((Packet)this.field3320.poll());
         }

         if (this.field3323 != null) {
            this.field3323.method1416();
            this.field3323 = null;
         }
      } else if (this.field3304.method461() == NoSlowItems.GrimV2 && mc.player.isUsingItem() && !mc.player.isRiding() && !mc.player.isFallFlying()) {
         if (mc.player.getActiveHand() == Hand.OFF_HAND) {
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot % 8 + 1));
            mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
         } else {
            Class5913.method16(Hand.OFF_HAND);
         }
      }
   }

   @EventHandler(
      priority = 100
   )
   public void method1851(PlayerMoveEvent event) {
      if (this.field3309.method419() && mc.world.getBlockState(mc.player.getBlockPos()).getBlock() == Blocks.SOUL_SAND) {
         mc.player.setVelocity(mc.player.getVelocity().multiply(2.5, 1.0, 2.5));
      }

      if (this.field3311.method419() && mc.player.isHoldingOntoLadder() && mc.player.getVelocity().y > 0.0 && Class5924.method87(Blocks.LADDER)) {
         mc.player.setVelocity(mc.player.getVelocity().x, 0.169, mc.player.getVelocity().z);
      }

      if (this.field3310.method419()
         && mc.world.getBlockState(BlockPos.ofFloored(mc.player.getPos().add(0.0, -0.01, 0.0))).getBlock() == Blocks.SLIME_BLOCK
         && mc.player.isOnGround()) {
         double var5 = Math.abs(mc.player.getVelocity().y);
         if (var5 < 0.1 && !mc.player.bypassesSteppingEffects()) {
            double var7 = 1.0 / (0.4 + var5 * 0.2);
            mc.player.setVelocity(mc.player.getVelocity().multiply(var7, 1.0, var7));
         }
      }
   }

   public void method1852() {
      if (this.isEnabled() && this.field3304.method461() == NoSlowItems.Blink && !PacketFly.INSTANCE.isEnabled() && !this.method1857()) {
         mc.player.stopUsingItem();
         ((MinecraftClientAccessor)mc).setItemUseCooldown(4);
      }
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      if (!(event.packet instanceof PlayerInteractItemC2SPacket)
         || !MinecraftUtils.isClientActive()
         || this.field3304.method461() != NoSlowItems.NCP
         || mc.player.getActiveHand() != Hand.OFF_HAND && this.field3305.method419()) {
         if (event.packet instanceof ClickSlotC2SPacket
            && mc.currentScreen instanceof HandledScreen
            && !this.field3325
            && this.field3312.method461() == NoSlowInvMove.NCPStrict) {
            if (this.field3313.method419()
               && ((ClickSlotC2SPacket)event.packet).getActionType() != SlotActionType.PICKUP
               && ((ClickSlotC2SPacket)event.packet).getActionType() != SlotActionType.PICKUP_ALL) {
               return;
            }

            this.method1854();
         } else {
            if (event.packet instanceof PlayerMoveC2SPacket var5
               && this.field3304.method461() == NoSlowItems.Blink
               && !PacketFly.INSTANCE.isEnabled()
               && !this.method1857()) {
               if (var5 instanceof OnGroundOnly || var5 instanceof LookAndOnGround) {
                  return;
               }

               if (this.field3306.method419()) {
                  if (((PlayerMoveC2SPacket)event.packet).isOnGround()) {
                     this.field3322 = true;
                  } else if (this.field3320.isEmpty()) {
                     this.field3322 = false;
                     return;
                  }
               }

               if (!mc.player.isUsingItem() || mc.player.getActiveHand() != Hand.OFF_HAND && this.field3305.method419()) {
                  if (!this.field3321) {
                     return;
                  }

                  this.field3321 = false;
               } else {
                  this.field3321 = true;
               }

               if (this.field3307.method419() && this.field3320.isEmpty()) {
                  RenderSystem.recordRenderCall(this::lambda$onPacketSend$3);
               }

               event.method1020();
               this.field3320.add(var5);
               return;
            }

            if (event.packet instanceof ClientCommandC2SPacket var6
               && this.field3304.method461() == NoSlowItems.Blink
               && !PacketFly.INSTANCE.isEnabled()
               && !this.method1857()) {
               if (this.field3306.method419() && this.field3320.isEmpty() && !this.field3322) {
                  return;
               }

               if (!mc.player.isUsingItem() || mc.player.getActiveHand() != Hand.OFF_HAND && this.field3305.method419()) {
                  if (!this.field3321) {
                     return;
                  }
               } else {
                  this.field3321 = true;
               }

               event.method1020();
               this.field3320.add(var6);
            }
         }
      } else if (ItemEnchantmentUtils.hasEnchantments(
         ((PlayerInteractItemC2SPacket)event.packet).getHand() == Hand.MAIN_HAND
            ? mc.player.getMainHandStack().getItem()
            : mc.player.getOffHandStack().getItem()
      )) {
         mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
      }
   }

   private void method1854() {
      if (((ClientPlayerEntityAccessor)mc.player).getLastSprinting()) {
         mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, Mode.STOP_SPRINTING));
         ((ClientPlayerEntityAccessor)mc.player).setLastSprinting(false);
      }

      if (mc.player.isUsingItem()) {
         mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
         mc.player.stopUsingItem();
      }

      if (mc.player.getVelocity().length() > 0.0) {
         mc.player.networkHandler.sendPacket(new PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.isOnGround()));
      }
   }

   @EventHandler
   public void method1855(dN event) {
      if (this.field3304.method461() != NoSlowItems.Off && (mc.player.getActiveHand() == Hand.OFF_HAND || !this.field3305.method419())) {
         if (this.field3304.method461() == NoSlowItems.GrimV3 && !this.method1859()) {
            return;
         }

         if (this.field3306.method419() && this.field3304.method461() == NoSlowItems.Blink && !this.field3322) {
            return;
         }

         if (this.field3304.method461() == NoSlowItems.Blink && (PacketFly.INSTANCE.isEnabled() || this.method1857())) {
            return;
         }

         if (this.field3304.method461() == NoSlowItems.GrimV2Old && mc.player.getActiveItem().getItem() == Items.BOW) {
            return;
         }

         event.method1021(true);
      }
   }

   @EventHandler
   public void method1856(PostRender event) {
      if (MinecraftUtils.isClientActive()) {
         float var5 = (float)(System.currentTimeMillis() - this.field3319) / 5.0F;
         this.field3319 = System.currentTimeMillis();
         if (this.field3312.method461() != NoSlowInvMove.Off
            && this.field3315.method419()
            && this.method1858(mc.currentScreen)
            && !FreeCam.INSTANCE.isEnabled()
            && !ElytraRecast.method1974()
            && !FreeLook.INSTANCE.isEnabled()) {
            float var6 = 0.0F;
            float var7 = 0.0F;
            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 263)) {
               var6 -= var5;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 262)) {
               var6 += var5;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 265)) {
               var7 -= var5;
            }

            if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 264)) {
               var7 += var5;
            }

            mc.player.setYaw(mc.player.getYaw() + var6);
            mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + var7, -90.0F, 90.0F));
         }

         if (this.field3312.method461() != NoSlowInvMove.Off
            && this.method1858(mc.currentScreen)
            && !FreeCam.INSTANCE.isEnabled()
            && !ElytraRecast.method1974()
            && !FreeLook.INSTANCE.isEnabled()) {
            for (KeyBinding var9 : new KeyBinding[]{
               mc.options.forwardKey, mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey
            }) {
               var9.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(var9.getBoundKeyTranslationKey()).getCode()));
            }

            if (this.field3314.method419()) {
               mc.options
                  .sneakKey
                  .setPressed(
                     InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode())
                  );
            }
         }
      }
   }

   private boolean method1857() {
      return ((EntityAccessor)mc.player).callGetFlag(7);
   }

   private boolean method1858(Screen var1) {
      return var1 == null
         ? false
         : !(var1 instanceof ChatScreen)
            && !(var1 instanceof BookEditScreen)
            && !(var1 instanceof SignEditScreen)
            && !(var1 instanceof CommandBlockScreen)
            && !(var1 instanceof AnvilScreen)
            && (
               !(var1 instanceof ClickGUI)
                  || !Gui.INSTANCE.field2359.method419()
                     && (
                        ClickGUI.field1335.method579() == null
                           || ClickGUI.field1335.method579() instanceof RGBASettingComponent
                           || ClickGUI.field1335.method579() instanceof ColorSettingComponent
                     )
            );
   }

   private boolean method1859() {
      return !ElytraBoost.INSTANCE.isEnabled()
         && !ElytraRecast.INSTANCE.isEnabled()
         && !ElytraFly.INSTANCE.isEnabled()
         && mc.player.isOnGround()
         && (ElytraBoost.INSTANCE.field1011.method1822() != -1 || mc.player.getEquippedStack(EquipmentSlot.CHEST).getItem() == Items.ELYTRA)
         && !mc.player.input.jumping
         && (mc.player.input.movementForward >= 0.0F || Sprint.INSTANCE.method1869());
   }

   private void lambda$onPacketSend$3() {
      this.field3323 = new FakePlayerEntity(mc.player, mc.player.getGameProfile().getName(), 20.0F, true);
      this.field3323.field1265 = true;
      this.field3323.method2142();
   }

   private boolean lambda$new$2() {
      return this.field3312.method461() == NoSlowInvMove.NCPStrict;
   }

   private boolean lambda$new$1() {
      return this.field3304.method461() == NoSlowItems.Blink;
   }

   private boolean lambda$new$0() {
      return this.field3304.method461() == NoSlowItems.Blink;
   }
}
