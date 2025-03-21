package dev.boze.client.systems.waypoints;

import com.google.gson.JsonObject;
import dev.boze.client.utils.misc.IJsonSerializable2;
import dev.boze.client.utils.misc.ISerializable;
import net.minecraft.nbt.NbtCompound;

import java.util.Objects;

public class WayPoint implements ISerializable<WayPoint>, IJsonSerializable2<WayPoint> {
   public String field908;
   public int field909;
   public int field910;
   public int field911;
   public String field912;
   public String field913;

   public WayPoint() {
   }

   public WayPoint(String name, int x, int y, int z, String dimension, String ip) {
      this.field908 = name;
      this.field909 = x;
      this.field910 = y;
      this.field911 = z;
      this.field912 = dimension;
      this.field913 = ip;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         WayPoint var5 = (WayPoint)o;
         return this.field909 == var5.field909
            && this.field910 == var5.field910
            && this.field911 == var5.field911
            && Objects.equals(this.field908, var5.field908)
            && Objects.equals(this.field912, var5.field912)
            && Objects.equals(this.field913, var5.field913);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.field908, this.field909, this.field910, this.field911, this.field912, this.field913});
   }

   @Override
   public JsonObject serialize() {
      JsonObject var4 = new JsonObject();
      var4.addProperty("name", this.field908);
      var4.addProperty("x", this.field909);
      var4.addProperty("y", this.field910);
      var4.addProperty("z", this.field911);
      var4.addProperty("dimension", this.field912);
      var4.addProperty("ip", this.field913);
      return var4;
   }

   @Override
   public NbtCompound toTag() {
      return null;
   }
   
   
   @Override
   public WayPoint fromTag(NbtCompound nbtCompound) {
      this.field908 = nbtCompound.getString("name");
      this.field909 = nbtCompound.getInt("x");
      this.field910 = nbtCompound.getInt("y");
      this.field911 = nbtCompound.getInt("z");
      this.field912 = nbtCompound.getString("dimension");
      this.field913 = nbtCompound.getString("ip");
      return this;
   }
   
   @Override
   public WayPoint deserialize(JsonObject jsonObject) {
      this.field908 = jsonObject.get("name").getAsString();
      this.field909 = jsonObject.get("x").getAsInt();
      this.field910 = jsonObject.get("y").getAsInt();
      this.field911 = jsonObject.get("z").getAsInt();
      this.field912 = jsonObject.get("dimension").getAsString();
      this.field913 = jsonObject.get("ip").getAsString();
      return this;
   }
}
