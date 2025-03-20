package dev.boze.client.utils.click;

import dev.boze.client.settings.FloatSetting;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.Timer;

public class ClickImplementationB extends IClickMethod implements IMinecraft {
    private final Timer field1327 = new Timer();
    private final FloatSetting field1328;

    public ClickImplementationB(FloatSetting offset) {
        this.field1328 = offset;
    }

    @Override
    public int method578(double targetedCPS) {
        if (mc.player.getAttackCooldownProgress(-this.field1328.method423() * mc.player.getAttackCooldownProgressPerTick() * 0.5F) < 1.0F) {
            this.field1327.reset();
            return 0;
        } else {
            return this.field1327.hasElapsed(1000.0 / targetedCPS) ? 1 : 0;
        }
    }

    @Override
    public void method938(double targetedCPS) {
    }
}
