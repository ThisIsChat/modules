package archive_none_delivered_message;

import msg_processor.Decoder;
import msg_processor.GenericPack;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class NoneDeliveredMessageController
{
    private File archive;
    private Logger logger;
    final String expansion;

    {
        logger = Logger.getLogger(NoneDeliveredMessageController.class.getName());
        expansion = ".msg";
    }

    public void clear()
    {
        File[] fileList = archive.listFiles();

        for(int i=0; i < fileList.length; ++i)
            fileList[i].delete();
    }

    public NoneDeliveredMessageController(final String pathToArchive)
    {
        archive = new File(pathToArchive);
        if(!archive.exists())
            archive.mkdir();
    }

    public void addMessage(final GenericPack msg)
    {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);
        msg.serializeTo(byteBuffer);

        File file = createNewFile();

        if(file == null)
        {
            logger.info("Can't create file " + file.getAbsolutePath());
            return;
        }

        try(FileChannel  channel = new FileOutputStream(file, false).getChannel())
        {
            int bytesToWrite = byteBuffer.position();
            byteBuffer.rewind();
            channel.write(byteBuffer.slice(0, bytesToWrite));
            byteBuffer.compact();
            byteBuffer.position(0);
        }
        catch (FileNotFoundException e)
        {
            logger.info("Can't find file " + file.getAbsolutePath());
        }
        catch (IOException e)
        {
            logger.info("Can't write to " + file.getAbsolutePath());
        }
    }

    private synchronized File createNewFile()
    {
        String fileName = null;

        File[] fileList = archive.listFiles();
        for(int i = 0; i < fileList.length; ++i)
        {
            fileName = Integer.toString(i) + expansion;

            String f = fileList[i].getName();

            if(!f.equals(fileName))
                break;

            fileName = null;
        }

        if(fileName == null)
            fileName = Integer.toString(fileList.length) + expansion;

        File file = new File(archive.getPath() + "/" + fileName);

        if(!file.exists())
        {
            try
            {
                file.createNewFile();
            }
            catch (IOException e)
            {
                file = null;
            }
        }

        return file;
    }

    public List<GenericPack> getAllMessages()
    {
        File[] fileList = archive.listFiles();

        List<GenericPack> msgList = new LinkedList<>();

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(8192);

        for(int i = 0; i < fileList.length; ++i)
        {
            if(fileList[i].isDirectory())
                continue;

            if(!fileList[i].getName().contains(expansion))
                continue;

            try(FileChannel  channel = new FileInputStream(fileList[i]).getChannel())
            {
                byteBuffer.rewind();
                int bytesCount = channel.read(byteBuffer);

                if(bytesCount > 0)
                {
                    byteBuffer.rewind();
                    GenericPack msg = Decoder.decodeGenericPack(byteBuffer);
                    if(msg != null)
                        msgList.add(msg);
                }
            }
            catch (FileNotFoundException e)
            {
                logger.info("Can't find file " + fileList[i].getAbsolutePath());
            }
            catch (IOException e)
            {
                logger.info("Can't write to " + fileList[i].getAbsolutePath());
            }

        }

        return msgList;
    }
}
