package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class Utils
{
    public static World GetWorldByEnvironment(JavaPlugin plugin, World.Environment environment) {
        for (World world : plugin.getServer().getWorlds()){
            if (world.getEnvironment().equals(environment)) {
                return world;
            }
        }
        return null;
    }
}
