package dev.boze.client.api;

import dev.boze.api.BozeInstance;
import dev.boze.api.event.EventHudRender;
import dev.boze.api.event.EventPlayerUpdate;
import dev.boze.api.event.EventWorldRender;
import dev.boze.api.interaction.Interaction;
import dev.boze.api.interaction.PlaceInteraction;
import dev.boze.api.interaction.RotateInteraction;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.utils.ActionWrapper;
import dev.boze.client.utils.PlaceAction;
import meteordevelopment.orbit.EventHandler;

public class APIEventHandler {
    @EventHandler
    public static void method948(MovementEvent event) {
        EventPlayerUpdate var4 = EventPlayerUpdate.get(!event.field1933.isEmpty());
        BozeInstance.INSTANCE.post(var4);

        for (Interaction var6 : var4.interactions) {
            if (var6 instanceof PlaceInteraction) {
                event.method1074(new ActionWrapper(new PlaceAction((PlaceInteraction) var6)));
            } else if (var6 instanceof RotateInteraction) {
                event.method1074(new ActionWrapper((RotateInteraction) var6));
            } else {
                event.method1074(new ActionWrapper(var6::execute));
            }
        }
    }

    @EventHandler
    public static void method949(Render3DEvent event) {
        BozeInstance.INSTANCE.post(EventWorldRender.get(BozeDrawer3D.method968(), event.matrix, event.camera, event.field1951));
    }

    @EventHandler
    public static void method950(Render2DEvent event) {
        BozeInstance.INSTANCE.post(EventHudRender.get(event.field1947, BozeDrawer2D.method967(), BozeDrawerText.method969(), event.tickDelta));
    }
}
