package mapped;

import dev.boze.client.jumptable.qF;
import net.minecraft.util.math.Direction;

public class Class3083 {
   public static final byte field196 = 2;
   public static final byte field197 = 4;
   public static final byte field198 = 8;
   public static final byte field199 = 16;
   public static final byte field200 = 32;
   public static final byte field201 = 64;

   // $VF: Unable to simplify switch on enum
   // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
   public static byte method6049(Direction dir) {
      return switch (qF.field2129[var3225.ordinal()]) {
         case 1 -> 2;
         case 2 -> 4;
         case 3 -> 8;
         case 4 -> 16;
         case 5 -> 32;
         case 6 -> 64;
         default -> throw new IncompatibleClassChangeError();
      };
   }

   public static boolean method6050(int dir, byte idk) {
      return (var3226 & var3227) == var3227;
   }

   public static boolean method6051(int dir, byte idk) {
      return (var3228 & var3229) != var3229;
   }
}
