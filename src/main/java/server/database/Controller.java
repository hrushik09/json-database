package server.database;

public class Controller {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public String executeCommand() {
        return command.execute();
    }
}