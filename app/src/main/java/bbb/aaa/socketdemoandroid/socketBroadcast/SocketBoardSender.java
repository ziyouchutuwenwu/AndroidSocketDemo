package bbb.aaa.socketdemoandroid.socketBroadcast;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class SocketBoardSender {

    public static void register(Context context, SocketBroadReceiver socketBroadReceiver){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketServiceActionConst.CONNECT_SUCCESS);
        intentFilter.addAction(SocketServiceActionConst.CONNECT_FAILED);
        intentFilter.addAction(SocketServiceActionConst.DIS_CONNECTED);
        intentFilter.addAction(SocketServiceActionConst.RECEIVE_DATA);
        context.registerReceiver(socketBroadReceiver, intentFilter);
    }

    public static void unRegister(Context context, SocketBroadReceiver socketBroadReceiver){
        context.unregisterReceiver(socketBroadReceiver);
    }

    public static void sendConnectSuccessBroadcast(Context context){
        Intent intent = new Intent();
        intent.setAction(SocketServiceActionConst.CONNECT_SUCCESS);
        context.sendBroadcast(intent);
    }

    public static void sendConnectFailedBroadcast(Context context){
        Intent intent = new Intent();
        intent.setAction(SocketServiceActionConst.CONNECT_FAILED);
        context.sendBroadcast(intent);
    }

    public static void sendDisConnectedBroadcast(Context context){
        Intent intent = new Intent();
        intent.setAction(SocketServiceActionConst.DIS_CONNECTED);
        context.sendBroadcast(intent);
    }

    public static void sendReceiveDataBroadcast(Context context, byte[] data){
        Intent intent = new Intent();
        intent.setAction(SocketServiceActionConst.RECEIVE_DATA);
        intent.putExtra("data", data);
        context.sendBroadcast(intent);
    }
}