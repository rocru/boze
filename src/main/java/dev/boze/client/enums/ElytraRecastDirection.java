package dev.boze.client.enums;

enum ElytraRecastDirection {
    Off(0.0F),
    North(180.0F),
    NE(-135.0F),
    East(-90.0F),
    SE(-45.0F),
    South(0.0F),
    SW(45.0F),
    West(90.0F),
    NW(135.0F);

    float field1766;
    private static final ElytraRecastDirection[] field1767 = method878();

    ElytraRecastDirection(float var3) {
        this.field1766 = var3;
    }

    private static ElytraRecastDirection[] method878() {
        return new ElytraRecastDirection[]{Off, North, NE, East, SE, South, SW, West, NW};
    }
}
