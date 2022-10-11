package cn.android.socketdemo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ClientSocket.Business.AsyncSocket;
import cn.android.socketdemo.codec.DecodeObject;
import cn.android.socketdemo.codec.GenCodec;
import cn.android.socketdemo.socketBroadcast.SocketBoardSender;
import cn.android.socketdemo.socketBroadcast.SocketBroadReceiver;

public class MainActivity extends AppCompatActivity implements IServiceSocketDelegate {

    private TextView _textView = null;
    private AsyncSocket _socket = null;
    private SocketBroadReceiver _socketBroadReceiver = null;
    private SocketService _socketService = null;
    private ServiceConnection _serviceConnection = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, SocketService.class));

        _serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                _socketService = ((SocketService.SocketServiceBinder) service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {}
        };

        // 调用 service 的 onBind
        Intent intent = new Intent(this, SocketService.class);
        bindService(intent, _serviceConnection, BIND_AUTO_CREATE);

        _textView = findViewById(R.id.textView);

        Button clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _textView.setText("");
            }
        });

        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _socketService.connect("192.168.0.179",9999);
            }
        });

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                byte[] dataBytes = GenCodec.encode((short)1112,"i am android");
                _socket.send(dataBytes);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();

        Intent intent = getIntent();
        byte[] data = intent.getByteArrayExtra("data");
        if ( null != data ){
            DecodeObject object = GenCodec.decode(data);
            _textView.setText("通知数据" + object.cmd + object.dataInfo);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();

        _socket = AsyncSocket.shareInstance();
        _socketBroadReceiver = new SocketBroadReceiver(MainActivity.this);
        //设置UI回调
        SocketBoardSender.register(this, _socketBroadReceiver);
    }

    @Override
    protected void onStop(){
        SocketBoardSender.unRegister(this, _socketBroadReceiver);

        super.onStop();
    }

    @Override
    public void onConnectSuccess() {
        _textView.setText("连接成功");
    }

    @Override
    public void onConnectFail() {
        _textView.setText("连接失败");
    }

    @Override
    public void onDisconnect() {
        _textView.setText("断开连接");
    }

    @Override
    public void onReceiveData(byte[] data) {
        DecodeObject object = GenCodec.decode(data);

        String oldInfo = _textView.getText().toString() + "\r\n";
        _textView.setText(oldInfo + "接收到数据" + object.cmd + object.dataInfo);
    }
}