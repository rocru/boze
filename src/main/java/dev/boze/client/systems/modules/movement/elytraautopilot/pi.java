package dev.boze.client.systems.modules.movement.elytraautopilot;

import dev.boze.client.instances.impl.ChatInstance;
import dev.boze.client.systems.modules.movement.ElytraAutoPilot;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class pi implements IMinecraft {
    public static float field1098 = 0.0F;
    public static double field1099 = 2.1605124;
    public static double field1100 = 0.20545267;
    public static double field1101 = 10.0;
    public static double field1102 = -46.633514;
    public static double field1103 = 37.19872;
    public static double field1104 = 1.9102669;
    public static double field1105 = 2.3250866;
    public static boolean field1106;
    public static boolean field1107;
    public static double field1110 = 1.0;
    public static Vec3d field1111;
    public static double field1112;
    public static double field1113;
    public static boolean field1114;
    public static boolean field1115;
    public static boolean field1116;
    public static int field1119;
    public static int field1120;
    public static boolean field1121 = false;
    public static boolean field1122 = false;
    public static boolean field1123 = false;
    public static boolean field1124 = false;
    public static boolean field1125 = false;
    public static double field1126 = 0.0;
    public static double field1127;
    private static int field1108 = 0;
    private static boolean field1109;
    private static double field1117 = 0.0;
    private static double field1118 = 0.0;

    public static void method2142() {
        field1106 = false;
        field1107 = false;
        field1108 = 0;
        field1109 = false;
        field1110 = 1.0;
        field1111 = null;
        field1112 = 0.0;
        field1113 = 0.0;
        field1114 = false;
        field1115 = false;
        field1116 = false;
        field1117 = 0.0;
        field1118 = 0.0;
        field1119 = 0;
        field1120 = 0;
        field1121 = false;
        field1122 = false;
        field1123 = false;
        field1124 = false;
        field1125 = false;
        field1126 = 0.0;
        field1127 = 0.0;
        field1125 = false;
        field1126 = 0.0;
        field1127 = 0.0;
    }

    public static void method1416() {
        if (!field1109) {
            if (mc.player != null) {
                Item var15 = mc.player.getMainHandStack().getItem();
                Item var16 = mc.player.getOffHandStack().getItem();
                Item var17 = mc.player.getInventory().armor.get(2).getItem();
                int var6 = mc.player.getInventory().armor.get(2).getMaxDamage() - mc.player.getInventory().armor.get(2).getDamage();
                if (var17 != Items.ELYTRA) {
                    ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "You need to equip an elytra to take off");
                    return;
                }

                if (var6 == 1) {
                    ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "Your elytra is broken");
                    return;
                }

                if (var15 != Items.FIREWORK_ROCKET && var16 != Items.FIREWORK_ROCKET && !method2114()) {
                    ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "You need to hold fireworks in your main or off hand to take off");
                    return;
                }

                Vec3d var7 = mc.player.getPos();
                int var8 = mc.world.getTopY();
                int var9 = 2;
                double var10 = var7.getY();

                for (double var12 = var10; var12 < (double) var8; var12++) {
                    BlockPos var14 = BlockPos.ofFloored(var7.getX(), var7.getY() + (double) var9, var7.getZ());
                    if (!mc.world.getBlockState(var14).isAir()) {
                        ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "You need to be in a clear area to take off");
                        return;
                    }

                    var9++;
                }

                field1108 = 5;
                mc.options.jumpKey.setPressed(true);
            }
        } else {
            if (mc.player != null) {
                if (field1127 > (double) ElytraAutoPilot.INSTANCE.field3205.method2010()) {
                    field1109 = false;
                    mc.options.useKey.setPressed(false);
                    mc.options.jumpKey.setPressed(false);
                    field1107 = true;
                    field1110 = 3.0;
                    if (field1121) {
                        field1121 = false;
                        field1107 = true;
                        field1122 = true;
                        field1110 = 3.0;
                    }

                    return;
                }

                if (!mc.player.isFallFlying()) {
                    mc.options.jumpKey.setPressed(!mc.options.jumpKey.isPressed());
                }

                Item var3 = mc.player.getMainHandStack().getItem();
                Item var4 = mc.player.getOffHandStack().getItem();
                boolean var5 = var3 == Items.FIREWORK_ROCKET || var4 == Items.FIREWORK_ROCKET;
                if (!var5) {
                    if (!method2114()) {
                        mc.options.useKey.setPressed(false);
                        mc.options.jumpKey.setPressed(false);
                        field1109 = false;
                        ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "You need to hold fireworks in your main or off hand to take off");
                        field1125 = true;
                    }
                } else {
                    mc.options.useKey.setPressed(field1112 < 0.75 && mc.player.getPitch() == -90.0F);
                }
            }
        }
    }

    public static void method1198() {
        if (mc.player != null) {
            float var3 = mc.getRenderTickCounter().getLastFrameDuration();
            float var4 = 20.0F / var3;
            double var5 = 60.0F / var4;
            float var7 = mc.player.getPitch();
            if (field1125) {
                if (var7 < field1098) {
                    mc.player.setPitch((float) ((double) var7 + field1100 * var5 * 3.0));
                    var7 = mc.player.getPitch();
                    if (var7 >= field1098) {
                        mc.player.setPitch(field1098);
                        field1125 = false;
                    }
                } else if (var7 > field1098) {
                    mc.player.setPitch((float) ((double) var7 - field1100 * var5) * 3.0F);
                    var7 = mc.player.getPitch();
                    if (var7 <= field1098) {
                        mc.player.setPitch(field1098);
                        field1125 = false;
                    }
                }
            } else if (field1109) {
                if (var7 > -90.0F) {
                    mc.player.setPitch((float) ((double) var7 - field1101 * var5));
                    var7 = mc.player.getPitch();
                }

                if (var7 <= -90.0F) {
                    mc.player.setPitch(-90.0F);
                }
            }

            if (field1107) {
                if (field1122 || field1123) {
                    if (!field1124 && !field1123) {
                        Vec3d var8 = mc.player.getPos();
                        double var9 = (double) field1119 - var8.x;
                        double var11 = (double) field1120 - var8.z;
                        float var13 = MathHelper.wrapDegrees((float) (MathHelper.atan2(var11, var9) * 180.0F / (float) Math.PI) - 90.0F);
                        float var14 = MathHelper.wrapDegrees(mc.player.getYaw());
                        if ((double) Math.abs(var14 - var13) < 6.0 * var5) {
                            mc.player.setYaw(var13);
                        } else {
                            if (var14 < var13) {
                                mc.player.setYaw((float) ((double) var14 + 3.0 * var5));
                            }

                            if (var14 > var13) {
                                mc.player.setYaw((float) ((double) var14 - 3.0 * var5));
                            }
                        }

                        field1126 = Math.sqrt(var9 * var9 + var11 * var11);
                        if (field1126 < 20.0) {
                            ChatInstance.method740(ElytraAutoPilot.INSTANCE.getName(), "Arrived at destination");
                            field1124 = true;
                        }
                    } else {
                        field1114 = true;
                        if (field1127 > 100.0) {
                            if (!(field1113 > 0.3F) && !(field1112 > 1.0)) {
                                method306(var5);
                            } else {
                                method938(var5);
                            }
                        } else {
                            method938(var5);
                        }
                    }
                }

                if (field1115 && !field1124 && !field1123) {
                    mc.player.setPitch((float) ((double) var7 - field1099 * var5));
                    var7 = mc.player.getPitch();
                    if ((double) var7 <= field1102) {
                        mc.player.setPitch((float) field1102);
                    }

                    mc.options.useKey.setPressed(!ElytraAutoPilot.INSTANCE.field3203.getValue() && field1112 < 1.25 && method2114());
                }

                if (field1116 && !field1124 && !field1123) {
                    mc.player.setPitch((float) ((double) var7 + field1100 * field1110 * var5));
                    var7 = mc.player.getPitch();
                    if ((double) var7 >= field1103) {
                        mc.player.setPitch((float) field1103);
                    }

                    mc.options.useKey.setPressed(!ElytraAutoPilot.INSTANCE.field3203.getValue() && field1112 < 1.25 && method2114());
                }
            } else {
                field1117 = 0.0;
                field1118 = 0.0;
                field1124 = false;
                field1123 = false;
                field1122 = false;
                field1115 = false;
                field1110 = 1.0;
                field1116 = false;
            }
        }
    }

    public static void method1904() {
        if (mc.player == null) {
            field1107 = false;
            field1109 = false;
        } else {
            if (mc.player.isFallFlying()) {
                field1106 = true;
            } else {
                field1106 = false;
                field1107 = false;
                field1127 = -1.0;
            }

            if (field1107) {
                if (!method2115()) {
                    ChatInstance.method742(ElytraAutoPilot.INSTANCE.getName(), "Landing because elytra will break");
                    field1123 = true;
                }

                double var5 = mc.player.getPos().y;
                if (mc.player.isTouchingWater() || mc.player.isInLava()) {
                    field1122 = false;
                    field1124 = false;
                    field1107 = false;
                    return;
                }

                if (field1114) {
                    field1115 = false;
                    field1116 = true;
                    if (var5 > (double) ElytraAutoPilot.INSTANCE.field3205.method1547()) {
                        field1117 = 0.3F;
                    } else if (var5 > (double) (ElytraAutoPilot.INSTANCE.field3205.method1547() - 10)) {
                        field1118 = 0.28475F;
                    }

                    double var3 = Math.max(field1117, field1118);
                    if (field1112 >= field1105 + var3) {
                        field1114 = false;
                        field1116 = false;
                        field1115 = true;
                        field1110 = 1.0;
                    }
                } else {
                    field1117 = 0.0;
                    field1118 = 0.0;
                    field1115 = true;
                    field1116 = false;
                    if (field1112 <= field1104 || var5 > (double) (ElytraAutoPilot.INSTANCE.field3205.method1547() - 10)) {
                        field1114 = true;
                        field1116 = true;
                        field1115 = false;
                    }
                }
            }

            if (field1108 > 0 && --field1108 == 0) {
                field1109 = true;
            }

            if (field1109) {
                method1416();
            }

            if (field1106) {
                method1854();
                int var7 = mc.world.getBottomY();
                Vec3d var8 = mc.player.getPos();

                for (double var9 = var8.getY(); var9 > (double) var7; var9--) {
                    BlockPos var11 = BlockPos.ofFloored(var8.getX(), var9, var8.getZ());
                    if (!mc.world.getBlockState(var11).isAir()) {
                        field1127 = var8.getY() - var9;
                        break;
                    }

                    field1127 = var8.getY();
                }
            } else {
                field1111 = null;
            }
        }
    }

    private static void method1854() {
        if (mc.player != null) {
            Vec3d var3 = mc.player.getPos();
            if (field1111 == null) {
                field1111 = var3;
            }

            Vec3d var4 = new Vec3d(var3.x - field1111.x, var3.y - field1111.y, var3.z - field1111.z);
            Vec3d var5 = new Vec3d(var3.x - field1111.x, 0.0, var3.z - field1111.z);
            field1111 = var3;
            field1112 = var4.length();
            field1113 = var5.length();
        }
    }

    private static boolean method2114() {
        if (mc.player.getMainHandStack().getItem() != Items.FIREWORK_ROCKET && mc.player.getOffHandStack().getItem() != Items.FIREWORK_ROCKET) {
            if (ElytraAutoPilot.INSTANCE.field3201.getValue()) {
                int var3 = InventoryHelper.method169(pi::lambda$tryRestockFirework$0);
                if (var3 != -1) {
                    int var4;
                    if (mc.player.getOffHandStack().isEmpty()) {
                        var4 = 45;
                    } else {
                        var4 = 36 + mc.player.getInventory().selectedSlot;
                    }

                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, var4, var3, SlotActionType.SWAP, mc.player);
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    private static boolean method2115() {
        int var3 = mc.player.getInventory().armor.get(2).getMaxDamage() - mc.player.getInventory().armor.get(2).getDamage();
        if (var3 <= 5) {
            ItemStack var4 = null;

            for (ItemStack var7 : mc.player.getInventory().main) {
                if (var7.getItem() == Items.ELYTRA) {
                    int var8 = var7.getMaxDamage() - var7.getDamage();
                    if (var8 >= 10) {
                        var4 = var7;
                        break;
                    }
                }
            }

            if (var4 == null) {
                return false;
            }

            byte var9 = 6;
            mc.interactionManager
                    .clickSlot(mc.player.playerScreenHandler.syncId, var9, mc.player.getInventory().main.indexOf(var4), SlotActionType.SWAP, mc.player);
            ChatInstance.method740(ElytraAutoPilot.INSTANCE.getName(), "Swapped elytra");
        }

        return true;
    }

    private static void method938(double var0) {
        float var5 = MathHelper.wrapDegrees(mc.player.getYaw());
        float var6 = MathHelper.wrapDegrees(mc.player.getPitch());
        float var9;
        if (field1127 > 50.0) {
            var9 = 50.0F;
        } else if (field1127 < 20.0) {
            var9 = 30.0F;
        } else {
            var9 = (float) ((field1127 - 20.0) / 30.0) * 20.0F + 30.0F;
        }

        field1110 = 3.0;
        mc.player.setYaw((float) ((double) var5 + 3.0 * var0));
        mc.player.setPitch((float) ((double) var6 + field1100 * field1110 * var0));
        var6 = mc.player.getPitch();
        if (var6 >= var9) {
            mc.player.setPitch(var9);
        }
    }

    private static void method306(double var0) {
        float var5 = mc.player.getPitch();
        mc.player.setPitch((float) ((double) var5 + field1101 * var0));
        var5 = mc.player.getPitch();
        if (var5 > 90.0F) {
            mc.player.setPitch(90.0F);
        }
    }

    private static boolean lambda$tryRestockFirework$0(ItemStack var0) {
        return var0.getItem() == Items.FIREWORK_ROCKET;
    }
}
