import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    private final int k;
    private Set<Long> readers;
    private long writer;
    private Condition cond;

    private static Lock lock;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        k = maxNumOfReaders;
        lock = new ReentrantLock();
        readers = new HashSet<>();
        writer = -1;
        cond = this.lock.newCondition();
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean checkRead() {
        try {
            lock.lock();
            return readers.size() < k && writer == -1;
        } finally {
            lock.unlock();
        }
    }

    public boolean checkWrite() {
        try {
            lock.lock();
            return readers.size() == 0 && writer == -1;
        } finally {
            lock.unlock();
        }
    }

    public void readAcquire() {
        try{
            lock.lock();
            while(!checkRead()) {
                cond.await();
            }
            readers.add(Thread.currentThread().getId());
        }
        catch (InterruptedException e) {}
        finally {
            lock.unlock();
        }
    }

    public boolean readTryAcquire() {
        try{
            lock.lock();
            if (checkRead()) {
                readers.add(Thread.currentThread().getId());
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    public void readRelease() {
        try {
            lock.lock();
            if (!(readers.contains(Thread.currentThread().getId())))
                throw new IllegalMonitorStateException("Illegal read release attempt");
            readers.remove(Thread.currentThread().getId());
            cond.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public void writeAcquire() {
        try {
            lock.lock();
            while(!checkWrite()) {
                cond.await();
            }
            writer = Thread.currentThread().getId();
        }
        catch (InterruptedException e) {}
        finally {
            lock.unlock();
        }
    }

    public boolean writeTryAcquire() {
        try {
            lock.lock();
            if (checkWrite()) {
                writer = Thread.currentThread().getId();
                return true;
            }
            return false;
        }
        finally {
            lock.unlock();
        }
    }

    public void writeRelease() {
        try {
            lock.lock();
            //writer = Thread.currentThread().getId();
            long current = Thread.currentThread().getId();
            if (writer != current){
                throw new IllegalMonitorStateException("Illegal write release attempt");
            }
            writer = -1;
            cond.signal();
        }
        finally {
            lock.unlock();
        }
    }
}