package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class DaytimeVote extends JavaPlugin
{

    @Override
    public void onEnable() {
        for (Commands command: Commands.values()){
            command.getCommand().setPlugin(this);
        }
        this.getCommand(Commands.DayPlease.getName()).setExecutor(new DaytimeVoteCommandExecutor(this));
        new DaytimeListener(this, (DayPleaseCommand) Commands.DayPlease.getCommand());
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new DaytimeTask(this, Utils.GetWorldByEnvironment(this, World.Environment.NORMAL)), 0L, 1200L/3L);
    }

    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
    }

}
