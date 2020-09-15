package scorpio1331.github.io.DaytimeVote;

public class NightPleaseCommand extends SetTimeVoteCommand
{
    @Override
    public String GetName() {
        return "nightplease";
    }

    @Override
    public String GetTimeName() {
        return "Night";
    }

    @Override
    protected long GetSetTime() {
        return 13000;
    }

    @Override
    protected String GetRequiredCountPath() {
        return ConfigurationSettings.NumberOfPlayersRequiredForVote.Nighttime;
    }

    @Override
    protected String GetIsAbsolutePath() { return ConfigurationSettings.NumberOfPlayersRequiredForVote.NighttimeValueIsAbsolute;  }

    @Override
    public void HandleDaytimeEvent(DayNightEvent event) {
        if (event.isNight()) {
            SetIsEnabled(false);
            disabledReason = String.format("It is already %s", GetTimeName());

            if (isDay && GetPlayerVotes() != null && GetPlayerVotes().size() > 0) {
                ResetVote();
            }
        }
        else {
            SetIsEnabled(true);
        }
        super.HandleDaytimeEvent(event);
    }
}
