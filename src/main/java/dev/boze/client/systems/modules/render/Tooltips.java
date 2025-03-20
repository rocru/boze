package dev.boze.client.systems.modules.render;

import dev.boze.client.events.TooltipDataEvent;
import dev.boze.client.settings.BindSetting;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.Bind;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.StackDeserializer;
import dev.boze.client.utils.tooltip.BannerTooltipComponent;
import dev.boze.client.utils.tooltip.ItemStackTooltipComponent;
import dev.boze.client.utils.tooltip.MapTooltipComponent;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.BannerPatternsComponent.Layer;
import net.minecraft.component.type.MapIdComponent;
import net.minecraft.item.BannerItem;
import net.minecraft.item.BannerPatternItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

public class Tooltips extends Module {
   public static final Tooltips INSTANCE = new Tooltips();
   public static final RGBAColor field3757 = new RGBAColor(45, 65, 65, 255);
   private final BindSetting field3758 = new BindSetting("Bind", Bind.create(), "Bind for showing tooltips, unbind to always show");
   private final BooleanSetting field3759 = new BooleanSetting("Maps", true, "Map tooltips");
   public final MinMaxSetting field3760 = new MinMaxSetting("Scale", 1.0, 0.1, 2.0, 0.05, "Map tooltip scale", this.field3759);
   private final BooleanSetting field3761 = new BooleanSetting("Banners", true, "Banner tooltips");
   public final MinMaxSetting field3762 = new MinMaxSetting("Scale", 1.0, 0.1, 2.0, 0.05, "Banner tooltip scale", this.field3761);
   private final BooleanSetting field3763 = new BooleanSetting("Containers", true, "Container tooltips");
   public final MinMaxSetting field3764 = new MinMaxSetting("Scale", 1.0, 0.1, 2.0, 0.05, "Container tooltip scale", this.field3763);

   public Tooltips() {
      super("Tooltips", "Better inventory tooltips", Category.Render);
   }

   @EventHandler
   private void method2043(TooltipDataEvent var1) {
      if (!this.field3758.getValue().isValid() || this.field3758.getValue().isPressed()) {
         if (var1.field1959.getItem() == Items.FILLED_MAP && this.field3759.getValue()) {
            MapIdComponent var8 = (MapIdComponent)var1.field1959.get(DataComponentTypes.MAP_ID);
            if (var8 != null) {
               var1.field1958 = new MapTooltipComponent(var8.id());
            }
         } else if (var1.field1959.getItem() instanceof BannerItem && this.field3761.getValue()) {
            var1.field1958 = new BannerTooltipComponent(var1.field1959);
         } else if (var1.field1959.getItem() instanceof BannerPatternItem && this.field3761.getValue()) {
            BannerPatternsComponent var7 = (BannerPatternsComponent)var1.field1959.get(DataComponentTypes.BANNER_PATTERNS);
            if (var7 != null) {
               var1.field1958 = new BannerTooltipComponent(this.method2044(var7.layers()));
            }
         } else if (var1.field1959.getItem() == Items.SHIELD && this.field3761.getValue()) {
            ItemStack var6 = this.method2045(var1.field1959);
            if (var6 != null) {
               var1.field1958 = new BannerTooltipComponent(var6);
            }
         } else if (this.field3763.getValue() && StackDeserializer.method1756(var1.field1959)) {
            ItemStack[] var5 = new ItemStack[27];
            StackDeserializer.method670(var1.field1959, var5);
            var1.field1958 = new ItemStackTooltipComponent(var5);
         }
      }
   }

   private ItemStack method2044(List<Layer> var1) {
      ItemStack var2 = new ItemStack(Items.GRAY_BANNER);
      BannerPatternsComponent var3 = (BannerPatternsComponent)var2.get(DataComponentTypes.BANNER_PATTERNS);
      var3.layers().addAll(var1);
      var2.set(DataComponentTypes.BANNER_PATTERNS, var3);
      return var2;
   }

   private ItemStack method2045(ItemStack var1) {
      if (var1.getComponents().isEmpty() && var1.get(DataComponentTypes.BLOCK_ENTITY_DATA) != null && var1.get(DataComponentTypes.BASE_COLOR) != null) {
         ItemStack var5 = new ItemStack(Items.GRAY_BANNER);
         BannerPatternsComponent var6 = (BannerPatternsComponent)var5.get(DataComponentTypes.BANNER_PATTERNS);
         BannerPatternsComponent var7 = (BannerPatternsComponent)var1.get(DataComponentTypes.BANNER_PATTERNS);
         if (var7 == null) {
            return var5;
         } else {
            var6.layers().addAll(var7.layers());
            return var5;
         }
      } else {
         return null;
      }
   }
}
