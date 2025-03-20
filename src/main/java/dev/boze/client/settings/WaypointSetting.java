package dev.boze.client.settings;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.boze.client.command.arguments.PlayerListArgument;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.waypoints.WayPoint;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;

import java.util.ArrayList;
import java.util.Locale;
import java.util.function.BooleanSupplier;

public class WaypointSetting extends Setting<ArrayList<WayPoint>> implements IMinecraft {
    private ArrayList<WayPoint> field924;

    public WaypointSetting(String name, ArrayList<WayPoint> value, String description) {
        super(name, description);
        this.field924 = value;
    }

    public WaypointSetting(String name, ArrayList<WayPoint> value, String description, BooleanSupplier visibility) {
        super(name, description, visibility);
        this.field924 = value;
    }

    public WaypointSetting(String name, ArrayList<WayPoint> value, String description, Setting parent) {
        super(name, description, parent);
        this.field924 = value;
    }

    public WaypointSetting(String name, ArrayList<WayPoint> value, String description, BooleanSupplier visibility, Setting parent) {
        super(name, description, visibility, parent);
        this.field924 = value;
    }

    @Override
    public ArrayList<WayPoint> getValue() {
        return this.field924;
    }

    @Override
    public ArrayList<WayPoint> resetValue() {
        this.field924.clear();
        return this.field924;
    }

    @Override
    public ArrayList<WayPoint> setValue(ArrayList<WayPoint> newVal) {
        return this.field924 = newVal;
    }

    @Override
    public boolean buildCommand(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(
                                method403("add")
                                        .then(
                                                method402("name", StringArgumentType.string())
                                                        .then(
                                                                method402("x", IntegerArgumentType.integer())
                                                                        .then(
                                                                                method402("y", IntegerArgumentType.integer())
                                                                                        .then(method402("z", IntegerArgumentType.integer()).executes(this::lambda$build$0))
                                                                        )
                                                        )
                                        )
                        )
        );
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(method403("add").then(method402("name", StringArgumentType.string()).executes(this::lambda$build$1)))
        );
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(method403("del").then(method402("name", StringArgumentType.string()).executes(this::lambda$build$3)))
        );
        builder.then(
                method403(this.method210().toLowerCase(Locale.ROOT))
                        .then(
                                method403("share")
                                        .then(method402("name", StringArgumentType.string()).then(method402("player", PlayerListArgument.method678()).executes(this::lambda$build$4)))
                        )
        );
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("list").executes(this::lambda$build$6)));
        builder.then(method403(this.method210().toLowerCase(Locale.ROOT)).then(method403("clear").executes(this::lambda$build$8)));
        return true;
    }

    @Override
    public NbtCompound save(NbtCompound tag) {
        return tag;
    }

    @Override
    public ArrayList<WayPoint> load(NbtCompound tag) {
        if (tag.contains("Waypoints")) {
            for (NbtElement var7 : tag.getList("Waypoints", 10)) {
                if (var7 instanceof NbtCompound) {
                    WayPoint var8 = new WayPoint().fromTag((NbtCompound) var7);
                    if (!this.field924.contains(var8)) {
                        this.field924.add(var8);
                    }
                }
            }
        }

        return this.field924;
    }

    @Override
    public JsonObject serialize() {
        JsonObject jsonObject = super.serialize();
        JsonArray jsonArray = new JsonArray();
        this.field924.forEach(arg_0 -> WaypointSetting.lambda$saveLocalData$9(jsonArray, arg_0));
        jsonObject.add("waypoints", jsonArray);
        return jsonObject;
    }

    @Override
    public Setting<ArrayList<WayPoint>> deserialize(JsonObject data) {
        if (data.has("waypoints")) {
            JsonArray var5 = data.getAsJsonArray("waypoints");
            var5.forEach(this::lambda$loadLocalData$10);
        }

        return super.deserialize(data);
    }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //  public Object load(NbtCompound nbtCompound) {
    //    return this.method407(nbtCompound);
    // }
//
    // $VF: synthetic method
    //  // $VF: bridge method
    //  @Override
    //  public Object setValue(Object object) {
    //    return this.method406((ArrayList<WayPoint>)object);
    // }
