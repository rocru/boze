package dev.boze.client.enums;

public enum NukerFilter {
    Off,
    Whitelist,
    Blacklist;

    private static final NukerFilter[] field1780 = method891();

    private static NukerFilter[] method891() {
        return new NukerFilter[]{Off, Whitelist, Blacklist};
    }
}
