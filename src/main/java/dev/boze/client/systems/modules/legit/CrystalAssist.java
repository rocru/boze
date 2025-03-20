package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.CrystalAssistAimPoint;
import dev.boze.client.enums.CrystalAssistPriority;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.RotationHelper;
import dev.boze.client.utils.Timer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import mapped.Class1202;
import mapped.Class3069;
import mapped.Class5917;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CrystalAssist extends Module {
   public static final CrystalAssist INSTANCE = new CrystalAssist();
   private final BooleanSetting field2771 = new BooleanSetting("AntiSuicide", true, "Try to avoid aiming at crystals that may kill you");
   private final BooleanSetting field2772 = new BooleanSetting("OnlyOwn", false, "Only aim at crystals that you placed");
   private final EnumSetting<CrystalAssistAimPoint> field2773 = new EnumSetting<>(
           "AimPoint", CrystalAssistAimPoint.Closest, "Aim point to use"
   );
   private final BooleanSetting field2774 = new BooleanSetting("StopOverCrystal", false, "Stop assisting when looking at crystal");
   private final BooleanSetting field2775 = new BooleanSetting("OnlyWhenClicking", true, "Only assist when clicking attack key");
   private final IntSetting field2776 = new IntSetting("Ticks", 5, 1, 40, 1, "Ticks to assist for after clicking", this.field2775::getValue);
   private final MinMaxSetting field2777 = new MinMaxSetting("Range", 3.0, 1.0, 7.0, 0.1, "Range to target crystals within");
   private final BooleanSetting field2778 = new BooleanSetting("ThroughWalls", false, "Target through walls");
   private final MinMaxSetting field2779 = new MinMaxSetting("BoxScale", 0.8, 0.1, 1.0, 0.1, "Scale of the box to target within");
   private final MinMaxDoubleSetting field2780 = new MinMaxDoubleSetting("Speed", new double[]{1.0, 2.0}, 0.1, 10.0, 0.1, "Assist speed");
   private final MinMaxSetting field2781 = new MinMaxSetting("MaxDelta", 5.0, 0.01, 10.0, 0.02, "Max mouse delta change per poll");
   private final BooleanSetting field2782 = new BooleanSetting("DontResist", false, "Don't resist mouse movement in opposite directions");
   private final BooleanSetting field2783 = new BooleanSetting("Vertical", true, "Assist vertical rotation");
   private final IntSetting field2784 = new IntSetting("FOV", 180, 1, 180, 1, "Maximum FOV to target within");
   private final EnumSetting<CrystalAssistPriority> field2785 = new EnumSetting<CrystalAssistPriority>(
           "Priority", CrystalAssistPriority.Distance, "The priority to target crystals with"
   );
   private final Comparator<Entity> field2786 = Comparator.comparing(this::lambda$new$0);
   private final Timer field2787 = new Timer();
   private final ConcurrentHashMap<BlockPos, Long> field2788 = new ConcurrentHashMap();

   public CrystalAssist() {
      super("CrystalAssist", "Assists in targeting crystals", Category.Legit);
   }

   @EventHandler
   public void method1596(PrePacketSendEvent event) {
      if (this.field2772.getValue()
              && event.packet instanceof PlayerInteractBlockC2SPacket var5
              && mc.player.getStackInHand(var5.getHand()).getItem() == Items.END_CRYSTAL) {
         this.field2788.put(var5.getBlockHitResult().getBlockPos(), System.currentTimeMillis());
      }
   }

   @EventHandler
   public void method1597(MovementEvent event) {
      if (this.field2772.getValue()) {
         this.field2788.entrySet().removeIf(CrystalAssist::lambda$onSendMovementPackets$1);
      }
   }

   @EventHandler
   public void method1598(MouseUpdateEvent event) {
      if (!MinecraftUtils.isClientActive() || event.method1022()) {
         return;
      }
      if (CrystalAssist.mc.options.attackKey.isPressed()) {
         this.field2787.reset();
      }
      if (this.field2775.getValue() && this.field2787.hasElapsed(this.field2776.getValue() * 50)) {
         return;
      }
      RotationHelper rotationHelper = new RotationHelper(CrystalAssist.mc.player);
      Entity targetEntity = this.method1599();
      if (targetEntity == null) {
         return;
      }
      if (this.field2774.getValue()) {
         HitResult crosshairTarget = CrystalAssist.mc.crosshairTarget;
         if (crosshairTarget instanceof EntityHitResult entityHitResult) {
            if (entityHitResult.getEntity() == targetEntity) {
               return;
            }
         }
      }
      Box box = targetEntity.getBoundingBox()
              .expand(targetEntity.getTargetingMargin())
              .contract(1 - this.field2779.getValue());
      Vec3d vec = box.getCenter();
      switch (this.field2773.getValue().ordinal()) {
         case 0 -> vec = Class5917.method136(box, rotationHelper.method1954(), CrystalAssist.mc.player.getEyePos());
         case 1 -> vec = Class5917.method123(box, CrystalAssist.mc.player.getEyePos());
         case 2 -> vec = Class5917.method34(box);
         case 3 -> {
            Vec3d eyePos = targetEntity.getEyePos();
            Box tempBox = new Box(eyePos.x - 0.25, eyePos.y - 0.25, eyePos.z - 0.25, eyePos.x + 0.25, eyePos.y, eyePos.z + 0.25);
            vec = Class5917.method123(tempBox, CrystalAssist.mc.player.getEyePos());
         }
      }
      RotationHelper targetRotationHelper = Class1202.method2391(CrystalAssist.mc.player.getEyePos(), vec);
      if (targetRotationHelper.method605(rotationHelper) > (float) (this.field2784.getValue() * 1.417)) {
         return;
      }
      RotationHelper adjustedRotationHelper = rotationHelper.method606(targetRotationHelper).method1600();
      Pair<Double, Double>[] pairs = RotationHelper.method614(adjustedRotationHelper);
      double deltaXMultiplier = MathHelper.clamp(
              pairs[0].getLeft(),
              -this.field2781.getValue() * 10.0,
              this.field2781.getValue() * 10.0
      );
      if (this.field2782.getValue() && event.deltaX * deltaXMultiplier >= 0) {
         event.deltaX += deltaXMultiplier;
         event.method1021(true);
      }
      if (this.field2783.getValue()) {
         double deltaYMultiplier = MathHelper.clamp(
                 pairs[0].getRight(),
                 -this.field2781.getValue() * 10.0,
                 this.field2781.getValue() * 10.0
         );
         if (this.field2782.getValue() && event.deltaY * deltaYMultiplier >= 0) {
            event.deltaY += deltaYMultiplier;
            event.method1021(true);
         }
      }
   }

   private Entity method1599() {
      ArrayList<Entity> var4 = new ArrayList<>();

      for (Entity var6 : mc.world.getEntities()) {
         if (this.method1601(var6)
                 && !((double)var6.distanceTo(mc.player) > this.field2777.getValue() + 1.0)
                 && (this.field2778.getValue() || mc.player.canSee(var6))) {
            var4.add(var6);
         }
      }

      return var4.stream().min(this.field2786).orElse(null);
   }

   private RotationHelper method1600() {
      return new RotationHelper(mc.player);
   }

   private boolean method1601(Entity var1) {
      if (var1 instanceof EndCrystalEntity var5) {
         if (this.field2771.getValue()) {
            double var6 = Class3069.method6003(mc.player, var5.getPos(), 0, null, false);
            if (var6 >= (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
               return false;
            }
         }

         return !this.field2772.getValue() || this.field2788.containsKey(var5.getBlockPos().down());
      } else {
         return false;
      }
   }

   private static boolean lambda$onSendMovementPackets$1(Entry var0) {
      return System.currentTimeMillis() - (Long)var0.getValue() > 10000L;
   }

   private Double lambda$new$0(Entity param1) {
      CrystalAssistPriority priority = this.field2785.getValue();

      switch (priority.ordinal()) {
         case 0 -> {
            Vec3d eyePos = CrystalAssist.mc.player.getEyePos();
            Box expandedBox = param1.getBoundingBox().expand(param1.getTargetingMargin());
            Vec3d targetPos = Class5917.method34(expandedBox);
            return eyePos.squaredDistanceTo(targetPos);
         }
         case 1 -> {
            Vec3d eyePos = CrystalAssist.mc.player.getEyePos();
            Vec3d targetPos = Class5917.method34(param1.getBoundingBox());
            RotationHelper helper = Class1202.method2391(eyePos, targetPos);
            RotationHelper currentHelper = this.method1600();
            return (double) helper.method605(currentHelper);
         }
         default -> {
            return 0.0;
         }
      }
   }
}