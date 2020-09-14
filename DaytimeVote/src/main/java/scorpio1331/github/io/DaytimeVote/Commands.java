package scorpio1331.github.io.DaytimeVote;

public enum Commands
{
    DayPlease(new DayPleaseCommand());

    private Command command;

    Commands(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public String getName() {
        return command.getName();
    }
}
