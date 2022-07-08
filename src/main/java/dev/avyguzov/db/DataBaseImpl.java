package dev.avyguzov.db;

import com.google.inject.Singleton;

import java.util.HashMap;

/**
 * Mock database for demonstration purpose.
 */
@Singleton
public class DataBaseImpl implements DataBase {
    private final HashMap<String, String> hashTable = new HashMap<>();

    @Override
    public synchronized void write(String key, String value) {
        hashTable.put(key, value);
    }

    @Override
    public String read(String key) {
        return hashTable.getOrDefault(key, "");
    }

    @Override
    public void clearDb() {
        hashTable.clear();
    }
}
