package dev.boze.client.manager;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.systems.modules.client.OldColors;

public abstract class ColorManager {
    public final BozeDrawColor field414;
    public final boolean field415;
    private BozeDrawColor field412;
    private boolean field413;

    protected ColorManager(BozeDrawColor color) {
        this.field412 = color;
        this.field414 = color.copy();
        this.field413 = false;
        this.field415 = false;
    }

    protected ColorManager(BozeDrawColor color, boolean sync) {
        this.field412 = color;
        this.field413 = sync;
        this.field414 = color.copy();
        this.field415 = sync;
    }

    public BozeDrawColor method1362() {
        if (this.field413) {
            BozeDrawColor var4 = OldColors.INSTANCE.clientGradient.getValue();
            return new BozeDrawColor(
                    var4.field408, var4.field409, var4.field410, this.field412.field411, var4.field1842, var4.field1843, var4.field1844, var4.field1845, var4.field1846
            );
        } else {
            return this.field412;
        }
    }

    public BozeDrawColor method1374() {
        return this.field412;
    }

    public void method205(BozeDrawColor color) {
        this.field412 = color;
    }

    public boolean method2114() {
        return this.field413;
    }

    public void method67(boolean sync) {
        this.field413 = sync;
    }
}
