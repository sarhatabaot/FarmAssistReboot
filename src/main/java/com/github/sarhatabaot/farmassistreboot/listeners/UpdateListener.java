package com.github.sarhatabaot.farmassistreboot.listeners;

import com.github.sarhatabaot.farmassistreboot.FarmAssistReboot;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author sarhatabaot
 */

public class UpdateListener implements Listener {
    private FarmAssistReboot plugin;
    private String updateUrl ="https://github.com/sarhatabaot/FarmAssistReboot/releases";

    public UpdateListener(FarmAssistReboot plugin) {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission("farmassist.update")){
            event.getPlayer().sendMessage("New update found:"+plugin.getNewVersion()+" You are running:"+plugin.getDescription().getVersion());
            event.getPlayer().sendMessage(updateUrl);
        }
    }

}
