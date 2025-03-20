package dev.boze.client.utils.click;

public class ClickImplementationA extends IClickMethod {
    private long field1329 = System.currentTimeMillis();

    @Override
    public int method578(double targetedCPS) {
        int var6 = (int) Math.round((double) (System.currentTimeMillis() - this.field1329) / (1000.0 / targetedCPS));
        this.field1329 += (long) ((double) var6 * (1000.0 / targetedCPS));
        return var6;
    }

    @Override
    public void method938(double targetedCPS) {
        this.field1329 = System.currentTimeMillis() - (long) (1000.0 / targetedCPS);
    }
}
