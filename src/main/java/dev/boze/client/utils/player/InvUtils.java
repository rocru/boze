package dev.boze.client.utils.player;

import dev.boze.client.utils.IMinecraft;
import net.minecraft.screen.slot.SlotActionType;

public class InvUtils implements IMinecraft {
    private static final Action field3949 = new Action();

    public static Action method2201() {
        field3949.field3950 = SlotActionType.PICKUP;
        field3949.field3951 = true;
        return field3949;
    }

    public static Action method2202() {
        field3949.field3950 = SlotActionType.SWAP;
        field3949.field3951 = true;
        return field3949;
    }

    public static Action method2203() {
        field3949.field3950 = SlotActionType.PICKUP;
        return field3949;
    }

    public static Action method2204() {
        field3949.field3950 = SlotActionType.QUICK_MOVE;
        return field3949;
    }

    public static Action method2205() {
        field3949.field3950 = SlotActionType.THROW;
        field3949.field3954 = 1;
        return field3949;
    }

    public static void dropHand() {
        if (!mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, -999, 0, SlotActionType.PICKUP, mc.player);
        }
    }

    public static final class Action {
        private SlotActionType field3950 = null;
        private boolean field3951 = false;
        private int field3952 = -1;
        private int field3953 = -1;
        private int field3954 = 0;
        private boolean field3955 = false;

        private Action() {
        }

        public Action method2206(int id) {
            this.field3952 = id;
            return this;
        }

        public Action method2207(int index) {
            return this.method2206(SlotUtils.method1541(index));
        }

        public Action method2208(int i) {
            return this.method2207(i);
        }

        public Action method2209() {
            return this.method2207(45);
        }

        public Action method2210(int i) {
            return this.method2207(9 + i);
        }

        public Action method2211(int i) {
            return this.method2207(36 + (3 - i));
        }

        public void method2212(int id) {
            this.field3953 = id;
            this.method2225();
        }

        public void method2213(int index) {
            this.method2212(SlotUtils.method1541(index));
        }

        public void method2214(int index) {
            this.method2212(index);
        }

        public void method2215(int i) {
            this.method2213(i);
        }

        public void method2216() {
            this.method2213(45);
        }

        public void method2217(int i) {
            this.method2213(9 + i);
        }

        public void method2218(int i) {
            this.method2213(36 + (3 - i));
        }

        public void method2219(int id) {
            this.field3952 = this.field3953 = id;
            this.method2225();
        }

        public void method2220(int index) {
            this.method2219(SlotUtils.method1541(index));
        }

        public void method2221(int i) {
            this.method2220(i);
        }

        public void method2222() {
            this.method2220(45);
        }

        public void method2223(int i) {
            this.method2220(9 + i);
        }

        public void method2224(int i) {
            this.method2220(36 + (3 - i));
        }

        private void method2225() {
            boolean var4 = IMinecraft.mc.player.currentScreenHandler.getCursorStack().isEmpty();
            if (this.field3950 != null && this.field3952 != -1 && this.field3953 != -1) {
                if (InvUtils.field3949.field3950 == SlotActionType.SWAP) {
                    IMinecraft.mc
                            .interactionManager
                            .clickSlot(IMinecraft.mc.player.currentScreenHandler.syncId, this.field3952, this.field3953, SlotActionType.SWAP, IMinecraft.mc.player);
                } else {
                    this.method2226(this.field3952);
                    if (this.field3951) {
                        this.method2226(this.field3953);
                    }
                }
            }

            SlotActionType var5 = this.field3950;
            boolean var6 = this.field3951;
            int var7 = this.field3952;
            int var8 = this.field3953;
            this.field3950 = null;
            this.field3951 = false;
            this.field3952 = -1;
            this.field3953 = -1;
            this.field3954 = 0;
            if (!this.field3955
                    && var4
                    && var5 == SlotActionType.PICKUP
                    && var6
                    && var7 != -1
                    && var8 != -1
                    && !IMinecraft.mc.player.currentScreenHandler.getCursorStack().isEmpty()) {
                this.field3955 = true;
                InvUtils.method2203().method2219(var7);
                this.field3955 = false;
            }
        }

        private void method2226(int var1) {
            IMinecraft.mc.interactionManager.clickSlot(IMinecraft.mc.player.currentScreenHandler.syncId, var1, this.field3954, this.field3950, IMinecraft.mc.player);
        }
    }
}
