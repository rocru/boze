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
    private static final FloatBuffer field2153;
    private static final CapabilityTrackerAccessor field2154;
    private static final CapabilityTrackerAccessor field2155;
    private static final CapabilityTrackerAccessor field2156;
    private static final CapabilityTrackerAccessor field2157;
    private static boolean field2158;
    private static boolean field2159;
    private static boolean field2160;
    private static boolean field2161;
    public static int field2162;
    private static int field2163;

    public GL() {
        super();
    }

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

    public static void method1164(final int shader) {
        GlStateManager.glDeleteShader(shader);
    }

    public static void method1165(final int id) {
        GlStateManager._deleteTexture(id);
    }

    public static void method1166(final int fbo) {
        GlStateManager._glDeleteFramebuffers(fbo);
    }

    public static void method1167(final int program) {
        GlStateManager.glDeleteProgram(program);
    }

    public static void method1168(final int vao) {
        GlStateManager._glBindVertexArray(vao);
        BufferRendererAccessor.setCurrentVertexBuffer(null);
    }

    public static void method1169(final int vbo) {
        GlStateManager._glBindBuffer(34962, vbo);
    }

    public static void method1170(final int ibo) {
        if (ibo != 0) {
            GL.field2163 = GL.field2162;
        }
        GlStateManager._glBindBuffer(34963, (ibo != 0) ? ibo : GL.field2163);
    }

    public static void method1171(final int fbo) {
        GlStateManager._glBindFramebuffer(36160, fbo);
    }

    public static void method1172(final int target, final ByteBuffer data, final int usage) {
        GlStateManager._glBufferData(target, data, usage);
    }

    public static void method1173(final int mode, final int first, final int type) {
        GlStateManager._drawElements(mode, first, type, 0L);
    }

    public static void method1174(final int i) {
        GlStateManager._enableVertexAttribArray(i);
    }

    public static void method1175(final int index, final int size, final int type, final boolean normalized, final int stride, final long pointer) {
        GlStateManager._vertexAttribPointer(index, size, type, normalized, stride, pointer);
    }

    public static int method1176(final int type) {
        return GlStateManager.glCreateShader(type);
    }

    public static void method1177(final int shader, final String source) {
        GlStateManager.glShaderSource(shader, ImmutableList.of(source));
    }

    public static String method1178(final int shader) {
        GlStateManager.glCompileShader(shader);
        if (GlStateManager.glGetShaderi(shader, 35713) == 0) {
            return GlStateManager.glGetShaderInfoLog(shader, 512);
        }
        return null;
    }

    public static int method1179() {
        return GlStateManager.glCreateProgram();
    }

    public static String method1180(final int program, final int vertShader, final int fragShader) {
        GlStateManager.glAttachShader(program, vertShader);
        GlStateManager.glAttachShader(program, fragShader);
        GlStateManager.glLinkProgram(program);
        if (GlStateManager.glGetProgrami(program, 35714) == 0) {
            return GlStateManager.glGetProgramInfoLog(program, 512);
        }
        return null;
    }

    public static void method1181(final int program) {
        GlStateManager._glUseProgram(program);
    }

    public static int method1182(final int program, final String name) {
        return GlStateManager._glGetUniformLocation(program, name);
    }

    public static void method1183(final int location, final int v) {
        GlStateManager._glUniform1i(location, v);
    }

    public static void method1184(final int location, final float v) {
        GL32C.glUniform1f(location, v);
    }

    public static void method1185(final int location, final float v1, final float v2) {
        GL32C.glUniform2f(location, v1, v2);
    }

    public static void method1186(final int location, final float v1, final float v2, final float v3) {
        GL32C.glUniform3f(location, v1, v2, v3);
    }

    public static void method1187(final int location, final float v1, final float v2, final float v3, final float v4) {
        GL32C.glUniform4f(location, v1, v2, v3, v4);
    }

    public static void method1188(final int location, final float[] v) {
        GL32C.glUniform3fv(location, v);
    }

    public static void method1189(final int location, final Matrix4f v) {
        v.get(GL.field2153);
        GlStateManager._glUniformMatrix4(location, false, GL.field2153);
    }

    public static void method1190(final int name, final int param) {
        GlStateManager._pixelStore(name, param);
    }

    public static void method1191(final int target, final int name, final int param) {
        GlStateManager._texParameter(target, name, param);
    }

    public static void method1192(final int target, final int level, final int internalFormat, final int width, final int height, final int border, final int format, final int type, final ByteBuffer pixels) {
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

    public static void method1194(final int target) {
        GL32C.glGenerateMipmap(target);
    }

    public static void method1195(final int target, final int attachment, final int textureTarget, final int texture, final int level) {
        GlStateManager._glFramebufferTexture2D(target, attachment, textureTarget, texture, level);
    }

    public static void method1196(final int mask) {
        GlStateManager._clearColor(0.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager._clear(mask, false);
    }

    public static void method1197() {
        GL.field2158 = GL.field2154.getState();
        GL.field2159 = GL.field2155.getState();
        GL.field2160 = GL.field2156.getState();
        GL.field2161 = GL.field2157.getState();
    }

    public static void method1198() {
        GL.field2154.iSetState(GL.field2158);
        GL.field2155.iSetState(GL.field2159);
        GL.field2156.iSetState(GL.field2160);
        GL.field2157.iSetState(GL.field2161);
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
        GL32C.glLineWidth(1.0f);
    }

    public static void method1208() {
        GL32C.glDisable(2848);
        GL32C.glLineWidth(1.0f);
    }

    public static void method1209(final Identifier id) {
        GlStateManager._activeTexture(33984);
        GL.mc.getTextureManager().bindTexture(id);
    }

    public static void method1210(final int i, final int slot) {
        GlStateManager._activeTexture(33984 + slot);
        GlStateManager._bindTexture(i);
    }

    public static void method1211(final int i) {
        method1210(i, 0);
    }

    private static CapabilityTrackerAccessor method1212(final String name) {
        try {
            final Field declaredField = GlStateManager.class.getDeclaredField(name);
            declaredField.setAccessible(true);
            final Object value = declaredField.get(null);
            final String mapClassName = FabricLoader.getInstance().getMappingResolver().mapClassName("intermediary", "com.mojang.blaze3d.platform.GlStateManager$class_1018");
            Field field = null;
            final Field[] declaredFields = value.getClass().getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field2 = declaredFields[i];
                if (field2.getType().getName().equals(mapClassName)) {
                    field = field2;
                    break;
                }
            }
            field.setAccessible(true);
            return (CapabilityTrackerAccessor) field.get(value);
        } catch (final NoSuchFieldException | IllegalAccessException e) {
            ErrorLogger.log(e);
            return null;
        }
    }

    public static Color method1213(final int x, final int y) {
        final ByteBuffer byteBuffer = BufferUtils.createByteBuffer(3);
        RenderSystem.readPixels((int) (x * 1.0), (int) (GL.mc.getWindow().getScaledHeight() - y * 1.0), 1, 1, 6407, 5120, byteBuffer);
        return new Color(Math.min(255, byteBuffer.get(0) % 256 * 2), Math.min(255, byteBuffer.get(1) % 256 * 2), Math.min(255, byteBuffer.get(2) % 256 * 2));
    }

    public static void method1214(final int x, final int y, final int width, final int height) {
        GlStateManager._viewport(x, y, width, height);
    }

    static {
        field2153 = BufferUtils.createFloatBuffer(16);
        field2154 = method1212("DEPTH");
        field2155 = method1212("BLEND");
        field2156 = method1212("CULL");
        field2157 = method1212("SCISSOR");
    }
}
