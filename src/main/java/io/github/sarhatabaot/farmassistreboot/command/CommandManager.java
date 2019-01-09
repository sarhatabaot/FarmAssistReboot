package io.github.sarhatabaot.farmassistreboot.command;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * CommandManager, based off mineresetlite
 */
public class CommandManager {
    private Map<String, Method> commands;
    private Map<Method, Object> instances;

    public CommandManager() {
        commands = new HashMap<>();
        instances = new HashMap<>();
    }

    public void register(Class<?> cls, Object obj) {
        for (Method method : cls.getMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Command command = method.getAnnotation(Command.class);

            for (String alias : command.aliases()) {
                commands.put(alias, method);
            }
            instances.put(method, obj);
        }
    }

    private void showHelpByPermission(CommandSender sender){
        List<Method> seenMethods = new LinkedList<>();
        for (Map.Entry<String, Method> entry : commands.entrySet()) {
            if (!seenMethods.contains(entry.getValue())) {
                seenMethods.add(entry.getValue());
                Command command = entry.getValue().getAnnotation(Command.class);
                //Only show help if the sender can use the command anyway
                if ((command.onlyPlayers() && !(sender instanceof Player)) || !checkPermission(command,sender)) {
                    continue;
                }
                sender.sendMessage("helpUsage"+ command.aliases()[0]+ command.usage());
                sender.sendMessage("helpDesc"+ command.description());
            }
        }
    }

    private boolean checkPermission(Command command, CommandSender sender) {
        boolean hasPermission = false;
        if(command.permissions().length == 0) {
            hasPermission = true;
        }
        for (String permission: command.permissions()){
            if(sender.hasPermission(permission)){
                hasPermission = true;
            }
        }
        return hasPermission;
    }

    public void callCommand(String cmdName, CommandSender sender, String[] args) {
        Method method = commands.get(cmdName.toLowerCase());

        if (method == null) {
            sender.sendMessage("unknownCommand");
            return;
        }
        //Get annotation
        Command command = method.getAnnotation(Command.class);

        //Validate arguments
        if (!(command.min() <= args.length && (command.max() == -1 || command.max() >= args.length))) {
            sender.sendMessage("invalidArguments");
            sender.sendMessage("/farmingassist"+ command.aliases()[0]+ command.usage());
            return;
        }

        //Player or console?
        if (command.onlyPlayers() && !(sender instanceof Player)) {
            sender.sendMessage("Not a player");
            return;
        }

        //Permission checks
        if (!checkPermission(command,sender)){
            sender.sendMessage("No permission");
            return;
        }

        //Run command
        Object[] methodArgs = {sender, args};
        try {
            method.invoke(instances.get(method), methodArgs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid methods on command!");
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof Exception) {
                sender.sendMessage("Invalid arguments");
                sender.sendMessage("/farmingassist"+ command.aliases()[0]+ command.usage());
            } else {
                e.printStackTrace();
                throw new RuntimeException("Invalid methods on command!");
            }
        }
    }
}
