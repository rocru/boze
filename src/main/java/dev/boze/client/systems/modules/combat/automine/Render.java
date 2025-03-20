package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.systems.modules.combat.AutoMine;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import mapped.Class3071;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

public class Render implements SettingsGroup {
   private final BooleanSetting field252 = new BooleanSetting("Render", true, "Render block being mined");
   private final ColorSetting field253 = new ColorSetting("Start", new BozeDrawColor(1694442018), "Color for unbroken blocks");
   private final ColorSetting field254 = new ColorSetting("StartLine", new BozeDrawColor(-56798), "Color for unbroken block lines");
   private final ColorSetting field255 = new ColorSetting("End", new BozeDrawColor(1680015138), "Color for broken blocks");
   private final ColorSetting field256 = new ColorSetting("EndLine", new BozeDrawColor(-14483678), "Color for broken block lines");
   final BooleanSetting field257 = new BooleanSetting("Fade", true, "Fade render");
   private final FloatSetting field258 = new FloatSetting("Duration", 1.0F, 0.1F, 5.0F, 0.1F, "Fade duration in seconds", this.field257);
   private final ColorSetting field259 = new ColorSetting("Fade", new BozeDrawColor(2293538), "Color for faded blocks", this.field257);
   private final ColorSetting field260 = new ColorSetting("FadeLine", new BozeDrawColor(2293538), "Color for faded block lines", this.field257);
   final BooleanSetting field261 = new BooleanSetting("InstantPos", false, "Show block that can be instant-mined");
   private final ColorSetting field262 = new ColorSetting("Color", new BozeDrawColor(0), "Color for faded blocks", this.field261);
   private final ColorSetting field263 = new ColorSetting("Outline", new BozeDrawColor(-1), "Color for faded block lines", this.field261);
   final BooleanSetting field264 = new BooleanSetting("Queue", false, "Show block in the queue");
   private final ColorSetting field265 = new ColorSetting("Color", new BozeDrawColor(1679958783), "Color for queued blocks", this.field264);
   private final ColorSetting field266 = new ColorSetting("Outline", new BozeDrawColor(-14540033), "Color for queued block lines", this.field264);
   public final SettingBlock field267 = new SettingBlock(
      "Renderer",
      "Renderer settings",
      this.field252,
      this.field253,
      this.field254,
      this.field255,
      this.field256,
      this.field257,
      this.field258,
      this.field259,
      this.field260,
      this.field261,
      this.field262,
      this.field263,
      this.field264,
      this.field265,
      this.field266
   );
   final HashMap<BlockPos, Long> field268 = new HashMap();

   @Override
   public Setting<?>[] get() {
      return this.field267.method472();
   }

   public void method2071(Render3DEvent event) {
      if (this.field252.getValue()) {
         for (TaskLogger var6 : AutoMine.INSTANCE.miner.field201) {
            this.method156(event, var6.field2532.field2523, var6.field2533);
            this.field268.remove(var6.field2532.field2523);
         }

         for (TaskLogger var17 : AutoMine.INSTANCE.miner.field204) {
            this.method156(event, var17.field2532.field2523, 1.0F);
            this.field268.remove(var17.field2532.field2523);
         }
      }

      if (this.field257.getValue()) {
         Iterator var14 = this.field268.entrySet().iterator();

         while (var14.hasNext()) {
            Entry var18 = (Entry)var14.next();
            BlockPos var7 = (BlockPos)var18.getKey();
            long var8 = (Long)var18.getValue();
            if ((float)(System.currentTimeMillis() - var8) >= 400.0F * this.field258.getValue()) {
               var14.remove();
               return;
            }

            Box var10 = new Box(var7);
            BozeDrawColor var11 = Class3071.method6015(
               this.field255.getValue(),
               this.field259.getValue(),
               MathHelper.clamp((double)((float)(System.currentTimeMillis() - var8) / (400.0F * this.field258.getValue())), 0.0, 1.0)
            );
            BozeDrawColor var12 = Class3071.method6015(
               this.field256.getValue(),
               this.field260.getValue(),
               MathHelper.clamp((double)((float)(System.currentTimeMillis() - var8) / (400.0F * this.field258.getValue())), 0.0, 1.0)
            );
            event.field1950.method1273(var10, var11, var12, ShapeMode.Full, 0);
         }
      }

      if (this.field264.getValue()) {
         for (BlockDirectionInfo var19 : AutoMine.INSTANCE.queue.field87) {
            Box var21 = new Box(var19.field2523);
            event.field1950.method1273(var21, this.field265.getValue(), this.field266.getValue(), ShapeMode.Full, 0);
         }
      }

      if (this.field261.getValue() && AutoMine.INSTANCE.field2519 != null && AutoMine.INSTANCE.instantRemine.getValue()) {
         BlockPos var16 = AutoMine.INSTANCE.field2519;
         Box var20 = new Box(var16);
         event.field1950.method1273(var20, this.field262.getValue(), this.field263.getValue(), ShapeMode.Full, 0);
      }
   }

   private void method156(Render3DEvent var1, BlockPos var2, float var3) {
      if (var3 > 1.0F) {
         var3 = 1.0F;
      }

      double var7 = 0.5 * (double)var3;
      Box var9 = new Box(
         (double)var2.getX() + 0.5 - var7,
         (double)var2.getY() + 0.5 - var7,
         (double)var2.getZ() + 0.5 - var7,
         (double)var2.getX() + 0.5 + var7,
         (double)var2.getY() + 0.5 + var7,
         (double)var2.getZ() + 0.5 + var7
      );
      BozeDrawColor var10 = Class3071.method6015(this.field253.getValue(), this.field255.getValue(), MathHelper.clamp((double)var3, 0.0, 1.0));
      BozeDrawColor var11 = Class3071.method6015(this.field254.getValue(), this.field256.getValue(), MathHelper.clamp((double)var3, 0.0, 1.0));
      var1.field1950.method1273(var9, var10, var11, ShapeMode.Full, 0);
   }
}
