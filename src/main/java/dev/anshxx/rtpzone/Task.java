package dev.anshxx.rtpzone;

import dev.anshxx.rtpzone.util.RtpUtil;
import dev.anshxx.rtpzone.util.WorldGuardUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Objects;

import static dev.anshxx.rtpzone.Main.formatColors;

public class Task {
    private final JavaPlugin plugin;
    private final List<String> regionNames;
    private RtpUtil rtpUtil;
    private long timeUntilNextTeleport = 30;
    private Location sharedLocation = null;

    private Sound teleportSound;
    private float teleportVolume;
    private float teleportPitch;

    private Sound countdownSound;
    private float countdownVolume;
    private float countdownPitch;

    public Task(JavaPlugin plugin, List<String> regionNames) {
        this.plugin = plugin;
        this.regionNames = regionNames;
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration config = plugin.getConfig();

        ConfigurationSection soundConfig = config.getConfigurationSection("sounds");
        teleportSound = Sound.valueOf(soundConfig.getString("teleport.sound", "ENTITY_ENDERMAN_TELEPORT"));
        teleportVolume = (float) soundConfig.getDouble("teleport.volume", 1.0);
        teleportPitch = (float) soundConfig.getDouble("teleport.pitch", 1.0);

        countdownSound = Sound.valueOf(soundConfig.getString("countdown.sound", "BLOCK_NOTE_BLOCK_PLING"));
        countdownVolume = (float) soundConfig.getDouble("countdown.volume", 1.0);
        countdownPitch = (float) soundConfig.getDouble("countdown.pitch", 1.0);
    }

    public void start() {
        new BukkitRunnable() {
            @Override
            public void run() {
                timeUntilNextTeleport = 30;
                for (String regionName : regionNames) {
                    World world = Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("rtp-world")));
                    rtpUtil = RtpUtil.fromConfig(plugin.getConfig(), world);
                    sharedLocation = rtpUtil.getSharedLocation();
                    teleportPlayersInRegion(regionName);
                }
            }
        }.runTaskTimer(plugin, 0, 20 * 30);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (timeUntilNextTeleport > 0) {
                    timeUntilNextTeleport--;
                    sendTitleSubtitleActionBarToPlayers();
                }
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    private void teleportPlayersInRegion(String regionName) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (WorldGuardUtil.isInRegion(player, regionName)) {
                try {
                    rtpUtil.teleportPlayerToHighestSafeLocationAboveBlock(player, sharedLocation);
                    sendTitleAndActionBarToPlayer(player);
                    playTeleportSound(player);
                } catch (Exception e) {
                    plugin.getLogger().warning("Failed to teleport player " + player.getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendTitleSubtitleActionBarToPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (String regionName : regionNames) {
                if (WorldGuardUtil.isInRegion(player, regionName)) {
                    sendTitleSubtitleActionBarToPlayer(player);
                    if (timeUntilNextTeleport >= 1 && timeUntilNextTeleport <= 5) {
                        playCountdownSound(player);
                    }
                    break;
                }
            }
        }
    }

    private void sendTitleSubtitleActionBarToPlayer(Player player) {
        FileConfiguration config = plugin.getConfig();
        String title = formatColors(config.getString("messages.title", "&#ff0000ʀᴛᴘ ᴢᴏɴᴇ"));
        String subtitle = formatColors(config.getString("messages.teleporting_subtitle", "&fTeleporting in %seconds% seconds").replace("%seconds%", String.valueOf(timeUntilNextTeleport)));
        String actionbar = formatColors(config.getString("messages.action_bar_prefix", "&#ff0000ʀᴛᴘ ᴢᴏɴᴇ &7|") + " " +
                config.getString("messages.teleporting_action_bar", "&fTeleporting in %seconds% seconds").replace("%seconds%", String.valueOf(timeUntilNextTeleport)));

        player.sendTitle(title, subtitle, 5, 30, 5);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(actionbar));
    }

    private void sendTitleAndActionBarToPlayer(Player player) {
        FileConfiguration config = plugin.getConfig();
        String message = formatColors(config.getString("messages.teleport_success", "&aYou have been teleported"));
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        String title = formatColors(config.getString("messages.title_on_teleport", "&#ff0000ʀᴛᴘ ᴢᴏɴᴇ"));
        String subtitle = formatColors(config.getString("messages.subtitle_on_teleport", "&fKill players and steal their loot"));
        player.sendTitle(title, subtitle, 5, 30, 5);
    }

    private void playTeleportSound(Player player) {
        player.playSound(player.getLocation(), teleportSound, teleportVolume, teleportPitch);
    }

    private void playCountdownSound(Player player) {
        player.playSound(player.getLocation(), countdownSound, countdownVolume, countdownPitch);
    }

    public long getTimeUntilNextTeleport() {
        return timeUntilNextTeleport;
    }
}
