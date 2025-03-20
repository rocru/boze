package dev.boze.client.systems.modules.legit;

import dev.boze.client.enums.ClickMethod;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.events.HandleInputEvent;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.MouseUpdateEvent;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.PrePacketSendEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.jumptable.na;
import dev.boze.client.mixin.KeyBindingAccessor;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntArraySetting;
import dev.boze.client.settings.MinMaxDoubleSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.click.ClickManager;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import mapped.Class3069;
import mapped.Class5924;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class CrystalTrigger extends Module {
   public static final CrystalTrigger INSTANCE = new CrystalTrigger();
   private final FloatSetting field686 = new FloatSetting("Jitter", 0.0F, 0.0F, 2.0F, 0.01F, "Mouse jitter amount");
   private final BooleanSetting field687 = new BooleanSetting("AutoSwap", true, "Automatically swap");
   private final BooleanSetting field688 = new BooleanSetting("CrystalOnly", false, "Only break/place when holding a crystal", this::lambda$new$0);
   private final BooleanSetting field689 = new BooleanSetting("Break", true, "Break crystals (left click on crystals)");
   private final BooleanSetting field690 = new BooleanSetting("OnlyOwn", false, "Only break crystals that you placed", this.field689);
   private final BooleanSetting field691 = new BooleanSetting("OnlyWhenHolding", false, "Only break when holding left click", this.field689);
   private final EnumSetting<ClickMethod> field692 = new EnumSetting<ClickMethod>("BreakClicking", ClickMethod.Normal, "Break (left) click mode", this.field689);
   private final IntArraySetting field693 = new IntArraySetting("CPS", new int[]{6, 10}, 1, 20, 1, "Left clicks per second", this.field689);
   private final FloatSetting field694 = new FloatSetting(
      "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$1, this.field689
   );
   private final IntArraySetting field695 = new IntArraySetting("SwapDelay", new int[]{4, 6}, 0, 20, 1, "Delay after swapping to break", this.field689);
   private final MinMaxDoubleSetting field696 = new MinMaxDoubleSetting(
      "OnsetDelay", new double[]{1.0, 3.0}, 0.0, 10.0, 0.01, "Delay after looking at crystal to start trying to break", this.field689
   );
   private final MinMaxDoubleSetting field697 = new MinMaxDoubleSetting(
      "OffsetDelay", new double[]{1.0, 3.0}, 0.0, 10.0, 0.01, "Delay after looking away from crystal to stop trying to break", this.field689
   );
   private final BooleanSetting field698 = new BooleanSetting("Place", true, "Place crystals (right click on obsidian)");
   private final BooleanSetting field699 = new BooleanSetting("OnlyWhenHolding", false, "Only place when holding right click", this.field698);
   private final EnumSetting<ClickMethod> field700 = new EnumSetting<ClickMethod>(
      "PlaceClicking", ClickMethod.Normal, "Place (right) click mode", this.field698
   );
   private final IntArraySetting field701 = new IntArraySetting("CPS", new int[]{4, 8}, 1, 20, 1, "Right clicks per second", this.field698);
   private final FloatSetting field702 = new FloatSetting(
      "CooldownOffset", 0.0F, -2.5F, 2.5F, 0.05F, "The offset for vanilla clicking", this::lambda$new$2, this.field698
   );
   private final IntArraySetting field703 = new IntArraySetting("SwapDelay", new int[]{2, 5}, 0, 20, 1, "Delay after swapping to place", this.field698);
   private final MinMaxDoubleSetting field704 = new MinMaxDoubleSetting(
      "OnsetDelay", new double[]{1.0, 3.0}, 0.0, 10.0, 0.01, "Delay after looking at crystal to start trying to place", this.field698
   );
   private final MinMaxDoubleSetting field705 = new MinMaxDoubleSetting(
      "OffsetDelay", new double[]{1.0, 3.0}, 0.0, 10.0, 0.01, "Delay after looking away from crystal to stop trying to place", this.field698
   );
   private BindSetting field706 = new BindSetting("Obsidian", Bind.create(), "Place obsidian");
   private final BooleanSetting field707 = new BooleanSetting("AntiSuicide", true, "Don't break/place crystals if you'll die");
   private final MinMaxSetting field708 = new MinMaxSetting("MaxSelfDmg", 20.0, 0.0, 36.0, 0.01, "Max self damage to break/place");
   private final BooleanSetting field709 = new BooleanSetting("Targeting", false, "Do targeting");
   private final MinMaxSetting field710 = new MinMaxSetting("BreakMinDmg", 2.0, 0.0, 20.0, 0.01, "Min damage to break crystals", this.field709::getValue);
   private final MinMaxSetting field711 = new MinMaxSetting("PlaceMinDmg", 4.0, 0.0, 20.0, 0.01, "Min damage to place crystals", this.field709::getValue);
   private final SettingCategory field712 = new SettingCategory("Targets", "Entities to target", this.field709::getValue);
   private final BooleanSetting field713 = new BooleanSetting("Players", true, "Target players", this.field712);
   private final BooleanSetting field714 = new BooleanSetting("Friends", false, "Target friends", this.field712);
   private final BooleanSetting aa = new BooleanSetting("Animals", false, "Target animals", this.field712);
   private final BooleanSetting ab = new BooleanSetting("Monsters", false, "Target monsters", this.field712);
   private float ac = 0.0F;
   private final ClickManager ad = new ClickManager(this.field692, this.field693, this.field694);
   private final ClickManager ae = new ClickManager(this.field700, this.field701, this.field702);
   private boolean af = false;
   private Timer ag = new Timer();
   private long ah = 0L;
   private long ai = 0L;
   private Timer aj = new Timer();
   private Timer ak = new Timer();
   private long al = 0L;
   private long am = 0L;
   private Timer an = new Timer();
   private Timer ao = new Timer();
   private long ap = 0L;
   private long aq = 0L;
   private Timer ar = new Timer();
   private Timer as = new Timer();
   private long at = 0L;
   private final ConcurrentHashMap<BlockPos, Long> au = new ConcurrentHashMap();

   public CrystalTrigger() {
      super("CrystalTrigger", "Automatically left-and-right clicks for crystal pvp", Category.Legit);
   }

   @EventHandler
   public void method1853(PrePacketSendEvent event) {
      if (this.field690.getValue()
         && event.packet instanceof PlayerInteractBlockC2SPacket var5
         && mc.player.getStackInHand(var5.getHand()).getItem() == Items.END_CRYSTAL) {
         this.au.put(var5.getBlockHitResult().getBlockPos(), System.currentTimeMillis());
      }
   }

   @EventHandler
   public void method2041(MovementEvent event) {
      if (this.field690.getValue()) {
         this.au.entrySet().removeIf(CrystalTrigger::lambda$onSendMovementPackets$3);
      }
   }

   @Override
   public void onEnable() {
      this.ac = 0.0F;
      this.ad.method2172();
      this.ae.method2172();
      this.af = false;
      this.ah = (long)this.field695.method1376() * 50L;
      this.ai = (long)this.field703.method1376() * 50L;
   }

   @EventHandler
   public void method1944(KeyEvent event) {
      if (event.action == KeyAction.Press) {
         if (this.field706.method476().isKey() && this.field706.method476().getBind() == event.key) {
            this.af = true;
            this.as.reset();
         }
      }
   }

   @EventHandler
   public void method1812(MouseButtonEvent event) {
      if (event.action == KeyAction.Press) {
         if (!this.field706.method476().isKey() && this.field706.method476().getBind() == event.button) {
            this.af = true;
            this.as.reset();
         }
      }
   }

   @EventHandler(
      priority = 51
   )
   public void method1695(MouseUpdateEvent event) {
      if (this.ac > 0.0F && !event.method1022()) {
         double var5 = (double)(this.ac * this.field686.getValue()) * Math.random();
         double var7 = (double)(this.ac * this.field686.getValue()) * Math.random();
         if (Math.random() > 0.5) {
            var5 *= -1.0;
         }

         if (Math.random() > 0.5) {
            var7 *= -1.0;
         }

         event.deltaX += var5;
         event.deltaY += var7;
         event.method1021(true);
         this.ac = 0.0F;
      }
   }

   @EventHandler
   public void method1693(HandleInputEvent event) {
      if (this.field687.getValue()) {
         Object var5 = null;
         if (this.af) {
            var5 = Items.OBSIDIAN;
         } else {
            if (!this.field698.getValue()) {
               return;
            }

            if (mc.crosshairTarget == null
               || !(mc.crosshairTarget instanceof BlockHitResult var6)
               || mc.world.getBlockState(var6.getBlockPos()).getBlock() != Blocks.OBSIDIAN
                  && mc.world.getBlockState(var6.getBlockPos()).getBlock() != Blocks.BEDROCK
               || !this.method2101(var6.getBlockPos())) {
               return;
            }

            var5 = Items.END_CRYSTAL;
         }

         if (mc.player.getMainHandStack().getItem() != var5 && mc.player.getOffHandStack().getItem() != var5) {
            for (int var9 = 0; var9 < 9; var9++) {
               if (mc.player.getInventory().getStack(var9).getItem() == var5) {
                  ((KeyBindingAccessor)mc.options.hotbarKeys[var9]).setTimesPressed(1);
                  this.ag.reset();
                  this.ah = (long)this.field695.method1376() * 50L;
                  this.ai = (long)this.field703.method1376() * 50L;
                  break;
               }
            }
         }
      }
   }

   @EventHandler
   public void method1883(RotationEvent event) {
      if (!event.method554(RotationMode.Vanilla)) {
         if (mc.currentScreen != null && !(mc.currentScreen instanceof ClickGUI)) {
            this.ad.method2172();
            this.ae.method2172();
         } else {
            if (this.af) {
               if (!this.as.hasElapsed(1000.0)) {
                  if (mc.crosshairTarget instanceof BlockHitResult var5 && mc.world.getBlockState(var5.getBlockPos()).getBlock() == Blocks.OBSIDIAN) {
                     this.af = false;
                     return;
                  }

                  if (!this.ag.hasElapsed((double)this.ai)) {
                     return;
                  }

                  if (!mc.player.isUsingItem() && this.method1973()) {
                     this.method349(mc.options.useKey, this.ae, event, this.field699.getValue());
                     this.ar.reset();
                     this.at = (long)(Math.random() * 250.0 + 250.0);
                  }

                  return;
               }

               this.af = false;
            }

            if (this.field687.getValue()
               || !this.field688.getValue()
               || mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL
               || mc.player.getOffHandStack().getItem() == Items.END_CRYSTAL) {
               if (this.field689.getValue() && this.ag.hasElapsed((double)this.ah) && !mc.interactionManager.isBreakingBlock() && this.method1971()) {
                  this.method349(mc.options.attackKey, this.ad, event, this.field691.getValue());
               }

               if (this.field698.getValue() && this.ag.hasElapsed((double)this.ai) && !mc.player.isUsingItem() && this.method1972()) {
                  this.method349(mc.options.useKey, this.ae, event, this.field699.getValue());
               }
            }
         }
      }
   }

   private boolean method1971() {
      if (mc.crosshairTarget == null
         || !(mc.crosshairTarget instanceof EntityHitResult var4)
         || var4.getEntity() == null
         || !(var4.getEntity() instanceof EndCrystalEntity var5)
         || !this.method347(var5.getPos(), false)
         || this.field690.getValue() && !this.au.containsKey(var5.getBlockPos().down())) {
         this.aj.reset();
         this.al = (long)(this.field696.method1296() * 50.0);
         return !this.ak.hasElapsed((double)this.am);
      } else {
         this.ak.reset();
         this.am = (long)(this.field697.method1296() * 50.0);
         return this.aj.hasElapsed((double)this.al);
      }
   }

   private boolean method1972() {
      if (mc.player.getMainHandStack().getItem() != Items.END_CRYSTAL && mc.player.getOffHandStack().getItem() != Items.END_CRYSTAL) {
         return false;
      } else if (mc.crosshairTarget != null
         && mc.crosshairTarget instanceof BlockHitResult var4
         && (
            mc.world.getBlockState(var4.getBlockPos()).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(var4.getBlockPos()).getBlock() == Blocks.BEDROCK
         )
         && this.method2101(var4.getBlockPos())) {
         this.ao.reset();
         this.aq = (long)(this.field705.method1296() * 50.0);
         return this.an.hasElapsed((double)this.ap);
      } else {
         this.an.reset();
         this.ap = (long)(this.field704.method1296() * 50.0);
         return !this.ao.hasElapsed((double)this.aq);
      }
   }

   private boolean method1973() {
      if (!this.ar.hasElapsed((double)this.at)) {
         return false;
      } else if (mc.player.getMainHandStack().getItem() != Items.OBSIDIAN && mc.player.getOffHandStack().getItem() != Items.OBSIDIAN) {
         return false;
      } else if (mc.crosshairTarget instanceof BlockHitResult var4) {
         if (Class5924.method2101(var4.getBlockPos()) && !mc.player.isSneaking()) {
            return false;
         } else {
            this.ao.reset();
            this.aq = (long)(this.field705.method1296() * 50.0);
            return this.an.hasElapsed((double)this.ap);
         }
      } else {
         this.an.reset();
         this.ap = (long)(this.field704.method1296() * 50.0);
         return !this.ao.hasElapsed((double)this.aq);
      }
   }

   private boolean method347(Vec3d var1, boolean var2) {
      double var6 = Class3069.method6003(mc.player, var1, 0, null, true);
      if (this.field707.getValue() && var6 + 2.0 > (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount())) {
         return false;
      } else if (var6 > this.field708.getValue()) {
         return false;
      } else if (this.field709.getValue()) {
         for (LivingEntity var10 : this.method348(var1)) {
            if (Class3069.method6003(var10, var1, 0, null, true) > var2 ? this.field711.getValue() : this.field710.getValue()) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private boolean method2101(BlockPos var1) {
      return this.method347(new Vec3d((double)var1.getX() + 0.5, (double)var1.getY() + 1.0, (double)var1.getZ() + 0.5), true);
   }

   private List<LivingEntity> method348(Vec3d var1) {
      ArrayList var5 = new ArrayList();

      for (Entity var7 : mc.world.getEntities()) {
         if (this.method2055(var7) && var7.squaredDistanceTo(var1.x, var1.y, var1.z) <= 100.0) {
            var5.add((LivingEntity)var7);
         }
      }

      return var5;
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private boolean method2055(Entity var1) {
      if (var1 instanceof PlayerEntity) {
         if (var1 == mc.player) {
            return false;
         } else if (var1 instanceof FakePlayerEntity) {
            return false;
         } else if (Friends.method2055(var1)) {
            return this.field714.getValue();
         } else {
            return AntiBots.method2055(var1) ? false : this.field713.getValue();
         }
      } else {
         switch (na.field2116[var1.getType().getSpawnGroup().ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
               return this.aa.getValue();
            case 5:
               return this.ab.getValue();
            default:
               return false;
         }
      }
   }

   private void method349(KeyBinding var1, ClickManager var2, RotationEvent var3, boolean var4) {
      if (!var1.isPressed() && var4) {
         var2.method2172();
      } else {
         int var8 = var2.method2171();
         if (var8 > 0 && ((KeyBindingAccessor)var1).getTimesPressed() == 0) {
            ((KeyBindingAccessor)var1).setTimesPressed(var8);
            var3.method2142();
            if (this.field686.getValue() > 0.0F) {
               this.ac++;
            }
         }
      }
   }

   private static boolean lambda$onSendMovementPackets$3(Entry var0) {
      return System.currentTimeMillis() - (Long)var0.getValue() > 10000L;
   }

   private boolean lambda$new$2() {
      return this.field700.getValue() == ClickMethod.Vanilla;
   }

   private boolean lambda$new$1() {
      return this.field692.getValue() == ClickMethod.Vanilla;
   }

   private boolean lambda$new$0() {
      return !this.field687.getValue();
   }
}
