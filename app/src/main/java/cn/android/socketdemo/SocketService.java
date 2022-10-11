package cn.android.socketdemo;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import ClientSocket.Business.AsyncSocket;
import ClientSocket.Business.ISocketDelegate;
import cn.android.socketdemo.socketBroadcast.SocketBoardSender;

public class SocketService extends Service implements ISocketDelegate {
    private static final String TAG = "SocketService" ;

    private AsyncSocket             _socket = null;

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate");
        super.onCreate();

        _socket = AsyncSocket.shareInstance();
        _socket.setDelegate(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand");

        _socket.connect("192.168.0.179",9999);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(TAG, "onBind");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestory()");

        _socket.setDelegate(null);
        _socket.disConnect();

        super.onDestroy();
    }

    @Override
    public void onConnectSuccess() {
        SocketBoardSender.sendConnectSuccessBroadcast(this);
    }

    @Override
    public void onConnectFail() {
        SocketBoardSender.sendConnectFailedBroadcast(this);
    }

    @Override
    public void onDisconnect() {
        SocketBoardSender.sendDisConnectedBroadcast(this);
    }

    @Override
    public void onReceiveData(byte[] data) {

        Context context = getApplicationContext();
        NotificationHelper.makeSocketNotification(context, "收到socket数据", "hello", "点击查看socket消息", data);

        SocketBoardSender.sendReceiveDataBroadcast(this, data);
    }
}