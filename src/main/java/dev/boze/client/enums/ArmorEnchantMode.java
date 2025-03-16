package dev.boze.client.enums;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.RegistryKey;

public enum ArmorEnchantMode {
   Prot(Enchantments.PROTECTION),
   Blast(Enchantments.BLAST_PROTECTION),
   Fire(Enchantments.FIRE_PROTECTION),
   Proj(Enchantments.PROJECTILE_PROTECTION);

   private final RegistryKey<Enchantment> field1809;
   private static final ArmorEnchantMode[] field1810 = method919();

   private ArmorEnchantMode(RegistryKey<Enchantment> var3) {
      this.field1809 = var3;
   }

   private static ArmorEnchantMode[] method919() {
      return new ArmorEnchantMode[]{Prot, Blast, Fire, Proj};
   }
}
