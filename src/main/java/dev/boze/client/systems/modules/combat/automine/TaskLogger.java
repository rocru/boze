package dev.boze.client.systems.modules.combat.automine;

import dev.boze.client.utils.Timer;

public class TaskLogger {
    BlockDirectionInfo field2532;
    public float field2533;
    int field2534;
    final Timer field2535;
    final Miner field2536;

    TaskLogger(Miner var1, BlockDirectionInfo var2) {
        this.field2536 = var1;
        this.field2533 = 0.0F;
        this.field2534 = -1;
        this.field2535 = new Timer();
        this.field2532 = var2;
    }

    void start() {
        Miner.method1800("Starting task at " + this.field2532.field2523.toShortString());
    }

    void finish() {
        Miner.method1800("Task at " + this.field2532.field2523.toShortString() + " has been instant mined");
    }

    void fail() {
        Miner.method1800("Failed to start task at " + this.field2532.field2523.toShortString());
    }

    void pause() {
        Miner.method1800("Left range of task at " + this.field2532.field2523.toShortString());
    }

    void complete() {
        Miner.method1800("Task at " + this.field2532.field2523.toShortString() + " has been completed");
    }

    void swap() {
        Miner.method1800("Swapping for task at " + this.field2532.field2523.toShortString());
    }

    void init() {
        Miner.method1800("Task at " + this.field2532.field2523.toShortString() + " has been initialized");
    }
}
