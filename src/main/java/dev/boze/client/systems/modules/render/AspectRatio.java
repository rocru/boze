package dev.boze.client.systems.modules.render;

import dev.boze.client.settings.FloatSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;

public class AspectRatio extends Module {
    public static final AspectRatio INSTANCE = new AspectRatio();
    public final FloatSetting field3380 = new FloatSetting("Ratio", 1.5F, 0.1F, 3.0F, 0.1F, "Aspect ratio");
    public final FloatSetting field3381 = new FloatSetting("CameraDepth", 0.05F, 0.01F, 1.0F, 0.01F, "Camera depth");
    public final FloatSetting field3382 = new FloatSetting("ViewDistMod", 1.0F, 0.1F, 2.0F, 0.1F, "View distance modifier");
    public final FloatSetting field3383 = new FloatSetting("FovMod", 1.0F, 0.1F, 2.0F, 0.1F, "FOV modifier");

    private AspectRatio() {
        super("AspectRatio", "Changes the aspect ratio of the game", Category.Render);
    }
}
