package singleton;

public enum EnumSingleton {
    INSTANCE;

    static {
        MessageHolder.messages.add(MessageConst.ENUM_STATIC_BLOCK);
    }

    {
        MessageHolder.messages.add(MessageConst.ENUM_CODE_BLOCK);
    }

    EnumSingleton() {
        MessageHolder.messages.add(MessageConst.ENUM_CONSTRUCTOR);
    }

    public void doWork() {
        MessageHolder.messages.add(MessageConst.ENUM_METHOD);
        int n = 0;
        for (int i = 0; i < 5; i++) {
            n += i;
        }
    }
}
