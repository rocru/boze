package dev.boze.client.instances.impl;

import dev.boze.api.internal.interfaces.IFriends;
import dev.boze.client.systems.modules.client.Friends;

public class FriendsInstance implements IFriends {
   public boolean isFriend(String name) {
      return Friends.method346(name);
   }

   public boolean addFriend(String name) {
      return Friends.addFriend(name);
   }

   public void delFriend(String friend) {
      Friends.method1750(friend);
   }
}
