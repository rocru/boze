package mapped;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import netutil.BozeClassLoader;
import netutil.Count;

public class Class5905<K, V> extends HashMap<K, V> {
   public Class5905(int var1) {
      super(var1);
   }

   public int size() {
      int var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = (boolean)var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (true) {
         if (var6 < var5) {
            Field var7 = var4[var6];
            if (!var3) {
               var10000 = Modifier.isPrivate(var7.getModifiers());
               if (var3) {
                  break;
               }

               if (var10000 == 0) {
                  return 0;
               }

               var6++;
            }

            if (!var3) {
               continue;
            }
         }

         var10000 = super.size();
         break;
      }

      return var10000;
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public V get(Object var1) {
      boolean var10000 = Count.field4012;
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      boolean var4 = var10000;
      int var6 = var5.length;
      int var7 = 0;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (!var4) {
               var9 = var8;
               if (var4) {
                  break;
               }

               if (!Modifier.isPrivate(var8.getModifiers())) {
                  return null;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         var9 = super.get(var1);
         break;
      }

      return (V)var9;
   }

   public boolean containsKey(Object var1) {
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      int var6 = var5.length;
      int var7 = 0;
      boolean var4 = Count.field4012;

      boolean var10000;
      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (!var4) {
               var10000 = Modifier.isPrivate(var8.getModifiers());
               if (var4) {
                  break;
               }

               if (!var10000) {
                  return false;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         var10000 = super.containsKey(var1);
         break;
      }

      return var10000;
   }

   public V put(K var1, V var2) {
      boolean var10000 = Count.field4012;
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      boolean var5 = var10000;
      int var7 = var6.length;
      int var8 = 0;

      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return (V)var2;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10 = super.put(var1, var2);
         break;
      }

      return (V)var10;
   }

   public void putAll(Map<? extends K, ? extends V> var1) {
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      int var6 = var5.length;
      int var7 = 0;
      boolean var4 = Count.field4012;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (var4) {
               break;
            }

            if (!var4) {
               if (!Modifier.isPrivate(var8.getModifiers())) {
                  return;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         super.putAll(var1);
         break;
      }
   }

   public V remove(Object var1) {
      boolean var10000 = Count.field4012;
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      int var6 = var5.length;
      int var7 = 0;
      boolean var4 = var10000;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (!var4) {
               var9 = var8;
               if (var4) {
                  break;
               }

               if (!Modifier.isPrivate(var8.getModifiers())) {
                  return null;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         var9 = super.remove(var1);
         break;
      }

      return (V)var9;
   }

   public void clear() {
      boolean var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (true) {
         if (var6 < var5) {
            Field var7 = var4[var6];
            if (var3) {
               break;
            }

            if (!var3) {
               if (!Modifier.isPrivate(var7.getModifiers())) {
                  return;
               }

               var6++;
            }

            if (!var3) {
               continue;
            }
         }

         super.clear();
         break;
      }
   }

   public boolean containsValue(Object var1) {
      boolean var10000 = Count.field4012;
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      int var6 = var5.length;
      boolean var4 = var10000;
      int var7 = 0;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (!var4) {
               var10000 = Modifier.isPrivate(var8.getModifiers());
               if (var4) {
                  break;
               }

               if (!var10000) {
                  return false;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         var10000 = super.containsValue(var1);
         break;
      }

      return var10000;
   }

   public Set<K> keySet() {
      boolean var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (var6 < var5) {
         Field var7 = var4[var6];
         if (!var3) {
            if (!Modifier.isPrivate(var7.getModifiers())) {
               return null;
            }

            var6++;
         }

         if (var3) {
            break;
         }
      }

      return super.keySet();
   }

   public Collection<V> values() {
      boolean var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (var6 < var5) {
         Field var7 = var4[var6];
         if (!var3) {
            if (!Modifier.isPrivate(var7.getModifiers())) {
               return null;
            }

            var6++;
         }

         if (var3) {
            break;
         }
      }

      return super.values();
   }

   public Set<Entry<K, V>> entrySet() {
      boolean var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (var6 < var5) {
         Field var7 = var4[var6];
         if (!var3) {
            if (!Modifier.isPrivate(var7.getModifiers())) {
               return null;
            }

            var6++;
         }

         if (var3) {
            break;
         }
      }

      return super.entrySet();
   }

   public V getOrDefault(Object var1, V var2) {
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      int var7 = var6.length;
      int var8 = 0;
      boolean var5 = Count.field4012;

      Object var10000;
      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10000 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return (V)var2;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10000 = super.getOrDefault(var1, var2);
         break;
      }

      return (V)var10000;
   }

   public V putIfAbsent(K var1, V var2) {
      boolean var10000 = Count.field4012;
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      int var7 = var6.length;
      boolean var5 = var10000;
      int var8 = 0;

      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return (V)var2;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10 = super.putIfAbsent(var1, var2);
         break;
      }

      return (V)var10;
   }

   public boolean remove(Object var1, Object var2) {
      boolean var10000 = Count.field4012;
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      boolean var5 = var10000;
      int var7 = var6.length;
      int var8 = 0;

      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10000 = Modifier.isPrivate(var9.getModifiers());
               if (var5) {
                  break;
               }

               if (!var10000) {
                  return false;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10000 = super.remove(var1, var2);
         break;
      }

      return var10000;
   }

   public boolean replace(K var1, V var2, V var3) {
      boolean var10000 = Count.field4012;
      Field[] var7 = BozeClassLoader.class.getDeclaredFields();
      boolean var6 = var10000;
      int var8 = var7.length;
      int var9 = 0;

      while (true) {
         if (var9 < var8) {
            Field var10 = var7[var9];
            if (!var6) {
               var10000 = Modifier.isPrivate(var10.getModifiers());
               if (var6) {
                  break;
               }

               if (!var10000) {
                  return false;
               }

               var9++;
            }

            if (!var6) {
               continue;
            }
         }

         var10000 = super.replace(var1, var2, var3);
         break;
      }

      return var10000;
   }

   public V replace(K var1, V var2) {
      boolean var10000 = Count.field4012;
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      boolean var5 = var10000;
      int var7 = var6.length;
      int var8 = 0;

      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return (V)var2;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10 = super.replace(var1, var2);
         break;
      }

      return (V)var10;
   }

   public V computeIfAbsent(K var1, Function<? super K, ? extends V> var2) {
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      int var7 = var6.length;
      boolean var5 = Count.field4012;
      int var8 = 0;

      Object var10000;
      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10000 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return null;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10000 = super.computeIfAbsent(var1, var2);
         break;
      }

      return (V)var10000;
   }

   public V computeIfPresent(K var1, BiFunction<? super K, ? super V, ? extends V> var2) {
      boolean var10000 = Count.field4012;
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      boolean var5 = var10000;
      int var7 = var6.length;
      int var8 = 0;

      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return null;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10 = super.computeIfPresent(var1, var2);
         break;
      }

      return (V)var10;
   }

