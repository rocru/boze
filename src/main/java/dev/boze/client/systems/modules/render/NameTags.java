package dev.boze.client.systems.modules.render;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.FontShadowMode;
import dev.boze.client.enums.NameTagsArmor;
import dev.boze.client.events.Render2DEvent;
import dev.boze.client.font.IFontRender;
import dev.boze.client.mixin.WorldRendererAccessor;
import dev.boze.client.settings.*;
import dev.boze.client.settings.generic.ScalingSetting;
import dev.boze.client.systems.modules.Category;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.Capes;
import dev.boze.client.systems.modules.client.Fonts;
import dev.boze.client.systems.modules.client.Friends;
import dev.boze.client.systems.modules.client.Media;
import dev.boze.client.systems.modules.misc.BetterTab;
import dev.boze.client.systems.modules.render.logoutspots.LogoutPlayerEntity;
import dev.boze.client.systems.waypoints.WayPoint;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.TargetTracker;
import dev.boze.client.utils.entity.fakeplayer.FakePlayerEntity;
import dev.boze.client.utils.math.NumberUtils;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class3060;
import mapped.Class3071;
import mapped.Class5922;
import mapped.Class5926;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.StringHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class NameTags extends Module {
   public static final NameTags INSTANCE = new NameTags();
   private final ScalingSetting field781 = new ScalingSetting();
   private final BooleanSetting field782 = new BooleanSetting("Players", true, "Render player nametags");
   private final BooleanSetting field783 = new BooleanSetting("Gamemode", false, "Show player gamemode", this.field782);
   private final BooleanSetting field784 = new BooleanSetting("Ping", false, "Show player ping", this.field782);
   private final BooleanSetting field785 = new BooleanSetting("Health", true, "Show player health", this.field782);
   private final BooleanSetting field786 = new BooleanSetting("Pops", false, "Show player pops", this.field782);
   private final BooleanSetting field787 = new BooleanSetting("Enchants", true, "Show armor enchants", this.field782);
   private final BooleanSetting field788 = new BooleanSetting("Mainhand", true, "Show player mainhand item", this.field782);
   private final EnumSetting<NameTagsArmor> field789 = new EnumSetting<NameTagsArmor>(
      "Armor",
      NameTagsArmor.Normal,
      "Show player armor\n - Off: Don't show armor\n - Normal: Show armor normally\n - Reverse: Show armor in reverse order\n",
      this.field782
   );
   private final BooleanSetting field790 = new BooleanSetting("Items", false, "Render item nametags");
   private final BooleanSetting field791 = new BooleanSetting("Count", false, "Show stack size", this.field790);
   private final ColorSetting field792 = new ColorSetting("Color", new BozeDrawColor(-1), "Font color", this.field790);
   private final MinMaxSetting field793 = new MinMaxSetting("ItemFontSize", 1.5, 0.5, 5.0, 0.05, "Font size", this.field790);
   private final SettingCategory field794 = new SettingCategory("Style", "Nametag style");
   private final ColorSetting field795 = new ColorSetting("Fill", new BozeDrawColor(Integer.MIN_VALUE), "Nametag fill color", this.field794);
   private final ColorSetting field796 = new ColorSetting("Outline", new BozeDrawColor(-805306368), "Nametag outline color", this.field794);
   private final BooleanSetting field797 = new BooleanSetting("MatchName", false, "Match outline color to name color for player nametags", this.field794);
   private final IntSetting field798 = new IntSetting("Radius", 1, 1, 10, 1, "Outline radius", this.field794);
   private final IntSetting field799 = new IntSetting("Margin", 2, 0, 10, 1, "Nametag margin", this.field794);
   private final IntSetting field800 = new IntSetting("Gap", 5, 0, 25, 1, "Gap between armor and nameplate", this::lambda$new$0, this.field794);
   private final IntSetting field801 = new IntSetting("ArmorSpacing", 35, 10, 100, 1, "Spacing between armors", this::lambda$new$1, this.field794);
   private final MinMaxSetting field802 = new MinMaxSetting("FontSize", 1.6, 0.5, 5.0, 0.05, "Font size", this.field794);
   private final FloatSetting field803 = new FloatSetting("EnchantFontSize", 0.6F, 0.1F, 1.0F, 0.05F, "Enchant font size", this.field794);
   private final BooleanSetting field804 = new BooleanSetting("ShowBurrow", false, "Different colors for when player is burrowed");
   private final ColorSetting field805 = new ColorSetting("Fill", new BozeDrawColor(-2144337920), "Nametag burrow fill color", this.field804);
   private final ColorSetting field806 = new ColorSetting("Outline", new BozeDrawColor(-802160640), "Nametag burrow outline color", this.field804);
   private final IntSetting field807 = new IntSetting("HRange", 256, 10, 512, 1, "Horizontal range");
   private final IntSetting field808 = new IntSetting("VRange", 384, 10, 384, 1, "Vertical range");
   private ArrayList<String> field809 = new ArrayList(Arrays.asList("soon_1", "soon_2", "soon_3", "soon_4", "soon_5", "help_desk_npc"));
   private static final MatrixStack aa = new MatrixStack();

   public NameTags() {
      super(
         "Nametags",
         "Draws custom nametags above players\nWarning: Armor breaks with vanilla text\nModule will be rewritten before 2.0 release\n",
         Category.Render
      );
   }

   @EventHandler
   public void method2040(Render2DEvent event) {
      if (this.field782.method419() || this.field790.method419()) {
         Vector3d var5 = new Vector3d(mc.gameRenderer.getCamera().getPos().toVector3f());
         if (this.field782.method419()) {
            for (PlayerEntity var7 : mc.world.getPlayers()) {
               if ((var7 != mc.player || FreeCam.INSTANCE.isEnabled())
                  && !(var7 instanceof FakePlayerEntity)
                  && !this.field809.contains(var7.getNameForScoreboard())) {
                  Frustum var8 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
                  if (var8 == null || var8.isVisible(var7.getBoundingBox())) {
                     Vec3d var9 = Class3071.method6019(var7);
                     var9 = var9.add(0.0, (double)var7.getHeight() + 0.2, 0.0);
                     Vector3d var10 = new Vector3d(var9.x, var9.y, var9.z);
                     double var11 = Math.sqrt(Math.pow(var10.x - var5.x, 2.0) + Math.pow(var10.z - var5.z, 2.0));
                     double var13 = Math.abs(var10.y - var5.y);
                     if (!(var11 > (double)this.field807.method434().intValue()) && !(var13 > (double)this.field808.method434().intValue())) {
                        boolean var15 = method377(var10);
                        if (var15) {
                           this.method373(event.field1947, var7, var10);
                        }
                     }
                  }
               }
            }
         }

         if (this.field790.method419()) {
            for (Entity var18 : mc.world.getEntities()) {
               if (var18 instanceof ItemEntity) {
                  ItemEntity var19 = (ItemEntity)var18;
                  Frustum var21 = ((WorldRendererAccessor)mc.worldRenderer).getFrustum();
                  if (var21 == null || var21.isVisible(var19.getBoundingBox())) {
                     Vec3d var22 = Class3071.method6019(var19);
                     var22 = var22.add(0.0, (double)var19.getHeight() + 0.2, 0.0);
                     Vector3d var24 = new Vector3d(var22.x, var22.y, var22.z);
                     double var12 = Math.sqrt(Math.pow(var24.x - var5.x, 2.0) + Math.pow(var24.z - var5.z, 2.0));
                     double var14 = Math.abs(var24.y - var5.y);
                     if (!(var12 > (double)this.field807.method434().intValue()) && !(var14 > (double)this.field808.method434().intValue())) {
                        boolean var16 = method377(var24);
                        if (var16) {
                           this.method372(var19, var24);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private BozeDrawColor method370(PlayerEntity var1) {
      if (this.field804.method419()) {
         BlockPos var5 = BlockPos.ofFloored(var1.getPos());
         if (mc.world.getBlockState(var5).isSolidBlock(mc.world, var5) || mc.world.getBlockState(var5).getBlock() == Blocks.ENDER_CHEST) {
            return this.field805.method1362();
         }
      }

      return this.field795.method1362();
   }

   private BozeDrawColor method371(PlayerEntity var1) {
      if (this.field804.method419()) {
         BlockPos var5 = BlockPos.ofFloored(var1.getPos());
         if (mc.world.getBlockState(var5).isSolidBlock(mc.world, var5) || mc.world.getBlockState(var5).getBlock() == Blocks.ENDER_CHEST) {
            return this.field806.method1362();
         }
      }

      return this.field796.method1362();
   }

   private void method372(ItemEntity var1, Vector3d var2) {
      Class5922.method61(var2);
      IFontRender.method499().startBuilding(this.field793.getValue());
      String var6 = var1.getStack().getItem().getName().getString();
      if (this.field791.method419() && var1.getStack().getCount() > 1) {
         var6 = var6 + " x" + var1.getStack().getCount();
      }

      IFontRender.method499()
         .drawShadowedText(
            var6,
            -IFontRender.method499().method501(var6) * 0.5,
            -IFontRender.method499().method1390() * 0.5,
            this.field792.method1362(),
            Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off
         );
      IFontRender.method499().endBuilding();
      Class5922.method2142();
   }

   private void method373(DrawContext var1, PlayerEntity var2, Vector3d var3) {
      double var7 = 0.0;
      Class5922.method62(var3, var1);
      Class3060[] var9 = new Class3060[5];
      IFontRender.method499().startBuilding(this.field802.getValue());
      if (this.field783.method419()) {
         var9[0] = new Class3060("[" + (var2.isCreative() ? "C" : "S") + "]", -1);
      }

      if (this.field784.method419()) {
         int var10 = Class5926.method100(var2);
         if (var10 > 0) {
            var9[1] = new Class3060(Integer.toString(var10), var10 > 150 ? -47032 : -1);
         }
      }

      int var26 = Friends.method2055(var2) ? -8323073 : (TargetTracker.method2055(var2) ? -1894890 : -1);
      if (var26 == -1 && Capes.INSTANCE.isEnabled() && var2.getGameProfile() != null && Capes.field1290.containsKey(var2.getGameProfile().getId().toString())) {
         GameProfile var11 = var2.getGameProfile();
         if (((String)Capes.field1290.get(var11.getId().toString())).equals("default")) {
            var26 = BetterTab.INSTANCE.field2918.method1347().method2010();
         } else if (((String)Capes.field1290.get(var11.getId().toString())).equals("beta")) {
            var26 = RGBAColor.field407.method2010();
         }
      }

      var9[2] = new Class3060(Media.method1341(var2.getNameForScoreboard()), var26);
      if (this.field785.method419()) {
         float var27 = NumberUtils.method2197(var2.getHealth() + var2.getAbsorptionAmount(), 1);
         if (var2.getNameForScoreboard().equalsIgnoreCase("antiflame") || var2.getNameForScoreboard().equalsIgnoreCase("0851_")) {
            var27 += 0.69F;
         }

         String var12 = Float.toString(var27).replace(".0", "");
         if (var27 < 5.0F) {
            var9[3] = new Class3060(var12, -47032);
         } else if (var27 < 20.0F) {
            var9[3] = new Class3060(var12, -12728);
         } else {
            var9[3] = new Class3060(var12, -14169088);
         }
      }

      if (this.field786.method419() && TargetTracker.field1359.containsKey(var2.getNameForScoreboard())) {
         var9[4] = new Class3060("-" + TargetTracker.field1359.get(var2.getNameForScoreboard()), -1);
      }

      String var28 = "";
      boolean var29 = true;

      for (Class3060 var16 : var9) {
         if (var16 != null) {
            if (!var29) {
               var28 = var28 + " ";
            }

            var29 = false;
            var28 = var28 + var16.field145;
         }
      }

      double var31 = IFontRender.method499().method501(var28) / 2.0 + 2.0;
      RenderUtil.field3965.method2233();
      if (this.field795.method1362().field411 > 0) {
         BozeDrawColor var33 = this.method370(var2);
         RenderUtil.field3965
            .method2253(
               -(var31 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               ((double)this.field799.method434().intValue() + var31) * 2.0,
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               var33
            );
      }

      if (this.field796.method1362().field411 > 0) {
         double var34 = -(var31 + (double)this.field799.method434().intValue());
         double var17 = -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2));
         double var19 = var34 + ((double)this.field799.method434().intValue() + var31) * 2.0;
         double var21 = var17 + IFontRender.method499().method1390() + (double)(this.field799.method434() * 2);
         BozeDrawColor var23 = this.method371(var2);
         if (this.field797.method419()) {
            var23 = new BozeDrawColor(var9[2].field146.method2010());
         }

         RenderUtil.field3965
            .method2251(
               var34 - (double)this.field798.method434().intValue(),
               var17 - (double)this.field798.method434().intValue(),
               var19 + (double)this.field798.method434().intValue(),
               var17,
               var23,
               var23,
               var23,
               var23
            );
         RenderUtil.field3965.method2251(var34 - (double)this.field798.method434().intValue(), var17, var34, var21, var23, var23, var23, var23);
         RenderUtil.field3965.method2251(var19, var17, var19 + (double)this.field798.method434().intValue(), var21, var23, var23, var23, var23);
         RenderUtil.field3965
            .method2251(
               var34 - (double)this.field798.method434().intValue(),
               var21,
               var19 + (double)this.field798.method434().intValue(),
               var21 + (double)this.field798.method434().intValue(),
               var23,
               var23,
               var23,
               var23
            );
      }

      RenderUtil.field3965.method2235(null);
      var31 = -(var31 - 3.0);
      var29 = true;

      for (Class3060 var18 : var9) {
         if (var18 != null) {
            if (!var29) {
               var31 += IFontRender.method499().method501(" ");
            }

            var29 = false;
            IFontRender.method499()
               .drawShadowedText(
                  var18.field145,
                  var31,
                  -(IFontRender.method499().method1390() + (double)this.field799.method434().intValue()),
                  var18.field146,
                  Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off
               );
            var31 += IFontRender.method499().method501(var18.field145);
         }
      }

      var7 += IFontRender.method499().method1390() + (double)(this.field799.method434() * 2);
      IFontRender.method499().endBuilding();
      if (this.field787.method419() || this.field789.method461() != NameTagsArmor.Off) {
         var7 += (double)this.field800.method434().intValue();
         double var36 = 0.0;

         for (ItemStack var41 : var2.getInventory().armor) {
            if (var41 != null && !var41.isEmpty()) {
               var36 -= (double)this.field801.method434().intValue() / 2.0;
            }
         }

         if (var2.getMainHandStack() != null && !var2.getMainHandStack().isEmpty()) {
            var36 -= (double)this.field801.method434().intValue() / 2.0;
         }

         if (var2.getOffHandStack() != null && !var2.getOffHandStack().isEmpty()) {
            var36 -= (double)this.field801.method434().intValue() / 2.0;
         }

         double var40 = 0.0;
         if (var2.getMainHandStack() != null && !var2.getMainHandStack().isEmpty()) {
            var40 = Math.max(var40, this.method376(var1, var2.getMainHandStack(), var36, -var7));
            var36 += (double)this.field801.method434().intValue();
         }

         if (this.field789.method461() == NameTagsArmor.Normal) {
            for (int var43 = var2.getInventory().armor.size() - 1; var43 >= 0; var43--) {
               ItemStack var45 = (ItemStack)var2.getInventory().armor.get(var43);
               if (var45 != null && !var45.isEmpty()) {
                  var40 = Math.max(var40, this.method376(var1, var45, var36, -var7));
                  var36 += (double)this.field801.method434().intValue();
               }
            }
         } else {
            for (int var42 = 0; var42 < mc.player.getInventory().armor.size(); var42++) {
               ItemStack var20 = (ItemStack)mc.player.getInventory().armor.get(var42);
               if (var20 != null && !var20.isEmpty()) {
                  var40 = Math.max(var40, this.method376(var1, var20, var36, -var7));
                  var36 += (double)this.field801.method434().intValue();
               }
            }
         }

         if (var2.getOffHandStack() != null && !var2.getOffHandStack().isEmpty()) {
            var40 = Math.max(var40, this.method376(var1, var2.getOffHandStack(), var36, -var7));
         }

         if (this.field788.method419() && var2.getMainHandStack() != null && !var2.getMainHandStack().isEmpty()) {
            IFontRender.method499().startBuilding((double)this.field803.method423().floatValue());
            MatrixStack var44 = var1.getMatrices();
            var44.push();
            IFontRender.method499()
               .drawShadowedText(
                  var2.getMainHandStack().getName().getString(),
                  -IFontRender.method499().method501(var2.getMainHandStack().getName().getString()) * 0.5,
                  (double)((float)(-(var40 + var7))),
                  RGBAColor.field402,
                  Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off
               );
            IFontRender.method499().endBuilding();
            var44.pop();
         }
      }

      Class5922.method332(var1);
   }

   public void method374(LogoutPlayerEntity logout, Vector3d iPos, boolean showHealth, RGBAColor normalColor, RGBAColor friendColor) {
      Class5922.method61(iPos);
      Class3060[] var9 = new Class3060[5];
      IFontRender.method499().startBuilding(this.field802.getValue());
      var9[2] = new Class3060(Media.method1341(logout.field3590), Friends.method346(logout.field3590) ? friendColor.method2010() : normalColor.method2010());
      if (showHealth) {
         float var10 = NumberUtils.method2197(logout.field3591, 1);
         String var11 = Float.toString(var10).replace(".0", "");
         if (var10 < 5.0F) {
            var9[3] = new Class3060(var11, -47032);
         } else if (var10 < 20.0F) {
            var9[3] = new Class3060(var11, -12728);
         } else {
            var9[3] = new Class3060(var11, -14169088);
         }
      }

      String var18 = "";
      boolean var19 = true;

      for (Class3060 var15 : var9) {
         if (var15 != null) {
            if (!var19) {
               var18 = var18 + " ";
            }

            var19 = false;
            var18 = var18 + var15.field145;
         }
      }

      double var21 = IFontRender.method499().method501(var18) / 2.0 + 2.0;
      RenderUtil.field3965.method2233();
      if (this.field795.method1362().field411 > 0) {
         RenderUtil.field3965
            .method2253(
               -(var21 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               ((double)this.field799.method434().intValue() + var21) * 2.0,
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               this.field795.method1362()
            );
      }

      if (this.field796.method1362().field411 > 0) {
         BozeDrawColor var23 = this.field796.method1362();
         if (this.field797.method419()) {
            var23 = new BozeDrawColor(var9[2].field146.method2010());
         }

         RenderUtil.field3965
            .method2249(
               -(var21 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2) + (double)(this.field798.method434() - 1)),
               ((double)this.field799.method434().intValue() + var21) * 2.0,
               (double)this.field798.method434().intValue(),
               var23,
               var23,
               var23,
               var23
            );
         RenderUtil.field3965
            .method2249(
               -(var21 + (double)this.field799.method434().intValue()),
               (double)(-this.field798.method434()),
               ((double)this.field799.method434().intValue() + var21) * 2.0,
               (double)this.field798.method434().intValue(),
               var23,
               var23,
               var23,
               var23
            );
         RenderUtil.field3965
            .method2249(
               -(var21 + (double)this.field799.method434().intValue() + (double)(this.field798.method434() - 1)),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               (double)this.field798.method434().intValue(),
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               var23,
               var23,
               var23,
               var23
            );
         RenderUtil.field3965
            .method2249(
               var21 + (double)this.field799.method434().intValue() - (double)this.field798.method434().intValue(),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               (double)this.field798.method434().intValue(),
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               var23,
               var23,
               var23,
               var23
            );
      }

      RenderUtil.field3965.method2235(null);
      var21 = -(var21 - 3.0);
      var19 = true;

      for (Class3060 var17 : var9) {
         if (var17 != null) {
            if (!var19) {
               var21 += IFontRender.method499().method501(" ");
            }

            var19 = false;
            IFontRender.method499()
               .drawShadowedText(
                  var17.field145,
                  var21,
                  -(IFontRender.method499().method1390() + (double)this.field799.method434().intValue()),
                  var17.field146,
                  Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off
               );
            var21 += IFontRender.method499().method501(var17.field145);
         }
      }

      IFontRender.method499().endBuilding();
      Class5922.method2142();
   }

   public void method375(WayPoint waypoint, Vector3d iPos) {
      Class5922.method61(iPos);
      IFontRender.method499().startBuilding(this.field802.getValue());
      int var9 = waypoint.field911;
      int var10 = waypoint.field910;
      int var11 = waypoint.field909;
      String var6 = waypoint.field908 + " X" + var11 + " Y" + var10 + " Z" + var9;
      double var7 = IFontRender.method499().method501(var6) / 2.0 + 2.0;
      RenderUtil.field3965.method2233();
      if (this.field795.method1362().field411 > 0) {
         RenderUtil.field3965
            .method2253(
               -(var7 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               ((double)this.field799.method434().intValue() + var7) * 2.0,
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               this.field795.method1362()
            );
      }

      if (this.field796.method1362().field411 > 0) {
         RenderUtil.field3965
            .method2249(
               -(var7 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               ((double)this.field799.method434().intValue() + var7) * 2.0,
               1.0,
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362()
            );
         RenderUtil.field3965
            .method2249(
               -(var7 + (double)this.field799.method434().intValue()),
               -1.0,
               ((double)this.field799.method434().intValue() + var7) * 2.0,
               1.0,
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362()
            );
         RenderUtil.field3965
            .method2249(
               -(var7 + (double)this.field799.method434().intValue()),
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               1.0,
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362()
            );
         RenderUtil.field3965
            .method2249(
               var7 + (double)this.field799.method434().intValue() - 1.0,
               -(IFontRender.method499().method1390() + (double)(this.field799.method434() * 2)),
               1.0,
               IFontRender.method499().method1390() + (double)(this.field799.method434() * 2),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362(),
               this.field796.method1362()
            );
      }

      RenderUtil.field3965.method2235(null);
      IFontRender.method499()
         .drawShadowedText(
            var6,
            -(IFontRender.method499().method501(var6) / 2.0),
            -(IFontRender.method499().method1390() + (double)this.field799.method434().intValue()),
            RGBAColor.field402,
            Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off
         );
      IFontRender.method499().endBuilding();
      Class5922.method2142();
   }

   private double method376(DrawContext var1, ItemStack var2, double var3, double var5) {
      if (!var2.isEmpty()) {
         AtomicReference var10 = new AtomicReference(0.0);
         MatrixStack var11 = var1.getMatrices();
         if (this.field789.method461() != NameTagsArmor.Off) {
            var11.push();
            var11.translate(var3, var5 - 29.0, 0.0);
            var11.scale(2.0F, 2.0F, 1.0F);
            var1.drawItem(var2, 0, 0);
            var1.drawItemInSlot(mc.textRenderer, var2, 0, 0);
            var10.set((Double)var10.get() + 30.0);
            var11.pop();
         }

         if (this.field787.method419()) {
            RenderSystem.getModelViewStack().pushMatrix();
            RenderSystem.getModelViewStack().translate((float)var3 + 2.0F, (float)var5, 0.0F);
            RenderSystem.getModelViewStack().scale(this.field803.method423(), this.field803.method423(), 1.0F);
            IFontRender.method499().startBuilding(1.0);
            ItemEnchantmentsComponent var12 = EnchantmentHelper.getEnchantments(var2);

            for (RegistryEntry var14 : var12.getEnchantments()) {
               String var15 = StringHelper.stripTextFormat(((Enchantment)var14.value()).description().getString()).charAt(0) + " " + var12.getLevel(var14);
               var10.set((Double)var10.get() + IFontRender.method499().method502(true));
               IFontRender.method499()
                  .drawShadowedText(var15, 0.0, -(Double)var10.get(), RGBAColor.field402, Fonts.INSTANCE.field2346.method461() != FontShadowMode.Off);
            }

            IFontRender.method499().endBuilding();
            RenderSystem.getModelViewStack().popMatrix();
         }

         return (Double)var10.get();
      } else {
         return 0.0;
      }
   }

   private static boolean method377(Vector3d var0) {
      return Class5922.method59(var0, INSTANCE.field781);
   }

   private boolean lambda$new$1() {
      return this.field789.method461() != NameTagsArmor.Off;
   }

   private boolean lambda$new$0() {
      return this.field789.method461() != NameTagsArmor.Off;
   }
}
