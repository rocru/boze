package netutil;

import net.fabricmc.loader.impl.launch.knot.MixinServiceKnot;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.transformers.MixinClassReader;
import org.spongepowered.asm.transformers.MixinClassWriter;

import javax.crypto.Cipher;
import java.lang.reflect.Field;
import java.security.CodeSource;
import java.security.SecureClassLoader;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BozeMixins extends SecureClassLoader {
    private static BozeMixins field4;
    private static Function<byte[], byte[]> field5;
    private static BiConsumer<Cipher, byte[]> field6;
    public static boolean field7;

    BozeMixins(final ClassLoader parent) {
        super(parent);
        if (BozeMixins.field4 == null) {
            BozeMixins.field4 = this;
        }
    }

    static final void method33(final Function<byte[], byte[]> field5) {
        if (field5 == null) {
            return;
        }
        if (BozeMixins.field5 == null) {
            BozeMixins.field5 = field5;
        }
    }

    static final void method34(final BiConsumer<Cipher, byte[]> field6) {
        if (field6 == null) {
            return;
        }
        if (BozeMixins.field6 == null) {
            BozeMixins.field6 = field6;
        }
    }

    private static final byte[] method35(final byte[] array) {
        return BozeMixins.field5.apply(array);
    }

    private static final byte[] method36(final byte[] array) {
        try {
            final Cipher instance = Cipher.getInstance("AES/GCM/NoPadding");
            BozeMixins.field6.accept(instance, array);
            final byte[] input = new byte[array.length - 12];
            System.arraycopy(array, 12, input, 0, array.length - 12);
            return instance.doFinal(input);
        } catch (final Exception ex) {
            return array;
        }
    }

    static final ClassNode method37(final String var1, byte[] var2, final int var3) {
        final ClassNode var4 = new ClassNode();
        if (var1.replace('.', '/').startsWith("dev/boze/client")) {
            var2 = method35(var2);
            new ClassReader(method36(var2)).accept(var4, var3);
        } else {
            new ClassReader(var2).accept(var4, var3);
        }
        return var4;
    }

    static final Class<?> method38(final String s, byte[] b, final CodeSource cs) {
        if (s.startsWith("dev.boze.client")) {
            try {
                final Class<?> loadedClass = BozeMixins.field4.findLoadedClass(s);
                if (loadedClass != null) {
                    return loadedClass;
                }
                b = method35(b);
                b = method36(b);
                if (s.startsWith("dev.boze.client.mixin.")) {
                    final MixinClassReader mixinClassReader = new MixinClassReader(b, s);
                    final ClassNode classVisitor = new ClassNode();
                    mixinClassReader.accept(classVisitor, 8);

                    IMixinTransformer transformer = null;

                    try {
                        Field field = MixinServiceKnot.class.getDeclaredField("transformer");
                        field.setAccessible(true);
                        transformer = (IMixinTransformer) field.get(null);
                    } catch (Throwable _t) {
                        System.err.println("not good");
                        _t.printStackTrace(System.err);
                    }

                    if (Objects.requireNonNull(transformer).transformClass(MixinEnvironment.getCurrentEnvironment(), s, classVisitor)) {
                        final MixinClassWriter classVisitor2 = new MixinClassWriter(mixinClassReader, 3);
                        classVisitor.accept(classVisitor2);
                        b = classVisitor2.toByteArray();
                    }
                }
                final Class<?> defineClass = BozeMixins.field4.defineClass(s, b, 0, b.length, cs);
                if (defineClass != null) {
                    return defineClass;
                }
            } catch (final Exception ex) {
            }
        }
        return null;
    }

    static {
        BozeMixins.field5 = null;
        BozeMixins.field6 = null;
    }
}
