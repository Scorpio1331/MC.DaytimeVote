package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        for (Commands command: Commands.values()){
            if (cmd.getName().equalsIgnoreCase(command.GetName())) {
                ICommand activatedCommand = command.GetCommand();
                return activatedCommand.TabComplete(args);
            }
        }
        return null;
    }
}
