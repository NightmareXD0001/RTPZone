package dev.anshxx.rtpzone;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class TimerPlaceholder extends PlaceholderExpansion {

    private final Task task;

    public TimerPlaceholder(Task task) {
        this.task = task;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getAuthor() {
        return "anshxx";
    }

    @Override
    public String getIdentifier() {
        return "rtpzone";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if (identifier.equals("timer")) {
            return String.valueOf(task.getTimeUntilNextTeleport());
        }
        return null;
    }
}

