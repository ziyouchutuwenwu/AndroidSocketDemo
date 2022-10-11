package cn.android.socketdemo.socketBroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.android.socketdemo.IServiceSocketDelegate;

public class SocketBroadReceiver extends BroadcastReceiver {
    private IServiceSocketDelegate _serviceSocketDelegate = null;

    public SocketBroadReceiver(IServiceSocketDelegate delegate){
        _serviceSocketDelegate = delegate;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action){
            case SocketServiceActionConst.CONNECT_SUCCESS:
                _serviceSocketDelegate.onConnectSuccess();
                break;
            case SocketServiceActionConst.CONNECT_FAILED:
                _serviceSocketDelegate.onConnectFail();
                break;
            case SocketServiceActionConst.DIS_CONNECTED:
                _serviceSocketDelegate.onDisconnect();
                break;
            case SocketServiceActionConst.RECEIVE_DATA:
                byte[] data = intent.getByteArrayExtra("data");
                _serviceSocketDelegate.onReceiveData(data);
                break;
        }
    }
}