//
    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //public Object resetValue() {
    //    return this.method405();
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    // public Object getValue() {
    //    return this.method2120();
    // }

    // $VF: synthetic method
    // $VF: bridge method
    // @Override
    //public Object deserialize(JsonObject jsonObject) {
    //    return this.deserialize(jsonObject);
    // }

    private void lambda$loadLocalData$10(JsonElement var1) {
        WayPoint var5 = new WayPoint().deserialize(var1.getAsJsonObject());
        if (!this.field924.contains(var5)) {
            this.field924.add(var5);
        }
    }

    private static void lambda$saveLocalData$9(JsonArray var0, WayPoint var1) {
        var0.add(var1.serialize());
    }

    private int lambda$build$8(CommandContext var1) throws CommandSyntaxException {
        ChatInstance.method624("Clearing all waypoints for the current server/dimension...");
        this.field924.removeIf(WaypointSetting::lambda$build$7);
        return 1;
    }

    private static boolean lambda$build$7(WayPoint var0) {
        return var0.field912.equals(mc.world.getRegistryKey().getValue().getPath()) && var0.field913.equalsIgnoreCase(mc.getServer().getServerIp());
    }

    private int lambda$build$6(CommandContext var1) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null && !mc.getCurrentServerEntry().address.isEmpty() && mc.world != null
        ) {
            ChatInstance.method624("Waypoints: ");
            this.field924.forEach(WaypointSetting::lambda$build$5);
            return 1;
        } else {
            return 0;
        }
    }

    private static void lambda$build$5(WayPoint var0) {
        if (var0.field912.equals(mc.world.getRegistryKey().getValue().getPath()) && var0.field913.equalsIgnoreCase(mc.getCurrentServerEntry().address)) {
            int var4 = var0.field911;
            int var5 = var0.field910;
            int var6 = var0.field909;
            String var7 = var0.field908;
            ChatInstance.method624(" - " + var7 + " X" + var6 + " Y" + var5 + " Z" + var4);
        }
    }

    private int lambda$build$4(CommandContext var1) throws CommandSyntaxException {
        PlayerListEntry var5 = PlayerListArgument.method679(var1);
        String var6 = var5.getProfile().getName();
        String var7 = (String) var1.getArgument("name", String.class);

        for (WayPoint var9 : this.field924) {
            if (var9.field908.equalsIgnoreCase(var7) && var9.field913.equalsIgnoreCase(mc.getCurrentServerEntry().address)) {
                int var10001 = var9.field909;
                int var10002 = var9.field910;
                int var10003 = var9.field911;
                String var10 = var9.field912.replace("_", " ");
                int var11 = var10003;
                int var12 = var10002;
                int var13 = var10001;
                ChatInstance.method1800("/msg " + var6 + " I shared a waypoint at X" + var13 + " Y" + var12 + " Z" + var11 + " in " + var10 + " with you");
                break;
            }
        }

        return 1;
    }

    private int lambda$build$3(CommandContext commandContext) throws CommandSyntaxException {
        String string = (String) commandContext.getArgument("name", String.class);
        this.field924.removeIf(arg_0 -> WaypointSetting.lambda$build$2(string, arg_0));
        return 1;
    }

    private static boolean lambda$build$2(String var0, WayPoint var1) {
        return var1.field908.equalsIgnoreCase(var0);
    }

    private int lambda$build$1(CommandContext var1) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null
                && mc.getCurrentServerEntry().address != null
                && mc.player != null
                && !mc.getCurrentServerEntry().address.isEmpty()
                && mc.world != null) {
            this.field924
                    .add(
                            new WayPoint(
                                    (String) var1.getArgument("name", String.class),
                                    (int) mc.player.getX(),
                                    (int) mc.player.getY(),
                                    (int) mc.player.getZ(),
                                    mc.world.getRegistryKey().getValue().getPath(),
                                    mc.getCurrentServerEntry().address
                            )
                    );
            return 1;
        } else {
            return 0;
        }
    }

    private int lambda$build$0(CommandContext var1) throws CommandSyntaxException {
        if (mc.getCurrentServerEntry() != null && mc.getCurrentServerEntry().address != null && !mc.getCurrentServerEntry().address.isEmpty() && mc.world != null
        ) {
            this.field924
                    .add(
                            new WayPoint(
                                    (String) var1.getArgument("name", String.class),
                                    (Integer) var1.getArgument("x", Integer.class),
                                    (Integer) var1.getArgument("y", Integer.class),
                                    (Integer) var1.getArgument("z", Integer.class),
                                    mc.world.getRegistryKey().getValue().getPath(),
                                    mc.getCurrentServerEntry().address
                            )
                    );
            return 1;
        } else {
            return 0;
        }
    }
}
