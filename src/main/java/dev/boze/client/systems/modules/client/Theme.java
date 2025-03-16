package dev.boze.client.systems.modules.client;

import dev.boze.api.BozeInstance;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.GUILayout;
import dev.boze.client.enums.LetterMode;
import dev.boze.client.enums.LetterStyle;
import dev.boze.client.enums.ModuleDisplayMode;
import dev.boze.client.settings.BooleanSetting;
import dev.boze.client.settings.ColorSetting;
import dev.boze.client.settings.EnumSetting;
import dev.boze.client.settings.FloatSetting;
import dev.boze.client.settings.IntSetting;
import dev.boze.client.settings.MinMaxSetting;
import dev.boze.client.settings.RGBASetting;
import dev.boze.client.settings.SettingCategory;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.ConfigCategory;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.utils.RGBAColor;

public class Theme extends Module {
   public static final Theme INSTANCE = new Theme();
   private final RGBASetting field2408 = new RGBASetting("Accent", new RGBAColor(-7046189), "Accent color");
   private final RGBASetting field2409 = new RGBASetting("AccentText", new RGBAColor(-1), "Accent text color");
   private final RGBASetting field2410 = new RGBASetting("AccentSubText", new RGBAColor(-922746881), "Accent sub-text color");
   private final RGBASetting field2411 = new RGBASetting("Primary", new RGBAColor(-937484513), "Primary color");
   private final RGBASetting field2412 = new RGBASetting("Secondary", new RGBAColor(-518448871), "Secondary color");
   private final RGBASetting field2413 = new RGBASetting("Background", new RGBAColor(-517001425), "Background color");
   private final RGBASetting field2414 = new RGBASetting("Text", new RGBAColor(-1), "Text color");
   private final RGBASetting field2415 = new RGBASetting("SubText", new RGBAColor(-922746881), "Sub-text color");
   private final BooleanSetting field2416 = new BooleanSetting("TextShadow", false, "Shadow for all texts in the ClickGUI");
   private final SettingCategory field2417 = new SettingCategory("Header", "Header settings");
   private final IntSetting field2418 = new IntSetting("Height", 12, 10, 20, 1, "Header height", this.field2417);
   private final EnumSetting<AlignMode> field2419 = new EnumSetting<AlignMode>("Align", AlignMode.Left, "Header alignment", this.field2417);
   private final EnumSetting<LetterStyle> field2420 = new EnumSetting<LetterStyle>("Style", LetterStyle.Normal, "Header style", this.field2417);
   private final EnumSetting<LetterMode> field2421 = new EnumSetting<LetterMode>("Mode", LetterMode.Text, "Header mode", Theme::lambda$new$0, this.field2417);
   private final BooleanSetting field2422 = new BooleanSetting("CustomColor", false, "Custom color for header", this.field2417);
   private final ColorSetting field2423 = new ColorSetting("Color", new BozeDrawColor(-7046189), "Color for header", this.field2422::method419, this.field2417);
   private final SettingCategory field2424 = new SettingCategory("ModuleList", "Module list settings");
   private final IntSetting field2425 = new IntSetting("Width", 120, 80, 200, 5, "Module list width", this.field2424);
   private final MinMaxSetting field2426 = new MinMaxSetting(
      "Spacing",
      0.25,
      0.1,
      1.0,
      0.05,
      "Spacing between module lists\nAs a fraction of width\nYou need to re-open the gui for changes to take effect\n",
      this.field2424
   );
   private final IntSetting field2427 = new IntSetting("Indentation", 2, 0, 10, 1, "Module indentation in list", this.field2424);
   private final IntSetting field2428 = new IntSetting("Separation", 1, 0, 10, 1, "Module separation in list", this.field2424);
   private final SettingCategory field2429 = new SettingCategory("Module", "Module settings");
   private final IntSetting field2430 = new IntSetting("Height", 12, 0, 20, 1, "Module height", this.field2429);
   private final EnumSetting<AlignMode> field2431 = new EnumSetting<AlignMode>("Align", AlignMode.Left, "Module alignment", this.field2429);
   private final EnumSetting<LetterStyle> field2432 = new EnumSetting<LetterStyle>("Style", LetterStyle.Normal, "Module style", this.field2429);
   private final BooleanSetting field2433 = new BooleanSetting("LightAccent", true, "Makes accent less opaque", this.field2429);
   private final EnumSetting<ModuleDisplayMode> field2434 = new EnumSetting<ModuleDisplayMode>(
      "Option", ModuleDisplayMode.Icons, "Option to show on module elements", this.field2429
   );
   public final BooleanSetting field2435 = new BooleanSetting("PowerButton", true, "Show power button icon", this.field2429);
   public final BooleanSetting field2436 = new BooleanSetting("TuneButton", true, "Show tune button icon", this.field2429);
   public final BooleanSetting aa = new BooleanSetting("ConfigsButton", true, "Show configs button icon", this.field2429);
   public final BooleanSetting ab = new BooleanSetting("ExpandButton", true, "Show expand button icon", this.field2429);
   private final BooleanSetting ac = new BooleanSetting("HoverOnly", true, "Only show option when hovering over module", this.field2429);
   private final BooleanSetting ad = new BooleanSetting("ShowBinds", true, "Show binds on modules when not hovering", this::lambda$new$1, this.field2429);
   private final BooleanSetting ae = new BooleanSetting("CustomColors", false, "Custom colors for modules", this.field2429);
   private final ColorSetting af = new ColorSetting("OffColor", new BozeDrawColor(-937484513), "Color for disabled modules", this.ae::method419, this.field2429);
   private final ColorSetting ag = new ColorSetting("OnColor", new BozeDrawColor(-7046189), "Color for enabled modules", this.ae::method419, this.field2429);
   private final SettingCategory ah = new SettingCategory("Setting", "Setting settings");
   private final IntSetting ai = new IntSetting("Height", 14, 6, 20, 1, "Setting height", this.ah);
   private final BooleanSetting aj = new BooleanSetting("SideBar", false, "Show sidebar for settings", this.ah);
   private final BooleanSetting ak = new BooleanSetting(
      "SimpleGradients", false, "Use simple gradient picker\n5x5 button grid instead of normal picker\n", this.ah
   );
   private final SettingCategory al = new SettingCategory("Footer", "Footer settings");
   private final IntSetting am = new IntSetting("Height", 0, 0, 20, 1, "Footer height", this.al);
   private final BooleanSetting an = new BooleanSetting("CustomColor", false, "Custom color for footer", this.al);
   private final ColorSetting ao = new ColorSetting("Color", new BozeDrawColor(-7046189), "Color for footer", this.an::method419, this.al);
   private final BooleanSetting ap = new BooleanSetting("Outline", true, "Outline");
   private final BooleanSetting aq = new BooleanSetting("CustomColor", false, "Custom color for outline", this.ap);
   private final ColorSetting ar = new ColorSetting("Color", new BozeDrawColor(-7046189), "Color for outline", this.aq::method419, this.ap);
   private final FloatSetting as = new FloatSetting("Glow", 0.0F, 0.0F, 5.0F, 0.1F, "Glow for shader", this.ap);
   private final FloatSetting at = new FloatSetting("Strength", 0.2F, 0.02F, 2.0F, 0.02F, "Glow strength for shader", this::lambda$new$2, this.ap);
   private final IntSetting au = new IntSetting("Radius", 1, 0, 10, 1, "Outline radius for shader", this.ap);
   private final BooleanSetting av = new BooleanSetting("RoundCorners", false, "Rounded corners");
   private final MinMaxSetting aw = new MinMaxSetting(
      "Scale", 0.8, 0.1, 2.0, 0.1, "Scale factor for the ClickGUI\nYou need to re-open the gui for changes to take effect"
   );
   public final MinMaxSetting ax = new MinMaxSetting("BrightHighlight", 0.7, 0.05, 0.95, 0.05, "Factor for brightening highlight");
   public final MinMaxSetting ay = new MinMaxSetting("DarkenHighlight", 0.4, 0.05, 0.95, 0.05, "Factor for darkening highlight");

