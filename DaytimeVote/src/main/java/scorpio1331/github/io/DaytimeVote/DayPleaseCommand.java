package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class DayPleaseCommand implements Command
{
    private static final String name = "daypls";
    private World overWorld = null;
    private Map<UUID, Boolean> playerVotes;
    private boolean isDay;

    private JavaPlugin plugin;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
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

            if (args.length > 1)
            {
                sender.sendMessage("Too many arguments!");
                return false;
            }
            String vote = args.length == 1 ? args[0] : "Yes";
            if (!vote.equalsIgnoreCase("No") && !vote.equalsIgnoreCase("Yes"))
            {
                sender.sendMessage("This command only accepts 'Yes' or 'No'.");
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        this.plugin = plugin;
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

        if (!isDay) {

            Player player = (Player) sender;
            boolean voteIsYes = args.length != 1 || args[0].equalsIgnoreCase("Yes");

            playerVotes.put(player.getUniqueId(), voteIsYes);

            int count = Collections.frequency(new ArrayList<>(playerVotes.values()), true);
            int requiredCount = Math.max(overWorld.getPlayers().size() / 2, 1);

            plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has voted %s to turn it daytime. %d/%d.", player.getDisplayName(), voteIsYes ? "Yes" : "No", count, requiredCount));

            if (count >= requiredCount) {
                overWorld.setTime(0);
                plugin.getServer().broadcastMessage("DayTimeVote: Set time to day.");
                playerVotes = new HashMap<>();
            }
            else if (count == 1 && voteIsYes)
            {
                plugin.getServer().broadcastMessage(String.format("DayTimeVote: %s has started a vote to turn it daytime. Requires %d more players to vote Yes.", player.getDisplayName(), requiredCount - 1));
            }
        }
        else
        {
            sender.sendMessage("DayTimeVote: It is already daytime.");
        }

        return true;
    }

    public void HandleDaytimeEvent(DayNightEvent event) {
        //plugin.getServer().broadcastMessage("DayTimeVote: Got HandleDaytimeEvent " + (event.isDay() ? "it's Day" : "it's Night"));
        if (!isDay && event.isDay() && playerVotes != null && playerVotes.size() > 0) {
            plugin.getServer().broadcastMessage("DayTimeVote: Its Daytime! Resetting vote.");
            playerVotes = new HashMap<>();
        }
        isDay = event.isDay();
    }
}
