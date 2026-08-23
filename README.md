> [!NOTE]
> **Maintenance Mode**
>
> Life has become a bit busier these days, so this project is no longer under active development.
>
> I'll still keep an eye on critical issues and compatibility updates, and I'm happy to review pull requests when I can. New features are unlikely unless they come from community contributions or I find myself with more free time in the future.
>
> Thanks for all the support and understanding—it really means a lot.


# FarmAssistReboot
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=sarhatabaot_FarmAssistReboot&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=sarhatabaot_FarmAssistReboot)
![Java CI with Gradle](https://github.com/sarhatabaot/FarmAssistReboot/workflows/Java%20CI%20with%20Gradle/badge.svg)
[![Crowdin](https://badges.crowdin.net/farmassistreboot/localized.svg)](https://crowdin.com/project/farmassistreboot)

A 1.13.2 – 26.2 version of FarmAssist. Requires **Java 8** or newer.
## Commands:
* **/farmassist toggle** - Lets a player turn off FarmAssist features.
* **/farmassist global** - Turns off FarmAssist globally without disabling the plugin.
* **/farmassist update** - Checks for updates
* **/farmassist reload** - reloads the config

## Permissions:

* farmassist.crops - Lets players auto-replant all crops that are enabled.
  * farmassist.wheat 
  * farmassist.sugar_cane
  * farmassist.cocoa
  * farmassist.nether_wart
  * farmassist.carrots
  * farmassist.potatoes
  * farmassist.beetroots
  * farmassist.cactus
* farmassist.toggle - Allows use of /farmassist toggle
* farmassist.toggle.global - Allows use of /farmassist global
* farmassist.update - Allows use of /farmassist update
* farmassist.reload - Allows use of /farmassist reload
* farmassist.till - Allows player to have AutoReplant on till.
* farmassist.notify.update - Notifies the player if there is a new update.

## Placeholders: 
`%farmassist_toggle%` - Show global farmassist state

`%farmassist_player_toggle%` - Show current player toggle state. Useful for guis.

`%farmassist_player_<uuid|name>` - Show specific player toggle state. Can be a name or an uuid.
## Configuration (config.yml):
```yaml
##################################################################
#                          FarmAssist                            #
##################################################################
use-permissions: true
##################################################################
#                        Global Settings                         #
##################################################################
# Anything set under global settings will be globally set,
# Even if you give the permission these settings will override it.
# Example:
# player has farmassist.wheat
# wheat:
#   Enabled: false
# player won't be able to auto-replant wheat.
wheat:
    enabled: true
    replant-when-ripe: false
    plant-on-till: true
sugar_cane:
    enabled: true
nether_wart:
    enabled: true
    replant-when-ripe: false
cocoa:
    enabled: true
    replant-when-ripe: false
carrots:
    enabled: true
    replant-when-ripe: false
potatoes:
    enabled: true
    replant-when-ripe: false
beetroots:
    enabled: true
    replant-when-ripe: false
cactus:
    enabled: true
worlds:
    enable-per-world: false
    enabled-worlds:
    - ExampleWorld
check-for-updates: true
debug: false
```

## Testing (local development)

A `docker-compose.yml` is provided for spinning up a local Paper 26.2 server
with the plugin mounted from `./build/libs/`. RCON is enabled for sending
commands without a Minecraft client.

1. Build the shaded plugin jar:
   ```bash
   ./gradlew shadowJar
   ```
2. Copy the built jar into the plugins directory the compose file mounts:
   ```bash
   cp build/libs/FarmAssistReboot-*.jar docker/plugins/
   ```
3. Start the server (named Docker volume `mc-data` persists the world):
   ```bash
   docker compose up -d
   ```
4. Tail the server logs to confirm startup:
   ```bash
   docker compose logs -f minecraft
   ```
5. Send commands via RCON. The `rcon` service runs under the `tools`
   profile so it doesn't start automatically — invoke it on demand:
   ```bash
   # one-shot: run a single command
   docker compose run --rm rcon mcrcon -H minecraft -p 25575 -p farmassist-dev "say hello"
   # or use the shorter form (the entrypoint passes args through)
   docker compose run --rm rcon "say hello from rcon"
   ```

Server console: `localhost:25565`. RCON: port `25575`, password `farmassist-dev`.
World data: Docker volume `mc-data` (named, not a host bind mount).

