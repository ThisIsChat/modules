package msg_processor;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import static msg_processor.MessageConstants.*;

/**
 * Класс-обертка над пакетом общего типа.
 * Пока сделаем так, чтобы сервер не парсил внутренние пакеты
 *
 * В базе данных может содержаться идентификатор чат-комнаты
 */

//TODO: а нужна ли вообще эта обертка....
public class GenericPack implements CustomSerializable
{
    MessageConstants start = kStartGenericPack;
    MessageConstants end = kEndGenericPack;

    public String idFrom;
    public String fromName;

    public String idTo;

    public CustomSerializable customSerializable;

    public GenericPack(CustomSerializable customSerializable, String idFrom, String idTo, String fromName)
    {
        this.customSerializable = customSerializable;
        this.fromName = fromName;
        this.idFrom = idFrom;
        this.idTo = idTo;
    }

    public GenericPack() { } //TODO: make it private

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof GenericPack))
            return false;

        GenericPack oth = (GenericPack) obj;

        return (start == oth.start) &&
                (idFrom.equals(oth.idFrom)) &&
                (idTo.equals(oth.idTo)) &&
                (fromName.equals(oth.fromName)) &&
                (customSerializable.equals(oth.customSerializable)) &&
                (end == oth.end);
    }

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();

        try
        {
            byteBuffer.put(start.getValue());

            byteBuffer.putInt(idFrom.length());
            byteBuffer.put(idFrom.getBytes());

            byteBuffer.putInt(fromName.length());
            byteBuffer.put(fromName.getBytes());

            byteBuffer.putInt(idTo.length());
            byteBuffer.put(idTo.getBytes());

            if(!customSerializable.serializeTo(byteBuffer))
                throw new BufferOverflowException();

            byteBuffer.put(end.getValue());
        }
        catch(BufferOverflowException ex)
        {
            byteBuffer.position(beginPosition);
            return false;
        }
        return true;
    }


}
