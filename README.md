# RTPZone

The RTPZone plugin (Random Teleport Zone) is a Bukkit/Spigot plugin designed to randomly teleport players within designated regions. This plugin is particularly useful for creating randomized spawn points or teleportation zones in your Minecraft server.

## Features

- Randomly teleport players within specified regions.
- Customize teleportation messages and sounds.
- Ensure safe teleportation by avoiding water, lava, and non-solid blocks.
- Integrates with WorldGuard and WorldEdit for region management.

## Installation

1. Download the latest RTPZone.jar file from the [releases page](https://github.com/anshxx99/RTPZone/releases).
2. Place the RTPZone.jar file in the `plugins` folder of your Bukkit/Spigot server.
3. Ensure that both WorldGuard and WorldEdit are installed and configured on your server.
4. Start or reload your server.

## Usage

1. Define the regions where you want players to be randomly teleported in the `config.yml` file.
2. Customize teleportation messages, sounds, and other settings in the `config.yml` file.
3. Players will be randomly teleported within the specified regions according to the configured settings.

## Configuration

The `config.yml` file allows you to customize various aspects of the plugin:

- Define teleportation regions.
- Configure teleportation messages and sounds.
- Adjust teleportation cooldowns and other settings.
- Use the `%rtpzone_timer%` placeholder to display the remaining time until the next teleportation.

## Commands

- `/rtpzone reload` - Reloads the plugin configuration from the `config.yml` file.

## Permissions

- `rtpzone.reload` - Allows players to reload the plugin configuration.

## Dependencies

- WorldGuard - For region management.
- WorldEdit - For region management and selection.

## Support

For questions, issues, or feature requests, please [submit an issue](https://github.com/anshxx99/RTPZone/issues) on GitHub.

## License

This plugin is licensed under the [GNU General Public License](https://github.com/anshxx99/RTPZone?tab=GPL-3.0-1-ov-file).
