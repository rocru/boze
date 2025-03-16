package dev.boze.client.utils;

import dev.boze.client.systems.modules.Category;

public class ColorUtil {
   public static RGBAColor generateRandomColor(Category category) {
      return RGBAColor.method189(category.hue + Math.random() * 60.0, category.saturation, 1.0);
   }
}
