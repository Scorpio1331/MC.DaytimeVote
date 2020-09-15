package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class DebugCommand implements Command {

    private static class AcceptedArguments {
        public static final String Enable = "Enable";
        public static final String Disable = "Disable";
        public static final String Query = "Query";
        public static final List<String> AcceptableArguments = Arrays.asList(Enable, Disable, Query);
    }

    private DebuggablePlugin plugin;

    @Override
    public String getName() {
        return "debugDaytimeVote";
    }

    @Override
    public void setPlugin(JavaPlugin plugin) {
        if (plugin instanceof DebuggablePlugin) {
            this.plugin = (DebuggablePlugin) plugin;
        }
        else {
            this.plugin = null;
        }
    }

    @Override
    public Boolean ValidateCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (sender instanceof Player)
        {
            Player player = (Player) sender;

            Player target = plugin.getServer().getPlayer(player.getUniqueId());
            if (target == null || !target.isOnline()) {
                sender.sendMessage(player.getDisplayName() + " is not online!");
                return false;
            }
        }

        if (args.length == 0)
        {
            sender.sendMessage(String.format("This command requires an argument of '%s', '%s' or '%s'.", AcceptedArguments.Enable, AcceptedArguments.Disable, AcceptedArguments.Query));
            return false;
        }
        else if (args.length > 1)
        {
            sender.sendMessage("Too many arguments! this command only accepts one argument.");
            return false;
        }

        final String givenArg = args[0];
        if (AcceptedArguments.AcceptableArguments.stream().noneMatch(givenArg::equalsIgnoreCase))
        {
            sender.sendMessage(String.format("This command only accepts an argument of '%s', '%s' or '%s'.", AcceptedArguments.Enable, AcceptedArguments.Disable, AcceptedArguments.Query));
            return false;
        }

        return true;
    }

    @Override
    public Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (this.plugin == null) {
            setPlugin(plugin);
        }
        if (this.plugin == null) {
            return false;
        }

        String givenArg = args[0];

        if (givenArg.equalsIgnoreCase(AcceptedArguments.Query)) {
            sender.sendMessage(String.format("DayTimeVote: Debugging is currently %s.", this.plugin.isInDebugMode() ? "Enabled" : "Disabled"));
        }
        else
        {
            final boolean enableDebugMode = givenArg.equalsIgnoreCase(AcceptedArguments.Enable);
            if (this.plugin.isInDebugMode() != enableDebugMode) {
                this.plugin.setIsInDebugMode(enableDebugMode);
                sender.sendMessage(String.format("DayTimeVote: %s Debugging mode.", this.plugin.isInDebugMode() ? "Enabled" : "Disabled"));
            }
            else {
                sender.sendMessage(String.format("DayTimeVote: Debug mode is already %s.", this.plugin.isInDebugMode() ? "Enabled" : "Disabled"));
            }
        }

        return true;
    }
}
