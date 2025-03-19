package dev.boze.client.enums;

enum NewChunksScan {
    New,
    Old,
    Both;

    private static final NewChunksScan[] field29 = method24();

    private static NewChunksScan[] method24() {
        return new NewChunksScan[]{New, Old, Both};
    }
}
