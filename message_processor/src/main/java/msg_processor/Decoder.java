package msg_processor;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import static msg_processor.MessageConstants.*;

public class Decoder
{
    static public GenericPack decodeGenericPack(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        GenericPack returnPack = null;

        try {
            if(kStartGenericPack.getValue() == byteBuffer.get())
            {
                returnPack = new GenericPack();

                int idFromLength = byteBuffer.getInt();
                byte[] arrayIdFrom = new byte[idFromLength];
                byteBuffer.get(arrayIdFrom, 0, idFromLength);
                returnPack.idFrom = new String(arrayIdFrom);

                int nameFromLength = byteBuffer.getInt();
                byte[] arrayNameFrom = new byte[nameFromLength];
                byteBuffer.get(arrayNameFrom, 0, nameFromLength);
                returnPack.fromName = new String(arrayNameFrom);

                int idToLength = byteBuffer.getInt();
                byte[] arrayIdTo = new byte[idToLength];
                byteBuffer.get(arrayIdTo, 0, idToLength);
                returnPack.idTo = new String(arrayIdTo);

                returnPack.customSerializable = decodePack(byteBuffer);
                if(returnPack.customSerializable == null)
                    throw new BufferUnderflowException();

                byteBuffer.get(); // Последний элемент
            }
        }
        catch (BufferUnderflowException ex)
        {
            byteBuffer.position(beginPosition);
            returnPack = null;
        }
        finally
        {
            return returnPack;
        }
    }

    static public CustomSerializable decodePack(ByteBuffer byteBuffer)
    {
        int beginPosition = byteBuffer.position();
        CustomSerializable returnPack = null;

        try
        {
            if(kStartMessage.getValue() != byteBuffer.get())
                return returnPack;

            byte messageType = byteBuffer.get();

                if(messageType == kLoginPasswordRequest.getValue())
                {
                    returnPack = decodeLoginPasswordRequestMessage(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();

                }
                else if (messageType == kLoginPasswordAnswer.getValue())
                {
                    returnPack = decodeLoginPasswordAnswerMessage(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }
                else if(messageType == kAuthentificationStatus.getValue())
                {
                    returnPack = decodeAuthentificationStatus(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }
                else if(messageType == kRegistration.getValue())
                {
                    returnPack = decodeRegistration(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }
                else if (messageType == kTextMessage.getValue())
                {
                    returnPack = decodeTextMessage(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }
                else if (messageType == kSearchPerson.getValue())
                {
                    returnPack = decodeSearchPerson(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }
                else if (messageType == kAnswerForSearchPerson.getValue())
                {
                    returnPack = decodeSearchPersonAnswer(byteBuffer);
                    if(returnPack == null)
                        throw new BufferUnderflowException();
                }

        }
        catch (BufferUnderflowException ex)
        {
            byteBuffer.position(beginPosition);
            returnPack = null;
        }
        finally
        {
            return returnPack;
        }
    }

    private static SearchPerson decodeSearchPerson(ByteBuffer byteBuffer)
    {
        return SearchPerson.deSerializeFrom(byteBuffer);
    }

    private static SearchPersonAnswer decodeSearchPersonAnswer(ByteBuffer byteBuffer)
    {
        return SearchPersonAnswer.deSerializeFrom(byteBuffer);
    }

    private static TextMessage decodeTextMessage(ByteBuffer byteBuffer)
    {
        return TextMessage.deSerializeFrom(byteBuffer);
    }

    public static LoginPasswordAnswerMessage decodeLoginPasswordAnswerMessage(ByteBuffer byteBuffer)
    {
        return LoginPasswordAnswerMessage.deSerializeFrom(byteBuffer);
    }

    public static LoginPasswordRequestMessage decodeLoginPasswordRequestMessage(ByteBuffer byteBuffer)
    {
        return LoginPasswordRequestMessage.deSerializeFrom(byteBuffer);
    }

    public static AuthentificationStatus decodeAuthentificationStatus(ByteBuffer byteBuffer)
    {
        return AuthentificationStatus.deSerializeFrom(byteBuffer);
    }

    public static Registration decodeRegistration(ByteBuffer byteBuffer)
    {
        return Registration.deSerializeFrom(byteBuffer);
    }
}
