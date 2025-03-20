package dev.boze.client.systems.modules.hud.core;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.font.IFontRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;

public class Armor extends HUDModule {
    private final BooleanSetting field2585 = new BooleanSetting("Bar", false, "Show durability as bar");
    private final BooleanSetting field2586 = new BooleanSetting("Text", true, "Show durability as text");
    private final BooleanSetting field2587 = new BooleanSetting("Percentage", true, "Show armor durability as a percentage", this.field2586);
    private final BooleanSetting field2588 = new BooleanSetting("Dynamic", true, "Dynamic text color", this.field2586);
    private final ColorSetting field2589 = new ColorSetting("Text", new BozeDrawColor(-13421569, true, 0.1, 0.0, 0.1), "Text color", this.field2586);
    private final BooleanSetting field2590 = new BooleanSetting("Shadow", false, "Text shadow");
    private final BooleanSetting field2591 = new BooleanSetting(
            "Background", false, "Draw background, like on other HUD modules", HUD.INSTANCE.field2394::getValue
    );
    public static final Armor INSTANCE = new Armor();
    private static final MatrixStack field2592 = new MatrixStack();

    public Armor() {
        super("Armor", "Shows your current Armor", 100.0, 24.0);
        this.field595.hide();
    }

    @Override
    public void method295(DrawContext context) {
        if (!mc.player.isSpectator()) {
            this.method316(this.field2585.getValue() ? 28.0 : 24.0);
            byte var5 = 3;
            IFontRender.method499().setFontScale(IFontRender.method499().getFontScale() * 0.6);

            for (int var6 = 3; var6 >= 0; var6--) {
                ItemStack var7 = mc.player.getInventory().getArmorStack(var6);
                if (var7 != null && (var7.getItem() instanceof ArmorItem || var7.getItem() instanceof ElytraItem)) {
                    RenderSystem.getModelViewStack().pushMatrix();
                    if (this.field2586.getValue()) {
                        String var8 = this.method1549(var7) + "";
                        IFontRender.method499()
                                .drawShadowedText(
                                        var8,
                                        this.method1391() + (double) var5 + 8.0 - IFontRender.method499().method501(var8) / 2.0,
                                        (int) this.method305(),
                                        this.method1548(var7),
                                        this.field2590.getValue()
                                );
                    }

                    context.drawItem(var7, (int) (this.method1391() + (double) var5), (int) this.method305() + 8);
                    if (this.field2585.getValue()) {
                        context.drawItemInSlot(mc.textRenderer, var7, (int) (this.method1391() + (double) var5), (int) this.method305() + 12);
                    }

                    RenderSystem.getModelViewStack().popMatrix();
                    RenderSystem.disableBlend();
                    var5 += 20;
                }
            }

            this.method314(var5);
            if (this.field2591.getValue()) {
                HUD.INSTANCE.field2397.method2252(this.method1391(), this.method305(), this.method313(), this.method315(), RGBAColor.field402);
            }
        }
    }

    private BozeDrawColor method1548(ItemStack var1) {
        return this.field2588.getValue() ? new BozeDrawColor(var1.getItem().getItemBarColor(var1) + -16777216) : this.field2589.getValue();
    }

    private int method1549(ItemStack var1) {
        int var5 = var1.getMaxDamage();
        int var6 = var5 - var1.getDamage();
        int var7 = (int) Math.round((double) var6 / ((double) var5 * 0.01));
        return this.field2587.getValue() ? var7 : var6;
    }
}
