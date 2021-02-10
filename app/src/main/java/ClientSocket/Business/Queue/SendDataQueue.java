package ClientSocket.Business.Queue;

import java.util.LinkedList;
import java.util.Queue;

public class SendDataQueue
{
    private static SendDataQueue _instance = null;
    private Queue _queue = null;

    public static SendDataQueue shareInstance()
    {
        if (null == _instance)
        {
            _instance = new SendDataQueue();
        }
        return _instance;
    }

    private SendDataQueue()
    {
        _queue = new LinkedList();
    }

    public int getSize()
    {
        return _queue.size();
    }

    public byte[] getDataBytes()
    {
        return (byte[])_queue.peek();
    }

    public void addDataBytes(byte[] dataBytes)
    {
        synchronized (_queue)
        {
            _queue.add(dataBytes);
        }
    }

    public void removeDataBytes()
    {
        synchronized (_queue)
        {
            if (_queue.size() > 0)
            {
                _queue.remove();
            }
        }
    }

    public void clear()
    {
        synchronized (_queue)
        {
            if (_queue.size() > 0) {
                _queue.clear();
            }
        }
    }
}