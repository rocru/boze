package de.florianmichael.waybackauthlib;

class ValidateRequest {
    public String field242;
    public String field243;

    public ValidateRequest(String accessToken, String clientToken) {
        this.field242 = clientToken;
        this.field243 = accessToken;
    }
}
