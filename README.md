# FarmAssistReboot
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/15cf526f01f64e6e956b1b3996636e72)](https://www.codacy.com/app/sarhatabaot/FarmAssistReboot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=sarhatabaot/FarmAssistReboot&amp;utm_campaign=Badge_Grade)
![Java CI with Maven](https://github.com/sarhatabaot/FarmAssistReboot/workflows/Java%20CI%20with%20Maven/badge.svg)

A 1.13.2-1.16.5 version of FarmAssist. 
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
