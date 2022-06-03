package server.database;

import util.Entry;
import util.Result;

public class GetCommand implements Command {
    private final Database database;
    private final Entry entry;
    private final Result result;

    public GetCommand(Database database, Entry entry, Result result) {
        this.database = database;
        this.entry = entry;
        this.result = result;
    }

    @Override
    public void execute() {
        database.get(entry, result);
    }
}