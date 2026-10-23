# Architecture

## Plugin and compatibility

`FarmAssistReboot` is a Bukkit plugin built with Java 8 bytecode. Gradle compiles
against Spigot API 1.13.2 and generates the Bukkit plugin metadata, including
`api-version: 1.13`, a soft dependency on PlaceholderAPI, and the Folia support
flag. The README advertises Minecraft 1.13.2–26.2; this repository does not
record a version-by-version server test matrix.

## Components

- `FarmAssistReboot` initializes configuration, languages, ACF commands,
  listeners, the update checker, bStats, and the optional PlaceholderAPI expansion.
- `BlockBreakListener` checks player, world, crop, and permission settings before
  scheduling replanting. `PlayerInteractionListener` handles wheat planting on
  tilling. `JoinListener` handles update notifications.
- `Util` checks inventory seeds and schedules `ReplantTask`, which verifies the
  block and supporting material before placing a crop or dropping a seed.
- `FarmAssistConfig` loads and updates `config.yml` through BoostedYAML.
  `LanguageManager` extracts and selects bundled language files. The reload
  command reloads the config and the active language file.

## Threading and state

Block changes in `ReplantTask` run after a five-tick delay through MorePaperLib's
region-specific scheduler. The GitHub release check runs through its asynchronous
scheduler. Crop and inventory access occurs from event handlers or the scheduled
region task; changes to these paths must preserve the appropriate server thread
or Folia region context.

The global toggle and disabled-player UUID set are held in memory. The set is
concurrent but is not persisted across a restart. Server `config.yml` and
language files live in the plugin data folder; the plugin does not store world
data itself. Replanting changes blocks and may drop items, so live behavior
needs an isolated test world before compatibility is claimed.
