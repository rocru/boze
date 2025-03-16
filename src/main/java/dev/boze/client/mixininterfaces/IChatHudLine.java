package dev.boze.client.mixininterfaces;

import com.mojang.authlib.GameProfile;

public interface IChatHudLine {
   String boze$getText();

   int boze$getID();

   void boze$setID(int var1);

   GameProfile boze$getSenderProfile();

   void boze$setSenderProfile(GameProfile var1);
}
