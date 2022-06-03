package server.database;

public class DeleteCommand implements Command {
    private Database database;
    private String[] cmdArr;

    public DeleteCommand(Database database, String[] cmdArr) {
        this.database = database;
        this.cmdArr = cmdArr;
    }

    @Override
    public String execute() {
        return database.delete(Integer.parseInt(cmdArr[1]));
    }
}