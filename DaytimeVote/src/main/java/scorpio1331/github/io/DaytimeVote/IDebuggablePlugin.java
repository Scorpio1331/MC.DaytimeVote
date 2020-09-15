package scorpio1331.github.io.DaytimeVote;

public interface IDebuggablePlugin {
    boolean isInDebugMode();
    void setIsInDebugMode(boolean value);
    void SendDebugMessage(String message);
}
