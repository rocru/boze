package mapped;

public class Class3070 {
    public Class3070() {
        super();
    }

    private static double method6012(double n) {
        n %= 6.283185307179586;
        if (Math.abs(n) > 3.141592653589793) {
            n -= 6.283185307179586;
        }
        if (Math.abs(n) > 1.5707963267948966) {
            n = 3.141592653589793 - n;
        }
        return n;
    }

    public static double method6013(double radians) {
        radians = method6012(radians);
        if (Math.abs(radians) <= 0.7853981633974483) {
            return Math.sin(radians);
        }
        return Math.cos(1.5707963267948966 - radians);
    }

    public static double method6014(final double radians) {
        return method6013(radians + 1.5707963267948966);
    }
}
