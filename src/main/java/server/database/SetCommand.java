package server.database;

public class SetCommand implements Command {
    private Database database;
    private String[] cmdArr;

    public SetCommand(Database database, String[] cmdArr) {
        this.database = database;
        this.cmdArr = cmdArr;
    }

    @Override
    public String execute() {
        return database.set(Integer.parseInt(cmdArr[1]), cmdArr[2]);
    }
}