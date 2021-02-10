package ClientSocket.Foundation.send;

import ClientSocket.Foundation.EndianHelper.BytesConverter;
import ClientSocket.Foundation.header.PkgHeaderOption;

public class PkgSend {

    public void setDataLenthToHeader(byte[] buffer, int length, PkgHeaderOption pkgHeaderOption) {
        if (pkgHeaderOption.HeaderSize == 2) {
            byte[] lengthBytes = BytesConverter.bigShortToBytes((short) length);
            System.arraycopy(lengthBytes, 0, buffer, 0, lengthBytes.length);
        }
        if (pkgHeaderOption.HeaderSize == 4) {
            byte[] lengthBytes = BytesConverter.bigIntToBytes(length);
            System.arraycopy(lengthBytes, 0, buffer, 0, lengthBytes.length);
        }
    }


    public byte[] makeDataToSend(byte[] data,  PkgHeaderOption pkgHeaderOption) {
        int dataLength = data.length;

        if ( dataLength > pkgHeaderOption.MaxDataSize) return null;

        byte[] buffer = new byte[pkgHeaderOption.HeaderFrameLenth + dataLength];
        setDataLenthToHeader(buffer, dataLength, pkgHeaderOption);

        System.arraycopy(data, 0, buffer, pkgHeaderOption.HeaderFrameLenth, dataLength);
        return buffer;
    }
}
