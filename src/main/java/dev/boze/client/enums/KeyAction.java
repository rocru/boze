package dev.boze.client.enums;

public enum KeyAction {
   Press,
   Repeat,
   Release;

   public static KeyAction method816(int action) {
      if (action == 1) {
         return Press;
      } else {
         return action == 0 ? Release : Repeat;
      }
   }

   // $VF: synthetic method
   private static KeyAction[] method817() {
      return new KeyAction[]{Press, Repeat, Release};
   }
}