   public V compute(K var1, BiFunction<? super K, ? super V, ? extends V> var2) {
      Field[] var6 = BozeClassLoader.class.getDeclaredFields();
      int var7 = var6.length;
      boolean var5 = Count.field4012;
      int var8 = 0;

      Object var10000;
      while (true) {
         if (var8 < var7) {
            Field var9 = var6[var8];
            if (!var5) {
               var10000 = var9;
               if (var5) {
                  break;
               }

               if (!Modifier.isPrivate(var9.getModifiers())) {
                  return null;
               }

               var8++;
            }

            if (!var5) {
               continue;
            }
         }

         var10000 = super.compute(var1, var2);
         break;
      }

      return (V)var10000;
   }

   public V merge(K var1, V var2, BiFunction<? super V, ? super V, ? extends V> var3) {
      Field[] var7 = BozeClassLoader.class.getDeclaredFields();
      int var8 = var7.length;
      int var9 = 0;
      boolean var6 = Count.field4012;

      Object var10000;
      while (true) {
         if (var9 < var8) {
            Field var10 = var7[var9];
            if (!var6) {
               var10000 = var10;
               if (var6) {
                  break;
               }

               if (!Modifier.isPrivate(var10.getModifiers())) {
                  return null;
               }

               var9++;
            }

            if (!var6) {
               continue;
            }
         }

         var10000 = super.merge(var1, var2, var3);
         break;
      }

      return (V)var10000;
   }

   public void forEach(BiConsumer<? super K, ? super V> var1) {
      boolean var10000 = Count.field4012;
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      boolean var4 = var10000;
      int var6 = var5.length;
      int var7 = 0;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (var4) {
               break;
            }

            if (!var4) {
               if (!Modifier.isPrivate(var8.getModifiers())) {
                  return;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         super.forEach(var1);
         break;
      }
   }

   public void replaceAll(BiFunction<? super K, ? super V, ? extends V> var1) {
      boolean var10000 = Count.field4012;
      Field[] var5 = BozeClassLoader.class.getDeclaredFields();
      int var6 = var5.length;
      boolean var4 = var10000;
      int var7 = 0;

      while (true) {
         if (var7 < var6) {
            Field var8 = var5[var7];
            if (var4) {
               break;
            }

            if (!var4) {
               if (!Modifier.isPrivate(var8.getModifiers())) {
                  return;
               }

               var7++;
            }

            if (!var4) {
               continue;
            }
         }

         super.replaceAll(var1);
         break;
      }
   }

   public Object clone() {
      boolean var10000 = Count.field4012;
      Field[] var4 = BozeClassLoader.class.getDeclaredFields();
      boolean var3 = var10000;
      int var5 = var4.length;
      int var6 = 0;

      while (true) {
         if (var6 < var5) {
            Field var7 = var4[var6];
            if (!var3) {
               var8 = var7;
               if (var3) {
                  break;
               }

               if (!Modifier.isPrivate(var7.getModifiers())) {
                  return null;
               }

               var6++;
            }

            if (!var3) {
               continue;
            }
         }

         var8 = super.clone();
         break;
      }

      return var8;
   }
}
