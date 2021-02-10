package ClientSocket.Business;

import android.os.Looper;

import ClientSocket.Business.Queue.SendDataQueue;
import ClientSocket.Foundation.CClientSocket;
import ClientSocket.Foundation.IClientSocket;

public class AsyncSocket implements IClientSocket {
    private static AsyncSocket _instance = null;

    private CClientSocket       _socket = null;
    private SocketUIHandler _uiHandler = null;
    private Thread _sendThread = null;
    private boolean             _shouldSendExit = false;
    private boolean             _isConnected = false;

    private int                 _timeout = 3;

    public static AsyncSocket shareInstance()
    {
        if (null == _instance)
        {
            _instance = new AsyncSocket();
        }
        return _instance;
    }

    private AsyncSocket()
    {
        _socket = new CClientSocket();
        _socket.init();
        _socket.setDelegate(this);

        _uiHandler = new SocketUIHandler(Looper.getMainLooper());
    }

    public void setTimeout(int timeout)
    {
        _timeout = timeout;
    }

    public void connect(final String ip, final int port)
    {
        new Thread(new Runnable(){
            public void run(){
                _socket.connectWithTimeout(ip, port, 1000 * _timeout);
            }
        }).start();
    }

    public void disConnect()
    {
        _socket.disConnect();
    }

    public boolean isConnected()
    {
        return _isConnected;
    }

    public void startSendLoop()
    {
        _sendThread = new Thread(new Runnable(){
            public void run(){
                loopSend();
            }
        });
        _sendThread.start();
    }

    private void loopSend()
    {
        while (true)
        {
            if (_shouldSendExit) break;

            int queueSize = SendDataQueue.shareInstance().getSize();
            if (queueSize == 0) continue;

            byte[] dataBytes = SendDataQueue.shareInstance().getDataBytes();

            if ( dataBytes.length > 0 )
            {
                _socket.sendData(dataBytes);
            }
        }
    }

    public void send(byte[] data)
    {
        if (!_shouldSendExit)
        {
            SendDataQueue.shareInstance().addDataBytes(data);
        }
    }

    public void setDelegate(ISocketDelegate delegate)
    {
        _uiHandler.setDelegate(delegate);
    }

    @Override
    public void onConnectSuccess() {
        _isConnected = true;

        _socket.startReadLoop();
        _shouldSendExit = false;
        this.startSendLoop();

        _uiHandler.sendConnectSuccessMsg();
    }

    @Override
    public void onConnectFail() {
        _isConnected = false;

        _shouldSendExit = true;
        _uiHandler.sendConnectFailMsg();
    }

    @Override
    public void onDisconnect() {
        _socket.init();
        _isConnected = false;

        _shouldSendExit = true;
        SendDataQueue.shareInstance().clear();

        _uiHandler.sendDisConnectedMsg();
    }

    @Override
    public void onReceiveData(byte[] data) {
        _uiHandler.sendOnReceiveDataMsg(data);
    }

    @Override
    public void onSendSuccess(byte[] data) {
        SendDataQueue.shareInstance().removeDataBytes();
    }

    @Override
    public void onSendFail(byte[] data) {
        SendDataQueue.shareInstance().removeDataBytes();
        this.disConnect();
    }
}