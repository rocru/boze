package de.florianmichael.waybackauthlib;

class AuthenticationRequest {
    public Agent field221;
    public String field222;
    public String field223;
    public String field224;
    private final boolean field225;

    protected AuthenticationRequest(Agent agent, String username, String password, String clientToken) {
        this.field221 = agent;
        this.field222 = username;
        this.field223 = password;
        this.field224 = clientToken;
        this.field225 = true;
    }
}
