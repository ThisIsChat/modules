package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

public class Registration extends BasePack implements CustomSerializable
{
    public String name; //TODO: refact
    public String surname;   //TODO: refact
    public String login; //TODO: refact
    public String password;   //TODO: refact
    public String phoneNumber;  //TODO: refact

    {
        super.messageType = MessageConstants.kRegistration;
    }

    public static Registration convert(CustomSerializable obj)
    {
        if(obj instanceof Registration)
            return (Registration) obj;

        return null;
    }

    public Registration()
    {
        name = "";
        surname = "";
        login = "";
        password = "";
        phoneNumber = "";
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Registration))
            return false;

        Registration oth = (Registration) obj;

        return (name.equals(oth.name)) &&
                (surname.equals(oth.surname)) &&
                (login.equals(oth.login)) &&
                (password.equals(oth.password));
    }

    public Registration(String name, String surname, String login, String password, String phoneNumber)
    {
        this.name = new String(name);
        this.surname = new String(surname);
        this.login = new String(login);
        this.password = new String(password);
        this.phoneNumber = new String(phoneNumber);
    }

    @Override
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());

            byteBuffer.putInt(name.length());
            byteBuffer.put(name.getBytes());

            byteBuffer.putInt(surname.length());
            byteBuffer.put(surname.getBytes());

            byteBuffer.putInt(login.length());
            byteBuffer.put(login.getBytes());

            byteBuffer.putInt(password.length());
            byteBuffer.put(password.getBytes());

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

    public static Registration deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        Registration returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new Registration();

            int nameLength = byteBuffer.getInt();
            byte[] nameBytes = new byte[nameLength];
            byteBuffer.get(nameBytes, 0, nameLength);
            returnValue.name = new String(nameBytes);

            int surnameLength = byteBuffer.getInt();
            byte[] surnameBytes = new byte[surnameLength];
            byteBuffer.get(surnameBytes, 0, surnameLength);
            returnValue.surname = new String(surnameBytes);

            int loginLength = byteBuffer.getInt();
            byte[] loginBytes = new byte[loginLength];
            byteBuffer.get(loginBytes, 0, loginLength);
            returnValue.login = new String(loginBytes);

            int passwordLength = byteBuffer.getInt();
            byte[] passwordBytes = new byte[passwordLength];
            byteBuffer.get(passwordBytes, 0, passwordLength);
            returnValue.password = new String(passwordBytes);

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
