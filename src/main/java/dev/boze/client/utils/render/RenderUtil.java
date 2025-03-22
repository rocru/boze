package dev.boze.client.utils.render;

import dev.boze.client.api.BozeDrawColor;
import dev.boze.client.enums.RenderMode;
import dev.boze.client.gui.renderer.packer.TextureRegion;
import dev.boze.client.renderer.DrawMode;
import dev.boze.client.renderer.Mesh;
import dev.boze.client.renderer.Mesh.Attrib;
import dev.boze.client.renderer.ShaderMesh;
import dev.boze.client.renderer.TextureShaderMesh;
import dev.boze.client.shaders.ShaderRegistry;
import dev.boze.client.utils.ColorWrapper;
import dev.boze.client.utils.RGBAColor;
import dev.boze.client.utils.render.color.ChangingColor;
import dev.boze.client.utils.render.color.GradientColor;
import dev.boze.client.utils.render.color.StaticColor;
import mapped.Class3070;
import net.minecraft.client.gui.DrawContext;

public class RenderUtil {
    public static final int field3958 = 1;
    public static final int field3959 = 2;
    public static final int field3960 = 4;
    public static final int field3961 = 8;
    public static final int field3962 = 15;
    public static RenderUtil field3963;
    public static RenderUtil field3964;
    public static RenderUtil field3965;
    public static RenderUtil field3966;
    public static RenderUtil field3967;
    public static boolean field3968 = false;
    private static boolean field3973 = false;
    public final Mesh field3970;
    public final Mesh field3971;
    public final Mesh field3972;
    private boolean field3969;

    public RenderUtil(RenderMode var1) {
        if (var1 == RenderMode.GRADIENT) {
            this.field3970 = new TextureShaderMesh(
                    ShaderRegistry.field2253, DrawMode.Triangles, Attrib.Vec2, Attrib.Color, Attrib.Vec2, Attrib.Float, Attrib.Float, Attrib.Float, Attrib.Float
            );
            this.field3971 = new TextureShaderMesh(ShaderRegistry.field2251, DrawMode.Triangles, Attrib.Vec2, Attrib.Color);
            this.field3972 = new TextureShaderMesh(ShaderRegistry.field2251, DrawMode.Lines, Attrib.Vec2, Attrib.Color);
        } else if (var1 == RenderMode.HSB) {
            this.field3970 = new ShaderMesh(
                    ShaderRegistry.field2255, DrawMode.Triangles, Attrib.Vec2, Attrib.Hsba, Attrib.Vec2, Attrib.Float, Attrib.Float, Attrib.Float, Attrib.Float
            );
            this.field3971 = new ShaderMesh(ShaderRegistry.field2254, DrawMode.Triangles, Attrib.Vec2, Attrib.Hsba);
            this.field3972 = new ShaderMesh(ShaderRegistry.field2254, DrawMode.Lines, Attrib.Vec2, Attrib.Hsba);
        } else if (var1 == RenderMode.RAINBOW) {
            this.field3970 = new ShaderMesh(
                    ShaderRegistry.field2250, DrawMode.Triangles, Attrib.Vec2, Attrib.Color, Attrib.Vec2, Attrib.Float, Attrib.Float, Attrib.Float, Attrib.Float
            );
            this.field3971 = new ShaderMesh(
                    ShaderRegistry.field2257, DrawMode.Triangles, Attrib.Vec2, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
            );
            this.field3972 = new ShaderMesh(
                    ShaderRegistry.field2257, DrawMode.Lines, Attrib.Vec2, Attrib.Color, Attrib.Float, Attrib.Vec2, Attrib.Float, Attrib.Vec2
            );
        } else {
            this.field3970 = new ShaderMesh(
                    ShaderRegistry.field2250, DrawMode.Triangles, Attrib.Vec2, Attrib.Color, Attrib.Vec2, Attrib.Float, Attrib.Float, Attrib.Float, Attrib.Float
            );
            this.field3971 = new ShaderMesh(
                    var1 == RenderMode.TEXTURE ? ShaderRegistry.field2256 : ShaderRegistry.field2249,
                    DrawMode.Triangles,
                    var1 == RenderMode.TEXTURE ? new Attrib[]{Attrib.Vec2, Attrib.Vec2, Attrib.Color} : new Attrib[]{Attrib.Vec2, Attrib.Color}
            );
            this.field3972 = new ShaderMesh(ShaderRegistry.field2249, DrawMode.Lines, Attrib.Vec2, Attrib.Color);
        }
    }

