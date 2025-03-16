package dev.boze.client.gui.screens;

import dev.boze.api.BozeInstance;
import dev.boze.client.enums.AAMode;
import dev.boze.client.enums.GUILayout;
import dev.boze.client.enums.GUIMenu;
import dev.boze.client.enums.MaxHeight;
import dev.boze.client.enums.RenderMode;
import dev.boze.client.font.FontLoader;
import dev.boze.client.font.FontManager;
import dev.boze.client.font.IFontRender;
import dev.boze.client.gui.components.AddonCategoryComponent;
import dev.boze.client.gui.components.AddonComponent;
import dev.boze.client.gui.components.BaseComponent;
import dev.boze.client.gui.components.EditableComponent;
import dev.boze.client.gui.components.GhostModeComponent;
import dev.boze.client.gui.components.InputBaseComponent;
import dev.boze.client.gui.components.ModuleCategoryComponent;
import dev.boze.client.gui.components.ModuleComponent;
import dev.boze.client.gui.components.ModulesCategoryComponent;
import dev.boze.client.gui.components.ScaledBaseComponent;
import dev.boze.client.gui.components.scaled.bottomrow.AccountManagerComponent;
import dev.boze.client.render.Framebuffer;
import dev.boze.client.settings.Setting;
import dev.boze.client.settings.SettingBlock;
import dev.boze.client.shaders.ChamsShaderRenderer;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.HUDModule;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Accounts;
import dev.boze.client.systems.modules.client.Gui;
import dev.boze.client.systems.modules.client.HUD;
import dev.boze.client.systems.modules.client.Theme;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.http.HttpUtil;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import java.util.ArrayList;
import java.util.Locale;
import mapped.Class1201;
import dev.boze.client.Boze;
import mapped.Class2779;
import mapped.Class2782;
import mapped.Class3077;
import mapped.Class5928;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.text.Text;
import org.lwjgl.opengl.GL32;

public class ClickGUI extends Screen implements IMinecraft {
   public GUIMenu field1332 = GUIMenu.Normal;
   public RenderUtil field1333;
   private final Timer field1334 = new Timer();
   public static final ClickGUI field1335 = new ClickGUI();
   public boolean field1336 = false;
   public CursorType field1337 = CursorType.Normal;
   public Screen field1338 = null;
   private final ArrayList<BaseComponent> field1339 = new ArrayList();
   private final ArrayList<BaseComponent> field1340 = new ArrayList();
   private final ArrayList<HUDModule> field1341 = new ArrayList();
   private ScaledBaseComponent field1342 = null;
   private String field1343 = null;
   private double field1344 = 0.0;
   private double field1345 = 0.0;
   private EditableComponent field1346 = null;
   private GhostModeComponent field1347 = null;
   private Framebuffer field1348;
   private final String field1349 = HttpUtil.get("https://capes.boze.dev/specialcape").method2187();

   protected ClickGUI() {
      super(Text.translatable("ClickGUI"));
   }

   public ScaledBaseComponent method579() {
      return this.field1342;
   }

   public void method580(ScaledBaseComponent popup) {
      this.field1342 = popup;
   }

   public void method581(String description, double leftX, double rightX) {
      this.field1343 = description;
      this.field1344 = leftX;
      this.field1345 = rightX;
   }

   private ArrayList<BaseComponent> method2120() {
      return this.field1336 ? this.field1340 : this.field1339;
   }

   private ArrayList<BaseComponent> method405() {
      return new ArrayList();
   }

