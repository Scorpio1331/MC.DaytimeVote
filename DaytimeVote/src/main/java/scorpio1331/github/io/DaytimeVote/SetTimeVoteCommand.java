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

    protected String name = null;
    protected String timeName = null;
    protected long setTime = 0;

    private World overWorld = null;
    private Map<UUID, Boolean> playerVotes;
    protected boolean isDay = true;

    protected boolean isEnabled = true;
    protected String disabledReason;

    protected JavaPlugin plugin;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    protected Map<UUID, Boolean> getPlayerVotes() {
        return playerVotes;
    }

    protected void setIsEnabled(boolean value) {
        isEnabled = value;
        if (value) {
            disabledReason = null;
        }
    }

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
                sender.sendMessage(String.format("This command only accepts an argument of '%s', '%s' or '%s'.", AcceptedArguments.Yes, AcceptedArguments.No, AcceptedArguments.Query));
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
            int requiredCount = (int) Math.max(Math.ceil(overWorld.getPlayers().size() / 2d), 1);

            Player player = (Player) sender;

            if (args.length == 1 && args[0].equalsIgnoreCase(AcceptedArguments.Query))
            {
                sender.sendMessage(playerVotes.size() >= 1
                        ? String.format("DayTimeVote: The current vote for %s is at %d/%d.", timeName, count, requiredCount)
                        : String.format("DayTimeVote: There is no ongoing vote for %s.", timeName));
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
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has started a vote to turn it to %s. Requires %d more players to vote %s.", player.getDisplayName(), timeName, requiredCount - 1, AcceptedArguments.Yes));
                }
                else if (prevVote != null && !prevVote.equalsIgnoreCase(newVote)) {
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has changed their vote from %s to %s (%s). %d/%d.", player.getDisplayName(), prevVote, newVote, timeName, count, requiredCount));
                }
                else {
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has voted %s to turn it to %s. %d/%d.", player.getDisplayName(),newVote, timeName, count, requiredCount));
                }

                if (count >= requiredCount) {
                    overWorld.setTime(setTime);
                    plugin.getServer().broadcastMessage(String.format("DayTimeVote: Set time to %s.", timeName));
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
                debuggablePlugin.SendDebugMessage(String.format("DayTimeVote: %s command Got HandleDaytimeEvent -> %s", name, event.isDay() ? "it's Day" : "it's Night"));
            }
        }
        isDay = event.isDay();
    }

    protected void ResetVote() {
        plugin.getServer().broadcastMessage(String.format("DayTimeVote: Its %s! Resetting %s vote.", timeName, name));
        playerVotes = new HashMap<>();
    }

    private String ConvertVoteToText(boolean vote) {
        return vote ? AcceptedArguments.Yes : AcceptedArguments.No;
    }
}
