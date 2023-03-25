package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class LoginPasswordRequestMessage extends BasePack implements CustomSerializable
{
    {
        super.messageType = MessageConstants.kLoginPasswordRequest;
    }

    public static LoginPasswordRequestMessage convert(CustomSerializable obj)
    {
        if(obj instanceof LoginPasswordRequestMessage)
            return (LoginPasswordRequestMessage) obj;

        return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof LoginPasswordRequestMessage))
        {
            return false;
        }

        LoginPasswordRequestMessage oth = (LoginPasswordRequestMessage) obj;

        return (beginPack.equals(oth.beginPack)) &&
                (messageType.equals(oth.messageType)) &&
                (endPack.equals(oth.endPack));
    }

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());
            byteBuffer.put(endPack.getValue());
        }
        catch(BufferOverflowException ex)
        {
            byteBuffer.position(beginPosition); // Если, вдруг, буфер оказался мал, то откатим каретку назад и вернем false
            return false;
        }
        return true;
    }

    static public LoginPasswordRequestMessage deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        LoginPasswordRequestMessage returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new LoginPasswordRequestMessage();
            byteBuffer.get();
        }
        catch(BufferUnderflowException ex)
        {
            byteBuffer.position(beginPosition);
            returnValue = null;
        }
        finally
        {
            return returnValue;
        }
    }
}
