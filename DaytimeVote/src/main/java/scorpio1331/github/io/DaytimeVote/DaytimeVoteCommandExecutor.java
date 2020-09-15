package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class DaytimeVoteCommandExecutor implements CommandExecutor
{
    private final JavaPlugin plugin;

    public DaytimeVoteCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    //When a command is executed loop through our commands to find match, if it matches then validate the command and run it.
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        for (Commands command: Commands.values()){
            if (cmd.getName().equalsIgnoreCase(command.GetName())) {
                ICommand activatedCommand = command.GetCommand();
                if (!activatedCommand.ValidateCommand(sender, cmd, label, args, plugin)) {
                     return false;
                }
                return activatedCommand.PerformCommand(sender, cmd, label, args, plugin);
            }
        }

        return false;
    }
}
