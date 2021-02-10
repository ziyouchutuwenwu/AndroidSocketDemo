package ClientSocket.Foundation.recv;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import ClientSocket.Foundation.EndianHelper.BytesConverter;
import ClientSocket.Foundation.header.PkgHeaderOption;

public class PkgRecv {
    private byte[] _savedData;
    private PkgHeaderOption _pkgHeaderOption;
    private IFullData _fullDataDelegate;

    public PkgRecv(){
        _savedData = new byte[]{0,6};
    }

    public void setPkgHeaderOption(PkgHeaderOption pkgHeaderOption){
        _pkgHeaderOption = pkgHeaderOption;
    }

    public void setFullDataCallback(IFullData fullDataDelegate){
        _fullDataDelegate = fullDataDelegate;
    }

    public void LoopRead(Socket socket)  {
        int readBufferSize = 8;
        while(true){
            byte[] readBuffer = new byte[readBufferSize];
            InputStream readStream = null;

            try {
                readStream = socket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            int readLen = 0;
            try {
                readLen = readStream.read(readBuffer, 0, readBufferSize);
            } catch (IOException e) {
                System.out.println(e);
                break;
            }

            if (readLen <= 0)
            {
                if (null != _fullDataDelegate) _fullDataDelegate.onPkgRecvError();
                break;
            }

            byte[] buffer = new byte[readLen];
            System.arraycopy(readBuffer, 0, buffer, 0, readLen);

            byte[] totalData = new byte[_savedData.length + buffer.length];
            System.arraycopy(_savedData, 0, totalData, 0, _savedData.length);
            System.arraycopy(buffer, 0, totalData, _savedData.length, buffer.length);

            dealWithData(totalData, _pkgHeaderOption);
        }
    }

    public void dealWithData(byte[] totalData, PkgHeaderOption pkgHeaderOption)  {
        while(true){
            int totalDataLen = totalData.length;
            if ( totalDataLen <= pkgHeaderOption.HeaderSize) {
                _savedData = new byte[totalDataLen];
                System.arraycopy(totalData, 0, _savedData, 0, totalDataLen);
                break;
            }

            if ( totalDataLen > pkgHeaderOption.HeaderSize ){
                int dataLen = getDataLenthFromHeader(totalData, pkgHeaderOption);

                if ( totalDataLen < pkgHeaderOption.HeaderSize + dataLen){
                    _savedData = new byte[totalDataLen];
                    System.arraycopy(totalData, 0, _savedData, 0, totalDataLen);
                    break;
                }
                if ( totalDataLen >= pkgHeaderOption.HeaderSize + dataLen) {

                    int frameLen = pkgHeaderOption.HeaderSize + dataLen;

                    byte[] pkg = new byte[dataLen];
                    System.arraycopy(totalData, pkgHeaderOption.HeaderSize, pkg, 0, dataLen);
                    System.out.println(pkg.toString());
                    if ( null != _fullDataDelegate) _fullDataDelegate.onFullDataReceived(totalData);

                    _savedData = new byte[totalData.length - frameLen];
                    System.arraycopy(totalData, frameLen, _savedData, 0, totalData.length - frameLen);

                    totalData = new byte[totalData.length - frameLen];
                    System.arraycopy(_savedData, 0, totalData, 0, _savedData.length);
                }
            }
        }

        return;
    }

    public int getDataLenthFromHeader(byte[] buffer, PkgHeaderOption pkgHeaderOption) {
        int dataLenth = 0;

        if (pkgHeaderOption.HeaderSize == 2){
            dataLenth = BytesConverter.bytesToBigShort(buffer);
        }
        if (pkgHeaderOption.HeaderSize == 4){
            dataLenth = BytesConverter.bytesToBigInt(buffer);
        }

        return dataLenth;
    }
}
