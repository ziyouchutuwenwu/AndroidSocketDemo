package ClientSocket.Foundation;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import ClientSocket.Foundation.header.PkgHeaderOption;
import ClientSocket.Foundation.recv.IFullData;
import ClientSocket.Foundation.recv.PkgRecv;
import ClientSocket.Foundation.send.PkgSend;

public class CClientSocket implements IFullData
{
    private Socket _socket = null;
    private IClientSocket _callBack = null;
    private Thread _readThread = null;

    private PkgSend _pkgSender = null;
    private PkgRecv _pkgRecv = null;
    private PkgHeaderOption _pkgHeaderOption = null;


    public void init() {
        _initSocket();
        _pkgHeaderOption = PkgHeaderOption.getPkgOptionWithHeaderSize(2);
        _pkgSender = new PkgSend();
        _pkgRecv = new PkgRecv();
        _pkgRecv.setPkgHeaderOption(_pkgHeaderOption);
        _pkgRecv.setFullDataCallback(this);
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
            _closeSocket();
            if (null != _callBack) _callBack.onConnectFail();
            _initSocket();
        }
        else
        {
            if (null != _callBack) _callBack.onConnectSuccess();
        }
    }

    public void disConnect()
    {
        _closeSocket();
        if (null != _callBack) _callBack.onDisconnect();
    }

    public void sendData(byte[] data)
    {
        _pkgSender = new PkgSend();

        byte[] fullDataToSend = _pkgSender.makeDataToSend(data, _pkgHeaderOption);

        if (null != _socket && 0 != fullDataToSend.length)
        {
            boolean isSendSuccess = true;
            OutputStream sendStream = null;

            try {
                sendStream = _socket.getOutputStream();
            } catch (IOException e) {
                isSendSuccess = false;
            }

            try {
                sendStream.write(fullDataToSend,0, fullDataToSend.length);
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
                _pkgRecv.LoopRead(_socket);
            }
        });
        _readThread.start();
    }

    @Override
    public void onFullDataReceived(byte[] fulldata) {
        if (null != _callBack) _callBack.onReceiveData(fulldata);
    }

    @Override
    public void onPkgRecvError() {
        if (null != _callBack) _callBack.onDisconnect();
    }

    private void _initSocket(){
        _socket = new Socket();
    }

    private void _closeSocket(){
        try {
            _socket.close();
            _socket = null;
        } catch (IOException e) {
        }
    }
}
