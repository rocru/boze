package mapped;

import net.minecraft.client.input.Input;

class Class3078 extends Input {
   final Input field175;

   Class3078(Input var1) {
      this.field175 = var1;
   }

   public void tick(boolean slowDown, float f) {
      this.movementForward = this.field175.movementForward;
      this.movementSideways = this.field175.movementSideways;
      this.pressingForward = this.field175.pressingForward;
      this.pressingBack = this.field175.pressingBack;
      this.pressingLeft = this.field175.pressingLeft;
      this.pressingRight = this.field175.pressingRight;
      this.jumping = this.field175.jumping;
      this.sneaking = this.field175.sneaking;
      if (slowDown) {
         this.movementSideways *= f;
         this.movementForward *= f;
      }
   }
}
