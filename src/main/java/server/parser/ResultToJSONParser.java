/**
 * Serializes an object of Result class to JSON string
 */

package server.parser;

import com.google.gson.Gson;
import util.Result;

public class ResultToJSONParser {
    private Result result;

    public ResultToJSONParser(Result result) {
        this.result = result;
    }

    public String parse() {
        Gson gson = new Gson();
        return gson.toJson(result);
    }
}