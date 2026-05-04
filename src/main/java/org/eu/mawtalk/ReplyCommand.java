package org.eu.mawtalk;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;
import java.util.UUID;

public class ReplyCommand implements CommandExecutor {

    private final MawTalk plugin;

    public ReplyCommand(MawTalk plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player pSender)) {
            sender.sendMessage("Only players may use that command.");
            return true;
        }

        if (args.length < 1) {
            pSender.sendMessage(Component.text("Usage: /reply <message>", NamedTextColor.RED));
            return true;
        }

        UUID last = plugin.getLastMessager().get(pSender.getUniqueId());
        if (last == null) {
            pSender.sendMessage(Component.text("No one has messaged you recently.", NamedTextColor.RED));
            return true;
        }

        Player target = plugin.getServer().getPlayer(last);
        if (target == null || !target.isOnline()) {
            pSender.sendMessage(Component.text("Player not found or not online.", NamedTextColor.RED));
            return true;
        }

        String message = String.join(" ", args);

        Set<UUID> recipientIgnored = plugin.getIgnored().get(target.getUniqueId());
        if (recipientIgnored != null && recipientIgnored.contains(pSender.getUniqueId())) {
            pSender.sendMessage(Component.text("That player is ignoring you.", NamedTextColor.RED));
            return true;
        }

        pSender.sendMessage(
                Component.text("To ", NamedTextColor.GRAY)
                        .append(Component.text(target.getName(), NamedTextColor.WHITE))
                        .append(Component.text(": ", NamedTextColor.GRAY))
                        .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
        );
        target.sendMessage(
                Component.text("From ", NamedTextColor.GRAY)
                        .append(Component.text(pSender.getName(), NamedTextColor.WHITE))
                        .append(Component.text(": ", NamedTextColor.GRAY))
                        .append(Component.text(message, NamedTextColor.LIGHT_PURPLE))
        );

        plugin.getLastMessager().put(target.getUniqueId(), pSender.getUniqueId());
        return true;
    }
}
