package scorpio1331.github.io.DaytimeVote;

public class DayPleaseCommand extends SetTimeVoteCommand
{
    @Override
    public String GetName() {
        return "dayplease";
    }

    @Override
    public String GetTimeName() {
        return "Day";
    }

    @Override
    protected long GetSetTime() {
        return 0;
    }

    @Override
    protected String GetRequiredCountPath() {
        return ConfigurationSettings.NumberOfPlayersRequiredForVote.Daytime;
    }

    @Override
    protected String GetIsAbsolutePath() { return ConfigurationSettings.NumberOfPlayersRequiredForVote.DaytimeValueIsAbsolute;  }

    @Override
    public void HandleDaytimeEvent(DayNightEvent event) {
        if (event.isDay()) {
            SetIsEnabled(false);
            disabledReason = String.format("It is already %s", GetTimeName());

            if (!isDay && GetPlayerVotes() != null && GetPlayerVotes().size() > 0) {
                ResetVote();
            }
        }
        else {
            SetIsEnabled(true);
        }
        super.HandleDaytimeEvent(event);
    }
}
