package dev.avyguzov.db;

import com.google.inject.ImplementedBy;

@ImplementedBy(DataBaseImpl.class)
public interface DataBase {
    void write(String key, String value);
    String read(String key);
    void clearDb();
}
