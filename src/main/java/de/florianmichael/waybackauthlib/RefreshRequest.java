package de.florianmichael.waybackauthlib;

import com.mojang.authlib.GameProfile;

class RefreshRequest {
    public String field228;
    public String field229;
    public GameProfile field230;
    public boolean field231;

    protected RefreshRequest(String clientToken, String accessToken, GameProfile selectedProfile) {
        this.field228 = clientToken;
        this.field229 = accessToken;
        this.field230 = selectedProfile;
        this.field231 = true;
    }
}
