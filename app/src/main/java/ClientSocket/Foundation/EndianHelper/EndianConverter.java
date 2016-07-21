package ClientSocket.Foundation.EndianHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class EndianConverter
{
    public static int littleIntToBig(int i)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.asIntBuffer().put(i);

        buffer.order(ByteOrder.BIG_ENDIAN);

        return buffer.getInt();
    }

    public static short littleShortToBig(short i)
    {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.asShortBuffer().put(i);

        buffer.order(ByteOrder.BIG_ENDIAN);

        return buffer.getShort();
    }

    public static int bigIntToLittle(int i)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);

        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asIntBuffer().put(i);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        return buffer.getInt();
    }

    public static short bigShortToLittle(short i)
    {
        ByteBuffer buffer = ByteBuffer.allocate(2);

        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.asShortBuffer().put(i);

        buffer.order(ByteOrder.LITTLE_ENDIAN);

        return buffer.getShort();
    }
}