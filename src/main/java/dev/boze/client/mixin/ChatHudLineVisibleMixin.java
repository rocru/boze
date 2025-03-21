package dev.boze.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.boze.client.mixininterfaces.IChatHudLineVisible;
import net.minecraft.client.gui.hud.ChatHudLine.Visible;
import net.minecraft.text.OrderedText;
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
   public String boze$getText() {
      StringBuilder builder = new StringBuilder();
      this.content.accept((index, style, codePoint) -> {
         builder.appendCodePoint(codePoint);
         return true;
      });
      return builder.toString();
   }

   @Override
   public GameProfile boze$getSenderProfile() {
      return this.sender;
   }

   @Override
   public void boze$setSenderProfile(GameProfile profile) {
      this.sender = profile;
   }

   @Override
   public boolean boze$isFirst() {
      return this.first;
   }

   @Override
   public void boze$setFirst(boolean first) {
      this.first = first;
   }
}
