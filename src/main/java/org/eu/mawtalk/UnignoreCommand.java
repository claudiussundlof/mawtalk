package org.eu.mawtalk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class UnignoreCommand implements CommandExecutor {

    private final MawTalk plugin;

    public UnignoreCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        if (args.length != 1) {
            p.sendMessage(Component.text("Usage: /unignore <player>", NamedTextColor.RED));
            return true;
        }

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            p.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        Set<UUID> set = plugin.getIgnored().get(p.getUniqueId());
        if (set == null || !set.remove(target.getUniqueId())) {
            p.sendMessage(Component.text("You are not ignoring " + target.getName(), NamedTextColor.YELLOW));
            return true;
        }

        p.sendMessage(Component.text("You have stopped ignoring " + target.getName(), NamedTextColor.GREEN));
        return true;
    }
}
