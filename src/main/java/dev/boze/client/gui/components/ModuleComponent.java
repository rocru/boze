package dev.boze.client.gui.components;

import dev.boze.client.enums.AlignMode;
import dev.boze.client.enums.ModuleDisplayMode;
import dev.boze.client.enums.NotificationLength;
import dev.boze.client.enums.ToggleStyle;
import dev.boze.client.font.IFontRender;
import dev.boze.client.font.IconManager;
import dev.boze.client.gui.components.scaled.bottomrow.ConfigComponent;
import dev.boze.client.gui.components.setting.*;
import dev.boze.client.gui.components.setting.scaled.*;
import dev.boze.client.gui.notification.Notifications;
import dev.boze.client.gui.screens.ClickGUI;
import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.settings.*;
import dev.boze.client.systems.modules.Module;
import dev.boze.client.systems.modules.client.*;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.KeyboardUtil;
import dev.boze.client.utils.Timer;
import dev.boze.client.utils.misc.CursorType;
import dev.boze.client.utils.render.RenderUtil;
import mapped.Class2774;
import mapped.Class3077;
import mapped.Class5928;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;

public class ModuleComponent extends BaseComponent implements IMinecraft {
    public final Module field334;
    private final double field335;
    private final ArrayList<BaseComponent> field336 = new ArrayList();
    private final Class2774[] field337 = new Class2774[4];
    public boolean field338;
    private boolean field339 = false;
    private final Timer field340 = new Timer();
    private double field341;
    private double field342;
    public SettingBlock field343 = null;
    private double field344;
    private double field345;
    private double field346;
    private double field347;

