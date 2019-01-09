package io.github.sarhatabaot.listeners;

import io.github.sarhatabaot.FarmAssist;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class onPlayerJoin implements Listener {
    private FarmAssist plugin;
    private String updateUrl ="https://github.com/sarhatabaot/FarmAssistReboot/releases";

    public onPlayerJoin(FarmAssist plugin) {
        this.plugin = plugin;
    }

    public void onJoin(PlayerJoinEvent event){
        if(event.getPlayer().hasPermission("farmassist.update")){
            event.getPlayer().sendMessage("New update found:"+plugin.getNewVersion()+" You are running:"+plugin.getDescription().getVersion());
            event.getPlayer().sendMessage(updateUrl);
        }
    }

}
