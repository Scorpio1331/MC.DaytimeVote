package scorpio1331.github.io.DaytimeVote;

public class NightPleaseCommand extends SetTimeVoteCommand
{
    public NightPleaseCommand() {
        name = "nightplease";
        timeName = "Night";
        setTime = 13000;
    }

    @Override
    public void HandleDaytimeEvent(DayNightEvent event) {
        if (event.isNight()) {
            setIsEnabled(false);
            disabledReason = String.format("It is already %s", timeName);

            if (isDay && getPlayerVotes() != null && getPlayerVotes().size() > 0) {
                ResetVote();
            }
        }
        else {
            setIsEnabled(true);
        }
        super.HandleDaytimeEvent(event);
    }
}
