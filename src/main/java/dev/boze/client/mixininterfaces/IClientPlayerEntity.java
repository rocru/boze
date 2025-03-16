package dev.boze.client.mixininterfaces;

public interface IClientPlayerEntity {
   void sendMovementPackets(double var1, double var3, double var5, float var7, float var8, boolean var9);

   void sendMovementPackets(double var1, double var3, double var5, float var7, float var8);

   void sendMovementPackets(double var1, double var3, double var5);

   void sendMovementPackets(float var1, float var2, boolean var3);

   void sendMovementPackets(float var1, float var2);

   void sendMovementPackets(double var1, double var3, double var5, boolean var7);

   void sendMovementPackets(boolean var1);

   void boze_tick();
}
