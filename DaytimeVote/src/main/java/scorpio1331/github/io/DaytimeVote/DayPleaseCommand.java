package scorpio1331.github.io.DaytimeVote;

public class DayPleaseCommand extends SetTimeVoteCommand
{
    public DayPleaseCommand() {
        name = "dayplease";
        timeName = "Day";
        setTime = 0;
    }

    @Override
    public void HandleDaytimeEvent(DayNightEvent event) {
        if (event.isDay()) {
            setIsEnabled(false);
            disabledReason = String.format("It is already %s", timeName);

            if (!isDay && getPlayerVotes() != null && getPlayerVotes().size() > 0) {
                ResetVote();
            }
        }
        else {
            setIsEnabled(true);
        }
        super.HandleDaytimeEvent(event);
    }
}
