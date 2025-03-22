package dev.boze.client.mixin;

import com.mojang.authlib.GameProfile;
import dev.boze.client.mixininterfaces.IChatHudLine;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ChatHudLine.class)
public abstract class ChatHudLineMixin implements IChatHudLine {
    @Shadow
    @Final
    private Text content;
    @Unique
    private GameProfile sender;
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
        return this.content.getString();
    }

    @Override
    public GameProfile boze$getSenderProfile() {
        return this.sender;
    }

    @Override
    public void boze$setSenderProfile(GameProfile sender) {
        this.sender = sender;
    }
}
