package com.github.sarhatabaot.farmassistreboot.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;

@CommandAlias("far|farmassistreboot")
public class FarmAssistRebootCommand extends BaseCommand {

    @Subcommand("version")
    public void onVersion(final CommandSender sender) {

    }
}
