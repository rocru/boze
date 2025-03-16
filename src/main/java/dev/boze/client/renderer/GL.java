package dev.boze.client.renderer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.mixin.BufferRendererAccessor;
import dev.boze.client.mixin.CapabilityTrackerAccessor;
import dev.boze.client.utils.IMinecraft;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL32C;

import java.awt.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class GL implements IMinecraft {
    private static final FloatBuffer field2153 = BufferUtils.createFloatBuffer(16);
    private static final CapabilityTrackerAccessor field2154 = method1212("DEPTH");
    private static final CapabilityTrackerAccessor field2155 = method1212("BLEND");
    private static final CapabilityTrackerAccessor field2156 = method1212("CULL");
    private static final CapabilityTrackerAccessor field2157 = method1212("SCISSOR");
    private static boolean field2158;
    private static boolean field2159;
    private static boolean field2160;
    private static boolean field2161;
    public static int field2162;
    private static int field2163;

    public static int method1160() {
        return GlStateManager._glGenVertexArrays();
    }

    public static int method1161() {
        return GlStateManager._glGenBuffers();
    }

    public static int method1162() {
        return GlStateManager._genTexture();
    }

    public static int method1163() {
        return GlStateManager.glGenFramebuffers();
    }

    public static void method1164(int shader) {
        GlStateManager.glDeleteShader(shader);
    }

    public static void method1165(int id) {
        GlStateManager._deleteTexture(id);
    }

    public static void method1166(int fbo) {
        GlStateManager._glDeleteFramebuffers(fbo);
    }

    public static void method1167(int program) {
        GlStateManager.glDeleteProgram(program);
    }

    public static void method1168(int vao) {
        GlStateManager._glBindVertexArray(vao);
        BufferRendererAccessor.setCurrentVertexBuffer(null);
    }

    public static void method1169(int vbo) {
        GlStateManager._glBindBuffer(34962, vbo);
    }

    public static void method1170(int ibo) {
        if (ibo != 0) {
            field2163 = field2162;
        }

        GlStateManager._glBindBuffer(34963, ibo != 0 ? ibo : field2163);
    }

    public static void method1171(int fbo) {
        GlStateManager._glBindFramebuffer(36160, fbo);
    }

    public static void method1172(int target, ByteBuffer data, int usage) {
        GlStateManager._glBufferData(target, data, usage);
    }

    public static void method1173(int mode, int first, int type) {
        GlStateManager._drawElements(mode, first, type, 0L);
    }

    public static void method1174(int i) {
        GlStateManager._enableVertexAttribArray(i);
    }

    public static void method1175(int index, int size, int type, boolean normalized, int stride, long pointer) {
        GlStateManager._vertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    public static int method1176(int type) {
        return GlStateManager.glCreateShader(type);
    }

    public static void method1177(int shader, String source) {
        GlStateManager.glShaderSource(shader, ImmutableList.of(source));
    }

    public static String method1178(int shader) {
        GlStateManager.glCompileShader(shader);
        return GlStateManager.glGetShaderi(shader, 35713) == 0 ? GlStateManager.glGetShaderInfoLog(shader, 512) : null;
    }

    public static int method1179() {
        return GlStateManager.glCreateProgram();
    }

    public static String method1180(int program, int vertShader, int fragShader) {
        GlStateManager.glAttachShader(program, vertShader);
        GlStateManager.glAttachShader(program, fragShader);
        GlStateManager.glLinkProgram(program);
        return GlStateManager.glGetProgrami(program, 35714) == 0 ? GlStateManager.glGetProgramInfoLog(program, 512) : null;
    }

    public static void method1181(int program) {
        GlStateManager._glUseProgram(program);
    }

    public static int method1182(int program, String name) {
        return GlStateManager._glGetUniformLocation(program, name);
    }

    public static void method1183(int location, int v) {
        GlStateManager._glUniform1i(location, v);
    }

    public static void method1184(int location, float v) {
        GL32C.glUniform1f(location, v);
    }

    public static void method1185(int location, float v1, float v2) {
        GL32C.glUniform2f(location, v1, v2);
    }

    public static void method1186(int location, float v1, float v2, float v3) {
        GL32C.glUniform3f(location, v1, v2, v3);
    }

    public static void method1187(int location, float v1, float v2, float v3, float v4) {
        GL32C.glUniform4f(location, v1, v2, v3, v4);
    }

    public static void method1188(int location, float[] v) {
        GL32C.glUniform3fv(location, v);
    }

    public static void method1189(int location, Matrix4f v) {
        v.get(field2153);
        GlStateManager._glUniformMatrix4(location, false, field2153);
    }

    public static void method1190(int name, int param) {
        GlStateManager._pixelStore(name, param);
    }

    public static void method1191(int target, int name, int param) {
        GlStateManager._texParameter(target, name, param);
    }

    public static void method1192(int target, int level, int internalFormat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
        GL32C.glTexImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
    }

    public static void method1193() {
        method1190(3312, 0);
        method1190(3313, 0);
        method1190(3314, 0);
        method1190(32878, 0);
        method1190(3315, 0);
        method1190(3316, 0);
        method1190(32877, 0);
        method1190(3317, 4);
    }

    public static void method1194(int target) {
        GL32C.glGenerateMipmap(target);
    }

    public static void method1195(int target, int attachment, int textureTarget, int texture, int level) {
        GlStateManager._glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
    }

    public static void method1196(int mask) {
        GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 1.0F);
        GlStateManager._clear(mask, false);
    }

    public static void method1197() {
        field2158 = field2154.getState();
        field2159 = field2155.getState();
        field2160 = field2156.getState();
        field2161 = field2157.getState();
    }

    public static void method1198() {
        field2154.iSetState(field2158);
        field2155.iSetState(field2159);
        field2156.iSetState(field2160);
        field2157.iSetState(field2161);
        method1208();
    }

    public static void method1199() {
        GlStateManager._enableDepthTest();
    }

    public static void method1200() {
        GlStateManager._disableDepthTest();
    }

    public static void method1201() {
        GlStateManager._enableBlend();
        GlStateManager._blendFunc(770, 771);
    }

    public static void method1202() {
        GlStateManager._disableBlend();
    }

    public static void method1203() {
        GlStateManager._enableCull();
        GL32C.glCullFace(1029);
    }

    public static void method1204() {
        GlStateManager._disableCull();
    }

    public static void method1205() {
        GlStateManager._enableScissorTest();
    }

    public static void method1206() {
        GlStateManager._disableScissorTest();
    }

    public static void method1207() {
        GL32C.glEnable(2848);
        GL32C.glLineWidth(1.0F);
    }

    public static void method1208() {
        GL32C.glDisable(2848);
        GL32C.glLineWidth(1.0F);
    }

    public static void method1209(Identifier id) {
        GlStateManager._activeTexture(33984);
        mc.getTextureManager().bindTexture(id);
    }

    public static void method1210(int i, int slot) {
        GlStateManager._activeTexture(33984 + slot);
        GlStateManager._bindTexture(i);
    }

    public static void method1211(int i) {
        method1210(i, 0);
    }

    private static CapabilityTrackerAccessor method1212(String var0) {
        try {
            Class<GlStateManager> var3 = GlStateManager.class;
            Field var4 = var3.getDeclaredField(var0);
            var4.setAccessible(true);
            Object var5 = var4.get(null);
            String var6 = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "com.mojang.blaze3d.platform.GlStateManager$class_1018");
            Field var7 = null;

            for (Field var11 : var5.getClass().getDeclaredFields()) {
                if (var11.getType().getName().equals(var6)) {
                    var7 = var11;
                    break;
                }
            }

            var7.setAccessible(true);
            return (CapabilityTrackerAccessor) var7.get(var5);
        } catch (IllegalAccessException | NoSuchFieldException var12) {
            ErrorLogger.log(var12);
            return null;
        }
    }

    public static Color method1213(int x, int y) {
        ByteBuffer var7 = BufferUtils.createByteBuffer(3);
        RenderSystem.readPixels((int) ((double) x), (int) ((double) mc.getWindow().getScaledHeight() - (double) y), 1, 1, 6407, 5120, var7);
        return new Color(Math.min(255, var7.get(0) % 256 * 2), Math.min(255, var7.get(1) % 256 * 2), Math.min(255, var7.get(2) % 256 * 2));
    }

    public static void method1214(int x, int y, int width, int height) {
        GlStateManager._viewport(x, y, width, height);
    }
}
