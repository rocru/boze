package dev.boze.client.systems.modules.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.BreakHighlightMode;
import dev.boze.client.enums.BreakHighlightShader;
import dev.boze.client.enums.ShaderMode;
import dev.boze.client.enums.ShapeMode;
import dev.boze.client.events.MovementEvent;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.events.Render3DEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.notification.Notification;
import dev.boze.client.gui.notification.NotificationPriority;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.manager.NotificationManager;
import dev.boze.client.mixin.ClientPlayerInteractionManagerAccessor;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.renderer.Renderer3D;
import dev.boze.client.renderer.packer.ByteTexturePacker;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.ByteTexture;
import dev.boze.client.utils.trackers.BlockBreakingTracker;
import mapped.Class5922;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Vector3d;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class BreakHighlight extends Module {
    public static final BreakHighlight INSTANCE = new BreakHighlight();
    private final BooleanSetting field3434 = new BooleanSetting("Predict", false, "Predicts block breaking");
    private final BooleanSetting field3435 = new BooleanSetting("BreakAlert", false, "Alerts you when a block besides is being broken");
    private final BooleanSetting field3436 = new BooleanSetting("Self", false, "Highlights blocks you're breaking");
    private final EnumSetting<BreakHighlightMode> field3437 = new EnumSetting<BreakHighlightMode>("Mode", BreakHighlightMode.Grow, "Render mode");
    public final ColorSetting field3438 = new ColorSetting("Color", new BozeDrawColor(1687452627), "Color for fill");
    public final ColorSetting field3439 = new ColorSetting("Outline", new BozeDrawColor(-7046189), "Color for outline");
    private final BooleanSetting field3440 = new BooleanSetting("Shader", false, "Use a shader");
    public final EnumSetting<BreakHighlightShader> field3441 = new EnumSetting<BreakHighlightShader>(
            "Shader", BreakHighlightShader.Normal, "Shader to use", this.field3440
    );
    public final BooleanSetting field3442 = new BooleanSetting("FastRender", true, "Make the shader render faster at the cost of quality", this.field3440);
    public final IntSetting field3443 = new IntSetting("Blur", 0, 0, 5, 1, "Glow for shader", this.field3440);
    public final FloatSetting field3444 = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.field3440);
    public final FloatSetting field3445 = new FloatSetting("Strength", 0.1F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$0, this.field3440);
    private final IntSetting field3446 = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.field3440);
    private final FloatSetting field3447 = new FloatSetting("Opacity", 0.3F, 0.0F, 1.0F, 0.01F, "Fill opacity for shader", this.field3440);
    public final StringSetting field3448 = new StringSetting("Fill", "", "Fill for image shader", this::lambda$new$1, this.field3440);
    private final BooleanSetting field3449 = new BooleanSetting("Percentage", false, "Show mining percentage");
    private final BooleanSetting field3450 = new BooleanSetting("Player", false, "Show mining player's name");
    private final ScalingSetting field3451 = new ScalingSetting();
    private Renderer3D field3452;
    private ByteTexture field3453;
    private String field3454 = "";
    private final ConcurrentHashMap<BlockPos, Long> field3455 = new ConcurrentHashMap();
    private final ConcurrentHashMap<BlockPos, Long> field3456 = new ConcurrentHashMap();

    public BreakHighlight() {
        super("BreakHighlight", "Highlights blocks being broken", Category.Render);
        this.field3451.field2234.setVisibility(this::lambda$new$2);
    }

    @EventHandler
    public void method1914(MovementEvent event) {
        this.field3455.forEach(this::lambda$onSendMovementPackets$3);
    }

    @EventHandler
    public void method1915(Render2DEvent event) {
        if (this.field3449.getValue() || this.field3450.getValue()) {
            for (Entry var6 : BlockBreakingTracker.field1511.method666(this.field3434.getValue()).entrySet()) {
                if ((!this.field3436.getValue() || !this.field3449.getValue())
                        && var6.getKey().equals(((ClientPlayerInteractionManagerAccessor) mc.interactionManager).getCurrentBreakingPos())) {
                    return;
                }

                BlockState var7 = mc.world.getBlockState((BlockPos) var6.getKey());
                VoxelShape var8 = var7.getOutlineShape(mc.world, (BlockPos) var6.getKey());
                if (var8.isEmpty()) {
                    return;
                }

                Box var9 = var8.getBoundingBox().offset((BlockPos) var6.getKey());
                Frustum var10 = ((WorldRendererAccessor) mc.worldRenderer).getFrustum();
                if (var10 == null || var10.isVisible(var9)) {
                    Vec3d var11 = var9.getCenter();
                    Vector3d var12 = new Vector3d(var11.x, var11.y, var11.z);
                    boolean var13 = Class5922.method59(var12, this.field3451);
                    if (var13) {
                        double var14 = -1.0;
                        int var16 = -1;
                        if (this.field3449.getValue()) {
                            var14 = ((Float) var6.getValue()).floatValue();
                        }

                        if (this.field3450.getValue()) {
                            var16 = BlockBreakingTracker.field1511.method667((BlockPos) var6.getKey());
                        }

                        this.method1917(var12, var14, var16);
                    }
                }
            }
        }
    }

    @EventHandler
    public void method1916(Render3DEvent event) {
        // $VF: Couldn't be decompiled
        // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
        // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
        //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
        //
        // Bytecode:
        // 000: aload 0
        // 001: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 004: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 007: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 00a: ifeq 028
        // 00d: aload 0
        // 00e: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 011: ifnonnull 021
        // 014: aload 0
        // 015: new dev/boze/client/renderer/Renderer3D
        // 018: dup
        // 019: bipush 0
        // 01a: bipush 1
        // 01b: invokespecial dev/boze/client/renderer/Renderer3D.<init> (ZZ)V
        // 01e: putfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 021: aload 0
        // 022: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 025: invokevirtual dev/boze/client/renderer/Renderer3D.method1217 ()V
        // 028: getstatic dev/boze/client/utils/trackers/BlockBreakingTracker.field1511 Ldev/boze/client/utils/trackers/BlockBreakingTracker;
        // 02b: aload 0
        // 02c: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3434 Ldev/boze/client/settings/BooleanSetting;
        // 02f: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 032: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 035: invokevirtual dev/boze/client/utils/trackers/BlockBreakingTracker.method666 (Z)Ljava/util/HashMap;
        // 038: invokevirtual java/util/HashMap.entrySet ()Ljava/util/Set;
        // 03b: invokeinterface java/util/Set.iterator ()Ljava/util/Iterator; 1
        // 040: astore 5
        // 042: aload 5
        // 044: invokeinterface java/util/Iterator.hasNext ()Z 1
        // 049: ifeq 34b
        // 04c: aload 5
        // 04e: invokeinterface java/util/Iterator.next ()Ljava/lang/Object; 1
        // 053: checkcast java/util/Map$Entry
        // 056: astore 6
        // 058: aload 0
        // 059: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3436 Ldev/boze/client/settings/BooleanSetting;
        // 05c: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 05f: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 062: ifne 084
        // 065: aload 6
        // 067: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 06c: checkcast net/minecraft/util/math/BlockPos
        // 06f: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 072: getfield net/minecraft/client/MinecraftClient.interactionManager Lnet/minecraft/client/network/ClientPlayerInteractionManager;
        // 075: checkcast dev/boze/client/mixin/ClientPlayerInteractionManagerAccessor
        // 078: invokeinterface dev/boze/client/mixin/ClientPlayerInteractionManagerAccessor.getCurrentBreakingPos ()Lnet/minecraft/util/math/BlockPos; 1
        // 07d: invokevirtual net/minecraft/util/math/BlockPos.equals (Ljava/lang/Object;)Z
        // 080: ifeq 084
        // 083: return
        // 084: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 087: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
        // 08a: aload 6
        // 08c: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 091: checkcast net/minecraft/util/math/BlockPos
        // 094: invokevirtual net/minecraft/client/world/ClientWorld.getBlockState (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;
        // 097: astore 7
        // 099: aload 7
        // 09b: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 09e: getfield net/minecraft/client/MinecraftClient.world Lnet/minecraft/client/world/ClientWorld;
        // 0a1: aload 6
        // 0a3: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 0a8: checkcast net/minecraft/util/math/BlockPos
        // 0ab: invokevirtual net/minecraft/block/BlockState.getOutlineShape (Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;
        // 0ae: astore 8
        // 0b0: aload 8
        // 0b2: invokevirtual net/minecraft/util/shape/VoxelShape.isEmpty ()Z
        // 0b5: ifeq 0b9
        // 0b8: return
        // 0b9: aload 8
        // 0bb: invokevirtual net/minecraft/util/shape/VoxelShape.getBoundingBox ()Lnet/minecraft/util/math/Box;
        // 0be: astore 9
        // 0c0: aload 0
        // 0c1: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3435 Ldev/boze/client/settings/BooleanSetting;
        // 0c4: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 0c7: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 0ca: ifeq 1d4
        // 0cd: aload 6
        // 0cf: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 0d4: checkcast net/minecraft/util/math/BlockPos
        // 0d7: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 0da: getfield net/minecraft/client/MinecraftClient.interactionManager Lnet/minecraft/client/network/ClientPlayerInteractionManager;
        // 0dd: checkcast dev/boze/client/mixin/ClientPlayerInteractionManagerAccessor
        // 0e0: invokeinterface dev/boze/client/mixin/ClientPlayerInteractionManagerAccessor.getCurrentBreakingPos ()Lnet/minecraft/util/math/BlockPos; 1
        // 0e5: invokevirtual net/minecraft/util/math/BlockPos.equals (Ljava/lang/Object;)Z
        // 0e8: ifne 1d4
        // 0eb: aload 9
        // 0ed: aload 6
        // 0ef: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 0f4: checkcast net/minecraft/util/math/BlockPos
        // 0f7: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 0fa: invokevirtual net/minecraft/util/math/Box.getCenter ()Lnet/minecraft/util/math/Vec3d;
        // 0fd: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 100: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
        // 103: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getPos ()Lnet/minecraft/util/math/Vec3d;
        // 106: invokevirtual net/minecraft/util/math/Vec3d.distanceTo (Lnet/minecraft/util/math/Vec3d;)D
        // 109: ldc2_w 1.5
        // 10c: dcmpg
        // 10d: ifge 1d4
        // 110: aload 0
        // 111: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3455 Ljava/util/concurrent/ConcurrentHashMap;
        // 114: aload 6
        // 116: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 11b: invokevirtual java/util/concurrent/ConcurrentHashMap.containsKey (Ljava/lang/Object;)Z
        // 11e: ifeq 140
        // 121: invokestatic java/lang/System.currentTimeMillis ()J
        // 124: aload 0
        // 125: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3455 Ljava/util/concurrent/ConcurrentHashMap;
        // 128: aload 6
        // 12a: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 12f: invokevirtual java/util/concurrent/ConcurrentHashMap.get (Ljava/lang/Object;)Ljava/lang/Object;
        // 132: checkcast java/lang/Long
        // 135: invokevirtual java/lang/Long.longValue ()J
        // 138: lsub
        // 139: ldc2_w 10000
        // 13c: lcmp
        // 13d: ifle 1d4
        // 140: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 143: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
        // 146: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getX ()D
        // 149: invokestatic net/minecraft/util/math/MathHelper.floor (D)I
        // 14c: aload 6
        // 14e: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 153: checkcast net/minecraft/util/math/BlockPos
        // 156: invokevirtual net/minecraft/util/math/BlockPos.getX ()I
        // 159: isub
        // 15a: i2f
        // 15b: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 15e: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
        // 161: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getY ()D
        // 164: invokestatic net/minecraft/util/math/MathHelper.floor (D)I
        // 167: aload 6
        // 169: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 16e: checkcast net/minecraft/util/math/BlockPos
        // 171: invokevirtual net/minecraft/util/math/BlockPos.getY ()I
        // 174: isub
        // 175: i2f
        // 176: getstatic dev/boze/client/systems/modules/render/BreakHighlight.mc Lnet/minecraft/client/MinecraftClient;
        // 179: getfield net/minecraft/client/MinecraftClient.player Lnet/minecraft/client/network/ClientPlayerEntity;
        // 17c: invokevirtual net/minecraft/client/network/ClientPlayerEntity.getZ ()D
        // 17f: invokestatic net/minecraft/util/math/MathHelper.floor (D)I
        // 182: aload 6
        // 184: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 189: checkcast net/minecraft/util/math/BlockPos
        // 18c: invokevirtual net/minecraft/util/math/BlockPos.getZ ()I
        // 18f: isub
        // 190: i2f
        // 191: invokestatic net/minecraft/util/math/Direction.getFacing (FFF)Lnet/minecraft/util/math/Direction;
        // 194: invokevirtual net/minecraft/util/math/Direction.getOpposite ()Lnet/minecraft/util/math/Direction;
        // 197: astore 10
        // 199: new dev/boze/client/gui/notification/Notification
        // 19c: dup
        // 19d: ldc "BreakAlert"
        // 19f: aload 10
        // 1a1: invokevirtual net/minecraft/util/math/Direction.name ()Ljava/lang/String;
        // 1a4: invokevirtual java/lang/String.toLowerCase ()Ljava/lang/String;
        // 1a7: ldc_w "Block facing "
        // 1aa: swap
        // 1ab: invokedynamic makeConcatWithConstants (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String; bsm=java/lang/invoke/StringConcatFactory.makeConcatWithConstants (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/invoke/CallSite; args=[ "\u0001\u0001" ]
        // 1b0: getstatic dev/boze/client/gui/notification/Notifications.WARNING Ldev/boze/client/gui/notification/Notifications;
        // 1b3: getstatic dev/boze/client/gui/notification/NotificationPriority.Yellow Ldev/boze/client/gui/notification/NotificationPriority;
        // 1b6: invokespecial dev/boze/client/gui/notification/Notification.<init> (Ljava/lang/String;Ljava/lang/String;Ldev/boze/client/gui/notification/Notifications;Ldev/boze/client/gui/notification/NotificationPriority;)V
        // 1b9: invokestatic dev/boze/client/manager/NotificationManager.method1151 (Ldev/boze/client/gui/notification/INotification;)V
        // 1bc: aload 0
        // 1bd: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3455 Ljava/util/concurrent/ConcurrentHashMap;
        // 1c0: aload 6
        // 1c2: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 1c7: checkcast net/minecraft/util/math/BlockPos
        // 1ca: invokestatic java/lang/System.currentTimeMillis ()J
        // 1cd: invokestatic java/lang/Long.valueOf (J)Ljava/lang/Long;
        // 1d0: invokevirtual java/util/concurrent/ConcurrentHashMap.put (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        // 1d3: pop
        // 1d4: aload 6
        // 1d6: invokeinterface java/util/Map$Entry.getValue ()Ljava/lang/Object; 1
        // 1db: checkcast java/lang/Float
        // 1de: invokevirtual java/lang/Float.floatValue ()F
        // 1e1: fstore 10
        // 1e3: aload 0
        // 1e4: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3437 Ldev/boze/client/settings/EnumSetting;
        // 1e7: invokevirtual dev/boze/client/settings/EnumSetting.method461 ()Ljava/lang/Enum;
        // 1ea: checkcast dev/boze/client/enums/BreakHighlightMode
        // 1ed: invokevirtual dev/boze/client/enums/BreakHighlightMode.ordinal ()I
        // 1f0: tableswitch 301 0 2 28 99 166
        // 20c: ldc2_w 0.5
        // 20f: fload 10
        // 211: f2d
        // 212: ldc2_w 0.5
        // 215: dmul
        // 216: dsub
        // 217: dstore 11
        // 219: aload 0
        // 21a: aload 9
        // 21c: dload 11
        // 21e: dneg
        // 21f: dload 11
        // 221: dneg
        // 222: dload 11
        // 224: dneg
        // 225: invokevirtual net/minecraft/util/math/Box.expand (DDD)Lnet/minecraft/util/math/Box;
        // 228: aload 6
        // 22a: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 22f: checkcast net/minecraft/util/math/BlockPos
        // 232: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 235: aload 0
        // 236: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 239: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 23c: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 23f: ifeq 249
        // 242: aload 0
        // 243: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 246: goto 24d
        // 249: aload 1
        // 24a: getfield dev/boze/client/events/Render3DEvent.field1950 Ldev/boze/client/renderer/Renderer3D;
        // 24d: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1919 (Lnet/minecraft/util/math/Box;Ldev/boze/client/renderer/Renderer3D;)V
        // 250: goto 348
        // 253: fload 10
        // 255: f2d
        // 256: ldc2_w 0.5
        // 259: dmul
        // 25a: dstore 11
        // 25c: aload 0
        // 25d: aload 9
        // 25f: dload 11
        // 261: dneg
        // 262: dload 11
        // 264: dneg
        // 265: dload 11
        // 267: dneg
        // 268: invokevirtual net/minecraft/util/math/Box.expand (DDD)Lnet/minecraft/util/math/Box;
        // 26b: aload 6
        // 26d: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 272: checkcast net/minecraft/util/math/BlockPos
        // 275: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 278: aload 0
        // 279: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 27c: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 27f: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 282: ifeq 28c
        // 285: aload 0
        // 286: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 289: goto 290
        // 28c: aload 1
        // 28d: getfield dev/boze/client/events/Render3DEvent.field1950 Ldev/boze/client/renderer/Renderer3D;
        // 290: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1919 (Lnet/minecraft/util/math/Box;Ldev/boze/client/renderer/Renderer3D;)V
        // 293: goto 348
        // 296: ldc2_w 0.5
        // 299: fload 10
        // 29b: f2d
        // 29c: ldc2_w 0.5
        // 29f: dmul
        // 2a0: dsub
        // 2a1: dstore 11
        // 2a3: aload 0
        // 2a4: aload 9
        // 2a6: dload 11
        // 2a8: dneg
        // 2a9: dload 11
        // 2ab: dneg
        // 2ac: dload 11
        // 2ae: dneg
        // 2af: invokevirtual net/minecraft/util/math/Box.expand (DDD)Lnet/minecraft/util/math/Box;
        // 2b2: aload 6
        // 2b4: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 2b9: checkcast net/minecraft/util/math/BlockPos
        // 2bc: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 2bf: aload 0
        // 2c0: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 2c3: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 2c6: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 2c9: ifeq 2d3
        // 2cc: aload 0
        // 2cd: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 2d0: goto 2d7
        // 2d3: aload 1
        // 2d4: getfield dev/boze/client/events/Render3DEvent.field1950 Ldev/boze/client/renderer/Renderer3D;
        // 2d7: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1919 (Lnet/minecraft/util/math/Box;Ldev/boze/client/renderer/Renderer3D;)V
        // 2da: fload 10
        // 2dc: f2d
        // 2dd: ldc2_w 0.5
        // 2e0: dmul
        // 2e1: dstore 11
        // 2e3: aload 0
        // 2e4: aload 9
        // 2e6: dload 11
        // 2e8: dneg
        // 2e9: dload 11
        // 2eb: dneg
        // 2ec: dload 11
        // 2ee: dneg
        // 2ef: invokevirtual net/minecraft/util/math/Box.expand (DDD)Lnet/minecraft/util/math/Box;
        // 2f2: aload 6
        // 2f4: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 2f9: checkcast net/minecraft/util/math/BlockPos
        // 2fc: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 2ff: aload 0
        // 300: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 303: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 306: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 309: ifeq 313
        // 30c: aload 0
        // 30d: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 310: goto 317
        // 313: aload 1
        // 314: getfield dev/boze/client/events/Render3DEvent.field1950 Ldev/boze/client/renderer/Renderer3D;
        // 317: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1919 (Lnet/minecraft/util/math/Box;Ldev/boze/client/renderer/Renderer3D;)V
        // 31a: goto 348
        // 31d: aload 0
        // 31e: aload 9
        // 320: aload 6
        // 322: invokeinterface java/util/Map$Entry.getKey ()Ljava/lang/Object; 1
        // 327: checkcast net/minecraft/util/math/BlockPos
        // 32a: invokevirtual net/minecraft/util/math/Box.offset (Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/Box;
        // 32d: aload 0
        // 32e: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 331: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 334: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 337: ifeq 341
        // 33a: aload 0
        // 33b: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3452 Ldev/boze/client/renderer/Renderer3D;
        // 33e: goto 345
        // 341: aload 1
        // 342: getfield dev/boze/client/events/Render3DEvent.field1950 Ldev/boze/client/renderer/Renderer3D;
        // 345: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1919 (Lnet/minecraft/util/math/Box;Ldev/boze/client/renderer/Renderer3D;)V
        // 348: goto 042
        // 34b: aload 0
        // 34c: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3440 Ldev/boze/client/settings/BooleanSetting;
        // 34f: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 352: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 355: ifeq 3ae
        // 358: aload 0
        // 359: aload 1
        // 35a: invokedynamic run (Ldev/boze/client/systems/modules/render/BreakHighlight;Ldev/boze/client/events/Render3DEvent;)Ljava/lang/Runnable; bsm=java/lang/invoke/LambdaMetafactory.metafactory (Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite; args=[ ()V, dev/boze/client/systems/modules/render/BreakHighlight.lambda$onRender3D$4 (Ldev/boze/client/events/Render3DEvent;)V, ()V ]
        // 35f: aload 0
        // 360: invokevirtual dev/boze/client/systems/modules/render/BreakHighlight.method1918 ()Ldev/boze/client/enums/ShaderMode;
        // 363: aload 0
        // 364: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3442 Ldev/boze/client/settings/BooleanSetting;
        // 367: invokevirtual dev/boze/client/settings/BooleanSetting.method419 ()Ljava/lang/Boolean;
        // 36a: invokevirtual java/lang/Boolean.booleanValue ()Z
        // 36d: aload 0
        // 36e: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3438 Ldev/boze/client/settings/ColorSetting;
        // 371: aload 0
        // 372: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3439 Ldev/boze/client/settings/ColorSetting;
        // 375: aload 0
        // 376: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3446 Ldev/boze/client/settings/IntSetting;
        // 379: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
        // 37c: invokevirtual java/lang/Integer.intValue ()I
        // 37f: aload 0
        // 380: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3447 Ldev/boze/client/settings/FloatSetting;
        // 383: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
        // 386: invokevirtual java/lang/Float.floatValue ()F
        // 389: aload 0
        // 38a: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3444 Ldev/boze/client/settings/FloatSetting;
        // 38d: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
        // 390: invokevirtual java/lang/Float.floatValue ()F
        // 393: aload 0
        // 394: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3445 Ldev/boze/client/settings/FloatSetting;
        // 397: invokevirtual dev/boze/client/settings/FloatSetting.method423 ()Ljava/lang/Float;
        // 39a: invokevirtual java/lang/Float.floatValue ()F
        // 39d: aload 0
        // 39e: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3443 Ldev/boze/client/settings/IntSetting;
        // 3a1: invokevirtual dev/boze/client/settings/IntSetting.method434 ()Ljava/lang/Integer;
        // 3a4: invokevirtual java/lang/Integer.intValue ()I
        // 3a7: aload 0
        // 3a8: getfield dev/boze/client/systems/modules/render/BreakHighlight.field3453 Ldev/boze/client/utils/render/ByteTexture;
        // 3ab: invokestatic dev/boze/client/shaders/ChamsShaderRenderer.method1310 (Ljava/lang/Runnable;Ldev/boze/client/enums/ShaderMode;ZLdev/boze/client/settings/ColorSetting;Ldev/boze/client/settings/ColorSetting;IFFFILdev/boze/client/utils/render/ByteTexture;)V
        // 3ae: return
    }

    private void method1917(Vector3d var1, double var2, int var4) {
        Class5922.method61(var1);
        String var8 = var2 >= 0.0 ? String.valueOf((int) MathHelper.clamp(var2 * 100.0, 0.0, 100.0)) : null;
        String var9 = null;
        if (var4 >= 0 && var4 != mc.player.getId() && mc.world.getEntityById(var4) != null) {
            var9 = mc.world.getEntityById(var4).getDisplayName().getString();
        }

        IFontRender.method499().startBuilding(1.5);
        double var10 = 0.0;
        if (var8 != null) {
            var10 -= IFontRender.method499().method1390() / 2.0;
        }

        if (var9 != null) {
            var10 -= IFontRender.method499().method1390() / 2.0;
        }

        if (var8 != null && var9 != null) {
            var10 -= IFontRender.method499().method1390() / 8.0;
        }

        if (var8 != null) {
            IFontRender.method499().drawShadowedText(var8, -IFontRender.method499().method501(var8) / 2.0, var10, RGBAColor.field402);
            var10 += IFontRender.method499().method1390() * 1.25;
        }

        if (var9 != null) {
            IFontRender.method499().drawShadowedText(var9, -IFontRender.method499().method501(var9) / 2.0, var10, RGBAColor.field402);
        }

        IFontRender.method499().endBuilding();
        Class5922.method2142();
    }

    private ShaderMode method1918() {
        if (this.field3441.getValue() == BreakHighlightShader.Image) {
            if (!this.field3448.getValue().isEmpty() && (!this.field3448.getValue().equals(this.field3454) || this.field3453 == null)) {
                File var4 = new File(ConfigManager.images, this.field3448.getValue() + ".png");

                try {
                    FileInputStream var5 = new FileInputStream(var4);
                    this.field3453 = ByteTexturePacker.method493(var5);
                    if (this.field3453 != null) {
                        this.field3454 = this.field3448.getValue();
                    } else {
                        this.field3454 = "";
                    }
                } catch (Exception var6) {
                    NotificationManager.method1151(new Notification(this.getName(), " Couldn't load image", Notifications.WARNING, NotificationPriority.Yellow));
                    this.field3448.setValue("");
                    this.field3454 = "";
                }
            }

            if (this.field3453 != null) {
                return ShaderMode.Image;
            }
        }

        return ShaderMode.Rainbow;
    }

    private void method1919(Box var1, Renderer3D var2) {
        if (this.field3440.getValue()) {
            var2.method1261(var1.minX, var1.minY, var1.minZ, var1.maxX, var1.maxY, var1.maxZ, RGBAColor.field402, 0);
        } else {
            var2.method1273(var1, this.field3438.getValue(), this.field3439.getValue(), ShapeMode.Full, 0);
        }
    }

    private void lambda$onRender3D$4(Render3DEvent var1) {
        this.field3452.method1219(var1.matrix);
    }

    private void lambda$onSendMovementPackets$3(BlockPos var1, Long var2) {
        if (System.currentTimeMillis() - var2 > 10000L || mc.world.getBlockState(var1).isAir()) {
            this.field3455.remove(var1);
        }
    }

    private boolean lambda$new$2() {
        return this.field3449.getValue() || this.field3450.getValue();
    }

    private boolean lambda$new$1() {
        return this.field3441.getValue() == BreakHighlightShader.Image;
    }

    private boolean lambda$new$0() {
        return this.field3444.getValue() > 0.0F;
    }
}
