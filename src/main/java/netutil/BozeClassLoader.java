package netutil;

import net.fabricmc.loader.impl.launch.knot.MixinServiceKnot;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.transformers.MixinClassReader;
import org.spongepowered.asm.transformers.MixinClassWriter;

import javax.crypto.Cipher;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BozeClassLoader extends SecureClassLoader {
    private static BozeClassLoader field4007;
    private static Function<byte[], byte[]> field4008 = null;
    private static BiConsumer<Cipher, byte[]> field4009 = null;
    public static boolean field4010;

    public BozeClassLoader(ClassLoader var1) {
        super(var1);
        boolean var4 = Count.field4012;
        BozeClassLoader var5 = field4007;
        if (!var4) {
            if (field4007 != null) {
                return;
            }

            var5 = this;
        }

        field4007 = var5;
    }

    static final void method2305(Function<byte[], byte[]> var0) {
        boolean var3 = Count.field4012;
        Function<byte[], byte[]> var10000 = var0;
        if (!var3) {
            if (var0 == null) {
                return;
            }

            var10000 = field4008;
        }

        if (!var3) {
            if (var10000 != null) {
                return;
            }

            var10000 = var0;
        }

        field4008 = var10000;
    }

    static final void method2306(BiConsumer<Cipher, byte[]> var0) {
        boolean var3 = Count.field4012;
        BiConsumer var10000 = var0;
        if (!var3) {
            if (var0 == null) {
                return;
            }

            var10000 = field4009;
        }

        if (!var3) {
            if (var10000 != null) {
                return;
            }

            var10000 = var0;
        }

        field4009 = var10000;
    }

    private static final byte[] method2307(byte[] var0) {
        return field4008.apply(var0);
    }

    private static final byte[] method2308(byte[] var0) {
        try {
            Cipher var3 = Cipher.getInstance("AES/GCM/NoPadding");
            field4009.accept(var3, var0);
            byte[] var4 = new byte[var0.length - 12];
            System.arraycopy(var0, 12, var4, 0, var0.length - 12);
            return var3.doFinal(var4);
        } catch (Exception var6) {
            return var0;
        }
    }

    public static final ClassNode method2309(String var0, byte[] var1, int var2) {
        boolean var5;
        ClassNode var6;
        label26:
        {
            label25:
            {
                boolean var10000 = Count.field4012;
                var6 = new ClassNode();
                var5 = var10000;
                if (!var5) {
                    if (!var0.replace('.', '/').startsWith("dev/boze/client")) {
                        break label25;
                    }

                    var1 = method2307(var1);
                    new ClassReader(method2308(var1)).accept(var6, var2);
                }

                if (!var5) {
                    break label26;
                }
            }

            new ClassReader(var1).accept(var6, var2);
        }

        if (field4010) {
            Count.field4012 = !var5;
        }

        return var6;
    }

    public static final Class<?> method2310(String var0, byte[] var1, CodeSource var2) {
        boolean var5 = Count.field4012;
        if (var0.startsWith("dev.boze.client")) {
            try {
                Class var6 = field4007.findLoadedClass(var0);
                if (!var5) {
                    if (var6 != null) {
                        return var6;
                    }

                    var1 = method2307(var1);
                    var1 = method2308(var1);
                }

                label41:
                if (!var5) {
                    if (var0.startsWith("dev.boze.client.mixin.")) {
                        MixinClassReader var7 = new MixinClassReader(var1, var0);
                        ClassNode var8 = new ClassNode();
                        var7.accept(var8, 8);
                        if (var5) {
                            break label41;
                        }

                        if (MixinServiceKnot.getTransformer().transformClass(MixinEnvironment.getCurrentEnvironment(), var0, var8)) {
                            MixinClassWriter var9 = new MixinClassWriter(var7, 3);
                            var8.accept(var9);
                            var1 = var9.toByteArray();
                        }
                    }

                    var6 = field4007.defineClass(var0, var1, 0, var1.length, var2);
                }

                Class var10000 = var6;
                if (!var5) {
                    if (var6 == null) {
                        return null;
                    }

                    var10000 = var6;
                }

                return var10000;
            } catch (Exception var10) {
            }
        }

        return null;
    }
}
