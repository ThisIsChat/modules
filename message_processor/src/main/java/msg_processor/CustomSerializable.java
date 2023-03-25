package msg_processor;

import java.nio.ByteBuffer;

public interface CustomSerializable
{
    boolean serializeTo(ByteBuffer byteBuffer);
}
