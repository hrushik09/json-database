/**
 * Deserializes JSON string to an object of Entry class
 */

package server.parser;

import com.google.gson.Gson;
import util.Entry;

public class JSONTOEntryParser {
    private String jsonStringToParse;

    public JSONTOEntryParser(String jsonStringToParse) {
        this.jsonStringToParse = jsonStringToParse;
    }

    public Entry parse() {
        Gson gson = new Gson();
        return gson.fromJson(jsonStringToParse, Entry.class);
    }
}