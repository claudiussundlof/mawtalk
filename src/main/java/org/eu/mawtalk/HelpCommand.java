package org.eu.mawtalk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    private final MawTalk plugin;

    public HelpCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(Component.text("MawTalk Commands:", NamedTextColor.LIGHT_PURPLE));
        sender.sendMessage(entry("/msg <player> <message>", "Send a private message (alias /w)"));
        sender.sendMessage(entry("/reply <message>", "Reply to last private message (alias /r)"));
        sender.sendMessage(entry("/ignore <player>", "Ignore a player (private & public chat)"));
        sender.sendMessage(entry("/unignore <player>", "Stop ignoring a player"));
        sender.sendMessage(entry("/ignorelist", "List players you are ignoring"));
        sender.sendMessage(entry("/party <create|invite|accept|leave|chat>", "Party management"));
        return true;
    }

    private Component entry(String usage, String description) {
        return Component.text(usage + "  ", NamedTextColor.GRAY)
                .append(Component.text(description, NamedTextColor.WHITE));
    }
}
