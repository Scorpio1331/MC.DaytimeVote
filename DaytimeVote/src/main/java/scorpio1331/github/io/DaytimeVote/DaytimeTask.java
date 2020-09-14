package scorpio1331.github.io.DaytimeVote;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class DaytimeTask implements Runnable {
    private final JavaPlugin plugin;
    private final World overWorld;
    private final long nightTime = 13000;

    public DaytimeTask(JavaPlugin plugin, World overWorld) {
        this.plugin = plugin;
        this.overWorld = overWorld;
    }

    @Override
    public void run() {
        //plugin.getServer().broadcastMessage("DayTimeVote: Checking Time => " + (overWorld.getTime() >= nightTime ? "It's Nighttime" : "It's Daytime") + " (" + overWorld.getTime() + " ticks).");
        Bukkit.getPluginManager().callEvent(new DayNightEvent(overWorld.getTime() < nightTime));
    }
}
