package scorpio1331.github.io.DaytimeVote;

public enum Commands
{
    DayPlease(new DayPleaseCommand()),
    NightPlease(new NightPleaseCommand()),
    Debug(new DebugCommand());

    private ICommand command;

    Commands(ICommand command) {
        this.command = command;
    }

    public ICommand getCommand() {
        return command;
    }

    public String getName() {
        return command.getName();
    }
}