    public static void method2232() {
        field3968 = true;
        field3963 = new RenderUtil(RenderMode.COLOR);
        field3964 = new RenderUtil(RenderMode.GRADIENT);
        field3965 = new RenderUtil(RenderMode.RAINBOW);
        field3966 = new RenderUtil(RenderMode.HSB);
        field3967 = new RenderUtil(RenderMode.TEXTURE);
    }

    public static void method2236() {
        field3973 = true;
    }

    public static void method2237(DrawContext var0) {
        if (field3973) {
            if (field3963.field3969) {
                field3963.method2235(var0);
            }

            if (field3964.field3969) {
                field3964.method2235(var0);
            }
        }
    }

    public static void method2238() {
        if (!field3963.field3969) {
            field3963.method2233();
        }
    }

    public static void method2239() {
        if (!field3964.field3969) {
            field3964.method2233();
        }
    }

    public static void method2240(double var0, double var2, double var4, double var6, ColorWrapper var8) {
        if (field3973) {
            if (var8.field3910 instanceof GradientColor var12) {
                method2239();
                field3964.method2241(var0, var2, var4, var6, var12.method208(), var8.field3911);
            } else if (var8.field3910 instanceof ChangingColor var13) {
                method2238();
                field3963.method2241(var0, var2, var4, var6, var13.method208(), var8.field3911);
            } else if (var8.field3910 instanceof StaticColor var14) {
                method2238();
                field3963.method2241(var0, var2, var4, var6, var14, var8.field3911);
            }
        }
    }

    public void method2233() {
        this.field3971.method2142();
        this.field3970.method2142();
        this.field3972.method2142();
        this.field3969 = true;
    }

    public void method2234() {
        this.field3971.method1198();
        this.field3970.method1198();
        this.field3972.method1198();
        this.field3969 = false;
    }

    public void method2235(DrawContext var1) {
        this.field3971.method720(var1 == null ? null : var1.getMatrices());
        this.field3970.method720(var1 == null ? null : var1.getMatrices());
        this.field3972.method720(var1 == null ? null : var1.getMatrices());
        this.field3969 = false;
    }

