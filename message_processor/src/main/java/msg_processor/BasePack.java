package msg_processor;

public class BasePack
{
    MessageConstants beginPack;
    MessageConstants messageType;
    MessageConstants endPack;

    {
        beginPack = MessageConstants.kStartMessage;
        endPack = MessageConstants.kEndMessage;
    }
}
