package meteordevelopment.discordipc.connection;

public enum State {
    Opcode,
    Length,
    Data;

    private static final State[] field1650 = method769();

    private static State[] method769() {
        return new State[]{Opcode, Length, Data};
    }
}
