package dev.boze.client.systems.modules.combat;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.PlaceMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.SwapMode;
import dev.boze.client.events.ACRotationEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.PlayerPositionEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixininterfaces.IClientPlayerEntity;
import dev.boze.client.render.Placement;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.systems.modules.movement.Step;
import dev.boze.client.systems.modules.render.PlaceRender;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.player.RotationHandler;
import dev.boze.client.utils.world.BlockInteraction;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import mapped.Class2784;
import mapped.Class2811;
import mapped.Class5914;
import mapped.Class5920;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

public class HoleFill extends Module {
   public static final HoleFill INSTANCE = new HoleFill();
   private final BooleanSetting render = new BooleanSetting("Render", true, "Render placements");
   private final ColorSetting fillColor = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill", this.render);
   private final ColorSetting outlineColor = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline", this.render);
   private final BooleanSetting multiTask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final EnumSetting<PlaceMode> placeMode = new EnumSetting<PlaceMode>("Place", PlaceMode.Vanilla, "Place mode");
   private final EnumSetting<AnticheatMode> InteractionMode = new EnumSetting<AnticheatMode>("Mode", AnticheatMode.NCP, "Interaction mode");
   private final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Rotate");
   private final BooleanSetting raycast = new BooleanSetting("RayCast", true, "RayCast");
   private final BooleanSetting strictDirection = new BooleanSetting("StrictDirection", true, "Strict Direction");
   private final IntSetting placeRate = new IntSetting("PlaceRate", 3, 1, 8, 1, "Rate of placing blocks per tick");
   private final IntSetting interval = new IntSetting("Interval", 0, 0, 5, 1, "Tick interval between placements");
   private final MinMaxSetting range = new MinMaxSetting("Range", 3.5, 1.0, 6.0, 0.1, "Horizontal range");
   private final IntSetting vRange = new IntSetting("VRange", 3, 1, 6, 1, "Vertical range");
   public final FloatSetting wallsRange = new FloatSetting("WallsRange", 2.0F, 0.0F, 6.0F, 0.1F, "Walls range");
   private final EnumSetting<SwapMode> swapMode = new EnumSetting<SwapMode>("Swap", SwapMode.Silent, "Auto Swap mode");
   private final BooleanSetting proximity = new BooleanSetting("Proximity", false, "Only fill holes when enemies are nearby");
   private final MinMaxSetting hradius = new MinMaxSetting("HRadius", 1.5, 0.5, 5.0, 0.05, "Proximity horizontal radius", this.proximity);
   private final MinMaxSetting vradius = new MinMaxSetting("VRadius", 1.5, 0.5, 5.0, 0.05, "Proximity vertical radius", this.proximity);
   private final IntSetting extrapolation = new IntSetting("Extrapolation", 0, 0, 20, 1, "Predict target motion by this amount of ticks", this.proximity);
   private final BooleanSetting double = new BooleanSetting("Double", true, "Fill double holes");
   private BooleanSetting obsidian = new BooleanSetting("Obsidian", true, "Use Obsidian");
   private BooleanSetting webs = new BooleanSetting("Webs", false, "Use Webs");
   private BooleanSetting other = new BooleanSetting("Other", false, "Use other blocks");
   public final BooleanSetting onlyWhileSneaking = new BooleanSetting("OnlyWhileSneaking", false, "Only holefill while sneaking");
   private final SettingCategory autoDisable = new SettingCategory("AutoDisable", "Auto Disable settings");
   public final BooleanSetting disableOnJump = new BooleanSetting("DisableOnJump", false, "Disable when you jump out of hole", this.autoDisable);
   public final BooleanSetting onStep = new BooleanSetting("OnStep", false, "Disable when you step", this.autoDisable);
   public final BooleanSetting disableOnTP = new BooleanSetting("DisableOnTP", true, "Disable when you teleport/chorus", this.autoDisable);
   public final BooleanSetting disableWhenDone = new BooleanSetting("DisableWhenDone", false, "Disable when done filling holes", this.autoDisable);
   private int aa = 0;
   private int ab = 0;
   private Timer ac = new Timer();
   private ArrayList<Placement> ad = new ArrayList();
   private List<BlockPos> ae = new ArrayList();
   private BlockInteraction af = null;
   private final Vector3d ag = new Vector3d(0.0, 0.0, 0.0);
   private final Vector3d ah = new Vector3d(0.0, 0.0, 0.0);

   public HoleFill() {
      super("HoleFill", "Fills holes around you", Category.Combat);
      this.addSettings(this.render);
   }

   @Override
   public void onEnable() {
      if (!MinecraftUtils.isClientActive()) {
         this.setEnabled(false);
      } else {
         this.ac.reset();
         this.ae.clear();
         this.af = null;
      }
   }

   @Override
   public String method1322() {
      return Integer.toString(this.ab);
   }

