package netutil;

import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import sun.misc.Unsafe;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Stream;

class BozeLoader {
    HashMap<String, byte[]> method11(final String v1, final String v2) throws Throwable {
        try {
            String s = "";
            File file = new File(System.getProperty("user.home"), "Boze" + File.separator + "cache");
            final String lowerCase = System.getProperty("os.name").toLowerCase();
            final boolean b = lowerCase.contains("nix") || lowerCase.contains("nux") || lowerCase.contains("aix");
            if (b) {
                file = new File(v2, "cache");
            }
            file.mkdirs();
            final File file2 = new File(file, "at");
            try {
                if (file2.exists()) {
                    final String string = Files.readString(file2.toPath());
                    if (string.length() == 92) {
                        s = string;
                    }
                }
            } catch (final Exception ex) {
            }
            if (s.isEmpty()) {
                //FabricGuiEntry.displayExitMessage("Authentication Error", "Authentication Error", "Unable to login, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            }
            this.method12(file, s);
            final Socket socket = new Socket("auth.boze.dev", 3000);
            final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(method18("loaderInitiateBeta"));
            final String[] array = {"-javaagent", "-Xdebug", "-agentlib", "-Xrunjdwp", "-Xnoagent", "-DproxySet", "-DproxyHost", "-DproxyPort", "-Djavax.net.ssl.trustStore", "-Djavax.net.ssl.trustStorePassword"};
            for (String s2 : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
                for (int length = array.length, i = 0; i < length; ++i) {
                    if (s2.toLowerCase(Locale.ROOT).contains(array[i].toLowerCase(Locale.ROOT))) {
                        final Field declaredField = Unsafe.class.getDeclaredField("theUnsafe");
                        declaredField.setAccessible(true);
                        ((Unsafe) declaredField.get(null)).putAddress(0L, 0L);
                    }
                }
            }
            final String method21 = method21(dataInputStream.readUTF(), dataInputStream.readUTF());
            final byte[] method22 = method22(method21.getBytes(StandardCharsets.UTF_8), s);
            final byte[] method23 = method23(this.method13(), method22);
            final byte[] method24 = method22(s.getBytes(StandardCharsets.UTF_8), method21);
            byte[] digest = null;
            final Field declaredField2 = Unsafe.class.getDeclaredField("theUnsafe");
            declaredField2.setAccessible(true);
            final Unsafe unsafe = (Unsafe) declaredField2.get(null);
            final String[] split = System.getProperty("java.class.path").split("[;:]");
            for (int length2 = split.length, j = 0; j < length2; ++j) {
                final String pathname = split[j];
                if (pathname.contains("boze-loader-beta-0.")) {
                    File absoluteFile;
                    if (System.getProperty("os.name").toLowerCase().contains("win")) {
                        absoluteFile = new File(pathname);
                        if (!absoluteFile.isAbsolute()) {
                            absoluteFile = absoluteFile.getAbsoluteFile();
                            if (!absoluteFile.exists()) {
                                final File[] listRoots = File.listRoots();
                                for (int length3 = listRoots.length, k = 0; k < length3; ++k) {
                                    final File file3 = new File(listRoots[k], pathname);
                                    if (file3.exists()) {
                                        absoluteFile = file3;
                                        break;
                                    }
                                }
                            }
                        }
                    } else if (b) {
                        absoluteFile = new File("/" + pathname);
                    } else {
                        absoluteFile = new File(pathname);
                    }
                    if (absoluteFile.exists()) {
                        final FileInputStream fileInputStream = new FileInputStream(absoluteFile);
                        final byte[] array3 = new byte[1024];
                        final MessageDigest instance = MessageDigest.getInstance("MD5");
                        int l;
                        do {
                            l = fileInputStream.read(array3);
                            if (l > 0) {
                                instance.update(array3, 0, l);
                            }
                        } while (l != -1);
                        fileInputStream.close();
                        digest = instance.digest();
                    }
                }
            }
            try {
                ClassLoader classLoader;
                if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                    final String s3 = System.getProperty("java.vm.name").contains("Client VM") ? "/bin/client/jvm.dll" : "/bin/server/jvm.dll";
                    try {
                        System.load(System.getProperty("java.home") + s3);
                    } catch (final UnsatisfiedLinkError cause) {
                        throw new RuntimeException(cause);
                    }
                    classLoader = BozeLoader.class.getClassLoader();
                } else {
                    classLoader = null;
                }
                try {
                    final Class<?> loadClass = ClassLoader.getSystemClassLoader().loadClass("jdk.internal.module.IllegalAccessLogger");
                    unsafe.putObjectVolatile(loadClass, unsafe.staticFieldOffset(loadClass.getDeclaredField("logger")), null);
                } catch (final Throwable t) {
                }
                final Method declaredMethod = ClassLoader.class.getDeclaredMethod("findNative", ClassLoader.class, String.class);
                declaredMethod.setAccessible(true);
                final long longValue = (long) declaredMethod.invoke(null, classLoader, "gHotSpotVMStructs");
                if (longValue != 0L) {
                    unsafe.putLong(unsafe.getLong(longValue), 0L);
                }
            } catch (final Exception ex2) {
            }
            final byte[] b2 = new byte[method23.length + method24.length + digest.length];
            System.arraycopy(method23, 0, b2, 0, method23.length);
            System.arraycopy(method24, 0, b2, method23.length, method24.length);
            System.arraycopy(digest, 0, b2, method23.length + method24.length, digest.length);
            dataOutputStream.write(b2);
            dataOutputStream.writeUTF(v1);
            final int int1 = dataInputStream.readInt();
            if (int1 == 1) {
                //FabricGuiEntry.displayExitMessage("Invalid HWID", "Invalid HWID", "Please open a HWID reset ticket", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            } else if (int1 == 2) {
                //FabricGuiEntry.displayExitMessage("Outdated files detected", "Outdated files detected", "Try restarting your minecraft launcher, if that doesn't work, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            } else if (int1 == 3) {
                if (!file2.delete()) {
                    file2.deleteOnExit();
                }
                //FabricGuiEntry.displayExitMessage("Invalid Token", "Invalid Token", "Please relaunch and re-login, if that doesn't work, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            } else if (int1 != 0) {
                //FabricGuiEntry.displayExitMessage("Error authenticating", "Error authenticating", "Error code: " + int1, FabricStatusTree$FabricTreeWarningLevel.ERROR);
                System.exit(0);
            }
            final VMOption vmOption = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class).getVMOption("DisableAttachMechanism");
            final HashMap<String, byte[]> hashMap = new HashMap<String, byte[]>();
            if ("false".equals(vmOption.getValue()) || vmOption.isWriteable()) {
                return hashMap;
            }
            final byte[] nBytes = dataInputStream.readNBytes(dataInputStream.readInt());
            final int int2 = dataInputStream.readInt();
            final String[] array4 = new String[int2];
            final int[] array5 = new int[int2];
            final byte[][] array6 = new byte[int2][20];
            for (int n = 0; n < int2; ++n) {
                array4[n] = dataInputStream.readUTF();
            }
            for (int n2 = 0; n2 < int2; ++n2) {
                array5[n2] = dataInputStream.readInt();
            }
            for (int n3 = 0; n3 < int2; ++n3) {
                array6[n3] = dataInputStream.readNBytes(20);
            }
            final HashMap hashMap2 = new HashMap<String, byte[]>(int2);
            for (int n4 = 0; n4 < int2; ++n4) {
                final String method25 = method20(array4[n4], method22);
                if (method25.startsWith("c-")) {
                    final byte[] method26 = method23(array6[n4], method22);
                    hashMap2.put(new String(method26), dataInputStream.readNBytes(array5[n4]));
                    hashMap.put(method25.substring(2), method26);
                } else if (method25.contains(".frag") || method25.contains(".vert")) {
                    hashMap.put(method25, method22(method23(dataInputStream.readNBytes(array5[n4]), method22), this.method24(method25)));
                } else {
                    hashMap.put(method25, method23(dataInputStream.readNBytes(array5[n4]), method22));
                }
            }
            final int int3 = dataInputStream.readInt();
            final ArrayList mixins = new ArrayList<String>(int3);
            for (int n5 = 0; n5 < int3; ++n5) {
                mixins.add(method20(dataInputStream.readUTF(), method22));
            }

            // advanced boze loader mechanism...
            // MixinBootstrap.setMixins((List)mixins);
            BozeMixins.method33(arg_0 -> BozeLoader.method26(hashMap, arg_0));
            BozeMixins.method34((arg_0, arg_1) -> BozeLoader.method25(nBytes, arg_0, arg_1));
            socket.close();
            return hashMap;
        } catch (final Exception ex3) {
            //Log.error(LogCategory.BOZE, "Error loading client", (Throwable)ex3);
            //FabricGuiEntry.displayExitMessage("Error Loading Client", "Error Loading Client", "Error: " + ex3.getMessage() + " - if this error persists, please contact support", FabricStatusTree$FabricTreeWarningLevel.ERROR);
            return new HashMap<String, byte[]>();
        }
    }

    private void method12(final File v1, final String v2) throws Throwable {
        final File file = new File(v1, "dt");
        try {
            if (file.exists()) {
                final String string = Files.readString(file.toPath());
                if (string.length() == 16) {
                    final Socket socket = new Socket("auth.boze.dev", 3000);
                    final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF(method18("debug0x0001"));
                    final String method21 = method21(dataInputStream.readUTF(), dataInputStream.readUTF());
                    dataOutputStream.writeUTF(method21(v2, method21));
                    dataOutputStream.writeUTF(method21(string, method21));
                    if (method21(dataInputStream.readUTF(), method21).equals("validtoken")) {
                        final String[] method22 = this.method14();
                        dataOutputStream.writeInt(method22.length);
                        final String[] array = method22;
                        for (int length = array.length, i = 0; i < length; ++i) {
                            dataOutputStream.writeUTF(method21(array[i], method21));
                        }
                    } else {
                        file.delete();
                    }
                    socket.close();
                }
            }
        } catch (final Exception ex) {
        }
    }

    private byte[] method13() throws Throwable {
        final StringBuilder sb = new StringBuilder();
        final String lowerCase = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (lowerCase.contains("win")) {
            sb.append(this.method15(this.method16() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"));
            sb.append(this.method15(this.method16() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"));
            sb.append(this.method15(this.method16() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"));
            sb.append(this.method15(this.method16() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description"));
        } else if (lowerCase.contains("mac")) {
            sb.append(this.method15("sysctl -n machdep.cpu.brand_string"));
            sb.append(this.method15("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"));
            sb.append(this.method15("sysctl hw.ncpu"));
            sb.append(this.method15("sysctl hw.memsize"));
        } else {
            sb.append(this.method17(new String[]{"/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\""}));
        }
        MessageDigest instance = null;
        try {
            instance = MessageDigest.getInstance("MD5");
        } catch (final NoSuchAlgorithmException ex) {
        }
        instance.update(sb.toString().getBytes(StandardCharsets.UTF_8));
        return instance.digest();
    }

    private String[] method14() throws Throwable {
        final String lowerCase = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String[] array;
        if (lowerCase.contains("win")) {
            array = new String[]{this.method15(this.method16() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).Capacity"), this.method15(this.method16() + " (get-wmiobject -class win32_processor -namespace root\\CIMV2)"), this.method15(this.method16() + " (get-wmiobject -class win32_physicalmemory -namespace root\\CIMV2).SMBiosMemoryType"), this.method15(this.method16() + " (get-wmiobject -class win32_videocontroller -namespace root\\CIMV2).Description")};
        } else if (lowerCase.contains("mac")) {
            array = new String[]{this.method15("sysctl -n machdep.cpu.brand_string"), this.method15("system_profiler SPHardwareDataType | awk '/Serial/ {print $4}'"), this.method15("sysctl hw.ncpu"), this.method15("sysctl hw.memsize")};
        } else {
            array = new String[]{this.method17(new String[]{"/bin/sh", "-c", "lscpu | grep -e \"Architecture:\" -e \"Byte Order:\" -e \"Model name:\""})};
        }
        return array;
    }

    private String method15(final String v1) throws Throwable {
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

    private String method16() throws Throwable {
        final File file = new File(System.getenv("SystemRoot"), "System32" + File.separatorChar + "WindowsPowerShell" + File.separatorChar + "v1.0");
        if (!file.exists() || !file.isDirectory()) {
            throw new IOException("\"" + file.getAbsolutePath() + "\" does not exist or is not a directory!");
        }
        return file.getAbsolutePath() + "\\powershell.exe";
    }

    private String method17(final String[] v1) throws Throwable {
        final Process exec = Runtime.getRuntime().exec(v1);
        exec.waitFor();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    public static String method18(final String v0) {
        try {
            return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(v0.getBytes(StandardCharsets.UTF_8)));
        } catch (final NoSuchAlgorithmException ex) {
            return "null";
        }
    }

    private byte[] method19(final InputStream v1) throws Throwable {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final byte[] b = new byte[65535];
        for (int i = v1.read(b); i != -1; i = v1.read(b)) {
            byteArrayOutputStream.write(b, 0, i);
        }
        return byteArrayOutputStream.toByteArray();
    }

    static String method20(final String v0, final byte[] v1) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v0.length(); ++i) {
            sb.append((char) (v0.charAt(i) ^ v1[i % v1.length]));
        }
        return sb.toString();
    }

    static String method21(final String v0, final String v1) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v0.length(); ++i) {
            sb.append((char) (v0.charAt(i) ^ v1.charAt(i % v1.length())));
        }
        return sb.toString();
    }

    static byte[] method22(final byte[] v0, final String v1) {
        final byte[] array = new byte[v0.length];
        for (int i = 0; i < v0.length; ++i) {
            array[i] = (byte) (v0[i] ^ v1.charAt(i % v1.length()));
        }
        return array;
    }

    static byte[] method23(final byte[] v0, final byte[] v1) {
        final byte[] array = new byte[v0.length];
        for (int i = 0; i < v0.length; ++i) {
            array[i] = (byte) (v0[i] ^ v1[i % v1.length]);
        }
        return array;
    }

    private String method24(final String v1) {
        final String name = new File(v1).getName();
        final int lastIndex = name.lastIndexOf(46);
        return (lastIndex == -1) ? "" : name.substring(lastIndex + 1);
    }

    private static void method25(final byte[] v0, final Cipher v1, final byte[] v2) {
        final byte[] src = new byte[12];
        System.arraycopy(v2, 0, src, 0, 12);
        final GCMParameterSpec params = new GCMParameterSpec(128, src);
        try {
            v1.init(2, new SecretKeySpec(v0, 0, v0.length, "AES"), params);
        } catch (final Exception ex) {
        }
    }

    private static byte[] method26(final HashMap v0, final byte[] v1) {
        return (byte[]) v0.get(new String(v1));
    }
}