    public ModuleComponent(Module module, BaseComponent parent, double x, double y, double width) {
        super(module.internalName, parent, x, y, width, (double) Theme.method1367() * scaleFactor);
        this.field334 = module;
        this.field335 = (double) Theme.method1367() * scaleFactor;
        double var12 = this.field335;
        if (module.getNotificationLength() != NotificationLength.Popup) {
            this.field336.add(new dev.boze.client.gui.components.setting.BindComponent(module, this, x, var12, width, (double) Theme.method1376() * scaleFactor));
            var12 += (double) Theme.method1376() * scaleFactor;

            for (Setting var15 : module.method1144()) {
                double var16 = var15.parent == null ? scaleFactor * 3.0 : scaleFactor * 3.0 * 2.0;
                double var18 = x + var16;
                double var20 = width - var16;
                if (var15 instanceof SettingCategory) {
                    this.field336.add(new SettingCategoryComponent((SettingCategory) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof BooleanSetting) {
                    this.field336.add(new BooleanSettingComponent((BooleanSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof EnumSetting) {
                    this.field336.add(new EnumSettingComponent((EnumSetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof MinMaxSetting && !((MinMaxSetting) var15).field945) {
                    this.field336.add(new MinMaxSettingComponent((MinMaxSetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof MinMaxDoubleSetting && !((MinMaxDoubleSetting) var15).field2204) {
                    this.field336
                            .add(
                                    new MinMaxDoubleSettingComponent((MinMaxDoubleSetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor)
                            );
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof FloatSetting && !((FloatSetting) var15).field933) {
                    this.field336.add(new FloatSettingComponent((FloatSetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof IntSetting && !((IntSetting) var15).field937) {
                    this.field336.add(new IntSettingComponent((IntSetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof IntArraySetting && !((IntArraySetting) var15).field923) {
                    this.field336
                            .add(new IntArraySettingComponent((IntArraySetting) var15, this, var18, var12, var20, ((double) Theme.method1376() + 4.0) * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof BindSetting) {
                    this.field336.add(new BindSettingComponent((BindSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof RGBASetting) {
                    this.field336.add(new RGBASettingComponent((RGBASetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof ColorSetting) {
                    this.field336.add(new ColorSettingComponent((ColorSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof WeirdColorSetting) {
                    this.field336.add(new WeirdColorSettingComponent((WeirdColorSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof FriendsSetting) {
                    this.field336.add(new FriendsSettingComponent((FriendsSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof CurrentProfileSetting) {
                    this.field336
                            .add(new CurrentProfileSettingComponent((CurrentProfileSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof ProfileSetting) {
                    this.field336.add(new ProfileSettingComponent((ProfileSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof StringSetting) {
                    this.field336.add(new StringSettingComponent((StringSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof ListSetting) {
                    this.field336.add(new ListSettingComponent((ListSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof SoundStringSetting) {
                    this.field336
                            .add(new SoundStringSettingComponent((SoundStringSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof MacroSetting) {
                    this.field336.add(new MacroSettingComponent((MacroSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof AccountSetting) {
                    this.field336.add(new AccountSettingComponent((AccountSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof MacroManagerSetting) {
                    this.field336
                            .add(new MacroManagerSettingComponent((MacroManagerSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof FontSetting) {
                    this.field336.add(new FontSettingComponent((FontSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof SettingBlock) {
                    this.field336.add(new SettingBlockComponent((SettingBlock) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                } else if (var15 instanceof ShaderSetting) {
                    this.field336.add(new ShaderSettingComponent((ShaderSetting) var15, this, var18, var12, var20, (double) Theme.method1376() * scaleFactor));
                    var12 += (double) Theme.method1376() * scaleFactor;
                }
            }

            if (module.getNotificationLength() == NotificationLength.Normal) {
                this.field337[0] = new Class2774(Notifications.POWER, () -> ModuleComponent.lambda$new$0(module), Theme.INSTANCE.field2435);
                this.field337[1] = new Class2774(Notifications.TUNE, () -> ModuleComponent.lambda$new$1(module), Theme.INSTANCE.field2436);
            }
            if (module != Profiles.INSTANCE && module != Fonts.INSTANCE) {
                this.field337[2] = new Class2774(Notifications.VIEW_LIST, () -> ModuleComponent.lambda$new$2(module), Theme.INSTANCE.aa);
            }

            this.field337[3] = new Class2774(this.field338 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE, this::lambda$new$3, Theme.INSTANCE.ab);
        }
    }

    // $VF: Unable to simplify switch on enum
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335)
                || Math.abs((double) mouseX - this.field341) > this.field320 / 4.0) {
            this.field340.reset();
        }

        this.field341 = mouseX;
        this.field342 = mouseY;
        String var8 = Theme.method1392(Theme.method1369(), this.field334.getName());

        double var9 = switch (Theme.method1368()) {
            case AlignMode.Left -> this.field318 + scaleFactor * 6.0;
            case AlignMode.Center ->
                    this.field318 + this.field320 / 2.0 - IFontRender.method499().method501(var8) / 2.0;
            case AlignMode.Right ->
                    this.field318 + this.field320 - scaleFactor * 6.0 - IFontRender.method499().method501(var8);
            default -> throw new IncompatibleClassChangeError();
        };
        if (Theme.method1373()) {
            RenderUtil.field3965
                    .method2253(this.field318, this.field319, this.field320, this.field335, this.field334.isEnabled() ? Theme.method1375() : Theme.method1374());
        } else {
            RenderUtil.field3963
                    .method2252(this.field318, this.field319, this.field320, this.field335, this.field334.isEnabled() ? Theme.method1353() : Theme.method1347());
        }

        IFontRender.method499()
                .drawShadowedText(
                        var8,
                        var9,
                        this.field319 + this.field335 / 2.0 - IFontRender.method499().method1390() / 2.0,
                        this.field334.isEnabled() ? Theme.method1354() : Theme.method1350()
                );
        if (this.field334.getNotificationLength() == NotificationLength.Popup) {
            this.field321 = this.field335;
            if (Theme.method1370() == ModuleDisplayMode.Icons
                    && (!Theme.method1372() || isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335))) {
                double var28 = IconManager.method1116();
                double var33 = Math.min((this.field335 - var28) * 0.5, scaleFactor * 6.0);
                double var41 = this.field318 + this.field320 - var28 - var33;
                double var47 = this.field319 + this.field335 * 0.5 - var28 * 0.5;
                boolean var50 = isMouseWithinBounds(mouseX, mouseY, var41, var47, var28, var28);
                Notifications.POPUP
                        .render(
                                var41,
                                var47,
                                var50
                                        ? (this.field334.isEnabled() ? Theme.method1354() : Theme.method1350())
                                        : (this.field334.isEnabled() ? Theme.method1355() : Theme.method1351())
                        );
            }
        } else {
            if (Theme.method1372() && !isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335)) {
                if (Theme.method1370() == ModuleDisplayMode.Icons && Theme.method1371()) {
                    String var26;
                    if (this.field334.bind.getBind() == -1) {
                        var26 = " ";
                    } else {
                        var26 = this.field334.bind.isKey()
                                ? KeyboardUtil.getKeyName(this.field334.bind.getBind())
                                : KeyboardUtil.getButtonName(this.field334.bind.getBind());
                    }

                    double var29 = this.field334.bind.getBind() == -1
                            ? IFontRender.method499().method1390() - scaleFactor * 2.0
                            : IFontRender.method499().method501(var26);
                    double var34 = IFontRender.method499().method1390();
                    double var44 = Math.max(var29, var34);
                    double var49 = Theme.method1368() == AlignMode.Right
                            ? this.field318 + scaleFactor * 6.0
                            : this.field318 + this.field320 - (var44 + scaleFactor * (double) (var44 > var29 ? 2 : 4)) * 0.5 - this.field335 * 0.1 - var29 / 2.0;
                    IFontRender.method499()
                            .drawShadowedText(
                                    var26, var49, this.field319 + this.field335 * 0.5 - var34 * 0.5, this.field334.isEnabled() ? Theme.method1355() : Theme.method1351()
                            );
                    if (this.field334.getHoldBind() && !this.field339) {
                        double var51 = Theme.method1368() == AlignMode.Right
                                ? this.field318 + scaleFactor * 6.0
                                : this.field318
                                + this.field320
                                - (var44 + scaleFactor * (double) (var44 > var29 ? 2 : 4)) * 0.5
                                - this.field335 * 0.1
                                - IFontRender.method499().method501("_") / 2.0;
                        IFontRender.method499()
                                .drawShadowedText(
                                        "_", var51, this.field319 + this.field335 * 0.5 - var34 * 0.5, this.field334.isEnabled() ? Theme.method1354() : Theme.method1350()
                                );
                    }
                }
            } else if (Theme.method1370() == ModuleDisplayMode.Icons) {
                double var11 = IconManager.method1116();
                double var13 = Math.min((this.field335 - var11) * 0.5, scaleFactor * 6.0);
                int var15 = 0;

                for (Class2774 var19 : this.field337) {
                    if (var19 != null && var19.method5428()) {
                        var15++;
                    }
                }

                double var42 = this.field318 + this.field320 - var11 * (double) var15 - var13 * (double) var15;
                if (Theme.method1368() == AlignMode.Right) {
                    var42 = this.field318 + scaleFactor * 6.0;
                }

                double var48 = this.field319 + this.field335 * 0.5 - var11 * 0.5;

                for (Class2774 var23 : this.field337) {
                    if (var23 != null && var23.method5428()) {
                        var23.field83 = var42;
                        var23.field84 = var48;
                        var23.field85 = var11;
                        var42 += var11 + var13;
                        boolean var24 = isMouseWithinBounds(mouseX, mouseY, var23.field83, var23.field84, var23.field85, var23.field85);
                        var23.field80
                                .render(
                                        var23.field83,
                                        var23.field84,
                                        var24
                                                ? (this.field334.isEnabled() ? Theme.method1354() : Theme.method1350())
                                                : (this.field334.isEnabled() ? Theme.method1355() : Theme.method1351())
                                );
                    }
                }
            } else if (Theme.method1370() == ModuleDisplayMode.Bind && this.field334.getNotificationLength() == NotificationLength.Normal) {
                String var25;
                if (this.field339) {
                    var25 = "...";
                } else if (this.field334.bind.getBind() == -1) {
                    var25 = " ";
                } else {
                    var25 = this.field334.bind.isKey()
                            ? KeyboardUtil.getKeyName(this.field334.bind.getBind())
                            : KeyboardUtil.getButtonName(this.field334.bind.getBind());
                }

                double var12 = this.field334.bind.getBind() == -1
                        ? IFontRender.method499().method1390() - scaleFactor * 2.0
                        : IFontRender.method499().method501(var25);
                double var14 = IFontRender.method499().method1390();
                double var43 = Math.max(var12, var14);
                RenderUtil.field3963
                        .method2257(
                                this.field344 = this.field318 + this.field320 - (var43 + scaleFactor * (double) (var43 > var12 ? 2 : 4)) - scaleFactor * 6.0,
                                this.field345 = this.field319 + this.field335 * 0.5 - (var14 * 0.5 + scaleFactor),
                                this.field346 = var43 + scaleFactor * (double) (var43 > var12 ? 2 : 4),
                                this.field347 = var14 + scaleFactor * 2.0,
                                15,
                                12,
                                scaleFactor * 4.0,
                                (this.field334.isEnabled() ? Theme.method1353() : Theme.method1347()).method183(Theme.method1390())
                        );
                IFontRender.method499()
                        .drawShadowedText(
                                var25,
                                this.field318 + this.field320 - (var43 + scaleFactor * (double) (var43 > var12 ? 2 : 4)) * 0.5 - scaleFactor * 6.0 - var12 / 2.0,
                                this.field319 + this.field335 * 0.5 - var14 * 0.5,
                                this.field334.isEnabled() ? Theme.method1354() : Theme.method1350()
                        );
                if (isMouseWithinBounds(mouseX, mouseY, this.field344, this.field345, this.field346, this.field347)) {
                    ClickGUI.field1335.field1337 = CursorType.IBeam;
                }

                if (this.field334.getHoldBind() && !this.field339) {
                    IFontRender.method499()
                            .drawShadowedText(
                                    "_",
                                    this.field318
                                            + this.field320
                                            - (var43 + scaleFactor * (double) (var43 > var12 ? 2 : 4)) * 0.5
                                            - scaleFactor * 6.0
                                            - IFontRender.method499().method501("_") / 2.0,
                                    this.field319 + this.field335 * 0.5 - var14 * 0.5,
                                    this.field334.isEnabled() ? Theme.method1354() : Theme.method1350()
                            );
                }
            } else if (Theme.method1370() == ModuleDisplayMode.State && this.field334.getNotificationLength() == NotificationLength.Normal) {
                switch (Gui.INSTANCE.field2371.getValue()) {
                    case ToggleStyle.Switch:
                        RenderUtil.field3963
                                .method2257(
                                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 1.2,
                                        this.field319 + this.field335 * 0.2,
                                        this.field335 * 1.2,
                                        this.field335 * 0.6,
                                        15,
                                        12,
                                        this.field335 * 0.8,
                                        this.field334.isEnabled() ? Theme.method1353() : Theme.method1347().method183(Theme.method1390())
                                );
                        RenderUtil.field3963
                                .method2261(
                                        this.field318 + this.field320 - 6.0 * scaleFactor - (this.field334.isEnabled() ? this.field335 * 0.5 : this.field335 * 1.1),
                                        this.field319 + this.field335 * 0.3,
                                        this.field335 * 0.4,
                                        this.field334.isEnabled() ? Theme.method1354() : Theme.method1350()
                                );
                        break;
                    case ToggleStyle.Circle:
                        RenderUtil.field3963
                                .method2261(
                                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 0.6,
                                        this.field319 + this.field335 * 0.2,
                                        this.field335 * 0.6,
                                        this.field334.isEnabled() ? Theme.method1353() : Theme.method1347().method183(Theme.method1390())
                                );
                        if (this.field334.isEnabled()) {
                            RenderUtil.field3963
                                    .method2261(
                                            this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 0.45,
                                            this.field319 + this.field335 * 0.35,
                                            this.field335 * 0.3,
                                            Theme.method1354()
                                    );
                        }
                        break;
                    case ToggleStyle.Check:
                        RenderUtil.field3963
                                .method2242(
                                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 0.3,
                                        this.field319 + this.field335 * 0.5,
                                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 0.15,
                                        this.field319 + this.field335 * 0.8,
                                        !this.field334.isEnabled() ? Theme.method1347().method183(Theme.method1390()) : Theme.method1352()
                                );
                        RenderUtil.field3963
                                .method2242(
                                        this.field318 + this.field320 - 6.0 * scaleFactor - this.field335 * 0.15,
                                        this.field319 + this.field335 * 0.8,
                                        this.field318 + this.field320 - 6.0 * scaleFactor,
                                        this.field319 + this.field335 * 0.2,
                                        !this.field334.isEnabled() ? Theme.method1347().method183(Theme.method1390()) : Theme.method1352()
                                );
                }
            }

            if (this.field338) {
                double var27 = this.field335;
                if (this.field343 != null) {
                    for (BaseComponent var35 : this.field336) {
                        if (var35 instanceof SettingBlockComponent var38 && var38.field400 == this.field343) {
                            var27 += this.method160(var38, var27, context, mouseX, mouseY, delta);
                            break;
                        }
                    }
                }

                for (BaseComponent var36 : this.field336) {
                    if (!(var36 instanceof ScaledSettingBaseComponent)) {
                        return;
                    }

                    if (!(var36 instanceof dev.boze.client.gui.components.setting.BindComponent) || Theme.method1370() != ModuleDisplayMode.Bind) {
                        ScaledSettingBaseComponent var39 = (ScaledSettingBaseComponent) var36;
                        if (this.field343 == null ? var39.field271.block == null : var39.field271.block == this.field343) {
                            var27 += this.method160(var39, var27, context, mouseX, mouseY, delta);
                        }
                    }
                }

                this.field321 = var27;
                if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335)
                        && (Gui.INSTANCE.field2355.getValue() == 0.0 || this.field340.hasElapsed(Gui.INSTANCE.field2355.getValue() * 1000.0))) {
                    String var32 = this.field334.description;

                    for (Class2774 var46 : this.field337) {
                        if (var46 != null
                                && var46.method5428()
                                && isMouseWithinBounds(mouseX, mouseY, var46.field83, var46.field84, var46.field85, var46.field85)) {
                            var32 = var46.field80 == Notifications.POWER
                                    ? (this.field334.isEnabled() ? "Disable " : "Enable ") + this.field334.getName()
                                    : (
                                    var46.field80 == Notifications.TUNE
                                            ? "Change " + this.field334.getName() + " bind"
                                            : (
                                            var46.field80 == Notifications.VIEW_LIST
                                                    ? "View " + this.field334.getName() + " configs"
                                                    : (this.field338 ? "Hide " : "Show ") + this.field334.getName() + "settings"
                                    )
                            );
                        }
                    }

                    ClickGUI.field1335
                            .method581(
                                    var32, this.field318 - (double) Theme.method1365() * scaleFactor, this.field318 + this.field320 + (double) Theme.method1365() * scaleFactor
                            );
                    this.field334.field433 = true;
                }
            } else {
                this.field321 = this.field335;
            }
        }
    }

    private double method160(ScaledSettingBaseComponent var1, double var2, DrawContext var4, int var5, int var6, float var7) {
        double var11 = var1.field271.parent == null ? scaleFactor * 3.0 : scaleFactor * 3.0 * 2.0;
        var1.field318 = this.field318 + var11;
        var1.field319 = this.field319 + var2;
        var1.render(var4, var5, var6, var7);
        if (var1.field271.method2116() && var1.field321 > 0.0 && var1.field272 != null) {
            RenderUtil.field3963.method2252(this.field318, this.field319 + var2, var11, var1.field321, var1.field272);
            if (Theme.method1377()) {
                RenderUtil.field3963.method2252(this.field318, this.field319 + var2, scaleFactor, var1.field321, Theme.method1351());
                if (var1.field271.parent != null) {
                    RenderUtil.field3963.method2252(this.field318 + scaleFactor, this.field319 + var2, scaleFactor, var1.field321, Theme.method1351());
                }
            }
        }

        return var1.field321;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.field334.getNotificationLength() == NotificationLength.Popup) {
            if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335)) {
                ScaledBaseComponent var15 = this.field334.method218().get();
                if (var15 != null) {
                    mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                    ClickGUI.field1335.method580(var15);
                }

                return true;
            } else {
                return super.mouseClicked(mouseX, mouseY, button);
            }
        } else {
            if (this.field338) {
                for (BaseComponent var10 : this.field336) {
                    if (var10 instanceof ScaledSettingBaseComponent var11
                            && (!(var10 instanceof dev.boze.client.gui.components.setting.BindComponent) || Theme.method1370() != ModuleDisplayMode.Bind)) {
                        if (this.field343 == null) {
                            if (var11.field271.block != null) {
                                continue;
                            }
                        } else if (var11.field271.block != this.field343) {
                            Setting var13 = var11.field271;
                            if (!(var13 instanceof SettingBlock var12)) {
                                continue;
                            }

                            if (!var12.getValue()) {
                                continue;
                            }
                        }

                        if (var11.mouseClicked(mouseX, mouseY, button)) {
                            return true;
                        }
                    }
                }
            }

            if (isMouseWithinBounds(mouseX, mouseY, this.field318, this.field319, this.field320, this.field335)) {
                if (Theme.method1370() == ModuleDisplayMode.Bind && isMouseWithinBounds(mouseX, mouseY, this.field344, this.field345, this.field346, this.field347)
                ) {
                    if (this.field334.getNotificationLength() == NotificationLength.Limited) {
                        return super.mouseClicked(mouseX, mouseY, button);
                    }

                    if (this.field339) {
                        this.field334.bind.set(false, button);
                        this.field339 = false;
                        Class3077.field174 = false;
                        return true;
                    }

                    if (button == 0 && !Class3077.field174) {
                        this.field339 = true;
                        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return true;
                    }

                    if (button == 1) {
                        this.field334.setHoldBind(!this.field334.getHoldBind());
                        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        return true;
                    }
                } else {
                    if (button == 0) {
                        if (Theme.method1370() == ModuleDisplayMode.Icons) {
                            for (Class2774 var18 : this.field337) {
                                if (var18 != null
                                        && var18.method5428()
                                        && isMouseWithinBounds(
                                        mouseX,
                                        mouseY,
                                        var18.field83 - var18.field85 * 0.1,
                                        var18.field84 - var18.field85 * 0.1,
                                        var18.field85 * 1.2,
                                        var18.field85 * 1.2
                                )) {
                                    var18.field81.run();
                                    return true;
                                }
                            }
                        }

                        if (this.field334.getNotificationLength() == NotificationLength.Limited) {
                            return super.mouseClicked(mouseX, mouseY, button);
                        }

                        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        if (Class5928.method159(340)) {
                            this.field334.setVisibility(!this.field334.getVisibility());
                            ChatInstance.method740(this.field334.getName(), (this.field334.getVisibility() ? "Unhidden" : "Hidden") + " from arraylist");
                        } else if (Class5928.method159(342)) {
                            this.field334.setNotify(!this.field334.getNotify());
                            ChatInstance.method740(this.field334.getName(), (this.field334.getNotify() ? "Enabled" : "Disabled") + " notifications");
                        } else {
                            this.field334.toggle();
                        }

                        return true;
                    }

                    if (button == 1) {
                        this.field338 = !this.field338;
                        this.field337[3].field80 = this.field338 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE;
                        return true;
                    }

                    if (button == 2) {
                        if (this.field334.getNotificationLength() == NotificationLength.Limited) {
                            return super.mouseClicked(mouseX, mouseY, button);
                        }

                        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        this.field334.setVisibility(!this.field334.getVisibility());
                        return true;
                    }
                }
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    public boolean method2116() {
        return !Options.INSTANCE.method1971() || this.field334.field435 || this.field334.method221() != null;
    }

    @Override
    public void onMouseClicked(double mouseX, double mouseY, int button) {
        for (BaseComponent var10 : this.field336) {
            var10.onMouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean onDrag(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.field338) {
            for (BaseComponent var14 : this.field336) {
                if (var14.onDrag(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }

        return super.onDrag(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double amount) {
        if (this.field338) {
            for (BaseComponent var11 : this.field336) {
                if (var11.onMouseScroll(mouseX, mouseY, amount)) {
                    return true;
                }
            }
        }

        return super.onMouseScroll(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.field339) {
            if (keyCode == 256) {
                this.field334.bind.set(true, -1);
            } else {
                this.field334.bind.set(true, keyCode);
            }

            this.field339 = false;
            Class3077.field174 = false;
            return true;
        } else {
            if (this.field338) {
                for (BaseComponent var8 : this.field336) {
                    if (var8.keyPressed(keyCode, scanCode, modifiers)) {
                        return true;
                    }
                }
            }

            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        for (BaseComponent var8 : this.field336) {
            if (var8.keyReleased(keyCode, scanCode, modifiers)) {
                return true;
            }
        }

        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void lambda$new$3() {
        this.field338 = !this.field338;
        this.field337[3].field80 = this.field338 ? Notifications.EXPAND_LESS : Notifications.EXPAND_MORE;
    }

    private static void lambda$new$2(Module var0) {
        ClickGUI.field1335.method580(new ConfigComponent(var0));
        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private static void lambda$new$1(Module var0) {
        ClickGUI.field1335.method580(new dev.boze.client.gui.components.scaled.ModuleComponent(var0));
        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    private static void lambda$new$0(Module var0) {
        var0.toggle();
        mc.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
