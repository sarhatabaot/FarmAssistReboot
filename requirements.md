# Requirements

1. Auto replant crops.
2. Plant on till (right-click hoe with crop or set with command.)
3. Replant when ripe
4. Custom locale / Per player locale (crowdin + admin customizable)
5. 1.8 → 1.21 support (technically 1.0 → 1.7 as well)
6. Server wide toggle
   1. /far admin toggle
7. Per player toggle
   1. Players should also be able to toggle everything at once or not /far toggle
8. Placeholders:
   1. farmassist_server_toggle v
   2. farmassist_player_toggle v
   3. farmassist_player_uuid v
9. Generic admin commands:
   1. /far admin version
      1. Lists the version & contributors
   2. /far admin reload
   3. /far admin placeholders
   4. /far admin locales:
      1. Show the globally set locale, as well as per player locales if they have anything set.
      2. /far admin locales update—Will update all locales with the latest values. Preserving the values but advising the 
      user to back up just in case.
         1. Confirm with the user /far admin locales update confirm
10. Disable per crop replanting globally:
   ```yaml
  disabled-crops: ["pumpkin", "melons", "sweet_berries"]
   ```
11. Disable seeds/crop replanting based on nbt data: With pattern support (regex)
   ```yaml
   ignore-nbt: ["itemsadder-*"]
   ```
12. Update checker (modrinth): 
      * "You are running version x version x.x is latest."
      * "There is an update available on: link"
      * "You are running the latest version."
13. Permissions:
    * Everything should have permissions, commands, update notifications, crop replanting, plant-on-till + command
14. Enable / Disable per world: (perm farmassist.worlds.<name>)
   ```yaml
   enabled-worlds: ["world", "world_nether"]
   ```
15. Migration from pre 2.0
    * Migration should be done seamlessly. Differences should be explained on a wiki page. 
    Config should be automatically backed up.
    * A message should show up every startup mentioning the "You have updated to version 2.0 of FarmAssistReboot, see the changes here <link>, your old config has been backed up. To remove this message, delete your config-1-x.old"
16. Full 1.0 -> Latest support :)
17. Update documentation with all the latest features, also add a comparison between 1.x to 2.x
    It's important that we have GIFs displaying all the latest features as well.
18. Local/Remote storage for keeping user settings: (sqllite/h2/sql database)
    1. Language Manager
    2. Toggle Manager
## Upcoming
1. Custom crop support:
   1. Via json file:
   ```json
   {
      "custom_crop": {
        "crop": "",
        "seed": "",
        "properties": {
        },
        "nbt-data": {
        }
      }
   }
   ```

## Tools

* Boosted YAML
* A command framework
* NBT API For custom seeds