package dev.anshxx.rtpzone.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * This class provides utility methods for interacting with WorldGuard regions.
 */
public class WorldGuardUtil {

    public static boolean isInRegion(Player player, String regionName) {
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = regionContainer.createQuery();
        com.sk89q.worldedit.util.Location playerLocation = BukkitAdapter.adapt(player.getLocation());
        for (ProtectedRegion region : query.getApplicableRegions(playerLocation).getRegions()) {
            if (region.getId().equalsIgnoreCase(regionName)) {
                return true;
            }
        }
        return false;
    }
}
