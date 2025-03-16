package dev.boze.client.gui.notification;

import dev.boze.client.font.IconManager;
import dev.boze.client.utils.RGBAColor;

public enum Notifications {
   CATEGORY_CLIENT("intermediary"),
   CATEGORY_COMBAT("1"),
   CATEGORY_LEGIT("2"),
   CATEGORY_MISC("3"),
   CATEGORY_MOVEMENT("4"),
   CATEGORY_RENDER("5"),
   VISIBLE_OFF("6"),
   TOGGLE_ON("7"),
   TOGGLE_OFF("8"),
   PRIORITY(":"),
   WARNING(";"),
   VIEW_LIST("A"),
   POWER("B"),
   TUNE("C"),
   EXPAND_MORE("D"),
   EXPAND_LESS("E"),
   ADD("F"),
   PLAYERS_ADD("G"),
   PLAYERS_REMOVE("H"),
   PLAYERS("I"),
   SAVE("J"),
   DELETE("K"),
   EDIT("L"),
   SHARE("M"),
   SYNC("N"),
   LOCK("O"),
   POPUP("P"),
   DUPLICATE("Q");

   private final String field1736;
   private static final Notifications[] field1737 = initializeValues();

   private Notifications(String var3) {
      this.field1736 = var3;
   }

   public void render(double x, double y, RGBAColor color) {
      IconManager.field1979.drawShadowedText(this.field1736, x, y, color, false);
   }

   public double method2091() {
      return IconManager.field1979.method501(this.field1736);
   }

   public double method1614() {
      return IconManager.field1979.method1390();
   }

   private static Notifications[] initializeValues() {
      return new Notifications[]{
         CATEGORY_CLIENT,
         CATEGORY_COMBAT,
         CATEGORY_LEGIT,
         CATEGORY_MISC,
         CATEGORY_MOVEMENT,
         CATEGORY_RENDER,
         VISIBLE_OFF,
         TOGGLE_ON,
         TOGGLE_OFF,
         PRIORITY,
         WARNING,
         VIEW_LIST,
         POWER,
         TUNE,
         EXPAND_MORE,
         EXPAND_LESS,
         ADD,
         PLAYERS_ADD,
         PLAYERS_REMOVE,
         PLAYERS,
         SAVE,
         DELETE,
         EDIT,
         SHARE,
         SYNC,
         LOCK,
         POPUP,
         DUPLICATE
      };
   }
}
