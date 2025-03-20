package dev.boze.client.utils.http;

import dev.boze.client.core.ErrorLogger;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.stream.Stream;

public class HttpRequestWrapper {
    private Builder field3943;
    private RequestType field3944;

    public HttpRequestWrapper(RequestType method, String url) {
        try {
            this.field3943 = HttpRequest.newBuilder().uri(new URI(url)).header("User-Agent", "Boze");
            this.field3944 = method;
        } catch (URISyntaxException var6) {
            ErrorLogger.log(var6);
        }
    }

    public HttpRequestWrapper method2178(String token) {
        this.field3943.header("Authorization", "Bearer " + token);
        return this;
    }

    public HttpRequestWrapper method2179(String name, String value) {
        this.field3943.header(name, value);
        return this;
    }

    public HttpRequestWrapper method2180(String string) {
        this.field3943.header("Content-Type", "text/plain");
        this.field3943.method(this.field3944.name(), BodyPublishers.ofString(string));
        this.field3944 = null;
        return this;
    }

    public HttpRequestWrapper method2181(String string) {
        this.field3943.header("Content-Type", "application/x-www-form-urlencoded");
        this.field3943.method(this.field3944.name(), BodyPublishers.ofString(string));
        this.field3944 = null;
        return this;
    }

    public HttpRequestWrapper method2182(String string) {
        this.field3943.header("Content-Type", "application/json");
        this.field3943.method(this.field3944.name(), BodyPublishers.ofString(string));
        this.field3944 = null;
        return this;
    }

    public HttpRequestWrapper method2183(Object object) {
        this.field3943.header("Content-Type", "application/json");
        this.field3943.method(this.field3944.name(), BodyPublishers.ofString(HttpUtil.field3946.toJson(object)));
        this.field3944 = null;
        return this;
    }

    private <T> T method2184(String var1, BodyHandler<T> var2) {
        this.field3943.header("Accept", var1);
        if (this.field3944 != null) {
            this.field3943.method(this.field3944.name(), BodyPublishers.noBody());
        }

        try {
            HttpResponse var5 = HttpUtil.field3945.send(this.field3943.build(), var2);
            return (T) (var5.statusCode() == 200 ? var5.body() : null);
        } catch (InterruptedException | IOException var6) {
            ErrorLogger.log(var6);
            return null;
        }
    }

    public void method2185() {
        this.method2184("*/*", BodyHandlers.discarding());
    }

    public InputStream method2186() {
        return this.method2184("*/*", BodyHandlers.ofInputStream());
    }

    public String method2187() {
        return this.method2184("*/*", BodyHandlers.ofString());
    }

    public Stream<String> method2188() {
        return this.method2184("*/*", BodyHandlers.ofLines());
    }

    public <T> T method2189(Type type) {
        InputStream var4 = this.method2184("application/json", BodyHandlers.ofInputStream());
        return var4 == null ? null : HttpUtil.field3946.fromJson(new InputStreamReader(var4), type);
    }
}
