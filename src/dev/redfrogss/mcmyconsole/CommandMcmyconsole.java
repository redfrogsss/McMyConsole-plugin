package dev.redfrogss.mcmyconsole;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandMcmyconsole implements CommandExecutor {
    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String msg = "Hello World from McMyConsole plugin.";

        sender.sendMessage(msg);

        return true;
    }
}
