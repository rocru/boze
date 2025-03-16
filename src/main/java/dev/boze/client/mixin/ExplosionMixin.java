package dev.boze.client.mixin;

import dev.boze.client.mixininterfaces.IExplosion;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.Explosion.DestructionType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({Explosion.class})
public class ExplosionMixin implements IExplosion {
   @Shadow
   @Final
   @Mutable
   private World world;
   @Shadow
   @Final
   @Mutable
   @Nullable
   private Entity entity;
   @Shadow
   @Final
   @Mutable
   private double field2143;
   @Shadow
   @Final
   @Mutable
   private double field2144;
   @Shadow
   @Final
   @Mutable
   private double field2145;
   @Shadow
   @Final
   @Mutable
   private float power;
   @Shadow
   @Final
   @Mutable
   private boolean createFire;
   @Shadow
   @Final
   @Mutable
   private DestructionType destructionType;

   @Override
   public void set(Vec3d pos, float power, boolean createFire) {
      this.world = IMinecraft.mc.world;
      this.entity = null;
      this.field2143 = pos.x;
      this.field2144 = pos.y;
      this.field2145 = pos.z;
      this.power = power;
      this.createFire = createFire;
      this.destructionType = DestructionType.DESTROY;
   }
}