    public void method2241(double var1, double var3, double var5, double var7, StaticColor var9, float var10) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method708(var9, var10).method2010(),
                        this.field3971.method711(var1, var3 + var7).method708(var9, var10).method2010(),
                        this.field3971.method711(var1 + var5, var3 + var7).method708(var9, var10).method2010(),
                        this.field3971.method711(var1 + var5, var3).method708(var9, var10).method2010()
                );
    }

    public void method2242(double var1, double var3, double var5, double var7, RGBAColor var9) {
        this.field3972
                .method1964(this.field3972.method711(var1, var3).method715(var9).method2010(), this.field3972.method711(var5, var7).method715(var9).method2010());
    }

    public void method2243(double var1, double var3, double var5, double var7, RGBAColor var9, RGBAColor var10) {
        this.field3972
                .method1964(this.field3972.method711(var1, var3).method715(var9).method2010(), this.field3972.method711(var5, var7).method715(var10).method2010());
    }

    public void method2244(double var1, double var3, double var5, double var7, BozeDrawColor var9) {
        this.field3972
                .method1964(
                        this.field3972
                                .method711(var1, var3)
                                .method715(var9)
                                .method714(var9.method958())
                                .method712(var9.method959())
                                .method714(var9.method960())
                                .method711(var9.getMinHue(), var9.getMaxHue())
                                .method2010(),
                        this.field3972
                                .method711(var5, var7)
                                .method715(var9)
                                .method714(var9.method958())
                                .method712(var9.method959())
                                .method714(var9.method960())
                                .method711(var9.getMinHue(), var9.getMaxHue())
                                .method2010()
                );
    }

    public void method2245(double var1, double var3, double var5, double var7, RGBAColor var9) {
        int var10 = this.field3972.method711(var1, var3).method715(var9).method2010();
        int var11 = this.field3972.method711(var1, var3 + var7).method715(var9).method2010();
        int var12 = this.field3972.method711(var1 + var5, var3 + var7).method715(var9).method2010();
        int var13 = this.field3972.method711(var1 + var5, var3).method715(var9).method2010();
        this.field3972.method1964(var10, var11);
        this.field3972.method1964(var11, var12);
        this.field3972.method1964(var12, var13);
        this.field3972.method1964(var13, var10);
    }

    public void method2246(double var1, double var3, double var5, double var7, float[] var9) {
        this.field3972
                .method1964(this.field3972.method711(var1, var3).method706(var9).method2010(), this.field3972.method711(var5, var7).method706(var9).method2010());
    }

    public void method2247(double var1, double var3, double var5, double var7, float[] var9) {
        int var10 = this.field3972.method711(var1, var3).method706(var9).method2010();
        int var11 = this.field3972.method711(var1, var3 + var7).method706(var9).method2010();
        int var12 = this.field3972.method711(var1 + var5, var3 + var7).method706(var9).method2010();
        int var13 = this.field3972.method711(var1 + var5, var3).method706(var9).method2010();
        this.field3972.method1964(var10, var11);
        this.field3972.method1964(var11, var12);
        this.field3972.method1964(var12, var13);
        this.field3972.method1964(var13, var10);
    }

    public void method2248(double var1, double var3, double var5, double var7, RGBAColor var9, RGBAColor var10, RGBAColor var11, RGBAColor var12) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method715(var9).method2010(),
                        this.field3971.method711(var1, var3 + var7).method715(var12).method2010(),
                        this.field3971.method711(var1 + var5, var3 + var7).method715(var11).method2010(),
                        this.field3971.method711(var1 + var5, var3).method715(var10).method2010()
                );
    }

    public void method2249(double var1, double var3, double var5, double var7, BozeDrawColor var9, BozeDrawColor var10, BozeDrawColor var11, BozeDrawColor var12) {
        this.field3971
                .method1214(
                        this.field3971
                                .method711(var1, var3)
                                .method715(var9)
                                .method714(var9.method958())
                                .method712(var9.method959())
                                .method714(var9.method960())
                                .method711(var9.getMinHue(), var9.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1, var3 + var7)
                                .method715(var12)
                                .method714(var12.method958())
                                .method712(var12.method959())
                                .method714(var12.method960())
                                .method711(var12.getMinHue(), var12.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1 + var5, var3 + var7)
                                .method715(var11)
                                .method714(var11.method958())
                                .method712(var11.method959())
                                .method714(var11.method960())
                                .method711(var11.getMinHue(), var11.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1 + var5, var3)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010()
                );
    }

    public void method2250(double var1, double var3, double var5, double var7, RGBAColor var9, RGBAColor var10, RGBAColor var11, RGBAColor var12) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method715(var9).method2010(),
                        this.field3971.method711(var1, var7).method715(var12).method2010(),
                        this.field3971.method711(var5, var7).method715(var11).method2010(),
                        this.field3971.method711(var5, var3).method715(var10).method2010()
                );
    }

    public void method2251(double var1, double var3, double var5, double var7, BozeDrawColor var9, BozeDrawColor var10, BozeDrawColor var11, BozeDrawColor var12) {
        this.field3971
                .method1214(
                        this.field3971
                                .method711(var1, var3)
                                .method715(var9)
                                .method714(var9.method958())
                                .method712(var9.method959())
                                .method714(var9.method960())
                                .method711(var9.getMinHue(), var9.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1, var7)
                                .method715(var12)
                                .method714(var12.method958())
                                .method712(var12.method959())
                                .method714(var12.method960())
                                .method711(var12.getMinHue(), var12.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var5, var7)
                                .method715(var11)
                                .method714(var11.method958())
                                .method712(var11.method959())
                                .method714(var11.method960())
                                .method711(var11.getMinHue(), var11.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var5, var3)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010()
                );
    }

    public void method2252(double var1, double var3, double var5, double var7, RGBAColor var9) {
        this.method2248(var1, var3, var5, var7, var9, var9, var9, var9);
    }

    public void method2253(double var1, double var3, double var5, double var7, BozeDrawColor var9) {
        this.method2249(var1, var3, var5, var7, var9, var9, var9, var9);
    }

    public void method2254(double var1, double var3, double var5, double var7, double var9, RGBAColor var11) {
        this.method2252(var1 - var9, var3 - var9, var5 + var9 * 2.0, var9, var11);
        this.method2252(var1 - var9, var3 + var7, var5 + var9 * 2.0, var9, var11);
        this.method2252(var1 - var9, var3, var9, var7, var11);
        this.method2252(var1 + var5, var3, var9, var7, var11);
    }

    public void method2255(double var1, double var3, double var5, double var7, float[] var9, float[] var10, float[] var11, float[] var12) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method706(var9).method2010(),
                        this.field3971.method711(var1, var3 + var7).method706(var12).method2010(),
                        this.field3971.method711(var1 + var5, var3 + var7).method706(var11).method2010(),
                        this.field3971.method711(var1 + var5, var3).method706(var10).method2010()
                );
    }

    public void method2256(double var1, double var3, double var5, double var7, float[] var9) {
        this.method2255(var1, var3, var5, var7, var9, var9, var9, var9);
    }

    public void method2257(double var1, double var3, double var5, double var7, int var9, int var10, double var11, RGBAColor var13) {
        if (var11 == 0.0) {
            this.method2252(var1, var3, var5, var7, var13);
        } else {
            if (var11 > var5 / 2.0) {
                var11 = var5 / 2.0;
            }

            if (var11 > var7 / 2.0) {
                var11 = var7 / 2.0;
            }

            this.field3971
                    .method1214(
                            this.field3971.method711(var1 + var11, var3).method715(var13).method2010(),
                            this.field3971.method711(var1 + var11, var3 + var7).method715(var13).method2010(),
                            this.field3971.method711(var1 + var5 - var11, var3 + var7).method715(var13).method2010(),
                            this.field3971.method711(var1 + var5 - var11, var3).method715(var13).method2010()
                    );
            this.field3971
                    .method1214(
                            this.field3971.method711(var1, var3 + var11).method715(var13).method2010(),
                            this.field3971.method711(var1, var3 + var7 - var11).method715(var13).method2010(),
                            this.field3971.method711(var1 + var11, var3 + var7 - var11).method715(var13).method2010(),
                            this.field3971.method711(var1 + var11, var3 + var11).method715(var13).method2010()
                    );
            this.field3971
                    .method1214(
                            this.field3971.method711(var1 + var5 - var11, var3 + var11).method715(var13).method2010(),
                            this.field3971.method711(var1 + var5 - var11, var3 + var7 - var11).method715(var13).method2010(),
                            this.field3971.method711(var1 + var5, var3 + var7 - var11).method715(var13).method2010(),
                            this.field3971.method711(var1 + var5, var3 + var11).method715(var13).method2010()
                    );
            if ((var9 & 1) != 0) {
                this.method2266(var10, var11, var1 + var11, var3 + var11, 270.0, 180.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971.method711(var1, var3).method715(var13).method2010(),
                                this.field3971.method711(var1, var3 + var11).method715(var13).method2010(),
                                this.field3971.method711(var1 + var11, var3 + var11).method715(var13).method2010(),
                                this.field3971.method711(var1 + var11, var3).method715(var13).method2010()
                        );
            }

            if ((var9 & 8) != 0) {
                this.method2266(var10, var11, var1 + var11, var3 + var7 - var11, 180.0, 90.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971.method711(var1, var3 + var7 - var11).method715(var13).method2010(),
                                this.field3971.method711(var1, var3 + var7).method715(var13).method2010(),
                                this.field3971.method711(var1 + var11, var3 + var7).method715(var13).method2010(),
                                this.field3971.method711(var1 + var11, var3 + var7 - var11).method715(var13).method2010()
                        );
            }

            if ((var9 & 4) != 0) {
                this.method2266(var10, var11, var1 + var5 - var11, var3 + var7 - var11, 90.0, 0.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971.method711(var1 + var5 - var11, var3 + var7 - var11).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5 - var11, var3 + var7).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5, var3 + var7).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5, var3 + var7 - var11).method715(var13).method2010()
                        );
            }

            if ((var9 & 2) != 0) {
                this.method2266(var10, var11, var1 + var5 - var11, var3 + var11, 360.0, 270.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971.method711(var1 + var5 - var11, var3).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5 - var11, var3 + var11).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5, var3 + var11).method715(var13).method2010(),
                                this.field3971.method711(var1 + var5, var3).method715(var13).method2010()
                        );
            }
        }
    }

    public void method2258(double var1, double var3, double var5, double var7, int var9, int var10, double var11, BozeDrawColor var13) {
        if (var11 == 0.0) {
            this.method2253(var1, var3, var5, var7, var13);
        } else {
            if (var11 > var5 / 2.0) {
                var11 = var5 / 2.0;
            }

            if (var11 > var7 / 2.0) {
                var11 = var7 / 2.0;
            }

            this.field3971
                    .method1214(
                            this.field3971
                                    .method711(var1 + var11, var3)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var11, var3 + var7)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var5 - var11, var3 + var7)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var5 - var11, var3)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010()
                    );
            this.field3971
                    .method1214(
                            this.field3971
                                    .method711(var1, var3 + var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1, var3 + var7 - var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var11, var3 + var7 - var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var11, var3 + var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010()
                    );
            this.field3971
                    .method1214(
                            this.field3971
                                    .method711(var1 + var5 - var11, var3 + var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var5 - var11, var3 + var7 - var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var5, var3 + var7 - var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010(),
                            this.field3971
                                    .method711(var1 + var5, var3 + var11)
                                    .method715(var13)
                                    .method714(var13.method958())
                                    .method712(var13.method959())
                                    .method714(var13.method960())
                                    .method711(var13.getMinHue(), var13.getMaxHue())
                                    .method2010()
                    );
            if ((var9 & 1) != 0) {
                this.method2267(var10, var11, var1 + var11, var3 + var11, 270.0, 180.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971
                                        .method711(var1, var3)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1, var3 + var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var11, var3 + var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var11, var3)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010()
                        );
            }

            if ((var9 & 8) != 0) {
                this.method2267(var10, var11, var1 + var11, var3 + var7 - var11, 180.0, 90.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971
                                        .method711(var1, var3 + var7 - var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1, var3 + var7)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var11, var3 + var7)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var11, var3 + var7 - var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010()
                        );
            }

            if ((var9 & 4) != 0) {
                this.method2267(var10, var11, var1 + var5 - var11, var3 + var7 - var11, 90.0, 0.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971
                                        .method711(var1 + var5 - var11, var3 + var7 - var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5 - var11, var3 + var7)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5, var3 + var7)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5, var3 + var7 - var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010()
                        );
            }

            if ((var9 & 2) != 0) {
                this.method2267(var10, var11, var1 + var5 - var11, var3 + var11, 360.0, 270.0, var13);
            } else {
                this.field3971
                        .method1214(
                                this.field3971
                                        .method711(var1 + var5 - var11, var3)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5 - var11, var3 + var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5, var3 + var11)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010(),
                                this.field3971
                                        .method711(var1 + var5, var3)
                                        .method715(var13)
                                        .method714(var13.method958())
                                        .method712(var13.method959())
                                        .method714(var13.method960())
                                        .method711(var13.getMinHue(), var13.getMaxHue())
                                        .method2010()
                        );
            }
        }
    }

    public void method2259(double var1, double var3, double var5, double var7, int var9, RGBAColor var10) {
        int var11 = this.field3971.method711(var1 + var5 / 2.0, var3).method715(var10).method2010();
        this.field3971
                .method1214(
                        this.field3971.method711(var1 + var7, var3).method715(var10).method2010(),
                        this.field3971.method711(var1 + var7, var3 + var7).method715(var10).method2010(),
                        this.field3971.method711(var1 + var5 - var7, var3 + var7).method715(var10).method2010(),
                        this.field3971.method711(var1 + var5 - var7, var3).method715(var10).method2010()
                );
        this.method2266(var9, var7, var1 + var7, var3 + var7 - var7, 180.0, 90.0, var10);
        this.method2266(var9, var7, var1 + var5 - var7, var3 + var7 - var7, 90.0, 0.0, var10);
    }

    public void method2260(double var1, double var3, double var5, double var7, int var9, BozeDrawColor var10) {
        this.field3971
                .method1214(
                        this.field3971
                                .method711(var1 + var7, var3)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1 + var7, var3 + var7)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1 + var5 - var7, var3 + var7)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010(),
                        this.field3971
                                .method711(var1 + var5 - var7, var3)
                                .method715(var10)
                                .method714(var10.method958())
                                .method712(var10.method959())
                                .method714(var10.method960())
                                .method711(var10.getMinHue(), var10.getMaxHue())
                                .method2010()
                );
        this.method2267(var9, var7, var1 + var7, var3 + var7 - var7, 180.0, 90.0, var10);
        this.method2267(var9, var7, var1 + var5 - var7, var3 + var7 - var7, 90.0, 0.0, var10);
    }

    public void method2261(double var1, double var3, double var5, RGBAColor var7) {
        double var8 = var5 / 2.0;
        this.method2262(var1, var3, 0.0, var8, 0.0, 360.0, var7);
    }

    public void method2262(double var1, double var3, double var5, double var7, double var9, double var11, RGBAColor var13) {
        double var14 = var1 + var7;
        double var16 = var3 + var7;
        this.field3970
                .method1214(
                        this.field3970
                                .method711(var1, var3)
                                .method715(var13)
                                .method711(var14, var16)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1, var3 + var7 * 2.0)
                                .method715(var13)
                                .method711(var14, var16)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1 + var7 * 2.0, var3 + var7 * 2.0)
                                .method715(var13)
                                .method711(var14, var16)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1 + var7 * 2.0, var3)
                                .method715(var13)
                                .method711(var14, var16)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010()
                );
    }

    public void method2263(double var1, double var3, double var5, float[] var7) {
        double var8 = var5 / 2.0;
        this.method2265(var1, var3, 0.0, var8, 0.0, 360.0, var7, var7, var7, var7);
    }

    public void method2264(double var1, double var3, double var5, double var7, double var9, double var11, float[] var13) {
        this.method2265(var1, var3, var5, var7, var9, var11, var13, var13, var13, var13);
    }

    public void method2265(
            double var1, double var3, double var5, double var7, double var9, double var11, float[] var13, float[] var14, float[] var15, float[] var16
    ) {
        double var17 = var1 + var7;
        double var19 = var3 + var7;
        this.field3970
                .method1214(
                        this.field3970
                                .method711(var1, var3)
                                .method706(var13)
                                .method711(var17, var19)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1, var3 + var7 * 2.0)
                                .method706(var16)
                                .method711(var17, var19)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1 + var7 * 2.0, var3 + var7 * 2.0)
                                .method706(var15)
                                .method711(var17, var19)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010(),
                        this.field3970
                                .method711(var1 + var7 * 2.0, var3)
                                .method706(var14)
                                .method711(var17, var19)
                                .method714(var5)
                                .method714(var7)
                                .method714(var9)
                                .method714(var11)
                                .method2010()
                );
    }

    private void method2266(int var1, double var2, double var4, double var6, double var8, double var10, RGBAColor var12) {
        int var16 = 360 / var1;
        int var17 = this.field3971.method711(var4, var6).method715(var12).method2010();

        for (double var18 = var8; var18 >= var10 + (double) var16; var18 -= var16) {
            double var20 = var18;
            if (var18 < var10) {
                var20 = var10;
            }

            this.field3971
                    .method1313(
                            this.field3971
                                    .method711(var4 + Class3070.method6014(Math.toRadians(var20)) * var2, var6 + Class3070.method6013(Math.toRadians(var20)) * var2)
                                    .method715(var12)
                                    .method2010(),
                            var17,
                            this.field3971
                                    .method711(
                                            var4 + Class3070.method6014(Math.toRadians(var20 - (double) var16)) * var2,
                                            var6 + Class3070.method6013(Math.toRadians(var20 - (double) var16)) * var2
                                    )
                                    .method715(var12)
                                    .method2010()
                    );
        }
    }

    private void method2267(int var1, double var2, double var4, double var6, double var8, double var10, BozeDrawColor var12) {
        int var16 = 360 / var1;
        int var17 = this.field3971
                .method711(var4, var6)
                .method715(var12)
                .method714(var12.method958())
                .method712(var12.method959())
                .method714(var12.method960())
                .method711(var12.getMinHue(), var12.getMaxHue())
                .method2010();

        for (double var18 = var8; var18 >= var10 + (double) var16; var18 -= var16) {
            double var20 = var18;
            if (var18 < var10) {
                var20 = var10;
            }

            this.field3971
                    .method1313(
                            this.field3971
                                    .method711(var4 + Class3070.method6014(Math.toRadians(var20)) * var2, var6 + Class3070.method6013(Math.toRadians(var20)) * var2)
                                    .method715(var12)
                                    .method714(var12.method958())
                                    .method712(var12.method959())
                                    .method714(var12.method960())
                                    .method711(var12.getMinHue(), var12.getMaxHue())
                                    .method2010(),
                            var17,
                            this.field3971
                                    .method711(
                                            var4 + Class3070.method6014(Math.toRadians(var20 - (double) var16)) * var2,
                                            var6 + Class3070.method6013(Math.toRadians(var20 - (double) var16)) * var2
                                    )
                                    .method715(var12)
                                    .method714(var12.method958())
                                    .method712(var12.method959())
                                    .method714(var12.method960())
                                    .method711(var12.getMinHue(), var12.getMaxHue())
                                    .method2010()
                    );
        }
    }

    public void method2268(double var1, double var3, double var5, double var7, RGBAColor var9) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method711(0.0, 0.0).method715(var9).method2010(),
                        this.field3971.method711(var1, var3 + var7).method711(0.0, 1.0).method715(var9).method2010(),
                        this.field3971.method711(var1 + var5, var3 + var7).method711(1.0, 1.0).method715(var9).method2010(),
                        this.field3971.method711(var1 + var5, var3).method711(1.0, 0.0).method715(var9).method2010()
                );
    }

    public void method2269(double var1, double var3, double var5, double var7, TextureRegion var9, RGBAColor var10) {
        this.field3971
                .method1214(
                        this.field3971.method711(var1, var3).method711(var9.field2076, var9.field2077).method715(var10).method2010(),
                        this.field3971.method711(var1, var3 + var7).method711(var9.field2076, var9.field2079).method715(var10).method2010(),
                        this.field3971.method711(var1 + var5, var3 + var7).method711(var9.field2078, var9.field2079).method715(var10).method2010(),
                        this.field3971.method711(var1 + var5, var3).method711(var9.field2078, var9.field2077).method715(var10).method2010()
                );
    }

    public void method2270(
            double var1, double var3, double var5, double var7, double var9, double var11, double var13, double var15, double var17, RGBAColor var19
    ) {
        double var20 = Math.toRadians(var9);
        double var22 = Math.cos(var20);
        double var24 = Math.sin(var20);
        double var26 = var1 + var5 / 2.0;
        double var28 = var3 + var7 / 2.0;
        double var30 = (var1 - var26) * var22 - (var3 - var28) * var24 + var26;
        double var32 = (var3 - var28) * var22 + (var1 - var26) * var24 + var28;
        int var34 = this.field3971.method711(var30, var32).method711(var11, var13).method715(var19).method2010();
        double var35 = (var1 - var26) * var22 - (var3 + var7 - var28) * var24 + var26;
        double var37 = (var3 + var7 - var28) * var22 + (var1 - var26) * var24 + var28;
        int var39 = this.field3971.method711(var35, var37).method711(var11, var17).method715(var19).method2010();
        double var40 = (var1 + var5 - var26) * var22 - (var3 + var7 - var28) * var24 + var26;
        double var42 = (var3 + var7 - var28) * var22 + (var1 + var5 - var26) * var24 + var28;
        int var44 = this.field3971.method711(var40, var42).method711(var15, var17).method715(var19).method2010();
        double var45 = (var1 + var5 - var26) * var22 - (var3 - var28) * var24 + var26;
        double var47 = (var3 - var28) * var22 + (var1 + var5 - var26) * var24 + var28;
        int var49 = this.field3971.method711(var45, var47).method711(var15, var13).method715(var19).method2010();
        this.field3971.method1214(var34, var39, var44, var49);
    }

    public void method2271(double var1, double var3, double var5, double var7, double var9, TextureRegion var11, RGBAColor var12) {
        this.method2270(var1, var3, var5, var7, var9, var11.field2076, var11.field2077, var11.field2078, var11.field2079, var12);
    }
}
