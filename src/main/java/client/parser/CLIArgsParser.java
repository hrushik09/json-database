/**
 * Command line arguments parser using JCommander
 */

package client.parser;

import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import util.Entry;

public class CLIArgsParser {
    @Parameter(names = "-t", description = "Type of the request")
    private String type;

    @Parameter(names = "-k", description = "Key in teh database")
    private String key;

    @Parameter(names = "-v", description = "Value to save")
    private String value;

    /**
     * Generates a JSON serialization of Entry for current arguments
     */
    public String generateMessage() {
        Entry entry = new Entry();
        entry.setType(type);
        entry.setKey(key);
        entry.setValue(value);

        Gson gson = new Gson();
        return gson.toJson(entry);
    }
}