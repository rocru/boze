package dev.boze.client.utils.player;

import dev.boze.client.mixin.HandledScreenAccessor;
import dev.boze.client.mixin.HorseScreenHandlerAccessor;
import dev.boze.client.utils.IMinecraft;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen.CreativeScreenHandler;
import net.minecraft.entity.mob.SkeletonHorseEntity;
import net.minecraft.entity.mob.ZombieHorseEntity;
import net.minecraft.entity.passive.AbstractDonkeyEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.LlamaEntity;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.math.Vec2f;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class SlotUtils implements IMinecraft {
   public static final int field1503 = 0;
   public static final int field1504 = 8;
   public static final int field1505 = 45;
   public static final int field1506 = 9;
   public static final int field1507 = 35;
   public static final int field1508 = 36;
   public static final int field1509 = 39;
   public static final int field1510 = 16;

   public static int method1541(int i) {
      if (mc.player == null) {
         return -1;
      } else {
         ScreenHandler var4 = mc.player.currentScreenHandler;
         if (var4 instanceof PlayerScreenHandler) {
            return method186(i);
         } else if (var4 instanceof CreativeScreenHandler) {
            return method187(i);
         } else if (var4 instanceof GenericContainerScreenHandler) {
            return method413(i, ((GenericContainerScreenHandler)var4).getRows());
         } else if (var4 instanceof CraftingScreenHandler) {
            return method188(i);
         } else if (var4 instanceof FurnaceScreenHandler) {
            return method648(i);
         } else if (var4 instanceof BlastFurnaceScreenHandler) {
            return method648(i);
         } else if (var4 instanceof SmokerScreenHandler) {
            return method648(i);
         } else if (var4 instanceof Generic3x3ContainerScreenHandler) {
            return method649(i);
         } else if (var4 instanceof EnchantmentScreenHandler) {
            return method650(i);
         } else if (var4 instanceof BrewingStandScreenHandler) {
            return method651(i);
         } else if (var4 instanceof MerchantScreenHandler) {
            return method652(i);
         } else if (var4 instanceof BeaconScreenHandler) {
            return method653(i);
         } else if (var4 instanceof AnvilScreenHandler) {
            return method654(i);
         } else if (var4 instanceof HopperScreenHandler) {
            return method655(i);
         } else if (var4 instanceof ShulkerBoxScreenHandler) {
            return method413(i, 3);
         } else if (var4 instanceof HorseScreenHandler) {
            return method656(var4, i);
         } else if (var4 instanceof CartographyTableScreenHandler) {
            return method657(i);
         } else if (var4 instanceof GrindstoneScreenHandler) {
            return method658(i);
         } else if (var4 instanceof SmithingScreenHandler) {
            return method659(i);
         } else if (var4 instanceof LecternScreenHandler) {
            return method2010();
         } else if (var4 instanceof LoomScreenHandler) {
            return method660(i);
         } else {
            return var4 instanceof StonecutterScreenHandler ? method661(i) : -1;
         }
      }
   }

   private static int method186(int var0) {
      if (method159(var0)) {
         return 36 + var0;
      } else {
         return method662(var0) ? 5 + (var0 - 36) : var0;
      }
   }

   private static int method187(int var0) {
      return !(mc.currentScreen instanceof CreativeInventoryScreen) ? -1 : method186(var0);
   }

   private static int method413(int var0, int var1) {
      if (method159(var0)) {
         return (var1 + 3) * 9 + var0;
      } else {
         return method518(var0) ? var1 * 9 + (var0 - 9) : -1;
      }
   }

   private static int method188(int var0) {
      if (method159(var0)) {
         return 37 + var0;
      } else {
         return method518(var0) ? var0 + 1 : -1;
      }
   }

   private static int method648(int var0) {
      if (method159(var0)) {
         return 30 + var0;
      } else {
         return method518(var0) ? 3 + (var0 - 9) : -1;
      }
   }

   private static int method649(int var0) {
      if (method159(var0)) {
         return 36 + var0;
      } else {
         return method518(var0) ? var0 : -1;
      }
   }

   private static int method650(int var0) {
      if (method159(var0)) {
         return 29 + var0;
      } else {
         return method518(var0) ? 2 + (var0 - 9) : -1;
      }
   }

   private static int method651(int var0) {
      if (method159(var0)) {
         return 32 + var0;
      } else {
         return method518(var0) ? 5 + (var0 - 9) : -1;
      }
   }

   private static int method652(int var0) {
      if (method159(var0)) {
         return 30 + var0;
      } else {
         return method518(var0) ? 3 + (var0 - 9) : -1;
      }
   }

   private static int method653(int var0) {
      if (method159(var0)) {
         return 28 + var0;
      } else {
         return method518(var0) ? 1 + (var0 - 9) : -1;
      }
   }

   private static int method654(int var0) {
      if (method159(var0)) {
         return 30 + var0;
      } else {
         return method518(var0) ? 3 + (var0 - 9) : -1;
      }
   }

   private static int method655(int var0) {
      if (method159(var0)) {
         return 32 + var0;
      } else {
         return method518(var0) ? 5 + (var0 - 9) : -1;
      }
   }

   private static int method656(ScreenHandler var0, int var1) {
      AbstractHorseEntity var5 = ((HorseScreenHandlerAccessor)var0).getEntity();
      if (var5 instanceof LlamaEntity) {
         int var6 = ((LlamaEntity)var5).getStrength();
         if (method159(var1)) {
            return 2 + 3 * var6 + 28 + var1;
         }

         if (method518(var1)) {
            return 2 + 3 * var6 + 1 + (var1 - 9);
         }
      } else if (!(var5 instanceof HorseEntity) && !(var5 instanceof SkeletonHorseEntity) && !(var5 instanceof ZombieHorseEntity)) {
         if (var5 instanceof AbstractDonkeyEntity) {
            boolean var7 = ((AbstractDonkeyEntity)var5).hasChest();
            if (method159(var1)) {
               return (var7 ? 44 : 29) + var1;
            }

            if (method518(var1)) {
               return (var7 ? 17 : 2) + (var1 - 9);
            }
         }
      } else {
         if (method159(var1)) {
            return 29 + var1;
         }

         if (method518(var1)) {
            return 2 + (var1 - 9);
         }
      }

      return -1;
   }

   private static int method657(int var0) {
      if (method159(var0)) {
         return 30 + var0;
      } else {
         return method518(var0) ? 3 + (var0 - 9) : -1;
      }
   }

   private static int method658(int var0) {
      if (method159(var0)) {
         return 30 + var0;
      } else {
         return method518(var0) ? 3 + (var0 - 9) : -1;
      }
   }

   private static int method659(int var0) {
      if (method159(var0)) {
         return 31 + var0;
      } else {
         return method518(var0) ? 4 + (var0 - 9) : -1;
      }
   }

   private static int method2010() {
      return -1;
   }

   private static int method660(int var0) {
      if (method159(var0)) {
         return 31 + var0;
      } else {
         return method518(var0) ? 4 + (var0 - 9) : -1;
      }
   }

   private static int method661(int var0) {
      if (method159(var0)) {
         return 29 + var0;
      } else {
         return method518(var0) ? 2 + (var0 - 9) : -1;
      }
   }

   public static boolean method159(int i) {
      return i >= 0 && i <= 8;
   }

   public static boolean method518(int i) {
      return i >= 9 && i <= 35;
   }

   public static boolean method662(int i) {
      return i >= 36 && i <= 39;
   }

   public static Vec2f method663(HandledScreenAccessor original, Slot slot) {
      return original != null
         ? new Vec2f((float)(original.getX() + slot.x) + 8.0F, (float)(original.getY() + slot.y) + 8.0F)
         : new Vec2f((float)slot.x + 8.0F, (float)slot.y + 8.0F);
   }

   public static List<Slot> method664(ScreenHandler screenHandler) {
      return (List<Slot>)screenHandler.slots.stream().filter(Objects::nonNull).filter(Slot::method_7682).collect(Collectors.toList());
   }

   public static Slot method665(ScreenHandler screenHandler, HandledScreenAccessor original, Vec2f lastMouseClick, BiPredicate<Slot, List<Slot>> block) {
      List var4 = method664(screenHandler);
      return (Slot)var4.stream().filter(SlotUtils::lambda$getClosestSlot$0).min(Comparator.comparingDouble(SlotUtils::lambda$getClosestSlot$1)).orElse(null);
   }

   private static double lambda$getClosestSlot$1(Vec2f var0, HandledScreenAccessor var1, Slot var2) {
      return (double)var0.distanceSquared(method663(var1, var2));
   }

   private static boolean lambda$getClosestSlot$0(BiPredicate var0, List var1, Slot var2) {
      return var0.test(var2, var1);
   }
}
