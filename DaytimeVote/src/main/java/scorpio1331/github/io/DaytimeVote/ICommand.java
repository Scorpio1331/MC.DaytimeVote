package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public interface ICommand
{
    String getName();
    void setPlugin(JavaPlugin plugin);
    Boolean ValidateCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin);
    Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin);
}
