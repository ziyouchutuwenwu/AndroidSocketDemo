package ClientSocket.Foundation;

public interface IClientSocket
{
    void onConnectSuccess();
    void onConnectFail();
    void onDisconnect();

    void onReceiveData(byte[] data);

    void onSendSuccess(byte[] data);
    void onSendFail(byte[] data);
}