package bbb.aaa.socketdemoandroid;

public interface IServiceSocketDelegate
{
    void onConnectSuccess();
    void onConnectFail();
    void onDisconnect();
    void onReceiveData(byte[] data);
}