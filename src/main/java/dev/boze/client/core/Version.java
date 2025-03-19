package dev.boze.client.core;

public class Version {
    public static String tag = "2.0-Beta 93";
    public static boolean isBeta = false;

    static {
        isBeta = tag.contains("Beta");
    }
}
