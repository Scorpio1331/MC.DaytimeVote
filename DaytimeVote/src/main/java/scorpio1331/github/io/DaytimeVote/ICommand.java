package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public interface ICommand
{
    String GetName();
    void SetPlugin(JavaPlugin plugin);
    Boolean ValidateCommand(CommandSender sender, Command cmd, String label, String[] args, JavaPlugin plugin);
    Boolean PerformCommand(CommandSender sender, Command cmd, String label, String[] args, JavaPlugin plugin);
    List<String> TabComplete(String[] args);
}
