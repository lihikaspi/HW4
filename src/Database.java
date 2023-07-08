import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents a database
 */
public class Database {
    private Map<String, String> data;
    private final int k;
    private Set<Long> readers;  //Set of reader IDs
    private long writer;  //Current writer thread ID
    private Condition cond;  //To signal waiting threads
    private static Lock lock;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();
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

    /**
     * Checks the condition for being able to read from database
     * @return bool is able to read
     */
    public boolean checkRead() {
        try {
            lock.lock();
            return readers.size() < k && writer == -1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks the condition for being able to write to database
     * @return bool is able to write
     */
    public boolean checkWrite() {
        try {
            lock.lock();
            return readers.size() == 0 && writer == -1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Happens before thread reads from the database. Waits until it is able to read
     */
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

    /**
     * Happens before thread reads from the database. Does not wait until it is able to read
     * @return bool is the thread able to read from database
     */
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

    /**
     * Happens after thread reads from the database
     * Throws exception if thread using the function was not reading from it
     */
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

    /**
     * Happens before thread writes to the database. Waits until it is able to write
     */
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

    /**
     * Happens before thread writes to the database. Waits until it is able to write
     * @return bool is the thread able to write to database
     */
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

    /**
     * Happens after thread writes to the database
     * Throws exception if thread using the function was not the one writing to it
     */
    public void writeRelease() {
        try {
            lock.lock();
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