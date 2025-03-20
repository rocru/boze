package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ChamsMode;
import dev.boze.client.enums.ChamsShaderMode;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.jumptable.no;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.trackers.TargetTracker;
import dev.boze.client.utils.render.ByteTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.io.File;
import java.io.FileInputStream;

public class Chams extends Module {
   public static final Chams INSTANCE = new Chams();
   public final BooleanSetting field3461 = new BooleanSetting("NoShadows", true, "Prevents shadows from being rendered for selected entities");
   public final BooleanSetting field3462 = new BooleanSetting("Tint", false, "Tint entities");
   public final EnumSetting<ChamsMode> field3463 = new EnumSetting<ChamsMode>("Mode", ChamsMode.Shader, "Mode for Chams");
   public final FloatSetting field3464 = new FloatSetting("WallOpacity", 1.0F, 0.0F, 1.0F, 0.01F, "Opacity of entities through walls", this::lambda$new$0);
   public final EnumSetting<ShaderMode> field3465 = new EnumSetting<ShaderMode>("Shader", ShaderMode.Colored, "Shader to use", this::lambda$new$1);
   public final BooleanSetting field3466 = new BooleanSetting("FastRender", false, "Make the shader render faster at the cost of quality", this.field3465);
   public final IntSetting field3467 = new IntSetting("Blur", 0, 0, 5, 1, "Blur for shader", this.field3465);
   public final IntSetting field3468 = new IntSetting("Radius", 1, 0, 10, 1, "Shader outline radius", this.field3465);
   public final FloatSetting field3469 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3465);
   public final FloatSetting field3470 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$2, this.field3465);
   public final IntSetting field3471 = new IntSetting("Opacity", 64, 0, 255, 1, "Opacity for shader fill", this::lambda$new$3, this.field3465);
   public final StringSetting field3472 = new StringSetting("Image", "", "Fill for image shader", this::lambda$new$4, this.field3465);
   public final ColorSetting field3473 = new ColorSetting("Fill", new BozeDrawColor(1681640397), "Color for shader fill", this::lambda$new$5, this.field3465);
   public final ColorSetting field3474 = new ColorSetting(
      "Hidden", new BozeDrawColor(1681640397), "Color for hidden shader fill", this::lambda$new$6, this.field3465
   );
   public final ColorSetting field3475 = new ColorSetting(
      "Outline", new BozeDrawColor(-12858419), "Color for shader outline", this::lambda$new$7, this.field3465
   );
   public final IntSetting field3476 = new IntSetting("Range", 250, 10, 500, 10, "Range for chams");
   private final BooleanSetting field3477 = new BooleanSetting("Players", true, "Apply Chams to players");
   private final BooleanSetting field3478 = new BooleanSetting("Self", true, "Apply chams to self", this.field3477);
   private final RGBASetting field3479 = new RGBASetting("Tint", new RGBAColor(-1), "Tint for players", this.field3462::getValue, this.field3477);
   private final RGBASetting field3480 = new RGBASetting("Color", new RGBAColor(-12858419), "Color for player shader", this::lambda$new$8, this.field3477);
   private final BooleanSetting field3481 = new BooleanSetting("Friends", true, "Apply Chams to friends");
   private final RGBASetting field3482 = new RGBASetting("Tint", new RGBAColor(-1), "Tint for friends", this.field3462::getValue, this.field3481);
   private final RGBASetting field3483 = new RGBASetting("Color", new RGBAColor(-15277290), "Color for friend shader", this::lambda$new$9, this.field3481);
   private final BooleanSetting field3484 = new BooleanSetting("Targets", true, "Apply Chams to targets");
   private final RGBASetting field3485 = new RGBASetting("Tint", new RGBAColor(-1), "Color for targets", this.field3484);
   private final RGBASetting field3486 = new RGBASetting("Color", new RGBAColor(-1894890), "Color for target shader", this.field3484);
   private final BooleanSetting field3487 = new BooleanSetting("Animals", false, "Apply Chams to animals");
   private final RGBASetting field3488 = new RGBASetting("Tint", new RGBAColor(-1), "Tint for animals", this.field3462::getValue, this.field3487);
   private final RGBASetting field3489 = new RGBASetting("Color", new RGBAColor(-15277196), "Color for animal shader", this::lambda$new$10, this.field3487);
   private final BooleanSetting aa = new BooleanSetting("Monsters", false, "Apply Chams to monsters");
   private final RGBASetting ab = new RGBASetting("Tint", new RGBAColor(-1), "Tint for monsters", this.field3462::getValue, this.field3487);
   private final RGBASetting ac = new RGBASetting("Color", new RGBAColor(-1894890), "Color for monster shader", this::lambda$new$11, this.aa);
   public final BooleanSetting ad = new BooleanSetting("Crystals", false, "Apply Chams to crystals");
   public final FloatSetting ae = new FloatSetting("Scale", 1.0F, -5.0F, 5.0F, 0.1F, "Scale for crystals", this.ad);
   public final FloatSetting af = new FloatSetting("VScale", 1.0F, -5.0F, 5.0F, 0.1F, "Vertical scale for crystals", this.ad);
   public final FloatSetting ag = new FloatSetting("Spin", 1.0F, -5.0F, 5.0F, 0.1F, "Spin multiplier for crystals", this.ad);
   public final FloatSetting ah = new FloatSetting("Bounce", 1.0F, -5.0F, 5.0F, 0.1F, "Bounce multiplier for crystals", this.ad);
   public final RGBASetting ai = new RGBASetting("CoreTint", new RGBAColor(-1), "Tint for crystal cores", this.field3462::getValue, this.ad);
   public final RGBASetting aj = new RGBASetting("FrameTint", new RGBAColor(-1), "Tint for crystal frames", this.field3462::getValue, this.ad);
   public final RGBASetting ak = new RGBASetting("Color", new RGBAColor(-56932), "Color for crystal shader", this::lambda$new$12, this.ad);
   public final BooleanSetting al = new BooleanSetting("Hands", false, "Apply chams to hands");
   public final BooleanSetting am = new BooleanSetting("Textured", true, "Textured hands", this.al);
   public final ColorSetting an = new ColorSetting("Fill", new BozeDrawColor(1691587707), "Color for hand fill", this.al);
   public final ColorSetting ao = new ColorSetting("Outline", new BozeDrawColor(-2911109), "Color for hand outline", this.al);
   public final EnumSetting<ChamsShaderMode> ap = new EnumSetting<ChamsShaderMode>("Shader", ChamsShaderMode.Normal, "Shader to use", this.al);
   public final BooleanSetting aq = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.al);
   public final IntSetting ar = new IntSetting("Blur", 0, 0, 5, 1, "Blur for shader", this.al);
   public final FloatSetting as = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.al);
   public final FloatSetting at = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$13, this.al);
   public final IntSetting au = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.al);
   public final FloatSetting av = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.al);
   public final StringSetting aw = new StringSetting("Image", "", "Fill for image shader", this::lambda$new$14, this.al);
   public final BooleanSetting ax = new BooleanSetting("Explosions", false, "Apply chams to explosions");
   public final BooleanSetting ay = new BooleanSetting("Textured", false, "Textured explosions", this.ax);
   public final ColorSetting az = new ColorSetting("Fill", new BozeDrawColor(816595199), "Color for explosion fill", this.ax);
   public final ColorSetting aA = new ColorSetting("Outline", new BozeDrawColor(-1599323905), "Color for explosion outline", this.ax);
   public final EnumSetting<ChamsShaderMode> aB = new EnumSetting<ChamsShaderMode>("Shader", ChamsShaderMode.Normal, "Shader to use", this.ax);
   public final BooleanSetting aC = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.ax);
   public final IntSetting aD = new IntSetting("Blur", 0, 0, 5, 1, "Blur for shader", this.ax);
   public final FloatSetting aE = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.ax);
   public final FloatSetting aF = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$15, this.ax);
   public final IntSetting aG = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.ax);
   public final FloatSetting aH = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.ax);
   public final StringSetting aI = new StringSetting("Image", "", "Fill for image shader", this::lambda$new$16, this.ax);
   public ByteTexture aJ;
   public String aK = "";
   public ByteTexture aL;
   public String aM = "";
   public ByteTexture aN;
   public String aO = "";

   public Chams() {
      super("Chams", "Render entities through walls", Category.Render);
   }

   public ShaderMode method1921() {
      if (this.ap.getValue() == ChamsShaderMode.Image) {
         if (!this.aw.getValue().isEmpty() && (!this.aw.getValue().equals(this.aM) || this.aL == null)) {
            File var4 = new File(ConfigManager.images, this.aw.getValue() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.aL = ByteTexturePacker.method493(var5);
               if (this.aL != null) {
                  this.aM = this.aw.getValue();
               } else {
                  this.aM = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.aw.setValue("");
               this.aM = "";
            }
         }

         if (this.aL != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }

   public ShaderMode method1922() {
      if (this.aB.getValue() == ChamsShaderMode.Image) {
         if (!this.aI.getValue().isEmpty() && (!this.aI.getValue().equals(this.aO) || this.aN == null)) {
            File var4 = new File(ConfigManager.images, this.aI.getValue() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.aN = ByteTexturePacker.method493(var5);
               if (this.aN != null) {
                  this.aO = this.aI.getValue();
               } else {
                  this.aO = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.aI.setValue("");
               this.aO = "";
            }
         }

         if (this.aN != null) {
            return ShaderMode.Image;
         }
      }

      return ShaderMode.Rainbow;
   }

   public ShaderMode method1923() {
      return this.field3465.getValue();
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public boolean method1924(Entity e) {
      if (e.distanceTo(mc.player) > (float)this.field3476.getValue().intValue()) {
         return false;
      } else if (e instanceof EndCrystalEntity) {
         return this.ad.getValue();
      } else if (!(e instanceof PlayerEntity)) {
         return switch (no.field2122[e.getType().getSpawnGroup().ordinal()]) {
            case 1, 2, 3, 4, 5, 6 -> this.field3487.getValue();
            case 7 -> this.aa.getValue();
            default -> false;
         };
      } else if (e != mc.player || (FreeCam.INSTANCE.isEnabled() || !mc.options.getPerspective().isFirstPerson()) && this.field3478.getValue()) {
         if (Friends.method2055(e)) {
            return this.field3481.getValue();
         } else {
            return TargetTracker.method2055(e) ? this.field3484.getValue() : this.field3477.getValue();
         }
      } else {
         return false;
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public RGBAColor method1925(Entity e) {
      if (e instanceof PlayerEntity) {
         if (Friends.method2055(e)) {
            return this.field3482.getValue();
         } else {
            return TargetTracker.method2055(e) ? this.field3485.getValue() : this.field3479.getValue();
         }
      } else {
         return switch (no.field2122[e.getType().getSpawnGroup().ordinal()]) {
            case 1, 2, 3, 4, 5, 6 -> this.field3488.getValue();
            case 7 -> this.ab.getValue();
            default -> RGBAColor.field402;
         };
      }
   }

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public RGBAColor method1926(Entity e) {
      if (e instanceof PlayerEntity) {
         if (Friends.method2055(e)) {
            return this.field3483.getValue();
         } else {
            return TargetTracker.method2055(e) ? this.field3486.getValue() : this.field3480.getValue();
         }
      } else if (e instanceof EndCrystalEntity) {
         return this.ak.getValue();
      } else {
         return switch (no.field2122[e.getType().getSpawnGroup().ordinal()]) {
            case 1, 2, 3, 4, 5, 6 -> this.field3489.getValue();
            case 7 -> this.ac.getValue();
            default -> RGBAColor.field402;
         };
      }
   }

   private boolean lambda$new$16() {
      return this.aB.getValue() == ChamsShaderMode.Image;
   }

   private boolean lambda$new$15() {
      return this.aE.getValue() > 0.0F;
   }

   private boolean lambda$new$14() {
      return this.ap.getValue() == ChamsShaderMode.Image;
   }

   private boolean lambda$new$13() {
      return this.as.getValue() > 0.0F;
   }

   private boolean lambda$new$12() {
      return this.field3463.getValue() != ChamsMode.Normal && this.method1923() == ShaderMode.Colored;
   }

   private boolean lambda$new$11() {
      return this.field3463.getValue() != ChamsMode.Normal && this.method1923() == ShaderMode.Colored;
   }

   private boolean lambda$new$10() {
      return this.field3463.getValue() != ChamsMode.Normal && this.method1923() == ShaderMode.Colored;
   }

   private boolean lambda$new$9() {
      return this.field3463.getValue() != ChamsMode.Normal && this.method1923() == ShaderMode.Colored;
   }

   private boolean lambda$new$8() {
      return this.field3463.getValue() != ChamsMode.Normal && this.method1923() == ShaderMode.Colored;
   }

   private boolean lambda$new$7() {
      return this.method1923() != ShaderMode.Colored;
   }

   private boolean lambda$new$6() {
      return this.method1923() == ShaderMode.Rainbow;
   }

   private boolean lambda$new$5() {
      return this.method1923() == ShaderMode.Rainbow;
   }

   private boolean lambda$new$4() {
      return this.method1923() == ShaderMode.Image;
   }

   private boolean lambda$new$3() {
      return this.method1923() != ShaderMode.Rainbow;
   }

   private boolean lambda$new$2() {
      return this.field3469.getValue() > 0.0F;
   }

   private boolean lambda$new$1() {
      return this.field3463.getValue() != ChamsMode.Normal;
   }

   private boolean lambda$new$0() {
      return this.field3463.getValue() == ChamsMode.Normal || this.field3463.getValue() == ChamsMode.Both;
   }
}