   public Theme() {
      super("Theme", "Theme for Boze GUI", Category.Client, ConfigCategory.Visuals);
      this.enabled = false;
   }

   @Override
   public boolean setEnabled(boolean newState) {
      this.enabled = false;
      return false;
   }

   public static RGBAColor method1347() {
      return INSTANCE.field2411.method1347();
   }

   public static RGBAColor method1348() {
      return INSTANCE.field2412.method1347();
   }

   public static RGBAColor method1349() {
      return INSTANCE.field2413.method1347();
   }

   public static RGBAColor method1350() {
      return INSTANCE.field2414.method1347();
   }

   public static RGBAColor method1351() {
      return INSTANCE.field2415.method1347();
   }

   public static RGBAColor method1352() {
      return INSTANCE.field2408.method1347();
   }

   public static RGBAColor method1353() {
      return INSTANCE.field2433.method419() && INSTANCE.field2408.method1347().field411 > 200
         ? INSTANCE.field2408.method1347().method1347().method196(200)
         : INSTANCE.field2408.method1347();
   }

   public static RGBAColor method1354() {
      return INSTANCE.field2409.method1347();
   }

   public static RGBAColor method1355() {
      return INSTANCE.field2410.method1347();
   }

   public static boolean method1356() {
      return INSTANCE.field2416.method419();
   }

   public static int method1357() {
      return INSTANCE.field2418.method434();
   }

