package dev.boze.client.systems.modules.misc.autoarmor;

import dev.boze.client.systems.modules.combat.AutoMend;
import dev.boze.client.systems.modules.misc.AutoArmor;
import dev.boze.client.systems.modules.movement.ElytraBoost;
import dev.boze.client.systems.modules.movement.ElytraFly;
import dev.boze.client.utils.IMinecraft;
import dev.boze.client.utils.ItemEnchantmentUtils;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class nd {
    private final int field2878;
    private int field2879;
    private int field2880;
    private int field2881;
    private int field2882;
    final AutoArmor field2883;

    public nd(final AutoArmor arg, final int id) {
        super();
        this.field2883 = arg;
        this.field2878 = id;
    }

    public void method1650() {
        this.field2879 = -1;
        this.field2880 = -1;
        this.field2881 = -1;
        this.field2882 = Integer.MAX_VALUE;
    }

    public void method1651(final ItemStack itemStack, final int slot) {
        final int method1645 = this.field2883.method1645(itemStack);
        if (method1645 > this.field2880) {
            this.field2880 = method1645;
            this.field2879 = slot;
        }
    }

    public void method1652() {
        if (this.field2883.method1647()) {
            return;
        }
        final ItemStack armorStack = IMinecraft.mc.player.getInventory().getArmorStack(this.field2878);
        if (armorStack.getItem() == Items.ELYTRA) {
            if (!ElytraItem.isUsable(armorStack)) {
                this.field2881 = -1;
            } else {
                switch (this.field2883.method1638().ordinal()) {
                    case 0:
                        this.field2881 = -1;
                        break;
                    case 1:
                        this.field2881 = (this.field2883.bindState.getValue() ? 690420 : -1);
                        break;
                    case 3:
                        this.field2881 = 690420;
                        break;
                    case 2:
                        this.field2881 = ((ElytraFly.INSTANCE.isEnabled() || ElytraBoost.INSTANCE.isEnabled() || (this.field2883.awaitLanding.getValue() && IMinecraft.mc.player.isFallFlying())) ? 690420 : -1);
                        break;
                }
            }
            this.field2882 = armorStack.getMaxDamage() - armorStack.getDamage();
            return;
        }
        ItemEnchantmentUtils.populateEnchantments(armorStack, this.field2883.field2871);
        if (this.field2883.field2871.containsKey(Enchantments.BINDING_CURSE)) {
            this.field2881 = Integer.MAX_VALUE;
            return;
        }
        this.field2881 = this.field2883.method1645(armorStack);
        this.field2881 = this.method1656(this.field2881);
        this.field2881 = this.method1657(this.field2881, armorStack);
        if (!armorStack.isEmpty()) {
            this.field2882 = armorStack.getMaxDamage() - armorStack.getDamage();
        }
    }

    public int method1653() {
        if (this.field2883.preserve.getValue() && this.field2882 <= 15) {
            return -1;
        }
        return this.field2880;
    }

    public void method1654() {
        if (this.field2883.method1647() || this.field2881 == Integer.MAX_VALUE) {
            return;
        }
        final ItemStack armorStack = IMinecraft.mc.player.getInventory().getArmorStack(this.field2878);
        if (AutoMend.INSTANCE.isEnabled() && AutoMend.INSTANCE.mendRemove.getValue() && !armorStack.isEmpty() && armorStack.isDamageable() && !armorStack.isDamaged()) {
            this.field2883.method1649(this.field2878);
        } else if (this.field2880 > this.field2881) {
            this.field2883.method1648(this.field2879, this.field2878);
        } else if (this.field2883.preserve.getValue() && this.field2882 <= 15) {
            this.field2883.method1649(this.field2878);
        }
    }

    public Slot method1655() {
        if (this.field2883.method1647() || this.field2881 == Integer.MAX_VALUE) {
            return null;
        }
        if (this.field2880 > this.field2881 && IMinecraft.mc.player.getInventory().getArmorStack(this.field2878).isEmpty()) {
            return IMinecraft.mc.player.currentScreenHandler.getSlot((this.field2879 < 9) ? (36 + this.field2879) : this.field2879);
        }
        return null;
    }

    private int method1656(int n) {
        if (this.field2883.avoidBinding.getValue() && this.field2883.field2871.containsKey(Enchantments.BINDING_CURSE)) {
            n -= 2;
        }
        return n;
    }

    private int method1657(final int n, final ItemStack itemStack) {
        if (this.field2883.preserve.getValue() && itemStack.isDamageable() && itemStack.getMaxDamage() - itemStack.getDamage() <= 15) {
            return -1;
        }
        return n;
    }
}
