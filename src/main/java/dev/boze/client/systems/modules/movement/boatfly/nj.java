package dev.boze.client.systems.modules.movement.boatfly;

import dev.boze.client.events.*;
import dev.boze.client.systems.modules.movement.BoatFly;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;

public abstract class nj implements IMinecraft {
    protected final BoatFly field1556;

    public nj(BoatFly module) {
        this.field1556 = module;
    }

    public abstract void method2142();

    public abstract void method1416();

    public abstract void method2042(PacketBundleEvent var1);

    public abstract void method1853(PrePacketSendEvent var1);

    public void method1794(PlayerTravelEvent var1) {
    }

    public void method2072(PreTickEvent var1) {
    }

    public boolean method2114() {
        return false;
    }

    public boolean method2115() {
        return false;
    }

    public boolean method1798(VehicleMoveS2CPacket var1) {
        return false;
    }

    public void method2071(Render3DEvent var1) {
    }
}
