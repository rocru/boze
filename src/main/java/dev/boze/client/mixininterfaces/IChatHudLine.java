package dev.boze.client.mixininterfaces;

import com.mojang.authlib.GameProfile;

public interface IChatHudLine {
   String getText();

   int boze$getID();

   void boze$setID(int var1);

   GameProfile getSenderProfile();

   void setSenderProfile(GameProfile var1);
}
