package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class SearchPerson extends BasePack implements CustomSerializable
{
    private String phoneNumber;

    {
        super.messageType = MessageConstants.kSearchPerson;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public SearchPerson(final String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    public static SearchPerson convert(CustomSerializable obj)
    {
        if(obj instanceof SearchPerson)
            return (SearchPerson) obj;

        return null;
    }

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());

            byteBuffer.putInt(phoneNumber.length());
            byteBuffer.put(phoneNumber.getBytes());

            byteBuffer.put(endPack.getValue());
        }
        catch(BufferOverflowException ex)
        {
            byteBuffer.position(beginPosition);
            return false;
        }

        return true;
    }

    public static SearchPerson deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        SearchPerson returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new SearchPerson("");

            int msgLength = byteBuffer.getInt();
            byte[] msgBytes = new byte[msgLength];
            byteBuffer.get(msgBytes, 0, msgLength);
            returnValue.phoneNumber = new String(msgBytes);

            byteBuffer.get(); // Читаем замыкающий байт
        }
        catch(BufferUnderflowException ex)
        {
            byteBuffer.position(beginPosition);
            returnValue = null;
        }
        catch(IndexOutOfBoundsException ex)
        {
            byteBuffer.position(beginPosition);
            returnValue = null;
        }
        finally
        {
            return returnValue;
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof SearchPerson))
            return false;

        SearchPerson oth = (SearchPerson) obj;

        return phoneNumber.equals(oth.phoneNumber);
    }
}
