package server.database;

public class GetCommand implements Command {
    private Database database;
    private String[] cmdArr;

    public GetCommand(Database database, String[] cmdArr) {
        this.database = database;
        this.cmdArr = cmdArr;
    }

    @Override
    public String execute() {
        return database.get(Integer.parseInt(cmdArr[1]));
    }
}