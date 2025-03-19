package de.florianmichael.waybackauthlib;

class InvalidateRequest {
    public String field226;
    public String field227;

    protected InvalidateRequest(String clientToken, String accessToken) {
        this.field226 = clientToken;
        this.field227 = accessToken;
    }
}
