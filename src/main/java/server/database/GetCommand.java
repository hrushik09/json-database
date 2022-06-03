package server.database;

import util.Entry;
import util.Result;

public class GetCommand implements Command {
    private Database database;
    private Entry entry;
    private Result result;

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