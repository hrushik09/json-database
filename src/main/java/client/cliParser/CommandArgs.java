package client.cliParser;

import com.beust.jcommander.Parameter;

public class CommandArgs {
    @Parameter(names = "-t", description = "Type of the request")
    private String type;

    @Parameter(names = "-i", description = "Index of the cell")
    private int recordNum = -1;

    @Parameter(names = "-m", description = "Value to save")
    private String text;

    public String generateMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(type);

        if (recordNum != -1) {
            sb.append(" ").append(recordNum);
        }

        if (text != null) {
            sb.append(" ").append(text);
        }
        return sb.toString();
    }
}