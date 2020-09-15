package scorpio1331.github.io.DaytimeVote;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class DaytimeTask implements Runnable {
    private final JavaPlugin plugin;
    private final World overWorld;
    private final long nightTime = 13000;
    private boolean isDay;

    public DaytimeTask(JavaPlugin plugin, World overWorld) {
        this.plugin = plugin;
        this.overWorld = overWorld;
    }

    //Dispatch event to any DayNightEvent listeners
    @Override
    public void run() {
        if (plugin instanceof IDebuggablePlugin) {
            IDebuggablePlugin debuggablePlugin = (IDebuggablePlugin) plugin;
            if (debuggablePlugin.isInDebugMode())
            {
                debuggablePlugin.SendDebugMessage(String.format("DayTimeVote: Checking Time -> %s (%d ticks).", overWorld.getTime() >= nightTime ? "It's Nighttime" : "It's Daytime", overWorld.getTime()));
            }
        }

        boolean isDay = overWorld.getTime() < nightTime;
        if (this.isDay != isDay) {
            this.isDay = isDay;
            Bukkit.getPluginManager().callEvent(new DayNightEvent(isDay));
        }
    }
}
