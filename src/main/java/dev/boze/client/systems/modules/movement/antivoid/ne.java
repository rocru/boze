package dev.boze.client.systems.modules.movement.antivoid;

import dev.boze.client.systems.modules.movement.AntiVoid;
import dev.boze.client.utils.Timer;
import java.util.ArrayList;
import java.util.List;
import mapped.Class3062;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import org.joml.Vector3d;

public class ne {
   private final List<Vector3d> field3138;
   private final Vector3d field3139;
   private final Timer field3140;
   private BlockHitResult field3141;
   private Entity field3142;
   private int field3143;
   final AntiVoid field3144;

   public ne(final AntiVoid this$0) {
      this.field3144 = this$0;
      this.field3138 = new ArrayList();
      this.field3139 = new Vector3d();
      this.field3140 = new Timer();
      this.field3143 = -1;
   }

   public void method1782() {
      for (Vector3d var5 : this.field3138) {
         this.field3144.field557.method5994(var5);
      }

      this.field3138.clear();
      this.field3141 = null;
      this.field3142 = null;
      this.field3143 = -1;
   }

   public void method1783() {
      this.method1784();
      Vector3d var4 = this.field3144.field557.method5993().set(this.field3144.field556.field13);
      this.field3139.set(0.0, 0.0, 0.0);
      this.method1785(var4);
      long var5 = System.nanoTime();

      while (System.nanoTime() - var5 < 1000000L) {
         HitResult var7 = this.field3144.field556.method49();
         if (var7 != null) {
            this.method1786(var7);
            break;
         }

         this.method1784();
      }
   }

   private void method1784() {
      this.field3138.add(this.field3144.field557.method5993().set(this.field3144.field556.field13));
   }

   private void method1785(Vector3d var1) {
      this.field3138.add(var1);
   }

   private void method1786(HitResult var1) {
      if (var1.getType() == Type.BLOCK) {
         this.field3141 = (BlockHitResult)var1;
         this.field3138.add(Class3062.method5989(this.field3144.field557.method5993(), var1.getPos()));
      } else if (var1.getType() == Type.ENTITY) {
         this.field3142 = ((EntityHitResult)var1).getEntity();
         this.field3138
            .add(Class3062.method5989(this.field3144.field557.method5993(), var1.getPos()).add(0.0, (double)(this.field3142.getHeight() / 2.0F), 0.0));
      }
   }
}
