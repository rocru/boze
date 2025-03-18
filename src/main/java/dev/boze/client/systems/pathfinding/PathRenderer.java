package dev.boze.client.systems.pathfinding;

import dev.boze.client.events.Render3DEvent;
import dev.boze.client.utils.RGBAColor;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class PathRenderer {
    public static void method2129(ArrayList<PathPos> path, Render3DEvent event) {
        for (int var5 = 0; var5 < path.size() - 1; var5++) {
            Vec3d var6 = path.get(var5).toCenterPos();
            Vec3d var7 = path.get(var5 + 1).toCenterPos();
            event.field1950.method1234(var6, var7, RGBAColor.field406);
        }
    }
}
