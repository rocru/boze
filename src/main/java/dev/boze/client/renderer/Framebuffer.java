package dev.boze.client.renderer;

import dev.boze.client.utils.IMinecraft;

public class Framebuffer implements IMinecraft {
    private int framebufferID;
    public int textureID;
    public double sizeMulti = 1.0;
    public int framebufferWidth;
    public int framebufferHeight;

    public Framebuffer(double sizeMulti) {
        this.sizeMulti = sizeMulti;
        this.initializeFramebuffer();
    }

    private void initializeFramebuffer() {
        this.framebufferID = GL.method1163();
        this.bindFramebuffer();
        this.textureID = GL.method1162();
        GL.method1211(this.textureID);
        GL.method1193();
        GL.method1191(3553, 10242, 33071);
        GL.method1191(3553, 10243, 33071);
        GL.method1191(3553, 10241, 9729);
        GL.method1191(3553, 10240, 9729);
        this.framebufferWidth = (int) ((double) mc.getWindow().getFramebufferWidth() * this.sizeMulti);
        this.framebufferHeight = (int) ((double) mc.getWindow().getFramebufferHeight() * this.sizeMulti);
        GL.method1192(3553, 0, 6407, this.framebufferWidth, this.framebufferHeight, 0, 6407, 5121, null);
        GL.method1195(36160, 36064, 3553, this.textureID, 0);
        this.unbindFramebuffer();
    }

    public void bindFramebuffer() {
        GL.method1171(this.framebufferID);
    }

    public void clearFramebuffer() {
        GL.method1214(0, 0, this.framebufferWidth, this.framebufferHeight);
    }

    public void unbindFramebuffer() {
        mc.getFramebuffer().beginWrite(false);
    }

    public void resizeFramebuffer() {
        GL.method1166(this.framebufferID);
        GL.method1165(this.textureID);
        this.initializeFramebuffer();
    }
}
