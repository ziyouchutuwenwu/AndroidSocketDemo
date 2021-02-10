package cn.android.socketdemo.codec;

import ClientSocket.Foundation.EndianHelper.BytesConverter;

public class GenCodec
{
    public static byte[] encode(short cmd, String dataInfo)
    {
        byte[] data = dataInfo.getBytes();

        byte[] cmdArray = BytesConverter.bigShortToBytes(cmd);
        byte[] dataBytes = new byte[cmdArray.length + data.length];

        System.arraycopy(cmdArray, 0, dataBytes, 0, cmdArray.length);
        System.arraycopy(data, 0, dataBytes, cmdArray.length, data.length);

        return dataBytes;
    }

    public static DecodeObject decode(byte[] fullData)
    {
        DecodeObject object = new DecodeObject();

        byte[] dataBytes = new byte[fullData.length - 2];
        System.arraycopy(fullData, 2, dataBytes, 0, fullData.length - 2);

        object.cmd = BytesConverter.bytesToBigShort(fullData);
        object.dataInfo = new String(dataBytes);

        return object;
    }
}