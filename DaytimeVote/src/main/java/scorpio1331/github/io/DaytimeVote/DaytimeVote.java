package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class DaytimeVote extends JavaPlugin implements DebuggablePlugin
{
    private boolean isInDebugMode = false;

    @Override
    public void onEnable() {
        isInDebugMode = false;
        CommandExecutor commandExecutor = new DaytimeVoteCommandExecutor(this);
        //Create daytime event listener
        DaytimeListener daytimeListener = new DaytimeListener(this);

        //Run through commands
        for (Commands command: Commands.values()){
            command.getCommand().setPlugin(this);
            //Set executor for command
            this.getCommand(command.getName()).setExecutor(commandExecutor);

            if (command.getCommand() instanceof IHandlesDayNightCycle) {
                daytimeListener.AddEventListener((IHandlesDayNightCycle) command.getCommand());
            }
        }

        BukkitScheduler scheduler = getServer().getScheduler();
        //Schedule DaytimeTask to run 60 seconds (1200 / 20), starting immediately.
        scheduler.scheduleSyncRepeatingTask(this, new DaytimeTask(this, Utils.GetWorldByEnvironment(this, World.Environment.NORMAL)), 0L, 1200L/4);
    }

    @Override
    public void onDisable() {
        // Insert logic to be performed when the plugin is disabled
        // No need to do anything on Disable for this.
    }

    @Override
    public boolean isInDebugMode() {
        return isInDebugMode;
    }

    @Override
    public void setIsInDebugMode(boolean value) {
        isInDebugMode = value;
    }
}
