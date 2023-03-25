package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class TextMessage extends BasePack implements CustomSerializable
{
    public String message;

    {
        super.messageType = MessageConstants.kTextMessage;
    }

    public TextMessage(String text_message)
    {
        message = text_message;
    }

    public static TextMessage convert(CustomSerializable obj)
    {
        if(obj instanceof TextMessage)
            return (TextMessage) obj;

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

            byteBuffer.putInt(message.length());
            byteBuffer.put(message.getBytes());

            byteBuffer.put(endPack.getValue());
        }
        catch(BufferOverflowException ex)
        {
            byteBuffer.position(beginPosition);
            return false;
        }

        return true;
    }

    public static TextMessage deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        TextMessage returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new TextMessage("");

            int msgLength = byteBuffer.getInt();
            byte[] msgBytes = new byte[msgLength];
            byteBuffer.get(msgBytes, 0, msgLength);
            returnValue.message = new String(msgBytes);

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
        if(!(obj instanceof TextMessage))
            return false;

        TextMessage textMessage = (TextMessage) obj;

        return (message.equals(textMessage.message));
    }
}
