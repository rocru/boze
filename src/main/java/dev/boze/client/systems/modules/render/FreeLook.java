package dev.boze.client.systems.modules.render;

import dev.boze.client.events.PostRender;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Options;
import dev.boze.client.utils.MinecraftUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.math.MathHelper;

public class FreeLook extends Module {
    public static final FreeLook INSTANCE = new FreeLook();
    private final BooleanSetting field3558 = new BooleanSetting(
            "Ghost", false, "Ghost mode\nWon't let you rotate your player with arrow keys\n", FreeLook::lambda$new$0
    );
    private float field3559;
    private float field3560;
    private float field3561;
    private float field3562;
    private Perspective field3563 = null;
    private long field3564 = 0L;

    public FreeLook() {
        super("FreeLook", "Look around in third person, without rotating player", Category.Render);
    }

    private static boolean lambda$new$0() {
        return !Options.INSTANCE.method1971();
    }

    @Override
    public void onEnable() {
        if (MinecraftUtils.isClientActive()) {
            this.field3561 = this.field3559 = mc.player.getYaw();
            this.field3562 = this.field3560 = mc.player.getPitch();
            this.field3563 = mc.options.getPerspective();
            mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
        }
    }

    @Override
    public void onDisable() {
        if (this.field3563 != null && MinecraftUtils.isClientActive()) {
            mc.options.setPerspective(this.field3563);
            this.field3563 = null;
        }
    }

    public void method1955(double dX, double dY) {
        this.field3559 = this.field3561;
        this.field3560 = this.field3562;
        this.field3561 = (float) ((double) this.field3561 + dX);
        this.field3562 = (float) ((double) this.field3562 + dY);
        this.field3562 = MathHelper.clamp(this.field3562, -90.0F, 90.0F);
    }

    public float method1956(float tickDelta) {
        return tickDelta == 1.0F ? this.field3562 : MathHelper.lerp(tickDelta, this.field3560, this.field3562);
    }

    public float method1957(float tickDelta) {
        return tickDelta == 1.0F ? this.field3561 : MathHelper.lerp(tickDelta, this.field3559, this.field3561);
    }

    @EventHandler
    public void method1958(PostRender event) {
        if (MinecraftUtils.isClientActive()) {
            if (!this.field3558.getValue() && !Options.INSTANCE.method1971()) {
                if (mc.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
                    mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
                }

                float var5 = (float) (System.currentTimeMillis() - this.field3564) / 5.0F;
                this.field3564 = System.currentTimeMillis();
                float var6 = 0.0F;
                float var7 = 0.0F;
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 263)) {
                    var6 -= var5;
                }

                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 262)) {
                    var6 += var5;
                }

                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 265)) {
                    var7 -= var5;
                }

                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), 264)) {
                    var7 += var5;
                }

                mc.player.setYaw(mc.player.getYaw() + var6);
                mc.player.setPitch(MathHelper.clamp(mc.player.getPitch() + var7, -90.0F, 90.0F));
            }
        }
    }
}
