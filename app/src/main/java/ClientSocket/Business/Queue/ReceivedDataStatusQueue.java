package ClientSocket.Business.Queue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReceivedDataStatusQueue
{
    private static ReceivedDataStatusQueue _instance = null;
    private Queue _queue = null;

    public static ReceivedDataStatusQueue shareInstance()
    {
        if (null == _instance)
        {
            _instance = new ReceivedDataStatusQueue();
        }
        return _instance;
    }

    private ReceivedDataStatusQueue()
    {
        _queue = new LinkedList();
    }

    public int getSize()
    {
        return _queue.size();
    }

    public int getStatus() {
        return (int)_queue.peek();
    }

    public void addStatus(int socketConst)
    {
        synchronized (_queue)
        {
            _queue.add(socketConst);
        }
    }

    public void removeStatus()
    {
        synchronized (_queue)
        {
            if (_queue.size() > 0) {
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