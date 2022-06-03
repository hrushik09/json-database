package server.database;

import com.google.gson.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {
    private static final String filePath = "src/server/data/db.json";

    // boolean flag to track if any key is missing from keyArr
    static boolean keyPresentForDeletion = true;

    // To manage the read and write access to db.json file
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.writeLock();

    public String get(JsonObject jsonObject) {
        String s = "";
        try {
            r.lock();

            JsonObject db = readDbFromFile(Path.of(filePath)).getAsJsonObject();
            JsonArray keyArr;
            if (jsonObject.get("key").isJsonArray()) {
                keyArr = jsonObject.get("key").getAsJsonArray();
            } else {
                keyArr = getJsonArray(jsonObject.get("key").getAsString());
            }

            JsonElement resultJsonElement = get(db, keyArr, 0);

            if (resultJsonElement != null) {
                s = "{\"response\":\"OK\",\"value\":" + new Gson().toJson(resultJsonElement) + "}";
            } else {
                s = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            r.unlock();
        }
        return s;
    }

    private JsonElement get(JsonObject currentJsonObject, JsonArray keyArr, int index) {
        String currentKey = keyArr.get(index).getAsString();

        if (currentJsonObject.get(currentKey) == null) {
            return null;
        }

        if (index == keyArr.size() - 1) {
            return currentJsonObject.get(currentKey);
        }

        JsonElement currentJsonValue = currentJsonObject.get(currentKey);
        return get(currentJsonValue.getAsJsonObject(), keyArr, index + 1);
    }

    public String set(JsonObject jsonObject) {
        String s = "";
        try {
            w.lock();

            JsonObject db = readDbFromFile(Path.of(filePath)).getAsJsonObject();
            JsonArray keyArr;
            if (jsonObject.get("key").isJsonArray()) {
                keyArr = jsonObject.get("key").getAsJsonArray();
            } else {
                keyArr = getJsonArray(jsonObject.get("key").getAsString());
            }
            JsonElement value = jsonObject.get("value");

            JsonElement resultJsonElement = set(db, value, keyArr, 0);

            writeDbToFile(resultJsonElement, Path.of(filePath));
            s = "{\"response\":\"OK\"}";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            w.unlock();
        }
        return s;
    }

    private JsonElement set(JsonObject currentJsonObject, JsonElement value, JsonArray keyArr, int index) {
        String currentKey = keyArr.get(index).getAsString();

        // when keys are missing
        if (currentJsonObject.get(currentKey) == null) {
            currentJsonObject.add(currentKey, new JsonObject());
        }

        if (index == keyArr.size() - 1) {
            currentJsonObject.add(currentKey, value);
            return currentJsonObject;
        }

        JsonElement currentJsonValue = currentJsonObject.get(currentKey);
        JsonElement newJsonValue = set(currentJsonValue.getAsJsonObject(), value, keyArr, index + 1);
        currentJsonObject.add(currentKey, newJsonValue);
        return currentJsonObject;
    }

    public String delete(JsonObject jsonObject) {
        String s = "";
        try {
            w.lock();

            JsonObject db = readDbFromFile(Path.of(filePath)).getAsJsonObject();
            JsonArray keyArr;
            if (jsonObject.get("key").isJsonArray()) {
                keyArr = jsonObject.get("key").getAsJsonArray();
            } else {
                keyArr = getJsonArray(jsonObject.get("key").getAsString());
            }

            JsonElement resultJsonElement = delete(db, keyArr, 0);
            if (keyPresentForDeletion) {
                s = "{\"response\":\"OK\"}";
            } else {
                // no value was deleted
                // return message will change accordingly
                s = "{\"response\":\"ERROR\",\"reason\":\"No such key\"}";
                // set flag back to normal for next call of delete()
                keyPresentForDeletion = true;
            }
            writeDbToFile(resultJsonElement, Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            w.unlock();
        }
        return s;
    }

    private JsonElement delete(JsonObject currentJsonObject, JsonArray keyArr, int index) {
        String currentKey = keyArr.get(index).getAsString();

        if (currentJsonObject.get(currentKey) == null) {
            keyPresentForDeletion = false;
            return currentJsonObject;
        }

        if (index == keyArr.size() - 1) {
            currentJsonObject.remove(currentKey);
            return currentJsonObject;
        }

        JsonElement currentJsonValue = currentJsonObject.get(currentKey);
        JsonElement newJsonValue = delete(currentJsonValue.getAsJsonObject(), keyArr, index + 1);
        currentJsonObject.add(currentKey, newJsonValue);
        return currentJsonObject;
    }

    public JsonElement readDbFromFile(Path path) throws IOException {
        try (Reader reader = Files.newBufferedReader(path)) {
            return JsonParser.parseReader(reader);
        }
    }

    public void writeDbToFile(JsonElement db, Path path) throws IOException {
        try (Writer writer = Files.newBufferedWriter(path)) {
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(db, writer);
        }
    }

    private JsonArray getJsonArray(String input) {
        String[] a = input.split(" ");
        JsonArray jsonArray = new JsonArray();
        for (String s : a) {
            jsonArray.add(s);
        }
        return jsonArray;
    }
}