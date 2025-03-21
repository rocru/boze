package dev.boze.client.instances.impl;

import dev.boze.api.internal.interfaces.IRender;
import dev.boze.api.render.DrawColor;
import dev.boze.client.api.BozeDrawColor;

public class RenderInstance implements IRender {
    public DrawColor newColor() {
        return new BozeDrawColor();
    }

    public DrawColor newColor(int rgba) {
        return new BozeDrawColor(rgba);
    }

    public DrawColor newColor(int r, int g, int b, int a) {
        return new BozeDrawColor(r, g, b, a);
    }
}
