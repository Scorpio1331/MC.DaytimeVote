package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public abstract class SetTimeVoteCommand implements ICommand, IHandlesDayNightCycle
{
    private static class AcceptedArguments {
        public static final String Yes = "Yes";
        public static final String No = "No";
        public static final String Query = "Query";
        public static final List<String> AcceptableArguments = Arrays.asList(Yes, No, Query);
    }

    private World overWorld = null;
    private Map<UUID, Boolean> playerVotes;
    protected boolean isDay = true;

    private boolean isEnabled = true;
    protected String disabledReason;

    protected JavaPlugin plugin;

    @Override
    public abstract String GetName();

    public abstract String GetTimeName();

    protected abstract long GetSetTime();

    @Override
    public void SetPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected Map<UUID, Boolean> GetPlayerVotes() {
        return playerVotes;
    }

    protected void SetIsEnabled(boolean value) {
        isEnabled = value;
        if (value) {
            disabledReason = null;
        }
    }

    protected abstract String GetRequiredCountPath();
    protected abstract String GetIsAbsolutePath();

    @Override
    public Boolean ValidateCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("This command can only be run by a player.");
            return false;
        }
        else
        {
            Player player = (Player) sender;

            Player target = plugin.getServer().getPlayer(player.getUniqueId());
            if (target == null || !target.isOnline()) {
                sender.sendMessage(player.getDisplayName() + " is not online!");
                return false;
            }

            if (!player.hasPermission(Permissions.CanVote.getPermissionName())) {
                sender.sendMessage("You do not have permission to use this command!");
                return false;
            }

            if (args.length > 1)
            {
                sender.sendMessage("Too many arguments!");
                return false;
            }
            String vote = args.length == 1 ? args[0] : AcceptedArguments.Yes;
            if (AcceptedArguments.AcceptableArguments.stream().noneMatch(vote::equalsIgnoreCase))
            {
                sender.sendMessage(String.format("This command only accepts an argument of '%s'.", Utils.GetListOfAvailableCommandsText(AcceptedArguments.AcceptableArguments)));
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (overWorld == null)
        {
            overWorld = Utils.GetWorldByEnvironment(plugin, World.Environment.NORMAL);
        }

        if (overWorld == null) {
            sender.sendMessage("Could not identify overworld.");
            return false;
        }

        if (playerVotes == null) {
            playerVotes = new HashMap<>();
        }

        if (isEnabled) {
            int count = Collections.frequency(new ArrayList<>(playerVotes.values()), true);

            int playerCount = overWorld.getPlayers().size();
            int requiredCount = GetRequiredCount(playerCount);

            if (plugin instanceof IDebuggablePlugin) {
                IDebuggablePlugin debuggablePlugin = (IDebuggablePlugin) plugin;
                if (debuggablePlugin.isInDebugMode())
                {
                    debuggablePlugin.SendDebugMessage(String.format("DayTimeVote: Required Count for %s is %d, using %s and Value is %s", GetName(), requiredCount,
                            ShouldUseAllForRequiredCount() ? "All" : GetName(),
                            (GetRequiredCountIsAbsolute(ShouldUseAllForRequiredCount()) ? "Absolute" : "Not Absolute")));
                }
            }

            Player player = (Player) sender;

            if (args.length == 1 && args[0].equalsIgnoreCase(AcceptedArguments.Query))
            {
                sender.sendMessage(playerVotes.size() >= 1
                        ? String.format("DayTimeVote: The current vote for %s is at %d/%d.", GetTimeName(), count, requiredCount)
                        : String.format("DayTimeVote: There is no ongoing vote for %s.", GetTimeName()));
            }
            else
            {
                boolean voteIsYes = args.length != 1 || args[0].equalsIgnoreCase(AcceptedArguments.Yes);

                String prevVote = null;
                if (playerVotes.containsKey(player.getUniqueId())) {
                    prevVote = ConvertVoteToText(playerVotes.get(player.getUniqueId()));
                }
                playerVotes.put(player.getUniqueId(), voteIsYes);

                String newVote = ConvertVoteToText(voteIsYes);

                count = Collections.frequency(new ArrayList<>(playerVotes.values()), true);

                if (playerVotes.size() == 1 && voteIsYes && requiredCount > 1)
                {
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has started a vote to turn it to %s. Requires %d more players to vote %s.", player.getDisplayName(), GetTimeName(), requiredCount - 1, AcceptedArguments.Yes));
                }
                else if (prevVote != null && !prevVote.equalsIgnoreCase(newVote)) {
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has changed their vote from %s to %s (%s). %d/%d.", player.getDisplayName(), prevVote, newVote, GetTimeName(), count, requiredCount));
                }
                else {
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has voted %s to turn it to %s. %d/%d.", player.getDisplayName(),newVote, GetTimeName(), count, requiredCount));
                }

                if (count >= requiredCount) {
                    overWorld.setTime(GetSetTime());
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: Set time to %s.", GetTimeName()));
                    playerVotes = new HashMap<>();
                }
            }
        }
        else
        {
            sender.sendMessage(String.format("DayTimeVote: %s.", disabledReason));
        }

        return true;
    }

    @Override
    public void HandleDaytimeEvent(DayNightEvent event) {
        if (plugin instanceof IDebuggablePlugin) {
            IDebuggablePlugin debuggablePlugin = (IDebuggablePlugin) plugin;
            if (debuggablePlugin.isInDebugMode())
            {
                debuggablePlugin.SendDebugMessage(String.format("DayTimeVote: %s command Got HandleDaytimeEvent -> %s", GetName(), event.isDay() ? "it's Day" : "it's Night"));
            }
        }
        isDay = event.isDay();
    }

    protected void ResetVote() {
        plugin.getServer().broadcastMessage(String.format("DayTimeVote: Its %s! Resetting %s vote.", GetTimeName(), GetName()));
        playerVotes = new HashMap<>();
    }

    private String ConvertVoteToText(boolean vote) {
        return vote ? AcceptedArguments.Yes : AcceptedArguments.No;
    }

    private boolean ShouldUseAllForRequiredCount() {
        return plugin.getConfig().getBoolean(ConfigurationSettings.NumberOfPlayersRequiredForVote.UseAll);
    }

    private int GetRequiredCount(int playerCount) {
        boolean useAll = ShouldUseAllForRequiredCount();
        int requiredCount = plugin.getConfig().getInt(useAll ? ConfigurationSettings.NumberOfPlayersRequiredForVote.All : GetRequiredCountPath());

        return GetRequiredCountIsAbsolute(useAll) ? requiredCount : (int) Math.max(Math.ceil(playerCount / (double)requiredCount), 1);
    }

    private boolean GetRequiredCountIsAbsolute(boolean useAll) {
        return plugin.getConfig().getBoolean(useAll ? ConfigurationSettings.NumberOfPlayersRequiredForVote.AllValueIsAbsolute : GetIsAbsolutePath());
    }
}
