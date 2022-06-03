/**
 * Command line arguments parser using JCommander
 */

package client.parser;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import util.Entry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
     * Generates a JSON serialization of Entry for current arguments
     */
    public String generateMessage() {
        if (in != null) {
            try {
                return Files.readString(Paths.get("src/client/data/" + in));
            } catch (IOException e) {
                System.out.println("cannot read file " + e.getMessage());
            }
        }

        Entry entry = new Entry();
        entry.setType(type);
        entry.setKey(key);
        entry.setValue(value);

        Gson gson = new Gson();
        return gson.toJson(entry);
    }
}