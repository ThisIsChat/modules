package msg_processor;

public enum MessageConstants
{
    kStartGenericPack((byte)0x33),
    kEndGenericPack((byte)0x22),

    kStartMessage((byte)0x55),
    kEndMessage((byte)0xaa),

    kLoginPasswordRequest((byte)0x01),
    kLoginPasswordAnswer((byte)0x02),

    kAuthentificationStatus((byte)0x03),
    kAuthentificationOk((byte)0x04),
    kAuthentificationFail((byte)0x05),
    kUserWithSameLoginExists((byte)0x45),
    kAuthentificationNotDefined((byte)0x06),

    kRegistration((byte) 0x07),

    kTextMessage((byte) 0x8),

    kSearchPerson((byte) 0x9),
    kAnswerForSearchPerson((byte) 0xa);

    private byte value;

    MessageConstants(byte value)
    {
        this.value = value;
    }

    public byte getValue()
    {
        return value;
    }

    public void setValue(byte value)
    {
        this.value = value;
    }

    public boolean equals(MessageConstants constant)
    {
        return value == constant.value;
    }

}