   public static AlignMode method1358() {
      return INSTANCE.field2419.method461();
   }

   public static LetterStyle method1359() {
      return INSTANCE.field2420.method461();
   }

   public static LetterMode method1360() {
      return INSTANCE.field2421.method461();
   }

   public static boolean method1361() {
      return INSTANCE.field2422.method419();
   }

   public static BozeDrawColor method1362() {
      return INSTANCE.field2423.method1362();
   }

   public static int method1363() {
      return INSTANCE.field2425.method434();
   }

   public static double method1364() {
      return INSTANCE.field2426.getValue();
   }

   public static int method1365() {
      return INSTANCE.field2427.method434();
   }

   public static int method1366() {
      return INSTANCE.field2428.method434();
   }

   public static int method1367() {
      return INSTANCE.field2430.method434();
   }

   public static AlignMode method1368() {
      return INSTANCE.field2431.method461();
   }

   public static LetterStyle method1369() {
      return INSTANCE.field2432.method461();
   }

   public static ModuleDisplayMode method1370() {
      return INSTANCE.field2434.method461();
   }

   public static boolean method1371() {
      return INSTANCE.ad.method419();
   }

   public static boolean method1372() {
      return INSTANCE.ac.method419();
   }

   public static boolean method1373() {
      return INSTANCE.ae.method419();
   }

   public static BozeDrawColor method1374() {
      return INSTANCE.ae.method419() ? INSTANCE.af.method1362() : new BozeDrawColor(INSTANCE.field2411.method1347());
   }

   public static BozeDrawColor method1375() {
      return INSTANCE.ae.method419() ? INSTANCE.ag.method1362() : new BozeDrawColor(INSTANCE.field2408.method1347());
   }

   public static int method1376() {
      return INSTANCE.ai.method434();
   }

   public static boolean method1377() {
      return INSTANCE.aj.method419();
   }

   public static boolean method1378() {
      return INSTANCE.ak.method419();
   }

   public static int method1379() {
      return INSTANCE.am.method434();
   }

   public static boolean method1380() {
      return INSTANCE.an.method419();
   }

   public static BozeDrawColor method1381() {
      return INSTANCE.ao.method1362();
   }

   public static boolean method1382() {
      return INSTANCE.ap.method419();
   }

   public static BozeDrawColor method1383() {
      return INSTANCE.aq.method419() ? INSTANCE.ar.method1362() : new BozeDrawColor(INSTANCE.field2408.method1347());
   }

   public static float method1384() {
      return INSTANCE.as.method423();
   }

   public static float method1385() {
      return INSTANCE.at.method423();
   }

   public static int method1386() {
      return INSTANCE.au.method434();
   }

   public static boolean method1387() {
      return INSTANCE.av.method419();
   }

   public static double method1388() {
      return method1389((double)mc.getWindow().getScaledWidth());
   }

   public static double method1389(double width) {
      return width / ((double)method1363() * (BozeInstance.INSTANCE.getModules().isEmpty() ? 7.75 : 9.0)) * INSTANCE.aw.getValue();
   }

   public static double method1390() {
      return INSTANCE.ax.getValue();
   }

   public static double method1391() {
      return INSTANCE.ay.getValue();
   }

   public static String method1392(LetterStyle style, String text) {
      // $VF: Couldn't be decompiled
      // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
      // java.lang.NullPointerException: Cannot read field "classStruct" because "classNode" is null
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifyNewEnumSwitch(SwitchHelper.java:319)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplify(SwitchHelper.java:41)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:30)
      //   at org.jetbrains.java.decompiler.modules.decompiler.SwitchHelper.simplifySwitches(SwitchHelper.java:34)
      //   at org.jetbrains.java.decompiler.main.rels.MethodProcessor.codeToJava(MethodProcessor.java:376)
      //
      // Bytecode:
      // 00: aload 0
      // 01: invokevirtual dev/boze/client/enums/LetterStyle.ordinal ()I
      // 04: tableswitch 40 0 2 28 35 30
      // 20: aload 1
      // 21: areturn
      // 22: aload 1
      // 23: invokevirtual java/lang/String.toUpperCase ()Ljava/lang/String;
      // 26: areturn
      // 27: aload 1
      // 28: invokevirtual java/lang/String.toLowerCase ()Ljava/lang/String;
      // 2b: areturn
      // 2c: aload 1
      // 2d: areturn
   }

   private boolean lambda$new$2() {
      return this.as.method423() > 0.0F;
   }

   private boolean lambda$new$1() {
      return this.ac.method419() && this.field2434.method461() == ModuleDisplayMode.Icons;
   }

   private static boolean lambda$new$0() {
      return Gui.INSTANCE.field2350.method461() == GUILayout.Classic;
   }
}
