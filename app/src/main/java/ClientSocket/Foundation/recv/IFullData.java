package ClientSocket.Foundation.recv;

public interface IFullData {
    void onFullDataReceived(byte[] fulldata);
    void onPkgRecvError();
}
