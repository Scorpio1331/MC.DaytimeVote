package scorpio1331.github.io.DaytimeVote;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DaytimeListener implements Listener
{
    private final List<IHandlesDayNightCycle> EventListeners;

    public DaytimeListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        EventListeners = new ArrayList<>();
    }

    public void AddEventListener(IHandlesDayNightCycle listener) {
        if (!EventListeners.contains(listener))
        {
            EventListeners.add(listener);
        }
    }

    public void RemoveEventListener(IHandlesDayNightCycle listener) {
        if (EventListeners.contains(listener))
        {
            EventListeners.remove(listener);
        }
    }

    //Listen for any DayNightEvents that are dispatched
    @EventHandler
    public void DayNightChanged(DayNightEvent event) {
        for (IHandlesDayNightCycle eventListener : EventListeners) {
            eventListener.HandleDaytimeEvent(event);
        }
    }
}
