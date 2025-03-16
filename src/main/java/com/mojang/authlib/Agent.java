package com.mojang.authlib;

class Agent {
   public static final Agent field1 = new Agent("Minecraft", 1);
   public String field2;
   public int field3;

   protected Agent(String name, int version) {
      this.field2 = name;
      this.field3 = version;
   }
}
