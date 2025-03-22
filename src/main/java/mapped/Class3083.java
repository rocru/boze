package mapped;

import net.minecraft.util.math.Direction;

public class Class3083 {
    public static final byte field196 = 2;
    public static final byte field197 = 4;
    public static final byte field198 = 8;
    public static final byte field199 = 16;
    public static final byte field200 = 32;
    public static final byte field201 = 64;

    public static byte method6049(Direction var3225) {
        return switch (var3225) {
            case Direction.UP -> 2;
            case Direction.DOWN -> 4;
            case Direction.NORTH -> 8;
            case Direction.SOUTH -> 16;
            case Direction.WEST -> 32;
            case Direction.EAST -> 64;
            default -> throw new IncompatibleClassChangeError();
        };
    }

    public static boolean method6050(int var3226, byte var3227) {
        return (var3226 & var3227) == var3227;
    }

    public static boolean method6051(int var3228, byte var3229) {
        return (var3228 & var3229) != var3229;
    }
}
