import java.util.HashMap;
import java.util.Map;


public class Database {
    private Map<String, String> data;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        // TODO: Add your code here...

        // used before reading from database
        // identical to readAcquire but:
        // doesn't make a thread wait

        // return true if can read
        // return false if can't
    }

    public void readAcquire() {
        // TODO: Add your code here...

        // used before reading from database
        // if another thread writes to database --> wait
        // if max number of readers --> wait
    }

    public void readRelease() {
        // TODO: Add your code here...

        // used after reading from database
        // marks that thread finished reading

        // if thread uses method but doesn't read from database
        // --> throw IllegalMonitorStateException ("Illegal read release attempt") --unchecked
    }

    public void writeAcquire() {
       // TODO: Add your code here...

        // used before writing to database
        // if another thread writes to database --> wait
        // if another thread reads from database --> wait
    }

    public boolean writeTryAcquire() {
        // TODO: Add your code here...

        // used before writing to database
        // identical to writeAcquire but:
        // doesn't make a thread wait

        // return true if can write
        // return false if can't
    }

    public void writeRelease() {
        // TODO: Add your code here...

        // used after writing to database
        // marks that thread finished writing

        // if thread uses method but doesn't write to database
        // --> throw IllegalMonitorStateException ("Illegal write release attempt") --unchecked
    }
}