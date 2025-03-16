package dev.boze.client.api;

import dev.boze.api.input.Bind;

public class BozeBind implements Bind {
   private boolean field1839;
   private int field1840;

   public BozeBind(boolean isButton, int bind) {
      this.field1839 = isButton;
      this.field1840 = bind;
   }

   public int getBind() {
      return this.field1840;
   }

   public boolean isButton() {
      return this.field1839;
   }
}
