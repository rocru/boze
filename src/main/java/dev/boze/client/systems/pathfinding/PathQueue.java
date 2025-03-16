package dev.boze.client.systems.pathfinding;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

public class PathQueue {
   private final PriorityQueue<PathSegment> field3897 = new PriorityQueue(Comparator.comparing(PathQueue::lambda$new$0));

   public boolean method2123() {
      return this.field3897.isEmpty();
   }

   public boolean method2124(PathPos pos, float priority) {
      return this.field3897.add(new PathSegment(pos, priority));
   }

   public PathPos[] method2125() {
      PathPos[] var4 = new PathPos[this.method2126()];
      Iterator var5 = this.field3897.iterator();

      for (int var6 = 0; var6 < this.method2126() && var5.hasNext(); var6++) {
         var4[var6] = ((PathSegment)var5.next()).field3898;
      }

      return var4;
   }

   public int method2126() {
      return this.field3897.size();
   }

   public void method2127() {
      this.field3897.clear();
   }

   public PathPos method2128() {
      return ((PathSegment)this.field3897.poll()).field3898;
   }

   private static Float lambda$new$0(PathSegment var0) {
      return var0.field3899;
   }
}
