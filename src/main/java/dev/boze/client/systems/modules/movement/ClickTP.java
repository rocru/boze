package dev.boze.client.systems.modules.movement;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ClickTPMode;
import dev.boze.client.enums.KeyAction;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.KeyEvent;
import dev.boze.client.events.MouseButtonEvent;
import dev.boze.client.events.PostTickEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.jumptable.ng;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.MinecraftUtils;
import dev.boze.client.utils.PositionUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.Camera;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public class ClickTP extends Module {
   public static final ClickTP INSTANCE = new ClickTP();
   private final BooleanSetting field3195 = new BooleanSetting("Render", true, "Render the block you're looking at");
   private final ColorSetting field3196 = new ColorSetting("Color", new BozeDrawColor(1685820627), "Color for fill", this.field3195);
   private final ColorSetting field3197 = new ColorSetting("Outline", new BozeDrawColor(-8678189), "Color for outline", this.field3195);
   private final EnumSetting<ClickTPMode> field3198 = new EnumSetting<ClickTPMode>(
      "Mode",
      ClickTPMode.RightClick,
      "Mode to use for ClickTP\n - Once: Teleports you to the block you're looking at on enable\n - RightClick: Teleports you to the block you're looking at when you right click\n - Bind: Teleports you to the block you're looking at when you press the bind key\n"
   );
   private final BindSetting field3199 = new BindSetting("Bind", Bind.fromKey(342), "Bind for ClickTP", this::lambda$new$0);
   private HitResult field3200 = null;

   private ClickTP() {
      super(
         "ClickTP",
         "Teleports you to the block you're looking at\nThis won't work on most servers with anti-cheats\nCredits: youtube.com/@LiveOverflow\n",
         Category.Movement
      );
      this.addSettings(this.field3195);
   }

   @EventHandler
   public void method1809(Render3DEvent event) {
      if (this.field3200 != null && this.field3200 instanceof BlockHitResult var5) {
         this.method1815(new Box(var5.getBlockPos()), var5.getSide(), event.field1950);
      }
   }

   @Override
   public void onEnable() {
      if (MinecraftUtils.isClientActive()) {
         this.field3200 = null;
         if (this.field3198.method461() == ClickTPMode.Once) {
            this.method1814();
            this.method1813();
            this.setEnabled(false);
         }
      }
   }

   @EventHandler
   public void method1810(PostTickEvent event) {
      if (MinecraftUtils.isClientActive()) {
         this.method1814();
         if (!mc.player.isUsingItem()) {
            if (this.field3198.method461() == ClickTPMode.RightClick && mc.options.useKey.isPressed()) {
               this.method1813();
            }
         }
      }
   }

   @EventHandler
   public void method1811(KeyEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.action == KeyAction.Press && mc.currentScreen == null) {
            if (this.field3198.method461() == ClickTPMode.Bind && this.field3199.method476().matches(true, event.key)) {
               this.method1813();
            }
         }
      }
   }

   @EventHandler
   public void method1812(MouseButtonEvent event) {
      if (MinecraftUtils.isClientActive()) {
         if (event.action == KeyAction.Press && mc.currentScreen == null) {
            if (this.field3198.method461() == ClickTPMode.Bind && this.field3199.method476().matches(false, event.button)) {
               this.method1813();
            }
         }
      }
   }

   private void method1813() {
      if (this.field3200 != null && this.field3200.getType() == Type.BLOCK) {
         PositionUtils.method146((BlockHitResult)this.field3200);
      }
   }

   private void method1814() {
      HitResult var4 = mc.player.raycast(6.0, 0.05F, false);
      if (var4.getType() != Type.ENTITY || mc.player.interact(((EntityHitResult)var4).getEntity(), Hand.MAIN_HAND) == ActionResult.PASS) {
         Camera var5 = mc.gameRenderer.getCamera();
         Vec3d var6 = var5.getPos();
         Vec3d var7 = Vec3d.fromPolar(var5.getPitch(), var5.getYaw()).multiply(210.0);
         Vec3d var8 = var6.add(var7);
         RaycastContext var9 = new RaycastContext(var6, var8, ShapeType.OUTLINE, FluidHandling.NONE, mc.player);
         this.field3200 = mc.world.raycast(var9);
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   private void method1815(Box var1, Direction var2, Renderer3D var3) {
      if (var2 != null) {
         switch (ng.field2120[var2.ordinal()]) {
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

      var3.method1273(var1, this.field3196.method1362(), this.field3197.method1362(), ShapeMode.Full, 0);
   }

   private boolean lambda$new$0() {
      return this.field3198.method461() == ClickTPMode.Bind;
   }
}
