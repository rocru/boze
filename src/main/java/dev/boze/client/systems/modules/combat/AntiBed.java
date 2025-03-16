package dev.boze.client.systems.modules.combat;

import dev.boze.client.enums.PlacePosition;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.PlaceAction;
import dev.boze.client.utils.Timer;
import mapped.Class2811;
import mapped.Class2812;
import mapped.Class5914;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class AntiBed extends Module {
   public static final AntiBed INSTANCE = new AntiBed();
   private final BooleanSetting multitask = new BooleanSetting("MultiTask", false, "Multi Task");
   private final BooleanSetting rotate = new BooleanSetting("Rotate", true, "Rotate");
   private final BooleanSetting swing = new BooleanSetting("Swing", true, "Swing");
   private final IntSetting maxActions = new IntSetting("MaxActions", 1, 1, 3, 1, "Max actions per tick");
   private final IntSetting interval = new IntSetting("Interval", 0, 0, 5, 1, "Action interval");
   private final BooleanSetting strictDirection = new BooleanSetting("StrictDirection", true, "Strict Direction");
   private final BooleanSetting spamPlace = new BooleanSetting("Spam", false, "Spam place webs");
   private final BooleanSetting smartPlace = new BooleanSetting("Smart", false, "Only place webs when a dangerous bed is nearby");
   private final MinMaxSetting range = new MinMaxSetting("Range", 4.0, 0.0, 6.0, 0.5, "Smart range", this.smartPlace);
   private final BooleanSetting placeOnlyInHoles = new BooleanSetting("OnlyHoles", true, "Only place when in holes");
   private final BooleanSetting placeOnlyWhenStill = new BooleanSetting("OnlyStill", true, "Only place when standing still");
   private final BooleanSetting placeWebAtFeet = new BooleanSetting("Feet", false, "Place strings at feet");
   private final BooleanSetting placeWebAtHead = new BooleanSetting("Head", true, "Place strings at head");
   private final BooleanSetting placeWebAboveHead = new BooleanSetting("Above", false, "Place strings above head");
   private PlacePosition placePosition = PlacePosition.Above;
   private int field2457 = 10;
   private Timer field2458 = new Timer();

   public AntiBed() {
      super("AntiBed", "Prevents bed placements by placing strings in the way", Category.Combat);
   }

   @Override
   public void onEnable() {
      this.field2457 = 10;
   }

   @EventHandler
   public void method1394(PacketBundleEvent event) {
      if (event.packet instanceof BlockEntityUpdateS2CPacket && ((BlockEntityUpdateS2CPacket)event.packet).getBlockEntityType() == BlockEntityType.BED) {
         BlockPos var5 = ((BlockEntityUpdateS2CPacket)event.packet).getPos();
         Vec3d var6 = new Vec3d((double)var5.getX(), (double)var5.getY(), (double)var5.getZ());
         if (var6.distanceTo(mc.player.getPos()) <= this.range.getValue()) {
            this.field2458.reset();
         }
      }
   }

   @EventHandler(
      priority = 65
   )
   public void method1395(MovementEvent event) {
      if (!event.method1022()) {
         this.field2457++;
         if (this.field2457 > this.interval.method434()) {
            if (!Options.method477(this.multitask.method419())) {
               if (!this.placeOnlyWhenStill.method419() || !Class5924.method2115()) {
                  int var5 = 0;
                  int var6 = this.rotate.method419() && Class5924.method2115() ? 1 : this.maxActions.method434();
                  BlockPos var7 = mc.player.getBlockPos();
                  if (!this.placeOnlyInHoles.method419() || Class5924.method76(true)) {
                     if (this.smartPlace.method419()) {
                        for (BlockEntity var9 : Class5914.method19()) {
                           if (var9 instanceof BedBlockEntity) {
                              BlockPos var10 = var9.getPos();
                              Vec3d var11 = new Vec3d((double)var10.getX(), (double)var10.getY(), (double)var10.getZ());
                              if (var11.distanceTo(mc.player.getPos()) <= this.range.getValue()) {
                                 this.field2458.reset();
                                 break;
                              }
                           }
                        }

                        if (this.field2458.hasElapsed(1000.0)) {
                           return;
                        }
                     }

                     Class2811.field99 = true;
                     if (this.placeWebAtFeet.method419()
                        && (
                           this.spamPlace.method419() && this.placePosition != PlacePosition.Feet
                              || Class2812.method5504(var7, this.strictDirection.method419())
                        )) {
                        PlaceAction var12 = this.method1396(var7);
                        if (var12 != null) {
                           event.method1074(new ActionWrapper(var12));
                           this.placePosition = PlacePosition.Feet;
                           var5++;
                        }
                     }

                     if (this.placeWebAtHead.method419()
                        && var5 < var6
                        && (
                           this.spamPlace.method419() && this.placePosition != PlacePosition.Head
                              || Class2812.method5504(var7.up(), this.strictDirection.method419())
                        )) {
                        PlaceAction var13 = this.method1396(var7.up());
                        if (var13 != null) {
                           event.method1074(new ActionWrapper(var13));
                           this.placePosition = PlacePosition.Head;
                           var5++;
                        }
                     }

                     if (this.placeWebAboveHead.method419()
                        && var5 < var6
                        && (
                           this.spamPlace.method419() && this.placePosition != PlacePosition.Above
                              || Class2812.method5504(var7.up(2), this.strictDirection.method419())
                        )) {
                        PlaceAction var14 = this.method1396(var7.up(2));
                        if (var14 != null) {
                           event.method1074(new ActionWrapper(var14));
                           this.placePosition = PlacePosition.Above;
                           var5++;
                        }
                     }

                     if (var5 > 0) {
                        this.field2457 = 0;
                     }

                     Class2811.field99 = false;
                  }
               }
            }
         }
      }
   }

   private PlaceAction method1396(BlockPos var1) {
      Class2811.field103 = true;
      PlaceAction var2 = Class2812.method5500(Items.STRING, var1, this.rotate.method419(), this.swing.method419(), this.strictDirection.method419());
      Class2811.field103 = false;
      return var2;
   }
}
