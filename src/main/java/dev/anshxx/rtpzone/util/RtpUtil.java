package dev.anshxx.rtpzone.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class RtpUtil {

    private final World world;
    private final int minX;
    private final int maxX;
    private final int minZ;
    private final int maxZ;

    public RtpUtil(World world, int minX, int maxX, int minZ, int maxZ) {
        this.world = world;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
    }

    public Location getSharedLocation() {
        int x = getRandomCoordinate(minX, maxX);
        int z = getRandomCoordinate(minZ, maxZ);
        int y = world.getHighestBlockYAt(x, z);
        return new Location(world, x, y, z);
    }

    private int getRandomCoordinate(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public boolean isSafeLocation(Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        // Check if the block at the location and the block above are not solid
        if (!world.getBlockAt(x, y, z).getType().isSolid() &&
                !world.getBlockAt(x, y + 1, z).getType().isSolid()) {
            // Check if the block below is solid and not water or lava
            Material belowType = world.getBlockAt(x, y - 1, z).getType();
            if (belowType.isSolid() && belowType != Material.WATER && belowType != Material.LAVA) {
                // Check the surrounding blocks for water or lava
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        Material surroundingType = world.getBlockAt(x + dx, y, z + dz).getType();
                        if (surroundingType == Material.WATER || surroundingType == Material.LAVA) {
                            return false;
                        }
                    }
                }
                // If all conditions pass, the location is safe
                return true;
            }
        }
        return false;
    }

    public static RtpUtil fromConfig(FileConfiguration config, World world) {
        int xRadius = config.getInt("rtp-x-radius", 0);
        int zRadius = config.getInt("rtp-z-radius", 0);
        int minX = -xRadius;
        int maxX = xRadius;
        int minZ = -zRadius;
        int maxZ = zRadius;
        return new RtpUtil(world, minX, maxX, minZ, maxZ);
    }

    public void teleportPlayerToHighestSafeLocationAboveBlock(Player player, Location blockLocation) {
        Location teleportLocation = blockLocation.clone();
        for (int y = blockLocation.getBlockY() + 1; y <= 256; y++) {
            teleportLocation.setY(y);
            if (isSafeLocation(teleportLocation)) {
                player.teleport(teleportLocation);
                break;
            }
        }
    }
}
