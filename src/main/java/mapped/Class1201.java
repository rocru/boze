package mapped;

import dev.boze.client.Boze;
import dev.boze.client.enums.ConfigType;
import dev.boze.client.manager.ConfigManager;
import dev.boze.client.settings.CurrentProfileSetting;
import dev.boze.client.systems.modules.client.Profiles;
import dev.boze.client.utils.network.BozeExecutor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import net.minecraft.nbt.NbtCompound;

public class Class1201 {
   public static final HashSet<String> field57 = new HashSet();
   public static final HashSet<String> field58 = new HashSet();
   public static final HashSet<String> field59 = new HashSet();
   public static final HashSet<String> field60 = new HashSet();
   public static final HashSet<String> field61 = new HashSet();
   private static boolean field62 = false;

   public static void method2378() {
      for (String var5 : new ArrayList(Arrays.asList(ConfigManager.get(ConfigType.PROFILE)))) {
         if (var5.equals("v2.special.selection")) {
            System.out.println("Selection exists");
            field62 = true;
         } else if (!var5.startsWith("v2.")) {
            field57.add(var5);
         } else if (var5.contains(".main.")) {
            field58.add(var5);
         } else if (var5.contains(".visuals.")) {
            field59.add(var5);
         } else if (var5.contains(".binds.")) {
            field60.add(var5);
         } else if (var5.contains(".client.")) {
            field61.add(var5);
         }
      }

      if (field62) {
         System.out.println("Loading from selection");
         method2387();
      } else {
         if (field58.isEmpty() && !field57.isEmpty()) {
            NbtCompound var6 = method2389("MAIN_PROFILE");
            if (var6 != null) {
               Boze.getModules().fromTag(var6);
            }
         }

         method2383(false);
      }
   }

   private static NbtCompound method2379(String var0) {
      return switch (var0) {
         case "v2.main." -> Boze.getModules().method225();
         case "v2.visuals." -> Boze.getModules().method226();
         case "v2.binds." -> Boze.getModules().method227();
         case "v2.client." -> Boze.getModules().method228();
         default -> throw new IllegalStateException("Unexpected value: " + var0);
      };
   }

   public static void method2380(String prefix, NbtCompound compound) {
      if (var1254 != null) {
         switch (var1253) {
            case "v2.main.":
               Boze.getModules().method232(var1254);
               break;
            case "v2.visuals.":
               Boze.getModules().method394(var1254);
               break;
            case "v2.binds.":
               Boze.getModules().method233(var1254);
               break;
            case "v2.client.":
               Boze.getModules().method234(var1254);
         }
      }
   }

   public static void method2381(CurrentProfileSetting setting, String newName) {
      NbtCompound var5 = method2379(var1255.field969);
      if (var5 != null) {
         String var7 = var1255.method1322();
         BozeExecutor.method2200(Class1201::lambda$swap$0);
      }

      var1255.method1341(var1256);
      NbtCompound var6 = method2389(var1256);
      method2380(var1255.field969, var6);
   }

   public static void method2382(boolean thread, CurrentProfileSetting setting, String name) {
      NbtCompound var6 = method2379(var1258.field969);
      if (var6 != null) {
         var1258.field968.add(var1259);
         if (var1257) {
            BozeExecutor.method2200(Class1201::lambda$save$1);
         } else {
            method2386(var1259, var6);
         }
      }
   }

   public static void method2383(boolean thread) {
      String var4 = Profiles.INSTANCE.field762.method1322();
      String var5 = Profiles.INSTANCE.field763.method1322();
      String var6 = Profiles.INSTANCE.field764.method1322();
      String var7 = Profiles.INSTANCE.field765.method1322();
      if (var4.isEmpty()) {
         var4 = "v2.main.default";
         Profiles.INSTANCE.field762.method1341(var4);
      }

      if (var5.isEmpty()) {
         var5 = "v2.visuals.default";
         Profiles.INSTANCE.field763.method1341(var5);
      }

      if (var6.isEmpty()) {
         var6 = "v2.binds.default";
         Profiles.INSTANCE.field764.method1341(var6);
      }

      if (var7.isEmpty()) {
         var7 = "v2.client.default";
         Profiles.INSTANCE.field765.method1341(var7);
      }

      method2384(var1260, var4, var5, var6, var7);
   }

