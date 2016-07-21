package ClientSocket.Business;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SocketUIHandler extends Handler{

    private ISocketDelegate _uiDelegate;

    public SocketUIHandler(Looper looper){
        super(looper);
    }

    public void setDelegate(ISocketDelegate delegate){
        _uiDelegate = delegate;
    }

    @Override
    public void handleMessage(Message msg)
    {
        if ( null != _uiDelegate ){

            switch (msg.what){
                case SocketStatusConst.CONNECT_SUCCESS:
                    _uiDelegate.onConnectSuccess();
                    break;
                case SocketStatusConst.CONNECT_FAILED:
                    _uiDelegate.onConnectFail();
                    break;
                case SocketStatusConst.DIS_CONNECTED:
                    _uiDelegate.onDisconnect();
                    break;
                case SocketStatusConst.RECEIVE_DATA:
                    byte[] data = (byte[])msg.obj;
                    _uiDelegate.onReceiveData(data);
                    break;
                default:
                    break;
            }
        }
    }

    public void sendConnectSuccessMsg(){
        Message message = obtainMessage();
        message.what = SocketStatusConst.CONNECT_SUCCESS;
        sendMessage(message);
    }

    public void sendConnectFailMsg(){
        Message message = obtainMessage();
        message.what = SocketStatusConst.CONNECT_FAILED;
        sendMessage(message);
    }

    public void sendDisConnectedMsg(){
        Message message = obtainMessage();
        message.what = SocketStatusConst.DIS_CONNECTED;
        sendMessage(message);
    }

    public void sendOnReceiveDataMsg(byte[] data){
        Message message = obtainMessage();
        message.what = SocketStatusConst.RECEIVE_DATA;
        message.obj = data;
        sendMessage(message);
    }
}