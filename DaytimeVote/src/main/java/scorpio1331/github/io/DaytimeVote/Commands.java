package scorpio1331.github.io.DaytimeVote;

public enum Commands
{
    DayPlease(new DayPleaseCommand()),
    NightPlease(new NightPleaseCommand()),
    Debug(new DebugCommand()),
    ChangeConfig(new ChangeConfigurationCommand());

    private ICommand command;

    Commands(ICommand command) {
        this.command = command;
    }

    public ICommand GetCommand() {
        return command;
    }

    public String GetName() {
        return command.GetName();
    }
}
