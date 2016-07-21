package ClientSocket.Foundation;

import ClientSocket.Foundation.EndianHelper.BytesConverter;

public class PackageHeader
{
    private static int HEADER_SIZE_2 = 2;
    private static int HEADER_SIZE_4 = 4;

    public static int size()
    {
        return HEADER_SIZE_2;
    }

    public static byte[] setDataSizeBytes(int dataSize){

        byte[] dataSizeBytes = null;

        if ( size() == HEADER_SIZE_2 ) {
            dataSizeBytes = BytesConverter.bigShortToBytes((short)dataSize);

        }else if ( size() == HEADER_SIZE_4 ){
            dataSizeBytes = BytesConverter.bigIntToBytes(dataSize);
        }

        return dataSizeBytes;
    }

    public static int getHeaderDataLen(byte[] buffer){

        int headerDataLen = 0;

        if ( size() == HEADER_SIZE_2 ) {
            headerDataLen = BytesConverter.bytesToBigShort(buffer);
        }else if ( size() == HEADER_SIZE_4 )
        {
            headerDataLen = BytesConverter.bytesToBigInt(buffer);
        }

        return headerDataLen;
    }
}