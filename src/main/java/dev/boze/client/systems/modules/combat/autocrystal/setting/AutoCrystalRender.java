package dev.boze.client.systems.modules.combat.autocrystal.setting;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AnticheatMode;
import dev.boze.client.enums.AutoCrystalShaderMode;
import dev.boze.client.enums.RotationMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.events.RotationEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.settings.generic.SettingsGroup;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.systems.modules.combat.AutoCrystal;
import dev.boze.client.utils.EntityUtil;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import mapped.Class3071;
import mapped.Class5922;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

class AutoCrystalRender implements IMinecraft, SettingsGroup {
    private final AutoCrystal field146;
    final BooleanSetting field147 = new BooleanSetting("Render", true, "Render placements");
    private final ColorSetting field148 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill");
    private final ColorSetting field149 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline");
    private final BooleanSetting field150 = new BooleanSetting("DamageRender", false, "Render damage");
    private final ScalingSetting field151 = new ScalingSetting();
    private final BooleanSetting field152 = new BooleanSetting("Shader", false, "Use a shader");
    private final EnumSetting<AutoCrystalShaderMode> field153 = new EnumSetting<AutoCrystalShaderMode>(
            "Shader", AutoCrystalShaderMode.Normal, "Shader to use", this.field152
    );
    private final BooleanSetting field154 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field152);
    private final IntSetting field155 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field152);
    private final FloatSetting field156 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field152);
    private final FloatSetting field157 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field152);
    private final IntSetting field158 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field152);
    private final FloatSetting field159 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field152);
    private final StringSetting field160 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field152);
    private final BooleanSetting field161 = new BooleanSetting("Interpolate", true, "Interpolate renders");
    private final MinMaxSetting field162 = new MinMaxSetting("Speed", 1.0, 0.01, 1.0, 0.01, "Interpolation speed", this.field161);
    private final BooleanSetting field163 = new BooleanSetting("Proportional", false, "Interpolate proportional to distance", this.field161);
    private final BooleanSetting field164 = new BooleanSetting("Shrink", false, "Shrink render size");
    private final BooleanSetting field165 = new BooleanSetting("Fade", false, "Fade render opacity");
    private final FloatSetting field166 = new FloatSetting("MinTicks", 10.0F, 0.0F, 20.0F, 0.5F, "Min ticks for shrinking/facing", this::lambda$new$2);
    private final FloatSetting field167 = new FloatSetting("Duration", 10.0F, 1.0F, 20.0F, 0.5F, "Shrinking/fading duration in ticks", this::lambda$new$3);
    final SettingBlock field168 = new SettingBlock(
            "Render",
            "Render settings",
            this.field147,
            this.field150,
            this.field151.field2234,
            this.field151.field2235,
            this.field151.field2236,
            this.field151.field2237,
            this.field151.field2238,
            this.field148,
            this.field149,
            this.field161,
            this.field162,
            this.field163,
            this.field164,
            this.field165,
            this.field166,
            this.field167,
            this.field152,
            this.field153,
            this.field154,
            this.field155,
            this.field156,
            this.field157,
            this.field158,
            this.field159,
            this.field160
    );
    private final ShaderRenderer field169 = new ShaderRenderer(this.field153, this.field160);
    private Renderer3D field170;
    Box field171 = null;
    double field172 = 0.0;
    Box field173 = null;
    Box field174 = null;
    final Timer field175 = new Timer();
    Vec3d field176 = null;

    AutoCrystalRender(AutoCrystal var1) {
        this.field146 = var1;
        this.field151.field2234.setVisibility(this.field150::getValue);
    }

    void method2142() {
        this.field173 = null;
        this.field171 = null;
        this.field174 = null;
        this.field176 = null;
    }

    @EventHandler
    public void method1883(RotationEvent event) {
        if (!event.method555(RotationMode.Sequential, false)) {
            if (this.field147.getValue() && this.field171 != null && !this.field175.hasElapsed(1000.0)) {
                this.field174 = this.field173;
                if (this.field173 != null && this.field161.getValue()) {
                    if (!this.field173.getCenter().equals(this.field171.getCenter())) {
                        float[] var5 = EntityUtil.method2147(this.field173.getCenter(), this.field171.getCenter());
                        Vec3d var6 = Vec3d.fromPolar(var5[1], var5[0]).multiply(this.field162.getValue());
                        double var7 = this.field173.getCenter().distanceTo(this.field171.getCenter());
                        if (this.field163.getValue()) {
                            var6 = var6.multiply(var7);
                        }

                        if (var6.length() >= var7) {
                            this.field173 = this.field171;
                        } else {
                            this.field173 = this.field173.offset(var6);
                        }
                    }
                } else {
                    this.field173 = this.field171;
                }

                if (this.field174 == null) {
                    this.field174 = this.field173;
                }
            } else {
                this.field173 = null;
                this.field174 = null;
            }
        }
    }

    @EventHandler
    public void method2071(Render3DEvent event) {
        if (AutoCrystal.field1038 && !this.field175.hasElapsed(500.0) && this.field176 != null && !mc.options.getPerspective().isFirstPerson()) {
            Vec3d var5 = mc.player.getEyePos();
            Vec3d var6 = this.field176.subtract(var5).normalize();
            Vec3d var7 = var6.multiply(
                    this.field146.field1041.field205.getValue() == AnticheatMode.Grim ? 3.0 : (double) this.field146.autoCrystalBreak.field178.getValue().floatValue()
            );
            Vec3d var8 = var6.multiply(this.field146.autoCrystalPlace.field138.getValue().floatValue());
            event.field1950.method1236(var5.x, var5.y, var5.z, var5.x + var8.x, var5.y + var8.y, var5.z + var8.z, RGBAColor.field406);
            event.field1950.method1236(var5.x, var5.y, var5.z, var5.x + var7.x, var5.y + var7.y, var5.z + var7.z, RGBAColor.field403);
        }

        if (this.field147.getValue()
                && this.field173 != null
                && this.field174 != null
                && !this.field175
                .hasElapsed(
                        !this.field164.getValue() && !this.field165.getValue() ? 1000.0 : (double) ((this.field166.getValue() + this.field167.getValue()) * 50.0F)
                )
                && (this.field146.autoCrystalTracker.field1534 != null || this.field164.getValue() || this.field165.getValue())) {
            Box var11;
            if (this.field161.getValue()) {
                var11 = Class3071.method6021(this.field174, this.field173, event.field1951);
            } else {
                var11 = this.field171;
            }

            if (this.field164.getValue()) {
                long var12 = this.field175.getElapsedTime();
                if ((float) var12 >= this.field166.getValue() * 50.0F) {
                    double var15 = ((float) var12 - this.field166.getValue() * 50.0F) / (this.field167.getValue() * 50.0F);
                    var11 = var11.expand(var15 * -0.5);
                }
            }

            BozeDrawColor var13 = this.field148.getValue();
            BozeDrawColor var14 = this.field149.getValue();
            if (this.field165.getValue()) {
                long var16 = this.field175.getElapsedTime();
                if ((float) var16 >= this.field166.getValue() * 50.0F) {
                    float var10 = 1.0F - ((float) var16 - this.field166.getValue() * 50.0F) / (this.field167.getValue() * 50.0F);
                    var13 = (BozeDrawColor) var13.copy().method197(var10);
                    var14 = (BozeDrawColor) var14.copy().method197(var10);
                }
            }

            if (this.field152.getValue()) {
                if (this.field170 == null) {
                    this.field170 = new Renderer3D(false, true);
                }

                this.field170.method1217();
                this.field170.method1273(var11, var13, var14, ShapeMode.Full, 0);
                ChamsShaderRenderer.method1311(
                        this::lambda$onRender3D$4,
                        this.field169.method1454(),
                        this.field154.getValue(),
                        var13,
                        var14,
                        this.field158.getValue(),
                        this.field159.getValue(),
                        this.field156.getValue(),
                        this.field157.getValue(),
                        this.field155.getValue(),
                        this.field169.method1453()
                );
            } else {
                event.field1950.method1273(var11, var13, var14, ShapeMode.Full, 0);
            }
        }
    }

    @EventHandler
    public void method2040(Render2DEvent event) {
        if (this.field147.getValue()
                && this.field150.getValue()
                && this.field173 != null
                && this.field174 != null
                && !this.field175
                .hasElapsed(
                        !this.field164.getValue() && !this.field165.getValue() ? 1000.0 : (double) ((this.field166.getValue() + this.field167.getValue()) * 50.0F)
                )
                && (this.field146.autoCrystalTracker.field1534 != null || this.field164.getValue() || this.field165.getValue())) {
            Box var5;
            if (this.field161.getValue()) {
                var5 = Class3071.method6021(this.field174, this.field173, event.tickDelta);
            } else {
                var5 = this.field171;
            }

            Frustum var6 = ((WorldRendererAccessor) mc.worldRenderer).getFrustum();
            if (var6 == null || var6.isVisible(var5)) {
                Vector3d var7 = new Vector3d(var5.getCenter().toVector3f());
                boolean var8 = Class5922.method59(var7, this.field151);
                if (var8) {
                    this.method92(var7, this.field172);
                }
            }
        }
    }

    private void method92(Vector3d var1, double var2) {
        Class5922.method61(var1);
        String var6 = String.format("%.1f", var2);
        IFontRender.method499().startBuilding(1.5);
        double var7 = IFontRender.method499().method501(var6) / 2.0;
        IFontRender.method499().drawShadowedText(var6, -var7, -(IFontRender.method499().method1390() / 2.0), RGBAColor.field402);
        IFontRender.method499().endBuilding();
        Class5922.method2142();
    }

    @Override
    public Setting<?>[] get() {
        return this.field168.method472();
    }

    private void lambda$onRender3D$4(Render3DEvent var1) {
        this.field170.method1219(var1.matrix);
    }

    private boolean lambda$new$3() {
        return this.field164.getValue() || this.field165.getValue();
    }

    private boolean lambda$new$2() {
        return this.field164.getValue() || this.field165.getValue();
    }

    private boolean lambda$new$1() {
        return this.field153.getValue() == AutoCrystalShaderMode.Image;
    }

    private boolean lambda$new$0() {
        return this.field156.getValue() > 0.0F;
    }
}
