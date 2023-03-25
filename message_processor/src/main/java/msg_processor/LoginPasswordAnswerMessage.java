package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

//TODO:private?
public class LoginPasswordAnswerMessage extends BasePack implements CustomSerializable
{
    public String login; //TODO: make it private
    public String password;   //TODO: make it private

    {
        super.messageType = MessageConstants.kLoginPasswordAnswer;
    }

    public LoginPasswordAnswerMessage() { }

    public static LoginPasswordAnswerMessage convert(CustomSerializable obj)
    {
        if(obj instanceof LoginPasswordAnswerMessage)
            return (LoginPasswordAnswerMessage) obj;

        return null;
    }

    public LoginPasswordAnswerMessage(String login, String password)
    {
        this.login = new String(login);
        this.password = new String(password);
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof LoginPasswordAnswerMessage))
            return false;

        LoginPasswordAnswerMessage oth = (LoginPasswordAnswerMessage) obj;

        return (beginPack.equals(oth.beginPack)) &&
                (login.equals(oth.login)) &&
                (password.equals(oth.password)) &&
                (messageType.equals(oth.messageType)) &&
                (endPack.equals(oth.endPack));
    }

    public static LoginPasswordAnswerMessage deSerializeFrom(ByteBuffer byteBuffer)
    {
        // Здесь, скоррее всего byteBuffer на позиции после проверки типа данных
        LoginPasswordAnswerMessage returnValue = null;
        int beginPosition = byteBuffer.position();

        try
        {
            returnValue = new LoginPasswordAnswerMessage();

            int loginLength = byteBuffer.getInt();
            byte[] loginBytes = new byte[loginLength];
            byteBuffer.get(loginBytes, 0, loginLength);
            returnValue.login = new String(loginBytes);

            int passwordLength = byteBuffer.getInt();
            byte[] passwordBytes = new byte[passwordLength];
            byteBuffer.get(passwordBytes, 0, passwordLength);
            returnValue.password = new String(passwordBytes);

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
    public boolean serializeTo(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        try
        {
            byteBuffer.put(beginPack.getValue());
            byteBuffer.put(messageType.getValue());

            byteBuffer.putInt(login.length());
            byteBuffer.put(login.getBytes());

            byteBuffer.putInt(password.length());
            byteBuffer.put(password.getBytes());

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
