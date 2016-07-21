package ClientSocket.Foundation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Arrays;

public class CClientSocket
{
    private Socket _socket = null;
    private IClientSocket _callBack = null;
    private Thread _readThread = null;

    private byte[] _savedBuffer = null;
    private int _savedBufferSize = 0;
    private int _packageMaxSize = 0;

    public void init()
    {
        _savedBuffer = new byte[BufferConst.SAVED_BUFFER_CAP];
        _socket = new Socket();
    }

    public void setPackageMaxSize(int packageMaxSize)
    {
        _packageMaxSize = packageMaxSize;
    }

    public void setDelegate(IClientSocket callBack)
    {
        _callBack = callBack;
    }

    //支持超时的connect
    public void connectWithTimeout(String ip, int port, int timeout)
    {
        boolean isConnected = true;

        SocketAddress serverAddr = new InetSocketAddress(ip, port);
        try {
            _socket.connect(serverAddr,timeout);
        } catch (IOException e) {
            isConnected = false;
        }

        if ( !isConnected )
        {
            if (null != _callBack) _callBack.onConnectFail();
        }
        else
        {
            if (null != _callBack) _callBack.onConnectSuccess();
        }
    }

    public void disConnect()
    {
        try {
            _socket.close();
        } catch (IOException e) {
        }

        if (null != _callBack) _callBack.onDisconnect();
    }

    public void sendData(byte[] data)
    {
        //包头内数据长度不包括包头长度
        byte[] headerSizeBytes = new byte[PackageHeader.size()];
        byte[] sendBuffer = new byte[BufferConst.SEND_BUFFER_CAP];

        int dataSize = data.length;
        int totalSendSize = headerSizeBytes.length + dataSize;

        byte[] dataSizeBytes = PackageHeader.setDataSizeBytes(dataSize);

        System.arraycopy(dataSizeBytes, 0, headerSizeBytes, 0, dataSizeBytes.length);

        System.arraycopy(headerSizeBytes, 0, sendBuffer, 0, headerSizeBytes.length);
        System.arraycopy(data, 0, sendBuffer, headerSizeBytes.length, data.length);

        if (null != _socket && 0 != data.length)
        {
            boolean isSendSuccess = true;
            OutputStream sendStream = null;

            try {
                sendStream = _socket.getOutputStream();
            } catch (IOException e) {
                isSendSuccess = false;
            }

            try {
                sendStream.write(sendBuffer,0,totalSendSize);
            } catch (IOException e) {
                isSendSuccess = false;
            }

            if (isSendSuccess)
            {
                if (null != _callBack) _callBack.onSendSuccess(data);
            }
            else
            {
                if (null != _callBack) _callBack.onSendFail(data);
            }
        }
    }

    public void startReadLoop()
    {
        _readThread = new Thread(new Runnable(){
            public void run(){
                loopRead();
            }
        });
        _readThread.start();
    }

    private void loopRead()
    {
        int receivedLen = 0;
        _savedBufferSize = 0;

        byte[] readBuffer = new byte[BufferConst.READ_BUFFER_CAP];
        byte[] totalBytes = new byte[BufferConst.TOTAL_BUFFER_CAP];

        byte[] completeData = new byte[BufferConst.READ_BUFFER_CAP];

        //循环接收
        while(true)
        {
            InputStream readStream = null;

            try {
                readStream = _socket.getInputStream();
            } catch (IOException e) {
            }
            try {
                receivedLen = readStream.read(readBuffer,0,BufferConst.READ_BUFFER_CAP);
            } catch (IOException e) {
            }

            if (receivedLen <= 0)
            {
                if (null != _callBack) _callBack.onDisconnect();
                break;
            }

            Arrays.fill(totalBytes,0,totalBytes.length, (byte) 0);

            System.arraycopy(_savedBuffer, 0, totalBytes, 0, _savedBufferSize);
            System.arraycopy(readBuffer, 0, totalBytes, _savedBufferSize, receivedLen);

            int totalSize = _savedBufferSize + receivedLen;
            int loopBufferPos = 0;

            //单次拆包循环
            while (true)
            {
                byte[] loopBuffer = new byte[BufferConst.TOTAL_BUFFER_CAP];
                int loopBufferLen = totalSize - loopBufferPos;

                System.arraycopy(totalBytes, loopBufferPos, loopBuffer, 0, loopBufferLen);

                if (loopBufferLen < PackageHeader.size() ) break;
                int headerDataLen = PackageHeader.getHeaderDataLen(loopBuffer);

                //大于包头，小于包长
                if ( loopBufferLen < headerDataLen || loopBufferLen > _packageMaxSize ) break;

                //完整数据包readBufferWithSavedBytes + loopBufferPos，给上层的时候，需要去掉包头长度
                Arrays.fill(completeData,0,completeData.length, (byte) 0);

                //arraycopy(Object src,int srcPos, Object dest, int destPos, int length)
                System.arraycopy(totalBytes, loopBufferPos + PackageHeader.size(), completeData, 0, headerDataLen);
                if (null != _callBack)
                {
                    byte[] receivedData = new byte[headerDataLen];
                    System.arraycopy(completeData, 0, receivedData, 0, headerDataLen);
                    _callBack.onReceiveData(receivedData);
                }

                loopBufferPos += headerDataLen + PackageHeader.size();
            }

            Arrays.fill(_savedBuffer,0,_savedBufferSize, (byte) 0);
            _savedBufferSize = 0;

            System.arraycopy(totalBytes, loopBufferPos, _savedBuffer, 0, totalSize - loopBufferPos);
            _savedBufferSize = totalSize - loopBufferPos;
        }
    }
}
