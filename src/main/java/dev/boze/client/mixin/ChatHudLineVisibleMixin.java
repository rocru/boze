package dev.boze.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.boze.client.mixininterfaces.IChatHudLineVisible;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({Visible.class})
public abstract class ChatHudLineVisibleMixin implements IChatHudLineVisible {
   @Shadow
   @Final
   private OrderedText content;
   @Unique
   private GameProfile sender;
   @Unique
   private boolean first;
   @Unique
   private int iD;

   @Override
   public int boze$getID() {
      return this.iD;
   }

   @Override
   public void boze$setID(int iD) {
      this.iD = iD;
   }

   @Override
   public String getText() {
      StringBuilder var1 = new StringBuilder();
      this.content.accept(ChatHudLineVisibleMixin::lambda$getText$0);
      return var1.toString();
   }

   @Override
   public GameProfile getSenderProfile() {
      return this.sender;
   }

   @Override
   public void setSenderProfile(GameProfile profile) {
      this.sender = profile;
   }

   @Override
   public boolean isFirst() {
      return this.first;
   }

   @Override
   public void setFirst(boolean first) {
      this.first = first;
   }

   private static boolean lambda$getText$0(StringBuilder var0, int var1, Style var2, int var3) {
      var0.appendCodePoint(var3);
      return true;
   }
}
