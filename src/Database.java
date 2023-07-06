import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    private final int k;
    private Set<Thread> readers;
    private Thread writer;

    // locks
    private static ReentrantLock readLock;
    private static ReentrantLock writeLock;

    private static Lock lock;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        k = maxNumOfReaders;
        readLock = new ReentrantLock();
        writeLock = new ReentrantLock();
        lock = new ReentrantLock();
        readers = new HashSet<>();
        writer = null;
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
            return readLock.getHoldCount() < k && writeLock.getHoldCount() == 0;
        } finally {
            lock.unlock();
        }
    }

    public boolean checkWrite() {
        try {
            lock.lock();
            readers.add(Thread.currentThread());
            return readLock.getHoldCount() == 0 && writeLock.getHoldCount() == 0;
        } finally {
            lock.unlock();
        }
    }

    public void readAcquire() {
        while(!checkRead()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        readers.add(Thread.currentThread());
        readLock.lock();
    }

    public boolean readTryAcquire() {
        if (checkRead()) {
            readLock.lock();
            readers.add(Thread.currentThread());
            return true;
        }
        return false;
    }

    public void readRelease() {
        try {
            if (!(readers.contains(Thread.currentThread())))
                throw new IllegalMonitorStateException("Illegal read release attempt");
            readers.remove(Thread.currentThread());
            readLock.unlock();
        } catch (IllegalMonitorStateException e) {
            System.out.println(e.getMessage());
        }
    }

    public void writeAcquire() {
        while(!checkWrite()) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }

        writer = Thread.currentThread();
        writeLock.lock();
    }

    public boolean writeTryAcquire() {
        if (checkWrite()) {
            writeLock.lock();
            writer = Thread.currentThread();
            return true;
        }
        return false;
    }

    public void writeRelease() {
        try {
            if (!Thread.currentThread().equals(writer))
                throw new IllegalMonitorStateException("Illegal write release attempt");
            writer = null;
            writeLock.unlock();
        } catch (IllegalMonitorStateException e) {
            System.out.println(e.getMessage());
        }
    }
}