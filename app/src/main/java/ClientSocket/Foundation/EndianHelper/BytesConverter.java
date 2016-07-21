package ClientSocket.Foundation.EndianHelper;

public class BytesConverter
{
    //单字节可无视大小端
    public static byte[] byteToBytes(Byte i)
    {
        byte[] abyte = new byte[1];

        abyte[0] = (byte) (0xff & i);

        return abyte;
    }

    public static byte bytesToByte(byte[] bytes) {
        int addr = 0;
        addr = bytes[0] & 0xFF;
        return (byte)addr;
    }

    //小端转换
    public static byte[] littleShortToBytes(short i)
    {
        byte[] abyte = new byte[2];

        abyte[0] = (byte) (0xff & i);
        abyte[1] = (byte) ((0xff00 & i) >> 8);

        return abyte;
    }

    public static byte[] littleIntToBytes(int i)
    {
        byte[] abyte = new byte[4];

        abyte[0] = (byte) (0xff & i);
        abyte[1] = (byte) ((0xff00 & i) >> 8);
        abyte[2] = (byte) ((0xff0000 & i) >> 16);
        abyte[3] = (byte) ((0xff000000 & i) >> 24);

        return abyte;
    }

    public static short bytesToLittleShort(byte[] bytes) {
        int addr = 0;

        addr = bytes[0] & 0xFF;
        addr |= (((int) bytes[1] << 8) & 0xFF00);

        return (short)addr;
    }

    public static int bytesToLittleInt(byte[] bytes) {
        int addr = 0;

        addr = bytes[0] & 0xFF;
        addr |= (((int) bytes[1] << 8) & 0xFF00);
        addr |= (((int) bytes[2] << 16) & 0xFF0000);
        addr |= (((int) bytes[3] << 24) & 0xFF000000);

        return addr;
    }

    //大端转换
    public static byte[] bigShortToBytes(short i) {
        byte[] abyte = new byte[2];

        abyte[0] = (byte) ((i >>> 8) & 0xff);
        abyte[1] = (byte) (i & 0xff);

        return abyte;
    }

    public static byte[] bigIntToBytes(int i) {
        byte[] abyte = new byte[4];

        abyte[0] = (byte) ((i >>> 24) & 0xff);
        abyte[1] = (byte) ((i >>> 16) & 0xff);
        abyte[2] = (byte) ((i >>> 8) & 0xff);
        abyte[3] = (byte) (i & 0xff);

        return abyte;
    }

    public static short bytesToBigShort(byte[] bytes) {
        int addr = 0;

        addr = bytes[0] & 0xFF;
        addr = (addr << 8) | (bytes[1] & 0xff);

        return (short)addr;
    }

    public static int bytesToBigInt(byte[] bytes) {
        int addr = 0;

        addr = bytes[0] & 0xFF;
        addr = (addr << 8) | (bytes[1] & 0xff);
        addr = (addr << 8) | (bytes[2] & 0xff);
        addr = (addr << 8) | (bytes[3] & 0xff);

        return addr;
    }
}
