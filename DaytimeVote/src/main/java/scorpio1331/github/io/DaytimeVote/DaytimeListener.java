package scorpio1331.github.io.DaytimeVote;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class DaytimeListener implements Listener
{
    private final JavaPlugin plugin;
    private final DayPleaseCommand command;

    public DaytimeListener(JavaPlugin plugin, DayPleaseCommand command) {
        this.plugin = plugin;
        this.command = command;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void DayNightChanged(DayNightEvent event) {
        command.HandleDaytimeEvent(event);
    }
}
