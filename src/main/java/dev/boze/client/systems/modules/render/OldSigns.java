package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
import net.minecraft.world.chunk.WorldChunk;

import java.util.ArrayList;
import java.util.List;

public class OldSigns extends Module {
   public static final OldSigns INSTANCE = new OldSigns();
   private final ColorSetting field3616 = new ColorSetting("Fill", new BozeDrawColor(255, 0, 0, 125), "Fill color for old signs");
   private final ColorSetting field3617 = new ColorSetting("Outline", new BozeDrawColor(255, 0, 0, 255), "Outline color for old signs");
   private int field3618 = 0;

   public OldSigns() {
      super("OldSigns", "Highlights pre 1.9 signs", Category.Render);
   }

   @Override
   public void onEnable() {
      this.field3618 = 0;
   }

   @Override
   public String method1322() {
      return String.valueOf(this.field3618);
   }

   @EventHandler
   private void method2006(Render3DEvent var1) {
      this.field3618 = 0;

      for (BlockEntity var6 : this.method2007()) {
         if (var6 instanceof SignBlockEntity) {
            SignBlockEntity var7 = (SignBlockEntity)var6;
            if (this.method2009(var7)) {
               this.field3618++;
               var1.field1950.method1272(var7.getPos(), this.field3616.method1362(), this.field3617.method1362(), ShapeMode.Full, 0);
            }
         }
      }
   }

   private List<BlockEntity> method2007() {
      ArrayList var4 = new ArrayList();

      for (WorldChunk var6 : this.method2008()) {
         var4.addAll(var6.getBlockEntities().values());
      }

      return var4;
   }

   private List<WorldChunk> method2008() {
      ArrayList var4 = new ArrayList();
      int var5 = (Integer)mc.options.getViewDistance().getValue();

      for (int var6 = -var5; var6 <= var5; var6++) {
         for (int var7 = -var5; var7 <= var5; var7++) {
            WorldChunk var8 = mc.world.getChunkManager().getWorldChunk((int)mc.player.getX() / 16 + var6, (int)mc.player.getZ() / 16 + var7);
            if (var8 != null) {
               var4.add(var8);
            }
         }
      }

      return var4;
   }

   private boolean method2009(SignBlockEntity var1) {
      SignText var5 = var1.getText(true);

      for (int var6 = 0; var6 < 4; var6++) {
         Text var7 = var5.getMessage(var6, true);
         if (!var7.getSiblings().isEmpty()) {
            return false;
         }
      }

      SignText var9 = var1.getText(false);

      for (int var10 = 0; var10 < 4; var10++) {
         Text var8 = var9.getMessage(var10, true);
         if (!var8.getSiblings().isEmpty()) {
            return false;
         }
      }

      return true;
   }
}
