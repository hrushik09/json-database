/**
 * Command line arguments parser using JCommander
 */

package client.parser;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class CLIArgsParser {
    @Parameter(names = "-t", description = "Type of the request")
    private String type;

    @Parameter(names = "-k", description = "Key in the database")
    private String key;

    @Parameter(names = "-v", description = "Value to save")
    private String value;

    @Parameter(names = "-in", description = "Path to a .json file containing the request")
    private String in;

    /**
     * Generates a JSON serialization for current arguments
     */
    public String generateMessage() {
        if (in != null) {
            try {
                return Files.readString(Paths.get("src/client/data/" + in));
            } catch (IOException e) {
                System.out.println("cannot read file " + e.getMessage());
            }
        }

        Map<String, String> map = new LinkedHashMap<>();
        map.put("type", type);
        map.put("key", key);
        map.put("value", value);

        Gson gson = new Gson();
        return gson.toJson(map);
    }
}