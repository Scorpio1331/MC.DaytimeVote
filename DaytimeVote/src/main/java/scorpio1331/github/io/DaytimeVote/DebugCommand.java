package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class DebugCommand implements ICommand {

    private static class AcceptedArguments {
        public static final String Enable = "Enable";
        public static final String Disable = "Disable";
        public static final String Query = "Query";
        public static final List<String> AcceptableArguments = Arrays.asList(Enable, Disable, Query);
    }

    private IDebuggablePlugin plugin;

    @Override
    public String GetName() {
        return "debugDaytimeVote";
    }

    @Override
    public void SetPlugin(JavaPlugin plugin) {
        if (plugin instanceof IDebuggablePlugin) {
            this.plugin = (IDebuggablePlugin) plugin;
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

            if (!player.hasPermission(Permissions.CanDebug.getPermissionName())) {
                sender.sendMessage("You do not have permission to use this command!");
                return false;
            }
        }

        if (args.length == 0)
        {
            sender.sendMessage(String.format("This command requires an argument of '%s'.", Utils.GetListOfAvailableCommandsText(AcceptedArguments.AcceptableArguments)));
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
            sender.sendMessage(String.format("This command only accepts an argument of '%s'.", Utils.GetListOfAvailableCommandsText(AcceptedArguments.AcceptableArguments)));
            return false;
        }

        return true;
    }

    @Override
    public Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin) {
        if (this.plugin == null) {
            SetPlugin(plugin);
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
                this.plugin.SendDebugMessage(String.format("DayTimeVote: %s Debugging mode.", this.plugin.isInDebugMode() ? "Enabled" : "Disabled"));
            }
            else {
                sender.sendMessage(String.format("DayTimeVote: Debug mode is already %s.", this.plugin.isInDebugMode() ? "Enabled" : "Disabled"));
            }
        }

        return true;
    }
}
