package netutil;

import org.objectweb.asm.tree.ClassNode;

import java.security.CodeSource;
import java.util.HashMap;

public class Util {
    public HashMap<String, byte[]> method3(final String v1, final String v2, final ClassLoader classLoader) throws Throwable {
        final HashMap<String, byte[]> method11 = new BozeLoader().method11(v1, v2);
        new BozeMixins(classLoader);
        return method11;
    }

    public static ClassNode method4(final String var1, final byte[] var2, final int var3) throws Throwable {
        return BozeMixins.method37(var1, var2, var3);
    }

    public static Class<?> method5(final String s, final byte[] array, final CodeSource codeSource) throws Throwable {
        return BozeMixins.method38(s, array, codeSource);
    }
}
