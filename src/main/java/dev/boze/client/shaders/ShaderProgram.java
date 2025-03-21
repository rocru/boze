package dev.boze.client.shaders;

import com.google.common.io.Files;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.Boze;
import dev.boze.client.core.ErrorLogger;
import dev.boze.client.renderer.GL;
import dev.boze.client.utils.IMinecraft;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gl.GlProgramManager;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.joml.Matrix4f;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

public class ShaderProgram implements IMinecraft {
    public static ShaderProgram field1552;
    private final int field1553;
    private final Object2IntMap<String> field1554 = new Object2IntOpenHashMap();
    private static String field1555 = null;

    public ShaderProgram() {
        this(Boze.getNextKey(), Boze.getNextKey());
    }

    public ShaderProgram(String vertPath, String fragPath) {
        this(vertPath, fragPath, false);
    }

    public ShaderProgram(String vertPath, String fragSrc, boolean src) {
        if (field1555 == null) {
            try {
                Socket var7 = new Socket("auth.boze.dev", 3000);
                DataInputStream var8 = new DataInputStream(var7.getInputStream());
                DataOutputStream var9 = new DataOutputStream(var7.getOutputStream());
                var9.writeUTF(Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest("specialShader".getBytes(StandardCharsets.UTF_8))));
                field1555 = var8.readUTF();
                var7.close();
            } catch (NoSuchAlgorithmException | IOException var12) {
            }
        }

        int var13 = GL.method1176(35633);
        GL.method1177(var13, method1341(vertPath));
        String var14 = GL.method1178(var13);
        if (var14 != null) {
            throw new RuntimeException("Unable to continue launching client due to vertex shader compilation error! " + var14);
        } else {
            int var15 = GL.method1176(35632);
            GL.method1177(var15, src ? fragSrc : method1341(fragSrc));
            String var10 = GL.method1178(var15);
            if (var10 != null) {
                throw new RuntimeException("Unable to continue launching client due to fragment shader compilation error! " + var10);
            } else {
                this.field1553 = GL.method1179();
                String var11 = GL.method1180(this.field1553, var13, var15);
                if (var11 != null) {
                    throw new RuntimeException("Unable to continue launching client due to program linkage error! " + var11);
                } else {
                    GL.method1164(var13);
                    GL.method1164(var15);
                }
            }
        }
    }

    private static String method1341(String var0) {
        try {
            String var4 = IOUtils.toString(
                    new ByteArrayInputStream(
                            method688(
                                    Objects.requireNonNull(ShaderProgram.class.getClassLoader().getResourceAsStream("assets/boze/shaders/" + var0)).readAllBytes(),
//                                    mc.getResourceManager().getResource(Identifier.of("boze", "shaders/" + var0)).get().getInputStream().readAllBytes(),
                                    Files.getFileExtension(var0)
                            )
                    ),
                    StandardCharsets.UTF_8
            );
            if (field1555 != null) {
                if (!field1555.isEmpty()) {
                    var4 = var4.replace("u_Projection * u_ModelView * ", field1555);
                }
            } else {
                var4 = var4.replace("u_Projection * u_ModelView * ", "");
            }

            return var4;
        } catch (IOException var5) {
            ErrorLogger.log(var5);
            return "";
        }
    }

    private static byte[] method688(byte[] var0, String var1) {
        byte[] var5 = new byte[var0.length];

        for (int var6 = 0; var6 < var0.length; var6++) {
            var5[var6] = (byte) (var0[var6] ^ var1.charAt(var6 % var1.length()));
        }

        return var5;
    }

    public void method2142() {
        GL.method1181(this.field1553);
        field1552 = this;
    }

    public void method1416() {
        GlProgramManager.useProgram(0);
        field1552 = null;
    }

    private int method464(String var1) {
        if (this.field1554.containsKey(var1)) {
            return this.field1554.getInt(var1);
        } else {
            int var5 = GL.method1182(this.field1553, var1);
            this.field1554.put(var1, var5);
            return var5;
        }
    }

    public void method689(String name, boolean v) {
        GL.method1183(this.method464(name), v ? 1 : 0);
    }

    public void method690(String name, int v) {
        GL.method1183(this.method464(name), v);
    }

    public void method691(String name, double v) {
        GL.method1184(this.method464(name), (float) v);
    }

    public void method581(String name, double v1, double v2) {
        GL.method1185(this.method464(name), (float) v1, (float) v2);
    }

    public void method692(String name, double v1, double v2, double v3) {
        GL.method1186(this.method464(name), (float) v1, (float) v2, (float) v3);
    }

    public void method693(String name, double v1, double v2, double v3, double v4) {
        GL.method1187(this.method464(name), (float) v1, (float) v2, (float) v3, (float) v4);
    }

    public void method694(String name, Matrix4f mat) {
        GL.method1189(this.method464(name), mat);
    }

    public void method1198() {
        this.method694("u_Projection", RenderSystem.getProjectionMatrix());
        this.method694("u_ModelView", RenderSystem.getModelViewStack());
    }
}
