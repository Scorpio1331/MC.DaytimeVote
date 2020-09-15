package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public final class DaytimeVote extends JavaPlugin implements IDebuggablePlugin
{
    private boolean isInDebugMode = false;

    @Override
    public void onEnable() {
        //Create config file if it doesnt exist
        this.saveDefaultConfig();
        this.reloadConfig();

        isInDebugMode = false;
        CommandExecutor commandExecutor = new DaytimeVoteCommandExecutor(this);
        //Create daytime event listener
        DaytimeListener daytimeListener = new DaytimeListener(this);

        //Run through commands
        for (Commands command: Commands.values()){
            command.GetCommand().SetPlugin(this);
            //Set executor for command
            this.getCommand(command.GetName()).setExecutor(commandExecutor);

            if (command.GetCommand() instanceof IHandlesDayNightCycle) {
                daytimeListener.AddEventListener((IHandlesDayNightCycle) command.GetCommand());
            }
        }

        long pollingTicks = this.getConfig().getLong("daytimePollingTicks");
        BukkitScheduler scheduler = getServer().getScheduler();
        //Schedule DaytimeTask to run every set amount of ticks, default is: 60 seconds (1200 / 20). Starting immediately.
        scheduler.scheduleSyncRepeatingTask(this, new DaytimeTask(this, Utils.GetWorldByEnvironment(this, World.Environment.NORMAL)), 0L, pollingTicks);
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

    @Override
    public void SendDebugMessage(String message) {
        getServer().getOnlinePlayers()
                .stream()
                .filter(p -> p.hasPermission(Permissions.CanDebug.getPermissionName()))
                .forEach(p -> p.sendMessage(message));
        getServer().getConsoleSender().sendMessage(message);
    }
}
