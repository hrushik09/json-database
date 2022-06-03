package server.database;

import java.util.Arrays;

public class Database {
    private static final String SUCCESS = "OK";
    private static final String FAILED = "ERROR";
    private static final int len = 1000;
    private String[] database;

    public Database() {
        database = new String[len];
        Arrays.fill(database, "");
    }

    public String set(int index, String text) {
        int recordNum = index - 1;
        if (isIndexIncorrect(recordNum)) {
            return FAILED;
        }
        database[recordNum] = text;
        return SUCCESS;
    }

    public String get(int index) {
        int recordNum = index - 1;
        if (isCellEmpty(recordNum) || isIndexIncorrect(recordNum)) {
            return FAILED;
        }
        return database[recordNum];
    }

    public String delete(int index) {
        int recordNum = index - 1;
        if (isIndexIncorrect(recordNum)) {
            return FAILED;
        }
        database[recordNum] = "";
        return SUCCESS;
    }

    private boolean isCellEmpty(int i) {
        return database[i].equals("");
    }

    private boolean isIndexIncorrect(int i) {
        return i < 0 || i > len - 1;
    }
}