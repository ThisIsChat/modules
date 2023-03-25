package msg_processor;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class AuthentificationStatus extends BasePack implements CustomSerializable
{
    MessageConstants authentificationStatus;

    public String name;
    public String surname;

    {
        super.messageType = MessageConstants.kAuthentificationStatus;
    }

    public MessageConstants getStatus()
    {
        return authentificationStatus;
    }

    public AuthentificationStatus(MessageConstants authentification_status, final String name, final String surname)
    {
        authentificationStatus = authentification_status;
        setSurname(surname);
        setName(name);
    }

    public AuthentificationStatus(final String name, final String surname)
    {
        authentificationStatus = MessageConstants.kAuthentificationNotDefined;
        setSurname(surname);
        setName(name);
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public void setSurname(final String surname)
    {
        this.surname = surname;
    }

    public static AuthentificationStatus convert(CustomSerializable obj)
    {
        if(obj instanceof AuthentificationStatus)
            return (AuthentificationStatus) obj;

        return null;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof AuthentificationStatus))
            return false;

        AuthentificationStatus oth = (AuthentificationStatus) obj;

        return (beginPack == oth.beginPack) &&
                (messageType == oth.messageType) &&
                (name.equals(oth.name)) &&
                (surname.equals(oth.surname)) &&
                (authentificationStatus.equals(oth.authentificationStatus)) &&
                (endPack == oth.endPack);
    }

    static public AuthentificationStatus deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        AuthentificationStatus returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new AuthentificationStatus("", "");
            returnValue.authentificationStatus.setValue(byteBuffer.get());

            int nameLength = byteBuffer.getInt();
            byte[] nameBytes = new byte[nameLength];
            byteBuffer.get(nameBytes, 0, nameLength);
            returnValue.name = new String(nameBytes);

            int surnameLength = byteBuffer.getInt();
            byte[] surnameBytes = new byte[surnameLength];
            byteBuffer.get(surnameBytes, 0, surnameLength);
            returnValue.surname = new String(surnameBytes);

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

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());
            byteBuffer.put(authentificationStatus.getValue());
            byteBuffer.putInt(name.length());
            byteBuffer.put(name.getBytes());
            byteBuffer.putInt(surname.length());
            byteBuffer.put(surname.getBytes());
            byteBuffer.put(endPack.getValue());
        }
        catch(BufferOverflowException ex)
        {
            byteBuffer.position(beginPosition);
            return false;
        }
        return true;
    }
}
