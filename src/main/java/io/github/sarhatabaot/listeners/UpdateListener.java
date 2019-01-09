package io.github.sarhatabaot.listeners;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author sarhatabaot
 */

public class UpdateListener implements Listener {
    private FarmAssist plugin;
    private String updateUrl ="https://github.com/sarhatabaot/FarmAssistReboot/releases";

    public UpdateListener(FarmAssist plugin) {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission("farmassist.update")){
            event.getPlayer().sendMessage("New update found:"+plugin.getNewVersion()+" You are running:"+plugin.getDescription().getVersion());
            event.getPlayer().sendMessage(updateUrl);
        }
    }

}
