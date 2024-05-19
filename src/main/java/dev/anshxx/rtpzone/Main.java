package dev.anshxx.rtpzone;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main extends JavaPlugin {
    public static String prefix;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        List<String> regionNames = getConfig().getStringList("rtp-regions");
        Task scheduler = new Task(Main.this, regionNames);
        scheduler.start();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new TimerPlaceholder(scheduler).register();
        } else {
            getLogger().warning("PlaceholderAPI not found! Placeholder expansion will not be available.");
        }
        config = getConfig();
        prefix = config.getString("prefix", "&#EE4B2Bʀᴛᴘᴢᴏɴᴇ &7|&r ");
        getServer().getConsoleSender().sendMessage(formatColors(" "));
        getServer().getConsoleSender().sendMessage(formatColors(prefix + "&7Registering PlaceHolderAPI, classes, event listeners."));
        getServer().getConsoleSender().sendMessage(formatColors(prefix + "&7Registered PlaceHolderAPI, classes, event listeners."));
        getServer().getConsoleSender().sendMessage(formatColors(" "));
        getServer().getConsoleSender().sendMessage(formatColors(prefix + "&7Plugin enabled successfully!"));
    }



    /**
     * This method formats color codes in a given message string.
     * Color codes are represented using '&' followed by hexadecimal color codes or RGB values.
     *
     * @param message the input message containing color codes
     * @return the formatted message with color codes replaced by their respective colors
     */
    public static String formatColors(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        Pattern pattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String colorCode = matcher.group();
            ChatColor color = ChatColor.of(colorCode.substring(1));
            message = message.replace(colorCode, color.toString());
        }
        return message;
    }
}