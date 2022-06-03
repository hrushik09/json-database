package server.database;

import com.google.gson.JsonObject;

public class GetCommand implements Command {
    private final Database database;
    private final JsonObject jsonObject;

    public GetCommand(Database database, JsonObject jsonObject) {
        this.database = database;
        this.jsonObject = jsonObject;
    }

    @Override
    public String execute() {
        return database.get(jsonObject);
    }
}