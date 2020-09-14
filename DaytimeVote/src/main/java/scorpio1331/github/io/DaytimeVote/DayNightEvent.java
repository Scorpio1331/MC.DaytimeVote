package scorpio1331.github.io.DaytimeVote;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DayNightEvent extends Event
{
    private boolean isDay;

    public DayNightEvent(boolean isDay) {
        this.isDay = isDay;
    }

    public boolean isDay() {
        return isDay;
    }

    public boolean isNight() {
        return !isDay;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
