package server.database;

import util.Entry;
import util.Result;

import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final String OK = "OK";
    private static final String ERROR = "ERROR";
    private static final String REASON = "No such key";
    private Map<String, String> map;

    public Database() {
        map = new HashMap<>();
    }

    public void set(Entry entry, Result result) {
        map.put(entry.getKey(), entry.getValue());
        result.setResponse(OK);
    }

    public void get(Entry entry, Result result) {
        if (map.containsKey(entry.getKey())) {
            result.setResponse(OK);
            result.setValue(map.get(entry.getKey()));
        } else {
            result.setResponse(ERROR);
            result.setReason(REASON);
        }
    }

    public void delete(Entry entry, Result result) {
        if (map.containsKey(entry.getKey())) {
            map.remove(entry.getKey());
            result.setResponse(OK);
        } else {
            result.setResponse(ERROR);
            result.setReason(REASON);
        }
    }
}