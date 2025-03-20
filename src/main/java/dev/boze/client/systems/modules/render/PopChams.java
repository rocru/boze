package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.events.PacketBundleEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.render.logoutspots.PoppedPlayerEntity;
import dev.boze.client.utils.fakeplayer.FakePlayerEntityWithTotem;
import dev.boze.client.utils.render.ByteTexture;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class PopChams extends Module {
   public static final PopChams INSTANCE = new PopChams();
   private final BooleanSetting field3641 = new BooleanSetting("Unique", false, "Only render one pop per player");
   private final BooleanSetting field3642 = new BooleanSetting("Match", false, "Match player bodies");
   public final ColorSetting field3643 = new ColorSetting("Color", new BozeDrawColor(1691587707), "Color for fill");
   public final ColorSetting field3644 = new ColorSetting("Outline", new BozeDrawColor(-2911109), "Color for outline");
   private final BooleanSetting field3645 = new BooleanSetting("Shader", false, "Use a shader");
   public final ColorSetting field3646 = new ColorSetting("Color", new BozeDrawColor(1691587707, true, 0.1, 0.0, 0.2), "Color for shader fill", this.field3645);
   public final ColorSetting field3647 = new ColorSetting(
      "Outline", new BozeDrawColor(-2911109, true, 0.1, 0.0, 0.2), "Color for shader outline", this.field3645
   );
   public final EnumSetting<ShaderMode> field3648 = new EnumSetting<ShaderMode>("Shader", ShaderMode.Colored, "Shader to use", this.field3645);
   public final BooleanSetting field3649 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3645);
   public final IntSetting field3650 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3645);
   public final FloatSetting field3651 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3645);
   public final FloatSetting field3652 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3645);
   private final IntSetting field3653 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3645);
   private final FloatSetting field3654 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3645);
   public final StringSetting field3655 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3645);
   private final MinMaxSetting field3656 = new MinMaxSetting("Fade", 1.0, 0.1, 10.0, 0.1, "Fade popped players", this::lambda$new$2);
   private final MinMaxSetting field3657 = new MinMaxSetting("Rise", 0.0, 0.0, 10.0, 0.1, "Rise popped players");
   private final List<PoppedPlayerEntity> field3658 = new ArrayList();
   private Renderer3D field3659;
   private ByteTexture field3660;
   private String field3661 = "";

   public PopChams() {
      super("PopChams", "Shows chams where players popped", Category.Render);
   }

   @Override
   public void onDisable() {
      synchronized (this.field3658) {
         this.field3658.clear();
      }
   }

   @EventHandler
   public void method2018(PacketBundleEvent event) {
      if (event.packet instanceof EntityStatusS2CPacket var5) {
         if (mc.world != null) {
            if (var5.getStatus() == 35) {
               Entity var11 = var5.getEntity(mc.world);
               if (var11 instanceof PlayerEntity var7 && var7 != mc.player) {
                  synchronized (this.field3658) {
                     if (this.field3641.getValue()) {
                        this.field3658.removeIf(PopChams::lambda$onReceivePacket$3);
                     }

                     this.field3658.add(new PoppedPlayerEntity(this, var7));
                     return;
                  }
               }
            }
         }
      }
   }

   public void method2019(FakePlayerEntityWithTotem entity) {
      synchronized (this.field3658) {
         if (this.field3641.getValue()) {
            this.field3658.removeIf(PopChams::lambda$onInternalPop$4);
         }

         this.field3658.add(new PoppedPlayerEntity(this, entity));
      }
   }

   @EventHandler
   private void method2020(Render3DEvent var1) {
      synchronized (this.field3658) {
         if (this.field3658.isEmpty()) {
            return;
         }
      }

      if (this.field3645.getValue()) {
         if (this.field3659 == null) {
            this.field3659 = new Renderer3D(false, true);
         }

         this.field3659.method1217();
      }

      synchronized (this.field3658) {
         this.field3658.removeIf(this::lambda$onRender3D$5);
      }

      if (this.field3645.getValue()) {
         ChamsShaderRenderer.method1310(
            this::lambda$onRender3D$6,
            this.method2021(),
            this.field3649.getValue(),
            this.field3646,
            this.field3647,
            this.field3653.method434(),
            this.field3654.getValue(),
            this.field3651.getValue(),
            this.field3652.getValue(),
            this.field3650.method434(),
            this.field3660
         );
      }
   }

   private ShaderMode method2021() {
      if (this.field3648.getValue() == ShaderMode.Image) {
         if (!this.field3655.getValue().isEmpty() && (!this.field3655.getValue().equals(this.field3661) || this.field3660 == null)) {
            File var4 = new File(ConfigManager.images, this.field3655.getValue() + ".png");

            try {
               FileInputStream var5 = new FileInputStream(var4);
               this.field3660 = ByteTexturePacker.method493(var5);
               if (this.field3660 != null) {
                  this.field3661 = this.field3655.getValue();
               } else {
                  this.field3661 = "";
               }
            } catch (Exception var6) {
               NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
               this.field3655.setValue("");
               this.field3661 = "";
            }
         }

         if (this.field3660 != null) {
            return ShaderMode.Image;
         }
      }

      return this.field3648.getValue() == ShaderMode.Rainbow ? ShaderMode.Rainbow : ShaderMode.Colored;
   }

   private void lambda$onRender3D$6(Render3DEvent var1) {
      this.field3659.method1219(var1.matrix);
   }

   private boolean lambda$onRender3D$5(Render3DEvent var1, PoppedPlayerEntity var2) {
      return var2.method550(this.field3645.getValue() ? this.field3659 : var1.field1950, var1.field1951);
   }

   private static boolean lambda$onInternalPop$4(FakePlayerEntityWithTotem var0, PoppedPlayerEntity var1) {
      return var1.field1273.equals(var0.getUuid());
   }

   private static boolean lambda$onReceivePacket$3(Entity var0, PoppedPlayerEntity var1) {
      return var1.field1273.equals(var0.getUuid());
   }

   private boolean lambda$new$2() {
      return !this.field3645.getValue();
   }

   private boolean lambda$new$1() {
      return this.field3648.getValue() == ShaderMode.Image;
   }

   private boolean lambda$new$0() {
      return this.field3651.getValue() > 0.0F;
   }
}
