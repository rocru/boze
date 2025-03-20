package dev.boze.client.enums;

public enum AutoLootInventory {
    Ignore,
    Await;

    private static final AutoLootInventory[] field1655 = method774();

    private static AutoLootInventory[] method774() {
        return new AutoLootInventory[]{Ignore, Await};
    }
}
