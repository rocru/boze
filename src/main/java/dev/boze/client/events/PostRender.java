package dev.boze.client.events;

public class PostRender {
    private static final PostRender field1942 = new PostRender();
    public float field1943;

    public static PostRender method1086(float tickDelta) {
        field1942.field1943 = tickDelta;
        return field1942;
    }
}
