package dev.boze.client.mixininterfaces;

public interface IClientPlayerEntity {
   void boze$sendMovementPackets(double var1, double var3, double var5, float var7, float var8, boolean var9);

   void boze$sendMovementPackets(double var1, double var3, double var5, float var7, float var8);

   void boze$sendMovementPackets(double var1, double var3, double var5);

   void boze$sendMovementPackets(float var1, float var2, boolean var3);

   void boze$sendMovementPackets(float var1, float var2);

   void boze$sendMovementPackets(double var1, double var3, double var5, boolean var7);

   void boze$sendMovementPackets(boolean var1);

   void boze_tick();
}
