package dev.boze.client.systems.modules.movement;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ClickFlyMode;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.*;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.jumptable.nf;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.FreeCam;
import dev.boze.client.systems.pathfinding.Path;
import dev.boze.client.systems.pathfinding.PathBuilder;
import dev.boze.client.systems.pathfinding.PathFinder;
import dev.boze.client.systems.pathfinding.PathRules;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.BlockItem;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class ClickFly extends Module {
   public static final ClickFly INSTANCE = new ClickFly();
   private final BooleanSetting field3180 = new BooleanSetting("Render", true, "Render the block you're looking at");
   private final ColorSetting field3181 = new ColorSetting("Color", new BozeDrawColor(1685820627), "Color for fill", this.field3180);
   private final ColorSetting field3182 = new ColorSetting("Outline", new BozeDrawColor(-8678189), "Color for outline", this.field3180);
   private final BooleanSetting field3183 = new BooleanSetting("RenderDest", true, "Render the block you're flying to");
   private final ColorSetting field3184 = new ColorSetting("Color", new BozeDrawColor(1687475067), "Color for fill", this.field3183);
   private final ColorSetting field3185 = new ColorSetting("Outline", new BozeDrawColor(-7023749), "Color for outline", this.field3183);
   private final MinMaxSetting field3186 = new MinMaxSetting("MaxDist", 64.0, 4.5, 256.0, 0.5, "Max distance to teleport to");
   private final MinMaxSetting field3187 = new MinMaxSetting("Speed", 5.0, 3.0, 10.0, 0.01, "Maximum speed to move at\nAround 5 works consistently on 2b2t\n");
   private final EnumSetting<ClickFlyMode> field3188 = new EnumSetting<ClickFlyMode>(
      "Mode",
      ClickFlyMode.RightClick,
      "Mode to use for ClickFly\n - Once: Teleports you to the block you're looking at on enable\n - RightClick: Teleports you to the block you're looking at when you right click\n - Bind: Teleports you to the block you're looking at when you press the bind key\n"
   );
   private final BindSetting field3189 = new BindSetting("Bind", Bind.fromKey(342), "Bind for ClickFly", this::lambda$new$0);
   private final PathRules field3190 = new PathRules(true, true, true, false);
   private PathFinder field3191 = null;
   private boolean field3192 = false;
   private BlockPos field3193 = null;
   private Path field3194 = null;

   private ClickFly() {
      super(
         "ClickFly",
         "Automatically flies to the block you're looking at\nWon't work on most servers, apart from:\n - Servers with no anti-cheat\n - Servers where Trident GrimDisabler works\n",
         Category.Movement
      );
      this.addSettings(this.field3180);
   }

   private void method1801(BlockPos var1) {
      this.field3191 = new PathFinder(var1, this.field3190);
   }

   @Override
   public void onEnable() {
      this.field3191 = null;
      if (this.field3188.getValue() == ClickFlyMode.Once) {
         this.field3192 = true;
      } else {
         this.field3192 = false;
      }

      this.field3194 = null;
      this.field3193 = null;
   }

   @EventHandler
   public void method1802(Render3DEvent event) {
      if (this.field3194 != null) {
         if (this.field3183.getValue() && this.field3193 != null) {
            event.field1950.method1273(new Box(this.field3193), this.field3184.getValue(), this.field3185.getValue(), ShapeMode.Full, 0);
         }
      } else if (this.field3180.getValue()) {
         HitResult var5 = this.method1807();
         if (var5 != null && var5 instanceof BlockHitResult var6) {
            this.method1808(new Box(var6.getBlockPos()), var6.getSide(), event.field1950);
         }
      }
   }

   @EventHandler
   public void method1803(PlayerMoveEvent event) {
      if (this.field3194 == null) {
         if (this.field3192) {
            HitResult var5 = this.method1807();
            if (var5 != null && var5 instanceof BlockHitResult var6) {
               BlockPos var7 = var6.getBlockPos().offset(var6.getSide());
               this.method1801(var7);
               this.field3193 = var7;
               this.field3192 = false;
            }
         }

         if (this.field3191 == null) {
            if (this.field3188.getValue() == ClickFlyMode.Once) {
               this.setEnabled(false);
            }

            return;
         }

         this.field3191.method2098();
         if (this.field3191.method2118()) {
            ChatInstance.method743(this.getName(), "Unable to find path");
            this.field3191 = null;
            if (this.field3188.getValue() == ClickFlyMode.Once) {
               this.setEnabled(false);
            }

            return;
         }

         if (this.field3191.method2117()) {
            this.field3194 = PathBuilder.method616(this.field3191.method2120(), 3.0, this.field3187.getValue());
            this.field3191 = null;
         }
      }

      if (this.field3194 != null) {
         Vec3d var8 = this.field3194.method2094();
         if (var8 == null) {
            this.field3194 = null;
            if (this.field3188.getValue() == ClickFlyMode.Once) {
               this.setEnabled(false);
            }
         } else {
            event.vec3 = var8;
            mc.player.setVelocity(Vec3d.ZERO);
            event.field1892 = true;
         }
      }
   }

   @EventHandler
   public void method1804(PacketBundleEvent event) {
      if (event.packet instanceof PlayerPositionLookS2CPacket && this.field3194 != null) {
         this.field3194 = null;
         if (this.field3188.getValue() == ClickFlyMode.Once) {
            this.setEnabled(false);
         }
      }
   }

   @EventHandler
   public void method1805(KeyEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.action == KeyAction.Press && mc.currentScreen == null) {
            if (this.field3188.getValue() == ClickFlyMode.Bind && this.field3189.getValue().matches(true, event.key)) {
               this.field3192 = true;
            }
         }
      }
   }

   @EventHandler
   public void method1806(MouseButtonEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.action == KeyAction.Press && mc.currentScreen == null) {
            if (this.field3188.getValue() == ClickFlyMode.Bind && this.field3189.getValue().matches(true, event.button)) {
               this.field3192 = true;
            } else if (this.field3188.getValue() == ClickFlyMode.RightClick && event.button == 1) {
               try {
                  if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
                     return;
                  }

                  if (mc.player.getOffHandStack().getItem() instanceof BlockItem) {
                     return;
                  }

                  if (mc.player.getMainHandStack().getUseAction() != UseAction.NONE) {
                     return;
                  }

                  if (mc.player.getOffHandStack().getUseAction() != UseAction.NONE) {
                     return;
                  }

                  this.field3192 = true;
               } catch (Exception var6) {
               }
            }
         }
      }
   }

   private HitResult method1807() {
      Vec3d var4 = FreeCam.INSTANCE.isEnabled()
         ? new Vec3d(FreeCam.INSTANCE.field3540, FreeCam.INSTANCE.field3541, FreeCam.INSTANCE.field3542)
         : mc.player.getCameraPosVec(1.0F);
      Vec3d var5 = FreeCam.INSTANCE.isEnabled() ? FreeCam.INSTANCE.method1954() : mc.player.getRotationVec(1.0F);
      Vec3d var6 = var4.add(var5.x * this.field3186.getValue(), var5.y * this.field3186.getValue(), var5.z * this.field3186.getValue());
      return mc.world.raycast(new RaycastContext(var4, var6, ShapeType.OUTLINE, FluidHandling.NONE, mc.player));
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method1808(Box var1, Direction var2, Renderer3D var3) {
      if (var2 != null) {
         switch (nf.field2119[var2.ordinal()]) {
            case 1:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.minY, var1.maxZ);
               break;
            case 2:
               var1 = new Box(var1.minX, var1.maxY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 3:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.minZ);
               break;
            case 4:
               var1 = new Box(var1.minX, var1.minY, var1.maxZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 5:
               var1 = new Box(var1.maxX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ);
               break;
            case 6:
               var1 = new Box(var1.minX, var1.minY, var1.minZ, var1.minX, var1.maxY, var1.maxZ);
         }
      }

      var3.method1273(var1, this.field3181.getValue(), this.field3182.getValue(), ShapeMode.Full, 0);
   }

   private boolean lambda$new$0() {
      return this.field3188.getValue() == ClickFlyMode.Bind;
   }
}
