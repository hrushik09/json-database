/**
 * Uses map object to store database temporarily.
 * Reads and writes this map contents to db.json after each operation.
 */

package server.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import util.Entry;
import util.Result;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private static final String filePath = "src/server/data/db.json";
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String REASON = "No such key";

    // To manage the read and write access to db.json file
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    private Map<String, String> map;

    public Database() {
        map = new HashMap<>();
    }

    public void set(Entry entry, Result result) {
        try {
            w.lock();

            readFrom();
            map.put(entry.getKey(), entry.getValue());
            result.setResponse(OK);
            writeTo();
        } finally {
            w.unlock();
        }
    }

    public void get(Entry entry, Result result) {
        try {
            r.lock();

            readFrom();
            if (map.containsKey(entry.getKey())) {
                result.setResponse(OK);
                result.setValue(map.get(entry.getKey()));
            } else {
                result.setResponse(ERROR);
                result.setReason(REASON);
            }
        } finally {
            r.unlock();
        }
    }

    public void delete(Entry entry, Result result) {
        try {
            w.lock();

            readFrom();
            if (map.containsKey(entry.getKey())) {
                map.remove(entry.getKey());
                result.setResponse(OK);
            } else {
                result.setResponse(ERROR);
                result.setReason(REASON);
            }
            writeTo();
        } finally {
            w.unlock();
        }
    }

    private void readFrom() {
        // read current map from db.json
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            map = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTo() {
        // write current map to db.json
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            gson.toJson(map, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}