   public static void method2384(boolean thread, String main, String visuals, String binds, String client) {
      NbtCompound var8 = Boze.getModules().method225();
      NbtCompound var9 = Boze.getModules().method226();
      NbtCompound var10 = Boze.getModules().method227();
      NbtCompound var11 = Boze.getModules().method228();
      field58.add(var1262);
      field59.add(var1263);
      field60.add(var1264);
      field61.add(var1265);
      if (var1261) {
         BozeExecutor.method2200(Class1201::lambda$save$2);
      } else {
         method2385(var1262, var1263, var1264, var1265, var8, var9, var10, var11);
      }
   }

   private static void method2385(String var0, String var1, String var2, String var3, NbtCompound var4, NbtCompound var5, NbtCompound var6, NbtCompound var7) {
      NbtCompound var10 = new NbtCompound();
      var10.putString("main", var0);
      var10.putString("visuals", var1);
      var10.putString("binds", var2);
      var10.putString("client", var3);
      method2386("v2.special.selection", var10);
      method2386(var0, var4);
      method2386(var1, var5);
      method2386(var2, var6);
      method2386(var3, var7);
   }

   public static void method2386(String name, NbtCompound tag) {
      ConfigManager.uploadConfig(var1266, var1267, ConfigType.PROFILE);
   }

   public static void method2387() {
      NbtCompound var3 = method2389("v2.special.selection");
      String var4 = null;
      String var5 = null;
      String var6 = null;
      String var7 = null;
      if (var3.contains("main")) {
         var4 = var3.getString("main");
         Profiles.INSTANCE.field762.method1341(var4);
      } else {
         Profiles.INSTANCE.field762.method1341("v2.main.default");
      }

      if (var3.contains("visuals")) {
         var5 = var3.getString("visuals");
         Profiles.INSTANCE.field763.method1341(var5);
      } else {
         Profiles.INSTANCE.field763.method1341("v2.visuals.default");
      }

      if (var3.contains("binds")) {
         var6 = var3.getString("binds");
         Profiles.INSTANCE.field764.method1341(var6);
      } else {
         Profiles.INSTANCE.field764.method1341("v2.binds.default");
      }

      if (var3.contains("client")) {
         var7 = var3.getString("client");
         Profiles.INSTANCE.field765.method1341(var7);
      } else {
         Profiles.INSTANCE.field765.method1341("v2.client.default");
      }

      method2388(var4, var5, var6, var7);
   }

   public static void method2388(String main, String visuals, String binds, String client) {
      NbtCompound var7 = method2389(var1268);
      NbtCompound var8 = method2389(var1269);
      NbtCompound var9 = method2389(var1270);
      NbtCompound var10 = method2389(var1271);
      if (var7 != null) {
         Boze.getModules().method232(var7);
         field58.add(var1268);
      }

      if (var8 != null) {
         Boze.getModules().method394(var8);
         field59.add(var1269);
      }

      if (var9 != null) {
         Boze.getModules().method233(var9);
         field60.add(var1270);
      }

      if (var10 != null) {
         Boze.getModules().method234(var10);
         field61.add(var1271);
      }
   }

   public static NbtCompound method2389(String name) {
      return var1272 == null ? null : ConfigManager.downloadConfig(var1272, ConfigType.PROFILE);
   }

   public static boolean method2390() {
      return !field57.isEmpty();
   }

   private static void lambda$save$2(String var0, String var1, String var2, String var3, NbtCompound var4, NbtCompound var5, NbtCompound var6, NbtCompound var7) {
      method2385(var0, var1, var2, var3, var4, var5, var6, var7);
   }

   private static void lambda$save$1(String var0, NbtCompound var1) {
      method2386(var0, var1);
   }

   private static void lambda$swap$0(String var0, NbtCompound var1) {
      method2386(var0, var1);
   }
}