   @EventHandler
   public void method2071(Render3DEvent event) {
      if (this.render.method419()) {
         for (Placement var6 : this.ad) {
            PlaceRender.INSTANCE.method2015(event, var6, this.fillColor.method1362(), this.outlineColor.method1362());
         }
      }
   }

   @EventHandler
   public void method2042(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket
         && this.disableOnTP.method419()
         && MinecraftUtils.isClientActive()
         && mc.player
               .getPos()
               .distanceTo(
                  new Vec3d(
                     ((PlayerPositionLookS2CPacket)event.packet).getX(),
                     ((PlayerPositionLookS2CPacket)event.packet).getY(),
                     ((PlayerPositionLookS2CPacket)event.packet).getZ()
                  )
               )
            > 1.0) {
         this.toggle();
      }
   }

   @EventHandler(
      priority = 65
   )
   public void method1885(ACRotationEvent event) {
      if (!event.method1018(this.InteractionMode.method461(), this.rotate.method419())) {
         this.ad.removeIf(HoleFill::lambda$onRotate$0);
         if (this.disableOnJump.method419() && mc.player.getVelocity().y > 0.0 && !mc.player.isOnGround()) {
            this.toggle();
         } else if (this.disableWhenDone.method419() && this.ac.hasElapsed(650.0)) {
            this.toggle();
         } else if (!this.onlyWhileSneaking.method419() || mc.player.input.sneaking) {
            if (!Options.method477(this.multiTask.method419())) {
               if (this.aa < this.interval.method434()) {
                  this.aa++;
               } else if (this.method2010() != -1) {
                  this.ae = this.method2032();
                  this.ab = this.ae.size();
                  this.ae = (List<BlockPos>)this.ae.stream().sorted(Comparator.comparing(HoleFill::lambda$onRotate$1)).collect(Collectors.toList());
                  if (this.ab != 0) {
                     int var5 = 0;
                     Class2811.field108 = this.wallsRange.method423();
                     Class2811.field108 = this.wallsRange.method423();

                     while (var5 < this.ae.size()) {
                        BlockPos var6 = (BlockPos)this.ae.get(var5);
                        var5++;
                        if (!(
                           EntityUtil.method2144(mc.player)
                                 .distanceTo(new Vec3d((double)var6.getX() + 0.5, (double)var6.getY() + 0.5, (double)var6.getZ() + 0.5))
                              > this.range.getValue() + 0.5
                        )) {
                           int var7 = this.method2010();
                           if (var7 == -1) {
                              break;
                           }

                           if (Class2784.method2101(var6)) {
                              BlockInteraction var8 = BlockInteraction.method2272(var6)
                                 .method2276(this.strictDirection.method419())
                                 .method2275(this.raycast.method419());
                              if (var8.method2279() != null) {
                                 this.af = var8;
                                 if (this.rotate.method419()) {
                                    float[] var9 = EntityUtil.method2146(var8.method2279().getPos());
                                    event.method1021(true);
                                    event.yaw = var9[0];
                                    event.pitch = var9[1];
                                 }
                                 break;
                              }
                           }
                        }
                     }

                     Class2811.field108 = 0.0F;
                  }
               }
            }
         }
      }
   }

   @EventHandler(
      priority = 65
   )
   public void method1883(RotationEvent event) {
      if (!Options.method477(this.multiTask.method419()) && !event.method554(RotationMode.Sequential)) {
         if (this.af != null) {
            int var5 = 0;
            Vec3d var6 = mc.player.getEyePos();
            int var7 = this.method2010();
            if (var7 != -1) {
               while (this.af != null && this.af.method2279() != null) {
                  if (this.rotate.method419()) {
                     Vec3d var8 = var6.add(RotationHandler.method1954().multiply(this.range.getValue()));
                     if (this.raycast.method419() && var6.distanceTo(this.af.method2279().getPos()) > (double)this.wallsRange.method423().floatValue()) {
                        BlockHitResult var11 = Class5924.method98(var6, var8);
                        if (!Class2784.method5444(var11, this.af.method2279())) {
                           break;
                        }
                     } else {
                        Box var9 = new Box(this.af.method2279().getBlockPos());
                        if (!var9.intersects(var6, var8)) {
                           break;
                        }
                     }
                  }

                  event.method556(
                     this, this.placeMode.method461(), this.swapMode.method461(), 70, var7, this.af.method2279(), var7 == -2 ? Hand.OFF_HAND : Hand.MAIN_HAND
                  );
                  if (this.placeMode.method461() == PlaceMode.Vanilla) {
                     Class2784.method1801(this.af.method2278());
                  }

                  if (this.render.method419()) {
                     this.ad.add(PlaceRender.INSTANCE.method2017(this.af.method2278()));
                  }

                  this.af = null;
                  if (++var5 >= this.placeRate.method434()) {
                     break;
                  }

                  ACRotationEvent var10 = ACRotationEvent.method1016(this.InteractionMode.method461(), RotationHandler.method215(), RotationHandler.method520());
                  this.method1885(var10);
                  if (var10.method1022() && this.rotate.method419()) {
                     if (!mc.player.isOnGround()) {
                        break;
                     }

                     ((IClientPlayerEntity)mc.player).boze$sendMovementPackets(var10.yaw, var10.pitch);
                  }
               }

               ((ClientPlayerInteractionManagerAccessor)mc.interactionManager).callSyncSelectedSlot();
               this.aa = 0;
            }
         }
      }
   }

   @EventHandler
   public void method1877(PlayerPositionEvent event) {
      if (this.onStep.method419() && Step.INSTANCE.isEnabled()) {
         this.setEnabled(false);
      }
   }

   private List<BlockPos> method2032() {
      List var4 = null;
      if (this.proximity.method419()) {
         var4 = (List)mc.world.getPlayers().stream().filter(this::method1617).collect(Collectors.toList());
      }

      ArrayList var5 = new ArrayList();
      BlockPos var6 = mc.player.getBlockPos();
      int var7 = (int)Math.ceil(this.range.getValue() + 1.0);
      int var8 = this.vRange.method434();

      for (int var9 = var6.getX() - var7; var9 < var6.getX() + var7; var9++) {
         for (int var10 = var6.getY() - var8; var10 < var6.getY() + var8; var10++) {
            for (int var11 = var6.getZ() - var7; var11 < var6.getZ() + var7; var11++) {
               BlockPos var12 = new BlockPos(var9, var10, var11);
               if (Class5914.method5504(var12, this.double.method419()) && mc.world.getBlockState(var12).getFluidState().isEmpty()) {
                  if (this.proximity.method419()) {
                     this.ah.set((double)var12.getX() + 0.5, (double)var12.getY() + 1.0, (double)var12.getZ() + 0.5);

                     for (PlayerEntity var14 : var4) {
                        Class5920.method52(var14, this.extrapolation.method434(), this.ag);
                        if (this.ag.getDistance(this.ah.x, this.ag.y, this.ah.z) <= this.hradius.getValue()
                           && Math.abs(this.ag.y - this.ah.y) <= this.vradius.getValue()) {
                           var5.add(var12);
                           break;
                        }
                     }
                  } else {
                     var5.add(var12);
                  }
               }
            }
         }
      }

      return var5;
   }

   private boolean method1617(PlayerEntity var1) {
      if (var1 == mc.player) {
         return false;
      } else if (var1 instanceof FakePlayerEntity) {
         return false;
      } else if (Friends.method2055(var1)) {
         return false;
      } else if ((double)var1.distanceTo(mc.player) > this.range.getValue() + 6.0) {
         return false;
      } else if (Class5924.method77(true, var1)) {
         return false;
      } else {
         BlockPos var5 = BlockPos.ofFloored(mc.player.getPos());
         return !mc.world.getBlockState(var5).isSolidBlock(mc.world, var5);
      }
   }

   private int method2010() {
      int var4 = -1;
      ItemStack var5 = mc.player.getMainHandStack();
      if (var5 != ItemStack.EMPTY && var5.getItem() instanceof BlockItem) {
         Block var6 = ((BlockItem)var5.getItem()).getBlock();
         if (this.method2088(var6)) {
            var4 = mc.player.getInventory().selectedSlot;
         }
      }

      ItemStack var10 = mc.player.getOffHandStack();
      if (var10 != ItemStack.EMPTY && var10.getItem() instanceof BlockItem) {
         Block var7 = ((BlockItem)var10.getItem()).getBlock();
         if (this.method2088(var7)) {
            var4 = -2;
         }
      }

      if (var4 == -1) {
         for (int var11 = 0; var11 < 9; var11++) {
            ItemStack var8 = mc.player.getInventory().getStack(var11);
            if (var8 != ItemStack.EMPTY && var8.getItem() instanceof BlockItem) {
               Block var9 = ((BlockItem)var8.getItem()).getBlock();
               if (this.method2088(var9)) {
                  var4 = var11;
                  break;
               }
            }
         }
      }

      return var4;
   }

   private boolean method2088(Block var1) {
      return var1 == Blocks.OBSIDIAN && this.obsidian.method419() || var1 == Blocks.COBWEB && this.webs.method419() || this.other.method419();
   }

   private static Double lambda$onRotate$1(BlockPos var0) {
      return EntityUtil.method2144(mc.player).distanceTo(new Vec3d((double)var0.getX() + 0.5, (double)var0.getY() + 0.5, (double)var0.getZ() + 0.5));
   }

   private static boolean lambda$onRotate$0(Placement var0) {
      return System.currentTimeMillis() - var0.method1159() > (long)PlaceRender.method2010();
   }
}
