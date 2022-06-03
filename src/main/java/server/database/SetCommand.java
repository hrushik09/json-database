package server.database;

import com.google.gson.JsonObject;

public class SetCommand implements Command {
    private final Database database;
    private final JsonObject jsonObject;

    public SetCommand(Database database, JsonObject jsonObject) {
        this.database = database;
        this.jsonObject = jsonObject;
    }

    @Override
    public String execute() {
        return database.set(jsonObject);
    }
}