package mapped;

import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class Class5927 implements IMinecraft {
    private static final List<FakePlayerEntity> field43;

    public Class5927() {
        super();
    }

    public static void method102(final String name, final float health, final boolean copyInventory) {
        final FakePlayerEntity fakePlayerEntity = new FakePlayerEntity(Class5927.mc.player, name, health, copyInventory);
        fakePlayerEntity.method2142();
        Class5927.field43.add(fakePlayerEntity);
    }

    public static void method2142() {
        if (Class5927.field43.isEmpty()) {
            return;
        }
        Class5927.field43.forEach(FakePlayerEntity::method1416);
        Class5927.field43.clear();
    }

    public static List<FakePlayerEntity> method1144() {
        return Class5927.field43;
    }

    public static int method2010() {
        return Class5927.field43.size();
    }

    static {
        field43 = new ArrayList<FakePlayerEntity>();
    }
}
