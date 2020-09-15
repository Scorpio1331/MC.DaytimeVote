package scorpio1331.github.io.DaytimeVote;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

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

    public static String GetListOfAvailableCommandsText(List<String> availableCommands) {
        StringBuilder text = new StringBuilder();

        for (int i = 0; i < availableCommands.size() - 1; i++) {
            text.append(availableCommands.get(i)).append(", ");
        }

        text.append(" or ").append(availableCommands.get(availableCommands.size() - 1));

        return text.toString();
    }
}