   public void render(DrawContext context, int mouseX, int mouseY, float delta) {
      this.field1337 = CursorType.Normal;
      if (this.field1332 == GUIMenu.AltManager && this.field1342 == null) {
         if (this.field1338 != null) {
            mc.setScreen(this.field1338);
         } else {
            mc.setScreen(new MultiplayerScreen(new TitleScreen()));
         }
      }

      if (mc.world == null) {
         this.renderBackground(context, mouseX, mouseY, delta);
      }

      if (Theme.method1356()) {
         FontLoader.field1062 = true;
      }

      if (this.field1336) {
         double var8 = HUD.INSTANCE.field2376.getValue() * (double)Math.min(mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
         RenderUtil.field3963.method2233();
         if (var8 > 0.0) {
            RenderUtil.field3963
               .method2245(
                  var8, var8, (double)mc.getWindow().getScaledWidth() - var8 * 2.0, (double)mc.getWindow().getScaledHeight() - var8 * 2.0, RGBAColor.field402
               );
         }

         RenderUtil.field3963
            .method2242(
               (double)mc.getWindow().getScaledWidth() / 2.0,
               var8,
               (double)mc.getWindow().getScaledWidth() / 2.0,
               (double)mc.getWindow().getScaledHeight() - var8,
               RGBAColor.field402
            );
         RenderUtil.field3963
            .method2242(
               var8,
               (double)mc.getWindow().getScaledHeight() / 2.0,
               (double)mc.getWindow().getScaledWidth() - var8,
               (double)mc.getWindow().getScaledHeight() / 2.0,
               RGBAColor.field402
            );
         RenderUtil.field3963.method2235(context);

         for (HUDModule var11 : this.field1341) {
            var11.method300((double)mouseX, (double)mouseY, (double)delta);
         }
      }

      boolean var28 = false;
      if (this.field1342 == null) {
         if (Theme.method1382()) {
            if (ChamsShaderRenderer.field2246 == null) {
               ChamsShaderRenderer.method1305();
            }

            if (!ShaderRegistry.field2273) {
               ShaderRegistry.method1315();
            }

            if (this.field1333 == null) {
               this.field1333 = new RenderUtil(RenderMode.COLOR);
            }

            this.field1333.method2233();
         }

         if (Gui.INSTANCE.field2362.method461() != AAMode.Off && !FontManager.method1106()) {
            var28 = true;
            GL32.glEnable(32925);
            if (this.field1348 == null) {
               this.field1348 = new Framebuffer();
            }

            if (this.field1348.field2150 != Gui.INSTANCE.field2362.method461().samples) {
               this.field1348.resize(this.field1348.textureWidth, this.field1348.textureHeight, MinecraftClient.IS_SYSTEM_MAC);
            }

            this.field1348.method1156(false, true);
            GL32.glBindFramebuffer(36008, mc.getFramebuffer().fbo);
            GL32.glBindFramebuffer(36009, this.field1348.fbo);
            GL32.glBlitFramebuffer(
               0,
               0,
               mc.getFramebuffer().textureWidth,
               mc.getFramebuffer().textureHeight,
               0,
               0,
               this.field1348.textureWidth,
               this.field1348.textureHeight,
               16384,
               9728
            );
         }

         if (this.field1349 != null && this.field1349.equals("easteregg")) {
            for (BaseComponent var32 : this.method405()) {
               var32.render(context, mouseX, mouseY, delta);
            }
         } else {
            for (BaseComponent var31 : this.method2120()) {
               var31.render(context, mouseX, mouseY, delta);
            }
         }

         if (var28) {
            GL32.glBindFramebuffer(36008, this.field1348.fbo);
            GL32.glBindFramebuffer(36009, mc.getFramebuffer().fbo);
            GL32.glBlitFramebuffer(
               0,
               0,
               this.field1348.textureWidth,
               this.field1348.textureHeight,
               0,
               0,
               mc.getFramebuffer().textureWidth,
               mc.getFramebuffer().textureHeight,
               16384,
               9728
            );
            mc.getFramebuffer().beginWrite(false);
         }

         if (this.field1347 != null && !this.field1336) {
            this.field1347.render(context, mouseX, mouseY, delta);
         }

         if (Theme.method1382()) {
            ChamsShaderRenderer.method1312(this::lambda$render$0, true, Theme.method1383(), Theme.method1386(), Theme.method1384(), Theme.method1385());
         }

         if (Gui.INSTANCE.field2358.method419() && this.field1346 != null) {
            this.field1346.render(context, mouseX, mouseY, delta);
         }

         if (Gui.INSTANCE.field2354.method419() && this.field1343 != null && this.field1342 == null) {
            RenderUtil.field3963.method2233();
            double var30 = this.field1345;
            double var33 = (double)mouseY;
            boolean var13 = this.field1343.startsWith("=");
            if (var13 && this.field1343.length() > 1) {
               this.field1343 = this.field1343.substring(1);
            }

            String[] var14 = this.field1343.split("\n");
            double var15 = 0.0;
            double var17 = 0.0;
            double var19 = 0.6;

            for (String var24 : var14) {
               byte var25 = 0;
               if (var24.startsWith(";")) {
                  var19 = 0.5;
                  var25 = 1;
               } else if (var24.startsWith(" - ")) {
                  var19 = 0.5;
               }

               IFontRender.method499().startBuilding(BaseComponent.scaleFactor * var19, true);
               var15 += IFontRender.method499().method1390() + 2.0 * BaseComponent.scaleFactor;
               double var26 = IFontRender.method499().method501(var24.substring(var25));
               IFontRender.method499().endBuilding(context);
               if (var26 > var17) {
                  var17 = var26;
               }

               var19 = var13 ? 0.6 : 0.55;
            }

            if (var30 + var17 + 4.0 * BaseComponent.scaleFactor > (double)mc.getWindow().getScaledWidth()) {
               var30 = this.field1344 - var17 - 4.0 * BaseComponent.scaleFactor;
            }

            if (var33 + var15 + 2.0 * BaseComponent.scaleFactor + 2.0 * BaseComponent.scaleFactor > (double)mc.getWindow().getScaledHeight()) {
               var33 = (double)mc.getWindow().getScaledHeight() - (var15 + 2.0 * BaseComponent.scaleFactor) - 2.0 * BaseComponent.scaleFactor;
            }

            RenderUtil.field3963
               .method2252(
                  var30,
                  var33 - 4.0 * BaseComponent.scaleFactor,
                  var17 + 4.0 * BaseComponent.scaleFactor,
                  var15 + 2.0 * BaseComponent.scaleFactor,
                  Gui.INSTANCE.field2356.method1347()
               );
            RenderUtil.field3963.method2235(context);
            var19 = BaseComponent.scaleFactor * 0.6;

            for (String var38 : var14) {
               byte var39 = 0;
               if (var38.startsWith(";")) {
                  var19 = BaseComponent.scaleFactor * 0.5;
                  var39 = 1;
               } else if (var38.startsWith(" - ")) {
                  var19 = BaseComponent.scaleFactor * 0.5;
               }

               IFontRender.method499().startBuilding(var19);
               IFontRender.method499()
                  .drawShadowedText(
                     var38.substring(var39), var30 + 2.0 * BaseComponent.scaleFactor, var33 - 2.0 * BaseComponent.scaleFactor, Theme.method1350()
                  );
               var33 += IFontRender.method499().method1390() + 2.0 * BaseComponent.scaleFactor;
               IFontRender.method499().endBuilding(context);
               var19 = BaseComponent.scaleFactor * 0.55;
            }
         }
      } else {
         if (Theme.method1382()) {
            if (ChamsShaderRenderer.field2246 == null) {
               ChamsShaderRenderer.method1305();
            }

            if (!ShaderRegistry.field2273) {
               ShaderRegistry.method1315();
            }

            if (this.field1333 == null) {
               this.field1333 = new RenderUtil(RenderMode.COLOR);
            }

            this.field1333.method2233();
         }

         this.field1342.render(context, mouseX, mouseY, delta);
         if (Theme.method1382()) {
            ChamsShaderRenderer.method1312(this::lambda$render$1, true, Theme.method1383(), Theme.method1386(), Theme.method1384(), Theme.method1385());
         }
      }

      this.field1343 = null;
      FontLoader.field1062 = false;
      Class5928.method112(this.field1337);
   }

   public boolean method582(BaseComponent element) {
      if (element instanceof ModuleComponent var5) {
         if (!var5.method2116()) {
            return true;
         } else if (this.field1346 == null || !Gui.INSTANCE.field2358.method419()) {
            return false;
         } else if (this.field1346.field387.isEmpty()) {
            return false;
         } else {
            Module var7 = var5.field334;
            return !var7.getName().toLowerCase(Locale.ROOT).contains(this.field1346.field387.toLowerCase(Locale.ROOT))
               && !var7.internalName.toLowerCase(Locale.ROOT).contains(this.field1346.field387.toLowerCase(Locale.ROOT));
         }
      } else if (!(element instanceof AddonComponent var6)) {
         return false;
      } else if (this.field1346 == null || !Gui.INSTANCE.field2358.method419()) {
         return false;
      } else {
         return this.field1346.field387.isEmpty()
            ? false
            : !var6.field362.getTitle().toLowerCase(Locale.ROOT).contains(this.field1346.field387.toLowerCase(Locale.ROOT))
               && !var6.field362.getName().toLowerCase(Locale.ROOT).contains(this.field1346.field387.toLowerCase(Locale.ROOT));
      }
   }

   public boolean method2114() {
      return this.field1346 != null && Gui.INSTANCE.field2358.method419() && !this.field1346.field387.isEmpty();
   }

   public void init() {
      double var4 = BaseComponent.scaleFactor;
      BaseComponent.scaleFactor = Theme.method1388();
      InputBaseComponent.field1131 = Theme.method1388();
      if (var4 != -1.0) {
         double var6 = BaseComponent.scaleFactor / var4;

         for (Category var11 : Category.values()) {
            if (var11 != Category.Hud && var11 != Category.Graph && var11.field42 >= 0.0 && var11.field43 >= 0.0) {
               var11.field42 *= var6;
               var11.field43 *= var6;
            }
         }

         if (Class2779.field86 >= 0.0 && Class2779.field87 >= 0.0) {
            Class2779.field86 *= var6;
            Class2779.field87 *= var6;
         }

         if (Class2782.field91 >= 0.0 && Class2782.field92 >= 0.0) {
            Class2782.field91 *= var6;
            Class2782.field92 *= var6;
         }

         if (!Class2782.field94) {
            Class2782.field95 *= var6;
            Class2782.field96 *= var6;
         }
      }

      this.field1339.clear();
      this.field1340.clear();
      if (!ShaderRegistry.field2273) {
         ShaderRegistry.method1315();
      }

      if (!RenderUtil.field3968) {
         RenderUtil.method2232();
      }

      this.field1342 = null;
      if (this.field1332 == GUIMenu.Normal) {
         if (Gui.INSTANCE.field2350.method461() != GUILayout.Classic) {
            double var19 = (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor;
            this.field1339
               .add(
                  new ModulesCategoryComponent(
                     null,
                     var19,
                     (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor,
                     (double)Theme.method1363() * BaseComponent.scaleFactor,
                     Gui.INSTANCE.field2357.method419() ? Class2782.field93 : 0.0
                  )
               );
            if (!BozeInstance.INSTANCE.getModules().isEmpty()) {
               var19 += (double)Theme.method1363() * 1.25 * BaseComponent.scaleFactor;
               double var24 = var19;
               double var28 = (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor;
               if (Class2779.field86 >= 0.0 && Class2779.field87 >= 0.0) {
                  var24 = Class2779.field86;
                  var28 = Class2779.field87;
               }

               this.field1339
                  .add(
                     new AddonCategoryComponent(
                        null,
                        var19,
                        (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor,
                        var24,
                        var28,
                        (double)Theme.method1363() * BaseComponent.scaleFactor,
                        Gui.INSTANCE.field2357.method419() ? Class2779.field89 : 0.0
                     )
                  );
            }
         } else {
            double var18 = (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor;
            double var23 = (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor;

            for (Category var13 : Category.values()) {
               if (var13 != Category.Hud && var13 != Category.Graph) {
                  double var14 = var18;
                  double var16 = var23;
                  if (Gui.INSTANCE.field2366.method461() == MaxHeight.Relative) {
                     var16 = (
                           (double)mc.getWindow().getScaledHeight() * (1.0 - Gui.INSTANCE.field2368.getValue())
                              - (double)Theme.method1357() * BaseComponent.scaleFactor
                        )
                        * 0.5;
                  }

                  if (var13.field42 >= 0.0
                     && var13.field43 >= 0.0
                     && var13.field42 + BaseComponent.scaleFactor < (double)mc.getWindow().getScaledWidth()
                     && var13.field43 + BaseComponent.scaleFactor < (double)mc.getWindow().getScaledHeight()) {
                     var14 = var13.field42;
                     var16 = var13.field43;
                  }

                  this.field1339
                     .add(
                        new ModuleCategoryComponent(
                           var13,
                           null,
                           var18,
                           var23,
                           var14,
                           var16,
                           (double)Theme.method1363() * BaseComponent.scaleFactor,
                           Gui.INSTANCE.field2357.method419() ? var13.scrollOffset : 0.0
                        )
                     );
                  var18 += (double)Theme.method1363() * (1.0 + Theme.method1364()) * BaseComponent.scaleFactor;
               }
            }

            if (!BozeInstance.INSTANCE.getModules().isEmpty()) {
               double var27 = var18;
               double var31 = (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor;
               if (Class2779.field86 >= 0.0 && Class2779.field87 >= 0.0) {
                  var27 = Class2779.field86;
                  var31 = Class2779.field87;
               }

               this.field1339
                  .add(
                     new AddonCategoryComponent(
                        null,
                        var18,
                        (double)Theme.method1363() * Theme.method1364() * BaseComponent.scaleFactor,
                        var27,
                        var31,
                        (double)Theme.method1363() * BaseComponent.scaleFactor,
                        Gui.INSTANCE.field2357.method419() ? Class2779.field89 : 0.0
                     )
                  );
            }
         }

         this.field1340
            .add(
               new ModuleCategoryComponent(
                  Category.Hud,
                  null,
                  (double)Theme.method1363() / 4.0 * BaseComponent.scaleFactor,
                  (double)Theme.method1363() / 4.0 * BaseComponent.scaleFactor,
                  (double)Theme.method1363() * BaseComponent.scaleFactor,
                  Gui.INSTANCE.field2357.method419() ? Category.Hud.scrollOffset : 0.0
               )
            );
         this.field1340
            .add(
               new ModuleCategoryComponent(
                  Category.Graph,
                  null,
                  (double)Theme.method1363() / 4.0 * BaseComponent.scaleFactor * 2.0 + (double)Theme.method1363() * BaseComponent.scaleFactor,
                  (double)Theme.method1363() / 4.0 * BaseComponent.scaleFactor,
                  (double)Theme.method1363() * BaseComponent.scaleFactor,
                  Gui.INSTANCE.field2357.method419() ? Category.Graph.scrollOffset : 0.0
               )
            );

         for (Module var7 : Boze.getModules().modules) {
            if (var7 instanceof HUDModule) {
               this.field1341.add((HUDModule)var7);
            }
         }

         double var22 = (double)mc.getWindow().getScaledWidth() * 0.2 * BaseComponent.scaleFactor;
         double var25 = 20.0 * BaseComponent.scaleFactor;
         if (!Class2782.field94 && !(Class2782.field95 < 0.0) && !(Class2782.field96 < 0.0)) {
            this.field1346 = new EditableComponent("Search", null, Class2782.field95, Class2782.field96, var22, var25);
         } else {
            this.field1346 = new EditableComponent(
               "Search",
               null,
               (double)mc.getWindow().getScaledWidth() * 0.5 - var22 * 0.5,
               (double)mc.getWindow().getScaledHeight() - var25 * 0.5 - BaseComponent.scaleFactor * 24.0,
               var22,
               var25
            );
         }

         IFontRender.method499().startBuilding(BaseComponent.scaleFactor * 0.5, true);
         double var29 = 12.0 * BaseComponent.scaleFactor + IFontRender.method499().method1390();
         double var32 = 18.0 * BaseComponent.scaleFactor + IFontRender.method499().method501("Ghost Mode") + var29 * 1.2;
         this.field1347 = new GhostModeComponent(
            (double)mc.getWindow().getScaledWidth() - 6.0 * BaseComponent.scaleFactor - var32,
            (double)mc.getWindow().getScaledHeight() - 6.0 * BaseComponent.scaleFactor - var29,
            var32,
            var29
         );
         IFontRender.method499().endBuilding();
      } else if (this.field1332 == GUIMenu.AltManager) {
         this.method580(new AccountManagerComponent(Accounts.INSTANCE.field2313));
      }
   }

   public void method1964(int width, int height) {
      if (this.field1348 != null) {
         this.field1348.resize(width, height, false);
      }
   }

   public boolean shouldPause() {
      return false;
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      if (this.field1342 != null) {
         if (this.field1342.isMouseOver(mouseX, mouseY, button)) {
            return true;
         }

         this.field1342.method2142();
         this.field1342 = null;
      }

      if (this.field1336) {
         for (HUDModule var10 : this.field1341) {
            if (var10.method324(mouseX, mouseY, button)) {
               return true;
            }
         }
      }

      for (int var13 = this.method2120().size() - 1; var13 >= 0; var13--) {
         BaseComponent var14 = (BaseComponent)this.method2120().get(var13);
         if (var14.mouseClicked(mouseX, mouseY, button)) {
            if (!this.field1336) {
               try {
                  this.method2120().remove(var13);
                  this.method2120().add(var14);
               } catch (IndexOutOfBoundsException var12) {
               }
            }

            return true;
         }
      }

      if (Gui.INSTANCE.field2358.method419() && this.field1346 != null && this.field1346.mouseClicked(mouseX, mouseY, button)) {
         return true;
      } else {
         return this.field1347 != null && !this.field1336 && this.field1347.mouseClicked(mouseX, mouseY, button)
            ? true
            : super.mouseClicked(mouseX, mouseY, button);
      }
   }

   public boolean mouseReleased(double mouseX, double mouseY, int button) {
      if (this.field1342 != null) {
         this.field1342.mouseClicked(mouseX, mouseY, button);
      }

      if (this.field1336) {
         for (HUDModule var10 : this.field1341) {
            var10.method325(mouseX, mouseY, button);
         }
      }

      for (BaseComponent var12 : this.method2120()) {
         var12.mouseClicked(mouseX, mouseY, button);
      }

      return super.mouseReleased(mouseX, mouseY, button);
   }

   public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      if (this.field1342 != null && this.field1342.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
         return true;
      } else {
         for (BaseComponent var14 : this.method2120()) {
            if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
               return true;
            }
         }

         return Gui.INSTANCE.field2358.method419() && this.field1346 != null && this.field1346.onDrag(mouseX, mouseY, button, deltaX, deltaY)
            ? true
            : super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
      }
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double amount) {
      if (this.field1342 != null && this.field1342.onMouseScroll(mouseX, mouseY, amount)) {
         return true;
      } else {
         for (BaseComponent var13 : this.method2120()) {
            if (var13.onMouseScroll(mouseX, mouseY, amount)) {
               return true;
            }
         }

         return true;
      }
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256 && this.shouldCloseOnEsc() && this.field1342 != null) {
         this.field1342.method2142();
         this.field1342 = null;
         return true;
      } else if (this.field1342 != null && this.field1342.keyPressed(keyCode, scanCode, modifiers)) {
         return true;
      } else {
         for (BaseComponent var8 : this.method2120()) {
            if (var8.keyPressed(keyCode, scanCode, modifiers)) {
               return true;
            }
         }

         if (Gui.INSTANCE.field2358.method419() && this.field1346 != null) {
            if (this.field1346.keyPressed(keyCode, scanCode, modifiers)) {
               return true;
            }

            this.field1346.field389 = false;
         }

         return super.keyPressed(keyCode, scanCode, modifiers);
      }
   }

   public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
      if (this.field1342 != null) {
         this.field1342.mouseClicked((double)keyCode, (double)scanCode, modifiers);
      }

      for (BaseComponent var8 : this.method2120()) {
         var8.keyReleased(keyCode, scanCode, modifiers);
      }

      return super.keyReleased(keyCode, scanCode, modifiers);
   }

   public void method583(char c) {
      if (this.field1342 != null) {
         this.field1342.method583(c);
      }
   }

   public void close() {
      Class5928.method112(CursorType.Normal);

      for (Module var5 : Boze.getModules().modules) {
         for (Setting var7 : var5.method1144()) {
            if (var7 instanceof SettingBlock var8) {
               var8.method421(false);
            }
         }
      }

      if (this.field1334.hasElapsed(1000.0)) {
         Class1201.method2383(true);
         this.field1334.reset();
      }

      if (this.field1346 != null) {
         this.field1346.field389 = true;
      }

      super.close();
   }

   public boolean shouldCloseOnEsc() {
      return !Class3077.field174;
   }

   private void lambda$render$1(DrawContext var1) {
      this.field1333.method2235(var1);
   }

   private void lambda$render$0(DrawContext var1) {
      this.field1333.method2235(var1);
   }
}
