package scorpio1331.github.io.DaytimeVote;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public interface Command
{
    public String getName();
    public void setPlugin(JavaPlugin plugin);
    public Boolean ValidateCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin);
    public Boolean PerformCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, JavaPlugin plugin);
}
