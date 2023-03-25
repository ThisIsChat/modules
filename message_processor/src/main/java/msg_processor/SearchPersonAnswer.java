package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class SearchPersonAnswer extends BasePack implements CustomSerializable
{
    private String id;
    private String name;
    private String surname;
    private String phoneNumber;

    public final String getId() {return id;}
    public final String getName() {return name;}
    public final String getSurname() {return surname;}
    public final String getPhoneNumber() {return phoneNumber;}

    {
        super.messageType = MessageConstants.kAnswerForSearchPerson;
    }

    public static SearchPersonAnswer convert(CustomSerializable obj)
    {
        if(obj instanceof SearchPersonAnswer)
            return (SearchPersonAnswer) obj;

        return null;
    }

    public SearchPersonAnswer()
    {
        this.id = "";
        this.name = "";
        this.surname = "";
        this.phoneNumber = "";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof SearchPersonAnswer))
            return false;

        SearchPersonAnswer oth = (SearchPersonAnswer) obj;

        return (id.equals(oth.id)) &&
                (name.equals(oth.name)) &&
                (surname.equals(oth.surname)) &&
                (phoneNumber.equals(oth.phoneNumber));
    }

    public SearchPersonAnswer(final String id,
                            final String name,
                            final String surname,
                            final String phoneNumber)
    {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());

            byteBuffer.putInt(id.length());
            byteBuffer.put(id.getBytes());

            byteBuffer.putInt(name.length());
            byteBuffer.put(name.getBytes());

            byteBuffer.putInt(surname.length());
            byteBuffer.put(surname.getBytes());

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

    public static SearchPersonAnswer deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        SearchPersonAnswer returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new SearchPersonAnswer();

            int idLength = byteBuffer.getInt();
            byte[] idBytes = new byte[idLength];
            byteBuffer.get(idBytes, 0, idLength);
            returnValue.id = new String(idBytes);

            int nameLength = byteBuffer.getInt();
            byte[] nameBytes = new byte[nameLength];
            byteBuffer.get(nameBytes, 0, nameLength);
            returnValue.name = new String(nameBytes);

            int surnameLength = byteBuffer.getInt();
            byte[] surnameBytes = new byte[surnameLength];
            byteBuffer.get(surnameBytes, 0, surnameLength);
            returnValue.surname = new String(surnameBytes);

            int phoneLength = byteBuffer.getInt();
            byte[] phoneBytes = new byte[phoneLength];
            byteBuffer.get(phoneBytes, 0, phoneLength);
            returnValue.phoneNumber = new String(phoneBytes);

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
}
