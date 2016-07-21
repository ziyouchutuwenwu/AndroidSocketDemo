package ClientSocket.Business;

public interface ISocketDelegate
{
    void onConnectSuccess();
    void onConnectFail();
    void onDisconnect();
    void onReceiveData(byte[] data);
}