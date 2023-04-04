package msg_processor;

import archive_none_delivered_message.NoneDeliveredMessageController;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Message_SerializationTest
{
    private void createFile(final String pathToFile) throws IOException
    {
        if(Files.exists(Paths.get(pathToFile)))
            Files.delete(Paths.get(pathToFile));
        Files.createFile(Paths.get(pathToFile));
    }

    @Test
    void noneDeliveredMessage()
    {
        String login = "@1234.abra_cadabra!";
        String password = "12345qwerty";

        LoginPasswordAnswerMessage message_for_serialization = new LoginPasswordAnswerMessage(login, password);
        GenericPack msg = new GenericPack(message_for_serialization, "12345", "54328", "Man");

        NoneDeliveredMessageController controller = new NoneDeliveredMessageController("src/test/java/msg_processor/tmp/noneDeliveredMessages");
        controller.clear();
        controller.addMessage(msg);

        List<GenericPack> allMessages = controller.getAllMessages();

        if(allMessages.size() == 1)
            assertTrue(msg.equals(allMessages.get(0)));
        else
            fail();
    }

    @Test
    void noneDeliveredMessageCount()
    {
        String login = "@1234.abra_cadabra!";
        String password = "12345qwerty";

        LoginPasswordAnswerMessage message_for_serialization = new LoginPasswordAnswerMessage(login, password);
        GenericPack msg = new GenericPack(message_for_serialization, "12345", "54328", "Man");

        NoneDeliveredMessageController controller = new NoneDeliveredMessageController("src/test/java/msg_processor/tmp/noneDeliveredMessages");
        controller.clear();
        controller.addMessage(msg);

        msg = new GenericPack(message_for_serialization, "12345", "54328", "BlaBla");
        controller.addMessage(msg);

        List<GenericPack> allMessages = controller.getAllMessages();

        assertTrue(allMessages.size() == 2);

    }

    @Test
    void login_password_request_message() throws IOException
    {
        LoginPasswordRequestMessage message_for_serialization = new LoginPasswordRequestMessage();
        GenericPack generic_for_serialization = new GenericPack(message_for_serialization, "12345", "54328", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");

        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(128);
            generic_for_serialization.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }



        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack message_for_de_serialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(128);
            f_o_s.read(b_b);
            b_b.rewind();

            message_for_de_serialization = Decoder.decodeGenericPack(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(generic_for_serialization.equals(message_for_de_serialization));
    }


    @Test
    void login_password_answer_message() throws IOException
    {
        String login = "@1234.abra_cadabra!";
        String password = "12345qwerty";

        LoginPasswordAnswerMessage message_for_serialization = new LoginPasswordAnswerMessage(login, password);
        GenericPack generick_for_serialization = new GenericPack(message_for_serialization, "12345", "54328", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            generick_for_serialization.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack message_for_de_serialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();


            message_for_de_serialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(generick_for_serialization.equals(message_for_de_serialization));
    }

    @Test
    void authentification_status_test() throws IOException
    {
        AuthentificationStatus aut_status = new AuthentificationStatus(MessageConstants.kAuthentificationOk, "Petr", "Petrov" );
        GenericPack generick_for_serialization = new GenericPack(aut_status, "12345", "54328", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            generick_for_serialization.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack message_for_de_serialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();


            message_for_de_serialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(generick_for_serialization.equals(message_for_de_serialization));
    }

    @Test
    void registration_test() throws IOException
    {
        Registration registration = new Registration("Petr", "Sidorov", "petya", "qwerty1234", "1234");
        GenericPack generick_for_serialization = new GenericPack(registration, "", "", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            generick_for_serialization.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack message_for_de_serialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();


            message_for_de_serialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(generick_for_serialization.equals(message_for_de_serialization));
    }

    @Test
    void text_msg_test() throws IOException
    {
        TextMessage registration = new TextMessage("Hello, guy");
        GenericPack generic_for_serialization = new GenericPack(registration, "1", "2", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            generic_for_serialization.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack message_for_de_serialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();


            message_for_de_serialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(generic_for_serialization.equals(message_for_de_serialization));
    }

    @Test
    void searchPerson_test() throws IOException
    {
        SearchPerson searchPerson = new SearchPerson("900");
        GenericPack genericPack = new GenericPack(searchPerson, "1", "2", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            genericPack.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack messageForDeSerialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();
            messageForDeSerialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(genericPack.equals(messageForDeSerialization));
    }

    @Test
    void searchPersonAnswer_test() throws IOException
    {
        SearchPersonAnswer searchPerson = new SearchPersonAnswer("id5", "Straus", "Pankratov", "5511234");
        GenericPack genericPack = new GenericPack(searchPerson, "", "2", "Man");
        String file_name = new String("src/test/java/msg_processor/tmp/file.tmp");
        createFile(file_name);

        try(
                FileChannel f_o_s = (FileChannel) Files.newByteChannel(Path.of(file_name), StandardOpenOption.WRITE);
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            genericPack.serializeTo(b_b);
            b_b.rewind();
            f_o_s.write(b_b);
        }
        catch (IOException e)
        {
            fail();
            return;
        }

        GenericPack messageForDeSerialization = null;

        try(
                SeekableByteChannel f_o_s = Files.newByteChannel(Path.of(file_name));
        )
        {
            ByteBuffer b_b = ByteBuffer.allocate(256);
            f_o_s.read(b_b);
            b_b.rewind();
            messageForDeSerialization = Decoder.decodeGenericPack(b_b);

        }
        catch (IOException e)
        {
            fail();
            return;
        }

        assertTrue(genericPack.equals(messageForDeSerialization));
    }
}


