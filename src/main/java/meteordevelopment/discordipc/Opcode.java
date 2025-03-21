package meteordevelopment.discordipc;

public enum Opcode {
    Handshake,
    Frame,
    Close,
    Ping,
    Pong;

    private static final Opcode[] field1688 = values();

    public static Opcode method807(int i) {
        return field1688[i];
    }

    // $VF: synthetic method
    private static Opcode[] method808() {
        return new Opcode[]{Handshake, Frame, Close, Ping, Pong};
    }
}
