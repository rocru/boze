package mapped;

import net.minecraft.client.gui.DrawContext;

public abstract class Class2770 {
    protected double field68;
    protected double field69;
    protected double field70;
    protected double field71;
    protected final Class2770[] field72;
    protected boolean field73 = false;

    public Class2770(Class2770... children) {
        this.field72 = children;
    }

    public double method5410() {
        return this.field68;
    }

    public double method5411() {
        return this.field69;
    }

    public double method5412() {
        return this.field70;
    }

    public double method5413() {
        return this.field71;
    }

    public boolean method5414() {
        return true;
    }

    public boolean method5415() {
        return this.method5414();
    }

    public boolean method5416() {
        return this.method5414();
    }

    public boolean method5417(int mouseX, int mouseY) {
        if (!this.method5414()) {
            boolean var11 = this.field73;
            this.field73 = false;
            return var11;
        } else {
            boolean var6 = !this.field73;
            this.field73 = true;

            for (Class2770 var10 : this.field72) {
                if (var10.method5417(mouseX, mouseY)) {
                    var6 = true;
                }
            }

            return var6;
        }
    }

    public void method5418(DrawContext context, int mouseX, int mouseY) {
        if (this.method5415()) {
            for (Class2770 var10 : this.field72) {
                var10.method5418(context, mouseX, mouseY);
            }
        }
    }

    public void method5419(DrawContext context, int mouseX, int mouseY) {
        if (this.method5415()) {
            for (Class2770 var10 : this.field72) {
                var10.method5419(context, mouseX, mouseY);
            }
        }
    }

    public void method5420(DrawContext context, int mouseX, int mouseY) {
    }

    public boolean method5421(int mouseX, int mouseY, int button) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var10 : this.field72) {
                if (var10.method5421(mouseX, mouseY, button)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean method5422(int mouseX, int mouseY, int button) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var10 : this.field72) {
                if (var10.method5422(mouseX, mouseY, button)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean method5423(int mouseX, int mouseY, int button, double deltaX, double deltaY) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var14 : this.field72) {
                if (var14.method5423(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean method5424(int mouseX, int mouseY, double scrollAmount) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var11 : this.field72) {
                if (var11.method5424(mouseX, mouseY, scrollAmount)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean method5425(int keyCode, int scanCode, int modifiers) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var10 : this.field72) {
                if (var10.method5425(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean method5426(int keyCode, int scanCode, int modifiers) {
        if (!this.method5416()) {
            return false;
        } else {
            for (Class2770 var10 : this.field72) {
                if (var10.method5426(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }

            return false;
        }
    }
}
