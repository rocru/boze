package dev.boze.client.utils;

import dev.boze.client.enums.SwapMode;
import dev.boze.client.systems.modules.Module;

public class SwapHandler implements IMinecraft {
    private final Module field1622;
    private final int field1623;
    private int field1624 = -1;
    private int field1625 = -1;

    public SwapHandler(Module module, int priority) {
        this.field1622 = module;
        this.field1623 = priority;
    }

    public boolean method723(SwapMode mode, int slot) {
        if (slot == this.field1624) {
            return true;
        } else {
            if (this.field1624 != -1) {
                InventoryUtil.method396(this.field1622);
            }

            if (InventoryUtil.method534(this.field1622, this.field1623, mode, slot)) {
                this.field1624 = slot;
                return true;
            } else {
                this.field1624 = -1;
                return false;
            }
        }
    }

    public void method2142() {
        if (this.field1624 != -1) {
            this.field1625 = this.field1624;
            mc.player.getInventory().selectedSlot = this.field1624;
        }
    }

    public void method1416() {
        if (this.field1625 != -1) {
            mc.player.getInventory().selectedSlot = this.field1625;
            this.field1625 = -1;
        }

        if (this.field1624 != -1) {
            InventoryUtil.method396(this.field1622);
            this.field1624 = -1;
        }
    }
}
