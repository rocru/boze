package mapped;

import dev.boze.api.input.Bind;

public class Class2783 implements Bind {
    private final dev.boze.client.utils.Bind field97;

    public Class2783(dev.boze.client.utils.Bind keybind) {
        this.field97 = keybind;
    }

    public int getBind() {
        return this.field97.getBind();
    }

    public boolean isButton() {
        return !this.field97.isKey();
    }
}
