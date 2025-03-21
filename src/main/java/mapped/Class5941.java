package mapped;

import java.nio.file.*;
import net.fabricmc.loader.impl.gui.*;
import java.net.*;
import java.lang.management.*;
import sun.misc.*;
import java.nio.charset.*;
import org.spongepowered.asm.launch.*;
import netutil.*;
import javax.crypto.*;
import net.fabricmc.loader.impl.util.log.*;
import java.lang.reflect.*;
import com.sun.management.*;
import java.util.function.*;
import java.util.stream.*;
import java.util.*;
import java.io.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;

class Class5941
{
    Class5941() {
        super();
    }

    HashMap<String, byte[]> method11524(final String v1, final String v2) {
        final boolean field4012 = Count.field4012;
        try {
            String s = "";
            File file = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
            final String lowerCase = System.getProperty("os.name").toLowerCase();
            final boolean contains = lowerCase.contains("nix");
            if (!field4012) {
                if (!contains) {
                    final boolean contains2 = lowerCase.contains("nux");
                    if (!field4012) {
                        if (!contains2) {
                            final boolean contains3 = lowerCase.contains("aix");
                            if (!field4012) {
                                if (contains3) {}
                            }
                        }
                    }
                }
            }
            final boolean b = contains;
            if (!field4012) {
                if (b) {
                    file = new File(v2, "cache");
                }
                file.mkdirs();
            }
            final File file2 = new File(file, "at");
            try {
                final File file3 = file2;
                if (field4012 || file3.exists()) {
                    final String string = Files.readString(file3.toPath());
                    if (field4012 || string.length() == 92) {
                        s = string;
                    }
                }
            }
            catch (final Exception ex) {}
            final String s2 = s;
            if (field4012 || s2.isEmpty()) {
                FabricGuiEntry.displayExitMessage(s2, "Authentication Error", "Unable to login, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            }
            this.method11525(file, s);
            final Socket socket = new Socket("auth.boze.dev", 3000);
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(method11531("loaderInitiateBeta"));
            final String[] array = { "-javaagent", "-Xdebug", "-agentlib", "-Xrunjdwp", "-Xnoagent", "-DproxySet", "-DproxyHost", "-DproxyPort", "-Djavax.net.ssl.trustStore", "-Djavax.net.ssl.trustStorePassword" };
            final Iterator<String> iterator = ManagementFactory.getRuntimeMXBean().getInputArguments().iterator();
            Label_0465:
            while (true) {
                do {
                    iterator.hasNext();
                    boolean contains4 = false;
                    Label_0348:
                    while (contains4) {
                        final String v3 = iterator.next();
                        if (!field4012) {
                            final String s3 = v3;
                            final String[] array2 = array;
                            final int length = array2.length;
                            int i = 0;
                            while (i < length) {
                                final String s4 = array2[i];
                                if (!field4012) {
                                    contains4 = s3.toLowerCase(Locale.ROOT).contains(s4.toLowerCase(Locale.ROOT));
                                    if (field4012) {
                                        continue Label_0348;
                                    }
                                    if (contains4) {
                                        final Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
                                        declaredField.setAccessible(true);
                                        ((Unsafe)declaredField.get(null)).putAddress(0L, 0L);
                                    }
                                    ++i;
                                }
                                if (field4012) {
                                    break;
                                }
                            }
                            continue Label_0465;
                        }
                        final String method11534 = method11534(dataInputStream.readUTF(), v3);
                        final byte[] method11535 = method11535(method11534.getBytes(StandardCharsets.UTF_8), s);
                        final byte[] method11536 = method11536(this.method11526(), method11535);
                        final byte[] method11537 = method11535(s.getBytes(StandardCharsets.UTF_8), method11534);
                        Object digest = null;
                        final Field declaredField2 = Unsafe.class.getDeclaredField("theUnsafe");
                        declaredField2.setAccessible(true);
                        final Unsafe unsafe = (Unsafe)declaredField2.get(null);
                        final String[] split = System.getProperty("java.class.path").split("[;:]");
                        final int length2 = split.length;
                        int j = 0;
                        Label_0930: {
                            boolean contains5 = false;
                            while (j < length2) {
                                final String pathname = split[j];
                                if (!field4012) {
                                    contains5 = pathname.contains("boze-loader-beta-0.");
                                    if (field4012) {
                                        break Label_0930;
                                    }
                                    if (contains5) {
                                        final boolean contains6 = System.getProperty("os.name").toLowerCase().contains("win");
                                        File absoluteFile = null;
                                        boolean absolute = false;
                                        Label_0822: {
                                            Label_0817: {
                                                if (!field4012 && contains6) {
                                                    absoluteFile = new File(pathname);
                                                    absolute = absoluteFile.isAbsolute();
                                                    if (field4012) {
                                                        break Label_0822;
                                                    }
                                                    if (!absolute) {
                                                        absoluteFile = absoluteFile.getAbsoluteFile();
                                                        final boolean exists = absoluteFile.exists();
                                                        if (field4012) {
                                                            break Label_0822;
                                                        }
                                                        if (!exists) {
                                                            final File[] listRoots = File.listRoots();
                                                            final int length3 = listRoots.length;
                                                            int k = 0;
                                                            while (k < length3) {
                                                                final File file4 = new File(listRoots[k], pathname);
                                                                if (!field4012) {
                                                                    final boolean exists2 = file4.exists();
                                                                    if (field4012) {
                                                                        break Label_0822;
                                                                    }
                                                                    if (exists2) {
                                                                        absoluteFile = file4;
                                                                        if (!field4012) {
                                                                            break;
                                                                        }
                                                                    }
                                                                    ++k;
                                                                }
                                                                if (field4012) {
                                                                    break;
                                                                }
                                                            }
                                                            if (field4012) {
                                                                goto Label_0780;
                                                            }
                                                        }
                                                    }
                                                }
                                                else {
                                                    if (contains6) {
                                                        absoluteFile = new File("/" + pathname);
                                                        if (!field4012) {
                                                            break Label_0817;
                                                        }
                                                    }
                                                    absoluteFile = new File(pathname);
                                                }
                                            }
                                            absoluteFile.exists();
                                        }
                                        if (absolute || field4012) {
                                            final FileInputStream fileInputStream = new FileInputStream(absoluteFile);
                                            final byte[] array3 = new byte[1024];
                                            final MessageDigest instance = MessageDigest.getInstance("MD5");
                                            MessageDigest messageDigest = null;
                                            while (true) {
                                                final int read = fileInputStream.read(array3);
                                                while (true) {
                                                    Label_0880: {
                                                        if (read <= 0) {
                                                            break Label_0880;
                                                        }
                                                        messageDigest.update(array3, 0, read);
                                                    }
                                                    if (read == -1) {
                                                        fileInputStream.close();
                                                        messageDigest = instance;
                                                        if (!field4012) {
                                                            break;
                                                        }
                                                        continue;
                                                    }
                                                    break;
                                                }
                                            }
                                            digest = messageDigest.digest();
                                        }
                                    }
                                    ++j;
                                }
                                if (field4012) {
                                    break;
                                }
                            }
                            try {
                                final String lowerCase2 = System.getProperty("os.name").toLowerCase();
                                ClassLoader classLoader = null;
                                Block_76: {
                                    if (!field4012) {
                                        lowerCase2.contains("windows");
                                        if (!contains5) {
                                            classLoader = null;
                                            break Block_76;
                                        }
                                        System.getProperty("java.vm.name");
                                    }
                                    final String s5 = lowerCase2;
                                    if (!field4012 && !s5.contains("Client VM")) {}
                                    final String s6 = s5;
                                    try {
                                        System.load(System.getProperty("java.home") + s6);
                                    }
                                    catch (final UnsatisfiedLinkError cause) {
                                        throw new RuntimeException(cause);
                                    }
                                    classLoader = Class5941.class.getClassLoader();
                                }
                                try {
                                    final Class<?> loadClass = ClassLoader.getSystemClassLoader().loadClass("jdk.internal.module.IllegalAccessLogger");
                                    unsafe.putObjectVolatile(loadClass, unsafe.staticFieldOffset(loadClass.getDeclaredField("logger")), null);
                                }
                                catch (final Throwable t) {}
                                final Method declaredMethod = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
                                declaredMethod.setAccessible(true);
                                final long longValue;
                                final long address = longValue = (long)declaredMethod.invoke(null, classLoader, "gHotSpotVMStructs");
                                Label_1144: {
                                    if (!field4012) {
                                        if (longValue == 0L) {
                                            break Label_1144;
                                        }
                                        unsafe.getLong(address);
                                    }
                                    unsafe.putLong(longValue, 0L);
                                }
                            }
                            catch (final Exception ex2) {}
                        }
                        final byte[] b2 = new byte[method11536.length + method11537.length + digest.length];
                        System.arraycopy(method11536, 0, b2, 0, method11536.length);
                        System.arraycopy(method11537, 0, b2, method11536.length, method11537.length);
                        System.arraycopy(digest, 0, b2, method11536.length + method11537.length, digest.length);
                        dataOutputStream.write(b2);
                        dataOutputStream.writeUTF(v1);
                        final int int1 = dataInputStream.readInt();
                        int n2;
                        final int n = n2 = int1;
                        int n4;
                        final int n3 = n4 = 1;
                        Label_1375: {
                            if (!field4012) {
                                if (n == n3) {
                                    FabricGuiEntry.displayExitMessage("Invalid HWID", "Invalid HWID", "Please open a HWID reset ticket", FabricStatusTree$FabricTreeWarningLevel.ERROR);
                                    if (!field4012) {
                                        break Label_1375;
                                    }
                                }
                                final int n5;
                                n2 = (n5 = int1);
                                final int n6;
                                n4 = (n6 = 2);
                            }
                            int status = 0;
                            int n7 = 0;
                            Label_1341: {
                                if (!field4012) {
                                    if (n == n3) {
                                        FabricGuiEntry.displayExitMessage("Outdated files detected", "Outdated files detected", "Try restarting your minecraft launcher, if that doesn't work, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
                                        if (!field4012) {
                                            break Label_1375;
                                        }
                                    }
                                    n7 = (n2 = (status = int1));
                                    if (field4012) {
                                        break Label_1341;
                                    }
                                    n4 = 3;
                                }
                                if (n2 == n4) {
                                    final File file5 = file2;
                                    if (field4012 || file5.delete()) {
                                        file5.deleteOnExit();
                                    }
                                    FabricGuiEntry.displayExitMessage("Invalid Token", "Invalid Token", "Please relaunch and re-login, if that doesn't work, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
                                    if (!field4012) {
                                        break Label_1375;
                                    }
                                }
                                status = (n7 = int1);
                            }
                            if (!field4012) {
                                if (n7 == 0) {
                                    break Label_1375;
                                }
                                FabricGuiEntry.displayExitMessage("Error authenticating", "Error authenticating", "Error code: " + int1, FabricStatusTree$FabricTreeWarningLevel.ERROR);
                                status = 0;
                            }
                            System.exit(status);
                        }
                        final VMOption vmOption = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class).getVMOption("DisableAttachMechanism");
                        final HashMap<String, byte[]> hashMap = new HashMap<String, byte[]>();
                        final boolean equals = "false".equals(vmOption.getValue());
                        if (!field4012) {
                            if (equals) {
                                return hashMap;
                            }
                            vmOption.isWriteable();
                        }
                        if (!equals) {
                            final byte[] nBytes = dataInputStream.readNBytes(dataInputStream.readInt());
                            final int int2 = dataInputStream.readInt();
                            final String[] array4 = new String[int2];
                            final int[] array5 = new int[int2];
                            final byte[][] array6 = new byte[int2][20];
                            int l = 0;
                            while (true) {
                                while (l < int2) {
                                    array4[l] = dataInputStream.readUTF();
                                    ++l;
                                    if (field4012) {
                                        while (true) {
                                            while (l < int2) {
                                                array5[l] = dataInputStream.readInt();
                                                ++l;
                                                if (field4012) {
                                                    while (l < int2) {
                                                        array6[l] = dataInputStream.readNBytes(20);
                                                        ++l;
                                                        if (field4012) {
                                                            break;
                                                        }
                                                    }
                                                    final HashMap hashMap2 = new HashMap<String, byte[]>(int2);
                                                    int n8 = 0;
                                                    while (true) {
                                                        while (n8 < int2) {
                                                            final String method11538 = method11533(array4[n8], method11535);
                                                            final int n10;
                                                            final int n9 = n10 = (method11538.startsWith("c-") ? 1 : 0);
                                                            if (field4012) {
                                                                final int initialCapacity = n10;
                                                                final ArrayList mixins = new ArrayList<String>(initialCapacity);
                                                                int n11 = 0;
                                                                while (n11 < initialCapacity) {
                                                                    mixins.add(method11533(dataInputStream.readUTF(), method11535));
                                                                    ++n11;
                                                                    if (field4012) {
                                                                        return hashMap;
                                                                    }
                                                                    if (field4012) {
                                                                        break;
                                                                    }
                                                                }
                                                                MixinBootstrap.setMixins((List)mixins);
                                                                BozeClassLoader.method2305((Function<byte[], byte[]>)Class5941::lambda$load$0);
                                                                BozeClassLoader.method2306((BiConsumer<Cipher, byte[]>)Class5941::lambda$load$1);
                                                                socket.close();
                                                                return hashMap;
                                                            }
                                                            Label_1774: {
                                                                Label_1752: {
                                                                    Label_1746: {
                                                                        if (!field4012) {
                                                                            if (n9 != 0) {
                                                                                final byte[] method11539 = method11536(array6[n8], method11535);
                                                                                hashMap2.put((Object)new String(method11539), (Object)dataInputStream.readNBytes(array5[n8]));
                                                                                hashMap.put(method11538.substring(2), method11539);
                                                                                if (!field4012) {
                                                                                    break Label_1774;
                                                                                }
                                                                            }
                                                                            final String s7 = method11538;
                                                                            if (field4012) {
                                                                                break Label_1746;
                                                                            }
                                                                            s7.contains(".frag");
                                                                        }
                                                                        if (n9 == 0) {
                                                                            final String s8 = method11538;
                                                                            if (field4012) {
                                                                                break Label_1774;
                                                                            }
                                                                            if (!s8.contains(".vert")) {
                                                                                break Label_1752;
                                                                            }
                                                                        }
                                                                        hashMap.put(method11538, method11535(method11536(dataInputStream.readNBytes(array5[n8]), method11535), this.method11537(method11538)));
                                                                    }
                                                                    if (!field4012) {
                                                                        break Label_1774;
                                                                    }
                                                                }
                                                                hashMap.put(method11538, method11536(dataInputStream.readNBytes(array5[n8]), method11535));
                                                            }
                                                            ++n8;
                                                            if (field4012) {
                                                                break;
                                                            }
                                                        }
                                                        int n10 = dataInputStream.readInt();
                                                        continue;
                                                    }
                                                }
                                                if (field4012) {
                                                    break;
                                                }
                                            }
                                            l = 0;
                                            continue;
                                        }
                                    }
                                    if (field4012) {
                                        break;
                                    }
                                }
                                l = 0;
                                continue;
                            }
                        }
                        return hashMap;
                    }
                    break;
                } while (!field4012);
                dataInputStream.readUTF();
                continue;
            }
        }
        catch (final Exception ex3) {
            Log.error(LogCategory.BOZE, "Error loading client", (Throwable)ex3);
            FabricGuiEntry.displayExitMessage("Error Loading Client", "Error Loading Client", "Error: " + ex3.getMessage() + " - if this error persists, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            return new HashMap<String, byte[]>();
        }
    }

    private void method11525(final File v1, final String v2) {
        final boolean field4012 = Count.field4012;
        final File file = new File(v1, "dt");
        final boolean b = field4012;
        try {
            final File file2 = file;
            if (b || file2.exists()) {
                final String string = Files.readString(file2.toPath());
                if (string.length() == 16) {
                    final Socket socket = new Socket("auth.boze.dev", 3000);
                    final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(method11531("debug0x0001"));
                    final String method11534 = method11534(dataInputStream.readUTF(), dataInputStream.readUTF());
                    dataOutputStream.writeUTF(method11534(v2, method11534));
                    dataOutputStream.writeUTF(method11534(string, method11534));
                    final boolean equals = method11534(dataInputStream.readUTF(), method11534).equals("validtoken");
                    Label_0254: {
                        if (!b) {
                            if (equals) {
                                final String[] method11535 = this.method11527();
                                dataOutputStream.writeInt(method11535.length);
                                final String[] array = method11535;
                                final int length = array.length;
                                int i = 0;
                                while (i < length) {
                                    dataOutputStream.writeUTF(method11534(array[i], method11534));
                                    ++i;
                                    if (b) {
                                        break Label_0254;
                                    }
                                    if (b) {
                                        break;
                                    }
                                }
                                if (!b) {
                                    break Label_0254;
                                }
                            }
                            file.delete();
                        }
                    }
                    socket.close();
                }
            }
        }
        catch (final Exception ex) {}
    }

    private byte[] method11526() {
        final boolean field4012 = Count.field4012;
        final StringBuilder sb = new StringBuilder();
        final boolean b = field4012;
        final String lowerCase = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        final boolean contains = lowerCase.contains("win");
        Label_0226: {
            if (!b) {
                if (contains) {
                    sb.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"));
                    sb.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"));
                    sb.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"));
                    sb.append(this.method11528(this.method11529() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description"));
                    if (!b) {
                        break Label_0226;
                    }
                }
                lowerCase.contains("mac");
            }
            if (contains) {
                sb.append(this.method11528("sysctl -n machdep.cpu.brand_string"));
                sb.append(this.method11528("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"));
                sb.append(this.method11528("sysctl hw.ncpu"));
                sb.append(this.method11528("sysctl hw.memsize"));
                if (!b) {
                    break Label_0226;
                }
            }
            sb.append(this.method11530(new String[] { "/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\"" }));
        }
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("MD5");
        }
        catch (final NoSuchAlgorithmException ex) {}
        instance.update(sb.toString().getBytes(StandardCharsets.UTF_8));
        return instance.digest();
    }

    private String[] method11527() {
        final boolean field4012 = Count.field4012;
        final String lowerCase = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        final boolean b = field4012;
        int contains;
        final int n = contains = (lowerCase.contains("win") ? 1 : 0);
        if (!b) {
            if (n != 0) {
                final String[] array = { this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"), this.method11528(this.method11529() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"), this.method11528(this.method11529() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"), this.method11528(this.method11529() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description") };
                if (!b) {
                    return array;
                }
            }
            final boolean contains2;
            contains = ((contains2 = lowerCase.contains("mac")) ? 1 : 0);
        }
        if (!b) {
            if (n != 0) {
                final String[] array = { this.method11528("sysctl -n machdep.cpu.brand_string"), this.method11528("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"), this.method11528("sysctl hw.ncpu"), this.method11528("sysctl hw.memsize") };
                if (!b) {
                    return array;
                }
            }
            contains = 1;
        }
        final String[] array = new String[contains];
        array[0] = this.method11530(new String[] { "/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\"" });
        return array;
    }

    private String method11528(final String v1) {
        final Process exec = Runtime.getRuntime().exec(v1);
        exec.waitFor();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        final StringBuilder obj = new StringBuilder();
        final Stream<String> lines = bufferedReader.lines();
        final StringBuilder sb = obj;
        Objects.requireNonNull(obj);
        lines.forEach(sb::append);
        return obj.toString();
    }

    private String method11529() {
        final String getenv = System.getenv("SystemRoot");
        final boolean field4012 = Count.field4012;
        final File file = new File(getenv, "System32" + File.separatorChar + "WindowsPowerShell" + File.separatorChar + "v1.0");
        final boolean b = field4012;
        final boolean exists = file.exists();
        File file2 = null;
        if (!b) {
            if (!exists) {
                throw new IOException("\"" + file.getAbsolutePath().toString() + "\" does not exist or is not a directory!");
            }
            file2 = file;
            if (b) {
                return file2.getAbsolutePath() + "\\powershell.exe";
            }
            file2.isDirectory();
        }
        if (exists) {
            return file2.getAbsolutePath() + "\\powershell.exe";
        }
        throw new IOException("\"" + file.getAbsolutePath().toString() + "\" does not exist or is not a directory!");
    }

    private String method11530(final String[] v1) {
        final Process exec = Runtime.getRuntime().exec(v1);
        final boolean field4012 = Count.field4012;
        exec.waitFor();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        final boolean b = field4012;
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            final StringBuilder append = sb.append(line);
            if (b) {
                return append.toString();
            }
            if (b) {
                break;
            }
        }
        final StringBuilder append = sb;
        return append.toString();
    }

    public static String method11531(final String v0) {
        try {
            return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(v0.getBytes(StandardCharsets.UTF_8)));
        }
        catch (final NoSuchAlgorithmException ex) {
            return "null";
        }
    }

    private byte[] method11532(final InputStream v1) {
        final boolean field4012 = Count.field4012;
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] b = new byte[65535];
        int i = v1.read(b);
        final boolean b2 = field4012;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        while (i != -1) {
            byteArrayOutputStream2 = byteArrayOutputStream;
            if (b2) {
                return byteArrayOutputStream2.toByteArray();
            }
            byteArrayOutputStream2.write(b, 0, i);
            i = v1.read(b);
            if (b2) {
                break;
            }
        }
        return byteArrayOutputStream2.toByteArray();
    }

    static String method11533(final String v0, final byte[] v1) {
        final StringBuilder sb = new StringBuilder();
        final boolean field4012 = Count.field4012;
        int i = 0;
        final boolean b = field4012;
        while (i < v0.length()) {
            final StringBuilder append = sb.append((char)(v0.charAt(i) ^ v1[i % v1.length]));
            if (b) {
                return append.toString();
            }
            ++i;
            if (b) {
                break;
            }
        }
        final StringBuilder append = sb;
        return append.toString();
    }

    static String method11534(final String v0, final String v1) {
        final boolean field4012 = Count.field4012;
        final StringBuilder sb = new StringBuilder();
        int i = 0;
        final boolean b = field4012;
        while (i < v0.length()) {
            final StringBuilder append = sb.append((char)(v0.charAt(i) ^ v1.charAt(i % v1.length())));
            if (b) {
                return append.toString();
            }
            ++i;
            if (b) {
                break;
            }
        }
        final StringBuilder append = sb;
        return append.toString();
    }

    static byte[] method11535(final byte[] v0, final String v1) {
        final boolean field4012 = Count.field4012;
        final byte[] array = new byte[v0.length];
        int i = 0;
        final boolean b = field4012;
        byte[] array2 = null;
        while (i < v0.length) {
            array2 = array;
            if (b) {
                return array2;
            }
            array2[i] = (byte)(v0[i] ^ v1.charAt(i % v1.length()));
            ++i;
            if (b) {
                break;
            }
        }
        return array2;
    }

    static byte[] method11536(final byte[] v0, final byte[] v1) {
        final boolean field4012 = Count.field4012;
        final byte[] array = new byte[v0.length];
        int i = 0;
        final boolean b = field4012;
        byte[] array2 = null;
        while (i < v0.length) {
            array2 = array;
            if (b) {
                return array2;
            }
            array2[i] = (byte)(v0[i] ^ v1[i % v1.length]);
            ++i;
            if (b) {
                break;
            }
        }
        return array2;
    }

    private String method11537(final String v1) {
        final String name = new File(v1).getName();
        final int lastIndex = name.lastIndexOf(46);
        return (lastIndex == -1) ? "" : name.substring(lastIndex + 1);
    }

    private static void lambda$load$1(final byte[] v0, final Cipher v1, final byte[] v2) {
        final byte[] src = new byte[12];
        System.arraycopy(v2, 0, src, 0, 12);
        final GCMParameterSpec params = new GCMParameterSpec(128, src);
        try {
            v1.init(2, new SecretKeySpec(v0, 0, v0.length, "AES"), params);
        }
        catch (final Exception ex) {}
    }

    private static byte[] lambda$load$0(final HashMap v0, final byte[] v1) {
        return v0.get(new String(v1));
    }
}
