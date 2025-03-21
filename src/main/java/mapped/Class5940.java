package mapped;

import netutil.BozeClassLoader;
import org.objectweb.asm.tree.ClassNode;

import java.security.CodeSource;
import java.util.HashMap;

public class Class5940 {
    public HashMap<String, byte[]> method11521(String var1, String var2, ClassLoader var3) {
        HashMap var6 = new Class5941().method11524(var1, var2);
        new BozeClassLoader(var3);
        return var6;
    }

    public static ClassNode method11522(String var0, byte[] var1, int var2) {
        return BozeClassLoader.method2309(var0, var1, var2);
    }

    public static Class<?> method11523(String var0, byte[] var1, CodeSource var2) {
        return BozeClassLoader.method2310(var0, var1, var2);
    }
}
